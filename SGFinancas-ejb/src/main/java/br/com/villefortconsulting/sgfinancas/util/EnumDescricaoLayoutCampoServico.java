package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescricaoLayoutCampoServico {

    DESCRICAO("Descrição"),
    CATEGORIA("Categoria"),
    VALOR_VENDA("Valor venda"),
    CUSTO_SERVICO("Custo do servico");

    private final String descricao;

    public static EnumDescricaoLayoutCampoServico retornaEnumSelecionado(String descricao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
