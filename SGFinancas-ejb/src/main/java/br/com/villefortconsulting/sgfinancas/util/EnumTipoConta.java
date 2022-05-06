package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoConta {

    NORMAL("N", "Conta sem vínculo"),
    MENSALIDADE("M", "Mensalidade"),
    LANCADA("L", "Lançada"),
    COMPRA("C", "Compra"),
    VENDA("V", "Venda"),
    TAXA_INSTALACAO("I", "Tx. instalação"),
    TAXA_ADESAO("A", "Tx. adesão"),
    CONTRATO_CLIENTE("K", "Contrato cliente"),
    CONTRATO_FORNECEDOR("F", "Contrato fornecedor"),
    TRANSFERENCIA("T", "Transferência"),
    LANCAMENTO_CONTABIL("B", "Lançamento Contábil"),
    ORDEM_DE_SERVICO("O", "Ordem de serviço");

    private final String tipo;

    private final String descricao;

    public static EnumTipoConta retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public static String getDescricao(String enumSelecionado) {
        for (EnumTipoConta en : EnumTipoConta.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en.getDescricao();
            }
        }

        return null;
    }

}
