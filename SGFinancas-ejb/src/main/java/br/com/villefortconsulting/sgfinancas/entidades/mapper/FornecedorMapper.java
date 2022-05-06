package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FornecedorDTO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public interface FornecedorMapper {

    @InheritInverseConfiguration
    @Mapping(target = "contato", expression = "java( entityMapper.map(fornecedor.getEndereco()) )")
    FornecedorDTO toDTO(Fornecedor fornecedor);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idPlanoConta", ignore = true)
    @Mapping(target = "fornecedorContatoList", ignore = true)
    @Mapping(target = "endereco", source = "contato")
    @Mapping(target = "foneComercial", source = "contato.telefoneComercial")
    @Mapping(target = "foneResidencial", source = "contato.telefoneResidencial")
    @Mapping(target = "email", source = "contato.email")
    @Mapping(target = "celular", source = "contato.telefoneCelular")
    @Mapping(target = "contato", source = "contato.telefoneComercial")
    Fornecedor toEntity(FornecedorDTO fornecedorDTO);

}
