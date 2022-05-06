package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoNota {

    SERVICO("S"),
    SERVICO_LOTE("L"),
    CANCELAMENTO_PRODUTO("CP"),
    CANCELAMENTO_SERVICO("CS"),
    PRODUTO("P"),
    EVENTO_PRODUTO("E");

    public final String tipo;

}
