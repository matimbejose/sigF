package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoEmpresa {

    PRIVADO("PR", "Privado"),
    PUBLICO("PB", "Público"),
    IMUNE("IT", "Imunidade tributária");

    private final String tipo;

    private final String descricao;

    public static EnumTipoEmpresa retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
