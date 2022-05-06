package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumDescricaoLayoutCampoProduto {

    DESCRICAO("Descrição"),
    CATEGORIA("Categoria"),
    VALOR_VENDA("Valor venda"),
    CUSTO_SERVICO("Custo do servico"),
    UNIDADE_MEDIDA("Unidade de medida"),
    NCM("Ncm"),
    CEST("Cest"),
    CODIGO_BARRA("Código de barra"),
    PESO_BRUTO("Peso bruto"),
    PESO_LIQUIDO("Peso líquido"),
    ESTOQUE_DISPONIVEL("Estoque disponível"),
    ESTOQUE_MINIMO("Estoque mínimo"),
    ESTOQUE_MAXIMO("Estoque máximo");

    private final String descricao;

    public static EnumDescricaoLayoutCampoProduto retornaEnumSelecionado(String descricao) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.descricao.contains(descricao))
                .findAny()
                .orElse(null);
    }

}
