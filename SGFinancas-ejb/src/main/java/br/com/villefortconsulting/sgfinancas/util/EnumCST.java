package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCST {

    C00("00", "Tributada integralmente"),
    C10("10", "Tributada e com cobrança do ICMS por substituição tributária"),
    C20("20", "Com redução de base de cálculo"),
    C30("30", "Isenta ou não tributada e com cobrança do ICMS por substituição tributária"),
    C40("40", "Isenta"),
    C41("41", "Não tributada"),
    C50("50", "Suspensão"),
    C51("51", "Diferimento"),
    C60("60", "ICMS cobrado anteriormente por substituição tributária"),
    C70("70", "Com redução de base de cálculo e cobrança do ICMS por substituição tributária"),
    C90("90", "Outras");

    private final String tipo;

    private final String descricao;

    public static EnumCST retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumCST en : EnumCST.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }
        return null;
    }

}
