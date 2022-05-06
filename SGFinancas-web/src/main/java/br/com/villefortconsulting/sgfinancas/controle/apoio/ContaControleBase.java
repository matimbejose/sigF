package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.controle.CadastroControle;
import br.com.villefortconsulting.sgfinancas.controle.ContaReceberControle;
import br.com.villefortconsulting.sgfinancas.controle.IntegracaoBancariaControle;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResumoContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContratoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.ExtratoContaCorrenteService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoBoleto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.PieChartModel;

@Getter
@Setter
public abstract class ContaControleBase extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    protected ContaService contaService;

    @EJB
    protected ContratoService contratoService;

    @EJB
    protected DocumentoService documentoService;

    @EJB
    protected DocumentoAnexoService documentoAnexoService;

    @EJB
    protected ExtratoContaCorrenteService extratoContaCorrenteService;

    @EJB
    protected FuncaoAjudaService funcaoAjudaService;

    @EJB
    private PlanoContaService planoContaService;

    @Inject
    private CadastroControle cadastroControle;

    @Inject
    private IntegracaoBancariaControle integracaoBancariaControle;

    @Inject
    protected DocumentoMapper documentoMapper;

    protected Conta contaSelecionada;

    protected ContaParcela contaParcelaSelecionada;

    protected ContaParcelaParcial contaParcelaParcialSelecionada;

    protected LazyDataModel<ContaParcela> model;

    protected ContaParcelaFiltro filtro = new ContaParcelaFiltro();

    protected Date dataPagamento;

    protected Double valorTotalPago;

    protected transient Part part;

    protected transient List<AnexoDTO> listaAnexos;

    protected transient List<ContaParcela> parcelasEmAberto;

    protected transient List<ContaParcela> parcelasAtribuidas;

    protected transient List<ContaParcela> parcelas;

    protected transient int numParcelaAtual;

    protected String telaAnterior;

    protected HorizontalBarChartModel barModel = new HorizontalBarChartModel();

    protected transient List<ResumoContaParcelaDTO> lista;

    protected String tipoListagem;

    protected PieChartModel pieModel2 = new PieChartModel();

    protected CardContaDTO cardAtraso;

    protected CardContaDTO cardReceber;

    protected CardContaDTO cardRecebido;

    protected CardContaDTO cardTotal;

    protected String codigoDeBarrasLeitora;

    protected String boletoDigitavel;

    protected String tipoBoleto;

    protected String informarCodigoBarra = "N";

    protected Double valorRecebido;

    protected boolean outrosCustos;

    protected ImportacaoContaDTO dtoCadastro;

    public abstract EnumConta getTipoConta();

    public abstract void preencherEstatisticaDTO();

    public abstract List<Conta> getListaConta();

    public String ultimaParcelaPaga(Conta conta) {
        try {
            List<ContaParcela> listaParcela = contaService.listarContaParcela(conta);
            Date data = null;
            boolean chegouEmUmaParcelaNaoPaga = false;
            boolean pulouParcelas = false;
            for (ContaParcela cp : listaParcela) {
                if (cp.getDataPagamento() == null) {
                    chegouEmUmaParcelaNaoPaga = true;
                } else {
                    data = cp.getDataPagamento();
                    if (chegouEmUmaParcelaNaoPaga) {
                        pulouParcelas = true;
                    }
                }
            }
            if (data != null) {
                return DataUtil.dateToString(data) + (pulouParcelas ? "*" : "");
            }
        } catch (Exception ex) {
            Logger.getLogger(ContaReceberControle.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public void iniciaFiltroPeriodo() {
        if (filtro == null) {
            filtro = new ContaParcelaFiltro();
        }
        if (filtro.getDataInicio() == null) {
            filtro.setDataInicio(DataUtil.getPrimeiroDiaDaSemana());
        }
        if (filtro.getDataFim() == null) {
            filtro.setDataFim(DataUtil.getUltimoDiaDaSemana());
        }
    }

    public String trataObservacao(ContaParcela contaParcela) {
        String obs = contaParcela.getObservacao() == null ? "" : contaParcela.getObservacao();

        if (obs.length() >= 100) {
            obs = obs.substring(0, 100) + "...";
        }

        if (contaParcela.getIdConta().getObservacao() != null && (contaParcela.getObservacao() == null || !contaParcela.getObservacao().equals(contaParcela.getIdConta().getObservacao()))) {
            if (!obs.isEmpty()) {
                obs += " - ";
            }
            if (contaParcela.getIdConta().getObservacao().length() > 100) {
                obs += contaParcela.getIdConta().getObservacao().substring(0, 100) + "...";
            } else {
                obs += contaParcela.getIdConta().getObservacao();
            }
        }

        return obs;
    }

    public String obterTamanhoGrafico() {
        return (54 + lista.size() * 30) + "px";
    }

    public String getListaAsJson() {
        return new Gson().toJson(lista);
    }

    public void setListaAsJson(String s) {
        // Setter para evitar erro no xhtml
    }

    public String doMudarListagemParaListagemGeral() {
        filtro.setTipoListagem(null);
        tipoListagem = null;
        return doMudarListagem();
    }

    public List<Object> getDadosAuditoria() {
        return contaService.getDadosAuditoriaByID(contaSelecionada);
    }

    public List<Object> getDadosAuditoriaContaParcela() {
        return contaService.getDadosAuditoriaContaParcelaByID(contaParcelaSelecionada);
    }

    public String buscarSituacao(String situacao) {
        if (getTipoConta().getTipoTransacao() == EnumTipoTransacao.PAGAR) {
            return EnumSituacaoConta.getDescricao(situacao);
        }
        return EnumSituacaoConta.getDescricaoReceber(situacao);
    }

    public boolean possuiOutrasParcelasEmAberto() {
        return contaService.possuiOutrasParcelasEmAberto(contaParcelaSelecionada);
    }

    public void deletarAnexo() {
        contaSelecionada.setIdDocumento(null);
        part = null;
    }

    public boolean possuiParcelasEmAberto() {
        return parcelas != null && parcelas.stream().anyMatch(contaParcela -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(contaParcela.getSituacao()));
    }

    public void preencherParcelasEmAberto() {
        parcelasEmAberto = contaService.listarParcelasEmAberto(contaParcelaSelecionada);
    }

    public String doStartCancelarParcela() {
        parcelasEmAberto = new LinkedList<>();
        return "cancelarParcela.xhtml";
    }

    public String doFinishCancelarParcela() {
        contaService.cancelarContaParcela(contaParcelaSelecionada, parcelasAtribuidas);
        return "listaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public void doFinishReativarParcela() {
        contaService.reativarContaParcela(contaParcelaSelecionada, parcelasAtribuidas);
        createFacesSuccessMessage("Parcela reativada com sucesso!");
    }

    public String doFinishExcluir() {
        try {
            contaService.remover(contaSelecionada);
            createFacesSuccessMessage("Conta removida com sucesso.");
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "listaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public void addParcela() {
        contaSelecionada.setNumeroParcelas(contaSelecionada.getNumeroParcelas() + 1);
        parcelas = contaService.alterarParcela(contaSelecionada, parcelas, null, null);
        contaSelecionada.setContaParcelaList(parcelas);
    }

    public void removerParcela(ContaParcela parcela) {
        contaSelecionada.setNumeroParcelas(contaSelecionada.getNumeroParcelas() - 1);
        parcelas = contaService.alterarParcela(contaSelecionada, parcelas, parcela, null);
        contaSelecionada.setContaParcelaList(parcelas);
    }

    public String alterarContaPorListagem() {
        telaAnterior = "listaContaReceber";
        return doStartAlterarConta();
    }

    public void calcularTributosTotais(ContaParcela parcela) {
        contaParcelaSelecionada = contaService.preencherTotalMaisTributosMaisRestanteParcela(parcela, null);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public void createPieModel2() {
        pieModel2 = new PieChartModel();

        pieModel2.setDatatipFormat("%s - %.2f");

        pieModel2.set(getTipoConta().getCardFuturo(), cardReceber.getValor());
        pieModel2.set(getTipoConta().getCardAtrasado(), cardAtraso.getValor());
        pieModel2.set(getTipoConta().getCardPassado(), cardRecebido.getValor());

        pieModel2.setTitle(getTipoConta().getCardTotal());
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
        pieModel2.setShadow(false);
    }

    public String doMudarListagem() {
        preencherEstatisticaDTO();
        // Deixar o caminho completo pois esse link é utilizado em duas páginas
        return "/restrito/conta" + getTipoConta().getUrl() + "/listaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public void carregaGrafico() {
        barModel.clear();
        barModel.setExtender("gambiarra");
        barModel.setTitle("Distribuição de " + getTipoConta().getGenitivo());
        ChartSeries conta = new ChartSeries();
        conta.setLabel("Plano de conta");

        lista = contaService.obterContaParcelaResumo(filtro, tipoListagem);

        lista.forEach(dto -> conta.set(dto.getDescricao(), dto.getValor()));
        barModel.addSeries(conta);
    }

    public void updateValorRecebido() {
        if ("N".equals(contaParcelaSelecionada.getPagamentoParcial())) {
            valorRecebido = contaParcelaSelecionada.getValor();
        } else {
            this.valorRecebido = (valorTotalPago == null) ? null : contaParcelaSelecionada.getValor() - valorTotalPago;
        }
    }

    public String doStartAlterarConta() {
        this.valorRecebido = null;
        this.outrosCustos = false;
        this.part = null;
        this.numParcelaAtual = this.model.getRowData().getNumParcela();
        this.parcelas = contaService.listarContaParcela(contaSelecionada);
        parcelas.forEach(parcela -> {
            calcularTributosTotais(parcela);
        });
        listaAnexos = new ArrayList<>();
        if (contaSelecionada.getIdDocumento() != null && !contaSelecionada.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = contaSelecionada.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return "/restrito/conta" + getTipoConta().getUrl() + "/alterarConta" + getTipoConta().getUrl() + ".xhtml?faces-redirect=true";
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CONTA_RECEBER.getChave()).getDescricao());
        return "cadastroConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public void cancelarbaixaParcela() {
        contaSelecionada.setValorPago(0d);
        contaParcelaSelecionada.setValorPago(null);
        contaParcelaSelecionada.setDataPagamento(null);
        contaParcelaSelecionada.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        contaParcelaSelecionada.setCanceladoAgora(true);
        parcelas.stream()
                .filter(cp -> cp.equals(contaParcelaSelecionada))
                .findAny().ifPresent(cp -> {
                    cp.setDataPagamento(null);
                    cp.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                });
        valorRecebido = 0d;
    }

    public String doFinishAdd() {
        try {
            if (valorRecebido != null && (contaSelecionada.getValor() > 0d && valorRecebido > contaSelecionada.getValor() || contaSelecionada.getValor() < 0d && valorRecebido < contaSelecionada.getValor())) {
                createFacesErrorMessage("Valor pago não pode ser superior o valor da conta");
                return "";
            }
            if (contaSelecionada.getDataEmissao() == null) {
                contaSelecionada.setDataEmissao(DataUtil.getHoje());
            }
            if (contaSelecionada.getId() != null) {
                parcelas = contaService.verificarPagamentoParcelas(parcelas);
                contaSelecionada.setContaParcelaList(parcelas);
            }
            if (contaSelecionada.getContaParcelaList() != null && !contaSelecionada.getContaParcelaList().isEmpty()) {
                MessageListException.throwIfNotEmpty(contaSelecionada.getContaParcelaList().stream()
                        .filter(cp -> cp.getDataEmissao() != null && cp.getDataVencimento() != null)
                        .filter(cp -> cp.getDataEmissao().after(cp.getDataVencimento()))
                        .map(cp -> "A parcela " + cp.getNumParcela() + " possui a data de emissão após a data de vencimento.")
                        .collect(Collectors.toList()));
                contaSelecionada.setDataEmissao(contaSelecionada.getContaParcelaList().get(0).getDataEmissao());
            }

            Documento removerDocumento = null;
            if (!listaAnexos.isEmpty()) {
                Documento doc;
                if (contaSelecionada.getIdDocumento() == null) {
                    doc = documentoService.criarDocumento(getUsuarioLogado(), "Anexos da conta");
                } else {
                    doc = documentoService.buscar(contaSelecionada.getIdDocumento().getId());
                }
                doc = documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), listaAnexos);
                contaSelecionada.setIdDocumento(doc);
            } else if (contaSelecionada.getIdDocumento() != null) {
                removerDocumento = contaSelecionada.getIdDocumento();
                contaSelecionada.setIdDocumento(null);
            }
            contaService.salvarConta(contaSelecionada, dataPagamento, valorRecebido);
            if (removerDocumento != null) {
                documentoService.remover(removerDocumento);
            }
            extratoContaCorrenteService.verificarAtualizarExtratoParcelas(parcelas);
            createFacesSuccessMessage("Conta salva com sucesso!");

            cleanCache();
            if ("contaPorCliente".equals(telaAnterior)) {
                return "/restrito/cliente/contaPorCliente.xhtml?faces-redirect=true";
            } else if ("cociliacao".equals(telaAnterior)) {
                return integracaoBancariaControle.vinculaContaCriada(contaSelecionada);
            }
            preencherEstatisticaDTO();
            return "listaConta" + getTipoConta().getUrl() + ".xhtml";
        } catch (MessageListException ex) {
            ex.getMessages().forEach(this::createFacesErrorMessage);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
        }
        return "";
    }

    public void cleanCodigoBarras() {
        this.informarCodigoBarra = "N";
        this.boletoDigitavel = null;
        this.tipoBoleto = null;
        this.codigoDeBarrasLeitora = null;
    }

    public String desfazerBaixa() {
        try {
            contaService.desfazerBaixa(contaParcelaSelecionada);
            createFacesSuccessMessage("Baixa cancelada com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public String doStartAddParcela() {
        this.valorTotalPago = contaService.somaTotalPago(contaParcelaSelecionada);
        this.valorRecebido = (valorTotalPago == null) ? null : NumeroUtil.formatarCasasDecimais(contaParcelaSelecionada.getValor() - valorTotalPago, 2);

        preencherTotalMaisTributosMaisRestanteParcela();

        return "/restrito/conta" + getTipoConta().getUrl() + "/" + getTipoConta().getInfinitivo() + "Parcela.xhtml";
    }

    public boolean preencherTotalMaisTributosMaisRestanteParcela() {
        try {
            contaService.preencherTotalMaisTributosMaisRestanteParcela(contaParcelaSelecionada, null);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String doFinishAddParcelaParcial() {
        try {
            contaService.pagarParcela(contaParcelaSelecionada, valorRecebido);
            createFacesSuccessMessage("Parcela recebida com sucesso!");
            preencherEstatisticaDTO();
        } catch (ContaException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "listaConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public String doShowHistorico() {
        return "listaHistoricoParcela.xhtml";
    }

    public void doAlteraContaPorCodigo() {
        try {
            if (codigoDeBarrasLeitora == null || tipoBoleto == null) {
                createFacesErrorMessage("Falha ao ler o código de barra");
                return;
            }
            if (EnumTipoBoleto.BOLETO_SERVICO.getTipo().equals(tipoBoleto)) {
                if (codigoDeBarrasLeitora.length() != 44) {
                    contaSelecionada = contaService.contaPorCodigoBarraServico(codigoDeBarrasLeitora, contaSelecionada);
                } else {
                    codigoDeBarrasLeitora = contaService.calculaCodigoBoletoServico(codigoDeBarrasLeitora);
                    contaSelecionada = contaService.contaPorCodigoBarraServico(codigoDeBarrasLeitora, contaSelecionada);
                }
            } else if (EnumTipoBoleto.BOLETO_CONCESSIONARIA.getTipo().equals(tipoBoleto)) {
                if (codigoDeBarrasLeitora.length() != 44) {
                    contaSelecionada = contaService.contaPorCodigoBarraConcessionaria(codigoDeBarrasLeitora, contaSelecionada);
                } else {
                    codigoDeBarrasLeitora = contaService.calculaCodigoBoletoConcessionaria(codigoDeBarrasLeitora);
                    contaSelecionada = contaService.contaPorCodigoBarraConcessionaria(codigoDeBarrasLeitora, contaSelecionada);
                }
            }
            createFacesSuccessMessage("Código de barras lido com sucesso.");
        } catch (Exception e) {
            createFacesErrorMessage("Falha ao ler o código de barra.");
        }
    }

    public void updateQteParcelas() {
        if (!Boolean.TRUE.equals(contaSelecionada.getNaoPodeSelecionarQteParcelas())) {
            contaSelecionada.setQtdRepeticao(1);
        }
    }

    public String mostraOrigem(Conta conta) {
        //Retorna o tipo de objeto que contém na conta.
        return EnumTipoConta.getDescricao(conta.getTipoConta());
    }

    public String doStartAdd() {
        telaAnterior = "listaConta";
        cleanCodigoBarras();
        listaAnexos = new ArrayList<>();
        this.contaSelecionada = new Conta(getTipoConta().getTipoTransacao().getTipo());
        this.contaParcelaSelecionada = new ContaParcela();
        this.valorRecebido = null;
        this.dataPagamento = null;
        this.part = null;
        this.parcelas = null;

        String tipo = getTipoConta().getInfinitivo().substring(0, 1).toUpperCase() + getTipoConta().getInfinitivo().substring(1);

        return "/restrito/conta" + tipo + "/cadastroConta" + getTipoConta().getUrl() + ".xhtml";
    }

    public String getCancelAction() {
        switch (telaAnterior) {
            case "listaContaReceber":
                return "/restrito/conta" + getTipoConta().getUrl() + "/listaConta" + getTipoConta().getUrl() + ".xhtml?faces-redirect=true";
            case "contaPorCliente":
                return "/restrito/cliente/contaPorCliente.xhtml?faces-redirect=true";
            case "telaGeral":
                return "/restrito/financeiro/geral.xhtml?faces-redirect=true";
            default:
                return "/restrito/conta" + getTipoConta().getUrl() + "/listaConta" + getTipoConta().getUrl() + ".xhtml?faces-redirect=true";
        }
    }

    public String doShowReceberParcela() {
        return "rebecerParcela.xhtml";
    }

    public void doInformarCodigoBarra() {
        informarCodigoBarra = informarCodigoBarra.equals("S") ? "N" : "S";
    }

    public void calcularImpostos() {

    }

    public void calcularTributosTotais() {
        calcularImpostos();
        contaParcelaSelecionada = contaService.preencherTotalMaisTributosMaisRestanteParcela(contaParcelaSelecionada, null);
    }

    public String doShowContaPorCodigo() {
        codigoDeBarrasLeitora = null;
        boletoDigitavel = null;
        tipoBoleto = null;

        return "novaContaPorCodeBar.xhtml";
    }

    public void baixarParcela() {
        if (contaParcelaSelecionada.getIdFormaPagamento() == null) {
            createFacesErrorMessage("Selecione a forma de pagamento.");
            return;
        }
        // Funciona apenas com pagamento integral
        calcularTributosTotais(contaParcelaSelecionada);
        contaParcelaSelecionada.setValorPago(contaParcelaSelecionada.getValorTotal());
        contaSelecionada.setValorPago(parcelas.stream()
                .map(ContaParcela::getValorPago)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b));
        if (contaParcelaSelecionada.getDataPagamento() == null) {
            contaParcelaSelecionada.setDataPagamento(DataUtil.getHoje());
        }
        contaParcelaSelecionada.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
    }

    public void updateListaParcelas() {
        if (contaSelecionada.getValor() == null || contaSelecionada.getDataVencimento() == null) {
            return;
        }
        final Double valor = contaSelecionada.getValor() / contaSelecionada.getNumeroParcelas();
        contaSelecionada.getListaValorParcela().clear();
        while (contaSelecionada.getNumeroParcelas() < contaSelecionada.getListaValorParcela().size()) {
            contaSelecionada.getListaValorParcela().remove(contaSelecionada.getNumeroParcelas().intValue());
        }
        while (contaSelecionada.getNumeroParcelas() > contaSelecionada.getListaValorParcela().size()) {
            int i = contaSelecionada.getListaValorParcela().size();
            contaSelecionada.getListaValorParcela().add(new ValorParcelaDTO(i + 1,
                    valor,
                    contaService.getDataVencimentoParaParcela(contaSelecionada, i + 1),
                    false));
        }
        contaSelecionada.getListaValorParcela().forEach(dto -> dto.reset(valor));
        Collections.sort(contaSelecionada.getListaValorParcela(), (a, b) -> a.getNumero().compareTo(b.getNumero()));
    }

    public List<PlanoConta> getPlanoConta() {
        if (getTipoConta() == EnumConta.RECEBER) {
            return planoContaService.listarPlanosContaParaTransacoesContaReceber();
        } else {
            return planoContaService.listarPlanosContaParaTransacoesCompra();
        }
    }

    @Getter
    @AllArgsConstructor
    public enum EnumConta {
        PAGAR("A pagar", "Em atraso", "Pago", "Total", "pagamento", "pagar", "paga", EnumTipoTransacao.PAGAR),
        RECEBER("A receber", "Em atraso", "Recebido", "Total", "recebimento", "receber", "recebida", EnumTipoTransacao.RECEBER);

        protected final String cardFuturo;

        protected final String cardAtrasado;

        protected final String cardPassado;

        protected final String cardTotal;

        protected final String genitivo;

        protected final String infinitivo;

        protected final String indicativoPassado;

        protected final EnumTipoTransacao tipoTransacao;

        public String getUrl() {
            return infinitivo.substring(0, 1).toUpperCase() + infinitivo.substring(1);
        }

    }

    public int buscarContaPorContrato(int numContrato) {
        Conta conta = contratoService.buscarContaPorContrato(numContrato);
        return conta.getId();

    }

    @Override
    public void limparOrdenacao() {
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent("collectorDataTable");
        table.setValueExpression("sortBy", null);
    }

    public Boolean getPodeEditarValor() {
        return !("S".equals(contaSelecionada.getAdvemContrato()) && contratoService.possuiListaServico(contaSelecionada.getIdContrato()));
    }

    public void abrirOutrosCustos() {
        outrosCustos = outrosCustos == false;
    }

    public String removerArquivos() {
        listaAnexos.clear();
        return "cadastroConta" + getTipoConta().getInfinitivo() + ".xhtml";
    }

    public String removerArquivo(AnexoDTO anexo) {
        if (listaAnexos.contains(anexo)) {
            listaAnexos.remove(anexo);
        }
        return "cadastroConta" + getTipoConta().getInfinitivo() + ".xhtml";
    }

    public String listAnexos(Conta conta) {
        contaSelecionada = conta;
        listaAnexos = new ArrayList<>();
        if (contaSelecionada.getIdDocumento() != null && !contaSelecionada.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = contaSelecionada.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return "listAnexos" + getTipoConta().getInfinitivo() + ".xhtml";
    }

    public void setPart(FileUploadEvent event) {
        AnexoDTO anexo = new AnexoDTO();
        try {
            anexo.setConteudo("data:" + event.getFile().getContentType() + ";base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(event.getFile().getInputstream())));
            anexo.setDataEnvio(new Date());
            anexo.setIdUsuarioEnvio(getUsuarioLogado().getId());
            anexo.setNomeUsuarioEnvio(getUsuarioLogado().getNome());
            anexo.setMimeType(event.getFile().getContentType());
            anexo.setNome(event.getFile().getFileName());
            listaAnexos.add(anexo);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerAnexo(AnexoDTO adto) {
        listaAnexos.remove(adto);
    }

    public Boolean getCampoPagamentoDisabled() {
        return contaSelecionada.getValor() == null || contaSelecionada.getDataVencimento() == null
                || contaSelecionada.getIdPlanoConta() == null || contaSelecionada.getIdContaBancaria() == null;
    }

    public Boolean getPodeEditarDados() {
        return parcelas != null && parcelas.stream()
                .noneMatch(cp -> cp.getDataPagamento() != null);
    }

    public String doStartAddContaConciliacao(TransacaoIntegracaoBancaria tib) {
        String path = doStartAdd();
        telaAnterior = "cociliacao";
        this.valorRecebido = Math.abs(tib.getValor());
        this.dataPagamento = tib.getData();
        return path;
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) throws MessageListException {
        return contaService.importDto(obj, getTipoConta().getTipoTransacao().getTipo(), getUsuarioLogado().getTenat());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CONTA_" + (getTipoConta().getTipoTransacao().name()) + "_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Conta a " + getTipoConta().getInfinitivo(),
                    contaService.hasAny(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    ImportacaoContaDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new ImportacaoContaDTO();
    }

}
