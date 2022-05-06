package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Marca;
import br.com.villefortconsulting.sgfinancas.entidades.Modelo;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MarcaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ModeloDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VeiculoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.CombustivelService;
import br.com.villefortconsulting.sgfinancas.servicos.CorVeiculoService;
import br.com.villefortconsulting.sgfinancas.servicos.ModeloService;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = EntityMapper.class, imports = {ClienteService.class, CombustivelService.class, CorVeiculoService.class, ModeloService.class},
        componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class VeiculoMapper {

    @Inject
    protected ClienteService clienteService;

    @Inject
    protected CombustivelService combustivelService;

    @Inject
    protected CorVeiculoService corVeiculoService;

    @Inject
    protected ModeloService modeloService;

    @Mapping(target = "tenatID", expression = "java(clienteVeiculo.getIdCliente().getTenatID())", dependsOn = {"idCliente"})
    @Mapping(target = "idCliente", expression = "java(clienteService.buscar(dto.getCliente().getId()))")
    @Mapping(target = "idCombustivel", expression = "java(combustivelService.buscar(dto.getCombustivel().getId()))")
    @Mapping(target = "idCorVeiculo", expression = "java(corVeiculoService.buscar(dto.getCorVeiculo().getId()))")
    @Mapping(target = "idModelo", expression = "java(modeloService.buscar(dto.getModelo().getId()))")
    public abstract ClienteVeiculo toEntity(VeiculoDTO dto);

    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "marca", source = "idModelo.idMarca")
    @Mapping(target = "modelo", source = "idModelo")
    @Mapping(target = "combustivel", source = "idCombustivel")
    @Mapping(target = "corVeiculo", source = "idCorVeiculo")
    public abstract VeiculoDTO toDto(ClienteVeiculo entity);

    @Mapping(target = "name", source = "nome")
    @Mapping(target = "fipeName", source = "fipeNome")
    @Mapping(target = "order", source = "fipeOrder")
    @Mapping(target = "key", source = "fipeId")
    public abstract MarcaDTO toDto(Marca entity);

    @Mapping(target = "name", source = "nome")
    @Mapping(target = "fipeName", source = "fipeNome")
    @Mapping(target = "fipeMarca", source = "idMarca.fipeNome")
    @Mapping(target = "marca", source = "idMarca.nome")
    @Mapping(target = "key", source = "fipeId")
    public abstract ModeloDTO toDto(Modelo entity);

}
