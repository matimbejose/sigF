package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoMensagem {

    ATIVA("A"),
    EXCLUIDA("E");

    public final String situacao;

}
