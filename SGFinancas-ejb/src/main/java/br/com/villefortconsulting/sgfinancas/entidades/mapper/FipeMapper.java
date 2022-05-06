package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.ModeloInformacao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MarcaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MarcaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloInformacaoCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloInformacaoDTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(uses = EntityMapper.class, componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR, typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class FipeMapper {

    @Mapping(target = "fipeNome", source = "nome")
    @Mapping(target = "fipeId", source = "codigo")
    @Mapping(target = "fipeOrder", source = "tipoVeiculo")
    @Mapping(target = "modeloList", ignore = true)
    public abstract Marca toEntity(MarcaCadastroDTO dto);

    @Mapping(target = "name", source = "nome")
    @Mapping(target = "fipeName", source = "fipeNome")
    @Mapping(target = "id", source = "fipeId")
    @Mapping(target = "tipoVeiculo", source = "fipeOrder")
    @Mapping(target = "key", ignore = true)
    public abstract MarcaDTO toDto(Marca entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "nome", source = "dto.nome")
    @Mapping(target = "idMarca", source = "marca")
    @Mapping(target = "fipeNome", source = "dto.nome")
    @Mapping(target = "fipeId", source = "dto.codigo")
    @Mapping(target = "anos", ignore = true)
    public abstract Modelo toEntity(ModeloCadastroDTO dto, Marca marca, String tipo);

    @Mapping(target = "name", source = "nome")
    @Mapping(target = "fipeName", source = "fipeNome")
    @Mapping(target = "id", source = "fipeId")
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "marca", ignore = true)
    @Mapping(target = "fipeMarca", ignore = true)
    public abstract ModeloDTO toDto(Modelo entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idModelo", source = "modelo")
    @Mapping(target = "ano", source = "dto.anoModelo")
    @Mapping(target = "preco", source = "dto.valor")
    @Mapping(target = "fipeCodigo", source = "dto.codigoFipe")
    public abstract ModeloInformacao toEntity(ModeloInformacaoCadastroDTO dto, Modelo modelo);

    @Mapping(target = "id", source = "fipeId")
    @Mapping(target = "veiculo", source = "idModelo.fipeNome")
    public abstract ModeloInformacaoDTO toDto(ModeloInformacao entity);

    protected Double map(String s) {
        return Double.parseDouble(s.replaceFirst("^[\\D]+", "").replace(".", "").replace(",", "."));
    }

}
