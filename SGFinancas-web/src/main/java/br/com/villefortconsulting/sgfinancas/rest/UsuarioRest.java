package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.servicos.rest.request.UsuarioRequest;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EnvioEmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PermissaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
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
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import springfox.documentation.annotations.ApiIgnore;

@Path("v1/public/usuarios")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Usuario")
public class UsuarioRest {

    @EJB
    private ServicosWebService servicosWebService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("token")
    @ApiOperation(value = "Gerar o token para o usuário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = UsuarioDTO.class)
    })
    public Object login(
            @ApiParam(value = "Dados de login do usuário. (Login, senha e id do dispositivo)", required = true) @RequestBody UsuarioRequest request
    ) {
        return servicosWebService.logarUsuario(request);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("empresas")
    @ApiOperation(value = "Utilizar o endpoint GET: v1/public/empresas/ para a listagem das empresas do usário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    @Deprecated
    public Object listarEmpresas() {
        return servicosWebService.listarEmpresas();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("token/{idEmpresa}")
    @ApiOperation(value = "Atualiza o token com o ID da empresa para o usuário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = UsuarioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object updateToken(
            @ApiParam(value = "ID da empresa que o usuário irá utilizar", required = true) @PathParam("idEmpresa") String empresa
    ) {
        return servicosWebService.updateToken(empresa);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Busca os dados do usuário logado", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "", response = UsuarioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscar() {
        return servicosWebService.buscarUsuarioLogado();
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Path("token")
    @ApiOperation(value = "Remove o token do sistema", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "O Token foi removido")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object deleteToken() {
        return servicosWebService.deleteToken();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("permissoes")
    @ApiOperation(value = "Listagem de permissões para o usuário logado", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = PermissaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listarPermissoes() {
        return servicosWebService.listarPermissoes();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("permissoes/{nome}")
    @ApiOperation(value = "Verifica se o usuário logado possui uma determinada permissão", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = PermissaoDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscarPermissao(
            @ApiParam(value = "Nome da permissão", required = true) @PathParam("nome") String nome
    ) {
        return servicosWebService.buscarPermissao(nome);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("recuperarSenha/{email}")
    @ApiOperation(value = "Reseta a senha do usuário e envia um email com a nova senha", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = EnvioEmailDTO.class)
    })
    public Object recuperarSenha(
            @ApiParam(value = "Login do usuário", required = true) @PathParam("email") String email
    ) {
        return servicosWebService.recuperarSenha(email);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("alterarSenha/{senha}")
    @ApiOperation(value = "Altera a senha do usuário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 408, message = "O token informado não foi gerado nos últimos 2 minutos", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "OK")
    })
    public Object alterarSenha(
            @ApiParam(value = "Nova senha", required = true) @PathParam("senha") String senha
    ) {
        return servicosWebService.alterarSenha(senha);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Cadastro de usuário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = UsuarioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cadastrar(
            @ApiParam(value = "Dados do usuário que será cadastrado", required = true) @RequestBody UsuarioCadastroDTO usuarioDTO
    ) {
        return servicosWebService.cadastrarUsuario(usuarioDTO);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    @ApiOperation(value = "Remoção de usuário", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Usuário não existente para a empresa selecionada", response = void.class),
        @ApiResponse(code = 200, message = "", response = void.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object delete(
            @ApiParam(value = "Id do usuário deletado", required = true) @PathParam("id") String id
    ) {
        return servicosWebService.removerUsuario(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aceitarTermo")
    @ApiOperation(value = "Permite que o usuário aceite o termo de uso", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "", response = UsuarioDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object aceitarTermo(@Context HttpServletRequest request) {
        return servicosWebService.aceitarTermo(request.getRemoteAddr());
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("aceitarTermo")
    @ApiIgnore
    public Object recusarTermo() {
        return servicosWebService.recusarTermo();
    }

}
