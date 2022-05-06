package br.com.villefortconsulting.sgfinancas.entidades.mapper;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioResposta;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioRespostaItemSecao;
import br.com.villefortconsulting.sgfinancas.entidades.ItemSecao;
import br.com.villefortconsulting.sgfinancas.entidades.Secao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AvariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioRespostaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FormularioRespostaItemSecaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ItemSecaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SecaoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.FormularioRespostaService;
import javax.inject.Inject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
        uses = EntityMapper.class,
        imports = {FormularioRespostaService.class},
        componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        typeConversionPolicy = ReportingPolicy.ERROR)
public abstract class VistoriaMapper {

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    protected FormularioRespostaService formularioRespostaService;

    public abstract AvariaDTO toDTO(Avaria avaria);

    @Mapping(target = "tenatID", expression = "java(adHocTenant.getTenantID())")
    public abstract Avaria toEntity(AvariaDTO dto);

    @Mapping(target = "secoes", source = "formularioSecaoList")
    @Mapping(target = "readOnly", expression = "java(formularioRespostaService.temRespostaParaO(formulario))")
    public abstract FormularioDTO toDTO(Formulario formulario);

    @Mapping(target = "tenatID", expression = "java(adHocTenant.getTenantID())")
    @Mapping(target = "formularioSecaoList", source = "secoes")
    public abstract Formulario toEntity(FormularioDTO dto);

    public Formulario toEntity(FormularioDTO dto, @MappingTarget Formulario formulario) {
        if (formulario.getFormularioSecaoList() != null) {
            formulario.getFormularioSecaoList().forEach(fs -> fs.setIdFormulario(formulario));
        }
        return formulario;
    }

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "idFormulario", source = "formulario")
    @Mapping(target = "idClienteVeiculo", source = "clienteVeiculo")
    @Mapping(target = "idDocumento", source = "documento")
    @Mapping(target = "formularioRespostaItemSecaoList", source = "listFormularioRespostaItemSecao")
    public abstract FormularioResposta toEntity(FormularioRespostaDTO dto);

    @Mapping(target = "formulario", source = "idFormulario")
    @Mapping(target = "clienteVeiculo", source = "idClienteVeiculo")
    @Mapping(target = "documento", source = "idDocumento")
    @Mapping(target = "listFormularioRespostaItemSecao", source = "formularioRespostaItemSecaoList")
    public abstract FormularioRespostaDTO toDTO(FormularioResposta formularioResposta);

    @Mapping(target = "tenatID", ignore = true)
    @Mapping(target = "idFormularioResposta", ignore = true)
    @Mapping(target = "idItemSecao", source = "itemSecao")
    @Mapping(target = "idAvaria", source = "avaria")
    @Mapping(target = "idSecao", source = "secao")
    public abstract FormularioRespostaItemSecao toEntity(FormularioRespostaItemSecaoDTO dto);

    @Mapping(target = "avaria", source = "idAvaria")
    @Mapping(target = "secao", source = "idSecao")
    @Mapping(target = "itemSecao", source = "idItemSecao")
    public abstract FormularioRespostaItemSecaoDTO toDTO(FormularioRespostaItemSecao formularioRespostaItemSecao);

    @Mapping(target = "itens", source = "secaoItemSecaoList")
    public abstract SecaoDTO toDTO(Secao secao);

    @Mapping(target = "tenatID", expression = "java(adHocTenant.getTenantID())")
    @Mapping(target = "secaoItemSecaoList", source = "itens")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "idFormulario", ignore = true)
    public abstract Secao toEntity(SecaoDTO dto);

    public Secao toEntity(SecaoDTO dto, @MappingTarget Secao secao) {
        if (secao.getSecaoItemSecaoList() != null) {
            secao.getSecaoItemSecaoList().forEach(s -> s.setIdSecao(secao));
        }
        return secao;
    }

    @Mapping(target = "subSecoes", source = "itemSecaoSubItemSecaoList")
    public abstract ItemSecaoDTO toDTO(ItemSecao itemSecao);

    @Mapping(target = "tenatID", expression = "java(adHocTenant.getTenantID())")
    @Mapping(target = "itemSecaoSubItemSecaoList", source = "subSecoes")
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "key", ignore = true)
    @Mapping(target = "idSecao", ignore = true)
    @Mapping(target = "idItemSecao", ignore = true)
    public abstract ItemSecao toEntity(ItemSecaoDTO dto);

    public ItemSecao toEntity(ItemSecaoDTO dto, @MappingTarget ItemSecao itemSecao) {
        if (itemSecao.getItemSecaoSubItemSecaoList() != null) {
            itemSecao.getItemSecaoSubItemSecaoList().forEach(is -> is.setIdItemSecao(itemSecao));
        }
        return itemSecao;
    }

}
