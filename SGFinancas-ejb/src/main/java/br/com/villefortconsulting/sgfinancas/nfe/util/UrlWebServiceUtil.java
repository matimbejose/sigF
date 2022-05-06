package br.com.villefortconsulting.sgfinancas.nfe.util;

import br.com.villefortconsulting.sgfinancas.nfe.ConfiguracoesIniciaisNfe;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Classe reposnsavel Por Retornar o URL do WebService. url = Endere�o do WebService para cada Estado. Ver rela��o dos endere�os em": Para
 * Homologacao": http://hom.nfe.fazenda.gov.br/PORTAL/WebServices.aspx Para Produ��o": http://www.nfe.fazenda.gov.br/portal/WebServices.aspx
 *
 * @author Samuel Oliveira - samuk.exe@hotmail.com - www.samuelweb.com.br
 *
 */
public class UrlWebServiceUtil {

    private UrlWebServiceUtil() {
    }

    private static URL url;

    private static ConfiguracoesIniciaisNfe configuracaoNfe;

    private static final String ERRO_BUSCAR_URL = "Erro ao pegar Url WebService: ";

    private static final String URL_HOMOLOG_RS_CONSULTA = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx";

    private static final String URL_PROD_RS_CONSULTA = "https://nfe.svrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx";

    private static final String URL_HOMOLOG_VIRTUAL_CONSULTA = "https://hom.sefazvirtual.fazenda.gov.br/NfeConsulta2/NfeConsulta2.asmx";

    private static final String URL_PROD_VIRTUAL_CONSULTA = "https://www.sefazvirtual.fazenda.gov.br/NfeConsulta2/NfeConsulta2.asmx";

    private static final String URL_HOMOLOG_RS_STATUS = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx";

    private static final String URL_PROD_RS_STATUS = "https://nfe.svrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx";

    private static final String URL_HOMOLOG_RS_AUTORIZACAO = "https://nfe-homologacao.svrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx";

    private static final String URL_PROD_RS_AUTORIZACAO = "https://nfe.svrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx";

    private static final String URL_HOMOLOG_VIRTUAL_AUTORIZACAO = "https://hom.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx";

    private static final String URL_PROD_VIRTUAL_AUTORIZACAO = "https://www.sefazvirtual.fazenda.gov.br/NfeAutorizacao/NfeAutorizacao.asmx";

    private static final String URL_HOMOLOG_RS_INUTILIZACAO = "https://nfe-homologacao.svrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx";

    private static final String URL_PROD_RS_INUTILIZACAO = "https://nfe.svrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx";

    private static final String URL_HOMOLOG_VIRTUAL_INUTILIZACAO = "https://hom.sefazvirtual.fazenda.gov.br/NfeInutilizacao2/NfeInutilizacao2.asmx";

    private static final String URL_PROD_VIRTUAL_INUTILIZACAO = "https://www.sefazvirtual.fazenda.gov.br/NfeInutilizacao2/NfeInutilizacao2.asmx";

    private static final String URL_HOMOLOG_EVENTO = "https://hom.nfe.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx";

    private static final String URL_PROD_EVENTO = "https://nfe-homologacao.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";

    private static final String URL_PROD_RECEPCAO = "https://www.nfe.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx";

    private static final String URL_PROD_RS_RECEPCAO = "https://nfe.svrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx";

    private static final String URL_HOMOLOG_VIRTUAL_RECEPCAO = "https://hom.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx";

    private static final String URL_PROD_VIRTUAL_RECEPCAO = "https://www.sefazvirtual.fazenda.gov.br/RecepcaoEvento/RecepcaoEvento.asmx";

    private static boolean isHomologacao() {
        return configuracaoNfe.getAmbiente().equals("2");
    }

    private static URL getUrl(String homologacao, String producao) throws NfeException {
        try {
            return new URL(isHomologacao() ? homologacao : producao);
        } catch (MalformedURLException ex) {
            throw new NfeException(ERRO_BUSCAR_URL + ex.getMessage(), ex);
        }
    }

    public static URL notaFiscalServicoBHMG() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        if (configuracaoNfe.getUf().equals("31") && configuracaoNfe.getCidade().equals("3106200")) {
            url = getUrl("https://bhisshomologa.pbh.gov.br/bhiss-ws/nfse?wsdl", "https://bhissdigital.pbh.gov.br/bhiss-ws/nfse?wsdl");
        }
        return url;
    }

    public static URL notaFiscalServicoFAPA() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        if (configuracaoNfe.getUf().equals("31") && configuracaoNfe.getCidade().equals("3106200")) {
            url = getUrl("http://e-gov.betha.com.br/e-nota-contribuinte-ws/nfseWS?wsdl", "http://e-gov.betha.com.br/e-nota-contribuinte-ws/nfseWS?wsdl");
        }
        return url;
    }

    //NfeConsultaProtocolo
    public static URL consultaXml() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        switch (configuracaoNfe.getUf()) {
            case "52":
                url = getUrl("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2?wsdl", "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeConsulta2?wsdl");
                break;
            case "11":
            case "12":
            case "14":
            case "16":
            case "17":
            case "24":
            case "25":
            case "27":
            case "28":
            case "32":
            case "33":
            case "42":
            case "53":
                url = getUrl(URL_HOMOLOG_RS_CONSULTA, URL_PROD_RS_CONSULTA);
                break;
            case "13":
                url = getUrl("https://homnfe.sefaz.am.gov.br/services2/services/NfeConsulta2", "https://nfe.sefaz.am.gov.br/services2/services/NfeConsulta2");
                break;
            case "15":
            case "21":
            case "22":
                url = getUrl(URL_HOMOLOG_VIRTUAL_CONSULTA, URL_PROD_VIRTUAL_CONSULTA);
                break;
            case "23":
                url = getUrl("https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeConsulta2?wsdl", "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeConsulta2?wsdl");
                break;
            case "26":
                url = getUrl("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeConsulta2", "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeConsulta2");
                break;
            case "29":
                url = getUrl("https://hnfe.sefaz.ba.gov.br/webservices/NfeConsulta/NfeConsulta.asmx", "https://nfe.sefaz.ba.gov.br/webservices/NfeConsulta/NfeConsulta.asmx");
                break;
            case "31":
                url = getUrl("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeConsulta2", "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeConsulta2");
                break;
            case "35":
                url = getUrl("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeconsulta2.asmx", "https://nfe.fazenda.sp.gov.br/ws/nfeconsulta2.asmx");
                break;
            case "41":
                url = getUrl("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeConsulta3?wsdl", "https://nfe.fazenda.pr.gov.br/nfe/NFeConsulta3?wsdl");
                break;
            case "43":
                url = getUrl("https://nfe-homologacao.sefazrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx", "https://nfe.sefazrs.rs.gov.br/ws/NfeConsulta/NfeConsulta2.asmx");
                break;
            case "50":
                url = getUrl("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeConsulta2", "https://nfe.fazenda.ms.gov.br/producao/services2/NfeConsulta2");
                break;
            case "51":
                url = getUrl("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeConsulta2?wsdl", "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeConsulta2?wsdl");
                break;
            default:
                break;
        }
        return url;
    }

    //NfeStatusServico
    public static URL status() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        switch (configuracaoNfe.getUf()) {
            case "52":
                url = getUrl("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2?wsdl", "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeStatusServico2?wsdl");
                break;
            case "11":
            case "12":
            case "14":
            case "15":
            case "16":
            case "17":
            case "21":
            case "22":
            case "24":
            case "25":
            case "27":
            case "28":
            case "32":
            case "33":
            case "42":
            case "53":
                url = getUrl(URL_HOMOLOG_RS_STATUS, URL_PROD_RS_STATUS);
                break;
            case "13":
                url = getUrl("https://homnfe.sefaz.am.gov.br/services2/services/NfeStatusServico2", "https://nfe.sefaz.am.gov.br/services2/services/NfeStatusServico2");
                break;
            case "23":
                url = getUrl("https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2?wsdl", "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeStatusServico2?wsdl");
                break;
            case "26":
                url = getUrl("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2", "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeStatusServico2");
                break;
            case "29":
                url = getUrl("https://hnfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx", "https://nfe.sefaz.ba.gov.br/webservices/NfeStatusServico/NfeStatusServico.asmx");
                break;
            case "31":
                url = getUrl("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeStatusServico2", "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeStatus2");
                break;
            case "35":
                url = getUrl("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx", "https://nfe.fazenda.sp.gov.br/ws/nfestatusservico2.asmx");
                break;
            case "41":
                url = getUrl("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeStatusServico3?wsdl", "https://nfe.fazenda.pr.gov.br/nfe/NFeStatusServico3?wsdl");
                break;
            case "43":
                url = getUrl("https://nfe-homologacao.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx", "https://nfe.sefazrs.rs.gov.br/ws/NfeStatusServico/NfeStatusServico2.asmx");
                break;
            case "50":
                url = getUrl("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeStatusServico2", "https://nfe.fazenda.ms.gov.br/producao/services2/NfeStatusServico2");
                break;
            case "51":
                url = getUrl("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2?wsdl", "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeStatusServico2?wsdl");
                break;
            default:
                break;
        }
        return url;
    }

    //NFeAutorizacao
    public static URL enviarSincrono() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        switch (configuracaoNfe.getUf()) {
            case "52":
                url = getUrl("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao?wsdl", "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeAutorizacao?wsdl");
                break;
            case "11":
            case "12":
            case "14":
            case "16":
            case "17":
            case "24":
            case "25":
            case "27":
            case "28":
            case "32":
            case "33":
            case "42":
            case "53":
                url = getUrl(URL_HOMOLOG_RS_AUTORIZACAO, URL_PROD_RS_AUTORIZACAO);
                break;
            case "13":
                url = getUrl("https://homnfe.sefaz.am.gov.br/services2/services/NfeAutorizacao", "https://nfe.sefaz.am.gov.br/services2/services/NfeAutorizacao");
                break;
            case "15":
            case "21":
            case "22":
                url = getUrl(URL_HOMOLOG_VIRTUAL_AUTORIZACAO, URL_PROD_VIRTUAL_AUTORIZACAO);
                break;
            case "23":
                url = getUrl("https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeAutorizacao?wsdl", "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeAutorizacao?wsdl");
                break;
            case "26":
                url = getUrl("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeAutorizacao?wsdl", "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeAutorizacao?wsdl");
                break;
            case "29":
                url = getUrl("https://hnfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx", "https://nfe.sefaz.ba.gov.br/webservices/NfeAutorizacao/NfeAutorizacao.asmx");
                break;
            case "31":
                url = getUrl("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeAutorizacao", "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeAutorizacao");
                break;
            case "35":
                url = getUrl("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx", "https://nfe.fazenda.sp.gov.br/ws/nfeautorizacao.asmx");
                break;
            case "41":
                url = getUrl("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeAutorizacao3?wsdl", "https://nfe.fazenda.pr.gov.br/nfe/NFeAutorizacao3?wsdl");
                break;
            case "43":
                url = getUrl("https://nfe-homologacao.sefazrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx", "https://nfe.sefazrs.rs.gov.br/ws/NfeAutorizacao/NFeAutorizacao.asmx");
                break;
            case "50":
                url = getUrl("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeAutorizacao", "https://nfe.fazenda.ms.gov.br/producao/services2/NfeAutorizacao");
                break;
            case "51":
                url = getUrl("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeAutorizacao?wsdl", "	https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeAutorizacao?wsdl");
                break;
            default:
                break;
        }
        return url;
    }

    //NfeInutilizacao
    public static URL inutilizar() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        switch (configuracaoNfe.getUf()) {
            case "52":
                url = getUrl("https://homolog.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2?wsdl", "https://nfe.sefaz.go.gov.br/nfe/services/v2/NfeInutilizacao2?wsdl");
                break;
            case "11":
            case "12":
            case "14":
            case "16":
            case "17":
            case "24":
            case "25":
            case "27":
            case "28":
            case "32":
            case "33":
            case "42":
            case "53":
                url = getUrl(URL_HOMOLOG_RS_INUTILIZACAO, URL_PROD_RS_INUTILIZACAO);
                break;
            case "13":
                url = getUrl("https://homnfe.sefaz.am.gov.br/services2/services/NfeInutilizacao2", "https://nfe.sefaz.am.gov.br/services2/services/NfeInutilizacao2");
                break;
            case "15":
            case "21":
            case "22":
                url = getUrl(URL_HOMOLOG_VIRTUAL_INUTILIZACAO, URL_PROD_VIRTUAL_INUTILIZACAO);
                break;
            case "23":
                url = getUrl("https://nfeh.sefaz.ce.gov.br/nfe2/services/NfeInutilizacao2?wsdl", "https://nfe.sefaz.ce.gov.br/nfe2/services/NfeInutilizacao2?wsdl");
                break;
            case "26":
                url = getUrl("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/NfeInutilizacao2", "https://nfe.sefaz.pe.gov.br/nfe-service/services/NfeInutilizacao2");
                break;
            case "29":
                url = getUrl("https://hnfe.sefaz.ba.gov.br/webservices/NfeInutilizacao/NfeInutilizacao.asmx", "https://nfe.sefaz.ba.gov.br/webservices/NfeInutilizacao/NfeInutilizacao.asmx");
                break;
            case "31":
                url = getUrl("https://hnfe.fazenda.mg.gov.br/nfe2/services/NfeInutilizacao2", "https://nfe.fazenda.mg.gov.br/nfe2/services/NfeInutilizacao2");
                break;
            case "35":
                url = getUrl("https://homologacao.nfe.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx", "https://nfe.fazenda.sp.gov.br/ws/nfeinutilizacao2.asmx");
                break;
            case "41":
                url = getUrl("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeInutilizacao3?wsdl", "https://nfe.fazenda.pr.gov.br/nfe/NFeInutilizacao3?wsdl");
                break;
            case "43":
                url = getUrl("https://nfe-homologacao.sefazrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx", "https://nfe.sefazrs.rs.gov.br/ws/nfeinutilizacao/nfeinutilizacao2.asmx");
                break;
            case "50":
                url = getUrl("https://homologacao.nfe.ms.gov.br/homologacao/services2/NfeInutilizacao2", "https://nfe.fazenda.ms.gov.br/producao/services2/NfeInutilizacao2");
                break;
            case "51":
                url = getUrl("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/NfeInutilizacao2?wsdl", "https://nfe.sefaz.mt.gov.br/nfews/v2/services/NfeInutilizacao2?wsdl");
                break;
            default:
                break;
        }
        return url;
    }

    //RecepcaoEvento
    public static URL evento(String uf) throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        try {
            switch (configuracaoNfe.getUf()) {
                case "52":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homolog.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento?wsdl");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.go.gov.br/nfe/services/v2/RecepcaoEvento?wsdl");
                        }
                    }
                    break;
                case "11":
                case "12":
                case "14":
                case "16":
                case "17":
                case "24":
                case "25":
                case "27":
                case "28":
                case "32":
                case "33":
                case "42":
                case "53":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL(URL_PROD_EVENTO);// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL(URL_PROD_RS_RECEPCAO);
                        }
                    }
                    break;
                case "13":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homnfe.sefaz.am.gov.br/services2/services/RecepcaoEvento");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.am.gov.br/services2/services/RecepcaoEvento");
                        }
                    }
                    break;
                case "15":
                case "21":
                case "22":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL(URL_HOMOLOG_VIRTUAL_RECEPCAO);// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL(URL_PROD_VIRTUAL_RECEPCAO);
                        }
                    }
                    break;
                case "23":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://nfeh.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento?wsdl");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.ce.gov.br/nfe2/services/RecepcaoEvento?wsdl");
                        }
                    }
                    break;
                case "26":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://nfehomolog.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.pe.gov.br/nfe-service/services/RecepcaoEvento");
                        }
                    }
                    break;
                case "29":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://hnfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.ba.gov.br/webservices/sre/recepcaoevento.asmx");
                        }
                    }
                    break;
                case "31":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://hnfe.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.fazenda.mg.gov.br/nfe2/services/RecepcaoEvento");
                        }
                    }
                    break;
                case "35":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homologacao.nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.fazenda.sp.gov.br/ws/recepcaoevento.asmx");
                        }
                    }
                    break;
                case "41":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homologacao.nfe.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento?wsdl");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.fazenda.pr.gov.br/nfe/NFeRecepcaoEvento?wsdl");
                        }
                    }
                    break;
                case "43":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://nfe-homologacao.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefazrs.rs.gov.br/ws/recepcaoevento/recepcaoevento.asmx");
                        }
                    }
                    break;
                case "50":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homologacao.nfe.ms.gov.br/homologacao/services2/RecepcaoEvento");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.fazenda.ms.gov.br/producao/services2/RecepcaoEvento");
                        }
                    }
                    break;
                case "51":
                    if (isHomologacao()) {
                        if (uf.equals("91")) {
                            url = new URL(URL_HOMOLOG_EVENTO);// Homologação
                        } else {
                            url = new URL("https://homologacao.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento?wsdl");// Homologação
                        }
                    } else {
                        if (uf.equals("91")) {
                            url = new URL(URL_PROD_RECEPCAO);
                        } else {
                            url = new URL("https://nfe.sefaz.mt.gov.br/nfews/v2/services/RecepcaoEvento?wsdl");
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException ex) {
            throw new NfeException(ERRO_BUSCAR_URL + ex.getMessage(), ex);
        }
        return url;
    }

    public static URL consultaNfe() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        url = getUrl("https://hom.nfe.fazenda.gov.br/NFeConsultaDest/NFeConsultaDest.asmx", "https://www.nfe.fazenda.gov.br/NFeConsultaDest/NFeConsultaDest.asmx");
        return url;
    }

    public static URL downloadXml() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        url = getUrl("https://hom.nfe.fazenda.gov.br/NfeDownloadNF/NfeDownloadNF.asmx", "https://www.nfe.fazenda.gov.br/NfeDownloadNF/NfeDownloadNF.asmx");
        return url;
    }

    public static URL distribuicaoDfe() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        url = getUrl("https://hom.nfe.fazenda.gov.br/NFeDistribuicaoDFe/NFeDistribuicaoDFe.asmx", "https://www1.nfe.fazenda.gov.br/NFeDistribuicaoDFe/NFeDistribuicaoDFe.asmx");
        return url;
    }

    public static URL consultaQrCode() throws NfeException {
        configuracaoNfe = ConfiguracoesIniciaisNfe.getInstance();
        switch (configuracaoNfe.getUf()) {
            case "17"://TOCANTINS
            case "52"://GOIAS
                url = getUrl("http://homolog.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe", "http://nfe.sefaz.go.gov.br/nfeweb/sites/nfce/danfeNFCe");
                break;
            case "15"://PARA
                url = getUrl("https://appnfc.sefa.pa.gov.br/portal-homologacao/view/consultas/nfce/nfceForm.seam", "https://appnfc.sefa.pa.gov.br/portal/view/consultas/nfce/nfceForm.seam");
                break;
            default:
                break;
        }
        return url;
    }

}
