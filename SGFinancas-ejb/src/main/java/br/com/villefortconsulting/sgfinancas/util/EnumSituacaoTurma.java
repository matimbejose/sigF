package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoTurma {

    ABERTA("A", "Aberta"),
    FECHADA("F", "Fechada");

    private final String situacao;

    private final String descricaoSituacao;

    public static String getDescricao(String situacao) {
        EnumSituacaoTurma e = retornaEnumSelecionado(situacao);
        if (e == null) {
            return null;
        }
        return e.getDescricaoSituacao();
    }

    public static EnumSituacaoTurma retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
