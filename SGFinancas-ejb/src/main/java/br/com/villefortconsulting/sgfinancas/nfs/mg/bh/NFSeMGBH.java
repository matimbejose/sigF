package br.com.villefortconsulting.sgfinancas.nfs.mg.bh;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.nfe.Assinar;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.CertificadoUtil;
import br.com.villefortconsulting.sgfinancas.nfe.util.UrlWebServiceUtil;
import br.com.villefortconsulting.sgfinancas.nfs.EnumNfse;
import br.com.villefortconsulting.sgfinancas.nfs.NFSeGerneric;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
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
import br.gov.pbh.schema.nfse.TcContato;
import br.gov.pbh.schema.nfse.TcCpfCnpj;
import br.gov.pbh.schema.nfse.TcDadosIntermediario;
import br.gov.pbh.schema.nfse.TcDadosServico;
import br.gov.pbh.schema.nfse.TcDadosTomador;
import br.gov.pbh.schema.nfse.TcEndereco;
import br.gov.pbh.schema.nfse.TcIdentificacaoIntermediario;
import br.gov.pbh.schema.nfse.TcIdentificacaoNfse;
import br.gov.pbh.schema.nfse.TcIdentificacaoPrestador;
import br.gov.pbh.schema.nfse.TcIdentificacaoRps;
import br.gov.pbh.schema.nfse.TcIdentificacaoTomador;
import br.gov.pbh.schema.nfse.TcInfPedidoCancelamento;
import br.gov.pbh.schema.nfse.TcInfRps;
import br.gov.pbh.schema.nfse.TcLoteRps3;
import br.gov.pbh.schema.nfse.TcPedidoCancelamento;
import br.gov.pbh.schema.nfse.TcRps;
import br.gov.pbh.schema.nfse.TcValores;
import java.math.BigInteger;
import java.rmi.RemoteException;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import org.apache.axis2.AxisFault;
import org.apache.commons.lang3.StringUtils;

public class NFSeMGBH {

    private static NfseWSServiceStub.Input input;

    private static NfseWSServiceStub stub1;

    private static CertificadoUtil certUtil;

    private static void iniciaConfiguracoes() throws NfeException, AxisFault {
        certUtil = new CertificadoUtil();
        certUtil.iniciaConfiguracoes();
        input = new NfseWSServiceStub.Input();
        stub1 = new NfseWSServiceStub(UrlWebServiceUtil.notaFiscalServicoBHMG().toString());
    }

    private static String obterXmlNfse(GerarNfseEnvio gerarNfseEnvio) throws JAXBException, NfeException {
        String xml = XmlUtilBHMG.objectToXml(gerarNfseEnvio);
        xml = xml.replace("xmlns:ns3=\"" + EnumNfse.BH_MG.getUrl() + "\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");
        xml = NFSeGerneric.converterXmlIntermediario(xml);

        return XmlUtilBHMG.removeAcentos(xml);
    }

    public static NFS transmitirNfse(NFS nfs, String xmlAssinado) throws JAXBException, NfeException, AxisFault, RemoteException {

        iniciaConfiguracoes();

        // cabecalho
        String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

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
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilBHMG.objectToXml(cancelarNfseEnvio);
            xml = xml.replace("xmlns:ns3=\"" + EnumNfse.BH_MG.getUrl() + "\"", "")
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
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

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

    public static String obterXmlCancelamentoNfs(NFS nfs, CancelarNfseEnvio cancelarNfseEnvio) throws NfeException, JAXBException {

        // corpo
        String xml = XmlUtilBHMG.objectToXml(cancelarNfseEnvio);
        xml = xml.replace("xmlns:ns3=\"" + EnumNfse.BH_MG.getUrl() + "\"", "")
                .replace("xmlns:ns2=\"http://www.w3.org/2000/09/xmldsig#\"", "")
                .replace("ns3:", "")
                .replace("Request", "Envio");

        return xml;
    }

    private static ConsultarSituacaoLoteRpsResponse consultarSituacaoLoteRps(ConsultarSituacaoLoteRpsEnvio consultarSituacaoLoteRpsEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilBHMG.objectToXml(consultarSituacaoLoteRpsEnvio);
            xml = xml.replace("xmlns:ns2=\"" + EnumNfse.BH_MG.getUrl() + "\"", "");
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

    private static ConsultarNfseResponse consultarNfse(ConsultarNfseEnvio consultarNfseEnvio) throws NfeException {

        try {

            iniciaConfiguracoes();

            // cabecalho
            String xmlCabecalho = NFSeGerneric.gerarCabecalhoXml();

            // corpo
            String xml = XmlUtilBHMG.objectToXml(consultarNfseEnvio);
            xml = xml.replace("xmlns:ns2=\"" + EnumNfse.BH_MG.getUrl() + "\"", "");
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

    public static String assinarXmlCancelamentoNfs(String xml) throws NfeException, JAXBException {
        return Assinar.assinaNfe(xml, "InfPedidoCancelamento");
    }

    private static NFS preencherRetornoNFS(NFS nfs, NfseWSServiceStub.GerarNfseResponse response, String xmlAssinado) throws JAXBException, NfeException {
        try {
            GerarNfseResposta gerarNfseResposta = XmlUtilBHMG.xmlToObject(response.getGerarNfseResponse().getOutputXML(), GerarNfseResposta.class);
            nfs.setProtocolo(gerarNfseResposta.getProtocolo());
            nfs.setXmlEnvio(xmlAssinado);
            nfs.setXmlRetorno(response.getGerarNfseResponse().getOutputXML());

            verificarEnvioComErro(gerarNfseResposta);

            String xmlIntermediarioRevertido = NFSeGerneric.reverterXmlIntermediario(response.getGerarNfseResponse().getOutputXML());
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
            CancelarNfseResposta cancelarNfseResposta = XmlUtilBHMG.xmlToObject(response.getCancelarNfseResponse().getOutputXML(), CancelarNfseResposta.class);
            nfs.setXmlEnvioCancelamento(xmlAssinado);
            nfs.setXmlRetornoCancelamento(response.getCancelarNfseResponse().getOutputXML());

            verificarCancelamentoComErro(cancelarNfseResposta);

            // monta o xml de cancelamento da nota
            TcCompNfse compNfse = XmlUtilBHMG.xmlToObject(nfs.getXmlArmazenamento(), TcCompNfse.class);
            compNfse.setNfseCancelamento(cancelarNfseResposta.getRetCancelamento().getNfseCancelamento().get(0));

            nfs.setXmlArmazenamentoCancelamento(XmlUtilBHMG.objectToXml(compNfse));
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
        for (TcCompNfse tcCompNfse : gerarNfseResposta.getListaNfse().getCompNfse()) {

            xml = XmlUtilBHMG.objectToXml(tcCompNfse);
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

    //---------------------------------------------------------------
    public static String gerarXmlEnvioNfse(NFS nfs, Empresa empresa, String ambiente, Integer numeroRps) throws NfeException, DatatypeConfigurationException {

        try {

            Cabecalho cabecalho = new Cabecalho();
            cabecalho.setVersao("1");
            cabecalho.setVersaoDados("1");

            // Obter numero NFS caso nao exista
            nfs.setNumeroRPS(numeroRps);

            // RpsSubstituido
            TcIdentificacaoRps tcIdentificacaoRps = preencherTcIdentificacaoRps(nfs);

            // Valores
            TcValores tcValores = preencherTcValores(nfs);

            // Servico
            TcDadosServico tcDadosServico = preencherTcDadosServico(nfs, tcValores);

            // TcDadosIntermediario
            TcDadosIntermediario tcDadosIntermediario = preencherTcDadosIntermediario(nfs);

            // TcDadosTomador
            TcDadosTomador tcDadosTomador = preencherTcDadosTomador(nfs);

            // TcIdentificacaoPrestador
            TcIdentificacaoPrestador tcIdentificacaoPrestador = preencherTcIdentificacaoPrestador(empresa);

            // IdentificacaoRps
            TcInfRps tcInfRps = preencherTcInfRps(nfs, tcDadosTomador, tcIdentificacaoPrestador, tcDadosServico, tcIdentificacaoRps, tcDadosIntermediario);

            // Id do lote
            TcLoteRps3 tcLoteRps3 = preencherTcLoteRps3(nfs, empresa, tcInfRps);

            // Gerar object envio
            GerarNfseEnvio gerarNfseEnvio = new GerarNfseEnvio();
            gerarNfseEnvio.setLoteRps(tcLoteRps3);

            // Gerar xml
            return obterXmlNfse(gerarNfseEnvio);

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
            tcIdentificacaoNfse.setCnpj(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
            tcIdentificacaoNfse.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
            tcIdentificacaoNfse.setCodigoMunicipio(Integer.parseInt(nfs.getIdMunicipioISSQN().getCodigoIBGE()));
            tcIdentificacaoNfse.setNumero(new BigInteger(nfs.getNumeroNotaFiscal()));

            TcInfPedidoCancelamento tcInfPedidoCancelamento = new TcInfPedidoCancelamento();
            tcInfPedidoCancelamento.setCodigoCancelamento(nfs.getCodigoCancelamento());
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

    private static TcValores preencherTcValores(NFS nfs) throws NfeException {
        try {
            TcValores tv = new TcValores();
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
            nfs.setValorIss(valorIss);
            tv.setValorIss(NumeroUtil.converterBigDecimal(nfs.getValorIss()));

            if (nfs.getIssRetido().equals("S")) {
                tv.setIssRetido(Byte.valueOf("1"));
                nfs.setValorIssRetido(valorIss);
                tv.setValorIssRetido(NumeroUtil.converterBigDecimal(nfs.getValorIssRetido()));
            } else {
                tv.setIssRetido(Byte.valueOf("2"));
            }
            return tv;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre valores monetários inválidos para NFS", ex);
        }
    }

    private static TcDadosServico preencherTcDadosServico(NFS nfs, TcValores tcValores) throws NfeException {
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
            tcEndereco.setCep(StringUtil.converterCep(nfs.getCepCliente()));
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
        try {
            TcIdentificacaoPrestador tip = new TcIdentificacaoPrestador();
            tip.setCnpj(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
            tip.setInscricaoMunicipal(empresa.getInscricaoMunicipal());

            return tip;

        } catch (Exception ex) {
            throw new NfeException("Informações sobre a empresa inválidos para NFS", ex);
        }
    }

    private static TcInfRps preencherTcInfRps(NFS nfs, TcDadosTomador tcDadosTomador, TcIdentificacaoPrestador tcIdentificacaoPrestador, TcDadosServico tcDadosServico, TcIdentificacaoRps tcIdentificacaoRps, TcDadosIntermediario tcDadosIntermediario) throws DatatypeConfigurationException, NfeException {

        try {

            TcInfRps tir = new TcInfRps();

            tir.setId("infrps" + nfs.getId().toString());

            if (StringUtils.isNotEmpty(nfs.getRegimeTributacao())) {
                tir.setRegimeEspecialTributacao(Byte.valueOf(nfs.getRegimeTributacao()));
            }

            tir.setDataEmissao(DataUtil.converterDateParaXmlGregorianCalendar(nfs.getDataEmissao()));
            tir.setNaturezaOperacao(Byte.valueOf(nfs.getNaturezaOperacao()));
            tir.setOptanteSimplesNacional(converterYesOrNo(nfs.getOptaSimples()));
            tir.setIncentivadorCultural(converterYesOrNo(nfs.getIncentivadorCultural()));
            tir.setStatus(Byte.valueOf("1")); // 1- Normal 2-Cancelado
            tir.setIdentificacaoRps(tcIdentificacaoRps);
            tir.setServico(tcDadosServico);
            tir.setPrestador(tcIdentificacaoPrestador);
            tir.setTomador(tcDadosTomador);
            if (tcDadosIntermediario != null) {
                tir.setIntermediarioServico(tcDadosIntermediario);
            }

            return tir;
        } catch (Exception ex) {
            throw new NfeException("Informações inválidas sobre a NFS", ex);
        }
    }

    private static TcLoteRps3 preencherTcLoteRps3(NFS nfs, Empresa empresa, TcInfRps tcInfRps) throws NfeException {

        try {
            TcRps tcRps = new TcRps();
            tcRps.setInfRps(tcInfRps);

            TcLoteRps3.ListaRps listaRps = new TcLoteRps3.ListaRps();
            listaRps.getRps().add(tcRps);

            TcLoteRps3 tlr = new TcLoteRps3();
            tlr.setId("loterps" + nfs.getId().toString());
            tlr.setVersao("1.00");
            tlr.setNumeroLote(new BigInteger(nfs.getNumeroRPS().toString()));
            tlr.setCnpj(CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()));
            tlr.setInscricaoMunicipal(empresa.getInscricaoMunicipal());
            tlr.setQuantidadeRps(1);
            tlr.setListaRps(listaRps);

            return tlr;
        } catch (Exception ex) {
            throw new NfeException("Informações sobre o lote inválidos para NFS", ex);
        }
    }

    private static Byte converterYesOrNo(String value) {
        if (value != null) {
            return "S".equals(value) ? Byte.valueOf("1") : Byte.valueOf("2");
        }

        return Byte.valueOf("2");
    }

}
