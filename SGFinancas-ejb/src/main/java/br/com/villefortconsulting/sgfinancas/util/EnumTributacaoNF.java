package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTributacaoNF {

    TRIBUTADO("T", "Tributado"),
    NAO_TRIBUTADO("N", "Não tributado"),
    OUTRA("O", "Outra");

    private final String tipo;

    private final String descricao;

}
