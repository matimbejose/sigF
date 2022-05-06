package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumCodigoReceitaDespesa {

    CODIGO_RECEITA("4", "Receita"),
    CODIGO_DESPESA("3", "Despesa"),
    CODIGO_CMV("5", "Contas de apuração"),;

    private final String tipo;

    private final String descricao;

    EnumCodigoReceitaDespesa(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumCodigoReceitaDespesa retornaEnumSelecionado(String enumSelecionado) {
        for (EnumCodigoReceitaDespesa en : EnumCodigoReceitaDespesa.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;
    }

}
