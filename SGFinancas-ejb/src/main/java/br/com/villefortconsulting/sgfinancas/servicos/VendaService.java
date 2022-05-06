package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Producao;
import br.com.villefortconsulting.sgfinancas.entidades.ProducaoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaSeguradora;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrcamentoClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrcamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoPorClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaPorVendedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaServicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.VendaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoCompraVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOS;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class VendaService extends BasicService<Venda, VendaRepositorio, VendaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ContaService contaService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private EmailService emailService;

    @EJB
    private ProducaoService producaoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new VendaRepositorio(em, adHocTenant);
    }

    private Venda cadastrarOrcamento(Venda venda) {
        if (venda.getId() != null) {
            venda.setNfList(null);
            return alterar(venda);
        }

        venda.setIdConta(null);
        venda.setListParcelaDTO(null);
        return adicionar(venda);
    }

    @Override
    public Venda salvar(Venda venda) {
        if (venda.getIdVendaSeguradora() != null) {
            venda.getIdVendaSeguradora().setTenatID(adHocTenant.getTenantID());
            venda.getIdVendaSeguradora().setIdVenda(venda);
        }
        adicionarTenatIdNasListasDaVenda(venda);

        if (isOrcamento(venda) || isOS(venda)) {
            return cadastrarOrcamento(venda);
        }

        if (Boolean.TRUE.equals(venda.getPodeVerificarEstoque())) {
            validaQuantidadeEmEstoque(venda);
        }

        Double pontosUtilizados = somarPontuacaoDaVenda(venda);
        if (venda.getId() == null) {
            venda = cadastrarNovaVenda(venda, pontosUtilizados);
        } else {
            if (!EnumOrigemVenda.APP.getOrigem().equals(venda.getOrigem())) {
                if (venda.getIdConta() == null) {
                    venda.setIdConta(contaService.buscarConta(venda));
                }
                if (venda.getIdConta() != null) {
                    venda.getIdConta().setContaParcelaList(new ArrayList<>());
                }
            }
            editarVenda(venda);
        }

        produtoService.atualizarQuantidadeEstoque(listarVendaProduto(venda.getId()));

        if (EnumTipoVenda.VENDA.getSituacao().equals(venda.getStatusNegociacao())) {
            atribuirPontos(venda, pontosUtilizados);
        }

        return venda;
    }

    public void preencheVendaFormaPagamentoList(Venda venda) {
        VendaFormaPagamento vendaFormaPagamentoSelecionada = new VendaFormaPagamento();
        vendaFormaPagamentoSelecionada.setIdVenda(venda);
        vendaFormaPagamentoSelecionada.setIdFormaPagamento(venda.getIdFormaPagamento());
        vendaFormaPagamentoSelecionada.setDesconto(venda.getValorDesconto());
        vendaFormaPagamentoSelecionada.setCondicaoPagamento(venda.getFormaPagamento());
        vendaFormaPagamentoSelecionada.setTenatID(venda.getTenatID());
        venda.getVendaFormaPagamentoList().clear();
        venda.getVendaFormaPagamentoList().add(vendaFormaPagamentoSelecionada);
    }

    public void editarVenda(Venda venda) {
        if (EnumTipoVenda.VENDA.getSituacao().equals(venda.getStatusNegociacao())) {
            removerPontosCliente(venda, venda.getPontuacao());
        }
        boolean temConta = venda.getVendaProdutoList().stream().anyMatch(VendaProduto::checkFornecidoTerceiro)
                || venda.getVendaServicoList().stream().anyMatch(VendaServico::checkFornecidoTerceiro);

        if (venda.getIdConta() == null && temConta) {
            venda.setIdConta(contaService.adicionarConta(preencherConta(venda), venda.getListParcelaDTO()));
        }

        preencheVendaFormaPagamentoList(venda);
        excluiEstoque(venda);

        if (temConta) {
            alterarConta(venda);
        }

        if (venda.isDarBaixa()) {
            venda = darBaixa(venda);
        }

        alterar(venda);
        alteraEstoque(venda);
    }

    public Venda cadastrarNovaVenda(Venda venda, Double pontosUtilizados) {
        verificarSeClientePossuiPontuacaoNecessaria(venda, pontosUtilizados);

        if (venda.getVendaProdutoList().stream().anyMatch(VendaProduto::checkFornecidoTerceiro)
                || venda.getVendaServicoList().stream().anyMatch(VendaServico::checkFornecidoTerceiro)) {
            venda.setIdConta(contaService.adicionarConta(preencherConta(venda), venda.getListParcelaDTO()));
        }
        venda = adicionar(venda);
        if (!EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao().equals(venda.getStatusNegociacao())) {
            removerProdutoEstoque(venda);
        }

        if (EnumTipoVenda.PONTUACAO.getSituacao().equals(venda.getStatusNegociacao())) {
            try {
                pagarParcelaComPontosEAlterarVenda(venda, pontosUtilizados);
            } catch (ContaException ex) {
                throw new CadastroException(ex.getMessage(), ex);
            }
        }
        if (venda.isDarBaixa()) {
            venda = darBaixa(venda);
        }
        return venda;
    }

    public void verificarSeClientePossuiPontuacaoNecessaria(Venda venda, Double pontosUtilizados) {
        boolean isPontuacaoClienteMenorQuePontuacaoNecessaria = EnumTipoVenda.PONTUACAO.getSituacao().equals(venda.getStatusNegociacao()) && venda.getIdCliente().getPontuacao() < pontosUtilizados;
        if (isPontuacaoClienteMenorQuePontuacaoNecessaria) {
            throw new CadastroException("O cliente nao possui os pontos necessários para adquirir o(s) produtos(s)/serviço(s) ", null);
        }
    }

    public static boolean isOrcamento(Venda venda) {
        return EnumTipoVenda.ORCAMENTO.getSituacao().equals(venda.getStatusNegociacao())
                || EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao().equals(venda.getStatusNegociacao())
                || EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao().equals(venda.getStatusNegociacao());
    }

    public static boolean isOS(Venda venda) {
        return EnumTipoVenda.ORDEM_SERVICO.getSituacao().equals(venda.getStatusNegociacao());
    }

    public void adicionarTenatIdNasListasDaVenda(Venda venda) {
        venda.getVendaServicoList().stream()
                .forEach(vendaServico -> vendaServico.setTenatID(adHocTenant.getTenantID()));
        venda.getVendaItemOrdemDeServicoList().stream()
                .forEach(vendaItemOrdemDeServico -> vendaItemOrdemDeServico.setTenatID(adHocTenant.getTenantID()));
        venda.getVendaFormaPagamentoList().stream()
                .forEach(vendaFormaPagamento -> vendaFormaPagamento.setTenatID(adHocTenant.getTenantID()));
    }

    public void validaQuantidadeEmEstoque(final Venda venda) {
        HashMap<Produto, Double> quantidadeProdutos = new HashMap<>();
        String validacaoProdComposto = parametroSistemaService.getParametro().getPermiteVendaSemEstoqueProdutosCompostos();
        final boolean validaQteProdNormal = parametroSistemaService.getParametro().getPermiteVendaSemEstoqueProdutosNormais().equals("N");
        final boolean validaQteProdComposto = validacaoProdComposto.equals("N");
        final boolean validaQteProd = validaQteProdNormal && validaQteProdComposto;
        venda.getVendaProdutoList().stream()
                .filter(vendaProduto -> "N".equals(vendaProduto.getFornecidoTerceiro()))
                .forEach(vendaProduto -> {
                    vendaProduto.setTenatID(adHocTenant.getTenantID());
                    if (validaQteProd) {
                        if (!quantidadeProdutos.containsKey(vendaProduto.getDadosProduto().getIdProduto())) {
                            quantidadeProdutos.put(vendaProduto.getDadosProduto().getIdProduto(), estoqueService.buscarSaldo(new Date(), vendaProduto.getDadosProduto().getIdProduto()));
                        }
                        Double qte = quantidadeProdutos.get(vendaProduto.getDadosProduto().getIdProduto());
                        qte -= vendaProduto.getDadosProduto().getQuantidade();
                        quantidadeProdutos.put(vendaProduto.getDadosProduto().getIdProduto(), qte);
                    }
                });

        List<String> erros = new ArrayList<>();
        quantidadeProdutos.forEach((prod, qte) -> {
            if (qte >= 0) {
                return;
            }
            if (EnumTipoProdutoVenda.COMPOSTO.getTipo().equals(prod.getComposto())) {
                if (validaQteProdComposto) {
                    erros.add("o produto " + prod.getDescricao() + " ficaria com " + NumeroUtil.formatarCasasDecimaisComPonto(qte, 2) + " no estoque");
                } else if (validacaoProdComposto.equals("P")) {
                    Producao producao = new Producao();
                    producao.setIdVenda(venda);
                    producao.setProducaoProdutoList(new ArrayList<>());

                    producao.getProducaoProdutoList().add(ProducaoProduto.builder()
                            .idProducao(producao)
                            .idProduto(prod)
                            .quantidade(qte)
                            .build()
                    );
                    producaoService.adicionar(producao);
                }
            } else if (validaQteProdNormal) {// Normal, insumo, kit
                erros.add("o produto " + prod.getDescricao() + " ficaria com " + NumeroUtil.formatarCasasDecimaisComPonto(qte, 2) + " no estoque");
            }
        });
        if (!erros.isEmpty() && !venda.isOrcamento() && !venda.getSituacao().equals("C")) {
            throw new CadastroException("Não será possível salvar a venda, " + StringUtil.listaParaTexto(erros) + ".", null);
        }
    }

    public Venda aprovarOrcamento(Venda venda, FormaPagamento formaPagamento) throws ContaException {
        if (venda.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao()) || venda.getOrigem().equals(EnumOrigemVenda.AGENDAMENTO.getOrigem())) {
            VendaFormaPagamento vfp = venda.getVendaFormaPagamentoList().stream()
                    .filter(fp -> fp.getIdFormaPagamento().equals(formaPagamento))
                    .collect(Collectors.toList()).get(0);
            vfp.setFormaEscolhida("S");
            vfp.setIdVenda(venda);

            venda.setDesconto(vfp.getDesconto());
            venda.setNumParcela(EnumFormaPagamento.retornaEnumSelecionado(vfp.getCondicaoPagamento()).getQuantidadeParcelas());
            venda.setCondicaoPagamento("C");// Crédito ???
            venda.setFormaPagamento(vfp.getCondicaoPagamento());// A condição está sendo salva no campo formaPagamento
            validaQuantidadeEmEstoque(venda);
        }

        if (!venda.getVendaProdutoList().isEmpty() || !venda.getVendaServicoList().isEmpty() || !venda.getVendaItemOrdemDeServicoList().isEmpty()) {
            if (EnumStatusOS.EM_EXECUCAO.getCodigo().equals(venda.getStatusOS())) {
                venda.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
                venda.setStatusOS(EnumStatusOS.FINALIZADO.getCodigo());

                salvar(venda);
            } else {
                venda.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao());
                salvar(venda);
            }
            Venda vendaNova = venda.clone();
            if (!venda.getVendaProdutoList().isEmpty() || !venda.getVendaServicoList().isEmpty()) {
                vendaNova.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
                if (venda.getOrigem().equals(EnumOrigemVenda.AGENDAMENTO.getOrigem())) {
                    vendaNova.setOrigem(EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem());
                } else {
                    vendaNova.setOrigem(EnumOrigemVenda.SITE.getOrigem());
                }
                vendaNova.setDataVenda(new Date());
                vendaNova.setValor(calcularValorTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), venda.getVendaItemOrdemDeServicoList(), new ArrayList<>()));
                vendaNova.setDesconto(calcularDescontoTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), new ArrayList<>()));
                vendaNova.setIdOrcamentoOSOrigem(venda);
                boolean removeEstoque = vendaNova.getStatusOS() == null;
                vendaNova.setStatusOS(null);
                salvarOrcamento(vendaNova, removeEstoque);
            }
            salvar(venda);
        }
        return venda;
    }

    public Venda aprovarOS(Venda venda) throws ContaException {
        if ((!venda.getVendaProdutoList().isEmpty() || !venda.getVendaServicoList().isEmpty() || !venda.getVendaItemOrdemDeServicoList().isEmpty())) {
            Venda os = venda.clone();
            os.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
            os.setStatusOS(EnumStatusOS.AGUARDANDO_EXECUCAO.getCodigo());
            os.setIdOrcamentoOSOrigem(venda);
            if (!venda.getListParcelaDTO().isEmpty()) {
                os.getVendaFormaPagamentoList().stream().forEach(fp -> fp.setIdVenda(os));
                os.setValor(calcularValorTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), venda.getVendaItemOrdemDeServicoList(), new ArrayList<>()));
                os.setDesconto(calcularDescontoTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), new ArrayList<>()));
            }
            salvarOrcamento(os, true);

            if (!venda.getVendaProdutoList().isEmpty() || !venda.getVendaServicoList().isEmpty()) {
                venda.setDataVenda(new Date());
                venda.setValor(calcularValorTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), venda.getVendaItemOrdemDeServicoList(), new ArrayList<>()));
                venda.setDesconto(calcularDescontoTotal(venda.getVendaProdutoList(), venda.getVendaServicoList(), new ArrayList<>()));
                venda.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao());
                salvar(venda);
            }

        }
        return venda;
    }

    public Boolean verificaStatusOrcamento(Venda orcamento) {
        return repositorio.verificaStatusOrcamentoVinculado(orcamento);
    }

    public Venda salvarOrcamento(Venda venda, boolean removeEstoque) throws ContaException {
        Double pontosUtilizados = somarPontuacaoDaVenda(venda);

        verificarSeClientePossuiPontuacaoNecessaria(venda, pontosUtilizados);

        venda = salvar(venda);

        if (removeEstoque && !venda.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())) {
            removerProdutoEstoque(venda);
        }
        if (venda.getStatusNegociacao().equals(EnumTipoVenda.VENDA.getSituacao())
                && venda.getIdConta() == null
                && venda.getVendaProdutoList().stream()
                        .anyMatch(VendaProduto::checkFornecidoTerceiro)) {
            venda.setIdConta(contaService.adicionarConta(preencherConta(venda), venda.getListParcelaDTO()));
        }
        venda = alterar(venda);

        // Pagar a venda com o saldo de pontuação do cliente
        if (EnumTipoVenda.PONTUACAO.getSituacao().equals(venda.getStatusNegociacao())) {
            pagarParcelaComPontosEAlterarVenda(venda, pontosUtilizados);
        }

        atribuirPontos(venda, pontosUtilizados);

        return venda;
    }

    public void pagarParcelaComPontosEAlterarVenda(Venda venda, Double pontosUtilizados) throws ContaException {
        ContaParcela parcela = venda.getIdConta().getContaParcelaList().get(0);
        parcela.setValorPago(parcela.getValor());
        parcela.setDataPagamento(DataUtil.getHoje());

        contaService.pagarParcelaIntegral(parcela);

        removerPontosCliente(venda, pontosUtilizados);

        venda.setPontosUtilizados(pontosUtilizados);
        alterar(venda);
    }

    public void removerPontosCliente(Venda venda, Double pontosUtilizados) {
        if ((pontosUtilizados != null && pontosUtilizados > 0.0) && (venda.getIdCliente().getPontuacao() != null && venda.getIdCliente().getPontuacao() > 0.0)) {
            venda.getIdCliente().setPontuacao(venda.getIdCliente().getPontuacao() - pontosUtilizados);
            clienteService.alterar(venda.getIdCliente());
        }
    }

    public void estornarPontosCliente(Venda venda) {
        if (venda.getPontosUtilizados() != null && venda.getIdCliente().getPontuacao() != null) {
            venda.getIdCliente().setPontuacao(venda.getIdCliente().getPontuacao() + venda.getPontosUtilizados());
            clienteService.alterar(venda.getIdCliente());
        }
    }

    public void atribuirPontos(Venda venda, Double pontosUtilizados) {
        if (venda.getIdCliente().getPontuacao() != null) {
            venda.getIdCliente().setPontuacao(venda.getIdCliente().getPontuacao() + pontosUtilizados);
        } else {
            venda.getIdCliente().setPontuacao(pontosUtilizados);
        }
        clienteService.alterar(venda.getIdCliente());

        venda.setPontuacao(pontosUtilizados);
        alterar(venda);
    }

    public Double somarPontuacaoDaVenda(Venda venda) {
        Double somatorioPontos = 0d;

        if (venda.getVendaProdutoList() != null) {
            somatorioPontos = venda.getVendaProdutoList().stream()
                    .filter(vendaProduto -> vendaProduto.getPontos() != null)
                    .map(VendaProduto::getPontos)
                    .reduce(somatorioPontos, (accumulator, item) -> accumulator + item);
        }

        if (venda.getVendaServicoList() != null) {
            somatorioPontos = venda.getVendaServicoList().stream()
                    .filter(vendaServico -> vendaServico.getPontos() != null)
                    .map(VendaServico::getPontos)
                    .reduce(somatorioPontos, (accumulator, item) -> accumulator + item);
        }

        return somatorioPontos;
    }

    public void alterarConta(Venda venda) {
        Conta conta = venda.getIdConta();

        conta = preencheContaAlterada(conta, venda);

        List<ContaParcela> listParcela = contaService.listarContaParcela(conta);
        List<ContaParcela> parcelasPagas = listParcela.stream().filter(p -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());

        //Lança a exception, informando se poderá ou não reparcelar a conta.
        if (parcelasPagas.size() > conta.getNumeroParcelas()) {
            throw new CadastroException("Impossível reparcelar a conta. Número de parcelas informado é menor que o número de parcelas abertas. Favor colocar o número de parcelas acima que " + parcelasPagas.size() + ".", null);
        }

        int diferenca = conta.getNumeroParcelas() - listParcela.size();
        if (diferenca >= 0) {
            preencheParcelasNovas(venda, listParcela, conta);
            conta.getContaParcelaList().clear();
            conta.getContaParcelaList().addAll(listParcela);
            contaService.alterarConta(conta);
        } else if (diferenca < 0) {
            int y = 1;
            ContaParcela contaParcela;
            List<ContaParcela> parcelasAbertas = listParcela.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());
            for (int i = 0; i > diferenca; i--) {
                int ultimaPosicao = parcelasAbertas.size() - y;
                contaParcela = parcelasAbertas.get(ultimaPosicao);
                listParcela.remove(contaParcela);
                y++;
            }
            preencheParcelasNovas(venda, listParcela, conta);
            conta.setContaParcelaList(listParcela);
            contaService.alterarConta(conta);
        }
    }

    public void preencheParcelasNovas(Venda venda, List<ContaParcela> listParcela, Conta conta) {
        for (ParcelaDTO parcelaDTO : venda.getListParcelaDTO()) {
            ContaParcela parcela = listParcela.stream()
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
                parcela.setIdContaBancaria(venda.getIdContaBancaria());
            }

            contaService.calcularImpostos(parcela);
            parcela.setDataVencimento(parcelaDTO.getDataVencimento());
            parcela.setIdCentroCusto(conta.getIdCentroCusto());
        }
    }

    public Conta preencheContaAlterada(Conta conta, Venda venda) {
        conta.setDataVencimento(venda.getDataVencimento());
        conta.setIdCliente(venda.getIdCliente());
        conta.setIdContaBancaria(venda.getIdContaBancaria());
        conta.setIdDocumento(venda.getIdDocumento());
        conta.setIdPlanoConta(venda.getIdPlanoConta());
        conta.setNumeroParcelas(venda.getNumParcela());
        conta.setValor(venda.getValor());
        conta.setValorTotal(venda.getValor());
        conta.setObservacao(venda.getObservacao());

        return conta;
    }

    public ContaParcela pegaContaParcela(List<ContaParcela> listContaParcela) {
        return listContaParcela.stream()
                .filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao()))
                .findAny().orElse(new ContaParcela());
    }

    //SOMENTE PARA ALTERAÇÃO DA VENDA
    public void excluiEstoque(Venda venda) {
        String inicioCodigo = venda.getIdPlanoConta().getCodigo().substring(0, 1);

        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            List<VendaProduto> listVenda = venda.getVendaProdutoList();

            listarVendaProduto(venda.getId()).stream()
                    .filter(vendaProduto -> !listVenda.contains(vendaProduto))
                    .forEachOrdered(estoqueService::excluirProdutoEstoque);
        }
    }

    public void alteraEstoque(Venda venda) {
        String inicioCodigo = venda.getIdPlanoConta().getCodigo().substring(0, 1);
        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            venda.getVendaProdutoList().forEach(vendaProduto -> {
                if (vendaProduto.getId() != null) {
                    estoqueService.alterarProdutoEstoque(vendaProduto);
                } else {
                    estoqueService.atualizaEstoqueAlterado(vendaProduto);
                }
            });
        }
    }

    private void removerProdutoEstoque(Venda venda) {
        String inicioCodigo = venda.getIdPlanoConta().getCodigo().substring(0, 1);

        if (!"5".equals(inicioCodigo) && !"1".equals(inicioCodigo)) {
            venda.getVendaProdutoList().stream()
                    .map(vendaProduto -> {
                        Produto produto = vendaProduto.getDadosProduto().getIdProduto();
                        vendaProduto.setIdVenda(venda);
                        alterarVendaProduto(vendaProduto);
                        Estoque estoque = estoqueService.lancaProdutoEstoque(vendaProduto, EnumOrigemEstoque.VENDA.getOrigem());
                        produto.setEstoqueDisponivel(estoque.getSaldoAnterior());
                        return produto;
                    })
                    .forEachOrdered(produtoService::alterar);
        }
    }

    private void adicionaProdutoEstoque(Venda venda) {
        listarVendaProduto(venda).stream()
                .map(vendaProduto -> {
                    Produto produto = vendaProduto.getDadosProduto().getIdProduto();
                    estoqueService.excluirProdutoEstoque(vendaProduto);
                    produto.setEstoqueDisponivel(estoqueService.buscarSaldo(DataUtil.getHoje(), produto));
                    return produto;
                })
                .forEachOrdered(produtoService::alterar);
    }

    public Conta preencherConta(Venda venda) {
        Conta conta = new Conta();

        conta.setIdContaBancaria(venda.getIdContaBancaria());
        conta.setIdPlanoConta(venda.getIdPlanoConta());
        conta.setDataVencimento(venda.getDataVencimento());
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setValor(venda.getValor());
        conta.setValorTotal(venda.getValor());
        conta.setRepetirConta("N");
        conta.setInformarPagamento("N");
        conta.setIdCliente(venda.getIdCliente());
        conta.setObservacao(venda.getObservacao());
        conta.setNumeroParcelas(venda.getNumParcela());
        conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
        conta.setTenatID(adHocTenant.getTenantID());
        conta.setIdCentroCusto(venda.getIdCentroCusto());
        conta.setTipoConta(EnumTipoConta.VENDA.getTipo());
        conta.setOutrosCustos(venda.getValorFrete());

        return conta;
    }

    public void alteraVendaPorConta(Venda venda, Conta conta) {
        venda.setIdCliente(conta.getIdCliente());
        venda.setIdContaBancaria(venda.getIdContaBancaria() != null ? venda.getIdContaBancaria() : conta.getIdContaBancaria());
        venda.setIdDocumento(conta.getIdDocumento());
        venda.setDataVencimento(conta.getDataVencimento());
        venda.setNumParcela(conta.getNumeroParcelas());
        venda.setObservacao(conta.getObservacao());

        alterar(venda);
    }

    public void atualizaTenatIdVendaProduto(VendaProduto vendaProduto) {
        vendaProduto.setTenatID(adHocTenant.getTenantID());
    }

    public void atualizaTenatIdVendaServico(VendaServico vendaServico) {
        vendaServico.setTenatID(adHocTenant.getTenantID());
    }

    public void atualizaTenatIdVendaServico(VendaItemOrdemDeServico vendaItemOrdemDeServico) {
        vendaItemOrdemDeServico.setTenatID(adHocTenant.getTenantID());
    }

    public void validarVendaProduto(VendaProduto vendaProduto) {
        if (vendaProduto == null) {
            throw new CadastroException("Informe o produto", null);
        }

        String messageError = "";
        if (vendaProduto.getDadosProduto().getQuantidade() == null || vendaProduto.getDadosProduto().getQuantidade() < 1) {
            messageError += "Informe a quantidade do produto \n";
        }
        if (vendaProduto.getDadosProduto().getValor() == null || vendaProduto.getDadosProduto().getValor() < 0.01) {
            messageError += "Informe o valor do produto \n";
        }

        if (StringUtils.isNotEmpty(messageError)) {
            throw new CadastroException(messageError, null);
        }
    }

    public boolean vendaPossuiNF(Venda venda) {
        return repositorio.vendaPossuiNF(venda);
    }

    public boolean vendaPossuiNFServico(Venda venda) {
        return repositorio.vendaPossuiNFServico(venda);
    }

    public boolean vendaPossuiProduto(Venda venda) {
        return repositorio.vendaPossuiProduto(venda);
    }

    public boolean vendaPossuiServico(Venda venda) {
        return repositorio.vendaPossuiServico(venda);
    }

    public boolean vendaPossuiOrdemDeServico(Venda venda) {
        return repositorio.vendaPossuiOrdemDeServico(venda);
    }

    public VendaProduto adicionar(VendaProduto vendaProduto) {
        return repositorio.adicionarVendaProduto(vendaProduto);
    }

    public int quantidadeRegistrosFiltradosVendas(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
        return repositorio.getQuantidadeRegistrosFiltrados(getVendaModel(filtro));
    }

    public List<Venda> getListaFiltradaVenda(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
        return repositorio.getListaFiltrada(getVendaModel(filtro), filtro);
    }

    public int quantidadeRegistrosFiltradosOS(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
        return repositorio.getQuantidadeRegistrosFiltrados(getVendaModel(filtro));
    }

    public List<Venda> getListaFiltradaOS(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
        return repositorio.getListaFiltrada(getVendaModel(filtro), filtro);
    }

    @Override
    public List<Venda> getListaFiltrada(VendaFiltro filtro) {
        return repositorio.getListaFiltrada(getVendaModel(filtro), filtro);
    }

    public List<Venda> getListaFiltradaSemTenant(VendaFiltro filter) {
        return getVendaModel(filter).list();
    }

    public int quantidadeRegistrosFiltradosOrcamento(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.ORCAMENTO.getSituacao());
        return repositorio.getQuantidadeRegistrosFiltrados(getVendaModel(filtro));
    }

    public List<Venda> getListaFiltradaOrcamento(VendaFiltro filtro) {
        filtro.setStatusNegociacao(EnumTipoVenda.ORCAMENTO.getSituacao());
        return repositorio.getListaFiltrada(getVendaModel(filtro), filtro);
    }

    public Criteria getVendaModel(VendaFiltro filtro) {
        Criteria criteria = super.getModel(filtro);

        criteria.createAlias("idDocumento", "idDocumento", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idContaBancaria", "idContaBancaria", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idPlanoConta", "idPlanoConta", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idUsuarioVendedor", "idUsuarioVendedor", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCentroCusto", "idCentroCusto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta", "idConta", JoinType.LEFT_OUTER_JOIN);

        criteria.createAlias("idConta.idCliente", "idConta.idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idFornecedor", "idConta.idFornecedor", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idPlanoConta", "idConta.idPlanoConta", JoinType.LEFT_OUTER_JOIN);

        criteria.createAlias("idConta.idContrato", "idConta.idContrato", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.contrato", "idConta.contrato", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.compra", "idConta.compra", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.venda", "idConta.venda", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idUnidadeOcupacao", "idConta.idUnidadeOcupacao", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idContaBancaria", "idConta.idContaBancaria", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idCentroCusto", "idConta.idCentroCusto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idDocumento", "idConta.idDocumento", JoinType.LEFT_OUTER_JOIN);

        criteria.add(Restrictions.ne("situacao", EnumSituacaoCompraVenda.DELETADO.getSituacao()));

        if (StringUtils.isNotEmpty(filtro.getId()) && filtro.getId().matches("P?S?O?\\d+")) {// Buscar pelo número do orçamento
            criteria.add(Restrictions.eq("id", Integer.parseInt(filtro.getId().replaceAll("\\D", ""))));
        }

        if (filtro.getUsuarioLogado() != null && filtro.getUsuarioLogado().getIdPerfil() != null
                && Boolean.TRUE.equals(filtro.getUsuarioLogado().getIdPerfil().getEhVendedor())) {
            criteria.add(Restrictions.eq("idUsuarioVendedor", filtro.getUsuarioLogado()));
        }

        if (filtro.getUsuarioLogado() != null && (filtro.getOrigem() != null && filtro.getOrigem().getValue() != null
                && filtro.getOrigem().getValue().stream().anyMatch(origem -> origem.equals(EnumOrigemVenda.AGENDAMENTO.getOrigem())))) {
            criteria.createAlias("idCliente.idUsuario", "idCliente.idUsuario");
            criteria.add(Restrictions.eq("idCliente.idUsuario", filtro.getUsuarioLogado()));
        }

        if (filtro.getTipo() == null) {
            if (StringUtils.isNotEmpty(filtro.getStatusNegociacao())) {
                if (filtro.getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO.getSituacao())) {
                    criteria.add(Restrictions.or(Restrictions.eq("statusNegociacao", filtro.getStatusNegociacao()),
                            Restrictions.eq("statusNegociacao", EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao()),
                            Restrictions.eq("statusNegociacao", EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())));
                } else {
                    criteria.add(Restrictions.eq("statusNegociacao", filtro.getStatusNegociacao()));
                }
            }
        } else {
            criteria.add(Restrictions.eq("statusNegociacao", filtro.getTipo().getSituacao()));
        }

        if (StringUtils.isNotEmpty(filtro.getAtivo())) {
            criteria.add(Restrictions.eq("situacao", filtro.getAtivo().equals("S") ? "A" : "C"));
        }

        if (filtro.getNumero() != null) {
            criteria.add(Restrictions.eq("id", Integer.parseInt(filtro.getNumero().replaceAll("\\D", ""))));
        }

        if (filtro.getIdFormaPagamento() != null) {
            criteria.createAlias("vendaFormaPagamentoList", "vendaFormaPagamentoList", JoinType.LEFT_OUTER_JOIN);
            criteria.add(Restrictions.eq("vendaFormaPagamentoList.idFormaPagamento", filtro.getIdFormaPagamento()));
        }

        if (filtro.getData() != null && filtro.getData().getMax() != null) {
            filtro.getData().setMax(DataUtil.converterStringParaDate(DataUtil.dateToString(filtro.getData().getMax(), "dd/MM/yyyy") + " 23:59:59", "dd/MM/yyyy HH:mm:ss"));
        }

        addRestrictionTo(criteria, "dataVencimento", filtro.getDataVencimento());
        addRestrictionTo(criteria, "dataVenda", filtro.getData());
        addRestrictionTo(criteria, "origem", filtro.getOrigem());
        if (filtro.getSituacao() != null && !filtro.getSituacao().getValue().isEmpty()) {
            addRestrictionTo(criteria, "situacao", filtro.getSituacao());
        }
        addEqRestrictionTo(criteria, "idCliente", filtro.getCliente());
        addEqRestrictionTo(criteria, "valor", filtro.getValor());
        addEqRestrictionTo(criteria, "numParcela", filtro.getParcelas());
        addEqRestrictionTo(criteria, "idUsuarioVendedor.idFuncionario", filtro.getFuncionario());
        addEqRestrictionTo(criteria, "tenatID", filtro.getTenantID());

        if (filtro.getServico() != null) {
            criteria.createAlias("vendaServicoList", "vendaServicoList", JoinType.LEFT_OUTER_JOIN);
            addEqRestrictionTo(criteria, "vendaServicoList.idServico", filtro.getServico());
        }

        if (filtro.getPlaca() != null && !filtro.getPlaca().trim().isEmpty() && StringUtil.validarPlaca(filtro.getPlaca())) {
            criteria.createAlias("idClienteVeiculo", "idClienteVeiculo");
            addEqRestrictionTo(criteria, "idClienteVeiculo.placa", filtro.getPlaca());
        }

        return criteria;
    }

    public VendaProduto salvarVendaProduto(VendaProduto vendaProduto) {
        if (vendaProduto.getId() == null) {
            return repositorio.adicionarVendaProduto(vendaProduto);
        }
        return repositorio.alterarVendaProduto(vendaProduto);
    }

    public VendaProduto adicionarVendaProduto(VendaProduto vendaProduto) {
        return repositorio.adicionarVendaProduto(vendaProduto);
    }

    public VendaProduto alterarVendaProduto(VendaProduto vendaProduto) {
        return repositorio.alterarVendaProduto(vendaProduto);
    }

    public void removerVendaProduto(VendaProduto vendaProduto) {
        repositorio.removerVendaProduto(vendaProduto);
    }

    public VendaProduto buscarVendaProduto(int id) {
        return repositorio.buscarVendaProduto(id);
    }

    public List<VendaProduto> listarVendaProduto(Venda venda) {
        return repositorio.listarVendaProduto(venda);
    }

    public List<VendaProduto> listarVendaNotaServico(Venda venda) {
        return repositorio.listarVendaProduto(venda);
    }

    public List<VendaProduto> listarVendaProduto(Integer idVenda) {
        return repositorio.listarVendaProduto(idVenda);
    }

    public int quantidadeVendaProdutoRegistrosFiltrados(VendaProdutoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getVendaProdutoModel(filtro));
    }

    public List<VendaProduto> getListaVendaProdutoFiltrada(VendaProdutoFiltro filtro) {
        return repositorio.getListaFiltrada(getVendaProdutoModel(filtro), filtro);
    }

    public Criteria getVendaProdutoModel(VendaProdutoFiltro filtro) {
        Session session = em.unwrap(Session.class);
        return session.createCriteria(VendaProduto.class);
    }

    public VendaServico salvarVendaServico(VendaServico vendaServico) {
        if (vendaServico.getId() == null) {
            return repositorio.adicionarVendaServico(vendaServico);
        } else {
            return repositorio.alterarVendaServico(vendaServico);
        }
    }

    public VendaServico adicionarVendaServico(VendaServico vendaServico) {
        return repositorio.adicionarVendaServico(vendaServico);
    }

    public VendaServico alterarVendaServico(VendaServico vendaServico) {
        return repositorio.alterarVendaServico(vendaServico);
    }

    public void removerVendaServico(VendaServico vendaServico) {
        repositorio.removerVendaServico(vendaServico);
    }

    public VendaServico buscarVendaServico(int id) {
        return repositorio.buscarVendaServico(id);
    }

    public List<VendaServico> listarVendaServico(Venda venda) {
        return repositorio.listarVendaServico(venda);
    }

    public List<VendaServico> listarVendaServico(Integer idVenda) {
        return repositorio.listarVendaServico(idVenda);
    }

    public int quantidadeVendaServicoRegistrosFiltrados(VendaServicoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getVendaServicoModel(filtro));
    }

    public List<VendaServico> getListaVendaServicoFiltrada(VendaServicoFiltro filtro) {
        return repositorio.getListaFiltrada(getVendaServicoModel(filtro), filtro);
    }

    public Criteria getVendaServicoModel(VendaServicoFiltro filtro) {
        Session session = em.unwrap(Session.class);
        return session.createCriteria(VendaServico.class);
    }

    public void rejeitaOrcamento(Venda orcamento) {
        orcamento.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao());
        orcamento.setDataCancelamento(DataUtil.getHoje());

        alterar(orcamento);
    }

    public void cancelaOS(Venda os) {
        os.setStatusOS(EnumStatusOS.CANCELADA.getCodigo());
        os.setDataCancelamento(DataUtil.getHoje());
        os.setMotivoCancelamentoOS("Venda cancelada.");

        alterar(os);

        if (os.getIdOrcamentoOSOrigem() != null && os.getIdOrcamentoOSOrigem().getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())) {
            rejeitaOrcamento(os.getIdOrcamentoOSOrigem());
        }
    }

    public void cancelarVenda(Venda venda) {
        venda.setSituacao(EnumSituacaoCompraVenda.CANCELADO.getSituacao());
        venda.setDataCancelamento(DataUtil.getHoje());

        if (venda.getIdOrcamentoOSOrigem() != null && venda.getIdOrcamentoOSOrigem().getStatusNegociacao().equals(EnumTipoVenda.ORDEM_SERVICO.getSituacao())) {
            cancelaOS(venda.getIdOrcamentoOSOrigem());
        } else if (venda.getIdOrcamentoOSOrigem() != null && venda.getIdOrcamentoOSOrigem().getStatusNegociacao().equals(EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao())) {
            rejeitaOrcamento(venda.getIdOrcamentoOSOrigem());
        }

        Conta contaReceber = contaService.buscarConta(venda);

        if (contaReceber != null) {
            List<ContaParcela> parcelas = contaService.listarContaParcela(contaReceber);
            adicionaProdutoEstoque(venda);
            contaService.cancelarParcelas(contaReceber, parcelas);
        }

        if (EnumTipoVenda.VENDA.getSituacao().equals(venda.getStatusNegociacao())) {
            removerPontosCliente(venda, venda.getPontuacao());
            venda.setPontuacao(null);
        }

        if (EnumTipoVenda.PONTUACAO.getSituacao().equals(venda.getStatusNegociacao())) {
            estornarPontosCliente(venda);
            venda.setPontosUtilizados(null);
        }

        alterar(venda);
    }

    public List<ValorLancamentoDTO> obterValorPorProduto(Date dataInicio, Date dataFim, String tipoProduto) {
        return repositorio.obterValorPorProduto(dataInicio, dataFim, tipoProduto);
    }

    public List<ValorLancamentoDTO> obterValorPorServico(Date dataInicio, Date dataFim) {
        return repositorio.obterValorPorServico(dataInicio, dataFim);
    }

    public Double calcularDescontoTotal(List<VendaProduto> listVendaProduto, List<VendaServico> listVendaServico, List<ParcelaDTO> parcelasDTO) {
        if (parcelasDTO != null && !parcelasDTO.isEmpty()) {// Soma os descontos de todas parcelas
            return parcelasDTO.stream()
                    .filter(parcelaDTO -> parcelaDTO.getDesconto() != null)
                    .mapToDouble(ParcelaDTO::getDesconto)
                    .sum();
        }
        Double valorDescontoProduto = listVendaProduto.stream()
                .filter(vendaProduto -> vendaProduto.getDadosProduto().getDesconto() != null)
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getDesconto)
                .sum();

        Double valorDescontoServico = listVendaServico.stream()
                .filter(vendaProduto -> vendaProduto.getDesconto() != null)
                .mapToDouble(VendaServico::getDesconto)
                .sum();

        return NumeroUtil.somar(valorDescontoProduto, valorDescontoServico);
    }

    public Double calcularDescontoTotal(List<VendaProduto> listVendaProduto, List<VendaServico> listVendaServico, List<VendaItemOrdemDeServico> listVendaOS, List<ParcelaDTO> parcelasDTO) {
        Double valorDescontoProduto = listVendaProduto.stream()
                .filter(vendaProduto -> vendaProduto.getDadosProduto().getDesconto() != null)
                .mapToDouble(vendaProduto -> vendaProduto.getDadosProduto().getDesconto() * vendaProduto.getDadosProduto().getQuantidade())
                .sum();

        Double valorDescontoServico = listVendaServico.stream()
                .filter(vendaServico -> vendaServico.getDesconto() != null)
                .mapToDouble(VendaServico::getDesconto)
                .sum();

        Double valorDescontoOS = listVendaOS.stream()
                .filter(vendaOS -> vendaOS.getDesconto() != null)
                .mapToDouble(VendaItemOrdemDeServico::getDesconto)
                .sum();

        return NumeroUtil.somar(valorDescontoProduto, valorDescontoServico, valorDescontoOS);
    }

    public Double calcularTotalDesconto(List<ItemVendaDTO> itensVendaDtos, List<VendaItemOrdemDeServico> listVendaOS, List<ParcelaDTO> parcelasDTO) {
        Double valorDescontoProduto = itensVendaDtos.stream()
                .filter(vendaProduto -> vendaProduto.getDesconto() != null)
                .mapToDouble(ItemVendaDTO::getDesconto)
                .sum();

        Double valorDescontoOS = listVendaOS.stream()
                .filter(vendaOS -> vendaOS.getDesconto() != null)
                .mapToDouble(VendaItemOrdemDeServico::getDesconto)
                .sum();

        return NumeroUtil.somar(valorDescontoProduto, valorDescontoOS);
    }

    public void calcularPontos(List<VendaProduto> listVendaProduto, List<VendaServico> listVendaServico) {
        listVendaProduto.stream()
                .filter(vendaProduto -> vendaProduto.getDadosProduto().getQuantidade() != null && vendaProduto.getDadosProduto().getIdProduto() != null && vendaProduto.getDadosProduto().getIdProduto().getPontos() != null)
                .forEachOrdered(vendaProduto -> vendaProduto.setPontos(vendaProduto.getDadosProduto().getQuantidade() * vendaProduto.getDadosProduto().getIdProduto().getPontos()));

        listVendaServico.stream()
                .filter(vendaServico -> vendaServico.getIdServico() != null && vendaServico.getIdServico().getPontos() != null)
                .forEachOrdered(vendaServico -> vendaServico.setPontos(vendaServico.getIdServico().getPontos()));
    }

    public Double calcularValorTotal(List<VendaProduto> listVendaProduto, List<VendaServico> listVendaServico, List<ParcelaDTO> parcelasDTO) {
        if (parcelasDTO != null && !parcelasDTO.isEmpty()) {
            return parcelasDTO.stream()
                    .filter(parcelaDTO -> parcelaDTO.getValor() != null)
                    .mapToDouble(ParcelaDTO::getValor)
                    .sum(); // Soma valores dos produtos (quantidade x valor unitario)
        }
        Double valorTotalProduto = listVendaProduto.stream()
                .filter(vendaProduto -> vendaProduto.getDadosProduto().getValorTotal() != null)
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getValorTotal)
                .sum();

        Double valorTotalServico = listVendaServico.stream()
                .filter(vendaServico -> vendaServico.getValorVenda() != null)
                .mapToDouble(VendaServico::getValorVenda)
                .sum();

        Double valorTotalOS = listVendaServico.stream()
                .filter(vendaServico -> vendaServico.getValorVenda() != null)
                .mapToDouble(VendaServico::getValorVenda)
                .sum();

        return NumeroUtil.somar(valorTotalProduto, valorTotalServico, valorTotalOS);
    }

    public Double calcularTotais(List<ItemVendaDTO> itemVendaDTOs, List<VendaItemOrdemDeServico> listVendaOS, List<ParcelaDTO> parcelasDTO) {
        Double valorTotal = itemVendaDTOs.stream()
                .filter(itemVendaDTO -> itemVendaDTO.getValor() != null)
                .mapToDouble(itemVendaDTO -> itemVendaDTO.getValor() * itemVendaDTO.getQuantidade())
                .sum();

        Double valorTotalOS = listVendaOS.stream()
                .filter(vendaServico -> vendaServico.getValor() != null)
                .mapToDouble(VendaItemOrdemDeServico::getValor)
                .sum();

        return NumeroUtil.somar(valorTotal, valorTotalOS);
    }

    public Double calcularValorTotal(List<VendaProduto> listVendaProduto, List<VendaServico> listVendaServico, List<VendaItemOrdemDeServico> listVendaOS, List<ParcelaDTO> parcelasDTO) {
        if (parcelasDTO != null && !parcelasDTO.isEmpty()) {
            return parcelasDTO.stream()
                    .filter(parcelaDTO -> parcelaDTO.getValor() != null)
                    .mapToDouble(ParcelaDTO::getValor)
                    .sum();
        }
        Double valorTotalProduto = listVendaProduto.stream()
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getValorTotal)
                .sum();

        Double valorTotalServico = listVendaServico.stream()
                .mapToDouble(VendaServico::getValorTotal)
                .sum();

        Double valorTotalOS = listVendaOS.stream()
                .mapToDouble(VendaItemOrdemDeServico::getValorTotal)
                .sum();

        return NumeroUtil.somar(valorTotalProduto, valorTotalServico, valorTotalOS);
    }

    public List<VendaPorVendedorDTO> obterVendaPorVendedor(Empresa empresa, Usuario usuario, Date dataInicio, Date dataFim) {
        return repositorio.listarVendaProdutoPorVendedor(usuario, empresa, dataInicio, dataFim);
    }

    public List<VendaPorVendedorDTO> obterVendaPorCliente(Empresa empresa, Cliente cliente, Date dataInicio, Date dataFim) {
        return repositorio.listarVendaProdutoPorCliente(cliente, empresa, dataInicio, dataFim);
    }

    public List<ServicoPorClienteDTO> obterServicoPorCliente(Empresa empresa, Cliente cliente, Date dataInicio, Date dataFim) {
        return repositorio.listarVendaServicoPorCliente(cliente, empresa, dataInicio, dataFim);
    }

    public List<Usuario> listarUsuario(Empresa empresa) {
        return repositorio.listarUsuario(empresa);
    }

    public List<Cliente> listaCliente(Empresa empresa) {
        return repositorio.listarCliente(empresa);
    }

    public List<Cliente> listaClienteServico(Empresa empresa) {
        return repositorio.listarClientePorServico(empresa);
    }

    public List<ProdutosMaisVendidosDTO> listarProdutosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.listarProdutosMaisVendidos(empresa, dataInicio, dataFim);
    }

    public List<ServicosMaisVendidosDTO> listarServicosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFim) {
        return repositorio.listarServicosMaisVendidos(empresa, dataInicio, dataFim);
    }

    public List<VendaProduto> listarVendaProdutoParaEmissaoNF(Venda venda) {
        return repositorio.listarVendaProduto(venda);
    }

    public Venda getVendaPorConta(Conta conta) {
        return repositorio.vendaPorConta(conta);
    }

    public List<VendaProduto> vendasPorProduto(Produto produto) {
        return repositorio.listarVendaPorProduto(produto);
    }

    public List<OrcamentoDTO> listaItensOrcamento(Venda orcamento) {
        List<OrcamentoDTO> itens = new ArrayList<>();

        listarVendaProduto(orcamento).forEach(item -> itens.add(new OrcamentoDTO(item)));
        listarVendaServico(orcamento).forEach(item -> itens.add(new OrcamentoDTO(item)));
        listarVendaItensOrdemDeServico(orcamento).forEach(item -> itens.add(new OrcamentoDTO(item)));

        return itens;
    }

    public List<OrcamentoClienteDTO> listaItensOrcamentoCliente(Venda orcamento) {
        List<OrcamentoClienteDTO> itens = new ArrayList<>();
        itens.add(new OrcamentoClienteDTO("Produtos"));
        listarVendaProduto(orcamento).forEach(item -> itens.add(new OrcamentoClienteDTO(item)));
        itens.add(new OrcamentoClienteDTO("Serviços"));
        listarVendaServico(orcamento).forEach(item -> itens.add(new OrcamentoClienteDTO(item)));
        listarVendaItensOrdemDeServico(orcamento).forEach(item -> itens.add(new OrcamentoClienteDTO(item)));

        return itens;
    }

    public List<VendaItemOrdemDeServico> listarVendaItensOrdemDeServico(Venda idVenda) {
        return repositorio.listarVendaItensOrdemDeServico(idVenda);
    }

    public List<VendaItemOrdemDeServico> listarVendaItensOrdemDeServico(Integer idVenda) {
        return repositorio.listarVendaItensOrdemDeServico(idVenda);
    }

    public VendaItemOrdemDeServico alterarVendaItensOrdemDeServico(VendaItemOrdemDeServico vendaServico) {
        return repositorio.alterarVendaItensOrdemDeServico(vendaServico);
    }

    public void removerVendaItensOrdemDeServico(VendaItemOrdemDeServico vendaServico) {
        repositorio.removerVendaItensOrdemDeServico(vendaServico);
    }

    public VendaItemOrdemDeServico buscarVendaItensOrdemDeServico(int id) {
        return repositorio.buscarVendaItensOrdemDeServico(id);
    }

    public List<VendaFormaPagamento> listarVendaFormaPagamento(Venda vendaSelecionada) {
        return repositorio.listarVendaFormaPagamento(vendaSelecionada);
    }

    public List<VendaFormaPagamento> listarVendaFormaPagamento(Integer vendaSelecionada) {
        return repositorio.listarVendaFormaPagamento(vendaSelecionada);
    }

    public List<String> getFormasDePagamentoAssociadas(Venda orcamento) {
        return repositorio.getFormasDePagamentoAssociadas(orcamento);
    }

    public void enviarOrcamentoPorEmail(Venda orcamento, FormaPagamento formaDePagamento, String numero) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        if (StringUtils.isEmpty(destinatario.getEmail())) {
            throw new EmailException("Cliente não possui endereço de e-mail cadastrado para envio", null);
        }

        emailDTO.setAssunto("Orçamento " + numero);
        emailDTO.setMensagem("Ola " + orcamento.getIdCliente().getNome() + " segue em anexo o orçamento nº " + numero + " no valor de R$"
                + NumeroUtil.converterDecimalParaString(orcamento.getValor(), 2) + ". <br/><br/>" + "Att. <br/>" + empresaService.getEmpresa().getNomeFantasia());
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoOrcamento(orcamento, formaDePagamento), "Orcamento.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public void enviarOrcamentoVeiculoPorEmail(Venda orcamento, FormaPagamento formaDePagamento, String numero) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        if (StringUtils.isEmpty(destinatario.getEmail())) {
            throw new EmailException("Cliente não possui endereço de e-mail cadastrado para envio", null);
        }

        emailDTO.setAssunto("Orçamento " + numero);
        emailDTO.setMensagem("Ola " + orcamento.getIdCliente().getNome() + " segue em anexo o orçamento nº " + numero + " no valor de R$"
                + NumeroUtil.converterDecimalParaString(orcamento.getValor(), 2) + ". <br/><br/>" + "Att. <br/>" + empresaService.getEmpresa().getNomeFantasia());
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoOrcamentoParaCliente(orcamento, formaDePagamento), "Orcamento.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public void enviarOSPorEmail(Venda orcamento, FormaPagamento formaDePagamento, String numero) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        emailDTO.setAssunto("Ordem de serviço " + numero);
        emailDTO.setMensagem("Em anexo o ordem de serviço " + numero + " no valor de "
                + NumeroUtil.converterDecimalParaString(orcamento.getValor(), 2));
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoOS(orcamento), "Ordem de seviço.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public void enviarOSMecanicaPorEmail(Venda orcamento, FormaPagamento formaDePagamento, String numero) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        emailDTO.setAssunto("Ordem de serviço " + numero);
        emailDTO.setMensagem("Em anexo o ordem de serviço " + numero + " no valor de "
                + NumeroUtil.converterDecimalParaString(orcamento.getValor(), 2));
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoOSVeiculo(orcamento), "Ordem de Serviço.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public void enviarGarantiaPorEmail(Venda orcamento) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        emailDTO.setAssunto("Termo de garantia " + orcamento.getIdClienteVeiculo().getNome());
        emailDTO.setMensagem("Em anexo o termo de garantia do veiculo " + orcamento.getIdClienteVeiculo().getNome());
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoGarantia(orcamento), "Termo de Gaantia.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public void enviarDeclaracaoPermanenciaPorEmail(Venda orcamento) throws EmailException, JRException, RelatorioException, IOException {
        Usuario destinatario = new Usuario();
        EmailDTO emailDTO = new EmailDTO();

        destinatario.setNome(orcamento.getIdCliente().getNome());
        destinatario.setEmail(orcamento.getIdCliente().getEmail());

        emailDTO.setAssunto("Declaração de permanencia do veiculo " + orcamento.getIdClienteVeiculo().getNome());
        emailDTO.setMensagem("Em anexo a declaração de permanência do veiculo " + orcamento.getIdClienteVeiculo().getNome());
        emailDTO.setTituloMensagem(emailDTO.getAssunto());

        emailDTO.getAnexos().add(FileUtil.convertByteToFile(relatorioService.gerarImpressaoPermanencia(orcamento), "Declaração de Permanência.pdf"));
        emailDTO.getDestinatarios().add(destinatario);

        emailService.enviarEmailMS(emailDTO, "N");
    }

    public Venda duplicar(Venda venda) {
        venda.setVendaProdutoList(listarVendaProduto(venda));
        venda.setVendaServicoList(listarVendaServico(venda));
        venda.setVendaItemOrdemDeServicoList(listarVendaItensOrdemDeServico(venda));
        venda.setVendaFormaPagamentoList(listarVendaFormaPagamento(venda));
        Venda duplicada = venda.clone();
        return salvar(duplicada);
    }

    public List<ItemVendaDTO> itensVenda(Venda venda) {
        List<ItemVendaDTO> listaItens = new ArrayList<>();

        listaItens.addAll(listarVendaProduto(venda).stream()
                .map(prod -> converterItemVendaDTO(prod, null))
                .collect(Collectors.toList()));

        listaItens.addAll(listarVendaServico(venda).stream()
                .map(serv -> converterItemVendaDTO(null, serv))
                .collect(Collectors.toList()));

        return listaItens;
    }

    public ItemVendaDTO converterItemVendaDTO(VendaProduto produto, VendaServico servico) {
        ItemVendaDTO item = new ItemVendaDTO();

        ProdutoServicoDTO produtoServico = new ProdutoServicoDTO();

        if (null != produto) {
            produtoServico = new ProdutoServicoDTO(produto.getId() + "", produto.getDadosProduto().getIdProduto().getDescricao(),
                    NumeroUtil.somar(0d, produto.getDadosProduto().getIdProduto().getValorVenda()), EnumTipoItemVenda.PRODUTO, produto.getDadosProduto().getIdProduto().getComposto(), "");
            item.setQuantidade(produto.getDadosProduto().getQuantidade());
        }

        if (null != servico) {
            produtoServico = new ProdutoServicoDTO(servico.getId() + "", servico.getIdServico().getDescricao(),
                    NumeroUtil.somar(0d, servico.getIdServico().getValorVenda()), EnumTipoItemVenda.SERVICO, servico.getIdServico().getAtivo(), "");
            item.setQuantidade(1d);
        }

        item.setIdProdutoServico(produtoServico);

        return item;
    }

    public Venda darBaixa(Venda venda) {
        venda.setValorPago(venda.getValor());
        Double valorRestante = venda.getValorPago();
        for (ContaParcela parcela : venda.getIdConta().getContaParcelaList()) {
            valorRestante -= parcela.getValorTotal() - parcela.getDesconto();
            if (valorRestante >= 0) {
                Conta conta = venda.getIdConta();
                conta.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
                parcela.setValorPago(parcela.getValorTotal());
                parcela.setIdFormaPagamento(venda.getIdFormaPagamento());
                if (parcela.getDataPagamento() == null) {
                    parcela.setDataPagamento(DataUtil.getHoje());
                }
                parcela.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
            } else {
                parcela.setValorPago(parcela.getValorTotal() + valorRestante);
                parcela.setIdFormaPagamento(venda.getIdFormaPagamento());
                if (parcela.getValorPago() >= 0.01d) {// Validação por causa do bug de precisão de floating point
                    if (parcela.getDataPagamento() == null) {
                        parcela.setDataPagamento(DataUtil.getHoje());
                    }
                    parcela.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
                }
                break;
            }
        }
        return alterar(venda);
    }

    public VendaSeguradora buscarVendaSeguradora(Integer id) {
        return repositorio.buscaVendaSeguradoraID(id);
    }

    public Boolean podeReabrirOS(Venda os) {
        if (os.getIdOrcamentoOSOrigem() != null) {
            return !"C".equals(os.getIdOrcamentoOSOrigem().getSituacao()) && "AP".equals(os.getIdOrcamentoOSOrigem().getStatusNegociacao());
        }
        return os.getStatusOS() != null && os.getStatusOS().equals(EnumStatusOS.CANCELADA.getCodigo());
    }

    public boolean temVendaPorIdOrcamentoOsOrigem(Venda venda) {
        return repositorio.temVendaPorIdOrcamentoOsOrigem(venda);
    }

}
