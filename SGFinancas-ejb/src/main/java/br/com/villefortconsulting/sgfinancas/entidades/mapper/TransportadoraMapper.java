package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TransportadoraDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface TransportadoraMapper {

    @InheritInverseConfiguration
    TransportadoraDTO toDTO(Transportadora transportadora);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idCidade", ignore = true)
    @Mapping(target = "contato", ignore = true)
    @Mapping(target = "idPlanoConta", source = "planoConta")
    @Mapping(target = "cep", source = "contato.cep")
    @Mapping(target = "endereco", source = "contato.logradouro")
    @Mapping(target = "numero", source = "contato.numero")
    @Mapping(target = "bairro", source = "contato.bairro")
    @Mapping(target = "complemento", source = "contato.complemento")
    @Mapping(target = "foneComercial", source = "contato.telefoneComercial")
    @Mapping(target = "email", source = "contato.email")
    @Mapping(target = "foneResidencial", source = "contato.telefoneResidencial")
    @Mapping(target = "celular", source = "contato.telefoneCelular")
    Transportadora toEntity(TransportadoraDTO transportadoraDTO);

}
