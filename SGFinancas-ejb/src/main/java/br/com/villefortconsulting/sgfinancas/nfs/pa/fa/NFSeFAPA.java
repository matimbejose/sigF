package br.com.villefortconsulting.sgfinancas.nfs.pa.fa;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.nfe.Assinar;
import br.com.villefortconsulting.sgfinancas.nfe.ConfiguracoesIniciaisNfe;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.UrlWebServiceUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.com.villefortconsulting.sgfinancas.nfs.EnumNfse;
import br.com.villefortconsulting.sgfinancas.nfs.NFSeGerneric;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.gov.fazenda.schema.nfse.Cabecalho;
import br.gov.fazenda.schema.nfse.CancelarNfseEnvio;
import br.gov.fazenda.schema.nfse.CancelarNfseResposta;
import br.gov.fazenda.schema.nfse.ConsultarLoteRpsEnvio;
import br.gov.fazenda.schema.nfse.ConsultarNfseRpsEnvio;
import br.gov.fazenda.schema.nfse.EnviarLoteRpsEnvio;
import br.gov.fazenda.schema.nfse.GerarNfseResposta;
import br.gov.fazenda.schema.nfse.ListaMensagemAlertaRetorno;
import br.gov.fazenda.schema.nfse.ListaMensagemRetorno;
import br.gov.fazenda.schema.nfse.TcCompNfse;
import br.gov.fazenda.schema.nfse.TcContato;
import br.gov.fazenda.schema.nfse.TcCpfCnpj;
import br.gov.fazenda.schema.nfse.TcDadosConstrucaoCivil;
import br.gov.fazenda.schema.nfse.TcDadosIntermediario;
import br.gov.fazenda.schema.nfse.TcDadosPrestador;
import br.gov.fazenda.schema.nfse.TcDadosServico;
import br.gov.fazenda.schema.nfse.TcDadosTomador;
import br.gov.fazenda.schema.nfse.TcDeclaracaoPrestacaoServico;
import br.gov.fazenda.schema.nfse.TcEndereco;
import br.gov.fazenda.schema.nfse.TcIdentificacaoIntermediario;
import br.gov.fazenda.schema.nfse.TcIdentificacaoNfse;
import br.gov.fazenda.schema.nfse.TcIdentificacaoOrgaoGerador;
import br.gov.fazenda.schema.nfse.TcIdentificacaoPrestador;
import br.gov.fazenda.schema.nfse.TcIdentificacaoRps;
import br.gov.fazenda.schema.nfse.TcIdentificacaoTomador;
import br.gov.fazenda.schema.nfse.TcInfDeclaracaoPrestacaoServico;
import br.gov.fazenda.schema.nfse.TcInfNfse;
import br.gov.fazenda.schema.nfse.TcInfPedidoCancelamento;
import br.gov.fazenda.schema.nfse.TcInfRps;
import br.gov.fazenda.schema.nfse.TcLoteRps;
import br.gov.fazenda.schema.nfse.TcNfse;
import br.gov.fazenda.schema.nfse.TcPedidoCancelamento;
import br.gov.fazenda.schema.nfse.TcValoresDeclaracaoServico;
import br.gov.fazenda.schema.nfse.TcValoresNfse;
import br.gov.fazendapa.nfse.wsdl.servico.NfseWSServiceStub;
import br.gov.fazendapa.nfse.wsdl.servico.NfseWSServiceStub.CancelarNfseResponseE;
import br.gov.fazendapa.nfse.wsdl.servico.NfseWSServiceStub.ConsultarLoteRpsResponseE;
import br.gov.fazendapa.nfse.wsdl.servico.NfseWSServiceStub.ConsultarNfsePorRpsResponseE;
import br.gov.fazendapa.nfse.wsdl.servico.NfseWSServiceStub.GerarNfseResponseE;
import java.math.BigInteger;
import java.rmi.RemoteException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;

public class NFSeFAPA {

    private static NfseWSServiceStub stub1;

    private static CertificadoUtil certUtil;

    private static void iniciaConfiguracoes() throws NfeException, AxisFault {
        certUtil = new CertificadoUtil();
        certUtil.iniciaConfiguracoes();
        ConfiguracoesIniciaisNfe.getInstance();
        stub1 = new NfseWSServiceStub(UrlWebServiceUtil.notaFiscalServicoFAPA().toString());
    }

    public static String obterXmlNfse(TcCompNfse tcCompNfse) throws JAXBException, NfeException {
        String xml = XmlUtilFAPA.objectToXml(tcCompNfse);
        xml = xml.replace("xmlns:ns3=\"" + EnumNfse.FA_PA.getUrl() + "\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");
        xml = NFSeGerneric.converterXmlIntermediario(xml);

        return XmlUtil.removeAcentos(xml);
    }

    public static NFS transmitirNfse(NFS nfs, String xmlAssinado) throws JAXBException, NfeException, AxisFault, RemoteException {

        iniciaConfiguracoes();

        // cabecalho
        String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

        // input
        NfseWSServiceStub.GerarNfse gerarNfse = new NfseWSServiceStub.GerarNfse();
        gerarNfse.setNfseCabecMsg(xmlCabecalho);
        gerarNfse.setNfseDadosMsg(xmlAssinado);

        // request
        NfseWSServiceStub.GerarNfseE gerarNfseRequest = new NfseWSServiceStub.GerarNfseE();
        gerarNfseRequest.setGerarNfse(gerarNfse);

        // response
        NfseWSServiceStub.GerarNfseResponseE gerarNfseResponseE = new NfseWSServiceStub.GerarNfseResponseE();
        gerarNfseResponseE = stub1.gerarNfse(gerarNfseRequest);

        // NFS-e
        return preencherRetornoNFS(nfs, gerarNfseResponseE, xmlAssinado);
    }

    public static NFS cancelarNfse(NFS nfs, CancelarNfseEnvio cancelarNfseEnvio) throws NfeException {

        try {
            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilFAPA.objectToXml(cancelarNfseEnvio);
            xml = xml.replace("xmlns:ns3=\"" + EnumNfse.FA_PA.getUrl() + "\"", "")
                    .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                    .replace("ns3:", "")
                    .replace("Request", "Envio");

            // assinatura
            xml = Assinar.assinaNfe(xml, "InfPedidoCancelamento");

            // input
            NfseWSServiceStub.CancelarNfse cancelarNfse = new NfseWSServiceStub.CancelarNfse();
            cancelarNfse.setNfseCabecMsg(xmlCabecalho);
            cancelarNfse.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.CancelarNfseE cancelarNfseRequest = new NfseWSServiceStub.CancelarNfseE();
            cancelarNfseRequest.setCancelarNfse(cancelarNfse);

            // response
            NfseWSServiceStub.CancelarNfseResponseE cancelarNfseResponse = new NfseWSServiceStub.CancelarNfseResponseE();
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
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // input
            NfseWSServiceStub.CancelarNfse cancelarNfse = new NfseWSServiceStub.CancelarNfse();
            cancelarNfse.setNfseCabecMsg(xmlCabecalho);
            cancelarNfse.setNfseDadosMsg(xmlAssinado);

            // request
            NfseWSServiceStub.CancelarNfseE cancelarNfseRequest = new NfseWSServiceStub.CancelarNfseE();
            cancelarNfseRequest.setCancelarNfse(new NfseWSServiceStub.CancelarNfse());

            // response
            NfseWSServiceStub.CancelarNfseResponseE cancelarNfseResponse = new NfseWSServiceStub.CancelarNfseResponseE();
            cancelarNfseResponse = stub1.cancelarNfse(cancelarNfseRequest);

            // NFS-e
            return preencherRetornoCancelamentoNFS(nfs, cancelarNfseResponse, xmlAssinado);

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }
    }

    public static String obterXmlCancelamentoNfs(NFS nfs, CancelarNfseEnvio cancelarNfseEnvio) throws NfeException, JAXBException {

        // corpo
        String xml = XmlUtilFAPA.objectToXml(cancelarNfseEnvio);
        xml = xml.replace("xmlns:ns3=\"" + EnumNfse.FA_PA.getUrl() + "\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");

        return xml;
    }

    public static ConsultarLoteRpsResponseE consultarSituacaoLoteRps(ConsultarLoteRpsEnvio consultarLoteRpsEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilFAPA.objectToXml(consultarLoteRpsEnvio);
            xml = xml.replace("xmlns:ns2=\"" + EnumNfse.FA_PA.getUrl() + "\"", "");
            xml = xml.replace("ns2:", "");
            xml = xml.replace("Request", "Envio");

            // input
            NfseWSServiceStub.ConsultarLoteRps consultarLoteRps = new NfseWSServiceStub.ConsultarLoteRps();
            consultarLoteRps.setNfseCabecMsg(xmlCabecalho);
            consultarLoteRps.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.ConsultarLoteRpsE consultarSituacaoLoteRpsRequest = new NfseWSServiceStub.ConsultarLoteRpsE();
            consultarSituacaoLoteRpsRequest.setConsultarLoteRps(consultarLoteRps);

            // response
            NfseWSServiceStub.ConsultarLoteRpsResponseE consultarLoteRpsResponseE = stub1.consultarLoteRps(consultarSituacaoLoteRpsRequest);
            return consultarLoteRpsResponseE;

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    public static ConsultarNfsePorRpsResponseE consultarNfse(ConsultarNfseRpsEnvio consultarNfseRpsEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilFAPA.objectToXml(consultarNfseRpsEnvio);
            xml = xml.replace("xmlns:ns2=\"" + EnumNfse.FA_PA.getUrl() + "\"", "");
            xml = xml.replace("ns2:", "");
            xml = xml.replace("Request", "Envio");

            // input
            NfseWSServiceStub.ConsultarNfsePorRps consultarNfsePorRps = new NfseWSServiceStub.ConsultarNfsePorRps();
            consultarNfsePorRps.setNfseCabecMsg(xmlCabecalho);
            consultarNfsePorRps.setNfseDadosMsg(xml);

            // request
            NfseWSServiceStub.ConsultarNfsePorRpsE consultarNfsePorRpsE = new NfseWSServiceStub.ConsultarNfsePorRpsE();
            consultarNfsePorRpsE.setConsultarNfsePorRps(consultarNfsePorRps);

            // reponse
            NfseWSServiceStub.ConsultarNfsePorRpsResponseE consultarNfsePorRpsResponseE = stub1.consultarNfsePorRps(consultarNfsePorRpsE);
            return consultarNfsePorRpsResponseE;

        } catch (Exception ex) {
            throw new NfeException(ex.getMessage(), ex);
        }

    }

    private static NFS preencherRetornoNFS(NFS nfs, GerarNfseResponseE response, String xmlAssinado) throws JAXBException, NfeException {
        try {
            GerarNfseResposta gerarNfseResposta = XmlUtilFAPA.xmlToObject(response.getGerarNfseResponse().get_return(), GerarNfseResposta.class);
            nfs.setXmlEnvio(xmlAssinado);
            nfs.setXmlRetorno(response.getGerarNfseResponse().get_return());

            verificarEnvioComErro(gerarNfseResposta);

            String xmlIntermediarioRevertido = NFSeGerneric.reverterXmlIntermediario(response.getGerarNfseResponse().get_return());
            nfs.setXmlArmazenamento(gerarXmlArmazenamento(gerarNfseResposta, xmlIntermediarioRevertido));
            nfs.setMensagemErroEnvio(null);
            nfs.setSituacao(EnumSituacaoNF.ENVIADA.getSituacao());
            nfs.setNumeroNotaFiscal(gerarNfseResposta.getListaNfse().getCompNfse().getNfse().getInfNfse().getNumero().toString());
        } catch (NfeException ex) {
            nfs.setMensagemErroEnvio(ex.getMessage());
            nfs.setSituacao(EnumSituacaoNF.REJEITADA.getSituacao());
        }

        return nfs;
    }

    private static void verificarEnvioComErro(GerarNfseResposta gerarNfseResposta) throws NfeException {
        StringBuilder msgErro = new StringBuilder();

        ListaMensagemRetorno mr = gerarNfseResposta.getListaMensagemRetorno();

        ListaMensagemAlertaRetorno listaMensagemAlertaRetorno = null;
        if (gerarNfseResposta.getListaNfse() != null) {
            listaMensagemAlertaRetorno = gerarNfseResposta.getListaNfse().getListaMensagemAlertaRetorno();
        }

        if (mr != null) {
            mr.getMensagemRetorno().stream().forEach(msg -> msgErro.append(msg.getCodigo()).append(" : ").append(msg.getMensagem()).append("\n"));
        }

        if (listaMensagemAlertaRetorno != null) {
            listaMensagemAlertaRetorno.getMensagemRetorno().stream().forEach(msg -> msgErro.append(msg.getCodigo()).append(" : ").append(msg.getMensagem()).append("\n"));
        }

        if (!StringUtils.isEmpty(msgErro)) {
            throw new NfeException("NFS rejeitada \n " + msgErro.toString(), null);
        }
    }

    private static NFS preencherRetornoCancelamentoNFS(NFS nfs, CancelarNfseResponseE response, String xmlAssinado) throws JAXBException, NfeException {

        try {
            CancelarNfseResposta cancelarNfseResposta = XmlUtil.xmlToObject(response.getCancelarNfseResponse().get_return(), CancelarNfseResposta.class);
            nfs.setXmlEnvioCancelamento(xmlAssinado);
            nfs.setXmlRetornoCancelamento(response.getCancelarNfseResponse().get_return());

            verificarCancelamentoComErro(cancelarNfseResposta);

            // monta o xml de cancelamento da nota
            TcCompNfse compNfse = XmlUtil.xmlToObject(nfs.getXmlArmazenamento(), TcCompNfse.class);
            compNfse.setNfseCancelamento(cancelarNfseResposta.getRetCancelamento().getNfseCancelamento());

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

    private static String gerarXmlArmazenamento(GerarNfseResposta gerarNfseResposta, String xmlIntermediarioRevertido) throws JAXBException, NfeException {
        String xml = null;

        TcCompNfse tcCompNfse = gerarNfseResposta.getListaNfse().getCompNfse();

        xml = XmlUtil.objectToXml(tcCompNfse);
        xml = xml.replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "");
        xml = xml.replace("<CompNfse>", "<CompNfse xmlns=\"http://www.abrasf.org.br/nfse.xsd\">");
        xml = xml.replace("<Nfse versao=\"1.00\">", "<Nfse xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\">");
        xml = xml.replace("ns2:", "");

        if (StringUtils.isNotEmpty(xmlIntermediarioRevertido)) {
            xml = xml.replace(xml.substring(xml.indexOf("<IntermediarioServico>"), xml.indexOf("</IntermediarioServico>") + 23), xmlIntermediarioRevertido);
        }

        return xml;
    }

    //---------------------------------------------------------------
    public static String gerarXmlEnvioNfse(NFS nfs, Empresa empresa, String ambiente, Integer numeroRps) throws NfeException, DatatypeConfigurationException {

        try {

            Cabecalho cabecalho = new Cabecalho();
            cabecalho.setVersao("1");
            cabecalho.setVersaoDados("1");

            // Obter numero NFS caso nao exista
            nfs.setNumeroRPS(numeroRps);

            // TcValoresDeclaracaoServico
            TcValoresDeclaracaoServico tcValores = preencherTcValoresDeclaracaoServico(nfs);

            // TcDadosServico
            TcDadosServico tcDadosServico = preencherTcDadosServico(nfs, tcValores);

            // TcDadosIntermediario
            TcDadosIntermediario tcDadosIntermediario = preencherTcDadosIntermediario(nfs);

            // TcDadosTomador
            TcDadosTomador tcDadosTomador = preencherTcDadosTomador(nfs);

            // TcIdentificacaoPrestador
            TcIdentificacaoPrestador tcIdentificacaoPrestador = preencherTcIdentificacaoPrestador(empresa);

            // TcDadosConstrucaoCivil
            TcDadosConstrucaoCivil tcDadosConstrucaoCivil = preencherTcDadosConstrucaoCivil(nfs);

            // TcIdentificacaoRps
            TcIdentificacaoRps tcIdentificacaoRps = preencherTcIdentificacaoRps(nfs);

            // IdentificacaoRps
            TcInfRps tcInfRps = preencherTcInfRps(nfs, tcIdentificacaoRps);

            // TcInfDeclaracaoPrestacaoServico
            TcInfDeclaracaoPrestacaoServico tcInfDeclaracaoPrestacaoServico = preencherTcTcInfDeclaracaoPrestacaoServico(nfs, tcDadosTomador, tcIdentificacaoPrestador, tcDadosServico, tcDadosIntermediario, tcDadosConstrucaoCivil, tcInfRps);

            // TcDeclaracaoPrestacaoServico
            TcDeclaracaoPrestacaoServico tcDeclaracaoPrestacaoServico = preencherTcDeclaracaoPrestacaoServico(tcInfDeclaracaoPrestacaoServico);

            // TcLoteRps
            TcLoteRps tcLoteRps = preencherTcLoteRps(nfs, empresa, tcDeclaracaoPrestacaoServico);

            // EnviarLoteRpsEnvio
            EnviarLoteRpsEnvio enviarLoteRpsEnvio = preencherEnviarLoteRpsEnvio(tcLoteRps);

            // TcDadosPrestador
            TcDadosPrestador tcDadosPrestador = preencherTcDadosPrestador(empresa, tcIdentificacaoPrestador);

            // TcValoresNfse
            TcValoresNfse tcValoresNfse = preencherTcValoresNfse(nfs);

            // TcIdentificacaoOrgaoGerador
            TcIdentificacaoOrgaoGerador tcIdentificacaoOrgaoGerador = preencherTcIdentificacaoOrgaoGerador(nfs);

            // TcInfNfse
            TcInfNfse tcInfNfse = preencherTcInfNfse(nfs, tcDadosPrestador, tcValoresNfse, tcIdentificacaoOrgaoGerador, tcDeclaracaoPrestacaoServico);

            // TcNfse
            TcNfse tcNfse = preencherTcNfse(tcInfNfse);

            TcCompNfse tcCompNfse = new TcCompNfse();
            tcCompNfse.setNfse(tcNfse);

            // Gerar object envio
//            GerarNfseEnvio gerarNfseEnvio = new GerarNfseEnvio();
//            gerarNfseEnvio.setRps(tcDeclaracaoPrestacaoServico);
            // Gerar xml
            return obterXmlNfse(tcCompNfse);

        } catch (Exception ex) {
            throw new NfeException("Falha ao transmitir NFS: " + ex.getMessage(), ex);
        }
    }

    public static String obterXmlCancelamento(NFS nfs, Empresa empresa) throws NfeException {

        try {

            Cabecalho cabecalho = new Cabecalho();
            cabecalho.setVersao("1");
            cabecalho.setVersaoDados("1");

            TcIdentificacaoNfse tcIdentificacaoNfse = new TcIdentificacaoNfse();
            tcIdentificacaoNfse.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
            tcIdentificacaoNfse.setCodigoMunicipio(Integer.parseInt(nfs.getIdMunicipioISSQN().getCodigoIBGE()));
            tcIdentificacaoNfse.setNumero(new BigInteger(nfs.getNumeroNotaFiscal()));

            TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
            tcCpfCnpj.setCnpj(CpfCnpjUtil.removerMascaraCnpj(nfs.getCpfCnpjIntermediario()));
            tcIdentificacaoNfse.setCpfCnpj(tcCpfCnpj);

            TcInfPedidoCancelamento tcInfPedidoCancelamento = new TcInfPedidoCancelamento();
            tcInfPedidoCancelamento.setCodigoCancelamento(Byte.parseByte(nfs.getCodigoCancelamento()));
            tcInfPedidoCancelamento.setId(nfs.getNumeroRPS().toString());
            tcInfPedidoCancelamento.setIdentificacaoNfse(tcIdentificacaoNfse);

            TcPedidoCancelamento pedidoCancelamento = new TcPedidoCancelamento();
            pedidoCancelamento.setInfPedidoCancelamento(tcInfPedidoCancelamento);

            CancelarNfseEnvio cancelarNfseEnvio = new CancelarNfseEnvio();
            cancelarNfseEnvio.setPedido(pedidoCancelamento);

            // Salvar NFS com os dados da transmição da nota
            return obterXmlCancelamentoNfs(nfs, cancelarNfseEnvio);

        } catch (Exception ex) {
            throw new NfeException("Falha ao cancelar NFS: " + ex.getMessage(), ex);
        }

    }

    private static TcIdentificacaoRps preencherTcIdentificacaoRps(NFS nfs) {
        // RpsSubstituido
        TcIdentificacaoRps tir = new TcIdentificacaoRps();
        tir.setNumero(new BigInteger(nfs.getNumeroRPS().toString()));
        tir.setSerie("1");
        tir.setTipo(Byte.valueOf("1"));

        return tir;
    }

    private static TcValoresNfse preencherTcValoresNfse(NFS nfs) throws NfeException {
        try {

            TcValoresNfse tv = new TcValoresNfse();
            tv.setValorLiquidoNfse(NumeroUtil.converterBigDecimal(nfs.getValorTotal()));

            // calcula o valor do iss
            Double valorIss = nfs.getValorTotal() / 100 * nfs.getIdCtiss().getAliquota();
            tv.setValorIss(NumeroUtil.converterBigDecimal(nfs.getValorIss()));
            tv.setAliquota(NumeroUtil.converterBigDecimal(nfs.getIdCtiss().getAliquota()));

            if (nfs.getIssRetido().equals("S")) {
                nfs.setValorIssRetido(valorIss);
            }

            //        tcValores.setBaseCalculo(new BigDecimal(5000));
            return tv;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre valores monetários inválidos para NFS", ex);
        }
    }

    private static TcValoresDeclaracaoServico preencherTcValoresDeclaracaoServico(NFS nfs) throws NfeException {
        try {

            TcValoresDeclaracaoServico tv = new TcValoresDeclaracaoServico();
            tv.setValorServicos(NumeroUtil.converterBigDecimal(nfs.getValorTotal()));
            tv.setValorDeducoes(NumeroUtil.converterBigDecimal(nfs.getValorDeducoes()));
            tv.setValorPis(NumeroUtil.converterBigDecimal(nfs.getValorPis()));
            tv.setValorCofins(NumeroUtil.converterBigDecimal(nfs.getValorCofins()));
            tv.setValorInss(NumeroUtil.converterBigDecimal(nfs.getValorInss()));
            tv.setValorIr(NumeroUtil.converterBigDecimal(nfs.getValorIr()));
            tv.setValorCsll(NumeroUtil.converterBigDecimal(nfs.getValorCsll()));
            tv.setDescontoCondicionado(NumeroUtil.converterBigDecimal(nfs.getDescontoCondicionado()));
            tv.setDescontoIncondicionado(NumeroUtil.converterBigDecimal(nfs.getDescontoIncondicionado()));

            // calcula o valor do iss
            Double valorIss = nfs.getValorTotal() / 100 * nfs.getIdCtiss().getAliquota();
            tv.setValorIss(NumeroUtil.converterBigDecimal(nfs.getValorIss()));
            tv.setAliquota(NumeroUtil.converterBigDecimal(nfs.getIdCtiss().getAliquota()));

            if (nfs.getIssRetido().equals("S")) {
                nfs.setValorIssRetido(valorIss);
            }

            //        tcValores.setBaseCalculo(new BigDecimal(5000));
            return tv;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre valores monetários inválidos para NFS", ex);
        }
    }

    private static TcDadosServico preencherTcDadosServico(NFS nfs, TcValoresDeclaracaoServico tcValores) throws NfeException {
        try {

            TcDadosServico tds = new TcDadosServico();
            tds.setValores(tcValores);
            tds.setDiscriminacao(nfs.getDescricaoServico());
            tds.setCodigoMunicipio(Integer.parseInt(nfs.getIdMunicipioISSQN().getCodigoIBGE()));
            tds.setCodigoTributacaoMunicipio(CpfCnpjUtil.removerMascaraCnpj(nfs.getIdCtiss().getCodigo()));
            tds.setItemListaServico(nfs.getIdCtiss().getSubitem());

            return tds;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre serviços inválidos para NFS", ex);
        }
    }

    private static TcDadosIntermediario preencherTcDadosIntermediario(NFS nfs) {

        // FIX-ME: intermediario com erro no xml
        if (!StringUtils.isEmpty(nfs.getTipoPessoaIntermediario())) {

            TcDadosIntermediario tdi = new TcDadosIntermediario();
            TcIdentificacaoIntermediario tid = new TcIdentificacaoIntermediario();
            tdi.setRazaoSocial(nfs.getNomeIntermediario());

            if ("PJ".equals(nfs.getTipoPessoaIntermediario())) {
                if (StringUtils.isNotEmpty(nfs.getInscricaoMunicipalIntermediario())) {
                    tid.setInscricaoMunicipal(nfs.getInscricaoMunicipalIntermediario());
                }
                TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
                tcCpfCnpj.setCnpj(CpfCnpjUtil.removerMascaraCnpj(nfs.getCpfCnpjIntermediario()));
                tid.setCpfCnpj(tcCpfCnpj);

            } else if ("PF".equals(nfs.getTipoPessoaIntermediario())) {
                TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
                tcCpfCnpj.setCpf(CpfCnpjUtil.removerMascaraCnpj(nfs.getCpfCnpjIntermediario()));
                tid.setCpfCnpj(tcCpfCnpj);
            }

            tdi.setIdentificacaoIntermediario(tid);
            return tdi;
        }

        return null;

    }

    private static TcDadosTomador preencherTcDadosTomador(NFS nfs) throws NfeException {
        try {
            // Endereco tomador
            TcEndereco tcEndereco = new TcEndereco();
            tcEndereco.setBairro(nfs.getBairroCliente());
            tcEndereco.setCep(nfs.getCepCliente());
            tcEndereco.setEndereco(nfs.getEnderecoCliente());
            tcEndereco.setNumero(nfs.getNumeroCliente());
            if (nfs.getIdCidadeCliente() != null) {
                tcEndereco.setCodigoMunicipio(Integer.parseInt(nfs.getIdCidadeCliente().getCodigoIBGE()));
                tcEndereco.setUf(nfs.getIdCidadeCliente().getIdUF().getDescricao());
            }

            // Identificacao tomador
            TcIdentificacaoTomador tit = new TcIdentificacaoTomador();
            if ("PJ".equals(nfs.getTipoPessoaCliente())) {
                TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
                tcCpfCnpj.setCnpj(CpfCnpjUtil.removerMascaraCnpj(nfs.getCpfCnpjCliente()));

                tit.setCpfCnpj(tcCpfCnpj);

                if (StringUtils.isNotEmpty(nfs.getInscricaoMunicipalCliente())) {
                    tit.setInscricaoMunicipal(nfs.getInscricaoMunicipalCliente());
                }

            } else {
                TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
                tcCpfCnpj.setCpf(CpfCnpjUtil.removerMascaraCnpj(nfs.getCpfCnpjCliente()));

                tit.setCpfCnpj(tcCpfCnpj);
            }

            // Contato tomador
            TcContato tc = new TcContato();
            if (!StringUtils.isEmpty(nfs.getEmailCliente())) {
                tc.setEmail(nfs.getEmailCliente());
            }
            if (!StringUtils.isEmpty(nfs.getTelefoneCliente())) {
                tc.setTelefone(StringUtil.removerCaracteresTelefone(nfs.getTelefoneCliente()));
            }

            TcDadosTomador tdt = new TcDadosTomador();
            tdt.setRazaoSocial(nfs.getNomeCliente());
            tdt.setContato(tc);
            tdt.setIdentificacaoTomador(tit);
            tdt.setEndereco(tcEndereco);

            return tdt;

        } catch (Exception ex) {
            throw new NfeException("Informações sobre o cliente inválidos para NFS", ex);
        }
    }

    private static TcIdentificacaoPrestador preencherTcIdentificacaoPrestador(Empresa empresa) throws NfeException {
        // Identificação do prestador
        TcIdentificacaoPrestador tip = new TcIdentificacaoPrestador();
        tip.setInscricaoMunicipal(empresa.getInscricaoMunicipal());

        TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
        tcCpfCnpj.setCnpj(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
        tip.setCpfCnpj(tcCpfCnpj);

        return tip;

    }

    private static TcDadosPrestador preencherTcDadosPrestador(Empresa empresa, TcIdentificacaoPrestador tcIdentificacaoPrestador) throws NfeException {
        try {

            // Endereco prestador
            TcEndereco tcEndereco = new TcEndereco();
            tcEndereco.setBairro(empresa.getEndereco().getBairro());
            tcEndereco.setCep(empresa.getEndereco().getCep());
            tcEndereco.setEndereco(empresa.getEndereco().getEndereco());
            tcEndereco.setNumero(empresa.getEndereco().getNumero());
            if (empresa.getEndereco().getIdCidade() != null) {
                tcEndereco.setCodigoMunicipio(Integer.parseInt(empresa.getEndereco().getIdCidade().getCodigoIBGE()));
                tcEndereco.setUf(empresa.getEndereco().getIdCidade().getIdUF().getDescricao());
            }

            TcContato tc = new TcContato();
            if (!StringUtils.isEmpty(empresa.getEmail())) {
                tc.setEmail(empresa.getEmail());
            }

            TcDadosPrestador tdp = new TcDadosPrestador();
            tdp.setNomeFantasia(empresa.getNomeFantasia());
            tdp.setRazaoSocial(empresa.getDescricao());
            tdp.setContato(tc);
            tdp.setEndereco(tcEndereco);
            tdp.setIdentificacaoPrestador(tcIdentificacaoPrestador);

            return tdp;

        } catch (Exception ex) {
            throw new NfeException("Informações sobre a empresa inválidos para NFS", ex);
        }
    }

    private static TcDadosConstrucaoCivil preencherTcDadosConstrucaoCivil(NFS nfs) {
        if (nfs.getNumeroCei() != null || nfs.getNumeroArt() != null) {

            TcDadosConstrucaoCivil tdcc = new TcDadosConstrucaoCivil();
            if (nfs.getNumeroCei() != null) {
                tdcc.setCodigoObra(nfs.getNumeroCei());
            }
            if (nfs.getNumeroArt() != null) {
                tdcc.setArt(nfs.getNumeroArt());
            }

            return tdcc;
        }
        return null;
    }

    private static TcInfDeclaracaoPrestacaoServico preencherTcTcInfDeclaracaoPrestacaoServico(NFS nfs, TcDadosTomador tcDadosTomador, TcIdentificacaoPrestador tcIdentificacaoPrestador, TcDadosServico tcDadosServico, TcDadosIntermediario tcDadosIntermediario, TcDadosConstrucaoCivil tcDadosConstrucaoCivil, TcInfRps tcInfRps) throws DatatypeConfigurationException {

        TcInfDeclaracaoPrestacaoServico tidps = new TcInfDeclaracaoPrestacaoServico();
        tidps.setRps(tcInfRps);
        tidps.setServico(tcDadosServico);
        tidps.setPrestador(tcIdentificacaoPrestador);
        tidps.setTomador(tcDadosTomador);
        tidps.setCompetencia(DataUtil.converterDateParaXmlGregorianCalendar(nfs.getDataEmissao()));
//        tidps.setNaturezaOperacao(Byte.valueOf(nfs.getNaturezaOperacao()));
        tidps.setOptanteSimplesNacional(converterYesOrNo(nfs.getOptaSimples()));
        tidps.setIncentivoFiscal(converterYesOrNo(nfs.getIncentivadorCultural()));
        if (tcDadosIntermediario != null) {
            tidps.setIntermediario(tcDadosIntermediario);
        }
        if (tcDadosConstrucaoCivil != null) {
            tidps.setConstrucaoCivil(tcDadosConstrucaoCivil);
        }

        if (StringUtils.isNotEmpty(nfs.getRegimeTributacao())) {
            tidps.setRegimeEspecialTributacao(Byte.valueOf(nfs.getRegimeTributacao()));
        }

        return tidps;
    }

    private static TcDeclaracaoPrestacaoServico preencherTcDeclaracaoPrestacaoServico(TcInfDeclaracaoPrestacaoServico tidps) {
        TcDeclaracaoPrestacaoServico tdps = new TcDeclaracaoPrestacaoServico();
        tdps.setInfDeclaracaoPrestacaoServico(tidps);

        return tdps;
    }

    private static TcIdentificacaoOrgaoGerador preencherTcIdentificacaoOrgaoGerador(NFS nfs) {

        TcIdentificacaoOrgaoGerador tiog = new TcIdentificacaoOrgaoGerador();
        tiog.setCodigoMunicipio(Integer.parseInt(nfs.getIdMunicipioISSQN().getCodigoIBGE()));
        tiog.setUf(nfs.getIdMunicipioISSQN().getIdUF().getDescricao());

        return tiog;
    }

    private static TcInfNfse preencherTcInfNfse(NFS nfs, TcDadosPrestador tcDadosPrestador, TcValoresNfse tcValoresNfse, TcIdentificacaoOrgaoGerador tcIdentificacaoOrgaoGerador, TcDeclaracaoPrestacaoServico tcDeclaracaoPrestacaoServico) throws NfeException {
        try {
            TcInfNfse tcInfNfse = new TcInfNfse();
            tcInfNfse.setNumero(new BigInteger(nfs.getNumeroRPS().toString()));
            tcInfNfse.setCodigoVerificacao("codigoVerificacao" + nfs.getId().toString());
            tcInfNfse.setDataEmissao(DataUtil.converterDateParaXmlGregorianCalendar(nfs.getDataEmissao()));
//            tcInfNfse.setNfseSubstituida(BigInteger.ZERO);
            tcInfNfse.setOutrasInformacoes(nfs.getIdVenda().getObservacao());
            tcInfNfse.setValoresNfse(tcValoresNfse);
            tcInfNfse.setPrestadorServico(tcDadosPrestador);
            tcInfNfse.setOrgaoGerador(tcIdentificacaoOrgaoGerador);
            tcInfNfse.setDeclaracaoPrestacaoServico(tcDeclaracaoPrestacaoServico);

            return tcInfNfse;
        } catch (Exception ex) {
            throw new NfeException("Informações inválidas sobre a NFS", ex);
        }
    }

    private static TcNfse preencherTcNfse(TcInfNfse tcInfNfse) {

        TcNfse tcNfse = new TcNfse();
        tcNfse.setInfNfse(tcInfNfse);
        tcNfse.setVersao("1.00");

        return tcNfse;
    }

    private static TcInfRps preencherTcInfRps(NFS nfs, TcIdentificacaoRps tcIdentificacaoRps) throws NfeException {

        try {
            TcInfRps tir = new TcInfRps();

            tir.setId("infrps" + nfs.getId().toString());

            tir.setDataEmissao(DataUtil.converterDateParaXmlGregorianCalendar(nfs.getDataEmissao()));
            tir.setStatus(Byte.valueOf("1")); // 1- Normal 2-Cancelado
            tir.setIdentificacaoRps(tcIdentificacaoRps);

            return tir;
        } catch (Exception ex) {
            throw new NfeException("Informações inválidas sobre a NFS", ex);
        }
    }

    private static TcLoteRps preencherTcLoteRps(NFS nfs, Empresa empresa, TcDeclaracaoPrestacaoServico tcDeclaracaoPrestacaoServico) throws NfeException {

        try {

            TcLoteRps.ListaRps listaRps = new TcLoteRps.ListaRps();
            listaRps.getRps().add(tcDeclaracaoPrestacaoServico);

            TcLoteRps tlr = new TcLoteRps();
            tlr.setId("loterps" + nfs.getId().toString());
            tlr.setVersao("1.00");
            tlr.setNumeroLote(new BigInteger(nfs.getNumeroRPS().toString()));
            tlr.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
            tlr.setQuantidadeRps(1);
            tlr.setListaRps(listaRps);

            TcCpfCnpj tcCpfCnpj = new TcCpfCnpj();
            tcCpfCnpj.setCnpj(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
            tlr.setCpfCnpj(tcCpfCnpj);

            return tlr;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre o lote inválidos para NFS", ex);
        }
    }

    private static EnviarLoteRpsEnvio preencherEnviarLoteRpsEnvio(TcLoteRps tcLoteRps) throws NfeException {
        EnviarLoteRpsEnvio elre = new EnviarLoteRpsEnvio();
        elre.setLoteRps(tcLoteRps);

        return elre;
    }

    private static Byte converterYesOrNo(String value) {
        if (value != null) {
            return "S".equals(value) ? Byte.valueOf("1") : Byte.valueOf("2");
        }

        return Byte.valueOf("2");
    }

    private NFSeFAPA() {
    }

}
