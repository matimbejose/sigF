package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.controle.ProdutoControle;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemVendaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.EstoqueService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeMedidaService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.IOUtils;
import org.primefaces.component.datagrid.DataGrid;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Getter
@Setter
public abstract class VendaControleBase extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    protected VendaService vendaService;

    @EJB
    protected FuncaoAjudaService funcaoAjudaService;

    @EJB
    protected ProdutoService produtoService;

    @EJB
    protected ServicoService servicoService;

    @EJB
    protected ContaService contaService;

    @EJB
    protected ContaBancariaService contaBancariaService;

    @EJB
    protected ClienteService clienteService;

    @EJB
    protected RelatorioService relatorioService;

    @EJB
    protected ParametroSistemaService parametroSistemaService;

    @EJB
    protected EmpresaService empresaService;

    @EJB
    protected FormaPagamentoService formaPagamentoService;

    @EJB
    protected UnidadeMedidaService unidadeMedidaService;

    @EJB
    protected EstoqueService estoqueService;

    @EJB
    protected DocumentoService documentoService;

    @EJB
    protected DocumentoAnexoService documentoAnexoService;

    @Inject
    protected ControleMenu controleMenu;

    @Inject
    protected DocumentoMapper documentoMapper;

    protected Venda vendaSelecionada;

    protected Conta contaSelecionada;

    protected Produto produtoSelecionado;

    protected Servico servicoSelecionado;

    protected VendaItemOrdemDeServico itensOrdemDeServicoSelecionado;

    protected VendaProduto vendaProdutoSelecionado;

    protected VendaServico vendaServicoSelecionado;

    protected transient List<VendaProduto> listVendaProduto = new LinkedList<>();

    protected transient List<VendaServico> listVendaServico = new LinkedList<>();

    protected transient List<VendaItemOrdemDeServico> listVendaOS = new LinkedList<>();

    protected int somaListas;

    protected LazyDataModel<Venda> model;

    protected VendaFiltro filtro = new VendaFiltro();

    protected Boolean cadastraCliente = false;

    protected Cliente cliente;

    protected transient List<String> tiposSelecionados;

    protected FormaPagamento formaDePagamentoSelecionada;

    protected transient List<VendaFormaPagamento> listFormaDePagamento;

    protected Boolean viewOnly;

    protected ItemVendaDTO itemVendaSelecionado;

    protected transient List<ProdutoServicoDTO> itensVendaDisponiveis;

    protected transient List<ItemVendaDTO> itensVendaSelecionados;

    protected String controleEdicao;

    protected Double estoqueProdutoSelecinado;

    protected String ehParticular = "S";

    protected transient List<AnexoDTO> listaAnexos;

    protected boolean adicionaProduto = false;

    protected boolean ehProduto = true;

    protected String addDescricao;

    protected UnidadeMedida addUnidade;

    protected Produto addProduto;

    protected Servico addServico;

    protected Double precoCusto;

    protected Double precoVenda;

    protected Double estoqueInicial;

    protected Double valorDesconto;

    protected Boolean sobreescreverDesconto;

    protected String tipoDesconto;

    public abstract String getPaginaCadastro();

    public String buscarDescricaoTipoVenda(String situacao) {
        return EnumTipoVenda.getDescricao(situacao);
    }

    public void cleanProdutosServicosOS() {
        listVendaProduto = new LinkedList<>();
        listVendaServico = new LinkedList<>();
        listVendaOS = new LinkedList<>();
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_COMPRA.getChave()).getDescricao());
        return "cadastroBanco.xhtml";
    }

    public void cleanCliente() {
        cadastraCliente = false;
        cliente = new Cliente();
    }

    @Override
    public void cleanCache() {
        this.vendaSelecionada = new Venda();
        cleanProdutosServicosOS();
        cleanProduto();
        cleanServico();
        cleanOS();
        cleanFormaPagamento();
        cleanCliente();
        controleEdicao = null;
        adicionaProduto = false;
    }

    public void cleanFormaPagamento() {
        this.vendaSelecionada.setListParcelaDTO(null);
        this.vendaSelecionada.setFormaPagamento(null);
        this.formaDePagamentoSelecionada = new FormaPagamento();
        if (vendaSelecionada.getDataVencimento() == null) {
            this.listFormaDePagamento = new ArrayList<>();
        }
    }

    public EnumFormaPagamento[] getFormasPagamento() {
        return EnumFormaPagamento.values();
    }

    public void doPrint(FormaPagamento formaDePagamento) {
        try {
            gerarPdf(relatorioService.gerarImpressaoOrcamento(vendaSelecionada, formaDePagamento), "Orçamento");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
        } catch (CadastroException | RelatorioException | JRException | IOException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public void doStartPrint() {
        FormaPagamento fp = null;
        try {
            fp = vendaSelecionada.getIdFormaPagamento();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
        doPrint(fp);
    }

    public void initItensVendaDTO() {
        try {
            itensVendaDisponiveis = produtoService.listarParaVenda().stream()
                    .map(ProdutoServicoDTO::new)
                    .collect(Collectors.toList());

            itensVendaDisponiveis.addAll(servicoService.listarServicosAtivos().stream()
                    .map(ProdutoServicoDTO::new)
                    .collect(Collectors.toList()));

            itemVendaSelecionado = new ItemVendaDTO();
            itensVendaSelecionados = new ArrayList<>();
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public String doStartAlterar() {
        initItensVendaDTO();
        adicionaProduto = false;
        controleEdicao = null;
        if (vendaSelecionada.getIdVendaSeguradora() != null) {
            ehParticular = "N";
        }

        itensVendaSelecionados = vendaService.listarVendaProduto(vendaSelecionada).stream()
                .map((VendaProduto vp) -> {
                    ProdutoServicoDTO prodDTO = itensVendaDisponiveis.stream()
                            .filter(produto -> produto.getIdProdutoServico().substring(2).equals(vp.getDadosProduto().getIdProduto().getId() + ""))
                            .collect(Collectors.toList())
                            .get(0);
                    return new ItemVendaDTO(vp.getId(), prodDTO, vp.getDadosProduto().getValor(), vp.getDadosProduto().getQuantidade(), vp.getDadosProduto().getDesconto(), vp.getDadosProduto().getDetalhesItem(), vp.getFornecidoTerceiro());
                })
                .collect(Collectors.toList());
        itensVendaSelecionados.addAll(vendaService.listarVendaServico(vendaSelecionada).stream()
                .map((VendaServico vs) -> {
                    ProdutoServicoDTO servDTO = itensVendaDisponiveis.stream()
                            .filter(produto -> produto.getIdProdutoServico().substring(2).equals(vs.getIdServico().getId() + ""))
                            .collect(Collectors.toList())
                            .get(0);
                    return new ItemVendaDTO(vs.getId(), servDTO, vs.getValorVenda(), vs.getQuantidade() * 1d, vs.getDesconto(), vs.getDetalhesItem(), vs.getFornecidoTerceiro());
                })
                .collect(Collectors.toList()));
        listaAnexos = new ArrayList<>();
        if (vendaSelecionada.getIdDocumento() != null && !vendaSelecionada.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = vendaSelecionada.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }

        viewOnly = false;
        cleanCliente();
        // preenchendo venda com a lista de produtos
        listVendaProduto = vendaService.listarVendaProduto(vendaSelecionada);
        listVendaProduto.stream().forEach(vendaProduto -> {
            vendaProduto.setControle(StringUtil.gerarStringAleatoria(8));
            vendaProduto.getDadosProduto().setDesconto(vendaProduto.getDadosProduto().getDesconto());
        });

        listVendaServico = vendaService.listarVendaServico(vendaSelecionada);
        listVendaServico.stream().forEach(vendaServico -> {
            vendaServico.setControle(StringUtil.gerarStringAleatoria(8));
            vendaServico.setDesconto(vendaServico.getDesconto());
        });

        listVendaOS = vendaService.listarVendaItensOrdemDeServico(vendaSelecionada);
        listVendaOS.stream().forEach(vendaOS -> vendaOS.setDesconto(vendaOS.getDesconto()));

        listFormaDePagamento = vendaService.listarVendaFormaPagamento(vendaSelecionada);
        formaDePagamentoSelecionada = new FormaPagamento();
        if (listFormaDePagamento != null) {
            for (int i = listFormaDePagamento.size() - 1; i > 0; i--) {
                listFormaDePagamento.remove(i);
            }
            if (!listFormaDePagamento.isEmpty()) {
                formaDePagamentoSelecionada = listFormaDePagamento.get(0).getIdFormaPagamento();
            }
        }

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

        if (vendaSelecionada.getIdDocumento() != null) {
            vendaSelecionada.getIdDocumento().setDocumentoAnexoList(documentoAnexoService.listByDocumento(vendaSelecionada.getIdDocumento()));
        }

        return getPaginaCadastro();
    }

    public String doStartView() {
        String ret = doStartAlterar();
        viewOnly = true;
        return ret;
    }

    public String alteraVendaOrcamento() {
        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.VENDA.getSituacao());
        // preenchendo venda com a lista de produtos
        listVendaProduto = vendaService.listarVendaProduto(vendaSelecionada);
        listVendaProduto.stream().forEach(vendaProduto -> vendaProduto.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaServico = vendaService.listarVendaServico(vendaSelecionada);
        listVendaServico.stream().forEach(vendaServico -> vendaServico.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaOS = vendaService.listarVendaItensOrdemDeServico(vendaSelecionada);

        listFormaDePagamento = vendaService.listarVendaFormaPagamento(vendaSelecionada);
        if (listFormaDePagamento.isEmpty()) {
            createFacesErrorMessage("Este orçamento não tem nenhuma forma de pagamento definida");
            return "listaOrcamento.xhtml";
        }

        contaSelecionada = contaService.buscarConta(vendaSelecionada);

        List<ParcelaDTO> listParcelaDTO = contaService.listarContaParcela(contaSelecionada).stream()
                .map(contaReceberParcela -> {
                    ParcelaDTO parcelaDTO = new ParcelaDTO();
                    parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
                    parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
                    parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
                    parcelaDTO.setValor(contaReceberParcela.getValor());
                    return parcelaDTO;
                }).collect(Collectors.toList());

        vendaSelecionada.setListParcelaDTO(listParcelaDTO);

        return "efetuarVendaOrcamento.xhtml";
    }

    public String alteraOrcamentoParaOS() {
        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORDEM_SERVICO.getSituacao());
        // preenchendo OS com a lista de produtos
        listVendaProduto = vendaService.listarVendaProduto(vendaSelecionada);
        listVendaProduto.stream().forEach(vendaProduto -> vendaProduto.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaServico = vendaService.listarVendaServico(vendaSelecionada);
        listVendaServico.stream().forEach(vendaServico -> vendaServico.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaOS = vendaService.listarVendaItensOrdemDeServico(vendaSelecionada);

        listFormaDePagamento = vendaService.listarVendaFormaPagamento(vendaSelecionada);
        if (listFormaDePagamento.isEmpty()) {
            createFacesErrorMessage("Este orçamento não tem nenhuma forma de pagamento definida");
            return "listaOrcamento.xhtml";
        }

        contaSelecionada = contaService.buscarConta(vendaSelecionada);

        List<ParcelaDTO> listParcelaDTO = contaService.listarContaParcela(contaSelecionada).stream()
                .map(contaReceberParcela -> {
                    ParcelaDTO parcelaDTO = new ParcelaDTO();
                    parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
                    parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
                    parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
                    parcelaDTO.setValor(contaReceberParcela.getValor());
                    return parcelaDTO;
                }).collect(Collectors.toList());

        vendaSelecionada.setListParcelaDTO(listParcelaDTO);

        return "efetuarVendaOrcamento.xhtml";
    }

    public String editarOrcamento() {
        // preenchendo venda com a lista de produtos
        listVendaProduto = vendaService.listarVendaProduto(vendaSelecionada);
        listVendaProduto.stream().forEach(vendaProduto -> vendaProduto.setControle(StringUtil.gerarStringAleatoria(8)));

        listVendaServico = vendaService.listarVendaServico(vendaSelecionada);
        listVendaServico.stream().forEach(vendaServico -> vendaServico.setControle(StringUtil.gerarStringAleatoria(8)));

        contaSelecionada = contaService.buscarConta(vendaSelecionada);

        List<ParcelaDTO> listParcelaDTO = new LinkedList<>();
        List<ContaParcela> listContaReceber = contaService.listarContaParcela(contaSelecionada);

        listContaReceber.stream()
                .map(contaReceberParcela -> {
                    ParcelaDTO parcelaDTO = new ParcelaDTO();
                    parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
                    parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
                    parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
                    parcelaDTO.setValor(contaReceberParcela.getValor());
                    return parcelaDTO;
                })
                .forEachOrdered(listParcelaDTO::add);

        vendaSelecionada.setListParcelaDTO(listParcelaDTO);

        return "efetuaVendaOrcamento.xhtml";
    }

    public boolean existeParcelaPaga() {
        return contaService.existeParcelaPaga(vendaSelecionada);
    }

    public String doRejectOrcamento() {
        try {
            vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORCAMENTO_REJEITADO.getSituacao());
            vendaSelecionada.setVendaProdutoList(listVendaProduto);
            vendaSelecionada.setVendaServicoList(listVendaServico);
            vendaSelecionada.setVendaItemOrdemDeServicoList(listVendaOS);
            vendaSelecionada.setVendaFormaPagamentoList(listFormaDePagamento);
            vendaService.salvar(vendaSelecionada);
            return "listaOrcamento.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroOrcamento.xhtml";
        }
    }

    public String doCancelApproval() {
        cleanCache();
        return "listaOrcamento.xhtml";
    }

    public void populateVendaProdServ() {
        listVendaProduto = new ArrayList<>();
        listVendaServico = new ArrayList<>();
        final String tenat = getUsuarioLogado().getTenat();
        itensVendaSelecionados.forEach(iv -> {
            if (iv.getIdProdutoServico().getTipo() == EnumTipoItemVenda.PRODUTO) {
                VendaProduto vp;
                if (iv.getIdVendaProduto() != null) {
                    vp = vendaService.buscarVendaProduto(iv.getIdVendaProduto());
                } else {
                    vp = new VendaProduto();
                    vp.getDadosProduto().setIdProduto(produtoService.buscar(Integer.parseInt(iv.getIdProdutoServico().getIdProdutoServico().substring(2))));
                    vp.setIdVenda(vendaSelecionada);
                    vp.setTenatID(tenat);
                }
                vp.getDadosProduto().setDesconto(iv.getDesconto());
                vp.getDadosProduto().setDetalhesItem(iv.getObservacao());
                vp.getDadosProduto().setQuantidade(iv.getQuantidade());
                vp.getDadosProduto().setValor(iv.getValor());
                vp.setFornecidoTerceiro(iv.getFornecidoTerceiro());
                listVendaProduto.add(vp);
            } else if (iv.getIdProdutoServico().getTipo() == EnumTipoItemVenda.SERVICO) {
                VendaServico vs;
                if (iv.getIdVendaServico() != null) {
                    vs = vendaService.buscarVendaServico(iv.getIdVendaServico());
                } else {
                    vs = new VendaServico();
                    vs.setIdServico(servicoService.buscar(Integer.parseInt(iv.getIdProdutoServico().getIdProdutoServico().substring(2))));
                    vs.setIdVenda(vendaSelecionada);
                    vs.setTenatID(tenat);
                }
                vs.setDesconto(iv.getDesconto());
                vs.setDetalhesItem(iv.getObservacao());
                vs.setQuantidade(iv.getQuantidade().intValue());
                vs.setValorVenda(iv.getValor());
                vs.setCusto(NumeroUtil.somar(vs.getIdServico().getCustoServico()));
                vs.setFornecidoTerceiro(iv.getFornecidoTerceiro());
                listVendaServico.add(vs);
            }
        });
    }

    public String cancelarVenda() {
        try {
            vendaService.cancelarVenda(vendaSelecionada);
            createFacesSuccessMessage("Venda cancelada com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaVenda.xhtml";
    }

    public void preencherFormaPagamento() {
        if (vendaSelecionada.getDataVencimento() == null) {
            listFormaDePagamento.clear();
            vendaSelecionada.setIdFormaPagamento(null);
        }
        vendaSelecionada.setValor(getValorT());
        vendaSelecionada = contaService.parcelarVenda(vendaSelecionada);
    }

    public void calcularDescontoTotalLimparFormaPagamento() {
        cleanFormaPagamento();
        calcularDescontoTotal();
    }

    public void calcularValorTotalLimparFormaPagamento() {
        vendaService.calcularPontos(listVendaProduto, listVendaServico);

        cleanFormaPagamento();
        calcularValorTotal();
    }

    public void calcularValorTotal() {
        calcularDescontoTotal();
        calcularValorTotalOrcamento();
    }

    public void calcularValorTotalOrcamento() {
        Double valorTotal = vendaService.calcularTotais(itensVendaSelecionados, listVendaOS, vendaSelecionada.getListParcelaDTO());
        vendaSelecionada.setValor(valorTotal - vendaSelecionada.getValorDesconto());
    }

    public void calcularDescontoTotal() {
        Double descontoTotal = vendaService.calcularTotalDesconto(itensVendaSelecionados, listVendaOS, new ArrayList<>());
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

    public void cleanOS() {
        itensOrdemDeServicoSelecionado = new VendaItemOrdemDeServico();
        itensOrdemDeServicoSelecionado.setValor(0d);
        itensOrdemDeServicoSelecionado.setDesconto(0d);
    }

    public void doStartAddProduto() {
        cleanProduto();
        cleanServico();
        cleanOS();
        listaAnexos = new ArrayList<>();
        listVendaProduto.add(vendaProdutoSelecionado);
    }

    public void doStartAddServico() {
        cleanProduto();
        cleanServico();
        cleanOS();
        listaAnexos = new ArrayList<>();
        listVendaServico.add(vendaServicoSelecionado);
    }

    public void doStartAddOS() {
        cleanProduto();
        cleanServico();
        cleanOS();
        listaAnexos = new ArrayList<>();
        listVendaOS.add(itensOrdemDeServicoSelecionado);
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

    public void carregarOS(VendaItemOrdemDeServico vendaItemOrdemDeServico) {
        if (vendaItemOrdemDeServico != null) {
            vendaItemOrdemDeServico.setIdVenda(vendaSelecionada);
            vendaItemOrdemDeServico.setValor(0d);
            vendaItemOrdemDeServico.setDesconto(0d);
            vendaItemOrdemDeServico.setIdVenda(vendaSelecionada);

            vendaService.atualizaTenatIdVendaServico(vendaItemOrdemDeServico);
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

    public void removeOS() {
        listVendaOS.remove(itensOrdemDeServicoSelecionado);
        calcularValorTotalLimparFormaPagamento();
        calcularDescontoTotalLimparFormaPagamento();
    }

    public String getDescontoTotal() {
        // somando os descontos dos produtos da lista
        Double desconto = vendaService.calcularDescontoTotal(listVendaProduto, listVendaServico, listVendaOS, vendaSelecionada.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(desconto);
    }

    public String getSubTotal() {
        // somando o total dos produtos da lista
        Double total = vendaService.calcularValorTotal(listVendaProduto, listVendaServico, listVendaOS, vendaSelecionada.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(total);
    }

    public String getValorTotal() {
        // somando o total dos produtos da lista
        Double desconto = vendaService.calcularDescontoTotal(listVendaProduto, listVendaServico, listVendaOS, vendaSelecionada.getListParcelaDTO());
        Double total = vendaService.calcularValorTotal(listVendaProduto, listVendaServico, listVendaOS, vendaSelecionada.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(total - desconto);
    }

    public Double getDescontoTotalDouble() {
        Double desconto = listVendaOS.stream()
                .map(VendaItemOrdemDeServico::getDesconto)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);
        desconto += itensVendaSelecionados.stream()
                .map(ItemVendaDTO::getDesconto)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);
        return desconto;
    }

    public String getDescontoTotalCard() {
        return "R$ " + super.converterValorParaMonetario(getDescontoTotalDouble());
    }

    public String getSubTotalCard() {
        Double total = listVendaOS.stream()
                .map(VendaItemOrdemDeServico::getValor)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);
        total += itensVendaSelecionados.stream()
                .map(itemDto -> itemDto.getValor() * itemDto.getQuantidade())
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);

        return "R$ " + super.converterValorParaMonetario(total);
    }

    public String getValorTotalCard() {

        Double total = listVendaOS.stream()
                .map(VendaItemOrdemDeServico::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);
        total += itensVendaSelecionados.stream()
                .map(ItemVendaDTO::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);

        return "R$ " + super.converterValorParaMonetario(total);
    }

    public Double getValorT() {
        Double desconto = vendaService.calcularTotalDesconto(itensVendaSelecionados, listVendaOS, vendaSelecionada.getListParcelaDTO());
        Double total = vendaService.calcularTotais(itensVendaSelecionados, listVendaOS, vendaSelecionada.getListParcelaDTO());

        return total - desconto;
    }

    public List<Produto> getProdutosDisponiveis() {
        List<Produto> produtosDisponiveis = produtoService.listar();
        List<Produto> produtosAtribuidos = listVendaProduto.stream().map(VendaProduto::getDadosProduto).map(DadosProduto::getIdProduto).distinct().collect(Collectors.toList());
        if (produtosAtribuidos != null) {
            produtosDisponiveis.removeAll(produtosAtribuidos);
        }
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

    public List<VendaProduto> listVendaProdutoPreenchida(Venda venda) {
        return vendaService.listarVendaProduto(venda);
    }

    public List<VendaServico> listVendaServicoPreenchida(Venda venda) {
        return vendaService.listarVendaServico(venda);
    }

    public List<Object> getDadosAuditoria() {
        return vendaService.getDadosAuditoriaByID(vendaSelecionada);
    }

    public void doToggleAddCliente() {
        this.cadastraCliente = !this.cadastraCliente;
        this.cliente = new Cliente();
    }

    public void doFinishAddCliente() {
        if (cliente.getCpfCNPJ() == null) {
            createFacesErrorMessage("CPF/CNPJ inválido.");
            return;
        }
        try {
            cliente.setTipo(cliente.getCpfCNPJ().length() > 11 ? "PJ" : "PF");
            cliente = clienteService.salvar(cliente);
            vendaSelecionada.setIdCliente(cliente);
            cadastraCliente = false;
            cliente = null;
        } catch (Exception e) {
            createFacesErrorMessage("Não foi possível salvar o cliente.");
        }
    }

    public void doCancelAddCliente() {
        cleanCliente();
    }

    public String getNumeroOrcamento() {
        String n = "";

        if (!listVendaProduto.isEmpty()) {
            n += "P";
        }
        if (!listVendaServico.isEmpty()) {
            n += "S";
        }
        if (!listVendaOS.isEmpty()) {
            n += "O";
        }

        return n + vendaSelecionada.getId();
    }

    public String getLabelTipoOrcamento() {
        switch (tiposSelecionados.size()) {
            case 3:
                return "Todos";
            case 0:
                return "Nenhum";
            default:
                return StringUtil.listaParaTexto(tiposSelecionados.stream()
                        .map(tipo -> EnumTipoVenda.retornaEnumSelecionado(tipo).getDescricao())
                        .collect(Collectors.toList()));
        }
    }

    public List<FormaPagamento> getListFormasDePagamentoPrint() {
        return vendaService.listarVendaFormaPagamento(vendaSelecionada).stream()
                .map(VendaFormaPagamento::getIdFormaPagamento).collect(Collectors.toList());
    }

    public List<FormaPagamento> getListFormasDePagamento() {
        return listFormaDePagamento.stream().map(VendaFormaPagamento::getIdFormaPagamento).collect(Collectors.toList());
    }

    public void removeListaParcelaPorFormaDePagamento() {
        for (int j = listFormaDePagamento.size() - 1; j >= 0; j--) {
            if (listFormaDePagamento.get(j).getIdFormaPagamento().equals(formaDePagamentoSelecionada)) {
                listFormaDePagamento.remove(j);
            }
        }
        formaDePagamentoSelecionada = new FormaPagamento();
        setPageDataTable();
    }

    public void setPageDataTable() {
        final DataGrid d = (DataGrid) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:gridFormaPagamento");
        int first = 0;
        for (VendaFormaPagamento vfp : listFormaDePagamento) {
            if (vfp.getIdFormaPagamento().equals(formaDePagamentoSelecionada)) {
                break;
            }
            ++first;
        }
        if (listFormaDePagamento.size() <= first) {
            first = 0;
        }
        d.setFirst(first);
    }

    public List<ParcelaDTO> getListParcelasPorFormasPagamento() {
        List<ParcelaDTO> lista = new ArrayList<>();
        if (vendaSelecionada.getDataVencimento() == null) {
            listFormaDePagamento.clear();
        }
        listFormaDePagamento.stream()
                .filter(vfp -> vfp.getIdFormaPagamento().equals(formaDePagamentoSelecionada) && vfp.getCondicaoPagamento() != null)
                .findFirst()
                .ifPresent(vfp -> {
                    int quantidade = 1;
                    if (!vfp.getCondicaoPagamento().equals(EnumFormaPagamento.AVISTA.getForma())) {
                        quantidade = Integer.parseInt(vfp.getCondicaoPagamento().replace("x", ""));
                    }
                    if (vfp.getDesconto() == null) {
                        vfp.setDesconto(0d);
                    }

                    Double valorBase = vendaSelecionada.getValor();
                    Double valorParcela = NumeroUtil.formatarCasasDecimais(valorBase / quantidade, 2);
                    for (int i = 0; i < quantidade; i++) {
                        ParcelaDTO parcela = new ParcelaDTO();

                        parcela.setDataVencimento(DataUtil.adicionarMes(vendaSelecionada.getDataVencimento(), i));
                        parcela.setNumParcela(i + 1);
                        parcela.setValor(valorParcela);

                        lista.add(parcela);
                    }
                    boolean isNotTheSame = BigDecimal.valueOf(valorParcela)
                            .multiply(BigDecimal.valueOf(quantidade))
                            .compareTo(BigDecimal.valueOf(valorBase)) != 0;
                    if (isNotTheSame) {
                        valorParcela = NumeroUtil.formatarCasasDecimais(valorBase - (valorParcela * (quantidade - 1)), 2);
                        lista.get(lista.size() - 1).setValor(valorParcela);
                    }
                });

        return lista;
    }

    public boolean hasFormaDePagamento(FormaPagamento forma) {
        return listFormaDePagamento.stream().anyMatch(fdep -> fdep.getIdFormaPagamento().equals(forma));
    }

    public String getDescricaoCondicaoPagamento(String codigo) {
        return EnumFormaPagamento.retornaEnumSelecionado(codigo).getDescricao();
    }

    public Date getDataVencimentoParcela(Integer parcela) {
        return DataUtil.adicionarMesVerficandoUltimoDia(vendaSelecionada.getDataVencimento(), parcela - 1);
    }

    public String getFormasDePagamentoAssociadas(Venda orcamento) {
        return StringUtil.listaParaTexto(vendaService.getFormasDePagamentoAssociadas(orcamento));
    }

    public boolean isCartao(VendaFormaPagamento vfp) {
        String fp = vfp.getIdFormaPagamento().getCodigoNfe();
        return fp != null && (fp.equals(EnumMeioDePagamento.CARTAO_DE_CREDITO.getCodigo())
                || fp.equals(EnumMeioDePagamento.CARTAO_DE_DEBITO.getCodigo()));
    }

    public String getEnumFormaPagamentoDesc(String cod) {
        return EnumFormaPagamento.retornaEnumSelecionado(cod).getDescricao();
    }

    public Double calculaValorComDesconto(Double valor, Double desconto) {
        if (desconto == null || desconto <= 0) {
            return valor;
        }
        return valor - (valor * desconto / 100);
    }

    public String getSituacaoVenda(String sigla) {
        return "icon-" + (controleMenu.isCadastroOk(sigla) ? "like font-blue-madison" : "dislike font-red-mint");
    }

    public void loadItem() {
        for (ProdutoServicoDTO item : itensVendaDisponiveis) {
            try {
                if (Objects.nonNull(itemVendaSelecionado.getIdProdutoServico())
                        && Objects.nonNull(itemVendaSelecionado.getIdProdutoServico().getIdProdutoServico())
                        && item.getIdProdutoServico().equals(itemVendaSelecionado.getIdProdutoServico().getIdProdutoServico())) {
                    itemVendaSelecionado.setIdProdutoServico(item);
                    itemVendaSelecionado.setValor(item.getValor());
                    if (itemVendaSelecionado.getIdProdutoServico().getTipo() == EnumTipoItemVenda.PRODUTO) {
                        estoqueProdutoSelecinado = estoqueService.buscarSaldoDTO(itemVendaSelecionado.getIdProdutoServico());
                    }
                    break;
                }
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addItem() {
        if (adicionaProduto) {
            ProdutoServicoDTO prodServ;
            if (ehProduto) {
                addProduto.setDescricao(addDescricao);
                addProduto.setDataEntrada(new Date());
                addProduto.setIdUnidadeMedida(addUnidade);
                addProduto.setValorCusto(precoCusto);
                addProduto.setValorVenda(precoVenda);
                addProduto.setEstoqueDisponivel(estoqueInicial);
                prodServ = new ProdutoServicoDTO(produtoService.salvar(addProduto));
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
        }
        Double desc = 0d;
        Double valor = 0d;
        for (ItemVendaDTO dto : itensVendaSelecionados) {

            if (dto.getDesconto() == null) {
                dto.setDesconto(0d);
            }
            desc += dto.getDesconto();
            valor += dto.getValorTotal();
        }
        vendaSelecionada.setDesconto(desc);
        vendaSelecionada.setValor(valor);
        calcularDescontoTotalLimparFormaPagamento();
        calcularValorTotalLimparFormaPagamento();
        cleanAddProdCache();
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

    public void exibirImagens() {
        if (vendaSelecionada.getIdDocumento() == null) {
            vendaSelecionada.setIdDocumento(new Documento());
            vendaSelecionada.getIdDocumento().setDocumentoAnexoList(new ArrayList<>());
            vendaSelecionada.getIdDocumento().setTenatID(getUsuarioLogado().getTenat());
        }
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("width", 1200);
        options.put("height", 454);
        showModal("modals/listaImagens.xhtml", options);
    }

    public void setPart(FileUploadEvent event) {
        AnexoDTO anexo = new AnexoDTO();
        try {
            anexo.setConteudo("data:image/png;base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(event.getFile().getInputstream())));
            anexo.setDataEnvio(new Date());
            anexo.setIdUsuarioEnvio(getUsuarioLogado().getId());
            anexo.setMimeType(event.getFile().getContentType());
            anexo.setNome(event.getFile().getFileName());
            listaAnexos.add(anexo);
        } catch (IOException ex) {
            Logger.getLogger(ProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerAnexo(AnexoDTO adto) {
        listaAnexos.remove(adto);
    }

    public StreamedContent baixarImagens() {
        try {
            vendaSelecionada.getIdDocumento().setDocumentoAnexoList(documentoAnexoService.listByDocumento(vendaSelecionada.getIdDocumento()));

            String nome = "imagens orçamento " + getNumeroOrcamento() + ".rar";
            Map<String, byte[]> files = new HashMap<>();
            vendaSelecionada.getIdDocumento().getDocumentoAnexoList()
                    .forEach(da -> files.put(da.getNomeArquivo(), da.readFromFile()));
            return FileUtil.downloadFile(FileUtil.ziparArquivos(files), "application/x-rar-compressed", nome);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public void limpaControle() {
        controleEdicao = null;
        itemVendaSelecionado = new ItemVendaDTO();
        itemVendaSelecionado.setFornecidoTerceiro("N");
        estoqueProdutoSelecinado = 0d;
        cleanAddProdCache();
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
    }

    public Boolean ehOficina(Empresa empresa) {
        return empresa.getIdCategoriaEmpresa() != null && empresa.getIdCategoriaEmpresa().getDescricao().equals("Oficina mecânica");
    }

    public void adicionarProduto() {
        adicionaProduto = adicionaProduto == false;
        addUnidade = unidadeMedidaService.buscarPorSigla("UN");
        addProduto = new Produto();
        addServico = new Servico();
        estoqueInicial = 0d;
        addDescricao = "";
        precoCusto = 0d;
        precoVenda = 0d;

    }

    public void cleanAddProdCache() {
        ehProduto = true;
        addDescricao = null;
        precoCusto = null;
        precoVenda = null;
        estoqueInicial = null;
        addUnidade = null;
        adicionaProduto = false;
    }

    public String removerArquivos() {
        listaAnexos.clear();
        return getPaginaCadastro() + ".xhtml";
    }

    public String removerArquivo(AnexoDTO anexo) {
        if (listaAnexos.contains(anexo)) {
            listaAnexos.remove(anexo);
        }
        return getPaginaCadastro() + ".xhtml";
    }

}
