package br.com.villefortconsulting.sgfinancas.nfe.util;

import br.com.swconsultoria.nfe.schema.distdfeint.DistDFeInt;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema_4.consSitNFe.TConsSitNFe;
import br.com.swconsultoria.nfe.schema_4.consStatServ.TConsStatServ;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TInutNFe;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.Normalizer;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class XmlUtil {

    private static final String STATUS = "TConsStatServ";

    private static final String SITUACAO_NFE = "TConsSitNFe";

    private static final String ENVIO_NFE = "TEnviNFe";

    private static final String DIST_DFE = "DistDFeInt";

    private static final String INUTILIZACAO = "TInutNFe";

    private static final String EVENTO = "TEnvEvento";

    private static final String TPROCEVENTO = "TProcEvento";

    private static final String TPROCCANCELAR = "br.com.swconsultoria.nfe.schema.envEventoCancNFe.TProcEvento";

    private static final String TPROCCCE = "br.com.swconsultoria.nfe.schema.envcce.TProcEvento";

    private static final String CANCELAR = "br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento";

    private static final String CCE = "br.com.swconsultoria.nfe.schema.envcce.TEnvEvento";

    private static final String MANIFESTAR = "br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento";

    public static <T> T xmlToObject(String xml, Class<T> classe) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classe);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), classe).getValue();
    }

    public static String objectToXml(Object obj) throws JAXBException, NfeException {
        JAXBContext context = null;
        JAXBElement<?> element = null;

        switch (obj.getClass().getSimpleName()) {
            case STATUS:
                context = JAXBContext.newInstance(TConsStatServ.class);
                element = new br.com.swconsultoria.nfe.schema_4.consStatServ.ObjectFactory().createConsStatServ((TConsStatServ) obj);
                break;
            case ENVIO_NFE:
                context = JAXBContext.newInstance(TEnviNFe.class);
                element = new br.com.swconsultoria.nfe.schema_4.enviNFe.ObjectFactory().createEnviNFe((TEnviNFe) obj);
                break;
            case SITUACAO_NFE:
                context = JAXBContext.newInstance(TConsSitNFe.class);
                element = new br.com.swconsultoria.nfe.schema_4.consSitNFe.ObjectFactory().createConsSitNFe((TConsSitNFe) obj);
                break;
            case DIST_DFE:
                context = JAXBContext.newInstance(DistDFeInt.class);
                element = new br.com.swconsultoria.nfe.schema.distdfeint.ObjectFactory().createDistDFeInt((DistDFeInt) obj);
                break;
            case INUTILIZACAO:
                context = JAXBContext.newInstance(TInutNFe.class);
                element = new br.com.swconsultoria.nfe.schema_4.inutNFe.ObjectFactory().createInutNFe((TInutNFe) obj);
                break;
            case TPROCEVENTO:
                switch (obj.getClass().getName()) {
                    case TPROCCANCELAR:
                        context = JAXBContext.newInstance(br.com.swconsultoria.nfe.schema.envEventoCancNFe.TProcEvento.class);
                        element = new br.com.swconsultoria.nfe.schema.envEventoCancNFe.ObjectFactory().createTProcEvento((br.com.swconsultoria.nfe.schema.envEventoCancNFe.TProcEvento) obj);
                        break;
                    case TPROCCCE:
                        context = JAXBContext.newInstance(br.com.swconsultoria.nfe.schema.envcce.TProcEvento.class);
                        element = new br.com.swconsultoria.nfe.schema.envcce.ObjectFactory().createTProcEvento((br.com.swconsultoria.nfe.schema.envcce.TProcEvento) obj);
                        break;
                    default:
                        break;
                }
                break;
            case EVENTO:
                switch (obj.getClass().getName()) {
                    case CANCELAR:
                        context = JAXBContext.newInstance(TEnvEvento.class);
                        element = new br.com.swconsultoria.nfe.schema.envEventoCancNFe.ObjectFactory().createEnvEvento((TEnvEvento) obj);
                        break;
                    case CCE:
                        context = JAXBContext.newInstance(br.com.swconsultoria.nfe.schema.envcce.TEnvEvento.class);
                        element = new br.com.swconsultoria.nfe.schema.envcce.ObjectFactory().createEnvEvento((br.com.swconsultoria.nfe.schema.envcce.TEnvEvento) obj);
                        break;
                    case MANIFESTAR:
                        context = JAXBContext.newInstance(br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento.class);
                        element = new br.com.swconsultoria.nfe.schema.envConfRecebto.ObjectFactory().createEnvEvento((br.com.swconsultoria.nfe.schema.envConfRecebto.TEnvEvento) obj);
                        break;
                    default:
                        break;
                }
                break;
            default:
                throw new NfeException("Objeto não mapeado no XmlUtil:" + obj.getClass().getSimpleName(), null);
        }
        if (context == null) {
            throw new NfeException("Objeto não mapeado no XmlUtil:" + obj.getClass().getSimpleName(), null);
        }
        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();

        marshaller.marshal(element, sw);

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>").append(sw.toString());

        if ((obj.getClass().getSimpleName().contains(TPROCEVENTO))) {
            return xml.toString().replaceAll("procEvento", "procEventoNFe");
        }
        return xml.toString();
    }

    public static String gZipToXml(byte[] conteudo) throws IOException {
        if (conteudo == null || conteudo.length == 0) {
            return "";
        }
        GZIPInputStream gis;
        gis = new GZIPInputStream(new ByteArrayInputStream(conteudo));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        StringBuilder outStr = new StringBuilder();
        String line;
        while ((line = bf.readLine()) != null) {
            outStr.append(line);
        }

        return outStr.toString();
    }

    public static String removeAcentos(String str) {
        str = str.replaceAll("\r", "");
        str = str.replaceAll("\t", "");
        str = str.replaceAll("\n", "");
        str = str.replaceAll(">\\s+<", "><");
        str = str.replaceAll("°", "o");
        str = str.replaceAll("&#13;", " ");
        CharSequence cs = new StringBuilder(str == null ? "" : str);
        return Normalizer.normalize(cs, Normalizer.Form.NFKD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

}
