package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumRegimeTributacao {

    ESTIMATIVA("2", "Estimativa"),
    SOCIEDADE_PROFISSIONAIS("3", "Sociedade de profissionais"),
    COOPERATIVA("4", "Cooperativa"),
    MEI_SN("5", "MEI do simples nacional"),
    ME_EPP_SN("6", "ME ou EPP do simples nacional");

    private final String regime;

    private final String descricao;

    public static EnumRegimeTributacao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.regime.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static Set<Map.Entry<String, String>> listarRegimeTributacao() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumRegimeTributacao en : EnumRegimeTributacao.values()) {
            som.put(en.getRegime(), en.getDescricao());
        }

        return som.entrySet();
    }

}
