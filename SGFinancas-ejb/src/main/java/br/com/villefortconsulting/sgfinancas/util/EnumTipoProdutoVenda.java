package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoProdutoVenda {

    NORMAL("P", "Produtos normais"),
    COMPOSTO("C", "Produtos compostos"),
    KIT("K", "Kits de produto"),
    INSUMO("I", "Insumos para produção");

    private final String tipo;

    private final String descricao;

    public static EnumTipoProdutoVenda retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
