package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormaPagamentoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface FormaPagamentoMapper {

    FormaPagamentoDTO toDTO(FormaPagamento formaPagamento);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "ativo", constant = "S")
    FormaPagamento toEntity(FormaPagamentoDTO formaPagamentoDTO);

}
