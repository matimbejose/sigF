package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface ServicoMapper {

    @Mapping(target = "valor", source = "valorVenda")
    @Mapping(target = "custo", source = "custoServico")
    @Mapping(target = "categoria", source = "idProdutoCategoria.id")
    @Mapping(target = "planoDeContas", source = "idPlanoConta.id")
    @Mapping(target = "categoriaImportacao", ignore = true)
    @Mapping(target = "fotos", source = "idDocumento")
    @Mapping(target = "servicoProdutoMap", expression = "java(entityMapper.listaServicoProdutoToMap(servico.getServicoProdutoList()))")
    @Mapping(target = "tempoExecucao", expression = "java( servico.getTempoExecucao() )")
    ServicoDTO toDTO(Servico servico);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idProdutoCategoria", ignore = true)
    @Mapping(target = "idPlanoConta", ignore = true)
    @Mapping(target = "valorVenda", source = "valor")
    @Mapping(target = "custoServico", source = "custo")
    @Mapping(target = "ativo", constant = "S")
    @Mapping(target = "idDocumento", source = "fotos")
    @Mapping(target = "servicoProdutoList", expression = "java(entityMapper.listaServicoProdutoToList(servicoDto.getServicoProdutoMap()))")
    @Mapping(target = "funcionarioServicoList", ignore = true)
    @Mapping(target = "tempoExecucao", expression = "java( servicoDto.getTempoExecucaoAsDate() )")
    Servico toEntity(ServicoDTO servicoDto);

}
