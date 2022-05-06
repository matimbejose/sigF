package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraImportacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoCompraDTO;
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
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/compras")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Compra")
public class CompraRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de compras", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = CompraDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para compras depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para compras antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17&data=le:2018-05-17 para compras depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @ApiParam(value = "ID do cliente da compra") @QueryParam("cliente") String cliente,
            @ApiParam(value = "Buscar por registros ativos(S), inativos(N) ou ambos(A)", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarCompras(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de compra pelo id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Compra não existente para a empresa selecionada", response = CompraDTO.class),
        @ApiResponse(code = 200, message = "", response = CompraDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id da compra buscada", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.consultarCompra(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cadastro de compra", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Compra cadastrada com sucesso. Retorna o ID da compra", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da compra que será cadastrada", required = true) @RequestBody CompraDTO compra
    ) {
        return servicosWebService.cadastrarCompra(compra);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de compra", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Compra não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id da compra deletada", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerCompra(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("importar")
    @ApiOperation(value = "Cadastro de compra via XML da nota", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Compra cadastrada com sucesso. Retorna o ID da compra", response = RetornoCompraDTO.class),
        @ApiResponse(code = 100, message = "XML da nota ok, porém nem todos os produtos estão cadastrados", response = RetornoCompraDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object importar(
            @ApiParam(value = "XML da nota que será importado", required = true) @RequestBody CompraImportacaoDTO xml
    ) {
        return servicosWebService.importarCompra(xml);
    }

}
