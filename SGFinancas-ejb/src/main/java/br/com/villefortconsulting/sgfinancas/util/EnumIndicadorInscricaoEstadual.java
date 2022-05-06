package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Named
public enum EnumIndicadorInscricaoEstadual {

    CONTRIBUINTE("CO", "Contribuinte"),
    CONTRIBUINTE_ISENTO("CI", "Contribuinte isento");

    private final String chave;

    private final String descricao;

    public static EnumIndicadorInscricaoEstadual retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.chave.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
