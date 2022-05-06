package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoExtrato {

    RECEITA("C", "Crédito"),
    DESPESA("D", "Débito");

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String tipo) {
        EnumTipoExtrato e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoExtrato retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
