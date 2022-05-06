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
public enum EnumStatusOrdemDeServico {

    NOVO("N", "Novo"),
    AGUARDANDO("W", "Aguardando"),
    ANALISANDO("A", "Analisando"),
    FINALIZADO("F", "Finalizado");

    private final String codigo;

    private final String descricao;

    public static EnumStatusOrdemDeServico retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumStatusOrdemDeServico en : EnumStatusOrdemDeServico.values()) {
            if (en.getCodigo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

    public static Set<Map.Entry<String, String>> listarstatus() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumStatusOrdemDeServico en : EnumStatusOrdemDeServico.values()) {
            som.put(en.getCodigo(), en.getDescricao());
        }

        return som.entrySet();
    }

}
