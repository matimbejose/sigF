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
public enum EnumMotivoAbono {

    FERIAS("F", "Férias"),
    FERIADO("E", "Feriado"),
    ATESTADO("A", "Atestado"),
    BANCO_HORAS("B", "Banco de horas"),
    VIAGEM("V", "Viagem"),
    OUTROS("O", "Outros");

    private final String motivo;

    private final String descricao;

    public static EnumMotivoAbono retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.motivo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumMotivoAbono en : EnumMotivoAbono.values()) {
            if (en.getMotivo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

    public static Set<Map.Entry<String, String>> listarMotivosAbono() {
        HashMap<String, String> som = new LinkedHashMap<>();
        for (EnumMotivoAbono en : EnumMotivoAbono.values()) {
            som.put(en.getMotivo(), en.getDescricao());
        }

        return som.entrySet();
    }

}
