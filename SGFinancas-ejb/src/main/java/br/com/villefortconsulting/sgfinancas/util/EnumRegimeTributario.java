package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumRegimeTributario {

    SIMPLES_NACIONAL_MICRO_EMPRE("SNME", "Simples nacional micro empresa", false),
    SIMPLES_NACIONAL_EMPRE_PEQ_PORT("SNEP", "Simples nacional empresa pequeno porte", false),
    LUCRO_PRESUMIDO("LCPR", "Lucro presumido", true),
    LUCRO_REAL("LCRE", "Lucro real", true);

    private final String forma;

    private final String descricao;

    private final boolean usaCST;

    public static String getDescricao(String forma) {
        EnumRegimeTributario e = retornaEnumSelecionado(forma);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumRegimeTributario retornaEnumSelecionado(String forma) {
        if (forma == null) {
            return null;
        }
        return Arrays.asList(values()).stream()
                .filter(item -> item.forma.contains(forma))
                .findAny()
                .orElse(null);
    }

}
