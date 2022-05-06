package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema_4.inutNFe.TInutNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.wsdl.NFeInutilizacao.NFeInutilizacao4Stub;
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
 * Classe Responsavel por inutilizar uma Faixa de numeracao da Nfe.
 *
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 *
 */
public class Inutilizar {

    private Inutilizar() {
    }

    private static NFeInutilizacao4Stub.NfeResultMsg result;

    private static CertificadoUtil certUtil;

    public static TRetInutNFe inutiliza(TInutNFe inutNFe, boolean valida) throws NfeException {
        certUtil = new CertificadoUtil();

        try {
            /**
             * Informacoes do Certificado Digital.
             */
            certUtil.iniciaConfiguracoes();

            String xml = XmlUtil.objectToXml(inutNFe);
            xml = xml.replaceAll(" xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");

            /**
             * Assina o Xml
             */
            xml = Assinar.assinaNfe(xml, Assinar.INFINUT);

            if (valida) {
                String erros = Validar.validaXml(xml, Validar.INUTILIZACAO);

                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            }

            OMElement ome = AXIOMUtil.stringToOM(xml);
            NFeInutilizacao4Stub.NfeDadosMsg dadosMsg = new NFeInutilizacao4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            NFeInutilizacao4Stub stub = new NFeInutilizacao4Stub(UrlWebServiceUtil.inutilizar().toString());
            result = stub.nfeInutilizacaoNF(dadosMsg);

            return XmlUtil.xmlToObject(result.getExtraElement().toString(), TRetInutNFe.class);
        } catch (RemoteException | XMLStreamException | JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

}
