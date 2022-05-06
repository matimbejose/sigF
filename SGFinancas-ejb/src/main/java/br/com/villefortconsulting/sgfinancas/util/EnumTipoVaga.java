package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoVaga {

    ALUGADA("A", "Alugada"),
    DISPONIVEL("D", "Disponível"),
    VENDIDA("V", "Vendida");

    private final String tipo;

    private final String observacao;

    public static EnumTipoVaga retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
