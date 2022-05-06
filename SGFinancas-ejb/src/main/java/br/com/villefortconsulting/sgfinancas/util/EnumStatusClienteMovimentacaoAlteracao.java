package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusClienteMovimentacaoAlteracao {

    ABERTO("A", "Aberto"),
    FECHADO("F", "Fechado");

    private final String status;

    private final String descricao;

    public static EnumStatusClienteMovimentacaoAlteracao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.stream(values())
                .filter(item -> item.status.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
