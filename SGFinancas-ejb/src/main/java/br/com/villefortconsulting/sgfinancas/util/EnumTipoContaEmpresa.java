package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoContaEmpresa {

    GRATUITA("G", "Gratuita"),
    PAGA("P", "Paga");

    private final String tipo;

    private final String descricao;

    public static EnumTipoContaEmpresa retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
