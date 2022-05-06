package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoEstoque {

    ENTRADA("EN", "Entrada"),
    SAIDA("SA", "Saída"),
    REMOVIDO("RE", "Removido");

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String tipo) {
        EnumTipoEstoque e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoEstoque retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
