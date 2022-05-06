package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DescricaoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("v1/public/veiculos")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Veículo")
public class VeiculoRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("marcas/{tipo}")
    @ApiOperation(value = "Listagem de marcas", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getListaMarca(
            @PathParam("tipo") String tipo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarMarca(tipo, info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modelos/{idMarca}")
    @ApiOperation(value = "Listagem de modelos", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Marca não localizada", response = void.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getListaModelo(
            @PathParam("idMarca") Integer marca,
            @Context UriInfo info
    ) {
        return servicosWebService.listarModelo(marca, info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("combustiveis")
    @ApiOperation(value = "Listagem de combustíveis", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getListaCombustivel() {
        return servicosWebService.listarCombustivel();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cnh")
    @ApiOperation(value = "Listagem de categoria de CNH", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public List<String> getListaCategoriaCNH() {
        return servicosWebService.listarCategoriaCNH();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tipos")
    @ApiOperation(value = "Listagem de tipo de Veiculos", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public List<String> getListaTipoVeiculos() {
        return servicosWebService.listarTipoVeiculos();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cores")
    @ApiOperation(value = "Listagem de cores de veículos", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = DescricaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getListaCorVeiculo() {
        return servicosWebService.listarCorVeiclo();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modelos/{idMarca}/{idModelo}")
    @ApiOperation(value = "Listagem de anos do modelo de uma versão de modelo", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Versão do modelo não localizado", response = void.class),
        @ApiResponse(code = 200, message = "", response = Integer.class, responseContainer = "List")
    })
    public Object getInformacaoModelo(@PathParam("idMarca") Integer marca, @PathParam("idModelo") Integer modelo) {
        return servicosWebService.listarInformacaoModelo(marca, modelo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modelos/{idMarca}/{idModelo}/anos")
    @ApiOperation(value = "Listagem de anos do modelo de uma versão de modelo", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Versão do modelo não localizado", response = void.class),
        @ApiResponse(code = 200, message = "", response = Integer.class, responseContainer = "List")
    })
    public Object getAnosModelo(@PathParam("idMarca") Integer marca, @PathParam("idModelo") Integer modelo) {
        return servicosWebService.listarAnosModelo(marca, modelo);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modelos/{idMarca}/{idModelo}/{anoModelo}/fipe")
    @ApiOperation(value = "Retorna o Valor Protegido/FIPE", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Integer.class, responseContainer = "List")
    })
    public Object getAnosModelo(@PathParam("idMarca") Integer marca, @PathParam("idModelo") Integer modelo, @PathParam("anoModelo") Integer ano) {
        return servicosWebService.valorProtegidoFipe(marca, modelo, ano);
    }
}
