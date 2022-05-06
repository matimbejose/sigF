package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteHdDDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ServicosWebService;
import io.swagger.annotations.Api;
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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("v1/public/busca")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Busca")
public class BuscaRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("empresa")
    @ApiOperation(value = "Busca dos dados da empresa pelo CNPJ", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Não foi possível encontrar o dado solicitado", response = void.class),
        @ApiResponse(code = 200, message = "", response = EmpresaDTO.class)
    })
    public Object buscaEmpresa(
            @ApiParam(value = "CNPJ", required = true) @QueryParam("cnpj") String termo,
            @Context UriInfo info
    ) {
        return servicosWebService.buscaEmpresa(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("pessoa")
    @ApiOperation(value = "Busca dos dados da pessoa por CPF e data de nascimento", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Não foi possível encontrar o dado solicitado", response = void.class),
        @ApiResponse(code = 200, message = "", response = ClienteHdDDTO.class)
    })
    public Object buscaPessoa(
            @ApiParam(value = "CPF", required = true) @QueryParam("cpf") String cpf,
            @ApiParam(value = "Data de nascimento no formato yyyy-MM-dd") @QueryParam("data") String data,
            @Context UriInfo info
    ) {
        return servicosWebService.buscaPessoa(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cep")
    @ApiOperation(value = "Busca do endereço pelo CEP", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 404, message = "Não foi possível encontrar o dado solicitado", response = void.class),
        @ApiResponse(code = 200, message = "", response = CepDTO.class)
    })
    public Object buscaCEP(
            @ApiParam(value = "CEP", required = true) @QueryParam("cep") String cep,
            @Context UriInfo info
    ) {
        return servicosWebService.buscaCEP(info.getQueryParameters());
    }

}
