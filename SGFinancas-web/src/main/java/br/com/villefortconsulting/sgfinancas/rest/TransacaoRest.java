package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.sgfinancas.entidades.dto.RequestRetornoMicroServico;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Path("v1/public/transacao/")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransacaoRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("boleto/callback")
    public Object boleto(@Context UriInfo info) {
        return doProxy(info, servicosWebService.getCallbackProducao() + "boleto/callback?");
    }

    @GET
    @Path("cartao/callback")
    public Object cartao(@Context UriInfo info) {
        return doProxy(info, servicosWebService.getCallbackProducao() + "cartao/callback?");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("boleto/homolog/callback")
    public Object boletoHomolog(@Context UriInfo info) {
        return doProxy(info, servicosWebService.getCallbackHomologacao() + "boleto/callback?");
    }

    @GET
    @Path("cartao/homolog/callback")
    public Object cartaoHomolog(@Context UriInfo info) {
        return doProxy(info, servicosWebService.getCallbackHomologacao() + "cartao/callback?");
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("atualizar/transacao")
    public Object atualizarTransacao(RequestRetornoMicroServico request) {
        return servicosWebService.atulizarTransacao(request);
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Path("fitpag/notificacao/sistema")
    public Object retornoFitPag(@FormParam("notificationCode") String notificationCode) {
        return servicosWebService.retornoFitpag(notificationCode);
    }

    private static Object doProxy(UriInfo info, String url) {
        try {
            url = info.getQueryParameters().keySet().stream()
                    .map(key -> key + "=" + info.getQueryParameters().getFirst(key) + "&")
                    .reduce(url, String::concat);
            ResponseEntity<String> re = new RestTemplate().getForEntity(url, String.class);
            return Response.status(re.getStatusCode().value()).entity(re.getBody()).build();
        } catch (HttpServerErrorException ex) {
            Logger.getLogger("TransacaoRest").log(Level.WARNING, ex.getMessage(), ex);
            return new ResponseEntity<>(ex.getResponseBodyAsString(), ex.getStatusCode());
        } catch (Exception ex) {
            Logger.getLogger("TransacaoRest").log(Level.WARNING, ex.getMessage(), ex);
            return ex.getMessage();
        }
    }

}
