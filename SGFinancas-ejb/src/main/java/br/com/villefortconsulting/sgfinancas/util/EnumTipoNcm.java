package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoNcm {

    NCM("NC"),
    CAPITULO("CA"),
    POSICAO("PO"),
    SUBPOSICAO_UM("S1"),
    SUBPOSICAO_DOIS("S2"),
    ITEM("IT"),
    SUBITEM("SI");

    private final String tipo;

    public static EnumTipoNcm retornaEnumSelecionado(String tipo) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(tipo))
                .findAny()
                .orElse(null);
    }

}
