package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroSistemaDTO;
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
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/parametros")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Parametro")
public class ParametroRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de parâmetros", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar() {
        return servicosWebService.listarParametros();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("diasGratis")
    @ApiOperation(value = "Quantidade de dias gratuitos", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Double.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object quantidadeDiasGratuitos() {
        return servicosWebService.quantidadeDiasGratuitos();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("valorAdesao")
    @ApiOperation(value = "Valor mínimo obrogatorio de adesão", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Double.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object valorAdesaoMinimo() {
        return servicosWebService.valorAdesaoMinimmo();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("valorMensalidade")
    @ApiOperation(value = "Valor mínimo obrogatorio de mensalidade", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Double.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object valorMesalidadeMinimo() {
        return servicosWebService.valorMensalidadeMinimmo();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{nome}")
    @ApiOperation(value = "Busca de parâmetros por nome", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ParametroDTO.class, responseContainer = "List"),
        @ApiResponse(code = 404, message = "Não existe um parâmetros com o nome informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Nome do parâmetro", required = true) @PathParam("nome") String nome
    ) {
        return servicosWebService.consultarParametro(nome);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Atualização de parâmetros", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ParametroDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da parâmetros que será atualizado", required = true) @RequestBody ParametroDTO paramDTO
    ) {
        return servicosWebService.atualizarParametro(paramDTO);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("enable/nfe")
    @ApiOperation(value = "Habilitar o envio de NFe", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ParametroDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar() {
        return servicosWebService.habilitarNFe();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crud")
    @ApiOperation(value = "Busca de parâmetros do sistema", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ParametroSistemaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscaParametroSistema() {
        return servicosWebService.buscaParametroSistema();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("crud")
    @ApiOperation(value = "Alteração de parâmetros do sistema", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ParametroSistemaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object alteraParametroSistema(
            @RequestBody ParametroSistemaDTO dto
    ) {
        return servicosWebService.alteraParametroSistema(dto);
    }

}
