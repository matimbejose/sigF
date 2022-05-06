package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.wsdl.NFeRecepcaoEvento.NFeRecepcaoEvento4Stub;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.ObjetoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.UrlWebServiceUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import java.rmi.RemoteException;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;

public class Evento {

    private static NFeRecepcaoEvento4Stub.NfeResultMsg result;

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    private static CertificadoUtil certUtil;

    private static String xmlParaEnvio;

    public static TRetEnvEvento eventoCancelamento(TEnvEvento evento, boolean valida) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(evento);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

            xml = evento(xml, "cancelar", valida, false);

            return XmlUtil.xmlToObject(xml, TRetEnvEvento.class);
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static TRetEnvEvento eventoCancelamento(String xml, boolean valida) throws NfeException {
        try {
            xml = evento(xml, "cancelar", valida, true);

            return XmlUtil.xmlToObject(xml, TRetEnvEvento.class);
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static String obterXmlCancelamento(TEnvEvento evento) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(evento);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

            return xml;
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static TRetEnvEvento eventoManifestacao(TEnvEvento evento, boolean valida) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(evento);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

            xml = evento(xml, "manifestar", valida, false);

            return XmlUtil.xmlToObject(xml, TRetEnvEvento.class);
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static TRetEnvEvento eventoCce(TEnvEvento evento, boolean valida) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(evento);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll("<evento v", "<evento xmlns=\"http://www.portalfiscal.inf.br/nfe\" v");

            xml = evento(xml, "cce", valida, false);

            return XmlUtil.xmlToObject(xml, TRetEnvEvento.class);
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private static String evento(String xml, String tipo, boolean valida, boolean xmlAssinado) throws NfeException {
        certUtil = new CertificadoUtil();
        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();
        String estado = String.valueOf(configuracoesNfe.getUf());

        try {
            /**
             * Informaes do Certificado Digital.
             */
            certUtil.iniciaConfiguracoes();

            if (!xmlAssinado) {

                xml = Assinar.assinaNfe(xml, Assinar.EVENTO);
            }

            if (valida) {
                String erros = "";
                switch (tipo) {
                    case "cancelar":
                        erros = Validar.validaXml(xml, Validar.CANCELAR);
                        break;
                    case "cce":
                        erros = Validar.validaXml(xml, Validar.CCE);
                        break;
                    case "manifestar":
                        erros = Validar.validaXml(xml, Validar.MANIFESTAR);
                        estado = "91";
                        break;
                    default:
                        break;
                }

                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            } else if (tipo.equals("manifestar")) {
                estado = "91";
            }

            xmlParaEnvio = xml;

            OMElement ome = AXIOMUtil.stringToOM(xml);

            NFeRecepcaoEvento4Stub.NfeDadosMsg dadosMsg = new NFeRecepcaoEvento4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NFeRecepcaoEvento4Stub stub = new NFeRecepcaoEvento4Stub(UrlWebServiceUtil.evento(estado).toString());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(120000);
            result = stub.nfeRecepcaoEvento(dadosMsg);

        } catch (RemoteException | XMLStreamException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

        return result.getExtraElement().toString();
    }

    public static String getXmlParaEnvio() {
        return xmlParaEnvio;
    }

    public static void setXmlParaEnvio(String xmlParaEnvio) {
        Evento.xmlParaEnvio = xmlParaEnvio;
    }

    private Evento() {
    }

}
