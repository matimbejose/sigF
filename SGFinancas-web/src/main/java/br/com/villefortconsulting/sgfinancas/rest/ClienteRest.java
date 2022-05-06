package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitacaoCadastroClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoDTO;
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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/clientes")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Cliente")
public class ClienteRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de clientes", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ClienteDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Nome do liente buscado", required = false) @QueryParam("nome") String nome,
            @ApiParam(value = "Buscar somente por registros ativos[S-Sim,N-Não,A-Ambos]", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @ApiParam(value = "Buscar somente por seguradoras[S,N]", required = false) @QueryParam("seguradora") String seguradora,
            @Context UriInfo info
    ) {
        return servicosWebService.listarCliente(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Busca de cliente por id", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ClienteDTO.class),
        @ApiResponse(code = 404, message = "Não existe um cliente com o ID informado")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object consultar(
            @ApiParam(value = "Id do cliente buscado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.consultarCliente(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("busca/{nome}")
    @ApiOperation(value = "Busca de cliente por nome", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ClienteDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscar(
            @ApiParam(value = "Nome do liente buscado", required = true) @PathParam("nome") String nome,
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @Context UriInfo info
    ) {
        MultivaluedMap<String, String> map = info.getQueryParameters();
        map.add("nome", nome);
        return servicosWebService.listarCliente(map);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de cliente", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ClienteCadastroDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados do cliente que será cadastrado", required = true) @RequestBody ClienteCadastroDTO contatoDTO
    ) {
        return servicosWebService.cadastrarCliente(contatoDTO);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de cliente", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Cliente não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id do cliente deletado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerCliente(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{idCliente}/veiculo")
    @ApiOperation(value = "Adicionar ve[iculo ao cliente", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = String.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object adicionaVeiculo(
            @ApiParam(value = "Id do cliente", required = true) @PathParam("idCliente") Integer id,
            @ApiParam(value = "Dados do veículo que será adicionado ao cliente", required = true) @RequestBody VeiculoDTO dto) {
        return servicosWebService.adicionaVeiculoParaCliente(id, dto);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("solicitacao")
    @ApiOperation(value = "Solicitação de cadastro de cliente", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Nenhuma solicitação com o ID informado foi encontrada ou a empresa informada não foi encontrada", response = void.class),
        @ApiResponse(code = 200, message = "", response = ClienteCadastroDTO.class)
    })
    public Object solicitarCadastro(
            @ApiParam(value = "Dados da solicitacao", required = true) @RequestBody SolicitacaoCadastroClienteDTO dto
    ) {
        return servicosWebService.solicitarCadastroCliente(dto);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("solicitacao/{tenantID}/{id}")
    @ApiOperation(value = "Busca de solicitação de cadastro", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = SolicitacaoCadastroClienteDTO.class, responseContainer = "List")
    })
    public Object buscarSolicitacao(
            @ApiParam(value = "TenantID da empresa", required = true) @PathParam("tenantID") String tenantID,
            @ApiParam(value = "ID da solicitação", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.buscarSolicitacao(tenantID, id);
    }

}
