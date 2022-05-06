package br.com.villefortconsulting.sgfinancas.rest;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.entidades.dto.BaixaContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaParcelaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResumoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TimelineDTO;
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
import org.primefaces.model.TreeNode;
import org.springframework.web.bind.annotation.RequestBody;

@Path("v1/public/contas")
@RequestScoped
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Api("Conta")
public class ContaRest {

    @EJB
    private ServicosWebService servicosWebService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("resumo")
    @Deprecated
    @ApiOperation(value = "Resumo financeiro geral (tela dashboard)", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Objeto com os valores a receber, a pagar e a informação dos gráficos."
                + "<br>Nos campos [pagarAtraso, receberAtraso, pagarHoje, receberHoje] utilizar apenas os campos [valorTotal, qtdTotal]."
                + "O resto não será inicializado pelo servidor."
                + "<br>Nos campos [pagar, receber], os campos que terminarem em \"Pendente\" são \"a receber\","
                + "os que terminarem em \"Pago\" são os \"recebidos\", \"pagos\"", response = ResumoContaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getResumoConta(
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para vendas depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para vendas antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain",
                value = "data=ge:2018-02-17&data=le:2018-05-17 para vendas depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @Context UriInfo info
    ) {
        return servicosWebService.getResumoConta(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("resumo/dashboard")
    @ApiOperation(value = "Resumo financeiro geral (tela dashboard)", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Objeto com os valores a receber, a pagar e a informação dos gráficos."
                + "<br>Nos campos [pagarAtraso, receberAtraso, pagarHoje, receberHoje] utilizar apenas os campos [valorTotal, qtdTotal]."
                + "O resto não será inicializado pelo servidor."
                + "<br>Nos campos [pagar, receber], os campos que terminarem em \"Pendente\" são \"a receber\","
                + "os que terminarem em \"Pago\" são os \"recebidos\", \"pagos\"", response = ResumoContaDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getResumoDashboard(
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para vendas depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para vendas antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain",
                value = "data=ge:2018-02-17&data=le:2018-05-17 para vendas depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @Context UriInfo info
    ) {
        return servicosWebService.getResumoConta(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("resumo/index/{tipo}")
    @ApiOperation(value = "Resumo financeiro geral (tela index)", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Objeto com a listagem de pagas/recebidas ou a pagar/receber", response = TimelineDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getResumoIndex(
            @ApiParam(value = "Tipo de listagem [pago,pagar]") @PathParam("tipo") String tipo,
            @ApiParam(value = "Data inicial e final", required = false, examples = @Example({
        @ExampleProperty(mediaType = "text/plain", value = "data=ge:2018-02-17 para vendas depois de 17/02/2018"),
        @ExampleProperty(mediaType = "text/plain", value = "data=le:2018-05-17 para vendas antes de 17/05/2018"),
        @ExampleProperty(mediaType = "text/plain",
                value = "data=ge:2018-02-17&data=le:2018-05-17 para vendas depois de 17/02/2018 e antes de 17/05/2018")
    })) @QueryParam("data") String[] data,
            @Context UriInfo info
    ) {
        return servicosWebService.getResumoIndex(tipo, info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("fluxoCaixa/{ano}")
    @ApiOperation(value = "Fluxo de caixa de um ano", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "Árvore do fluxo de caixa", response = TreeNode.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object getFluxoCaixa(
            @ApiParam(value = "Ano para buscar os dados do fluxo de caixa") @PathParam("ano") Integer ano
    ) {
        return servicosWebService.getFluxoCaixa(ano);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Listagem de contas a pagar/receber", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 400, message = "O tipo da transação não foi informado", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @ApiParam(value = "Quantidade máxima de itens retornados", required = false) @DefaultValue("-1") @QueryParam("limit") int limit,
            @ApiParam(value = "Número do primeiro registro para retornar.", required = false) @DefaultValue("0") @QueryParam("offset") int offset,
            @ApiParam(
                    value = "Data inicial e final (data=ge:2018-02-17 para parcelas depois de 17/02/2018, data=le:2018-05-17 para parcelas antes de 17/05/2018, data=ge:2018-02-17&data=le:2018-05-17 para parcelas depois de 17/02/2018 e antes de 17/05/2018)",
                    required = false) @QueryParam("data") String[] data,
            @ApiParam(
                    value = "Tipo de conta (N = Conta sem vínculo, M = Mensalidade, L = Lançada, C = Compra, V = Venda, I = Tx. instalação, A = Tx. adesão, K = Contrato cliente, F = Contrato fornecedor, O = Ordem de serviço)",
                    required = false) @QueryParam("tipoConta") String tipoConta,
            @ApiParam(value = "Tipo de transacao (R = Receber, P = Pagar)", required = true) @QueryParam("tipoTransacao") String tipoTransacao,
            @ApiParam(value = "ID da conta bancaria", required = false) @QueryParam("contaBancaria") Integer contaBancaria,
            @ApiParam(value = "Valor", required = false) @QueryParam("valor") Double valor,
            @ApiParam(value = "ID do plano de conta", required = false) @QueryParam("planoConta") Integer planoConta,
            @ApiParam(value = "Observação", required = false) @QueryParam("observacao") String observacao,
            @ApiParam(value = "ID do cliente", required = false) @QueryParam("cliente") Integer cliente,
            @ApiParam(value = "ID da forma de pagamento", required = false) @QueryParam("formaPagamento") Integer formaPagamento,
            @ApiParam(
                    value = "Tipo de listagem (receber = Parcelas a receber, pagar = Parcelas a pagar, atraso = Parcelas em atraso, recebido = Parcelas recebidas, pago = Parcelas pagas, hoje = Parcelas para receber/pagar hoje)",
                    required = false) @QueryParam("tipoListagem") String tipoListagem,
            @ApiParam(value = "ID do fornecedor", required = false) @QueryParam("fornecedor") Integer fornecedor,
            @ApiParam(value = "Valor pago", required = false) @QueryParam("valorPago") Double valorPago,
            @Context UriInfo info
    ) {
        return servicosWebService.listarConta(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("baixa/{id}")
    @ApiOperation(value = "Busca a parcela calculada para a baixa de parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class)
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object baixa(
            @ApiParam(value = "ID da parcela que será baixada", required = true) @PathParam("id") Integer id
    ) {
        return servicosWebService.prepararBaixaContaParcela(id);
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("baixa/{id}")
    @ApiOperation(value = "Dar baixa de parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object baixa(
            @ApiParam(value = "ID da parcela que será baixada", required = true) @PathParam("id") Integer id,
            @ApiParam(value = "Dados para baixar a parcela", required = true) @RequestBody BaixaContaParcelaDTO dados
    ) {
        return servicosWebService.baixarContaParcela(id, dados);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("baixa/{id}")
    @ApiOperation(value = "Cancelar baixa de parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cancelarBaixa(
            @ApiParam(value = "ID da parcela que será terá a baixa desfeita", required = false) @PathParam("id") int id
    ) {
        return servicosWebService.cancelarBaixaContaParcela(id);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("arquivo/{id}")
    @ApiOperation(value = "Baixar arquivo da parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "PDF do arquivo codificado em base 64", response = String.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object baixarArquivo(
            @ApiParam(value = "ID da parcela que será terá a baixa desfeita", required = false) @PathParam("id") int id
    ) {
        return servicosWebService.baixarArquivoContaParcela(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("")
    @ApiOperation(value = "Criar uma parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object novo(
            @ApiParam(value = "Dados para baixar a parcela", required = true) @RequestBody ContaCadastroRestDTO dados
    ) {
        return servicosWebService.lancarParcela(dados);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("planos")
    @ApiOperation(value = "Listagem de planos de contas", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "", response = PlanoContaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object listar(
            @Deprecated @ApiParam(value = "Código", required = false) @QueryParam("codigo") String codigo,
            @Deprecated @ApiParam(value = "Códigos para busca", required = false) @QueryParam("codigos") String[] codigos,
            @ApiParam(value = "Descrição", required = false) @QueryParam("descricao") String descricao,
            @ApiParam(value = "Tipo [C-Crédito, D-Débito]", required = false) @QueryParam("tipo") String tipo,
            @ApiParam(value = "Tipo de balanço [P-Patrimônio, R-Resultado]", required = false) @QueryParam("tipoBalanco") String tipoBalanco,
            @ApiParam(value = "Padrão [S-Sim, N-Não]", required = false) @QueryParam("padrao") String padrao,
            @ApiParam(value = "Mostra na tela inicial [S-Sim, N-Não]", required = false) @QueryParam("mostraTelaInicial") String mostraTelaInicial,
            @ApiParam(value = "Grupo contábil [A-Ativo, P-Passivo, D-Despesa, R-Receita]", required = false) @QueryParam("grupoContabil") String grupoContabil,
            @ApiParam(value = "Selecionáveis [S-Selecionáveis, N-Não selecionáveis]", required = false) @QueryParam("selecionaveis") String selecionaveis,
            @Context UriInfo info
    ) {
        return servicosWebService.listarPlanoConta(info.getQueryParameters());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("planos/{id}")
    @ApiOperation(value = "Busca de plano de conta por ID", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 200, message = "", response = PlanoContaDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object buscarPlanoConta(
            @ApiParam(value = "ID do plano de contas", required = false) @PathParam("id") Integer idPlanoConta
    ) {
        return servicosWebService.buscarPlanoConta(idPlanoConta);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("cancelar/{id}")
    @ApiOperation(value = "Cancelar parcela", consumes = "application/json")
    @ApiResponses({
        @ApiResponse(code = 500, message = "Erro no servidor ou disparado devido a falha de alguma validação", response = RetornoWs.Erros.class),
        @ApiResponse(code = 200, message = "", response = ContaParcelaRestDTO.class, responseContainer = "List")
    })
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")
    })
    public Object cancelarContaParcela(
            @ApiParam(value = "ID da parcela que será cancelada", required = false) @PathParam("id") int id
    ) {
        return servicosWebService.cancelarContaParcela(id);
    }

}
