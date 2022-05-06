package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AgendamentoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FuncionarioMinDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.HorarioDisponivelDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ReagendamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoAgendamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioAppCadastroDTO;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/agendamentos")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Agendamento")
public class AgendamentoRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id: \\d+}")
    @ApiOperation(value = "Busca de agendamento por ID", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = VendaRestDTO.class)
    })
    public Object buscar(
            @PathParam("id") Integer id
    ) {
        return servicosWebService.buscaAgendamento(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de agendamentos", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = RetornoAgendamentoDTO.class, responseContainer = "List")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Buscar por registros ativos(S), inativos(N) ou ambos(A)", required = false) @DefaultValue("A") @QueryParam("ativo") String ativo,
            @ApiParam(value = "IDs dos funcionários para buscar", required = false) @QueryParam("funcionario") Integer idFuncionarioList,
            @ApiParam(value = "IDs dos clientes para buscar", required = false) @QueryParam("cliente") Integer idClienteList,
            @ApiParam(value = "IDs dos serviços para buscar", required = false) @QueryParam("servico") Integer idServicoList,
            @ApiParam(value = "Tipo de agendamento (AA - Aprovado, AG - Não aprovado)", required = false) @QueryParam("origem") String[] origem,
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para agendamentos depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para agendamentos antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17&data=le:2018-05-17 para agendamentos depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @Context UriInfo info
    ) {
        return servicosWebService.listaAgendamentos(info.getQueryParameters());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de agendamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados do agendamento que será cadastrado. (cliente, funcionario, servicos, horario são campos obrigatórios)", required = true) @RequestBody AgendamentoCadastroDTO dados
    ) {
        return servicosWebService.cadastrarAgendamento(dados);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("horario")
    @ApiOperation(value = "Listagem de horários livres por dia")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = HorarioDisponivelDTO.class, responseContainer = "List")
    })
    public Object livre(
            @ApiParam(value = "Dia do agendamento", required = true) @QueryParam("data") String data,
            @ApiParam(value = "Funcionário selecionado", required = true) @QueryParam("funcionario") Integer funcionario,
            @ApiParam(value = "Serviço selecionado", required = true) @QueryParam("servico") Integer[] servico,
            @ApiParam(value = "Apenas retornar os horários válidos", required = false) @QueryParam("validOnly") @DefaultValue("false") String validOnly
    ) {
        return servicosWebService.listaTempoLivre(data, funcionario, servico, null, validOnly != null && validOnly.equals("true"));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("empresas")
    @ApiOperation(value = "Listagem de empresas disponíveis para agendamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarEmpresas(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Código do IBGE do o município.", required = false) @QueryParam("codIBGE") int codIBGE,
            @Context UriInfo info
    ) {
        return servicosWebService.listarEmpresasAgendamento(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("funcionarios")
    @ApiOperation(value = "Listagem de empresas disponíveis para agendamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = FuncionarioMinDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarFuncionarios(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Código da empresa selecionada.", required = true) @QueryParam("tenantID") int tenantID,
            @ApiParam(value = "Nome do funcionário.", required = true) @QueryParam("nome") String nome,
            @Context UriInfo info
    ) {
        return servicosWebService.listarFuncionariosAgendamento(info.getQueryParameters());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("usuarios")
    @ApiOperation(value = "Cadastro de usuário para o APP de agendamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrarUser(
            @ApiParam(value = "Dados do agendamento que será cadastrado. (cliente, funcionario, servicos, horario são campos obrigatórios)", required = true) @RequestBody UsuarioAppCadastroDTO dados
    ) {
        return servicosWebService.cadastrarUsuarioAgendamento(dados);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reagendar")
    @ApiOperation(value = "Reagendamento de agendamento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = Integer.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object reagendar(
            @ApiParam(value = "Dados do agendamento que será reagendado.", required = true) @RequestBody ReagendamentoDTO dados
    ) {
        return servicosWebService.reagendarAgendamento(dados);
    }

}
