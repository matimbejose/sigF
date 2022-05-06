package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema_4.consSitNFe.TRetConsSitNFe;
import br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TConsSitNFe;
import br.com.swconsultoria.nfe.wsdl.CadConsultaCadastro.CadConsultaCadastro4Stub;
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
 * Classe responsavel por Consultar a Situaçao do XML na SEFAZ.
 *
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 *
 */
public class ConsultaXml {

    private static CadConsultaCadastro4Stub.NfeResultMsg result;

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    private static CertificadoUtil certUtil;

    /**
     * Classe Reponsavel Por Consultar o status da NFE na SEFAZ
     *
     * @param Chave
     * @return Resposta da Sefaz
     * @throws NfeException
     */
    public static TRetConsSitNFe consultaXml(TConsSitNFe consSitNFe, boolean valida) throws NfeException {
        certUtil = new CertificadoUtil();
        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();

        try {
            /**
             * Informaçoes do Certificado Digital.
             */
            certUtil.iniciaConfiguracoes();

            String xml = XmlUtil.objectToXml(consSitNFe);

            if (valida) {
                // Validação
                String erros = Validar.validaXml(xml, Validar.CONSULTA_XML);
                if (!ObjetoUtil.isEmpty(erros)) {
                    throw new NfeException("Erro Na Validação do Xml: " + erros, null);
                }
            }

            OMElement ome = AXIOMUtil.stringToOM(xml);

            CadConsultaCadastro4Stub.NfeDadosMsg dadosMsg = new CadConsultaCadastro4Stub.NfeDadosMsg();
            dadosMsg.setExtraElement(ome);

            CadConsultaCadastro4Stub stub = new CadConsultaCadastro4Stub(UrlWebServiceUtil.consultaXml().toString());
            result = stub.consultaCadastro(dadosMsg);

            return XmlUtil.xmlToObject(result.getExtraElement().toString(), TRetConsSitNFe.class);

        } catch (RemoteException | XMLStreamException | JAXBException ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    private ConsultaXml() {
    }

}
