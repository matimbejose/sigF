package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.wsdl.NFeRetAutorizacao.NFeRetAutorizacao4Stub;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.ObjetoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.UrlWebServiceUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import java.rmi.RemoteException;
import java.util.Iterator;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.AXIOMUtil;

/**
 * Classe Responsavel por Enviar o XML.
 *
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 */
public class Enviar {

    private static NFeRetAutorizacao4Stub.NfeResultMsg result;

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    private static CertificadoUtil certUtil;

    private static String xmlParaEnvio;

    public static String assinarXmlNfe(String xml, boolean valida) throws NfeException {
        certUtil = new CertificadoUtil();
        certUtil.iniciaConfiguracoes();

        xmlParaEnvio = Assinar.assinaNfe(xml, Assinar.NFE);

        if (valida) {
            String erros = Validar.validaXml(xmlParaEnvio, Validar.ENVIO);
            if (!ObjetoUtil.isEmpty(erros)) {
                throw new NfeException("Erro Na Validação do Xml: " + erros, null);
            }
        }
        return xmlParaEnvio;
    }

    public static String obterXmlNfe(TEnviNFe enviNFe) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(enviNFe);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll(" xmlns=\"\" xmlns:ns3=\"http://www.portalfiscal.inf.br/nfe\"", "");
            xml = xml.replaceAll("ns3:", "");

            return xml;
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static TEnviNFe montaObjetoNfe(TEnviNFe enviNFe) throws NfeException {
        try {
            String xml = XmlUtil.objectToXml(enviNFe);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll(" xmlns=\"\" xmlns:ns3=\"http://www.portalfiscal.inf.br/nfe\"", "");
            xml = xml.replaceAll("ns3:", "");

            return XmlUtil.xmlToObject(xml, TEnviNFe.class);
        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static TRetEnviNFe enviaNfe(TEnviNFe enviNFe) throws NfeException {
        certUtil = new CertificadoUtil();
        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();
        String qrCode = "";

        try {
            if (enviNFe.getNFe().get(0).getInfNFe().getIde().getMod().equals("65")) {
                qrCode = enviNFe.getNFe().get(0).getInfNFeSupl().getQrCode();
                enviNFe.getNFe().get(0).getInfNFeSupl().setQrCode("");
            }

            String xml = XmlUtil.objectToXml(enviNFe);

            if (enviNFe.getNFe().get(0).getInfNFe().getIde().getMod().equals("65")) {
                enviNFe.getNFe().get(0).getInfNFeSupl().setQrCode(qrCode);
            }

            xml = xml.replaceAll("ns2:", "");
            xml = xml.replaceAll("<Signature>", "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\">");

            OMElement ome = AXIOMUtil.stringToOM(xml);

            Iterator<?> children = ome.getChildrenWithLocalName("NFe");
            while (children.hasNext()) {
                OMElement omElementNFe = (OMElement) children.next();
                if ((omElementNFe != null) && ("NFe".equals(omElementNFe.getLocalName()))) {
                    omElementNFe.addAttribute("xmlns", "http://www.portalfiscal.inf.br/nfe", null);
                    if (enviNFe.getNFe().get(0).getInfNFe().getIde().getMod().equals("65")) {

                        OMFactory f = OMAbstractFactory.getOMFactory();
                        OMText omt = f.createOMText(qrCode, OMElement.CDATA_SECTION_NODE);

                        Iterator<?> itInfSupl = omElementNFe.getChildrenWithLocalName("infNFeSupl");
                        while (itInfSupl.hasNext()) {
                            Object elementInfSupl = itInfSupl.next();
                            if (elementInfSupl instanceof OMElement) {
                                OMElement omElementInfSupl = (OMElement) elementInfSupl;
                                Iterator<?> itqrCode = omElementInfSupl.getChildrenWithLocalName("qrCode");
                                while (itqrCode.hasNext()) {
                                    Object elementQrCode = itqrCode.next();
                                    if (elementQrCode instanceof OMElement) {
                                        OMElement omElementQrCode = (OMElement) elementQrCode;
                                        omElementQrCode.addChild(omt);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            NFeRetAutorizacao4Stub.NfeDadosMsg dadosMsg = new NFeRetAutorizacao4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);
            NFeRetAutorizacao4Stub stub = new NFeRetAutorizacao4Stub(UrlWebServiceUtil.enviarSincrono().toString());
            stub._getServiceClient().getOptions().setTimeOutInMilliSeconds(120000);
            result = stub.nfeRetAutorizacaoLote(dadosMsg);

            return XmlUtil.xmlToObject(result.getExtraElement().toString(), TRetEnviNFe.class);

        } catch (RemoteException | XMLStreamException | JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static String obterXmlNfe(TEnviNFe enviNFe, boolean valida) throws NfeException {
        certUtil = new CertificadoUtil();

        try {

            /**
             * Informacoes do Certificado Digital.
             */
            certUtil.iniciaConfiguracoes();

            /**
             * Cria o xml
             */
            String xml = XmlUtil.objectToXml(enviNFe);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replaceAll(" xmlns=\"\" xmlns:ns3=\"http://www.portalfiscal.inf.br/nfe\"", "");
            xml = xml.replaceAll("ns3:", "");

            /**
             * Assina o Xml
             */
            xml = Assinar.assinaNfe(xml, Assinar.NFE);

            /**
             * Valida o Xml caso sejá selecionado True
             */
            if (valida) {
                String erros = Validar.validaXml(xml, Validar.ENVIO);
                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            }

            return xml;

        } catch (JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static String getXmlParaEnvio() {
        return xmlParaEnvio;
    }

    public static void setXmlParaEnvio(String xmlParaEnvio) {
        Enviar.xmlParaEnvio = xmlParaEnvio;
    }

    private Enviar() {
    }

}
