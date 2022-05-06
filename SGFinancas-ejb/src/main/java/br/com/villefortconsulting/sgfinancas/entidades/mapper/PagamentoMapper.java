package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TipoPagamentoDTO;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class PagamentoMapper {

    @Inject
    AdHocTenant adHocTenant;

    public abstract TipoPagamentoDTO toDTO(TipoPagamento tp);

    @Mapping(target = "tenatID", expression = "java(adHocTenant.getTenantID())")
    public abstract TipoPagamento toEntity(TipoPagamentoDTO dto);

}
