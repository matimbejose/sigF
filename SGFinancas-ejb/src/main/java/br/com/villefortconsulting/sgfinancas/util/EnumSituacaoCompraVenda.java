package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumSituacaoCompraVenda {

    ATIVO("A", "Ativa"),
    CANCELADO("C", "Cancelada"),
    DELETADO("D", "Deletado");

    private final String situacao;

    private final String descricao;

    public static EnumSituacaoCompraVenda retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
