package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FaturaCallbackDto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NFSeRetornoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosPrivadosWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/private/callback")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Callback")
public class CallbackRest {

    @EJB
    private ServicosPrivadosWebService servicosPrivadosWebService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nfse")
    @ApiOperation(value = "Callback de envio de NFSe", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object salvaNfse(
            @ApiParam(value = "Lista de retornos", required = true) @RequestBody List<NFSeRetornoDTO> listaRetorno
    ) {
        return servicosPrivadosWebService.nfse(listaRetorno);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Path("iugu")
    @ApiOperation(value = "Callback de faturas do IUGU", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object salvaFatura(
            @ApiParam(value = "Retorno de fatura", required = true) @RequestBody FaturaCallbackDto fatura
    ) {
        return servicosPrivadosWebService.salvaFatura(fatura);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("nfse/{id}")
    @ApiOperation(value = "Busca os dados de uma NFSe", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object buscaNfse(
            @ApiParam(value = "Lista de retornos", required = true) @PathParam("id") Integer id
    ) {
        return servicosPrivadosWebService.nfse(id);
    }

}
