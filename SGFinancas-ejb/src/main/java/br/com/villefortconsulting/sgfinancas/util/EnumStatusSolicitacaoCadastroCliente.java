package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusSolicitacaoCadastroCliente {

    AGUARDANDO("W", "Aguardando"),
    APROVADO("A", "Aprovado"),
    REJEITADO("D", "Rejeitado");

    private final String sigla;

    private final String descricao;

    public static EnumStatusSolicitacaoCadastroCliente retornaEnumSelecionado(String sigla) {
        return Arrays.stream(values())
                .filter(item -> item.sigla.equals(sigla))
                .findAny()
                .orElse(null);
    }

}
