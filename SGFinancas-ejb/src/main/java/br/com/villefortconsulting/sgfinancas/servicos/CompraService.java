package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TNFe.InfNFe.Det;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TNfeProc;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CompraFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CompraProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CompraRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoCompraVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.xml.bind.JAXBException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CompraService extends BasicService<Compra, CompraRepositorio, CompraFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private CentroCustoService centroCustoService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private EstoqueService estoqueService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new CompraRepositorio(em, adHocTenant);
    }

    @Override
    public Compra salvar(Compra compra) {
        compra.getListCompraProduto().stream()
                .forEach(cp -> {
                    cp.setTenatID(adHocTenant.getTenantID());
                    Double custo = cp.getDadosProduto().getIdProduto().getValorCusto();
                    if (custo == null) {
                        custo = cp.getDadosProduto().getValor();
                    }
                    if (custo != null && !cp.getDadosProduto().getValor().equals(custo)) {
                        cp.getDadosProduto().getIdProduto().setValorCusto(cp.getDadosProduto().getValor());
                        produtoService.salvar(cp.getDadosProduto().getIdProduto());
                    }
                });
        if (compra.getId() == null) {
            // Calculando juros e descontos da compra
            compra.setValorTotal(calcularValorTotal(compra));
            if (compra.getDataPedido() == null) {
                compra.setDataPedido(DataUtil.getHoje());
            }
            compra.setTenatID(adHocTenant.getTenantID());

            Conta conta = contaService.adicionarConta(preencherConta(compra), compra.getListParcelaDTO());

            compra.setIdConta(conta);
            adicionar(compra);
            adicionarProdutoEstoque(compra);
        } else {
            if (!contaService.existeParcelaPaga(compra)) {
                compra.setValorTotal(calcularValorTotal(compra));
            }

            excluiEstoque(compra);
            alterarConta(compra);
            alterar(compra);
            alteraEstoque(compra);
        }
        contaService.listarContaParcela(compra.getIdConta()).stream()
                .peek(cp -> cp.setObservacao(compra.getObservacao()))
                .forEach(contaService::salvarContaParcela);
        return compra;
    }

    public void alterarConta(Compra compra) {
        Conta conta = preencheContaAlterada(compra.getIdConta(), compra);
        List<ContaParcela> listParcela = contaService.listarContaParcela(conta);
        List<ContaParcela> parcelasPagas = listParcela.stream().filter(p -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());

        //Lança a exception, informando se poderá ou não reparcelar a conta.
        if (parcelasPagas.size() >= conta.getNumeroParcelas()) {
            throw new CadastroException("Impossível reparcelar a conta. Número de parcelas informado é menor que o número de parcelas abertas. Favor colocar o número de parcelas acima que " + parcelasPagas.size() + ".", null);
        }

        int diferenca = conta.getNumeroParcelas() - listParcela.size();
        if (diferenca >= 0) {
            preencheParcelasNovas(compra, listParcela, conta);
            conta.getContaParcelaList().clear();
            conta.getContaParcelaList().addAll(listParcela);
            contaService.alterarConta(conta);

        }
        ContaParcela contaParcela;
        if (diferenca < 0) {
            int y = 1;
            List<ContaParcela> parcelasAbertas = listParcela.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());
            for (int i = 0; i > diferenca; i--) {
                int ultimaPosicao = parcelasAbertas.size() - y;
                contaParcela = parcelasAbertas.get(ultimaPosicao);
                listParcela.remove(contaParcela);
                y++;
            }
            preencheParcelasNovas(compra, listParcela, conta);
            conta.getContaParcelaList().clear();
            conta.getContaParcelaList().addAll(listParcela);
            contaService.alterarConta(conta);
        }
    }

    public void preencheParcelasNovas(Compra compra, List<ContaParcela> listParcela, Conta conta) {
        List<ParcelaDTO> parcelaDTOs = compra.getListParcelaDTO();

        for (ParcelaDTO parcelaDTO : parcelaDTOs) {
            ContaParcela parcela = listParcela
                    .stream()
                    .filter(parc -> parc.getNumParcela().equals(parcelaDTO.getNumParcela()))
                    .findFirst().orElse(null);

            if (parcela == null) {
                parcela = new ContaParcela();
                parcela.setIdConta(conta);
                parcela.setSituacao("NP");
                parcela.setTenatID(conta.getTenatID());
                listParcela.add(parcela);
                contaService.listParcelaAlteraNumeroParcelas(listParcela);

            }

            parcela.setValor(parcelaDTO.getValor());

            parcela.setValorTotal(BigDecimal.valueOf(parcelaDTO.getValor()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            parcela.setDesconto(parcelaDTO.getDesconto());
            parcela.setJuros(parcelaDTO.getJuros());
            parcela.setMulta(parcelaDTO.getMulta());
            parcela.setEncargos(parcelaDTO.getEncargos());
            parcela.setOutrosCustos(parcelaDTO.getOutrosCustos());
            if (parcela.getIdContaBancaria() == null) {
                parcela.setIdContaBancaria(compra.getIdContaBancaria());
            }

            contaService.calcularImpostos(parcela);
            parcela.setDataVencimento(parcelaDTO.getDataVencimento());
            parcela.setIdCentroCusto(conta.getIdCentroCusto());

        }
    }

    public Conta preencheContaAlterada(Conta conta, Compra compra) {
        conta.setDataVencimento(compra.getPrazoRecebimento());
        conta.setIdFornecedor(compra.getIdFornecedor());
        conta.setIdContaBancaria(compra.getIdContaBancaria());
        conta.setIdPlanoConta(compra.getIdPlanoConta());
        conta.setNumeroParcelas(compra.getNumParcela());
        conta.setValor(compra.getValorTotal());
        conta.setValorTotal(compra.getValorTotal());
        conta.setObservacao(compra.getObservacao());
        conta.setIdCentroCusto(compra.getIdCentroCusto());

        return conta;
    }

    public ContaParcela pegaContaParcela(List<ContaParcela> listContaParcela) {
        return listContaParcela.stream()
                .filter(parcela -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(parcela.getSituacao()))
                .findAny()
                .orElseGet(ContaParcela::new);
    }

    public void alteraCompraPorConta(Compra compra, Conta conta) {
        compra.setIdFornecedor(conta.getIdFornecedor());
        compra.setDataPedido(conta.getDataVencimento());
        compra.setNumParcela(conta.getNumeroParcelas());
        compra.setObservacao(conta.getObservacao());

        alterar(compra);
    }

    //SOMENTE PARA ALTERAÇÃO DA COMPRA
    public void excluiEstoque(Compra compra) {
        //Pega a primeira posição do código do planoConta referente a compra.
        String inicioCodigo = compra.getIdPlanoConta().getCodigo().substring(0, 1);

        //Só lança o produto em estoque caso o item seja diferente das iniciais 1 e 5.
        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            //Lista os produtos de acordo com aquela compra
            List<CompraProduto> listCompra = compra.getListCompraProduto();

            // remove os produtos excluidos do estoque
            listarCompraProduto(compra.getId()).stream()
                    .filter(comproProduto -> !listCompra.contains(comproProduto))
                    .forEachOrdered(estoqueService::excluirProdutoEstoque);
        }
    }

    public void alteraEstoque(Compra compra) {
        //Pega a primeira posição do código do planoConta referente a compra.
        String inicioCodigo = compra.getIdPlanoConta().getCodigo().substring(0, 1);

        //Só lança o produto em estoque caso o item seja diferente das iniciais 1 e 5.
        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            //Manda a compra produto que está de acordo com a lista do objeto para a alteração do estoque
            compra.getListCompraProduto().forEach(compraProduto -> {
                if (compraProduto.getId() != null) {
                    estoqueService.alterarProdutoEstoque(compraProduto);
                } else {
                    estoqueService.atualizaEstoqueAlterado(compraProduto);
                }
            });
        }
    }

    private static Double calcularValorTotal(Compra compra) {
        Double valorTotal = compra.getListCompraProduto().stream()
                .map(CompraProduto::getDadosProduto)
                .mapToDouble(dp -> dp.getQuantidade() * dp.getValor())
                .sum();

        if (compra.getValorFrete() != null) {
            valorTotal += compra.getValorFrete();
        }

        return valorTotal;
    }

    private void adicionarProdutoEstoque(Compra compra) {
        //Pega a primeira posição do código do planoConta referente a compra.
        String inicioCodigo = compra.getIdPlanoConta().getCodigo().substring(0, 1);

        //Só lança o produto em estoque caso o item seja diferente das iniciais 1 e 5.
        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            //Adiciona a quantidade de produtos comprados ao estoque disponível do produto
            compra.getListCompraProduto().stream()
                    .map(compraProduto -> {
                        Produto produto = compraProduto.getDadosProduto().getIdProduto();
                        estoqueService.lancarProdutoEstoque(compraProduto, EnumOrigemEstoque.COMPRA.getOrigem());
                        produto.setEstoqueDisponivel(estoqueService.buscarSaldo(DataUtil.getHoje(), produto));
                        return produto;
                    })
                    .forEachOrdered(produtoService::alterar);
        }
    }

    private void removerProdutoEstoque(Compra compra) {
        //Remove a quantidade de produtos comprados ao estoque disponível do produto
        listarCompraProduto(compra).stream()
                .map(compraProduto -> {
                    Produto produto = compraProduto.getDadosProduto().getIdProduto();
                    estoqueService.excluirProdutoEstoque(compraProduto);
                    produto.setEstoqueDisponivel(estoqueService.buscarSaldo(DataUtil.getHoje(), produto));
                    return produto;
                })
                .forEachOrdered(produtoService::alterar);
    }

    public void atualizaTenatIDCompraProduto(CompraProduto compraProduto) {
        compraProduto.setTenatID(adHocTenant.getTenantID());
    }

    public Conta preencherConta(Compra compra) {
        //Criando uma conta a pagar
        Conta conta = new Conta();

        conta.setIdContaBancaria(compra.getIdContaBancaria());
        conta.setIdPlanoConta(compra.getIdPlanoConta());
        conta.setDataVencimento(compra.getPrazoRecebimento());
        conta.setDataEmissao(compra.getDataCompra());
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setValorTotal(compra.getValorTotal());
        conta.setValor(compra.getListCompraProduto().stream()
                .mapToDouble(cp -> cp.getDadosProduto().getQuantidade() * cp.getDadosProduto().getValor())
                .sum());
        conta.setRepetirConta("N");
        conta.setInformarPagamento("N");
        conta.setIdFornecedor(compra.getIdFornecedor());
        conta.setObservacao(compra.getObservacao());
        conta.setNumeroParcelas(compra.getNumParcela());
        conta.setTenatID(adHocTenant.getTenantID());
        conta.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
        conta.setIdCentroCusto(compra.getIdCentroCusto());
        conta.setTipoConta(EnumTipoConta.COMPRA.getTipo());
        conta.setOutrosCustos(compra.getValorFrete());

        return conta;
    }

    public CompraProduto adicionar(CompraProduto compraProduto) {
        return repositorio.adicionarCompraProduto(compraProduto);
    }

    @Override
    public Criteria getModel(CompraFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idPlanoConta", "idPlanoConta", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idFornecedor", "idFornecedor", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idContaBancaria", "idContaBancaria", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idTransportadora", "idTransportadora", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idNaturezaOperacao", "idNaturezaOperacao", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCentroCusto", "idCentroCusto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta", "idConta", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idPlanoPagamento", "idPlanoPagamento", JoinType.LEFT_OUTER_JOIN);

        if (StringUtils.isNotEmpty(filter.getAtivo())) {
            criteria.add(Restrictions.eq("situacao", filter.getAtivo().equals("S") ? "A" : "C"));
        }

        addIlikeRestrictionTo(criteria, "idPlanoConta.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addRestrictionTo(criteria, "dataCompra", filter.getData());
        addEqRestrictionTo(criteria, "idFornecedor", filter.getFornecedor());
        addEqRestrictionTo(criteria, "valorTotal", filter.getValor());
        addEqRestrictionTo(criteria, "numParcela", filter.getParcelas());

        if (filter.getProduto() != null) {
            criteria.createAlias("listCompraProduto", "compraProduto");
            criteria.createAlias("compraProduto.dadosProduto", "dadosProduto");
            addEqRestrictionTo(criteria, "dadosProduto.idProduto", filter.getProduto());
        }
        return criteria;
    }

    public CompraProduto salvarCompraProduto(CompraProduto compraProduto) {
        if (compraProduto.getId() == null) {
            return repositorio.adicionarCompraProduto(compraProduto);
        } else {
            return repositorio.alterarCompraProduto(compraProduto);
        }
    }

    public CompraProduto adicionarCompraProduto(CompraProduto compraProduto) {
        return repositorio.adicionarCompraProduto(compraProduto);
    }

    public CompraProduto alterarCompraProduto(CompraProduto compraProduto) {
        return repositorio.alterarCompraProduto(compraProduto);
    }

    public void removerCompraProduto(CompraProduto compraProduto) {
        repositorio.removerCompraProduto(compraProduto);
    }

    public CompraProduto buscarCompraProduto(int id) {
        return repositorio.buscarCompraProduto(id);
    }

    public List<CompraProduto> listarCompraProduto(Compra compra) {
        return repositorio.listarCompraProduto(compra);
    }

    public List<CompraProduto> listarCompraProduto(Integer idCompra) {
        return repositorio.listarCompraProduto(idCompra);
    }

    public int quantidadeCompraProdutoRegistrosFiltrados(CompraProdutoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getCompraProdutoModel(filtro));
    }

    public List<Compra> getListaCompraProdutoFiltrada(CompraProdutoFiltro filtro) {
        return repositorio.getListaFiltrada(getCompraProdutoModel(filtro), filtro);
    }

    public Criteria getCompraProdutoModel(CompraProdutoFiltro filter) {
        Session session = em.unwrap(Session.class);
        return session.createCriteria(CompraProduto.class);
    }

    public void cancelarCompra(Compra compra) {
        compra.setSituacao(EnumSituacaoCompraVenda.CANCELADO.getSituacao());
        compra.setDataCancelamento(DataUtil.getHoje());

        Conta conta = contaService.buscarConta(compra);
        List<ContaParcela> parcelas = contaService.listarContaParcela(conta);

        removerProdutoEstoque(compra);

        contaService.cancelarParcelas(conta, parcelas);

        alterar(compra);
    }

    private Fornecedor preencherFornecedor(TNfeProc nota) {
        Fornecedor fornecedor;
        String tipoPessoa;

        // Preenche registro de identificao de acordo com o tipo de pessoa (Fisica ou Juridica)
        String registro = nota.getNFe().getInfNFe().getEmit().getCNPJ();
        if (registro == null) {
            tipoPessoa = "PF";
            registro = nota.getNFe().getInfNFe().getEmit().getCPF();
            fornecedor = fornecedorService.buscarPorCpfCnpj(CpfCnpjUtil.mascararCpf(registro));
        } else {
            tipoPessoa = "PJ";
            fornecedor = fornecedorService.buscarPorCpfCnpj(CpfCnpjUtil.mascararCnpj(registro));
        }

        if (fornecedor == null) {
            fornecedor = new Fornecedor();
            fornecedor.getEndereco().setBairro(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getXBairro());
            fornecedor.getEndereco().setCep(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getCEP());
            fornecedor.getEndereco().setComplemento(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getXCpl());
            fornecedor.setContato(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getFone());
            fornecedor.setCpfCnpj(CpfCnpjUtil.mascararCpfOuCnpj(registro));
            fornecedor.getEndereco().setEndereco(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getXLgr());
            fornecedor.setInscricaoEstadual(nota.getNFe().getInfNFe().getEmit().getIE());
            fornecedor.setInscricaoMunicipal(nota.getNFe().getInfNFe().getEmit().getIM());
            fornecedor.setRazaoSocial(nota.getNFe().getInfNFe().getEmit().getXNome());
            fornecedor.setTipoPessoa(tipoPessoa);
            fornecedor.setUsaInscricaoEstadual(StringUtils.isEmpty(nota.getNFe().getInfNFe().getEmit().getIE()) ? "N" : "S");
            // cidade
            fornecedor.getEndereco().setIdCidade(cidadeService.buscarPeloCodigoIBGE(nota.getNFe().getInfNFe().getEmit().getEnderEmit().getXMun()));

            fornecedor = fornecedorService.adicionar(fornecedor);
        }

        return fornecedor;
    }

    private UnidadeMedida preencherUnidadeMedida(Det det) {
        String sigla = det.getProd().getUCom();
        UnidadeMedida unidadeMedida = unidadeMedidaService.buscarPorSigla(sigla);
        if (null == unidadeMedida) {
            unidadeMedida = new UnidadeMedida();
            unidadeMedida.setDescricao(sigla);
            unidadeMedida.setSigla(sigla);
            unidadeMedida = unidadeMedidaService.adicionar(unidadeMedida);
        }

        return unidadeMedida;
    }

    private Produto preencherProduto(Det det) {
        // Tenta achar o produto pelo codigo de barras
        String registro = det.getProd().getCEAN();
        Produto produto = null;

        if (StringUtils.isNotEmpty(registro)) {
            produto = produtoService.buscarPorCodigoBarras(registro);
            if (produto != null && "N".equals(produto.getAtivo())) {
                produto = null;
            }
        }

        // Tenta achar o produto pela descrição
        if (produto == null) {
            produto = produtoService.buscar(det.getProd().getXProd());
            if (produto != null && "N".equals(produto.getAtivo())) {
                produto = null;
            }
        }

        return produto;
    }

    public Compra processarXML(byte[] contents) throws JAXBException {

        String xml = new String(contents, StandardCharsets.UTF_8);
        TNfeProc nota = XmlUtil.xmlToObject(xml, TNfeProc.class);

        if (null == nota.getNFe()) {
            throw new JAXBException("Arquivo inválido!");
        }

        // preenche valores da compra
        Compra compra = new Compra();
        compra.setNumeroPedido(nota.getNFe().getInfNFe().getIde().getNNF());
        compra.setNReferencia(nota.getNFe().getInfNFe().getId().replace("NFe", ""));
        compra.setDataCompra(DataUtil.retornaDataNFeConvertida(nota.getNFe().getInfNFe().getIde().getDhEmi()));
        compra.setDataPedido(compra.getDataCompra());
        compra.setDataPedidoImportacaoNFe(compra.getDataPedido());
        compra.setPrazoRecebimento(DataUtil.retornaDataNFeConvertida(nota.getNFe().getInfNFe().getIde().getDhEmi()));
        compra.setValorFrete(Double.parseDouble(nota.getNFe().getInfNFe().getTotal().getICMSTot().getVFrete()));
        compra.setValorTotal(Double.parseDouble(nota.getNFe().getInfNFe().getTotal().getICMSTot().getVProd()));
        compra.setValorDesconto(Double.parseDouble(nota.getNFe().getInfNFe().getTotal().getICMSTot().getVDesc()));
        compra.setSituacao(EnumSituacaoCompraVenda.ATIVO.getSituacao());

        // verifica se o fornecedor existe.
        Fornecedor fornecedor = preencherFornecedor(nota);
        compra.setIdFornecedor(fornecedor);

        // preenche o centro de custo caso exista com CNPJ contido na nota.
        String cnpj = nota.getNFe().getInfNFe().getDest().getCNPJ();
        if (cnpj != null && !cnpj.isEmpty()) {
            CentroCusto centro = centroCustoService.buscaCentroCustoByCNPJ(CpfCnpjUtil.removerMascaraCnpj(cnpj));
            if (centro != null) {
                compra.setIdCentroCusto(centro);
            }
        }
        // produtos da nota
        nota.getNFe().getInfNFe().getDet().stream()
                .map(det -> {
                    // preenche a unidade de medida
                    UnidadeMedida unidadeMedida = preencherUnidadeMedida(det);
                    // Tenta achar o produto pelo codigo de barras
                    Produto produto = preencherProduto(det);
                    // preenche valores da compra do produto
                    CompraProduto compraProduto = new CompraProduto();
                    compraProduto.setTenatID(adHocTenant.getTenantID());
                    compraProduto.getDadosProduto().setValor(Double.parseDouble(det.getProd().getVUnCom()));
                    compraProduto.getDadosProduto().setQuantidade(Double.parseDouble(det.getProd().getQCom()));
                    compraProduto.setDescricaoProdutoXML(det.getProd().getXProd());
                    compraProduto.getDadosProduto().setIdProduto(produto);
                    compraProduto.setDevolvido("N");
                    // para cadastro do produto, quando for o caso
                    compraProduto.setIdUnidadeMedida(unidadeMedida);
                    compraProduto.setNcm(det.getProd().getNCM());
                    compraProduto.setCodigoBarras(det.getProd().getCEAN());
                    // verifica desconto na compra do produto
                    if (StringUtils.isNotEmpty(det.getProd().getVDesc())) {
                        compraProduto.getDadosProduto().setDesconto(Double.parseDouble(det.getProd().getVDesc()));
                    }
                    return compraProduto;
                }).forEachOrdered(compra::addProduto);
        return compra;
    }

    public Double calcularDescontoTotal(List<CompraProduto> listCompraProduto, List<ParcelaDTO> parcelasDTO) {
        return listCompraProduto.stream()
                .map(CompraProduto::getDadosProduto)
                .filter(compraProduto -> compraProduto.getDesconto() != null)
                .mapToDouble(DadosProduto::getDesconto)
                .sum();
    }

    public Double calcularValorTotal(List<CompraProduto> listCompraProduto, List<ParcelaDTO> parcelasDTO) {
        return listCompraProduto.stream()
                .map(CompraProduto::getDadosProduto)
                .filter(compraProduto -> compraProduto.getValorTotal() != null)
                .mapToDouble(DadosProduto::getValorTotal)
                .sum();
    }

    public Compra buscaCompraPorConta(Conta conta) {
        return repositorio.buscarCompraPorConta(conta);
    }

    public List<CompraProduto> comprasPorProduto(Produto produto) {
        return repositorio.listarCompraPorProduto(produto);
    }

    public boolean compraPossuiNF(Compra compra) {
        return repositorio.compraPossuiNF(compra);
    }

    public List<CompraProduto> getComprasProdutoList(Produto produtoSelecionado) {
        return repositorio.getComprasProdutoList(produtoSelecionado);
    }

}
