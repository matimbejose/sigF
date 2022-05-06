package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema.distdfeint.DistDFeInt;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.wsdl.NFeDistribuicaoDFe.NFeDistribuicaoDFeStub;
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
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 *
 */
public class DistribuicaoDFe {

    private static NFeDistribuicaoDFeStub.NfeDistDFeInteresseResponse result;

    private static CertificadoUtil certUtil;

    /**
     * Classe Reponsavel Por Consultar as NFE na SEFAZ
     *
     * @param nfe
     * @return nfe
     */
    public static RetDistDFeInt consultaNfe(DistDFeInt distDFeInt, boolean valida) throws NfeException {

        certUtil = new CertificadoUtil();

        try {

            /**
             * Carrega Informaçoes do Certificado Digital.
             */
            certUtil.iniciaConfiguracoes();

            String xml = XmlUtil.objectToXml(distDFeInt);

            if (valida) {
                String erros = Validar.validaXml(xml, Validar.DIST_DFE);

                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            }

            OMElement ome = AXIOMUtil.stringToOM(xml);

            NFeDistribuicaoDFeStub.NfeDadosMsg_type0 dadosMsgType0 = new NFeDistribuicaoDFeStub.NfeDadosMsg_type0();
            dadosMsgType0.setExtraElement(ome);

            NFeDistribuicaoDFeStub.NfeDistDFeInteresse distDFeInteresse = new NFeDistribuicaoDFeStub.NfeDistDFeInteresse();
            distDFeInteresse.setNfeDadosMsg(dadosMsgType0);

            NFeDistribuicaoDFeStub stub = new NFeDistribuicaoDFeStub(UrlWebServiceUtil.distribuicaoDfe().toString());
            result = stub.nfeDistDFeInteresse(distDFeInteresse);

            return XmlUtil.xmlToObject(result.getNfeDistDFeInteresseResult().getExtraElement().toString(), RetDistDFeInt.class);

        } catch (RemoteException | XMLStreamException | JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    private DistribuicaoDFe() {
    }

}
