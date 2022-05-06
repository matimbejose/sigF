package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoReceitaDespesa;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.primefaces.PrimeFaces;
import org.primefaces.event.data.PageEvent;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    private final SimpleDateFormat sdfHora = new SimpleDateFormat("dd/MM/yy HH:mm");

    private int first;

    protected void cleanCache() {
        // Placeholder para a função cleanCache
    }

    protected StatusCadastroDTO getImportConfig() {
        return null;
    }

    protected Set<ConstraintViolation<Serializable>> getViolations(Serializable entidade) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(entidade);
    }

    protected void createFacesErrorMessage(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void createFacesSuccessMessage(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected void createFacesInfoMessage(String msg) {
        FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
        FacesContext.getCurrentInstance().addMessage(null, fm);
    }

    protected boolean existsViolationsForJSF(Serializable entidade) {
        Set<ConstraintViolation<Serializable>> toReturn = getViolations(entidade);
        if (toReturn.isEmpty()) {
            return false;
        }
        toReturn.stream()
                .map(ConstraintViolation::getMessage)
                .forEach(this::createFacesErrorMessage);
        return true;
    }

    /**
     * Indica se alguma mensagem de erro foi enviada para a view
     *
     * @return
     */
    protected boolean existsErrorMessages() {
        return !FacesContext.getCurrentInstance().getMessageList()
                .stream()
                .filter(mensagem -> mensagem.getSeverity().equals(FacesMessage.SEVERITY_ERROR))
                .collect(Collectors.toList()).isEmpty();
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public SimpleDateFormat getSdfComHora() {
        return sdfHora;
    }

    public Date getDataByString(String data) {
        try {
            SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
            return formato.parse(data);
        } catch (ParseException ex) {
            return null;
        }
    }

    protected ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return facesContext.getApplication().getExpressionFactory().createValueExpression(
                facesContext.getELContext(), valueExpression, valueType);
    }

    protected MethodExpression createMethodExpression(String valueExpression, Class<?> expectedReturnType, Class<?>[] expectedParamTypes) {
        MethodExpression methodExpression = null;
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExpressionFactory factory = fc.getApplication().getExpressionFactory();
            methodExpression = factory.createMethodExpression(fc.getELContext(), valueExpression, expectedReturnType, expectedParamTypes);
        } catch (Exception e) {
            throw new FacesException("Method expression '" + valueExpression
                    + "' could not be created.");
        }
        return methodExpression;
    }

    protected static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, (Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
                -> o1.getValue().compareTo(o2.getValue()) * (order ? 1 : -1));

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public boolean checarPermissao(String nome) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .filter(auth -> auth.getAuthority().equals(nome))
                .count() > 0;
    }

    public boolean isPerfilSuporte() {
        return ((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getIdPerfil().getEhSuporte();
    }

    public boolean isPerfilMaster() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario.getIdPerfil().getEhSuporte() || usuario.getIdPerfil().getEhUsuarioMaster();
    }

    protected void gerarPdf(byte[] pdf, String nomeDoPdf) throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(pdf.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nomeDoPdf + ".pdf\"");
        OutputStream output = ec.getResponseOutputStream();
        output.write(pdf);
        fc.responseComplete();
    }

    protected void gerarExcel(byte[] excel, String nomeExcel) throws IOException {

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/vnd.ms-excel");
        ec.setResponseContentLength(excel.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + nomeExcel + ".xls\"");
        OutputStream output = ec.getResponseOutputStream();
        output.write(excel);
        fc.responseComplete();
    }

    protected void gerarExcel(JasperPrint jasperPrint, String fileName, boolean umaAbaPorPagina) throws IOException, JRException {
        SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
        configuration.setOnePagePerSheet(umaAbaPorPagina);
        configuration.setDetectCellType(true);
        configuration.setCollapseRowSpan(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setWhitePageBackground(false);
        configuration.setWrapText(false);
        configuration.setOverrideHints(true);
        configuration.setSheetNames(new String[]{"tab1"});

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/vnd.ms-excel");
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xls\"");

        try (OutputStream output = ec.getResponseOutputStream()) {
            JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporterXLS.setExporterOutput(new SimpleOutputStreamExporterOutput(output));
            exporterXLS.setConfiguration(configuration);
            exporterXLS.exportReport();

            output.flush();
        }
        fc.responseComplete();
    }

    protected void gerarBoleto(File file) throws IOException, FileException {

        byte[] conteudo = FileUtil.convertFileToBytes(file);

        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        ec.responseReset();
        ec.setResponseContentType("application/pdf");
        ec.setResponseContentLength(conteudo.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + ".pdf\"");
        OutputStream output = ec.getResponseOutputStream();
        output.write(conteudo);
        fc.responseComplete();
    }

    public boolean podeVisualizarPainelInicial() {
        return getUsuarioLogado().getIdPerfil().getTipo().equals("AD") || getUsuarioLogado().getIdPerfil().getTipo().equals("MU");
    }

    public String converterValorParaMonetario(Double valor) {
        return NumeroUtil.converterValorParaMonetario(valor, 2);
    }

    public String dataHoje() {
        return new SimpleDateFormat("dd/MM/yyyy").format(DataUtil.getHoje());
    }

    public Date getHoje() {
        return DataUtil.getHoje();
    }

    public String getCodigoReceita() {
        return EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo();
    }

    public String getCodigoDespesa() {
        return EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo();
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void onPageChange(PageEvent event) {
        setFirst(((UIData) event.getSource()).getFirst());
    }

    public void showModal(String nome) {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", false);
        showModal(nome, options);
    }

    public void showModal(String nome, Map<String, Object> options) {
        PrimeFaces.current().dialog().openDynamic(nome, options, null);
    }

    public void closeModal() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    public DtoId getDtoCadastro() {
        throw new UnsupportedOperationException();
    }

    public void setDtoCadastro(DtoId dtoCadastro) {
        throw new UnsupportedOperationException();
    }

    protected List<EntityId> doFinishImport(List<DtoId> obj) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected String mudaSituacaoImportacao() {
        throw new UnsupportedOperationException();
    }

    protected void initDtoCadastro(Object context) {
        throw new UnsupportedOperationException();
    }

    public String getStyleClass() {
        return "card p-0 ui-datatable-scrollable ui-datatable-striped ui-datatable-sm ui-datatable-gridlines ui-datatable-reflow";
    }

    public String getCurrentPageReportTemplate() {
        return "Total de registros: {totalRecords}";
    }

    public String getPaginatorTemplate() {
        return "{CurrentPageReport} {FirstPageLink} {PreviousPageLink} {PageLinks} {NextPageLink} {LastPageLink} {RowsPerPageDropdown}";
    }

    public String getEmptyMessage() {
        return "Nenhum registro encontrado.";
    }

    public void setStyleClass(String s) {
    }

    public void setCurrentPageReportTemplate(String s) {
    }

    public void setPaginatorTemplate(String s) {
    }

    public void setEmptyMessage(String s) {
    }

    public void limparOrdenacao() {
        UIComponent table = FacesContext.getCurrentInstance().getViewRoot().findComponent("collectorDataTable");
        table.setValueExpression("sortBy", null);
    }

}
