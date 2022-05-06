package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.dto.BancoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface BancoMapper {

    BancoDTO toDTO(Banco conta);

}
