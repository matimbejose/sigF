package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoPagamento {

    CREDITO("C", "Credito"),
    DEBITO("D", "Débito");

    private final String tipo;

    private final String descricaoTipo;

    public static String getDescricao(String tipo) {
        EnumTipoPagamento e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricaoTipo();
    }

    public static EnumTipoPagamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
