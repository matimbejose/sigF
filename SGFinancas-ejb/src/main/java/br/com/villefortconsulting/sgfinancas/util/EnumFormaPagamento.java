package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumFormaPagamento {

    AVISTA("V", "À vista", 1),
    UM_X("1x", "1x", 1),
    DOIS_X("2x", "2x", 2),
    TRES_X("3x", "3x", 3),
    QUATRO_X("4x", "4x", 4),
    CINCO_X("5x", "5x", 5),
    SEIS_X("6x", "6x", 6),
    SETE_X("7x", "7x", 7),
    OITO_X("8x", "8x", 8),
    NOVE_X("9x", "9x", 9),
    DEZ_X("10x", "10x", 10),
    ONZE_X("11x", "11x", 11),
    DOZE_X("12x", "12x", 12);

    private final String forma;

    private final String descricao;

    private final Integer quantidadeParcelas;

    public static EnumFormaPagamento retornaEnumSelecionado(String descricao) {
        if (descricao == null) {
            return null;
        }
        return Arrays.asList(values()).stream()
                .filter(item -> item.forma.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
