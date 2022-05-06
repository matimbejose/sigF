package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumBuscaMva {

    NAO("-", "Não buscar"),
    NCM("N", "Tabela NCM"),
    UF("E", "Tabela estado");

    private final String tipo;

    private final String descricao;

    public static EnumBuscaMva retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
