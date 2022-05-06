package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoTarefa {

    FIXA("F"),
    EXECUCAO("E");

    private final String tipo;

}
