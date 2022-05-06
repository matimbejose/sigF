package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoContrato {

    CLIENTE("C", "Cliente", EnumTipoTransacao.RECEBER, "contratoEntrada"),
    FORNECEDOR("F", "Fornecedor", EnumTipoTransacao.PAGAR, "contratoSaida");

    private final String tipo;

    private final String descricao;

    private final EnumTipoTransacao tipoTransacao;

    private final String nomePasta;

    public static EnumTipoContrato retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
