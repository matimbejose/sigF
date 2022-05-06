package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumStatusSolicitacaoAdiantamento {

    AGUARDANDO("AG", "Aguardando", "Aguardando"),
    ACEITADO("AC", "Aguardando", "Aceito"),
    REJEITADO("RE", "Aguardando", "Rejeitado"),
    ESPERANDO_CONFIRMACAO_PAGAMENTO("ES", "Pago", "Esperando confirmação do pagamento"),
    PAGO("PG", "Pago", "Pago"),
    NAO_PAGO("NP", "Não pago", "Não pago");

    private final String status;

    private final String descricao;

    private final String descricaoGestor;

    public static EnumStatusSolicitacaoAdiantamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.status.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
