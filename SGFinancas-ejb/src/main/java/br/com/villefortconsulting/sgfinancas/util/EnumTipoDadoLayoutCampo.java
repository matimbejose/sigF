package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoDadoLayoutCampo {

    TEXTO_FIXO("F"),
    TEXTO("T"),
    NUMERO("I"),
    DATA("D");

    public final String tipoDado;

    public static EnumTipoDadoLayoutCampo retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipoDado.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
