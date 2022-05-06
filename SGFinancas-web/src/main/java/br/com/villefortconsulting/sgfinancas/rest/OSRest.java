package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OsCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaRestDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.enterprise.context.RequestScoped;
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

@Path("v1/public/osnova")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("OS")
public class OSRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de ordens de serviços", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = VendaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para ordem de serviço depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para ordem de serviço antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17&data=le:2018-05-17 para ordem de serviço depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @ApiParam(value = "Tipo de ordem de serviço OS") @QueryParam("tipo") String tipo,
            @ApiParam(value = "ID do cliente da ordem de serviço") @QueryParam("cliente") String cliente,
            @ApiParam(value = "Buscar por registros ativos(S), inativos(N) ou ambos(A)", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarOSnova(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de ordem de serviço por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = VendaCadastroRestDTO.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id da ordem de serviço buscado", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.consultarOSnova(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("finalizar/{id}")
    @ApiOperation(value = "Finalizar OS, gerar venda, liberado quando statusOS = EE", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Ordem de Serviço finalizada com sucesso.", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object finalizarOSnova(
            @ApiParam(value = "ID do orçamento atualizado", required = true) @PathParam("id") Integer numero
    ) {
        return servicosWebService.finalizarOSnova(numero);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de ordem de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = OsCadastroRestDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da ordens de serviço que será cadastrada", required = true) @RequestBody OsCadastroRestDTO os
    ) {
        return servicosWebService.cadastrarOSnova(os);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Cancelar ordens de serviço", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Ordem de serviço não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cancelar(
            @ApiParam(value = "Id da ordem de serviço cancelada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.cancelarOS(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reabrir/{id}")
    @ApiOperation(value = "Reabrir ordem de serviço, quando podeReabrir = true", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Ordem de serviço não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object reabrir(
            @ApiParam(value = "Id da ordem de serviço a ser reaberta", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.reabrirOS(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("executar/{id}")
    @ApiOperation(value = "Iniciar execução da ordem de serviço, quando statusOS = AE", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Ordem de serviço não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object iniciarExecucao(
            @ApiParam(value = "Id da ordem de serviço começar a execução", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.executarOS(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("imprimir/os/{id}")
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
            @ApiParam(value = "Id da ordem de serviço a imprimir", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirOSnova(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("imprimir/garantia/{id}")
    @ApiOperation(value = "Imprimir garantia da ordem de serviço (aparecerá a opção somente para empresas onde o ID_Categoria_empresa = 51 e statusOS = F", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF da ordem de serviço codificado em base 64", response = String.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirGarantia(
            @ApiParam(value = "Id da ordem de serviço a imprimir", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirGarantia(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("imprimir/permanencia/{id}")
    @ApiOperation(value = "Imprimir permancia da ordem de serviço (aparecerá a opção somente para empresas onde o ID_Categoria_empresa = 51  e statusOS = F", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF da ordem de serviço codificado em base 64", response = String.class),
        @ApiResponse(code = 404, message = "Não existe uma ordem de serviço com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirPermanencia(
            @ApiParam(value = "Id da ordem de serviço a imprimir", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirPermanencia(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Finaliza a ordem de serviço por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = VendaCadastroRestDTO.class),
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

}
