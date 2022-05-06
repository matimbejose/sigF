package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.Data;

@Data
public class FormularioRespostaItemSecaoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private ItemSecaoDTO itemSecao;

    private SecaoDTO secao;

    private AvariaDTO avaria;

    private String resposta;

}
