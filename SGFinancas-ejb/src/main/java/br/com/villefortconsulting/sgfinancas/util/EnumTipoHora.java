package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoHora {

    ENTRADA_MANHA("EM", "Entrada manhã", 0),
    SAIDA_MANHA("SM", "Saída manhã", 1),
    ENTRADA_TARDE("ET", "Entrada tarde", 2),
    SAIDA_TARDE("ST", "Saída tarde", 3),
    ENTRADA_EXTRA("EE", "Entrada extra", 4),
    SAIDA_EXTRA("SE", "Saída extra", 5),;

    private final String tipo;

    private final String descricao;

    private final Integer id;

    public static String getDescricao(String tipo) {
        EnumTipoHora e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumTipoHora retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static EnumTipoHora retornaEnumSelecionado(Integer id) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.id.equals(id))
                .findAny()
                .orElse(null);
    }

}
