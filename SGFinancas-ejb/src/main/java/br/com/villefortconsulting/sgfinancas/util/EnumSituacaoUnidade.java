package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoUnidade {

    ALUGADA("A", "Alugada"),
    CEDIDA("C", "Cedida"),
    PROPRIA("P", "Própria"),
    SOB_JURISDICAO("S", "Sob Jurisdição"),
    VAZIA("V", "Vazia");

    private final String situacao;

    private final String observacao;

    public static EnumSituacaoUnidade retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
