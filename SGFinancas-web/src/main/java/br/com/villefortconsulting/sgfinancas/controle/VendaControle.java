package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.controle.apoio.ControleMenu;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.EstoqueService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.TransacaoGetnetService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeMedidaService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoCompraVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoComposicaoProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.springframework.web.client.RestClientException;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VendaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VendaService vendaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ServicoService servicoService;

    @EJB
    private ContaService contaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    private TransacaoGetnetService transacaoService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @Inject
    private ControleMenu controleMenu;

    private Venda vendaSelecionada;

    private Conta contaSelecionada;

    private Produto produtoSelecionado;

    private Servico servicoSelecionado;

    private VendaProduto vendaProdutoSelecionado;

    private VendaServico vendaServicoSelecionado;

    private List<VendaProduto> listVendaProduto = new LinkedList<>();

    private List<VendaItemOrdemDeServico> listVendaOS = new LinkedList<>();

    private List<VendaServico> listVendaServico = new LinkedList<>();

    private int somaListas;

    private String codigoDeBarras;

    private FormaPagamento formaPagamentoSelecionado;

    private LazyDataModel<Venda> model;

    private VendaFiltro filtro = new VendaFiltro();

    private ItemVendaDTO itemVendaSelecionado;

    private List<ProdutoServicoDTO> itensVendaDisponiveis;

    private List<ItemVendaDTO> itensVendaSelecionados;

    private String controleEdicao;

    private Double estoqueProdutoSelecinado;

    protected boolean adicionaProduto = false;

    protected boolean ehProduto = true;

    protected String addDescricao;

    protected UnidadeMedida addUnidade;

    protected Produto addProduto;

    protected Servico addServico;

    protected Double precoCusto;

    protected Double precoVenda;

    protected Double estoqueInicial;

    private Double valorDesconto;

    private Boolean sobreescreverDesconto;

    private String tipoDesconto;

    private boolean linkPagamento = false;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                vendaService::quantidadeRegistrosFiltradosVendas,
                vendaService::getListaFiltradaVenda,
                filter -> filter.setUsuarioLogado(getUsuarioLogado()));
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public boolean vendaPossuiItensNF(Venda venda) {
        return vendaService.listarVendaProduto(venda).stream().anyMatch(vp -> "N".equals(vp.getFornecidoTerceiro()));
    }

    public boolean vendaPossuiNF(Venda venda) {
        if (venda != null) {
            return vendaService.vendaPossuiNF(venda);
        }
        return false;
    }

    public boolean vendaPossuiNFServico(Venda venda) {
        return vendaService.vendaPossuiNFServico(venda);
    }

    public String buscarDescricaoTipoVenda(String situacao) {
        return EnumTipoVenda.getDescricao(situacao);
    }

    public void cleanProdutosServicos() {
        listVendaProduto = new LinkedList<>();
        listVendaServico = new LinkedList<>();
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_COMPRA.getChave()).getDescricao());
        return "cadastroBanco.xhtml";
    }

    public String mostrarAjudaPDV() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_COMPRA_PDV.getChave()).getDescricao());
        return "pdv.xhtml";
    }

    @Override
    public void cleanCache() {
        this.vendaSelecionada = new Venda();
        this.listVendaProduto = new LinkedList<>();
        this.listVendaServico = new LinkedList<>();
        estoqueProdutoSelecinado = 0d;
        controleEdicao = null;
        linkPagamento = false;
        adicionaProduto = false;
        cleanProduto();
        cleanServico();
    }

    public void cleanFormaPagamento() {
        this.vendaSelecionada.setListParcelaDTO(null);
        this.vendaSelecionada.setFormaPagamento(null);
    }

    public List<Venda> getVendas() {
        return vendaService.listar();
    }

    public EnumFormaPagamento[] getFormasPagamento() {

        return EnumFormaPagamento.values();
    }

    public boolean formaDePagamentoDisponiveis(EnumFormaPagamento enumFormaPagamento) {
        long parcelasPagas = vendaSelecionada.getListParcelaDTO()
                .stream()
                .filter(parcela -> parcela.getValorPago() != null && parcela.getValorPago() > 0d).count();
        return parcelasPagas >= enumFormaPagamento.getQuantidadeParcelas();
    }

    public void initItensVendaDTO() {
        itensVendaDisponiveis = produtoService.listarParaVenda().stream()
                .map(ProdutoServicoDTO::new)
                .collect(Collectors.toList());
        itensVendaDisponiveis.addAll(servicoService.listarServicosAtivos().stream()
                .map(ProdutoServicoDTO::new)
                .collect(Collectors.toList()));

        itemVendaSelecionado = new ItemVendaDTO();
        itensVendaSelecionados = new ArrayList<>();
    }

    public String doStartAdd() {
        try {
            initItensVendaDTO();
            cleanCache();
            vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
            vendaSelecionada.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
            vendaSelecionada.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
            return "cadastroVenda.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "listaVenda.xhtml";
        }
    }

    public String addOrcamento() {
        cleanCache();
        vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
        vendaSelecionada.setTipoItemVenda(EnumTipoVenda.ORCAMENTO.getDescricao());
        return "cadastroOrcamento.xhtml";
    }

    public String doStartAlterar() {
        initItensVendaDTO();
        controleEdicao = null;
        adicionaProduto = false;

        itensVendaSelecionados = vendaService.listarVendaProduto(vendaSelecionada).stream()
                .map((VendaProduto vp) -> {
                    ProdutoServicoDTO prodDTO = itensVendaDisponiveis.stream()
                            .filter(produto -> produto.getIdProdutoServico().substring(2).equals(vp.getDadosProduto().getIdProduto().getId() + ""))
                            .findAny().orElse(null);
                    if (prodDTO == null) {
                        return null;
                    }
                    return new ItemVendaDTO(vp.getId(), prodDTO, vp.getDadosProduto().getValor(), vp.getDadosProduto().getQuantidade(), vp.getDadosProduto().getDesconto(), vp.getDadosProduto().getDetalhesItem(), vp.getFornecidoTerceiro());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        itensVendaSelecionados.addAll(vendaService.listarVendaServico(vendaSelecionada).stream()
                .map((VendaServico vs) -> {
                    ProdutoServicoDTO servDTO = itensVendaDisponiveis.stream()
                            .filter(produto -> produto.getIdProdutoServico().substring(2).equals(vs.getIdServico().getId() + ""))
                            .findAny().orElse(null);
                    if (servDTO == null) {
                        return null;
                    }
                    return new ItemVendaDTO(vs.getId(), servDTO, vs.getValorVenda(), vs.getQuantidade() * 1d, vs.getDesconto(), vs.getDetalhesItem(), vs.getFornecidoTerceiro());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));

        contaSelecionada = contaService.buscarConta(vendaSelecionada);

        List<ContaParcela> listContaReceber = contaService.listarContaParcela(contaSelecionada);

        List<ParcelaDTO> listParcelaDTO = listContaReceber.stream()
                .map(contaReceberParcela -> {
                    ParcelaDTO parcelaDTO = new ParcelaDTO();
                    parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
                    parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
                    parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
                    parcelaDTO.setValor(contaReceberParcela.getValor());
                    parcelaDTO.setDataPagamento(contaReceberParcela.getDataPagamento());
                    parcelaDTO.setValorPago(contaReceberParcela.getValorPago());
                    return parcelaDTO;
                })
                .collect(Collectors.toList());

        vendaSelecionada.setListParcelaDTO(listParcelaDTO);

        return "cadastroVenda.xhtml";
    }

    public String doStartDuplicar() {
        try {
            Venda duplicada = vendaService.duplicar(vendaSelecionada);
            duplicada.setSituacao(EnumSituacaoCompraVenda.ATIVO.getSituacao());
            duplicada.setValorPago(0d);
            vendaSelecionada = duplicada;
            itemVendaSelecionado = new ItemVendaDTO();
            doStartAlterar();
            return "cadastroVenda.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaVenda.xhtml";
    }

    public String alteraVendaOrcamento() {

        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
        // preenchendo venda com a lista de produtos
        listVendaProduto = vendaService.listarVendaProduto(vendaSelecionada);
        listVendaProduto.stream().forEach(vendaProduto -> vendaProduto.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaServico = vendaService.listarVendaServico(vendaSelecionada);
        listVendaServico.stream().forEach(vendaServico -> vendaServico.setControle(StringUtil.gerarStringAleatoria(8)));

        contaSelecionada = contaService.buscarConta(vendaSelecionada);

        List<ParcelaDTO> listParcelaDTO = new LinkedList<>();
        List<ContaParcela> listContaReceber = contaService.listarContaParcela(contaSelecionada);

        for (ContaParcela contaReceberParcela : listContaReceber) {
            ParcelaDTO parcelaDTO = new ParcelaDTO();
            parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
            parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
            parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
            parcelaDTO.setValor(contaReceberParcela.getValor());

            listParcelaDTO.add(parcelaDTO);
        }

        vendaSelecionada.setListParcelaDTO(listParcelaDTO);

        return "efetuaVendaOrcamento.xhtml";
    }

    public boolean podeCancelarVenda(Venda venda) {
        return !contaService.existeParcelaPaga(venda) && !EnumSituacaoCompraVenda.CANCELADO.getSituacao().equals(venda.getSituacao());
    }

    public boolean existeParcelaPaga() {
        return contaService.existeParcelaPaga(vendaSelecionada);
    }

    public boolean existeParcelaPaga(Venda venda) {
        return contaService.existeParcelaPaga(venda);
    }

    public void populateVendaProdServ() {
        listVendaProduto = new ArrayList<>();
        listVendaServico = new ArrayList<>();
        final String tenat = getUsuarioLogado().getTenat();
        itensVendaSelecionados.forEach(iv -> {
            if (iv.getIdProdutoServico().getTipo() == EnumTipoItemVenda.PRODUTO) {
                if (iv.getIdVendaProduto() != null) {
                    VendaProduto vp = vendaService.buscarVendaProduto(iv.getIdVendaProduto());
                    vp.setIdVenda(vendaSelecionada);
                    vp.getDadosProduto().setDesconto(iv.getDesconto());
                    vp.getDadosProduto().setQuantidade(iv.getQuantidade());
                    vp.getDadosProduto().setValor(iv.getValor());
                    vp.setFornecidoTerceiro(iv.getFornecidoTerceiro());
                    listVendaProduto.add(vp);

                    return;
                }
                Produto produto = produtoService.buscar(Integer.parseInt(iv.getIdProdutoServico().getIdProdutoServico().substring(2)));
                if (produto.getComposto().equals(EnumTipoComposicaoProduto.KIT_DE_PRODUTO.getTipo())) {
                    produto.setProdutoComposicaoList(produtoService.listarProdutosCompostosPorProduto(produto, true));
                    Double valorTotal = produto.getProdutoComposicaoList().stream()
                            .mapToDouble(pc -> pc.getIdProduto().getValorVenda())
                            .sum();
                    produto.getProdutoComposicaoList().forEach(prodComposicao -> {
                        VendaProduto vp = new VendaProduto();
                        vp.getDadosProduto().setDesconto(prodComposicao.getIdProduto().getValorVenda() / valorTotal * iv.getDesconto());
                        vp.getDadosProduto().setDetalhesItem(iv.getObservacao());
                        vp.getDadosProduto().setIdProduto(prodComposicao.getIdProduto());
                        vp.setIdVenda(vendaSelecionada);
                        vp.getDadosProduto().setQuantidade(prodComposicao.getQuantidade() * iv.getQuantidade());
                        vp.setTenatID(tenat);
                        vp.getDadosProduto().setValor(prodComposicao.getIdProduto().getValorVenda() * iv.getQuantidade());
                        vp.setFornecidoTerceiro(iv.getFornecidoTerceiro());
                        listVendaProduto.add(vp);
                    });
                    return;
                }
                VendaProduto vp = new VendaProduto();
                vp.getDadosProduto().setDesconto(iv.getDesconto());
                vp.getDadosProduto().setDetalhesItem(iv.getObservacao());
                vp.getDadosProduto().setIdProduto(produto);
                vp.setIdVenda(vendaSelecionada);
                vp.getDadosProduto().setQuantidade(iv.getQuantidade());
                vp.setTenatID(tenat);
                vp.getDadosProduto().setValor(iv.getValor());
                vp.setFornecidoTerceiro(iv.getFornecidoTerceiro());

                listVendaProduto.add(vp);
            } else if (iv.getIdProdutoServico().getTipo() == EnumTipoItemVenda.SERVICO) {
                VendaServico vs;
                if (iv.getIdVendaServico() != null) {
                    vs = vendaService.buscarVendaServico(iv.getIdVendaServico());
                } else {
                    vs = new VendaServico();
                    vs.setDesconto(iv.getDesconto());
                    vs.setDetalhesItem(tenat);
                    vs.setIdServico(servicoService.buscar(Integer.parseInt(iv.getIdProdutoServico().getIdProdutoServico().substring(2))));
                    vs.setCusto(NumeroUtil.somar(vs.getIdServico().getCustoServico(), 0d));
                    vs.setIdVenda(vendaSelecionada);
                    vs.setTenatID(tenat);
                    vs.setValorVenda(iv.getValor());
                    vs.setQuantidade(iv.getQuantidade().intValue());
                    vs.setFornecidoTerceiro(iv.getFornecidoTerceiro());
                }
                listVendaServico.add(vs);
            }
        });
    }

    public String doFinishAdd() {
        try {
            if (vendaSelecionada.getIdUsuarioVendedor() == null) {
                vendaSelecionada.setIdUsuarioVendedor(getUsuarioLogado());
            }
            boolean novaVenda = vendaSelecionada.getId() == null;
            itemVendaSelecionado = new ItemVendaDTO();
            preencherFormaPagamento();
            populateVendaProdServ();
            if (!listVendaProduto.isEmpty() || !listVendaServico.isEmpty() && !vendaSelecionada.getListParcelaDTO().isEmpty()) {
                Funcionario funcionario = getUsuarioLogado().getIdFuncionario();
                if (null != funcionario) {
                    Double limiteDeDesconto = NumeroUtil.somar(funcionario.getLimiteDeDesconto());
                    if (limiteDeDesconto > 0 && vendaSelecionada.getValorDesconto() > limiteDeDesconto) {
                        createFacesErrorMessage("Limite de desconto excedido. Não será possível salvar a venda!");
                        return "cadastroVenda.xhtml";
                    }
                }

                if (vendaSelecionada.getListParcelaDTO() == null) {
                    preencherFormaPagamento();
                }

                vendaSelecionada.setVendaProdutoList(listVendaProduto);
                vendaSelecionada.setVendaServicoList(listVendaServico);
                vendaSelecionada.setVendaItemOrdemDeServicoList(new ArrayList<>());
                vendaSelecionada.setVendaFormaPagamentoList(new ArrayList<>());
                vendaSelecionada.setOrigem(EnumOrigemVenda.SITE.getOrigem());
                vendaSelecionada.setValor(getValorT());
                vendaSelecionada.setValorDesconto(getValorDescontoT());

                ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
                vendaSelecionada.setIdPlanoConta(parametroSistemaSelecionado.getIdPlanoContaVendaPadrao());
                if (vendaSelecionada.getIdPlanoConta() == null) {
                    createFacesErrorMessage("Paramentro de plano de contas padrão não foi definido. Não será possivel salvar venda.");
                    return "cadastroVenda.xhtml";
                }
                vendaService.salvar(vendaSelecionada);

                if (novaVenda && linkPagamento) {
                    transacaoService.adicionarEnviarTransacaoPendente(vendaSelecionada);
                }

                createFacesSuccessMessage("Venda salva com sucesso!");
                return "listaVenda.xhtml";
            } else {
                createFacesErrorMessage("Informe ao menos um produto para efetuar a venda");
                return "cadastroVenda.xhtml";
            }
        } catch (RestClientException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível enviar o SMS para o cliente.");
            return "cadastroVenda.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar a venda: " + ex.getMessage());
            return "cadastroVenda.xhtml";
        }
    }

    public void doStartSendPaymentLink() {
        if (vendaSelecionada.getTelefoneCliente() == null) {
            createFacesErrorMessage("Telefone do cliente não informado");
            return;
        }
        boolean enviou = transacaoService.enviarTransacaoPendente(vendaSelecionada);
        if (enviou) {
            createFacesSuccessMessage("SMS enviado com sucesso");
        } else {
            createFacesErrorMessage("Não foi possível enviar o SMS");
        }
    }

    public String salvarVendaOrcamento() {

        try {
            if (!listVendaProduto.isEmpty() || !listVendaServico.isEmpty() && !vendaSelecionada.getListParcelaDTO().isEmpty()) {
                vendaSelecionada.setVendaProdutoList(listVendaProduto);
                vendaSelecionada.setVendaServicoList(listVendaServico);
                vendaService.salvarOrcamento(vendaSelecionada, true);

                createFacesSuccessMessage("Venda salva com sucesso!");
                return "listaVenda.xhtml";
            } else {
                createFacesErrorMessage("Informe ao menos um produto para efetuar a venda");
                return "cadastroVenda.xhtml";
            }
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroVenda.xhtml";
        }

    }

    public String cancelarVenda() {
        try {
            if (existeParcelaPaga(vendaSelecionada)) {
                throw new CadastroException("Existem parcelas pagas para essa venda, para cancelar a venda é necessário antes cancelar essas parcelas.", null);
            }
            vendaService.cancelarVenda(vendaSelecionada);
            createFacesSuccessMessage("Venda cancelada com sucesso!");
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(VendaControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao cancelar a venda.");
        }
        return "listaVenda.xhtml";
    }

    public void preencherFormaPagamento() {
        populateVendaProdServ();
        vendaSelecionada.setValor(getValorT());
        vendaSelecionada.setValorDesconto(getValorDescontoT());
        vendaSelecionada = contaService.parcelarVenda(vendaSelecionada);
    }

    public void calcularDescontoTotalLimparFormaPagamento() {
        cleanFormaPagamento();
        calcularDescontoTotal();
        vendaSelecionada.setValor(getValorT());
    }

    public void calcularValorTotalLimparFormaPagamento() {

        vendaService.calcularPontos(listVendaProduto, listVendaServico);

        cleanFormaPagamento();
        calcularValorTotal();
    }

    public void calcularValorTotal() {
        Double valorTotal = vendaService.calcularValorTotal(listVendaProduto, listVendaServico, vendaSelecionada.getListParcelaDTO());
        vendaSelecionada.setValor(valorTotal);
    }

    public void calcularDescontoTotal() {
        Double descontoTotal = vendaService.calcularDescontoTotal(listVendaProduto, listVendaServico, vendaSelecionada.getListParcelaDTO());
        vendaSelecionada.setValorDesconto(descontoTotal);
    }

    public void cleanProduto() {
        produtoSelecionado = null;
        vendaProdutoSelecionado = new VendaProduto();
        vendaProdutoSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
        vendaProdutoSelecionado.getDadosProduto().setValor(0d);
        vendaProdutoSelecionado.getDadosProduto().setDesconto(0d);
    }

    public void cleanServico() {
        servicoSelecionado = null;
        vendaServicoSelecionado = new VendaServico();
        vendaServicoSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
        vendaServicoSelecionado.setValorVenda(0d);
        vendaServicoSelecionado.setDesconto(0d);
    }

    public void doStartAddProduto() {
        cleanProduto();
        cleanServico();
        listVendaProduto.add(vendaProdutoSelecionado);
    }

    public void doStartAddServico() {
        cleanProduto();
        cleanServico();
        listVendaServico.add(vendaServicoSelecionado);
    }

    public void carregarProduto(VendaProduto vendaProduto) {
        if (vendaProduto != null && vendaProduto.getDadosProduto().getIdProduto() != null) {
            vendaProduto.setIdVenda(vendaSelecionada);
            vendaProduto.getDadosProduto().setValor(vendaProduto.getDadosProduto().getIdProduto().getValorVenda());
            vendaProduto.setPontos(vendaProduto.getDadosProduto().getIdProduto().getPontos());
            vendaProduto.getDadosProduto().setIdNcm(vendaProduto.getDadosProduto().getIdProduto().getIdNcm());
            vendaProduto.getDadosProduto().setIdCsosn(vendaProduto.getDadosProduto().getIdProduto().getIdCsosn());
            vendaProduto.getDadosProduto().setQuantidade(1d);
            vendaProduto.getDadosProduto().setDesconto(0d);
            vendaService.atualizaTenatIdVendaProduto(vendaProduto);
        }

        calcularValorTotalLimparFormaPagamento();
        calcularDescontoTotalLimparFormaPagamento();
    }

    public void carregarServico(VendaServico vendaServico) {
        if (vendaServico != null && vendaServico.getIdServico() != null) {
            vendaServico.setIdVenda(vendaSelecionada);

            if (vendaServico.getIdServico().getCustoServico() != null) {
                vendaServico.setCusto(vendaServico.getIdServico().getCustoServico());
            } else {
                vendaServico.setCusto(0d);
            }

            if (vendaServico.getIdServico().getValorVenda() != null) {
                vendaServico.setValorVenda(vendaServico.getIdServico().getValorVenda());
            } else {
                vendaServico.setValorVenda(0d);
            }

            vendaServico.setDesconto(0d);
            vendaServico.setPontos(vendaServico.getIdServico().getPontos());

            vendaService.atualizaTenatIdVendaServico(vendaServico);
        }

        calcularValorTotalLimparFormaPagamento();
        calcularDescontoTotalLimparFormaPagamento();
    }

    public void removeProduto() {
        listVendaProduto.remove(vendaProdutoSelecionado);
        calcularValorTotalLimparFormaPagamento();
        calcularDescontoTotalLimparFormaPagamento();
    }

    public void removeServico() {
        listVendaServico.remove(vendaServicoSelecionado);
        calcularValorTotalLimparFormaPagamento();
        calcularDescontoTotalLimparFormaPagamento();
    }

    public String getDescontoTotal() {
        // somando os descontos dos produtos da lista
        // Removida a lista de parcela pois ao alterar a venda, o valor não era atualizado já que as parcelas já existiam
        Double desconto = vendaService.calcularDescontoTotal(listVendaProduto, listVendaServico, new ArrayList<>());

        return "R$ " + super.converterValorParaMonetario(desconto);
    }

    public String getSubTotal() {
        populateVendaProdServ();
        // somando o total dos produtos da lista
        // Removida a lista de parcela pois ao alterar a venda, o valor não era atualizado já que as parcelas já existiam
        Double total = vendaService.calcularTotais(itensVendaSelecionados, listVendaOS, new ArrayList<>());

        return "R$ " + super.converterValorParaMonetario(total);
    }

    public String getValorTotal() {
        // somando o total dos produtos da lista
        // Removida a lista de parcela pois ao alterar a venda, o valor não era atualizado já que as parcelas já existiam
        Double desconto = vendaService.calcularTotalDesconto(itensVendaSelecionados, listVendaOS, new ArrayList<>());
        Double total = vendaService.calcularTotais(itensVendaSelecionados, listVendaOS, new ArrayList<>());

        return "R$ " + super.converterValorParaMonetario(total - desconto);
    }

    public Double getValorT() {
        // Removida a lista de parcela pois ao alterar a venda, o valor não era atualizado já que as parcelas já existiam
        Double desconto = vendaService.calcularTotalDesconto(itensVendaSelecionados, listVendaOS, vendaSelecionada.getListParcelaDTO());
        Double total = vendaService.calcularTotais(itensVendaSelecionados, listVendaOS, vendaSelecionada.getListParcelaDTO());

        return total - desconto;
    }

    public Double getValorDescontoT() {
        // Removida a lista de parcela pois ao alterar a venda, o valor não era atualizado já que as parcelas já existiam
        return vendaService.calcularDescontoTotal(listVendaProduto, listVendaServico, new ArrayList<>());
    }

    public List<Produto> getProdutosDisponiveis() {
        List<Produto> produtosDisponiveis = produtoService.listar();
        if (EnumTipoVenda.PONTUACAO.getSituacao().equals(vendaSelecionada.getStatusNegociacao())) {
            produtosDisponiveis.removeIf(p -> p.getPontos() == null);
        }
        return produtosDisponiveis;
    }

    public List<Servico> getServicosDisponiveis() {
        List<Servico> servicosDisponiveis = servicoService.listar();
        List<Servico> servicosAtribuidos = listVendaServico.stream().map(VendaServico::getIdServico).distinct().collect(Collectors.toList());
        if (servicosAtribuidos != null) {
            servicosDisponiveis.removeAll(servicosAtribuidos);
        }

        if (EnumTipoVenda.PONTUACAO.getSituacao().equals(vendaSelecionada.getStatusNegociacao())) {
            servicosDisponiveis.removeIf(s -> s.getPontos() == null);
        }
        return servicosDisponiveis;
    }

    public Integer somaListas() {
        somaListas = listVendaProduto.size() + listVendaServico.size();

        return somaListas;
    }

    public String doShowAuditoria() {
        return "listaAuditoriaVenda.xhtml";
    }

    public String doFinishExcluir() {
        try {
            vendaService.cancelarVenda(vendaSelecionada);
            vendaSelecionada.setSituacao(EnumSituacaoCompraVenda.DELETADO.getSituacao());
            vendaService.alterar(vendaSelecionada);
            createFacesSuccessMessage("Venda removida com sucesso.");
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível remover a venda.");
        }
        return "listaVenda.xhtml";
    }

    public List<VendaProduto> listVendaProdutoPreenchida(Venda venda) {
        return vendaService.listarVendaProduto(venda);
    }

    public List<VendaServico> listVendaServicoPreenchida(Venda venda) {
        return vendaService.listarVendaServico(venda);
    }

    public List<Object> getDadosAuditoria() {
        return vendaService.getDadosAuditoriaByID(vendaSelecionada);
    }

    public String getDarBaixa() {
        return vendaSelecionada.isDarBaixa() ? "S" : "N";
    }

    public void setDarBaixa(String darBaixa) {
        if (vendaSelecionada != null) {
            vendaSelecionada.setDarBaixa(darBaixa != null && darBaixa.equals("S"));
        }
    }

    public Boolean getPodeEmitirNFCE() {
        ParametroSistema params = parametroSistemaService.getParametro();
        boolean podeEmitir = params != null && params.getNfceCsc() != null && !params.getNfceCsc().isEmpty()
                && params.getNfceToken() != null && !params.getNfceToken().isEmpty();
        if (podeEmitir) {
            setEmitirNFCe("S");
            setDarBaixa("S");
        }
        return podeEmitir;
    }

    public String getEmitirNFCe() {
        return vendaSelecionada.isEmitirNFCe() ? "S" : "N";
    }

    public void setEmitirNFCe(String emitirNFCe) {
        this.vendaSelecionada.setEmitirNFCe(emitirNFCe.equals("S"));
    }

    public Double totalizaQuantidadeProduto() {
        return vendaSelecionada.getVendaProdutoList().stream()
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getQuantidade)
                .sum();
    }

    public Double totalizaDescontoProduto() {
        return vendaSelecionada.getVendaProdutoList().stream()
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getDesconto)
                .sum() + 0d;
    }

    public Double totalizaValorProduto() {
        return vendaSelecionada.getVendaProdutoList().stream()
                .map(VendaProduto::getDadosProduto)
                .mapToDouble(DadosProduto::getValorTotal)
                .sum() + 0d;
    }

    public boolean exibeFooterServico() {
        return !vendaSelecionada.getVendaServicoList().isEmpty() && !listVendaServico.isEmpty();
    }

    public Double totalizaDescontoServico() {
        return vendaSelecionada.getVendaServicoList().stream()
                .mapToDouble(VendaServico::getDesconto)
                .sum() + 0d;
    }

    public Double totalizaValorServico() {
        return vendaSelecionada.getVendaServicoList().stream()
                .mapToDouble(VendaServico::getValorVenda)
                .sum() + 0d;
    }

    public void initPDV() {
        if (!FacesContext.getCurrentInstance().isPostback()) {
            vendaSelecionada = new Venda();
            ParametroSistema ps = parametroSistemaService.getParametro();
            String obs = ps.getObservacaoPadraoPdv();
            if (obs == null) {
                obs = "";
            }

            if (ps.getIdPlanoContaPadraoPdv() == null
                    || ps.getIdCentroCustoPadraoPdv() == null
                    || ps.getIdContaBancariaPadraoPdv() == null) {
                FacesContext facesContext = FacesContext.getCurrentInstance();
                facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "informacaoPDV.xhtml");
                return;
            }

            vendaSelecionada.setIdPlanoConta(ps.getIdPlanoContaPadraoPdv());
            vendaSelecionada.setIdCentroCusto(ps.getIdCentroCustoPadraoPdv());
            vendaSelecionada.setObservacao(obs);
            vendaSelecionada.setStatusNegociacao("VD");
            vendaSelecionada.setIdContaBancaria(ps.getIdContaBancariaPadraoPdv());
            vendaSelecionada.setDataVenda(new Date());
            vendaSelecionada.setDarBaixa(true);
            vendaSelecionada.setDesconto(0d);

            cleanCache();
            codigoDeBarras = "";
        }
    }

    public String getSituacao(String tipo) {
        ParametroSistema ps = parametroSistemaService.getParametro();

        switch (tipo) {
            case "PC":
                return ps.getIdPlanoContaPadraoPdv() != null ? "icon-like font-blue-madison" : "icon-dislike font-red-mint";
            case "CC":
                return ps.getIdCentroCustoPadraoPdv() != null ? "icon-like font-blue-madison" : "icon-dislike font-red-mint";
            case "CB":
                return ps.getIdContaBancariaPadraoPdv() != null ? "icon-like font-blue-madison" : "icon-dislike font-red-mint";
            default:
                return "icon-dislike";
        }
    }

    public List<FormaPagamento> getFormaPagamentoParaNFeList() {
        return formaPagamentoService.listarParaNFe();
    }

    public void doStartAddProdutoPDV() {
        Produto prod = produtoService.buscarPorCodigoBarras(codigoDeBarras);
        cleanProduto();
        if (prod != null) {
            vendaProdutoSelecionado.getDadosProduto().setIdProduto(prod);
            vendaProdutoSelecionado.getDadosProduto().setQuantidade(1d);
            vendaProdutoSelecionado.getDadosProduto().setValor(prod.getValorVenda());
            vendaProdutoSelecionado.getDadosProduto().setDesconto(0d);
        }
    }

    public void addProdutoToList() {
        boolean alreadyHasTheProduct = vendaProdutoSelecionado.getDadosProduto().getIdProduto() == null || listVendaProduto.stream()
                .anyMatch(vp -> vp.getDadosProduto().getIdProduto().equals(vendaProdutoSelecionado.getDadosProduto().getIdProduto()));
        if (!alreadyHasTheProduct) {
            listVendaProduto.add(vendaProdutoSelecionado);
            cleanProduto();
            codigoDeBarras = "";
        }
    }

    public Integer getIndex(VendaProduto vp) {
        return listVendaProduto.indexOf(vp) + 1;
    }

    public Double getQuantidadeTotalCarrinho() {
        return listVendaProduto.stream().map(VendaProduto::getDadosProduto).mapToDouble(DadosProduto::getQuantidade).sum();
    }

    public Double getValorTotalCarrinho() {
        return listVendaProduto.stream().mapToDouble(vp -> vp.getDadosProduto().getValor() * vp.getDadosProduto().getQuantidade()).sum();
    }

    public String doFinishAddPDV() {
        if (vendaSelecionada.getDesconto() > 0) {
            Double vlrDesconto = vendaSelecionada.getDesconto() / getValorTotalCarrinho();
            listVendaProduto = listVendaProduto.stream()
                    .map(vp -> {
                        vp.getDadosProduto().setValor(vp.getDadosProduto().getIdProduto().getValorVenda());
                        vp.getDadosProduto().setDesconto(vlrDesconto * vp.getDadosProduto().getValor());
                        return vp;
                    }).collect(Collectors.toList());
        }

        doFinishAdd();
        return "pdv.xhtml";
    }

    public void loadItem() {
        for (ProdutoServicoDTO item : itensVendaDisponiveis) {
            try {
                if (item.getIdProdutoServico().equals(itemVendaSelecionado.getIdProdutoServico().getIdProdutoServico())) {
                    itemVendaSelecionado.setIdProdutoServico(item);
                    itemVendaSelecionado.setValor(item.getValor());
                    if (itemVendaSelecionado.getIdProdutoServico().getTipo() == EnumTipoItemVenda.PRODUTO) {
                        estoqueProdutoSelecinado = estoqueService.buscarSaldoDTO(itemVendaSelecionado.getIdProdutoServico());
                    } else {
                        estoqueProdutoSelecinado = 0d;
                    }
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(VendaControle.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addItem() {
        if (adicionaProduto) {
            ProdutoServicoDTO prodServ;
            if (ehProduto) {
                addProduto.setDescricao(addDescricao);
                addProduto.setDataEntrada(new Date());
                addProduto.setEstoqueDisponivel(estoqueInicial);
                addProduto.setIdUnidadeMedida(addUnidade);
                addProduto.setValorCusto(precoCusto);
                addProduto.setValorVenda(precoVenda);
                addProduto.setComposto("N");
                prodServ = new ProdutoServicoDTO(produtoService.salvarProduto(addProduto));
            } else {
                addServico.setDescricao(addDescricao);
                addServico.setCustoServico(precoCusto);
                addServico.setValorVenda(precoVenda);
                prodServ = new ProdutoServicoDTO(servicoService.salvar(addServico));
            }
            itemVendaSelecionado.setValor(precoVenda);
            itemVendaSelecionado.setIdProdutoServico(prodServ);
            itensVendaDisponiveis.add(prodServ);
        }
        if (itemVendaSelecionado.getIdProdutoServico() != null) {
            if (controleEdicao == null) {
                itemVendaSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
                itensVendaSelecionados.add(itemVendaSelecionado);
            } else {
                for (int i = 0; i < itensVendaSelecionados.size(); i++) {
                    ItemVendaDTO item = itensVendaSelecionados.get(i);
                    if (item.getControle().equals(controleEdicao)) {
                        itensVendaSelecionados.set(i, itemVendaSelecionado);
                        controleEdicao = null;
                        break;
                    }
                }
            }
            itemVendaSelecionado = new ItemVendaDTO();
            controleEdicao = null;
        }

    }

    public void limpaControle() {
        controleEdicao = null;
        itemVendaSelecionado = new ItemVendaDTO();
        estoqueProdutoSelecinado = 0d;
    }

    public void editItem(ItemVendaDTO item) {
        controleEdicao = item.getControle();
        itemVendaSelecionado = item;
        estoqueProdutoSelecinado = estoqueService.buscarSaldoDTO(itemVendaSelecionado.getIdProdutoServico());
    }

    public void removeItem(ItemVendaDTO item) {
        itensVendaSelecionados.remove(item);
        itemVendaSelecionado = new ItemVendaDTO();
    }

    public ItemVendaDTO getBlankItemVenda() {
        return new ItemVendaDTO();
    }

    public void doPrint() {
        try {
            gerarPdf(relatorioService.gerarImpressaoVenda(vendaSelecionada), "Venda " + vendaSelecionada.getId());
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doPrintRecibo() {
        try {
            InputStream iStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/images/handshakereport.jpg");
            gerarPdf(relatorioService.gerarImpressaoReciboVenda(vendaSelecionada, IOUtils.toByteArray(iStream)), "Recibo Venda " + vendaSelecionada.getId());
            //       gerarImpressaoReciboVenda(vendaSelecionada),
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public String getSituacaoVenda(String sigla) {
        return "icon-" + (controleMenu.isCadastroOk(sigla) ? "like font-blue-madison" : "dislike font-red-mint");
    }

    public void addCelular() {
        if (!StringUtils.isEmpty(vendaSelecionada.getIdCliente().getTelefoneCelular())) {
            vendaSelecionada.setTelefoneCliente(vendaSelecionada.getIdCliente().getTelefoneCelular());
        }
    }

    public boolean ehAVista() {
        return EnumFormaPagamento.AVISTA.getForma().equals(vendaSelecionada.getFormaPagamento());
    }

    public void adicionarProduto() {
        adicionaProduto = adicionaProduto == false;
        addUnidade = unidadeMedidaService.buscarPorSigla("UN");
        addProduto = new Produto();
        addServico = new Servico();
        addDescricao = "";
        estoqueInicial = 0d;
        precoCusto = 0d;
        precoVenda = 0d;
    }

    public void limpaDesconto() {
        valorDesconto = 0d;
        sobreescreverDesconto = true;
        tipoDesconto = "PS";
    }

    public void addDesconto() {
        if (valorDesconto <= 0d) {
            createFacesErrorMessage("Informe um valor positivo para o desconto.");
            return;
        }
        Double valorTotal = itensVendaSelecionados.stream()
                .filter(iv -> tipoDesconto.contains(iv.getIdProdutoServico().getTipo().getTipo()))
                .mapToDouble(iv -> Boolean.TRUE.equals(sobreescreverDesconto) ? iv.getValorTotal() : (iv.getValor() * iv.getQuantidade())).sum();

        Double razao = valorDesconto / valorTotal;

        itensVendaSelecionados.stream()
                .filter(iv -> tipoDesconto.contains(iv.getIdProdutoServico().getTipo().getTipo()))
                .forEach(iv -> {
                    Double novoDesconto = razao * iv.getValor() * iv.getQuantidade();
                    if (!Boolean.TRUE.equals(sobreescreverDesconto)) {
                        novoDesconto += iv.getDesconto();
                    }
                    iv.setDesconto(novoDesconto);
                });
        preencherFormaPagamento();
    }

    public boolean estaPago(Conta conta) {
        return contaService.estaPago(conta);
    }

}
