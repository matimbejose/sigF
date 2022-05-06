package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CadastroMovimentacaoEstoqueDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MovimentacaoEstoqueDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import br.com.villefortconsulting.util.DataUtil;
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

@Path("v1/public/estoques")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Estoque")
public class EstoqueRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de movimentações de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = MovimentacaoEstoqueDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados") @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.") @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "ID do produto", required = true) @QueryParam("produto") Integer produto,
            @Context UriInfo info
    ) {
        return servicosWebService.listarMovimentacaoEstoque(info.getQueryParameters());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cadastro de movimentação de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Movimentação cadastrada com sucesso.", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da movimentação que será cadastrada", required = true) @RequestBody CadastroMovimentacaoEstoqueDTO dados
    ) {
        return servicosWebService.cadastrarMovimentacaoEstoque(dados);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("imprimir/giroEstoque/{dataInicial}/{dataFinal}")
    @ApiOperation(value = "Imprimir relatório de giro de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do relatório codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirGiroEstoque(
            @ApiParam(value = "ID do produto") @QueryParam("produto") Integer produto,
            @ApiParam(value = "Data inicial") @PathParam("dataInicial") String dataInicial,
            @ApiParam(value = "Data final") @PathParam("dataFinal") String dataFinal
    ) {
        return servicosWebService.imprimirGiroEstoque(produto, DataUtil.converterStringParaDate(dataInicial, "yyyy-MM-dd"), DataUtil.converterStringParaDate(dataFinal, "yyyy-MM-dd"));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("imprimir/posicaoEstoque")
    @ApiOperation(value = "Imprimir relatório de posição de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do relatório codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirPosicaoEstoque(
            @ApiParam(value = "ID do produto") @QueryParam("produto") Integer produto
    ) {
        return servicosWebService.imprimirPosicaoEstoque(produto);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("imprimir/movimentoEstoque/{dataInicial}/{dataFinal}")
    @ApiOperation(value = "Imprimir relatório de movimento de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do relatório codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirMovimentoEstoque(
            @ApiParam(value = "ID do produto") @QueryParam("produto") Integer produto,
            @ApiParam(value = "Data inicial") @PathParam("dataInicial") String dataInicial,
            @ApiParam(value = "Data final") @PathParam("dataFinal") String dataFinal
    ) {
        return servicosWebService.imprimirMovimentoEstoque(produto, DataUtil.converterStringParaDate(dataInicial, "yyyy-MM-dd"), DataUtil.converterStringParaDate(dataFinal, "yyyy-MM-dd"));
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("imprimir/extratoEstoque/{dataInicial}/{dataFinal}")
    @ApiOperation(value = "Imprimir relatório de extrato de estoque", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do relatório codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirExtratoEstoque(
            @ApiParam(value = "ID do produto") @QueryParam("produto") Integer produto,
            @ApiParam(value = "Data inicial") @PathParam("dataInicial") String dataInicial,
            @ApiParam(value = "Data final") @PathParam("dataFinal") String dataFinal
    ) {
        return servicosWebService.imprimirExtratoEstoque(produto, DataUtil.converterStringParaDate(dataInicial, "yyyy-MM-dd"), DataUtil.converterStringParaDate(dataFinal, "yyyy-MM-dd"));
    }

}
