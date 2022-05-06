package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoConfirmacaoAgendamento {

    AUTOMATICA("A", "Automática (automaticamente aprovado após a criação do agendamento)"),
    MANUAL("F", "Aprovada por funcionário (manualmente, aprovado por um funcionário)"),
    PAGA("P", "Mediante pagamento (automaticamente, após confirmação do pagamento)");

    private final String tipo;

    private final String descricao;

    public static EnumTipoConfirmacaoAgendamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
