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
public enum EnumNaturezaOperacaoNFS {

    TRIBUTACAO_MUNICIPIO("1", "Tributação no município"),
    TRIBUTACAO_FORA_MUNICIPIO("2", "Tributação fora do município"),
    ISENCAO("3", "Isenção"),
    IMUNE("4", "Imune"),
    SUSPENSA_JUDICIALMENTE("5", "Exigibilidade suspensa por decisão judicial");

    private final String natureza;

    private final String descricao;

    public static EnumNaturezaOperacaoNFS retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.natureza.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static Set<Map.Entry<String, String>> listarNaturezaOperacao() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumNaturezaOperacaoNFS en : EnumNaturezaOperacaoNFS.values()) {
            som.put(en.getNatureza(), en.getDescricao());
        }

        return som.entrySet();
    }

}
