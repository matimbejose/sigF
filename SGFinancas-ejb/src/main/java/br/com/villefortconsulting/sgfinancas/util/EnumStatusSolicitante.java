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
public enum EnumStatusSolicitante {

    SOLICITANTE_ANTIGO("A", "Solicitante antigo"),
    SOLICITANTE("S", "Solicitante"),
    INTERESSADO("I", "Interessado"),
    INTERESSADO_CONFIRMAR("C", "Interessado a confirmar"),
    INTERESSADISSIMO("D", "Interessadíssimo"),
    NAO_TEM_INTERESSE("N", "Não tem interesse"),
    ONLINE("O", "Online"),
    EX_ALUNO("X", "Ex-aluno"),
    MATRICULA_EFETVADA("M", "Matrícula efetivada");

    private final String chave;

    private final String descricao;

    public static EnumStatusSolicitante retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.chave.contains(enumSelecionado))
                .findAny()
                .orElse(null);

    }

    public static Set<Map.Entry<String, String>> listaStatusSolicitante() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumStatusSolicitante en : EnumStatusSolicitante.values()) {
            som.put(en.getChave(), en.getDescricao());
        }

        return som.entrySet();
    }

    public static String buscarDescricao(String chave) {
        for (EnumStatusSolicitante en : EnumStatusSolicitante.values()) {
            if (en.getChave().contains(chave)) {
                return en.getDescricao();
            }
        }

        return null;

    }

}
