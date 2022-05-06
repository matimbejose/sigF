package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumTipoEntradaSaida {

    ENTRADA("0", "Entrada"),
    SAIDA("1", "Saida"),;

    private final String tipo;

    private final String descricao;

    EnumTipoEntradaSaida(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumTipoEntradaSaida retornaEnumSelecionado(String enumSelecionado) {
        for (EnumTipoEntradaSaida en : EnumTipoEntradaSaida.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
