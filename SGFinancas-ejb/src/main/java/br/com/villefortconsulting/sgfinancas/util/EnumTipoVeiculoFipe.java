package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoVeiculoFipe {

    CARROS("Carros", "cars"),
    MOTOS("Motos", "motocycles"),
    CAMINHOES("Caminhões", "trucks");

    private final String descricao;

    private final String apiV2Path;
    
     public static EnumTipoVeiculoFipe retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static EnumTipoVeiculoFipe retornaEnumSelecionadoByName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findAny().orElse(null);
    }
    
    public static List<String> retornaTodosTipos() {
        return Arrays.asList(values()).stream()
                .map(item -> item.name())
                .collect(Collectors.toList());
    }

}
