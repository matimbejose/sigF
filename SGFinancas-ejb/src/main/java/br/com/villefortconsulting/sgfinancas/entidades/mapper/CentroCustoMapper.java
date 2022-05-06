package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CentroCustoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface CentroCustoMapper {

    CentroCustoDTO toDTO(CentroCusto centroCusto);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idTurma", ignore = true)
    CentroCusto toEntity(CentroCustoDTO centroCusto);

}
