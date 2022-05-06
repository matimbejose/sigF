package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CtissDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnvioEmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NfsDTO;
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
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/notafiscal")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("NF-e / NFS-e")
public class NFeRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico")
    @ApiOperation(value = "Listagem de NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de NFS-e retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Número da nota fiscal", required = false) @QueryParam("numero") int numero,
            @ApiParam(value = "Valor da nota fiscal", required = false) @QueryParam("valorNota") double valorNota,
            @ApiParam(value = "ID do cliente", required = false) @QueryParam("cliente") int cliente,
            @ApiParam(value = "Email do cliente") @QueryParam("email") String email,
            @ApiParam(value = "Data inicial e final", required = false) @QueryParam("data") String[] data,
            @ApiParam(value = "Situações da NFS [E=Transmitida,J=Rejeitada,R=Rascunho,P=Em processamento,C=Cancelada]", required = false) @QueryParam("situacoes") String[] situacoes,
            @Context UriInfo info
    ) {
        return servicosWebService.listarNfs(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico/ctiss")
    @ApiOperation(value = "Listagem de CTISS", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = CtissDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarCtiss(
            @ApiParam(value = "municipioIssqn") @QueryParam("municipioIssqn") Integer municipioIssqn
    ) {
        return servicosWebService.listarCtiss(municipioIssqn);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico/{id}")
    @ApiOperation(value = "Busca de NFS-e pelo id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "NFS-e não existente para a empresa selecionada", response = NfsDTO.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id da NFS-e buscada", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.consultarNfs(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico/transmitir/{id}")
    @ApiOperation(value = "Transmitir NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "NFS-e não existente para a empresa selecionada", response = NfsDTO.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object transmitirNfs(
            @ApiParam(value = "Id da NFS-e buscada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.trasmitirNfs(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico/cancelar/{id}")
    @ApiOperation(value = "Cancelar NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "NFS-e não existente para a empresa selecionada", response = NfsDTO.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cancelarNfs(
            @ApiParam(value = "Id da NFS-e buscada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.cancelarNfs(id);
    }

    @POST
    @Path("servico")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Cadastro de NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "NFS-e cadastrada com sucesso. Retorna o ID da venda", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da NFS-e que será cadastrada", required = true) @RequestBody NfsDTO nfs
    ) {
        return servicosWebService.cadastrarNfs(nfs);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("servico/{id}")
    @ApiOperation(value = "Remoção da NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Venda não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id da NFS-e deletada", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerNFS(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("servico/imprimir/{id}")
    @ApiOperation(value = "Imprimir NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do orçamento codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimir(
            @ApiParam(value = "ID da NFS-e", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirNFS(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("servico/xml/{id}")
    @ApiOperation(value = "Baixar XML da NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do orçamento codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object baixarXML(
            @ApiParam(value = "ID da NFS-e", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.baixarXML(id);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("servico/email/{id}")
    @ApiOperation(value = "Enviar NFS-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Status de envio do email", response = EnvioEmailDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object email(
            @ApiParam(value = "ID da NFS-e", required = true) @PathParam("id") Integer id) {
        return servicosWebService.enviarEmailNfs(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("produto/transmitir/{id}/{ambiente}")
    @ApiOperation(value = "Transmitir NF-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 406, message = "Os parâmetros da empresa para a emissão de NF-e não estão corretos", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "NF-e não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object transmitirNf(
            @ApiParam(value = "Id da NF-e buscada", required = true) @PathParam("id") Integer id,
            @ApiParam(value = "Ambiente para emissão da NF-e [1-Produção,2-Homologação]", required = true) @PathParam("ambiente") String ambiente
    ) {
        return servicosWebService.trasmitirNf(id, ambiente);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("produto/{id}")
    @ApiOperation(value = "Remoção da NF-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Venda não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object deleteNF(
            @ApiParam(value = "Id da NF-e deletada", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerNF(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("produto")
    @ApiOperation(value = "Listagem de NF-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = NfsDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarNF(
            @ApiParam(value = "Quantidade máxima de NF-e retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Número da nota fiscal", required = false) @QueryParam("numero") int numero,
            @ApiParam(value = "Valor da nota fiscal", required = false) @QueryParam("valorNota") double valorNota,
            @ApiParam(value = "ID do cliente", required = false) @QueryParam("cliente") int cliente,
            @ApiParam(value = "Email do cliente") @QueryParam("email") String email,
            @ApiParam(value = "Data inicial e final", required = false) @QueryParam("data") String[] data,
            @ApiParam(value = "Situações da NF [E=Transmitida,J=Rejeitada,R=Rascunho,P=Em processamento,C=Cancelada]", required = false) @QueryParam("situacoes") String[] situacoes,
            @Context UriInfo info
    ) {
        return servicosWebService.listarNf(info.getQueryParameters());
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("produto/imprimir/{id}")
    @ApiOperation(value = "Imprimir NF-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do orçamento codificado em base 64", response = String.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object imprimirNf(
            @ApiParam(value = "ID da NF-e", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.imprimirNf(id);
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
    @Path("produto/email/{id}")
    @ApiOperation(value = "Enviar NF-e", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Status de envio do email", response = EnvioEmailDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object emailNf(
            @ApiParam(value = "ID da NF-e", required = true) @PathParam("id") Integer id) {
        return servicosWebService.enviarEmailNf(id);
    }

}
