package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoRespostaFormulario {

    RADIO("R", "Escolher um dos itens"),
    CHECKBOX("C", "Marcar sim/não nos itens"),
    OPTION("O", "Escolher da lista de opções");

    private final String tipo;

    private final String descricao;

}
