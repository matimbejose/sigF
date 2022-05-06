package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.DataUtil;
import br.gov.pbh.nfse.wsdl.NfseWSServiceStub;
import br.gov.pbh.nfse.wsdl.NfseWSServiceStub.ConsultarNfseResponse;
import br.gov.pbh.nfse.wsdl.NfseWSServiceStub.ConsultarSituacaoLoteRpsResponse;
import br.gov.pbh.schema.nfse.Cabecalho;
import br.gov.pbh.schema.nfse.CancelarNfseEnvio;
import br.gov.pbh.schema.nfse.CancelarNfseResposta;
import br.gov.pbh.schema.nfse.ConsultarNfseEnvio;
import br.gov.pbh.schema.nfse.ConsultarSituacaoLoteRpsEnvio;
import br.gov.pbh.schema.nfse.GerarNfseEnvio;
import br.gov.pbh.schema.nfse.GerarNfseResposta;
import br.gov.pbh.schema.nfse.ListaMensagemRetorno;
import br.gov.pbh.schema.nfse.ListaMensagemRetornoLote;
import br.gov.pbh.schema.nfse.TcCompNfse;
import com.thoughtworks.xstream.XStream;
import java.rmi.RemoteException;
import javax.xml.bind.JAXBException;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;

public class NFSeMGBH {

    private static NfseWSServiceStub.Input input;

    private static NfseWSServiceStub stub1;

    private static ConfiguracoesIniciaisNfe configuracoesNfe;

    private static CertificadoUtil certUtil;

    private static final XStream GERADOR_XML = new XStream();

    private static void iniciaConfiguracoes() throws NfeException, AxisFault {
        certUtil = new CertificadoUtil();
        certUtil.iniciaConfiguracoes();
        configuracoesNfe = ConfiguracoesIniciaisNfe.getInstance();
        input = new NfseWSServiceStub.Input();
//        stub1 = new NfseWSServiceStub(UrlWebServiceUtil.notaFiscalServico().toString());
    }

    public static String obterXmlNfse(GerarNfseEnvio gerarNfseEnvio) throws JAXBException, NfeException {
        String xml = XmlUtil.objectToXml(gerarNfseEnvio);
        xml = xml.replace("xmlns:ns3=\"http://ws.bhiss.pbh.gov.br\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");
        xml = converterXmlIntermediario(xml);

        return XmlUtil.removeAcentos(xml);
    }

    public static NFS transmitirNfse(NFS nfs, String xmlAssinado) throws JAXBException, NfeException, AxisFault, RemoteException {

        iniciaConfiguracoes();

        // cabecalho
        String xmlCabecalho = gerarCabecalhoXml();

        // input
        input.setNfseCabecMsg(xmlCabecalho);
        input.setNfseDadosMsg(xmlAssinado);

        // request
        NfseWSServiceStub.GerarNfseRequest gerarNfseRequest = new NfseWSServiceStub.GerarNfseRequest();
        gerarNfseRequest.setGerarNfseRequest(input);

        // response
        NfseWSServiceStub.GerarNfseResponse gerarNfseResponse = new NfseWSServiceStub.GerarNfseResponse();
        gerarNfseResponse = stub1.gerarNfse(gerarNfseRequest);

        // NFS-e
        return preencherRetornoNFS(nfs, gerarNfseResponse, xmlAssinado);
    }

    public static NFS cancelarNfse(NFS nfs, CancelarNfseEnvio cancelarNfseEnvio) throws NfeException {

        try {
            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = gerarCabecalhoXml();

            // corpo
            String xml = XmlUtil.objectToXml(cancelarNfseEnvio);
            xml = xml.replace("xmlns:ns3=\"http://ws.bhiss.pbh.gov.br\"", "")
                    .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                    .replace("ns3:", "")
                    .replace("Request", "Envio");

            // assinatura
            xml = Assinar.assinaNfe(xml, "InfPedidoCancelamento");

            // input
            input.setNfseCabecMsg(xmlCabecalho);
            input.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.CancelarNfseRequest cancelarNfseRequest = new NfseWSServiceStub.CancelarNfseRequest();
            cancelarNfseRequest.setCancelarNfseRequest(input);

            // response
            NfseWSServiceStub.CancelarNfseResponse cancelarNfseResponse = new NfseWSServiceStub.CancelarNfseResponse();
            cancelarNfseResponse = stub1.cancelarNfse(cancelarNfseRequest);

            // NFS-e
            return preencherRetornoCancelamentoNFS(nfs, cancelarNfseResponse, xml);

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static NFS cancelarNfse(NFS nfs, String xmlAssinado) throws NfeException {

        try {
            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = gerarCabecalhoXml();

            // input
            input.setNfseCabecMsg(xmlCabecalho);
            input.setNfseDadosMsg(xmlAssinado);

            // request
            NfseWSServiceStub.CancelarNfseRequest cancelarNfseRequest = new NfseWSServiceStub.CancelarNfseRequest();
            cancelarNfseRequest.setCancelarNfseRequest(input);

            // response
            NfseWSServiceStub.CancelarNfseResponse cancelarNfseResponse = new NfseWSServiceStub.CancelarNfseResponse();
            cancelarNfseResponse = stub1.cancelarNfse(cancelarNfseRequest);

            // NFS-e
            return preencherRetornoCancelamentoNFS(nfs, cancelarNfseResponse, xmlAssinado);

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static String assinarXmlCancelamentoNfs(String xml) throws NfeException, JAXBException {
        return Assinar.assinaNfe(xml, "InfPedidoCancelamento");
    }

    public static String obterXmlCancelamentoNfs(NFS nfs, CancelarNfseEnvio cancelarNfseEnvio) throws NfeException, JAXBException {

        // corpo
        String xml = XmlUtil.objectToXml(cancelarNfseEnvio);
        xml = xml.replace("xmlns:ns3=\"http://ws.bhiss.pbh.gov.br\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");

        return xml;
    }

    public static ConsultarSituacaoLoteRpsResponse consultarSituacaoLoteRps(ConsultarSituacaoLoteRpsEnvio consultarSituacaoLoteRpsEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = gerarCabecalhoXml();

            // corpo
            String xml = XmlUtil.objectToXml(consultarSituacaoLoteRpsEnvio);
            xml = xml.replace("xmlns:ns2=\"http://ws.bhiss.pbh.gov.br\"", "");
            xml = xml.replace("ns2:", "");
            xml = xml.replace("Request", "Envio");

            // input
            input.setNfseCabecMsg(xmlCabecalho);
            input.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.ConsultarSituacaoLoteRpsRequest consultarSituacaoLoteRpsRequest = new NfseWSServiceStub.ConsultarSituacaoLoteRpsRequest();
            consultarSituacaoLoteRpsRequest.setConsultarSituacaoLoteRpsRequest(input);

            // reponse
            return stub1.consultarSituacaoLoteRps(consultarSituacaoLoteRpsRequest);

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    public static ConsultarNfseResponse consultarNfse(ConsultarNfseEnvio consultarNfseEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = gerarCabecalhoXml();

            // corpo
            String xml = XmlUtil.objectToXml(consultarNfseEnvio);
            xml = xml.replace("xmlns:ns2=\"http://ws.bhiss.pbh.gov.br\"", "");
            xml = xml.replace("ns2:", "");
            xml = xml.replace("Request", "Envio");

            // input
            input.setNfseCabecMsg(xmlCabecalho);
            input.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.ConsultarNfseRequest consultarNfseRequest = new NfseWSServiceStub.ConsultarNfseRequest();
            consultarNfseRequest.setConsultarNfseRequest(input);

            // reponse
            return stub1.consultarNfse(consultarNfseRequest);

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    private static NFS preencherRetornoNFS(NFS nfs, NfseWSServiceStub.GerarNfseResponse response, String xmlAssinado) throws JAXBException, NfeException {

        try {

            GerarNfseResposta gerarNfseResposta = XmlUtil.xmlToObject(response.getGerarNfseResponse().getOutputXML(), GerarNfseResposta.class);
            nfs.setProtocolo(gerarNfseResposta.getProtocolo());
            nfs.setXmlEnvio(xmlAssinado);
            nfs.setXmlRetorno(response.getGerarNfseResponse().getOutputXML());

            verificarEnvioComErro(gerarNfseResposta);

            String xmlIntermediarioRevertido = reverterXmlIntermediario(response.getGerarNfseResponse().getOutputXML());
            nfs.setXmlArmazenamento(gerarXmlArmazenamento(gerarNfseResposta, xmlIntermediarioRevertido));
            nfs.setMensagemErroEnvio(null);
            nfs.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            nfs.setNumeroNotaFiscal(gerarNfseResposta.getListaNfse().getCompNfse().get(0).getNfse().getInfNfse().getNumero().toString());

        } catch (NfeException ex) {

            nfs.setMensagemErroEnvio(ex.getMessage());
            nfs.setSituacao(EnumSituacaoNF.REJEITADA.getSituacao());
        }

        return nfs;

    }

    private static void verificarEnvioComErro(GerarNfseResposta gerarNfseResposta) throws NfeException {
        StringBuilder msgErro = new StringBuilder();

        ListaMensagemRetorno mr = gerarNfseResposta.getListaMensagemRetorno();

        ListaMensagemRetornoLote mrl = gerarNfseResposta.getListaMensagemRetornoLote();

        if (mr != null) {
            mr.getMensagemRetorno().stream().forEach(msg -> msgErro.append(msg.getCodigo()).append(" : ").append(msg.getMensagem()).append("\n"));
        }

        if (mrl != null) {
            mrl.getMensagemRetorno().stream().forEach(msg -> msgErro.append(msg.getCodigo()).append(" : ").append(msg.getMensagem()).append("\n"));
        }

        if (!StringUtils.isEmpty(msgErro)) {
            throw new NfeException("NFS rejeitada \n " + msgErro.toString(), null);
        }
    }

    private static NFS preencherRetornoCancelamentoNFS(NFS nfs, NfseWSServiceStub.CancelarNfseResponse response, String xmlAssinado) throws JAXBException, NfeException {

        try {
            CancelarNfseResposta cancelarNfseResposta = XmlUtil.xmlToObject(response.getCancelarNfseResponse().getOutputXML(), CancelarNfseResposta.class);
            nfs.setXmlEnvioCancelamento(xmlAssinado);
            nfs.setXmlRetornoCancelamento(response.getCancelarNfseResponse().getOutputXML());

            verificarCancelamentoComErro(cancelarNfseResposta);

            // monta o xml de cancelamento da nota
            TcCompNfse compNfse = XmlUtil.xmlToObject(nfs.getXmlArmazenamento(), TcCompNfse.class);
            compNfse.setNfseCancelamento(cancelarNfseResposta.getRetCancelamento().getNfseCancelamento().get(0));

            nfs.setXmlArmazenamentoCancelamento(XmlUtil.objectToXml(compNfse));
            nfs.setMensagemErroEnvio(null);
            nfs.setSituacao(EnumSituacaoNF.CANCELADA.getSituacao());
            nfs.setDataCancelamento(DataUtil.getHoje());

        } catch (NfeException ex) {
            nfs.setMensagemErroEnvio(ex.getMessage());
        }
        return nfs;

    }

    private static void verificarCancelamentoComErro(CancelarNfseResposta cancelarNfseResposta) throws NfeException {
        StringBuilder msgErro = new StringBuilder();

        ListaMensagemRetorno mr = cancelarNfseResposta.getListaMensagemRetorno();

        if (mr != null) {
            mr.getMensagemRetorno().stream().forEach(msg -> msgErro.append(msg.getCodigo()).append(" : ").append(msg.getMensagem()).append("\n"));
        }

        if (!StringUtils.isEmpty(msgErro)) {
            throw new NfeException("Cancelamento não permitido: \n " + msgErro.toString(), null);
        }
    }

    private static String gerarCabecalhoXml() throws JAXBException, NfeException {
        Cabecalho cabecalho = new Cabecalho();
        cabecalho.setVersaoDados("1.00");
        String xmlCabecalho = GERADOR_XML.toXML(cabecalho);

        xmlCabecalho = "<?xml version='1.0' encoding='UTF-8'?>" + xmlCabecalho;
        xmlCabecalho = xmlCabecalho.replace("<br.gov.fazenda.schema.nfse.Cabecalho>", "<cabecalho xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\">");
        xmlCabecalho = xmlCabecalho.replace("</br.gov.fazenda.schema.nfse.Cabecalho>", "</cabecalho>");

        return xmlCabecalho;
    }

    private static String gerarXmlArmazenamento(GerarNfseResposta gerarNfseResposta, String xmlIntermediarioRevertido) throws JAXBException, NfeException {
        String xml = null;
        for (TcCompNfse tcCompNfse : gerarNfseResposta.getListaNfse().getCompNfse()) {

            xml = XmlUtil.objectToXml(tcCompNfse);
            xml = xml.replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
            xml = xml.replace("<CompNfse>", "<CompNfse xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");
            xml = xml.replace("<Nfse versao=\"1.00\">", "<Nfse xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\">");
            xml = xml.replace("ns2:", "");

            if (StringUtils.isNotEmpty(xmlIntermediarioRevertido)) {
                xml = xml.replace(xml.substring(xml.indexOf("<IntermediarioServico>"), xml.indexOf("</IntermediarioServico>") + 23), xmlIntermediarioRevertido);
            }

        }

        return xml;
    }

    public static void main(String[] args) {
        reverterXmlIntermediario("<?xml version='1.0' encoding='UTF-8'?><GerarNfseResposta xmlns=\"http://www.abrasf.org.br/nfse.xsd\"><NumeroLote>90</NumeroLote><DataRecebimento>2017-02-10T17:41:41</DataRecebimento><Protocolo>S40579270k172041l5MVEOBP</Protocolo><ListaNfse><CompNfse><Nfse xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\"><InfNfse Id=\"nfse\"><Numero>201700000000044</Numero><CodigoVerificacao>00000000</CodigoVerificacao><DataEmissao>2017-02-10T17:41:41</DataEmissao><IdentificacaoRps><Numero>90</Numero><Serie>1</Serie><Tipo>1</Tipo></IdentificacaoRps><DataEmissaoRps>2017-02-10</DataEmissaoRps><NaturezaOperacao>1</NaturezaOperacao><OptanteSimplesNacional>2</OptanteSimplesNacional><IncentivadorCultural>2</IncentivadorCultural><Competencia>2017-02-10T00:00:00</Competencia><OutrasInformacoes>NFS-e gerada em ambiente de teste. NÃO TEM VALOR JUR�?DICO NEM FISCAL.</OutrasInformacoes><Servico><Valores><ValorServicos>1.00</ValorServicos><ValorPis>0.10</ValorPis><ValorCofins>0.10</ValorCofins><ValorInss>0.10</ValorInss><ValorIr>0.10</ValorIr><ValorCsll>0.10</ValorCsll><IssRetido>2</IssRetido><ValorIss>0.03</ValorIss><BaseCalculo>1.00</BaseCalculo><Aliquota>0.025</Aliquota><ValorLiquidoNfse>0.50</ValorLiquidoNfse></Valores><ItemListaServico>1.03</ItemListaServico><CodigoTributacaoMunicipio>10300188</CodigoTributacaoMunicipio><Discriminacao>Arm int</Discriminacao><CodigoMunicipio>3106200</CodigoMunicipio></Servico><PrestadorServico><IdentificacaoPrestador><Cnpj>08644984000155</Cnpj><InscricaoMunicipal>0208554001X</InscricaoMunicipal></IdentificacaoPrestador><RazaoSocial>W &amp; A VILLEFORT CONSULTORIA E TECNOLOGIA LTDA - ME</RazaoSocial><NomeFantasia>VILLEFORT CONSULTING</NomeFantasia><Endereco><Endereco>RUA SAO PAULO</Endereco><Numero>1106</Numero><Complemento>SALA: 401; B, C, D, E, F;</Complemento><Bairro>Centro</Bairro><CodigoMunicipio>3106200</CodigoMunicipio><Uf>MG</Uf><Cep>30170131</Cep></Endereco></PrestadorServico><TomadorServico><IdentificacaoTomador><CpfCnpj><Cpf>03984542640</Cpf></CpfCnpj></IdentificacaoTomador><RazaoSocial>Christopher</RazaoSocial><Endereco><Endereco>Avenida Ribeiro de Paiva</Endereco><Numero>870</Numero><Bairro>Joao Pinheiro</Bairro><CodigoMunicipio>3106200</CodigoMunicipio><Uf>MG</Uf><Cep>30530170</Cep></Endereco><Contato><Email>nodejs@email.com</Email></Contato></TomadorServico><IntermediarioServico><RazaoSocial>ACDC</RazaoSocial><CpfCnpj><Cpf>07461603647</Cpf></CpfCnpj></IntermediarioServico><OrgaoGerador><CodigoMunicipio>3106200</CodigoMunicipio><Uf>MG</Uf></OrgaoGerador></InfNfse><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\" Id=\"NfseAssSMF_nfse\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></CanonicalizationMethod><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"></SignatureMethod><Reference URI=\"#nfse\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"></Transform><Transform Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"></Transform></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"></DigestMethod><DigestValue>d24bjvcqwirXn1oZtoKHsg1St/c=</DigestValue></Reference></SignedInfo><SignatureValue>aEwWusdMK+UXEtOkq86wivzHtsPCtAj3wJVjJ+9Im08ZzdQwII6ZUbnwTHa6NUE7B84QcDDYWusElKKGhjFijpOo6rq+cMxbgKd0URWno0PbcdRuh6JHgM5oR2dqqvYrYxhNCoCVnovvAFGbXr1+ZoA0CQrfARiT8pIP9JkPUV2WUNS46KjqUqBN+FCX9U+6uqEmD8bcYQP463uMEDROInPIoHdqfdIoq4/f9Zt+/oTf4MMzpnmsiOAZdFc0IYZJU6gkyDdFi02D07sWr3IDsxD4tuk9oTYvhdWlRfyuOhVwM9xhIMxMEzfZoTSgsT0cJdkJpjy00xLyOnKgmST4GA==</SignatureValue><KeyInfo><X509Data><X509Certificate>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</X509Certificate></X509Data></KeyInfo></Signature></Nfse></CompNfse></ListaNfse></GerarNfseResposta>");
    }

    private static String converterXmlIntermediario(String xml) {
        // verifica se existe intermediario no XML
        if (xml.contains("<IntermediarioServico>")) {
            // troca de posição o lugar do IdentificacaoIntermediario com a RazaoSocial
            String razaoSocialIntermediario = xml.substring(xml.indexOf("<RazaoSocial>", xml.indexOf("<IntermediarioServico>")), xml.indexOf("</RazaoSocial>", xml.indexOf("<IntermediarioServico>")) + 14);
            String identificacaoIntermediario = xml.substring(xml.indexOf("<IdentificacaoIntermediario>", xml.indexOf("<IntermediarioServico>")), xml.indexOf("</IdentificacaoIntermediario>", xml.indexOf("<IdentificacaoIntermediario>")) + 29);

            identificacaoIntermediario = identificacaoIntermediario.replace("<IdentificacaoIntermediario>", "");
            identificacaoIntermediario = identificacaoIntermediario.replace("</IdentificacaoIntermediario>", "");

            String xmlConvertido = xml.substring(0, xml.indexOf("<IntermediarioServico>") + 22);

            xmlConvertido += razaoSocialIntermediario;
            xmlConvertido += identificacaoIntermediario;
            xmlConvertido += xml.substring(xml.indexOf("</IntermediarioServico>"), xml.length());

            xml = xmlConvertido;

        }

        return xml;
    }

    private static String reverterXmlIntermediario(String xml) {
        // verifica se existe intermediario no XML
        if (xml.contains("<IntermediarioServico>")) {
            String razaoSocialIntermediario = xml.substring(xml.indexOf("<RazaoSocial>", xml.indexOf("<IntermediarioServico>")), xml.indexOf("</RazaoSocial>", xml.indexOf("<IntermediarioServico>")) + 14);

            String identificacaoIntermediario = "<IdentificacaoIntermediario>";
            identificacaoIntermediario += xml.substring(xml.indexOf("<CpfCnpj>", xml.indexOf("<IntermediarioServico>")), xml.indexOf("</CpfCnpj>", xml.indexOf("<IntermediarioServico>")) + 10);
            identificacaoIntermediario += "</IdentificacaoIntermediario>";

            return "<IntermediarioServico>" + identificacaoIntermediario + razaoSocialIntermediario + "</IntermediarioServico>";
        }

        return null;
    }

}
