package br.com.villefortconsulting.sgfinancas.nfe;

import br.com.swconsultoria.nfe.schema.distdfeint.DistDFeInt;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TEnvEvento;
import br.com.swconsultoria.nfe.schema.envEventoCancNFe.TRetEnvEvento;
import br.com.swconsultoria.nfe.schema.retdistdfeint.RetDistDFeInt;
import br.com.swconsultoria.nfe.schema_4.consSitNFe.TRetConsSitNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TEnviNFe;
import br.com.swconsultoria.nfe.schema_4.enviNFe.TRetEnviNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TInutNFe;
import br.com.swconsultoria.nfe.schema_4.inutNFe.TRetInutNFe;
import br.com.swconsultoria.nfe.schema_4.retConsSitNFe.TConsSitNFe;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TConsStatServ;
import br.com.swconsultoria.nfe.schema_4.retConsStatServ.TRetConsStatServ;
import br.com.villefortconsulting.sgfinancas.nfe.exception.NfeException;

public class Nfe {

    private Nfe() {
    }

    public static TRetConsStatServ statusServico(TConsStatServ consStatServ, boolean valida) throws NfeException {
        return Status.statusServico(consStatServ, valida);
    }

    public static TRetConsSitNFe consultaXml(TConsSitNFe consSitNFe, boolean valida) throws NfeException {
        return ConsultaXml.consultaXml(consSitNFe, valida);
    }

    public static TRetInutNFe inutilizacao(TInutNFe inutNFe, boolean valida) throws NfeException {
        return Inutilizar.inutiliza(inutNFe, valida);
    }

    public static RetDistDFeInt distribuicaoDfe(DistDFeInt distDFeInt, boolean valida) throws NfeException {
        return DistribuicaoDFe.consultaNfe(distDFeInt, valida);
    }

    public static String obterXmlNfe(TEnviNFe enviNFe) throws NfeException {
        return Enviar.obterXmlNfe(enviNFe);
    }

    public static String assinarXmlNfe(String xml, boolean valida) throws NfeException {
        return Enviar.assinarXmlNfe(xml, valida);
    }

    public static TEnviNFe montaObjetoNfe(TEnviNFe enviNFe) throws NfeException {
        return Enviar.montaObjetoNfe(enviNFe);
    }

    public static String getXmlEnvio() {
        return Enviar.getXmlParaEnvio();
    }

    public static String getXmlEnvioCancelamento() {
        return Evento.getXmlParaEnvio();
    }

    public static TRetEnviNFe enviarNfe(TEnviNFe enviNFe) throws NfeException {
        return Enviar.enviaNfe(enviNFe);
    }

    public static TRetEnvEvento cancelarNfe(TEnvEvento evento, boolean valida) throws NfeException {
        return Evento.eventoCancelamento(evento, valida);
    }

    public static TRetEnvEvento cancelarNfe(String xml, boolean valida) throws NfeException {
        return Evento.eventoCancelamento(xml, valida);
    }

    public static TRetEnvEvento cce(TEnvEvento evento, boolean valida) throws NfeException {
        return Evento.eventoCce(evento, valida);
    }

    public static TRetEnvEvento manifestacao(TEnvEvento envEvento, boolean valida) throws NfeException {
        return Evento.eventoManifestacao(envEvento, valida);
    }

    public static String obterXmlNfe(TEnviNFe enviNFe, boolean valida) throws NfeException {
        return Enviar.obterXmlNfe(enviNFe, valida);
    }

}
