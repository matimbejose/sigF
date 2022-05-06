package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoPlanoContaLancamentoContabil {

    ATIVO("A", "Ativa"),
    CANCELADO("C", "Cancelada");

    private final String situacao;

    private final String descricao;

    public static EnumSituacaoPlanoContaLancamentoContabil retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
