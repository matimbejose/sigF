package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AvariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioRespostaDTO;
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

@Path("v1/public/vistoria")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Vistoria")
public class VistoriaRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("avaria")
    @ApiOperation(value = "Listagem de avarias", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = AvariaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarAvaria(
            @ApiParam(value = "Descrição", required = false) @QueryParam("descricao") String descricao,
            @ApiParam(value = "Ativo [S-Ativo,N-Inativo]", required = false) @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarAvarias(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("avaria/{id}")
    @ApiOperation(value = "Buscar avaria por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = AvariaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscarAvaria(
            @ApiParam(value = "ID da avaria", required = false) @PathParam("id") Integer id,
            @Context UriInfo info
    ) {
        return servicosWebService.buscarAvaria(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("avaria")
    @ApiOperation(value = "Cadastro de avaria", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = AvariaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrarAvaria(
            @ApiParam(value = "Dados da avaria que será cadastrada", required = true) @RequestBody AvariaDTO avaria
    ) {
        return servicosWebService.cadastrarAvaria(avaria);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("avaria/{id}")
    @ApiOperation(value = "Remoção de avaria", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Avaria não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object deleteAvaria(
            @ApiParam(value = "Id da avaria deletada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.removerAvaria(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formulario")
    @ApiOperation(value = "Listagem de formulários", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarFormulario(
            @ApiParam(value = "Descrição", required = false) @QueryParam("descricao") String descricao,
            @Context UriInfo info
    ) {
        return servicosWebService.listarFormularios(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formulario/{id}")
    @ApiOperation(value = "Buscar formulario por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscarFormulario(
            @ApiParam(value = "ID do formulario", required = false) @PathParam("id") Integer id,
            @Context UriInfo info
    ) {
        return servicosWebService.buscarFormulario(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formulario")
    @ApiOperation(value = "Cadastro de formulario", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrarFormulario(
            @ApiParam(value = "Dados do formulario que será cadastrada", required = true) @RequestBody FormularioDTO formulario
    ) {
        return servicosWebService.cadastrarFormulario(formulario);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formulario/{id}")
    @ApiOperation(value = "Remoção de formulario", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Formulario não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object deleteFormulario(
            @ApiParam(value = "Id da formulario deletada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.removerFormulario(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formularioResposta")
    @ApiOperation(value = "Reposta do formulario", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioRespostaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastraFormularioResposta(
            @ApiParam(value = "Dados da reposta do formulario", required = true) @RequestBody FormularioRespostaDTO formularioResposta
    ) {
        return servicosWebService.cadastrarFormularioResposta(formularioResposta);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formularioResposta/{id}")
    @ApiOperation(value = "Buscar formulario reposta por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioRespostaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscarFormularioReposta(
            @ApiParam(value = "ID do formulario resposta", required = false) @PathParam("id") Integer id,
            @Context UriInfo info
    ) {
        return servicosWebService.buscarFormulario(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("formularioResposta")
    @ApiOperation(value = "Listagem de formulários resposta", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FormularioRespostaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarFormularioResposta(
            @ApiParam(value = "Cliente", required = false) @QueryParam("cliente") String cliente,
            @Context UriInfo info
    ) {
        return servicosWebService.listarFormulariosResposta(info.getQueryParameters());
    }

}
