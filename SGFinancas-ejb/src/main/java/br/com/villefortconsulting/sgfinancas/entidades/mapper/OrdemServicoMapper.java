package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.ItensOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.StatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItensOrdemServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrdemServicoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusOrdemServicoDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface OrdemServicoMapper {

    @Mapping(target = "cliente", source = "idCliente")
    @Mapping(target = "funcionarioEntrada", source = "idFuncionarioEntrada")
    @Mapping(target = "funcionarioSaida", source = "idFuncionarioSaida")
    @Mapping(target = "conta", source = "idConta")
    OrdemServicoDTO toDTO(OrdemDeServico cliente);

    @Mapping(target = "idCliente", source = "cliente")
    @Mapping(target = "idFuncionarioEntrada", source = "funcionarioEntrada")
    @Mapping(target = "idFuncionarioSaida", source = "funcionarioSaida")
    @Mapping(target = "idConta", source = "conta")
    OrdemDeServico toEntity(OrdemServicoDTO clienteDTO);

    @Mapping(target = "funcionario", source = "idFuncionario")
    @Mapping(target = "status", source = "statusEnum")
    StatusOrdemServicoDTO toDTO(StatusOrdemDeServico cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idOrdemDeServico", ignore = true)
    @Mapping(target = "idFuncionario", source = "funcionario")
    @Mapping(target = "statusEnum", source = "status")
    @Mapping(target = "status", ignore = true)
    StatusOrdemDeServico toEntity(StatusOrdemServicoDTO clienteDTO);

    ItensOrdemServicoDTO toDTO(ItensOrdemDeServico cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idOrdemDeServico", ignore = true)
    ItensOrdemDeServico toEntity(ItensOrdemServicoDTO clienteDTO);

    default EnumStatusOrdemDeServico toEnum(String s) {
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(s);
    }

    default String toString(EnumStatusOrdemDeServico e) {
        return e.getCodigo();
    }

}
