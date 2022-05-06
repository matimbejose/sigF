package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCategoriaCNH {

    ACC("ACC", "ACC"),
    A("A", "A"),
    B("B", "B"),
    C("C", "C"),
    D("D", "D"),
    E("E", "E"),
    AB("AB", "AB"),
    AC("AC", "AC"),
    AD("AD", "AD"),
    AE("AE", "AE");

    private final String categoria;

    private final String descricao;

    public static EnumCategoriaCNH retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.categoria.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }
    
    public static List<String> retornaTodasCategorias() {
        return Arrays.asList(values()).stream()
                .map(item -> item.categoria)
                .collect(Collectors.toList());
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumCategoriaCNH en : EnumCategoriaCNH.values()) {
            if (en.getCategoria().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

}
