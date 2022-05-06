package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.ProducaoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoComposicao;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstoqueDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EstoqueFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EstoqueRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEntradaProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEstoque;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EstoqueService extends BasicService<Estoque, EstoqueRepositorio, EstoqueFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private VendaService vendaService;

    @EJB
    private CompraService compraService;

    @EJB
    private ProducaoService producaoService;

    @EJB
    private ProdutoService produtoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new EstoqueRepositorio(em, adHocTenant);
    }

    @Override
    public Estoque salvar(Estoque estoque) {
        if (estoque.getId() == null) {
            boolean isSaida = estoque.getTipoOperacao().equals("SA");
            if (isSaida) {
                estoque.setOrigem(EnumOrigemEstoque.SAIDA_LANCADA.getOrigem());
                estoque.setTotalOperacao(estoque.getQuantidade() * estoque.getIdProduto().getValorVenda());
            } else {
                estoque.setOrigem(EnumOrigemEstoque.ENTRADA_LANCADA.getOrigem());
                estoque.setTotalOperacao(estoque.getQuantidade() * estoque.getIdProduto().getValorCusto());
            }
            Double estoqueAtual = buscarSaldo(new Date(), estoque.getIdProduto());
            estoque.setSaldoAnterior(estoqueAtual);
            estoque.setSaldo(estoqueAtual + estoque.getQuantidade() * (isSaida ? -1 : 1));
        }
        return super.salvar(estoque);
    }

    public List<Estoque> listarEstoquePorProduto(Produto produto) {
        return repositorio.listarEstoquePorProduto(produto);
    }

    public void criarEstoqueInicial(Produto produto) {
        Estoque estoque = new Estoque();

        estoque.setData(produto.getDataEntrada());
        estoque.setIdProduto(produto);
        estoque.setQuantidade(produto.getEstoqueDisponivel());
        estoque.setSaldoAnterior(0d);
        estoque.setSaldo(produto.getEstoqueDisponivel());
        estoque.setTipoOperacao(EnumTipoEstoque.ENTRADA.getTipo());
        estoque.setPrimeiroCadastro("S");
        estoque.setOrigem(EnumOrigemEstoque.ESTOQUE_INICIAL.getOrigem());
        estoque.setTotalOperacao(estoque.getQuantidade() * estoque.getIdProduto().getValorCusto());

        adicionar(estoque);
    }

    public void atualizaEstoqueAlterado(Object lancamento) {
        Estoque estoque = null;

        if (lancamento instanceof VendaProduto) {
            VendaProduto vendaProduto = (VendaProduto) lancamento;
            vendaService.adicionar(vendaProduto);
            estoque = criarEstoque(vendaProduto.getIdVenda().getDataVenda(),
                    vendaProduto.getDadosProduto().getQuantidade(), EnumTipoEstoque.SAIDA.getTipo(),
                    vendaProduto.getDadosProduto().getIdProduto(), EnumOrigemEstoque.VENDA.getOrigem());
            estoque.setIdProduto(vendaProduto.getDadosProduto().getIdProduto());
            estoque.setIdVendaProduto(vendaProduto);
            estoque.setTotalOperacao(vendaProduto.getDadosProduto().getValorTotal());

        } else if (lancamento instanceof CompraProduto) {
            CompraProduto compraProduto = (CompraProduto) lancamento;
            compraService.adicionar(compraProduto);
            estoque = criarEstoque(compraProduto.getIdCompra().getDataCompra(),
                    compraProduto.getDadosProduto().getQuantidade(), EnumTipoEstoque.ENTRADA.getTipo(),
                    compraProduto.getDadosProduto().getIdProduto(), EnumOrigemEstoque.COMPRA.getOrigem());
            estoque.setIdProduto(compraProduto.getDadosProduto().getIdProduto());
            estoque.setIdCompraProduto(compraProduto);
            estoque.setTotalOperacao(compraProduto.getDadosProduto().getValorTotal());
        } else if (lancamento instanceof ProducaoProduto) {
            ProducaoProduto pp = (ProducaoProduto) lancamento;
            estoque = criarEstoque(pp.getIdProducao().getDataProducao(), pp.getQuantidade(),
                    EnumTipoEstoque.ENTRADA.getTipo(), pp.getIdProduto(),
                    EnumOrigemEstoque.COMPRA.getOrigem());
            estoque.setIdProduto(pp.getIdProduto());
            estoque.setIdProducaoProduto(pp);
        }

        adicionar(estoque);
    }

    public void lancarProdutoEstoque(Object lancamento, String origemEstoque) {
        Estoque estoque = null;
        boolean alterarEstoque = true;

        if (lancamento instanceof Produto) {
            Produto produto = (Produto) lancamento;

            if (EnumTipoEntradaProduto.SISTEMA.getTipo().equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(produto.getDataEntrada(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.ENTRADA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            } else if (EnumTipoEntradaProduto.IMPORTACAO_ENTRADA.getTipo()
                    .equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(DataUtil.getHoje(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.ENTRADA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            } else if (EnumTipoEntradaProduto.IMPORTACAO_SAIDA.getTipo().equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(DataUtil.getHoje(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.SAIDA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            }
        } else if (lancamento instanceof VendaProduto) {
            VendaProduto vendaProduto = (VendaProduto) lancamento;
            estoque = criarEstoque(vendaProduto.getIdVenda().getDataVenda(),
                    vendaProduto.getDadosProduto().getQuantidade(), EnumTipoEstoque.SAIDA.getTipo(),
                    vendaProduto.getDadosProduto().getIdProduto(), origemEstoque);
            estoque.setIdProduto(vendaProduto.getDadosProduto().getIdProduto());
            estoque.setIdVendaProduto(vendaProduto);
            estoque.setTotalOperacao(vendaProduto.getDadosProduto().getValorTotal());
        } else if (lancamento instanceof CompraProduto) {
            CompraProduto compraProduto = (CompraProduto) lancamento;
            Date dataPedido = compraProduto.getIdCompra().getDataPedidoImportacaoNFe();
            if (dataPedido == null) {
                dataPedido = compraProduto.getIdCompra().getDataCompra();
            }
            estoque = criarEstoque(dataPedido,
                    compraProduto.getDadosProduto().getQuantidade(), EnumTipoEstoque.ENTRADA.getTipo(),
                    compraProduto.getDadosProduto().getIdProduto(), origemEstoque);
            estoque.setIdProduto(compraProduto.getDadosProduto().getIdProduto());
            estoque.setIdCompraProduto(compraProduto);
            estoque.setTotalOperacao(compraProduto.getDadosProduto().getValorTotal());
        }

        if (estoque != null) {
            atualizarSaldo(estoque, estoque.getQuantidade(), alterarEstoque);
            adicionar(estoque);
        }
    }

    public Estoque lancaProdutoEstoque(Object lancamento, String origemEstoque) {
        Estoque estoque = null;
        boolean alterarEstoque = true;
        if (lancamento instanceof Produto) {
            Produto produto = (Produto) lancamento;

            if (EnumTipoEntradaProduto.SISTEMA.getTipo().equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(produto.getDataEntrada(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.ENTRADA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            } else if (EnumTipoEntradaProduto.IMPORTACAO_ENTRADA.getTipo()
                    .equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(DataUtil.getHoje(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.ENTRADA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            } else if (EnumTipoEntradaProduto.IMPORTACAO_SAIDA.getTipo().equals(produto.getUltimaEntrada())) {
                estoque = criarEstoque(DataUtil.getHoje(), produto.getEstoqueDisponivel(),
                        EnumTipoEstoque.SAIDA.getTipo(), produto, origemEstoque);
                estoque.setIdProduto(produto);
            }
        } else if (lancamento instanceof VendaProduto) {
            VendaProduto vendaProduto = (VendaProduto) lancamento;
            estoque = criarEstoque(DataUtil.getHoje(), vendaProduto.getDadosProduto().getQuantidade(),
                    EnumTipoEstoque.SAIDA.getTipo(), vendaProduto.getDadosProduto().getIdProduto(),
                    origemEstoque);
            estoque.setIdProduto(vendaProduto.getDadosProduto().getIdProduto());
            estoque.setIdVendaProduto(vendaProduto);
            estoque.setTotalOperacao(vendaProduto.getDadosProduto().getValorTotal());
        } else if (lancamento instanceof CompraProduto) {
            CompraProduto compraProduto = (CompraProduto) lancamento;
            estoque = criarEstoque(compraProduto.getIdCompra().getDataCompra(),
                    compraProduto.getDadosProduto().getQuantidade(), EnumTipoEstoque.ENTRADA.getTipo(),
                    compraProduto.getDadosProduto().getIdProduto(), origemEstoque);
            estoque.setIdProduto(compraProduto.getDadosProduto().getIdProduto());
            estoque.setIdCompraProduto(compraProduto);
            estoque.setTotalOperacao(compraProduto.getDadosProduto().getValorTotal());
        } else if (lancamento instanceof ProdutoComposicao) {
            ProdutoComposicao pc = (ProdutoComposicao) lancamento;
            estoque = criarEstoque(pc.getDataProducao(), pc.getQuantidadeFinal(),
                    EnumTipoEstoque.SAIDA.getTipo(), pc.getIdProduto(), origemEstoque);
            estoque.setIdProduto(pc.getIdProduto());
        } else if (lancamento instanceof ProducaoProduto) {
            ProducaoProduto pp = (ProducaoProduto) lancamento;
            estoque = criarEstoque(pp.getIdProducao().getDataProducao(), pp.getQuantidade(),
                    EnumTipoEstoque.ENTRADA.getTipo(), pp.getIdProduto(), origemEstoque);
            estoque.setIdProduto(pp.getIdProduto());
            estoque.setIdProducaoProduto(pp);
        }

        if (estoque != null) {
            atualizarSaldo(estoque, estoque.getQuantidade(), alterarEstoque);
            adicionar(estoque);
        }
        return estoque;
    }

    public void adicionarEstoqueImportacao(Produto produto, Double saldoInformado) {
        if (produto == null || saldoInformado == null) {
            return;
        }

        Double estoqueAtual = buscarSaldo(DataUtil.getHoje(), produto);
        if (estoqueAtual == null || saldoInformado.compareTo(estoqueAtual) == 0) {
            return;
        }
        if (estoqueAtual > saldoInformado) {
            salvarSaidaEstoque(saldoInformado, estoqueAtual, produto);
        } else if (estoqueAtual < saldoInformado) {
            salvarEntradaEstoque(estoqueAtual, saldoInformado, produto);
        }
    }

    private void salvarSaidaEstoque(Double saldoInformado, Double estoqueAtual, Produto produto) {
        Double estoqueDisponivelAtual = estoqueAtual - saldoInformado;

        Estoque estoque = new Estoque();

        preencherEstoqueImportacao(estoque, DataUtil.getHoje(), produto, estoqueDisponivelAtual, estoqueAtual, saldoInformado);
        estoque.setTipoOperacao(EnumTipoEstoque.SAIDA.getTipo());
        estoque.setOrigem(EnumOrigemEstoque.SAIDA_LANCADA.getOrigem());
        produto.setUltimaEntrada(EnumTipoEntradaProduto.IMPORTACAO_SAIDA.getTipo());
        produto.setEstoqueDisponivel(saldoInformado);
        estoque.setTotalOperacao(estoque.getQuantidade() * produto.getValorVenda());
        adicionar(estoque);
    }

    private void salvarEntradaEstoque(Double estoqueAtual, Double saldoInformado, Produto produto) {
        Double estoqueDisponivelAtual = saldoInformado - estoqueAtual;

        Estoque estoque = new Estoque();
        preencherEstoqueImportacao(estoque, DataUtil.getHoje(), produto, estoqueDisponivelAtual, estoqueAtual, saldoInformado);
        estoque.setTipoOperacao(EnumTipoEstoque.ENTRADA.getTipo());
        estoque.setOrigem(EnumOrigemEstoque.ENTRADA_LANCADA.getOrigem());
        estoque.setTotalOperacao(estoque.getQuantidade() * produto.getValorCusto());
        produto.setUltimaEntrada(EnumTipoEntradaProduto.IMPORTACAO_ENTRADA.getTipo());
        produto.setEstoqueDisponivel(saldoInformado);
        adicionar(estoque);
    }

    private void preencherEstoqueImportacao(Estoque estoque, Date data, Produto produto, Double quantidade, Double saldoAnterior, Double saldoAtual) {
        estoque.setData(data);
        estoque.setIdProduto(produto);
        estoque.setQuantidade(quantidade);
        estoque.setSaldoAnterior(saldoAnterior);
        estoque.setSaldo(saldoAtual);
        estoque.setTenatID(adHocTenant.getTenantID());
    }

    public void excluirProdutoEstoque(Object lancamento) {
        Estoque estoque = null;
        boolean alterarEstoque = false;

        if (lancamento instanceof CompraProduto) {
            CompraProduto compraProduto = (CompraProduto) lancamento;
            estoque = repositorio.buscarLancamento(compraProduto);
        } else if (lancamento instanceof VendaProduto) {
            VendaProduto vendaProduto = (VendaProduto) lancamento;
            estoque = repositorio.buscarLancamento(vendaProduto);
        } else if (lancamento instanceof Produto) {
            Produto produto = (Produto) lancamento;
            estoque = repositorio.buscarLancamento(produto);
        }

        if (estoque != null) {
            if (EnumTipoEstoque.SAIDA.getTipo().equals(estoque.getTipoOperacao())) {
                atualizarSaldo(estoque, estoque.getQuantidade(), alterarEstoque);
            } else if (EnumTipoEstoque.ENTRADA.getTipo().equals(estoque.getTipoOperacao())) {
                atualizarSaldo(estoque, estoque.getQuantidade() * -1d, alterarEstoque);
            }

            remover(estoque);
        }
    }

    public void alterarProdutoEstoque(Object lancamento) {
        Estoque estoque = null;
        boolean alterarEstoque = true;

        if (lancamento instanceof CompraProduto) {
            CompraProduto compraProduto = (CompraProduto) lancamento;
            estoque = repositorio.buscarLancamento(compraProduto);
            if (estoque != null) {
                estoque.setQuantidade(compraProduto.getDadosProduto().getQuantidade());
                estoque.setSaldo(estoque.getSaldoAnterior() + compraProduto.getDadosProduto().getQuantidade());
                estoque.setTotalOperacao(compraProduto.getDadosProduto().getValorTotal());
            }
        } else if (lancamento instanceof VendaProduto) {
            VendaProduto vendaProduto = (VendaProduto) lancamento;
            estoque = repositorio.buscarLancamento(vendaProduto);
            if (estoque != null) {
                estoque.setQuantidade(vendaProduto.getDadosProduto().getQuantidade());
                estoque.setSaldo(estoque.getSaldoAnterior() - vendaProduto.getDadosProduto().getQuantidade());
                estoque.setTotalOperacao(vendaProduto.getDadosProduto().getValorTotal());
            }
        } else if (lancamento instanceof Produto) {
            Produto produto = (Produto) lancamento;
            estoque = repositorio.buscarLancamento(produto);
            if (estoque != null) {
                estoque.setQuantidade(produto.getEstoqueDisponivel());
                estoque.setSaldo(estoque.getSaldoAnterior() + produto.getEstoqueDisponivel());
            }
        }

        if (estoque != null) {
            atualizarSaldo(estoque, estoque.getQuantidade(), alterarEstoque);
            alterar(estoque);
        }
    }

    private void atualizarSaldo(Estoque estoque, Double quantidade, boolean alteraEstoque) {
        if (alteraEstoque) {
            atualizarSaldoAlteracao(estoque);
        } else {
            List<Estoque> lancamentosPosteriores = repositorio.buscarLancamentosPosterioresData(estoque.getData(), estoque.getIdProduto(), estoque);

            for (Estoque lancamento : lancamentosPosteriores) {
                preencherSaldoAtualEAnterior(lancamento, lancamento.getSaldoAnterior() + quantidade, lancamento.getSaldo() + quantidade);
                alterar(lancamento);
            }
        }
    }

    private void atualizarSaldoAlteracao(Estoque estoque) {

        List<Estoque> lancamentosPosteriores = repositorio.buscarLancamentosPosterioresDataCrescente(estoque.getData(), estoque.getIdProduto(), estoque);

        Double saldoAnterior = estoque.getSaldo();

        for (Estoque lancamento : lancamentosPosteriores) {
            Double saldoAtual;
            if (lancamento.getOrigem().equals(EnumOrigemEstoque.ENTRADA_LANCADA.getOrigem())) {
                saldoAtual = lancamento.getSaldoAnterior() + lancamento.getQuantidade();

            } else {
                saldoAtual = saldoAnterior - lancamento.getQuantidade();

            }
            saldoAnterior = preencherSaldoAtualEAnterior(lancamento, saldoAnterior, saldoAtual);

            alterar(lancamento);
        }

    }

    public Double preencherSaldoAtualEAnterior(Estoque lancamento, Double saldoAnterior, Double saldoAtual) {
        lancamento.setSaldoAnterior(saldoAnterior);
        lancamento.setSaldo(saldoAtual);
        return saldoAtual;
    }

    private Estoque criarEstoque(Date dataMovimentacao, Double quantidade, String tipo,
            Produto produto, String origemEstoque) {
        Double saldo;

        if (produto.getId() != null) {
            saldo = buscarSaldo(new Date(), produto);
        } else {
            saldo = 0d;
        }

        Estoque estoque = new Estoque();
        estoque.setTenatID(adHocTenant.getTenantID());
        estoque.setData(dataMovimentacao);
        estoque.setIdProduto(produto);
        estoque.setQuantidade(quantidade);
        estoque.setSaldoAnterior(saldo);
        estoque.setTipoOperacao(tipo);
        estoque.setOrigem(origemEstoque);

        if (EnumTipoEstoque.ENTRADA.getTipo().equals(tipo)) {
            estoque.setSaldo(saldo + quantidade);
        } else if (EnumTipoEstoque.SAIDA.getTipo().equals(tipo)) {
            estoque.setSaldo(saldo - quantidade);
        }

        return estoque;
    }

    public Double buscarSaldo(Date dataMovimentacao, Produto produto) {

        //Preenche saldo de acordo com o produto preenchido no Estoque.
        Double saldo = repositorio.buscarSaldo(dataMovimentacao, produto);

        if (saldo != null) {
            return saldo;
        }
        return 0d;
    }

    @Override
    public Criteria getModel(EstoqueFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idCompraProduto", "idCompraProduto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idVendaProduto", "idVendaProduto", JoinType.LEFT_OUTER_JOIN);

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion c1 = Restrictions.ilike("idCompraProduto.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion c2 = Restrictions.ilike("idVendaProduto.descricao", filter.getDescricao(), MatchMode.ANYWHERE);

            criteria.add(Restrictions.disjunction(c1, c2));
        }
        addEqRestrictionTo(criteria, "tipoOperacao", filter.getTipoOperacao());
        addEqRestrictionTo(criteria, "idProduto", filter.getProduto());

        if ("S".equals(filter.getAtivo())) {
            criteria.add(Restrictions.in("tipoOperacao", "SA", "EN"));
        }

        return criteria;
    }

    public List<Estoque> listarGiroEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarGiroEstoque(produto, empresa, dataInicio, dataFim);
    }

    public List<Estoque> listarGiroEstoque(Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarGiroEstoque(empresa, dataInicio, dataFim);
    }

    public List<Estoque> listarMovimentoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarMovimentoEstoque(produto, empresa, dataInicio, dataFim);
    }

    public List<Estoque> listarMovimentoEstoque(Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarMovimentoEstoque(empresa, dataInicio, dataFim);
    }

    public List<Estoque> listarExtratoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarMovimentoEstoque(produto, empresa, dataInicio, dataFim);
    }

    public List<Estoque> listarExtratoEstoque(Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.buscarMovimentoEstoque(empresa, dataInicio, dataFim);
    }

    public List<EstoqueDTO> listarPosicaoEstoque(Produto produto, Empresa empresa) {
        List<EstoqueDTO> lista = repositorio.buscarPosicaoEstoque(produto, empresa);
        lista.forEach(item -> item.setSaldo(buscarSaldo(new Date(), produtoService.buscar(item.getIdProduto()))));
        return lista;
    }

    public Double buscarSaldoDTO(ProdutoServicoDTO idProdutoServico) {
        try {
            return buscarSaldo(new Date(), produtoService.buscar(Integer.parseInt(idProdutoServico.getIdProdutoServico().split("-")[1])));
        } catch (Exception ex) {
            return null;
        }
    }

}
