package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoPagamentoSistema {

    RENOVACAO("R", "Renovação do sistema"),
    CONTRATACAO("C", "Contratação do sistema"),
    CONTRATACAO_MODULO("CM", "Contratação de módulos"),
    RENOVACAO_ANTECIPADA("RA", "Renovação antecipada");

    private final String categoria;

    private final String descricao;

    public static EnumTipoPagamentoSistema retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.categoria.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumTipoPagamentoSistema en : EnumTipoPagamentoSistema.values()) {
            if (en.getCategoria().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

}
