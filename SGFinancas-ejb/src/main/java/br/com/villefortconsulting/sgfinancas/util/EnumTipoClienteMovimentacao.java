package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Named
public enum EnumTipoClienteMovimentacao {

    SALDO_INICIAL("SI", "Saldo Inicial", false, false, true, false),
    CORRECAO("CO", "Correção", false, false, true, false),
    CUPOM_FISCAL_ELETRONICO("CFE", "CFe", true, false, false, false),
    NOTA_FISCAL_PRODUTO("NFE", "NFe", true, false, false, false),
    NOTA_FISCAL_PRODUTO_CONSUMIDOR("NFCE", "NFCe", true, false, false, false),
    NOTA_FISCAL_SERVICO("NFSE", "NFSe", true, false, false, false),
    CUPOM_FISCAL_ELETRONICO_CANCELADO("CFE-C", "CFe cancelada", true, false, false, true),
    NOTA_FISCAL_PRODUTO_CANCELADA("NFE-C", "NFe cancelada", true, false, false, true),
    NOTA_FISCAL_PRODUTO_CONSUMIDOR_CANCELADA("NFCE-C", "NFCe cancelada", true, false, false, true),
    NOTA_FISCAL_SERVICO_CANCELADA("NFSE-C", "NFSe cancelada", true, false, false, true),
    IUGU("IUGU", "Lançamento no IUGU", true, true, true, false),
    SAIDA_LANCADA("SL", "Saída lançada", true, true, true, false),
    ENTRADA_LANCADA("EL", "Entrada lançada", true, false, false, false);

    private final String tipo;

    private final String descricao;

    private final boolean permiteAlterar;

    private final boolean tipoPagamento;

    private final boolean inverteValor;

    private final boolean cancelamento;

    public static EnumTipoClienteMovimentacao retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
