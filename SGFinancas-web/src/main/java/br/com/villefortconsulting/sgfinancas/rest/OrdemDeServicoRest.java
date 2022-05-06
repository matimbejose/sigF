package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrdemServicoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/os")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Ordem de serviço")
public class OrdemDeServicoRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de ordens de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = OrdemServicoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Nome do liente buscado", required = false) @QueryParam("nome") String nome,
            @ApiParam(value = "Buscar somente por registros ativos[S,N]", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarOS(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de ordem de serviço por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = OrdemServicoDTO.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id da ordem de serviço buscado", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.consultarOS(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de ordem de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = OrdemServicoDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da ordens de serviço que será cadastrada", required = true) @RequestBody OrdemServicoDTO contatoDTO
    ) {
        return servicosWebService.cadastrarOS(contatoDTO);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de ordens de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Ordem de serviço não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id da ordem de serviço deletada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.removerOS(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("imprimir/{id}")
    @ApiOperation(value = "Imprimir ordem de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF da ordem de serviço codificado em base 64", response = String.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimir(
            @ApiParam(value = "Id da ordem de serviço buscado", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirOS(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Finaliza a ordem de serviço por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = OrdemServicoDTO.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object finalizar(
            @ApiParam(value = "Id da ordem de serviço a ser fializado", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.finalizarOS(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}/{status}")
    @ApiOperation(value = "Atualizar status da OS", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object atualizaStatus(
            @ApiParam(value = "ID da OS", required = false) @PathParam("id") Integer id,
            @ApiParam(value = "Novo status da OS.", required = false) @PathParam("status") String status
    ) {
        return servicosWebService.atualizaStatusOS(id, EnumStatusOrdemDeServico.valueOf(status));
    }

}
