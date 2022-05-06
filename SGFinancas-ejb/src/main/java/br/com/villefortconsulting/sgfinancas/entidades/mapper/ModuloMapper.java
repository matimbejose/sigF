package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModuloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModuloPacoteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModuloPermissaoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface ModuloMapper {

    @Mapping(target = "listaPermissoes", source = "moduloPermissaoList")
    ModuloDTO toDTO(Modulo modulo);

    @Mapping(target = "id", source = "idPermissao.id")
    @Mapping(target = "nome", source = "idPermissao.descricao")
    @Mapping(target = "descricao", source = "idPermissao.descricaoDetalhada")
    ModuloPermissaoDTO toDTO(ModuloPermissao mp);

    ModuloPacoteDTO toDTO(ModuloPacote moduloPacote);

}
