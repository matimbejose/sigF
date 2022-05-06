package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoItemVenda {

    PRODUTO("P", "Produto"),
    SERVICO("S", "Serviço"),
    PRODUTO_SERVICO("PS", "Produto e serviço");

    private final String tipo;

    private final String observacao;

    public static EnumTipoItemVenda retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
