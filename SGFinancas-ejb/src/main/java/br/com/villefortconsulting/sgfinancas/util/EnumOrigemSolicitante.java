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
public enum EnumOrigemSolicitante {

    TELEFONE("T", "Telefone"),
    WHATSAPP("W", "Whatsapp"),
    EMAIL("E", "Email"),
    INDICACAO("I", "Indicação"),
    INDICACAO_WHATSAPP("2", "Indicação/Whatsapp"),
    SITE("S", "Site");

    private final String chave;

    private final String descricao;

    public static EnumOrigemSolicitante retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.chave.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String buscarDescricao(String situacao) {
        if (situacao != null) {
            for (EnumOrigemSolicitante en : EnumOrigemSolicitante.values()) {
                if (en.getChave().contains(situacao)) {
                    return en.getDescricao();
                }
            }
        }

        return null;

    }

    public static Set<Map.Entry<String, String>> listarOrigemSolicitante() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumOrigemSolicitante en : EnumOrigemSolicitante.values()) {
            som.put(en.getChave(), en.getDescricao());
        }

        return som.entrySet();
    }

}
