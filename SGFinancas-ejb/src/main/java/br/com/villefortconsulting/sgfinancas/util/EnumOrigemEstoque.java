package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Named
public enum EnumOrigemEstoque {

    SAIDA_LANCADA("SL", "Saída lançada"),
    COMPRA("CP", "Compra"),
    ESTOQUE_INICIAL("EI", "Estoque Inicial"),
    ENTRADA_LANCADA("EL", "Entrada lançada"),
    VENDA("VD", "Venda");

    private final String origem;

    private final String descricao;

    public static EnumOrigemEstoque retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.origem.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
