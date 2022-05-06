package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoConta {

    PAGA("PG", "Pago", "Recebido"),
    NAO_PAGA("NP", "Não pago", "Não recebido"),
    PAGA_PARCIALMENTE("PP", "Pago parcialmente", "Recebido parcialmente"),
    CANCELADO("CC", "Cancelado", "Cancelado"),
    INTERROMPIDO("IT", "Interrompido", "Interrompido");

    private final String situacao;

    private final String descricaoSituacao;

    private final String descricaoSituacaoReceber;

    public static String getDescricao(String opcao) {
        EnumSituacaoConta e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricaoSituacao();
    }

    public static String getDescricaoReceber(String opcao) {
        EnumSituacaoConta e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricaoSituacaoReceber();
    }

    public static EnumSituacaoConta retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String retornaDescricaoPorSituacao(String situacao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.equals(situacao))
                .findAny()
                .map(EnumSituacaoConta::getDescricaoSituacao)
                .orElse("");
    }

}
