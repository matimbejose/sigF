package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CidadeDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface CidadeMapper {

    @Mapping(target = "descricao", source = "descricao")
    @Mapping(target = "codigoIBGE", source = "codigoIBGE")
    @Mapping(target = "nomeUF", source = "idUF.nomeCompleto")
    @Mapping(target = "siglaUF", source = "idUF.descricao")

    CidadeDTO toDTO(Cidade cidade);

}
