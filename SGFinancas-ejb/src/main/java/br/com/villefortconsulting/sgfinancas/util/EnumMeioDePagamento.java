package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumMeioDePagamento {

    DINHEIRO("01", "Dinheiro", false),
    CHEQUE("02", "Cheque", false),
    CARTAO_DE_CREDITO("03", "Cartão de Crédito", true),
    CARTAO_DE_DEBITO("04", "Cartão de Débito", true),
    CREDITO_DA_LOJA("05", "Crédito Loja", false),
    VALE_ALIMENTACAO("10", "Vale Alimentação", false),
    VALE_REFEICAO("11", "Vale Refeição", false),
    VALE_PRESENTE("12", "Vale Presente", false),
    VALE_COMBUSTIVEL("13", "Vale Combustível", false),
    BOLETO("15", "Boleto Bancário", true),
    PIX("07", "Pix", false),
    SEM_PAGAMENTO("90", "Sem Pagamento", false),
    OUTRO("99", "Outros", false);

    private final String codigo;

    private final String descricao;

    private final boolean permiteLinkPagamento;

    public static EnumMeioDePagamento retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.codigo.equals(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
