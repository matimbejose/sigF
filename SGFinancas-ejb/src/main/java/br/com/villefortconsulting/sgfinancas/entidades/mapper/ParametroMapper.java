package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroAgedamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroAppMobileDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroOrcamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroPlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroSistemaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParametroVistoriaDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.util.ListUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class ParametroMapper {

    @Inject
    EntityMapper entityMapper;

    @Mapping(target = "planoConta", expression = "java(toPlanoContaDTO(ps))")
    @Mapping(target = "produto", expression = "java(toProdutoDTO(ps))")
    @Mapping(target = "orcamento", expression = "java(toOrcamentoDTO(ps))")
    @Mapping(target = "app", expression = "java(toAppMobileDTO(ps))")
    @Mapping(target = "agendamento", expression = "java(toAgendamentoDTO(ps))")
    @Mapping(target = "vistoria", expression = "java(toVistoriaDTO(ps))")
    public abstract ParametroSistemaDTO toDTO(ParametroSistema ps);

    @Mapping(target = "contaBancaria", source = "idPlanoContaContaBancaria")
    @Mapping(target = "cliente", source = "idPlanoContaCliente")
    @Mapping(target = "fornecedor", source = "idPlanoContaFornecedor")
    @Mapping(target = "produto", source = "idPlanoContaProduto")
    @Mapping(target = "servico", source = "idPlanoContaServico")
    @Mapping(target = "transportadora", source = "idPlanoContaTransportadora")
    @Mapping(target = "planoContaPadraoVenda", source = "idPlanoContaVendaPadrao")
    @Mapping(target = "planoContaPadraoCompra", source = "idPlanoContaCompraPadrao")
    public abstract ParametroPlanoContaDTO toPlanoContaDTO(ParametroSistema ps);

    public ParametroProdutoDTO toProdutoDTO(ParametroSistema ps) {
        ParametroProdutoDTO ppd = new ParametroProdutoDTO();

        ppd.setTiposCompra(tipoProdutoToEnum(ps.getTipoProdutoCompraList()));
        ppd.setTiposVenda(tipoProdutoToEnum(ps.getTipoProdutoVendaList()));
        ppd.setPermiteVendaComEstoqueNegativo(entityMapper.map(ps.getPermiteVendaSemEstoqueProdutosNormais()));

        return ppd;
    }

    @Mapping(target = "geraContaUnificada", source = "geraContaUnicaEmOrcamento")
    @Mapping(target = "observacao", source = "observacaoImpressaoOrcamento")
    public abstract ParametroOrcamentoDTO toOrcamentoDTO(ParametroSistema ps);

    @Mapping(target = "contaBancariaPadrao", source = "appContaBancariaPadrao")
    @Mapping(target = "contaBancariaPadrao.nomeBanco", source = "appContaBancariaPadrao.idBanco.descricao")
    public abstract ParametroAppMobileDTO toAppMobileDTO(ParametroSistema ps);

    @Mapping(target = "formularioPadrao", source = "idFormulario")
    public abstract ParametroVistoriaDTO toVistoriaDTO(ParametroSistema ps);

    @Mapping(target = "habilitaUsoAgenda", source = "habilitaAgenda")
    @Mapping(target = "envioSmsParaClienteAoAgendar", source = "enviaSmsClienteAgendamento")
    @Mapping(target = "envioSmsParaClienteAoAlterar", source = "enviaSmsClienteAlteracaoAgendamento")
    @Mapping(target = "envioSmsParaClienteUmDiaAntes", source = "enviaSmsClienteUmDiaAntes")
    @Mapping(target = "envioSmsParaFuncionarioAoConfirmar", source = "enviaSmsFuncionarioConfirmacao")
    @Mapping(target = "envioSmsParaEmpresaAoSolicitar", source = "enviaSmsEmpresaSolicitacao")
    @Mapping(target = "envioSmsTelefone", source = "celularEnvioSms")
    public abstract ParametroAgedamentoDTO toAgendamentoDTO(ParametroSistema ps);

    private static EnumTipoProdutoVenda[] tipoProdutoToEnum(List<TipoProdutoUso> tipoProdutoCompraList) {
        EnumTipoProdutoVenda[] e = new EnumTipoProdutoVenda[tipoProdutoCompraList.size()];
        for (int i = 0; i < tipoProdutoCompraList.size(); i++) {
            e[i] = EnumTipoProdutoVenda.retornaEnumSelecionado(tipoProdutoCompraList.get(i).getTipoProduto());
        }
        return e;
    }

    //ParametroPlanoContaDTO
    @Mapping(target = "idPlanoContaContaBancaria", source = "planoConta.contaBancaria.id")
    @Mapping(target = "idPlanoContaCliente", source = "planoConta.cliente.id")
    @Mapping(target = "idPlanoContaFornecedor", source = "planoConta.fornecedor.id")
    @Mapping(target = "idPlanoContaProduto", source = "planoConta.produto.id")
    @Mapping(target = "idPlanoContaServico", source = "planoConta.servico.id")
    @Mapping(target = "idPlanoContaTransportadora", source = "planoConta.transportadora.id")
    @Mapping(target = "idPlanoContaVendaPadrao", source = "planoConta.planoContaPadraoVenda.id")
    @Mapping(target = "idPlanoContaCompraPadrao", source = "planoConta.planoContaPadraoCompra.id")
    //ParametroOrcamentoDTO
    @Mapping(target = "geraContaUnicaEmOrcamento", source = "orcamento.geraContaUnificada")
    @Mapping(target = "statusNovaOS", source = "orcamento.statusNovaOS")
    @Mapping(target = "observacaoImpressaoOrcamento", source = "orcamento.observacao")
    //ParametroAppMobileDTO
    @Mapping(target = "appContaBancariaPadrao", source = "app.contaBancariaPadrao.id")
    //ParametroAgedamentoDTO
    @Mapping(target = "habilitaAgenda", source = "agendamento.habilitaUsoAgenda")
    @Mapping(target = "enviaSmsClienteAgendamento", source = "agendamento.envioSmsParaClienteAoAgendar")
    @Mapping(target = "enviaSmsClienteAlteracaoAgendamento", source = "agendamento.envioSmsParaClienteAoAlterar")
    @Mapping(target = "enviaSmsClienteUmDiaAntes", source = "agendamento.envioSmsParaClienteUmDiaAntes")
    @Mapping(target = "enviaSmsFuncionarioConfirmacao", source = "agendamento.envioSmsParaFuncionarioAoConfirmar")
    @Mapping(target = "enviaSmsEmpresaSolicitacao", source = "agendamento.envioSmsParaEmpresaAoSolicitar")
    @Mapping(target = "celularEnvioSms", source = "agendamento.envioSmsTelefone")
    @Mapping(target = "tipoConfirmacao", source = "agendamento.tipoConfirmacao")
    protected abstract void baseConversion(@MappingTarget ParametroSistema ps, ParametroSistemaDTO dto);

    public ParametroSistema toEntity(@MappingTarget ParametroSistema ps, ParametroSistemaDTO dto) throws IllegalAccessException, InvocationTargetException {
        baseConversion(ps, dto);
        List<TipoProdutoUso> aux = new ArrayList<>();
        if (dto.getProduto() != null) {
            aux.addAll(Arrays.asList(dto.getProduto().getTiposCompra()).stream()
                    .map(tipo -> TipoProdutoUso.builder()
                    .tipoProduto(tipo.getTipo())
                    .tipoUso("C")
                    .idParametroSistema(ps)
                    .build())
                    .collect(Collectors.toList()));
        }
        if (dto.getProduto() != null) {
            aux.addAll(Arrays.asList(dto.getProduto().getTiposVenda()).stream()
                    .map(tipo -> TipoProdutoUso.builder()
                    .tipoProduto(tipo.getTipo())
                    .tipoUso("V")
                    .idParametroSistema(ps)
                    .build())
                    .collect(Collectors.toList()));
        }
        ListUtil.persist(ps.getTipoProdutoUsoList(), aux, TipoProdutoUso::contains);
        return ps;
    }

}
