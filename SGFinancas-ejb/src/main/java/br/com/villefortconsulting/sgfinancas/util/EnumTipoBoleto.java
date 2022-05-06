package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoBoleto {

    BOLETO_SERVICO("BS", "Boleto de serviço"),
    BOLETO_CONCESSIONARIA("BC", "Boleto de concessionária");

    private final String tipo;

    private final String descricaoTipo;

    public static String getDescricao(String tipo) {
        EnumTipoBoleto e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricaoTipo();
    }

    public static EnumTipoBoleto retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
