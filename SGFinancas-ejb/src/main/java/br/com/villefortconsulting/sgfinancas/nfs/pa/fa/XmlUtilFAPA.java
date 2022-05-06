package br.com.villefortconsulting.sgfinancas.nfs.pa.fa;

import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil;
import br.gov.fazenda.schema.nfse.CancelarNfseEnvio;
import br.gov.fazenda.schema.nfse.GerarNfseEnvio;
import br.gov.fazenda.schema.nfse.ObjectFactory;
import br.gov.fazenda.schema.nfse.TcCompNfse;
import br.gov.fazenda.schema.nfse.TcNfse;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

public class XmlUtilFAPA extends br.com.villefortconsulting.sgfinancas.nfe.util.XmlUtil {

    private static final String GERAR_NFSE_ENVIO = "GerarNfseEnvio";

    private static final String CANCELAR_NFSE = "CancelarNfseEnvio";

    private static final String TCCOMPNFSE = "TcCompNfse";

    private static final ObjectFactory OBJECT_FACTORY_NFSE = new ObjectFactory();

    private static final br.gov.fazenda.schema.nfse.ObjectFactory OBJECT_FACTORY_NFSE_SCHEMA = new br.gov.fazenda.schema.nfse.ObjectFactory();

    public static <T> T xmlToObject(String xml, Class<T> classe) throws JAXBException {

        JAXBContext context = JAXBContext.newInstance(classe);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), classe).getValue();
    }

    public static String objectToXml(Object obj) throws JAXBException, NfeException {

        JAXBContext context = null;
        JAXBElement<?> element = null;

        switch (obj.getClass().getSimpleName()) {

            case TCCOMPNFSE:
                context = JAXBContext.newInstance(TcNfse.class);
                element = OBJECT_FACTORY_NFSE_SCHEMA.createCompNfse((TcCompNfse) obj);
                break;

            case GERAR_NFSE_ENVIO:
                context = JAXBContext.newInstance(GerarNfseEnvio.class);
//                element = OBJECT_FACTORY_NFSE.createGerarNfseRequest((GerarNfseEnvio) obj);
                break;

            case CANCELAR_NFSE:
                context = JAXBContext.newInstance(CancelarNfseEnvio.class);
//                element = OBJECT_FACTORY_NFSE.createCancelarNfseRequest((CancelarNfseEnvio) obj);
                break;
            default:
                return XmlUtil.objectToXml(obj);
        }

        Marshaller marshaller = context.createMarshaller();

        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);

        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();

        marshaller.marshal(element, sw);

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(sw.toString());

        return xml.toString();
    }

}
