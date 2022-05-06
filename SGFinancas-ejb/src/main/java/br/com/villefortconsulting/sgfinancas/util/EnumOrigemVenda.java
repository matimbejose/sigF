package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Named
public enum EnumOrigemVenda {

    SITE("ST", "Site", ""),
    APP("AP", "Aplicativo", ""),
    AGENDAMENTO("AG", "Agendamento", "Aguardando aprovação"),
    AGENDAMENTO_APROVADO("AA", "Agendamento aprovado", "Agendamento aprovado");

    private final String origem;

    private final String descricao;

    private final String descricaoOrcamento;

    public static EnumOrigemVenda retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.origem.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
