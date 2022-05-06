/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoTransacaoGetnet {
    PAGAMENTO_VENCIDO("V", "Pagamento vencido"),
    CANCELADO("C", "Pagamento cancelado"),
    PENDENTE("P", "Pagamento pendente"),
    NEGADO("N", "Pagamento Negado"),
    AUTORIZADO("A", "Pagamento autorizado");

    private final String sitaucao;

    private final String descricao;

    public static EnumSituacaoTransacaoGetnet retornarEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.sitaucao.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
