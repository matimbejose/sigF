package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoTransacao {

    INICIADO("1", "Iniciado"),
    BOLETO("2", "Boleto impresso"),
    CANCELADO("3", "Pagamento Cancelado pelo emissor"),
    ANALISE("4", "Pagamento em analise"),
    APROVADO("5", "Aprovado"),
    APROVADO_PARCIAL("6", "Aprovado valor parcial"),
    RECUSADO("7", "Pagamento recusado"),
    APROVADO_CAPTURADO("8", "Aprovado e Capturado");

    public final String situacao;

    public final String descricao;

    public static EnumSituacaoTransacao retornarEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String retornoDescricaoEnumSituacaoTransacao(String situacao) {
        EnumSituacaoTransacao enumSituacaoTransacao = retornarEnumSelecionado(situacao);
        if (enumSituacaoTransacao != null) {
            return enumSituacaoTransacao.getDescricao();
        }
        return null;
    }

}
