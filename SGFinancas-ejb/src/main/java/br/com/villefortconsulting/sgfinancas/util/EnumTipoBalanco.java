package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoBalanco {

    PATRIMONIO("P", "Patrimônio"),
    RESULTADO("R", "Resultado");

    private final String tipo;

    private final String descricaoTipo;

    public static String getDescricao(String tipo) {
        EnumTipoBalanco e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricaoTipo();
    }

    public static EnumTipoBalanco retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
