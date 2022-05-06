package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusCadastro;
import br.com.villefortconsulting.sgfinancas.util.ImportadorConsumer;
import java.io.Serializable;
import java.util.List;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private EnumStatusCadastro status;

    private transient Supplier<String> callBack;

    private transient ImportadorConsumer<List<DtoId>, List<EntityId>> importador;

    private String[] dependencias;

    private transient Class<?> classeDto;

    public StatusCadastroDTO(String nome, boolean hasAny, boolean obrigatorio, Supplier<String> callBack, Class<?> classeDto) {
        this.nome = nome;
        this.callBack = callBack;
        this.classeDto = classeDto;
        if (hasAny) {
            status = EnumStatusCadastro.OK;
        } else if (obrigatorio) {
            status = EnumStatusCadastro.OBRIGATORIO_VAZIO;
        } else {
            status = EnumStatusCadastro.NAO_OBRIGATORIO_VAZIO;
        }
    }

    public StatusCadastroDTO(String nome, boolean hasAny, boolean obrigatorio, Supplier<String> callBack, Class<?> classeDto, String[] dependencias) {
        this.nome = nome;
        this.callBack = callBack;
        this.classeDto = classeDto;
        this.dependencias = dependencias;
        if (hasAny) {
            status = EnumStatusCadastro.OK;
        } else if (obrigatorio) {
            status = EnumStatusCadastro.OBRIGATORIO_VAZIO;
        } else {
            status = EnumStatusCadastro.NAO_OBRIGATORIO_VAZIO;
        }
    }

    public StatusCadastroDTO(String nome, boolean hasAny, boolean obrigatorio, Supplier<String> callBack, ImportadorConsumer<List<DtoId>, List<EntityId>> importador, Class<?> classeDto) {
        this.nome = nome;
        this.callBack = callBack;
        this.importador = importador;
        this.classeDto = classeDto;
        if (hasAny) {
            status = EnumStatusCadastro.OK;
        } else if (obrigatorio) {
            status = EnumStatusCadastro.OBRIGATORIO_VAZIO;
        } else {
            status = EnumStatusCadastro.NAO_OBRIGATORIO_VAZIO;
        }
    }

    public StatusCadastroDTO(String nome, boolean hasAny, boolean obrigatorio, Supplier<String> callBack, ImportadorConsumer<List<DtoId>, List<EntityId>> importador, Class<?> classeDto, String[] dependencias) {
        this.nome = nome;
        this.callBack = callBack;
        this.importador = importador;
        this.classeDto = classeDto;
        this.dependencias = dependencias;
        if (hasAny) {
            status = EnumStatusCadastro.OK;
        } else if (obrigatorio) {
            status = EnumStatusCadastro.OBRIGATORIO_VAZIO;
        } else {
            status = EnumStatusCadastro.NAO_OBRIGATORIO_VAZIO;
        }
    }

    public String nomeLowerCase() {
        return nome.toLowerCase();
    }

    public Boolean getPossuiDependencias() {
        return dependencias != null && dependencias.length > 0;
    }

}
