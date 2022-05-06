package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoSegmentoConta {

    CONTA_CORRENTE("C", "Conta Corrente", "corrente"),
    SALARIO("S", "Salário", "salario"),
    POUPANCA("P", "Poupança", "poupanca");

    private final String tipo;

    private final String descricao;

    private final String referenciaPagCerto;

    public static EnumTipoSegmentoConta retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumTipoSegmentoConta en : EnumTipoSegmentoConta.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

}
