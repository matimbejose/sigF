package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoProduto {

    PRODUTO("P"),
    SERVICO("S");

    private final String tipo;

}
