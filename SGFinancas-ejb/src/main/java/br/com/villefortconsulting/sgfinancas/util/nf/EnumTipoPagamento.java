package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumTipoPagamento {

    A_VISTA("0", "Pagamento à vista"),
    A_PRAZO("1", "Pagamento a prazo"),
    OUTROS("2", "Outros"),;

    private final String tipo;

    private final String descricao;

    EnumTipoPagamento(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumTipoPagamento retornaEnumSelecionado(String enumSelecionado) {
        for (EnumTipoPagamento en : EnumTipoPagamento.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
