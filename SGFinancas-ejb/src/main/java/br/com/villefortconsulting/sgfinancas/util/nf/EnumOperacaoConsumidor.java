package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumOperacaoConsumidor {

    NORMAL("0", "Normal"),
    CONSUMIDOR_FINAL("1", "Consumidor final"),;

    private final String tipo;

    private final String descricao;

    EnumOperacaoConsumidor(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumOperacaoConsumidor retornaEnumSelecionado(String enumSelecionado) {
        for (EnumOperacaoConsumidor en : EnumOperacaoConsumidor.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
