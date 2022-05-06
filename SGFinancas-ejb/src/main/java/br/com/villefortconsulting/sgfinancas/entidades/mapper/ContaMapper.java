package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaBancariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaParcelaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class ContaMapper {

    @Inject
    ClienteService clienteService;

    @Inject
    FornecedorService fornecedorService;

    @Inject
    CentroCustoService centroCustoService;

    @Inject
    PlanoContaService planoContaService;

    @Mapping(target = "cliente", source = "idCliente")
    @Mapping(target = "fornecedor", source = "idFornecedor")
    @Mapping(target = "contaBancaria", source = "idContaBancaria")
    @Mapping(target = "planoConta", source = "idPlanoConta")
    @Mapping(target = "centroCusto", source = "idCentroCusto")
    @Mapping(target = "tipoContaDescricao", expression = "java(br.com.villefortconsulting.sgfinancas.util.EnumTipoConta.retornaEnumSelecionado(conta.getTipoConta()).getDescricao())")
    @Mapping(target = "tipoTransacaoDescricao", expression = "java(br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao.retornaEnumSelecionado(conta.getTipoTransacao()).getDescricao())")
    @Mapping(target = "temDocumento", expression = "java(conta.getIdDocumento() != null)")
    public abstract ContaRestDTO toDTO(Conta conta);

    @Mapping(target = "conta", expression = "java( toDTO(conta.getIdConta()) )")
    @Mapping(target = "contaBancaria", source = "idContaBancaria")
    @Mapping(target = "centroCusto", source = "idCentroCusto")
    @Mapping(target = "formaPagamento", source = "idFormaPagamento")
    @Mapping(target = "temDocumento", expression = "java(conta.getIdConta().getIdDocumento() != null)")
    @Mapping(target = "numeroDocumento", source = "numNf")
    @Mapping(target = "tipoPagamento", source = "idConta.idTipoPagamento")
    public abstract ContaParcelaRestDTO toDTO(ContaParcela conta);

    @Mapping(target = "nomeBanco", source = "idBanco.descricao")
    @Mapping(target = "idPlanoConta", source = "idPlanoConta.id")
    public abstract ContaBancariaDTO toDTO(ContaBancaria conta);

    @Mapping(target = "idBanco", source = "banco")
    @Mapping(target = "tenatID", ignore = true)
    public abstract ContaBancaria toEntity(ContaBancariaDTO contaBancariaDTO);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "dataPagamento", source = "dadosParcela.dataPagamento")
    @Mapping(target = "informarPagamento", source = "informarRecebimento")
    @Mapping(target = "tipoRepeticao", source = "intervaloRepeticao")
    @Mapping(target = "qtdRepeticao", source = "qteRepeticoes")
    @Mapping(target = "valor", source = "dadosParcela.valor")
    @Mapping(target = "valorTotal", source = "dadosParcela.valor")
    @Mapping(target = "motivoCancelamento", ignore = true)
    @Mapping(target = "outrosCustos", source = "dadosParcela.valorOutrosCustos")
    @Mapping(target = "juros", source = "dadosParcela.valorJuros")
    @Mapping(target = "multa", source = "dadosParcela.valorMulta")
    @Mapping(target = "desconto", source = "dadosParcela.valorDescontos")
    @Mapping(target = "idUnidadeOcupacao", ignore = true)
    @Mapping(target = "idDocumento", ignore = true)
    @Mapping(target = "encargos", source = "dadosParcela.valorEncargos")
    @Mapping(target = "contaParcelaList", ignore = true)
    @Mapping(target = "valorIR", source = "dadosParcela.valorIR")
    @Mapping(target = "valorPIS", source = "dadosParcela.valorPIS")
    @Mapping(target = "valorCSLL", source = "dadosParcela.valorCSLL")
    @Mapping(target = "valorINSS", source = "dadosParcela.valorINSS")
    @Mapping(target = "valorCOFINS", source = "dadosParcela.valorCOFINS")
    @Mapping(target = "valorISS", source = "dadosParcela.valorISS")
    @Mapping(target = "valorICMS", source = "dadosParcela.valorICMS")
    @Mapping(target = "advemContrato", constant = "N")
    @Mapping(target = "idContrato", ignore = true)
    @Mapping(target = "contrato", ignore = true)
    @Mapping(target = "compra", ignore = true)
    @Mapping(target = "venda", ignore = true)
    @Mapping(target = "pagamentoParcial", expression = "java(dto.getDadosParcela().isPagamentoIntegral() ? \"N\" : \"S\")")
    @Mapping(target = "formaPagamento", ignore = true)
    @Mapping(target = "listaValorParcela", source = "detalheParcela")
    @Mapping(target = "tipoRepeticaoParcelas", ignore = true)
    @Mapping(target = "idCliente", expression = "java(getClienteFromDB(dto.getIdCliente()))")
    @Mapping(target = "idFornecedor", expression = "java(getFornecedorFromDB(dto.getIdFornecedor()))")
    @Mapping(target = "idCentroCusto", expression = "java(getCentroCustoFromDB(dto.getIdCentroCusto()))")
    public abstract Conta toEntity(ContaCadastroRestDTO dto);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "tipo", source = "idContaPai.tipo")
    public abstract PlanoConta toEntity(PlanoContaCadastroDTO dto);

    @Mapping(target = "codigoOrigem", ignore = true)
    public abstract PlanoContaCadastroDTO toDtoCadastro(PlanoConta pc);

    public Cliente getClienteFromDB(Integer id) {
        return id == null ? null : clienteService.buscar(id);
    }

    public Fornecedor getFornecedorFromDB(Integer id) {
        return id == null ? null : fornecedorService.buscar(id);
    }

    public CentroCusto getCentroCustoFromDB(Integer id) {
        return id == null ? null : centroCustoService.buscar(id);
    }

}
