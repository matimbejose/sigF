package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.XmlElementSignatureController;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValidacaoNFSeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NotaFiscalServicoFiltro;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.CtissService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFSService;
import br.com.villefortconsulting.sgfinancas.servicos.NotaServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumNaturezaOperacaoNFS;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNota;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumAmbienteEmissaoNF;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotaFiscalServicoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private NFSService nfsService;

    @EJB
    private UFService ufService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private CtissService ctissService;

    @EJB
    private NotaServicoService notaServicoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    @EJB
    private RelatorioService relatorioService;

    @Inject
    private ContaReceberControle contaReceberControle;

    private Venda vendaSelecionada;

    private ContaParcela parcelaSelecionada;

    private NFS nfsSelecionado;

    private LazyDataModel<NFS> model;

    private String cancNota;

    private ValidacaoNFSeDTO validacaoNFSeDTO;

    private NotaFiscalServicoFiltro filtro = new NotaFiscalServicoFiltro();

    private String certificateField;

    private String novaNotaServico;

    private String signatureField;

    private String signatureFieldInfRps;

    private String signatureFieldLoteRps;

    private String xmlEnvio;

    private String hashInfRps;

    private String hashLoteRps;

    private boolean faseTransmissao = false;

    private boolean preenchidoComNotaExistente;

    private String numeroNotaFiscalFormatado;

    private boolean novaNf;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, nfsService::quantidadeRegistrosFiltrados, nfsService::getListaFiltrada);
        filtro.setData(new MinMax<>());
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    @Override
    public void cleanCache() {
        this.nfsSelecionado = new NFS();
        vendaSelecionada = null;
        parcelaSelecionada = null;
    }

    public Set<Map.Entry<String, String>> listarRegimeTributacao() {
        return EnumRegimeTributacao.listarRegimeTributacao();
    }

    public Set<Map.Entry<String, String>> listarNaturezaOperacao() {
        return EnumNaturezaOperacaoNFS.listarNaturezaOperacao();
    }

    public List<Cidade> listarMunicipios() {
        return cidadeService.listarMunicipiosNFS();
    }

    public List<Ctiss> listarCtiss() {
        if (nfsSelecionado.getIdMunicipioISSQN() != null) {
            return ctissService.listarCtissRelacionadoCnaeEmpresa(empresaService.getEmpresa(), nfsSelecionado.getIdMunicipioISSQN());
        }
        return new ArrayList<>();
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_NOTA_FISCAL_SERVICO.getChave()).getDescricao());
        return "cadastroNotaFiscalServico.xhtml";
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (nfsSelecionado.getIdUFCliente() != null) {
            return cidadeService.listar(nfsSelecionado.getIdUFCliente());
        }
        return new LinkedList<>();
    }

    public void preencherDescricaoCidade() {
        if (nfsSelecionado.getIdCidadeCliente() != null) {
            nfsSelecionado.setCidadeCliente(nfsSelecionado.getIdCidadeCliente().getDescricao());
            nfsSelecionado.setUfCliente(nfsSelecionado.getIdCidadeCliente().getIdUF().getDescricao());
        }
    }

    public void preencherCliente() {
        nfsSelecionado = nfsService.preencherNfsPorCliente(nfsSelecionado);
    }

    public String chaveFormatada() {
        return nfsSelecionado.getNumeroNotaFiscalFormatada();
    }

    public StreamedContent gerarNotaServico() {
        return gerarNotaServico(nfsSelecionado);
    }

    public StreamedContent gerarNotaServico(NFS nfs) {
        try {
            Empresa empresa = empresaService.getEmpresa();
            return notaServicoService.gerarNotaFiscalServico(empresa, nfs);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }

        return null;
    }

    public void preencherNotaComNotaExistente() {
        try {
            nfsSelecionado = nfsService.preencherNotaComNotaExistente(nfsSelecionado, numeroNotaFiscalFormatado);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }
    }

    public String enviarXmlPorEmail() {
        try {
            if (StringUtils.isEmpty(nfsSelecionado.getIdCliente().getEmail())) {
                createFacesErrorMessage("Email não informado para o cliente");
                return "visualizarNotaFiscalServicoEmitida.xhtml";
            }
            nfsService.enviarXmlPorEmail(nfsSelecionado, empresaService.getEmpresa());
            createFacesSuccessMessage("Arquivo Xml enviado com sucesso! Email de envio: " + nfsSelecionado.getIdCliente().getEmail());
            return "visualizarNotaFiscalServicoEmitida.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "visualizarNotaFiscalServicoEmitida.xhtml";
        }
    }

    public String transmitirNFS() {
        try {
            Empresa empresa = empresaService.getEmpresa();
            String ambiente = parametroSistemaService.getAmbienteNotaFiscalProduto();
            xmlEnvio = nfsService.gerarXmlEnvioNfse(nfsSelecionado, ambiente);
            String xmlAssinado = XmlUtil.removeAcentos(xmlEnvio);

            if (empresa.getTipoCertificadoDigital() != null) {
                if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                    return doStartAssinarNfs();
                }
                xmlAssinado = nfsService.assinarXmlA1(empresa, ambiente, xmlEnvio);
            } else if (!"MEI".equals(empresa.getPorte())) {
                throw new CadastroException("Não foi informado o certificado digital e a empresa não foi cadastrada como MEI.", null);
            }

            return transmitirNfse(xmlAssinado);
        } catch (NfeException | CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalServico.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            createFacesErrorMessage("Ocorreu um erro ao emitir a nota");
            return "listaNotaFiscalServico.xhtml";

        }
    }

    public void cleanSignature() {
        this.certificateField = null;
        this.signatureFieldInfRps = null;
        this.signatureFieldLoteRps = null;
        this.hashInfRps = null;
        this.hashLoteRps = null;
        this.faseTransmissao = false;
    }

    public String getHashCancelamento() {
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio));
            return XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.CANCELAMENTO_SERVICO.getTipo());
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return null;
        }
    }

    public String doStartAssinarNfs() {
        try {
            cleanSignature();

            // Gerar hash InfRps
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio));
            hashInfRps = XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.SERVICO.getTipo());
            xmlEnvio = new String(arquivo, StandardCharsets.UTF_8);

            return "assinarNotaFiscalServicoA3.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaNotaFiscalServico.xhtml";
        }
    }

    public String doFinishAssinarNfsFase1() {
        try {
            xmlEnvio = XmlElementSignatureController.assinarXmlNotaFiscalServicoA3Fase1(
                    certificateField,
                    signatureFieldInfRps,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio)));

            // Gerar hash InfRps
            byte[] arquivo = FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio));

            hashLoteRps = XmlElementSignatureController.gerarHash(arquivo, EnumTipoNota.SERVICO_LOTE.getTipo());
            xmlEnvio = new String(arquivo, StandardCharsets.UTF_8);
            faseTransmissao = true;

            return "assinarNotaFiscalServicoA3.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "assinarNotaFiscalServicoA3.xhtml";
        }
    }

    public String doFinishAssinarNfsFase2() {
        try {
            String xmlAssinado = XmlElementSignatureController.assinarXmlNotaFiscalServicoA3Fase2(
                    certificateField,
                    signatureFieldLoteRps,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio)));

            return transmitirNfse(xmlAssinado);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "assinarNotaFiscalServicoA3.xhtml";
        }
    }

    public String transmitirNfse(String xmlAssinado) {
        try {
            nfsService.transmitirNfse(nfsSelecionado, xmlAssinado);

            createFacesSuccessMessage("Nota fiscal em processamento!");
            vendaSelecionada = null;
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaNotaFiscalServico.xhtml";
    }

    public String assinarCancelarXml() {
        try {
            if (certificateField == null) {
                createFacesErrorMessage("Informe o certificado");
                return "assinarNotaFiscalServicoA3.xhtml";
            }

            String xmlAssinado = XmlElementSignatureController.obterXmlCancelamentoNotaFiscalServicoA3(
                    certificateField,
                    signatureField,
                    FileUtil.convertFileToBytes(FileUtil.createTxtFile("nfs", ".xml", xmlEnvio)));

            return cancelarNfse(xmlAssinado);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "assinarNotaFiscalServicoA3.xhtml";
        }
    }

    public String doFinishCancelar() {
        try {
            Empresa empresa = empresaService.getEmpresa();
            String ambiente = parametroSistemaService.getAmbienteNotaFiscalServico();

            xmlEnvio = nfsService.obterXmlCancelamento(nfsSelecionado, ambiente);

            if ("A3".equals(empresa.getTipoCertificadoDigital())) {
                return "cancelarNotaFiscalServicoA3.xhtml";
            }

            xmlEnvio = nfsService.assinarXmlCancelamentoNfs(empresa, ambiente, xmlEnvio);
            return cancelarNfse(xmlEnvio);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "visualizarNotaFiscalServicoEmitida.xhtml";
        }
    }

    public String cancelarNfse(String xmlAssinado) {
        try {
            nfsService.cancelarNfse(empresaService.getEmpresa(), parametroSistemaService.getAmbienteNotaFiscalServico(), nfsSelecionado, xmlAssinado);

            createFacesSuccessMessage("Nota fiscal em processamento!");
            vendaSelecionada = null;

            return "listaNotaFiscalServico.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "visualizarNotaFiscalServicoEmitida.xhtml";
        }
    }

    public StreamedContent downloadXmlArmazenamento(NFS nfs) {
        try {
            String nomeNota = "nfse_" + nfs.getNumeroNotaFiscal();

            return FileUtil.downloadFile(FileUtil.convertFileToBytes(FileUtil
                    .createTxtFile(nomeNota, ".xml", nfs.getSituacao().equals(EnumSituacaoNF.ENVIADA.getSituacao()) ? nfs.getXmlArmazenamento() : nfs.getXmlArmazenamentoCancelamento())), "application/xml", nomeNota + ".xml");
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public StreamedContent downloadXmls(NFS nfs) {
        try {
            String nomeNota = "nfse_" + nfs.getNumeroNotaFiscal() + ".rar";
            return FileUtil.downloadFile(nfsService.compactarXmlsNFS(nfs), "application/x-rar-compressed", nomeNota);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public String doStartCancelar() {
        cancNota = "S";
        nfsSelecionado.setCancelarParcelas("S");
        return "visualizarNotaFiscalServicoEmitida.xhtml";
    }

    public String doStartVisualizar() {
        cancNota = "N";
        return "visualizarNotaFiscalServicoEmitida.xhtml";
    }

    public String consultaSituacaoAmbiente() {
        if (parametroSistemaService.getAmbienteNotaFiscalServico().equals(EnumAmbienteEmissaoNF.PRODUCAO.getTipo())) {
            return "Ambiente de produção";
        } else {
            return "Ambiente de homologação";
        }
    }

    public String doStartAdd() {

        try {
            preenchidoComNotaExistente = false;
            novaNf = false;
            if (validarConfiguracaoEmissaoNFS()) {
                return "/restrito/notaFiscalServico/informacao.xhtml?faces-redirect=true";
            }

            if (vendaSelecionada != null) {
                nfsSelecionado = nfsService.preencherNotaPorVenda(vendaSelecionada);
            } else if (parcelaSelecionada != null) {
                nfsSelecionado = nfsService.preencherNotaPorParcela(parcelaSelecionada);
            } else {
                nfsSelecionado = new NFS();
                limparPreenchimentoPorNotaExistente();
            }

            nfsSelecionado.setDataEmissao(DataUtil.getHoje());

            return "/restrito/notaFiscalServico/cadastroNotaFiscalServico.xhtml?faces-redirect=true";
        } catch (Exception ex) {
            createFacesSuccessMessage(ex.getMessage());
            return "listaNotaFiscalServico.xhtml";
        }
    }

    public String doStartAdicionarNovaNota() {
        novaNf = true;
        cleanCache();
        return "/restrito/notaFiscalServico/cadastroNotaFiscalServico.xhtml?faces-redirect=true";
    }

    public void limparPreenchimentoPorNotaExistente() {
        preenchidoComNotaExistente = false;
        numeroNotaFiscalFormatado = "";
    }

    private boolean validarConfiguracaoEmissaoNFS() {
        validacaoNFSeDTO = nfsService.empresaAptaEmitirNFS(parametroSistemaService.getAmbienteNotaFiscalServico(), empresaService.getEmpresa());
        return !validacaoNFSeDTO.isTodosOK();
    }

    public String doStartAlterar() {
        limparPreenchimentoPorNotaExistente();
        return "cadastroNotaFiscalServico.xhtml";
    }

    public String doFinishAdd() {
        List<ContaParcela> listaCP = nfsSelecionado.getContaParcelaList();
        nfsSelecionado.setContaParcelaList(null);
        NFS nfs = nfsService.salvarNotaFiscalServico(nfsSelecionado);
        if (listaCP != null) {
            listaCP.forEach(cp -> {
                cp.setIdNFS(nfs);
                contaService.salvarContaParcela(cp);
            });
        }

        if (parcelaSelecionada != null) {
            parcelaSelecionada.setIdNFS(nfs);
            contaService.salvarContaParcela(parcelaSelecionada);
        }

        createFacesSuccessMessage("Nota fiscal de serviço salva com sucesso!");
        return "listaNotaFiscalServico.xhtml";
    }

    public String doFinishExcluir() {
        nfsSelecionado.getContaParcelaList()
                .forEach(cp -> {
                    cp.setIdNFS(null);
                    contaService.salvarContaParcela(cp);
                });
        nfsService.remover(nfsSelecionado);
        return "listaNotaFiscalServico.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaNotaFiscalServico.xhtml";
    }

    public void calculaValoresRetencao() {
        nfsSelecionado = nfsService.preencherNfsPorCliente(nfsSelecionado);
    }

    public List<Object> getDadosAuditoria() {
        return nfsService.getDadosAuditoriaByID(nfsSelecionado);
    }

    public String buscarSituacaoNota(NFS nfs) {
        if (nfs.getSituacao() != null) {
            return EnumSituacaoNF.getDescricao(nfs.getSituacao());
        }
        return "";
    }

    public void doShowParcelaVinculada() {
        contaReceberControle.setContaParcelaSelecionada(contaService.buscarParcelaPorNFS(nfsSelecionado));
        contaReceberControle.setContaSelecionada(contaReceberControle.getContaParcelaSelecionada().getIdConta());
        contaReceberControle.alterarContaPorListagem();
        PrimeFaces.current().executeScript("window.open('/restrito/contaReceber/alterarContaReceber.xhtml', '_BLANK');");
    }

    public void gerarRelatorio() {
        try {
            if (filtro.getData() == null) {
                postConstruct();
            }
            ContaParcelaFiltro filtroConta = new ContaParcelaFiltro();
            filtroConta.setDataInicio(filtro.getData().getMin());
            filtroConta.setDataFim(filtro.getData().getMax());

            gerarExcel(relatorioService.imprimeNfse(filtroConta), "NFSe emitidas");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
    }

}
