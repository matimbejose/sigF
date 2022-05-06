package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoVenda {

    VENDA("VD", "Venda"),
    ORCAMENTO("OR", "Orçamento em aberto"),
    PONTUACAO("PO", "Pontuação"),
    ORCAMENTO_REJEITADO("RE", "Orçamento rejeitado"),
    ORCAMENTO_APROVADO("AP", "Orçamento aprovado"),
    ORDEM_SERVICO("OS", "Ordem de serviço");

    private final String situacao;

    private final String descricao;

    public static EnumTipoVenda retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.situacao.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String situacao) {
        for (EnumTipoVenda en : EnumTipoVenda.values()) {
            if (en.getSituacao().contains(situacao)) {
                return en.getDescricao();
            }
        }

        return null;
    }

}
