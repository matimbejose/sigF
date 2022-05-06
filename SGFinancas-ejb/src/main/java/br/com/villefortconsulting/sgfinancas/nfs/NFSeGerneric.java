package br.com.villefortconsulting.sgfinancas.nfs;

import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;
import br.com.villefortconsulting.sgfinancas.nfs.mg.bh.NFSeMGBH;
import br.com.villefortconsulting.sgfinancas.nfs.pa.fa.NFSeFAPA;
import br.gov.fazenda.schema.nfse.Cabecalho;
import com.thoughtworks.xstream.XStream;
import java.rmi.RemoteException;
import javax.xml.bind.JAXBException;

public class NFSeGerneric {

    private static final XStream GERADOR_XML = new XStream();

    public static String gerarCabecalhoXml() {
        Cabecalho cabecalho = new Cabecalho();
        cabecalho.setVersaoDados("1.00");
        String xmlCabecalho = GERADOR_XML.toXML(cabecalho);

        xmlCabecalho = "<?xml version='1.0' encoding='UTF-8'?>" + xmlCabecalho;
        xmlCabecalho = xmlCabecalho.replace("<br.gov.fazenda.schema.nfse.Cabecalho>", "<cabecalho xmlns=\"http://www.abrasf.org.br/nfse.xsd\" versao=\"1.00\">");
        xmlCabecalho = xmlCabecalho.replace("</br.gov.fazenda.schema.nfse.Cabecalho>", "</cabecalho>");

        return xmlCabecalho;
    }

    public static String converterXmlIntermediario(String xml) {
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

    public static String reverterXmlIntermediario(String xml) {
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

    public static NFS transmitirNfse(NFS nfs, String xml) throws JAXBException, NfeException, RemoteException {

        // Transmitir nota
        if (nfs.getIdMunicipioISSQN().getDescricao().equals(EnumNfse.BH_MG.getCidade())) {
            return NFSeMGBH.transmitirNfse(nfs, xml);
        } else if (nfs.getIdMunicipioISSQN().getDescricao().equals(EnumNfse.FA_PA.getCidade())) {
            return NFSeFAPA.transmitirNfse(nfs, xml);
        } else {
            throw new NfeException("Município ISSQN inválido: o sistema não possui configuração para envio de nota fiscal para seu município, favor entrar em contato com o suporte", null);
        }

    }

    private NFSeGerneric() {
    }

}
