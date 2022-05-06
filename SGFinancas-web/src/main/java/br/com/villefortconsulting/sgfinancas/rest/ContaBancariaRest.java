package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaBancariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaParcelaRestDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/contasBancarias")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("ContaBancaria")
public class ContaBancariaRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de contas bancárias", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Descrição", required = false) @QueryParam("descricao") String descricao,
            @ApiParam(value = "Tipo de conta [C-Corrente, P-Poupança, S-Salário]", required = false) @QueryParam("tipoConta") String tipoConta,
            @ApiParam(value = "Situação [A-Ativa, C-Canelada]", required = false) @QueryParam("situacao") String situacao,
            @ApiParam(value = "Agência", required = false) @QueryParam("agencia") String agencia,
            @ApiParam(value = "Número da conta", required = false) @QueryParam("numConta") String numConta,
            @Context UriInfo info
    ) {
        return servicosWebService.listarContaBancaria(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Buscar conta bancária por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscar(
            @ApiParam(value = "ID da conta bancária", required = false) @PathParam("id") Integer id,
            @Context UriInfo info
    ) {
        return servicosWebService.buscarContaBancaria(id);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Cancelamento de conta bancária por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object deletar(
            @ApiParam(value = "ID da conta que será cancelar", required = false) @PathParam("id") int id
    ) {
        return servicosWebService.cancelarContaBancaria(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de conta bancária", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object salvar(
            @ApiParam(value = "Dados da conta bancária", required = false) @RequestBody ContaBancariaDTO dados
    ) {
        return servicosWebService.cadastrarContaBancaria(dados);
    }

}
