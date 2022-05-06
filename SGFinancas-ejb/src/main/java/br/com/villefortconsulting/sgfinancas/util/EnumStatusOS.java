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
public enum EnumStatusOS {

    AGUARDANDO_EXECUCAO("AE", "Aguardando execução"),
    EM_EXECUCAO("EE", "Em execução"),
    CANCELADA("CC", "OS cancelada"),
    FINALIZADO("F", "Finalizada");

    private final String codigo;

    private final String descricao;

    public static EnumStatusOS retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumStatusOS en : EnumStatusOS.values()) {
            if (en.getCodigo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

    public static Set<Map.Entry<String, String>> listarstatus() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumStatusOS en : EnumStatusOS.values()) {
            som.put(en.getCodigo(), en.getDescricao());
        }

        return som.entrySet();
    }

}
