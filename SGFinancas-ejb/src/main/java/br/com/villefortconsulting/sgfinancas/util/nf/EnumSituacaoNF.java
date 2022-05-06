package br.com.villefortconsulting.sgfinancas.util.nf;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoNF {

    PROCESSANDO("P", "Em processamento"),
    PROCESSANDO_CANCELAMENTO("L", "Cancelamento em processamento"),
    RASCUNHO("R", "Rascunho"),
    ENVIADA("E", "Transmitida"),
    REJEITADA("J", "Rejeitada"),
    REJEITADA_CANCELAMENTO("G", "Rejeitada"),
    CANCELADA("C", "Cancelada"),
    RASCUNHO_DEVOLUCAO("B", "Rascunho de devolução"),
    REJEITADA_DEVOLUCAO("K", "Devolução rejeitada"),
    CANCELADA_DEVOLUCAO("F", "Cancelamento rejeitado"),
    ENVIADA_DEVOLUCAO("D", "Devolvida"),;

    private final String situacao;

    private final String descricao;

    public static String getDescricao(String opcao) {
        EnumSituacaoNF e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumSituacaoNF retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
