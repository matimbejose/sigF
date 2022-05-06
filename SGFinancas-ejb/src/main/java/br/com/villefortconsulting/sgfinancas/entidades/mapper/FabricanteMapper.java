package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Fabricante;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FabricanteDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface FabricanteMapper {

    FabricanteDTO toDTO(Fabricante cliente);

    @Mapping(target = "tenatID", ignore = true)
    Fabricante toEntity(FabricanteDTO clienteDTO);

}
