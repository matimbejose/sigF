package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioDTO;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/funcionarios")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Funcionario")
public class FuncionarioRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de funcionários", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FuncionarioDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Nome do funcionário buscado", required = false) @QueryParam("nome") String nome,
            @ApiParam(value = "Buscar somente por registros ativos[S,N]", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarFuncionario(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de funcionário por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FuncionarioDTO.class),
        @ApiResponse(code = 404, message = "Não existe um funcionário com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id do funcionário buscado", required = true) @PathParam("id") String id,
            @Context UriInfo info
    ) {
        return servicosWebService.consultarFuncionario(id, info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("busca/{nome}")
    @ApiOperation(value = "Busca de funcionário por nome", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FuncionarioDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    @Deprecated
    public Object buscar(
            @ApiParam(value = "Nome do liente buscado", required = true) @PathParam("nome") String nome,
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @Context UriInfo info
    ) {
        MultivaluedMap<String, String> map = info.getQueryParameters();
        map.add("nome", nome);
        return servicosWebService.listarFuncionario(map);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de funcionário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FuncionarioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados do funcionário que será cadastrado", required = true) @RequestBody FuncionarioDTO contatoDTO
    ) {
        return servicosWebService.cadastrarFuncionario(contatoDTO);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de funcionário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Funcionário não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 409, message = "Funcionário não pode ser removido (constraint do banco)", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id do funcionário deletado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerFuncionario(id);
    }

}
