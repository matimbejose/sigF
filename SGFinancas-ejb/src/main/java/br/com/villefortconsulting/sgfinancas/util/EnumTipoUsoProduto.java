package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoUsoProduto {

    VENDA("V", "Venda"),
    COMPRA("C", "Compra");

    public final String tipo;

    public final String descricao;

    public static EnumTipoUsoProduto retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.getTipo().equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
