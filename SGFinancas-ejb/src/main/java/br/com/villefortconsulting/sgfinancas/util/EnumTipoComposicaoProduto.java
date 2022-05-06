package br.com.villefortconsulting.sgfinancas.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoComposicaoProduto {

    SEM_COMPOSICAO("N", "Sem composição"),
    PRODUTO_COMPOSTO("C", "Produto composto"),
    KIT_DE_PRODUTO("K", "Kit de produto");

    private final String tipo;

    private final String descricao;

}
