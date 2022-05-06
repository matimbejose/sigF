package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TConsStatServ;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.swconsultoria.nfe.wsdl.NFeStatusServico4.NFeStatusServico4Stub;
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

/**
 * Classe responsavel por fazer a Verificacao do Status Do Webservice
 *
 * @autor Samuel Oliveira
 */
public class Status {

    static NFeStatusServico4Stub.NfeResultMsg result;

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    public static TRetConsStatServ statusServico(TConsStatServ consStatServ, boolean valida) throws NfeException {

        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();
        CertificadoUtil certificadoUtil = new CertificadoUtil();
        certificadoUtil.iniciaConfiguracoes();

        try {

            String xml = XmlUtil.objectToXml(consStatServ);

            if (valida) {
                String erros = Validar.validaXml(xml, Validar.STATUS);
                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            }

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NFeStatusServico4Stub.NfeDadosMsg dadosMsg = new NFeStatusServico4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NFeStatusServico4Stub stub = new NFeStatusServico4Stub(UrlWebServiceUtil.status().toString());
            result = stub.nfeStatusServicoNF(dadosMsg);

            return XmlUtil.xmlToObject(result.getExtraElement().toString(), TRetConsStatServ.class);

        } catch (RemoteException | XMLStreamException | JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    private Status() {
    }

}
