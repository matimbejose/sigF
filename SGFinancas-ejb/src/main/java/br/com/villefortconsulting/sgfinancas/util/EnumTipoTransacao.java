package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoTransacao {

    RECEBER("R", "Receber"),
    PAGAR("P", "Pagar");

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String situacao) {
        EnumTipoTransacao e = retornaEnumSelecionado(situacao);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoTransacao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
