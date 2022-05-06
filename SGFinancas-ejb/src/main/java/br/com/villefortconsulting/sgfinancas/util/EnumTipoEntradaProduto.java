package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoEntradaProduto {

    SISTEMA("SI", "Sistema"),
    IMPORTACAO_ENTRADA("IE", "Importação de entrada"),
    IMPORTACAO_SAIDA("IS", "Importação de saída");

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String tipo) {
        EnumTipoEntradaProduto e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoEntradaProduto retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
