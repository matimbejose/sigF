package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.Erro;
import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.spi.Failure;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        if (Failure.class.isAssignableFrom(exception.getClass())) {
            return ((Failure) exception).getResponse();
        }
        RetornoWs.Erros erros = new RetornoWs.Erros();
        if (!SystemPreferencesUtil.getProp("ambiente", "PRODUCAO").equalsIgnoreCase("PRODUCAO")) {
            Throwable aux = exception;
            while (aux != null) {
                erros.addErro(new Erro(aux.getClass().getSimpleName() + (aux.getMessage() != null ? (": " + aux.getMessage()) : "")));
                aux = aux.getCause();
            }
        } else {
            String[] nomeClasse = exception.getClass().getName().split("\\.");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, exception);
            if ("UnrecognizedPropertyException".equals(nomeClasse[nomeClasse.length - 1])) {
                erros.addErro(new Erro("Campo inválido"));
            } else {
                erros.addErro(new Erro("Erro ao completar a requisição"));
            }
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(erros).build();
    }

}
