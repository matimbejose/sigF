package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AtualizaOrcamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnvioEmailDTO;
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

@Path("v1/public/orcamentos")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Orcamento")
public class OrcamentoRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de orçamentos", consumes = "application/json")
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
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para orçamentos depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para orçamentos antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17&data=le:2018-05-17 para orçamentos depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @ApiParam(value = "Tipo de orçamento OR = Orçamento em aberto, AP = Orçamento aprovado, RE = Orçamento rejeitado") @QueryParam("tipo") String tipo,
            @ApiParam(value = "ID do cliente do orçamento") @QueryParam("cliente") String cliente,
            @ApiParam(value = "Buscar por registros ativos(S), inativos(N) ou ambos(A)", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @Context UriInfo info
    ) {
        return servicosWebService.listarOrcamentos(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de orçamento pelo id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Orçamento não existente para a empresa selecionada", response = VendaRestDTO.class),
        @ApiResponse(code = 200, message = "", response = VendaRestDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id do orçamento buscado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.consultarOrcamento(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de orçamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Orçamento cadastrada com sucesso. Retorna o ID da orçamento", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da orçamento que será cadastrada", required = true) @RequestBody VendaCadastroRestDTO orcamento
    ) {
        return servicosWebService.cadastrarOrcamento(orcamento);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de orçamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Orçamento não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id do orçamento deletado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerOrcamento(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Aprovação ou rejeição de orçamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Orçamento atualizado com sucesso.", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object atualizar(
            @ApiParam(value = "Dados da orçamento que será atualizado", required = true) @RequestBody AtualizaOrcamentoDTO dados,
            @ApiParam(value = "ID do orçamento atualizado", required = true) @PathParam("id") Integer numero
    ) {
        return servicosWebService.atualizarOrcamento(dados, numero);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("imprimir/{idOrcamento}/{idFormaPagamento}")
    @ApiOperation(value = "Imprimir orçamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do orçamento codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimir(
            @ApiParam(value = "ID do orçamento", required = true) @PathParam("idOrcamento") Integer idOrcamento,
            @ApiParam(value = "ID da forma de pagamento do orçamento", required = true) @PathParam("idFormaPagamento") Integer idFormaPagamento
    ) {
        return servicosWebService.imprimirOrcamento(idOrcamento, idFormaPagamento);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("email/{idOrcamento}/{idFormaPagamento}")
    @ApiOperation(value = "Enviar orçamento por email", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Status de envio do email", response = EnvioEmailDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object email(
            @ApiParam(value = "ID do orçamento", required = true) @PathParam("idOrcamento") Integer idOrcamento,
            @ApiParam(value = "ID da forma de pagamento do orçamento", required = true) @PathParam("idFormaPagamento") Integer idFormaPagamento
    ) {
        return servicosWebService.enviarEmailOrcamento(idOrcamento, idFormaPagamento);
    }

}
