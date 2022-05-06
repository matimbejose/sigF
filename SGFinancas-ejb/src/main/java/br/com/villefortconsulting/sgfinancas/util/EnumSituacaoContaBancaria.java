package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoContaBancaria {

    ATIVA("A", "Ativa"),
    CANCELADA("C", "Cancelada");

    private final String situacao;

    private final String descricaoSituacao;

    public static String getDescricao(String situacao) {
        EnumSituacaoContaBancaria e = retornaEnumSelecionado(situacao);
        if (e == null) {
            return null;
        }
        return e.getDescricaoSituacao();
    }

    public static EnumSituacaoContaBancaria retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
