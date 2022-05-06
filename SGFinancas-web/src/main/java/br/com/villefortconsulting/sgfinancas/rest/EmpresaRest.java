package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContratacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAtualizacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModuloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoMensalidadeDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.RetornoContratacaoDTO;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/empresas")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Empresa")
public class EmpresaRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de empresas para o usuário logado", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarEmpresas() {
        return servicosWebService.listarEmpresas();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("categorias")
    @ApiOperation(value = "Listagem de categorias de empresas", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listaCategoriaEmpresa() {
        return servicosWebService.listaCategoriaEmpresa();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pagamentoMensalidade")
    @ApiOperation(value = "Listagem de pagamento de mensalidade do sistema para a empresa logada", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = PagamentoMensalidadeDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarPagamentoMensalidade() {
        return servicosWebService.listarPagamentoMensalidade();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modulos")
    @ApiOperation(value = "Listagem de módulos disponiveis", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ModuloDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarModulos(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(value = "Buscar por registros ativos? (S-Sim, N-Não)", required = false) @QueryParam("ativo") String ativo,
            @ApiParam(value = "Permite renovação? (S-Sim, N-Não)", required = false) @QueryParam("permiteRenovacao") String permiteRenovacao,
            @ApiParam(value = "Módulo obrigatório (S-Sim, N-Não)", required = false) @QueryParam("obrigatorio") String obrigatorio,
            @Context UriInfo info
    ) {
        return servicosWebService.listarModulos(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("modulosContratados")
    @ApiOperation(value = "Módulos contratados", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ModuloDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object modulosContratados() {
        return servicosWebService.modulosContratados();
    }

    @GET
    @Produces("text/plain")
    @Path("termo")
    @ApiOperation(value = "Retorna o termo de adesão em base64", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "PDF do termo de adesão em base64", response = String.class)
    })
    public String buscarTermo() {
        return servicosWebService.buscarTermo();
    }
    
    @GET
    @Produces("text/plain")
    @Path("termoAbapav")
    @ApiOperation(value = "Retorna o termo de adesão em base64", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "PDF do termo de adesão em base64", response = String.class)
    })
    public String buscarTermoAbapav() {
        return servicosWebService.buscarTermoAbapav();
    }

    @GET
    @Produces("text/plain")
    @Path("politicaPrivacidade")
    @ApiOperation(value = "Retorna a política de privadade em base64", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "PDF da política de privadade em base64", response = String.class)
    })
    public String buscarPoliticaPrivacidade() {
        return servicosWebService.buscarPoliticaPrivacidade();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de empresa", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 202, message = "Empresa cadastrada porém é necessário realizar o pagamento do sistema para começar a utilizar", response = EmpresaDTO.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object cadastrar(
            @ApiParam(value = "Dados da empresa que será cadastrada.\n"
                    + "O campo telefone é opcional.\n"
                    + "Os campos [dataNascimento, cep, codCidade, endereco, numero, complemento, bairro] são obrigatórios se e apenas se for cadastro de pessoa física."
                    + "Em um cadastro de pessoa jurídica os campos acima serão desconsiderados.", required = true) @RequestBody EmpresaCadastroDTO empresaCadastroDTO
    ) {
        return servicosWebService.cadastrarEmpresa(empresaCadastroDTO);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("atualizar")
    @ApiOperation(value = "Atualização de empresa", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object atualizar(
            @ApiParam(value = "Dados da empresa.", required = true) @RequestBody EmpresaAtualizacaoDTO empresaCadastroDTO
    ) {
        return servicosWebService.atualizarEmpresa(empresaCadastroDTO);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("contratar")
    @ApiOperation(value = "Contratação do sistema/ renovação de plano", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = RetornoContratacaoDTO.class)
    })
    public Object contratar(
            @ApiParam(value = "Listagem de modulos, e dados de pagamento para contratação ou renovação do plano",
                    required = true) @RequestBody ContratacaoDTO contratacaoDTO
    ) {
        return servicosWebService.contratar(contratacaoDTO);
    }

}
