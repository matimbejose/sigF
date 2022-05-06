package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.SchemaUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Validar implements ErrorHandler {

    private static String xsd;

    private String listaComErrosDeValidacao;

    public static final String STATUS = "status";

    public static final String CONSULTA_XML = "consultaXml";

    public static final String ENVIO = "envio";

    public static final String DIST_DFE = "destDfe";

    public static final String INUTILIZACAO = "inutilizacao";

    public static final String CANCELAR = "cancelar";

    public static final String MANIFESTAR = "manifestar";

    public static final String CCE = "cce";

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    /**
     * Construtor privado
     */
    private Validar() {
        this.listaComErrosDeValidacao = "";
    }

    public static String validaXml(String xml, String tipo) throws NfeException {

        String errosValidacao = null;

        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();

        switch (tipo) {
            case STATUS:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.STATUS;
                break;
            case ENVIO:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.ENVIO;
                break;
            case CONSULTA_XML:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.CONSULTA_XML;
                break;
            case DIST_DFE:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.DIST_DFE;
                break;
            case INUTILIZACAO:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.INUTILIZACAO;
                break;
            case CANCELAR:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.CANCELAR;
                break;
            case CCE:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.CCE;
                break;
            case MANIFESTAR:
                xsd = configuracoesNfe.getPastaSchemas() + "/" + SchemaUtil.MANIFESTAR;
                break;
            default:
                break;
        }

        if (!new File(xsd).exists()) {
            throw new NfeException("Schema Nfe não Localizado: " + xsd, null);
        }

        Validar validar = new Validar();

        errosValidacao = validar.validateXml(xml, xsd);

        return errosValidacao;
    }

    public String validateXml(String xml, String xsd) throws NfeException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(true);
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
        factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", xsd);
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(this);
        } catch (ParserConfigurationException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

        try {
            builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (Exception ex) {
            throw new NfeException(ex.toString(), ex);
        }

        return this.getListaComErrosDeValidacao();
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {

        if (isError(exception)) {
            listaComErrosDeValidacao += tratamentoRetorno(exception.getMessage());
        }
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {

        listaComErrosDeValidacao += tratamentoRetorno(exception.getMessage());
    }

    @Override
    public void warning(SAXParseException exception) throws SAXException {

        listaComErrosDeValidacao += tratamentoRetorno(exception.getMessage());
    }

    private String tratamentoRetorno(String message) {

        message = message.replaceAll("cvc-type.3.1.3:", "-");
        message = message.replaceAll("cvc-attribute.3:", "-");
        message = message.replaceAll("cvc-complex-type.2.4.a:", "-");
        message = message.replaceAll("cvc-complex-type.2.4.b:", "-");
        message = message.replaceAll("The value", "O valor");
        message = message.replaceAll("of element", "do campo");
        message = message.replaceAll("is not valid", "nao é valido");
        message = message.replaceAll("Invalid content was found starting with element", "Encontrado o campo");
        message = message.replaceAll("One of", "Campo(s)");
        message = message.replaceAll("is expected", "é obrigatorio");
        message = message.replaceAll("\\{", "");
        message = message.replaceAll("\\}", "");
        message = message.replaceAll("\"", "");
        message = message.replaceAll("http://www.portalfiscal.inf.br/nfe:", "");
        return System.getProperty("line.separator") + message.trim();
    }

    public String getListaComErrosDeValidacao() {

        return listaComErrosDeValidacao;
    }

    private boolean isError(SAXParseException exception) {

        return !(exception.getMessage().startsWith("cvc-enumeration-valid") || exception.getMessage().startsWith("cvc-pattern-valid") || exception.getMessage().startsWith("cvc-maxLength-valid") || exception.getMessage().startsWith("cvc-datatype"));
    }

}
