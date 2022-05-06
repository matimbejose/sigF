package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumDestinoOperacao {

    OPERACAO_INTERNA("1", "Operação interna"),
    OPERACAO_INTERESTADUAL("2", "Operação interestadual"),
    OPERACAO_COM_EXTERIOR("3", "Operação com exterior"),;

    private final String tipo;

    private final String descricao;

    EnumDestinoOperacao(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumDestinoOperacao retornaEnumSelecionado(String enumSelecionado) {
        for (EnumDestinoOperacao en : EnumDestinoOperacao.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
