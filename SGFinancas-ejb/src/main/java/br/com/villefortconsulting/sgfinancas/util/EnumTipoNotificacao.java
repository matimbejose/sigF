package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoNotificacao {

    CONTAS_PENDENTES("A", "Contas pendentes");

    private final String tipo;

    private final String descricao;

}
