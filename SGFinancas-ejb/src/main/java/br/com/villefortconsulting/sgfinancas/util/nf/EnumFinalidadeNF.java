package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumFinalidadeNF {

    NF_NORMAL("1", "NF-e normal"),
    NF_COMPLEMENTAR("2", "NF-e complementar"),
    NF_AJUSTE("3", "NF-e de ajuste"),
    NF_DEVOLUCAO_MERCADORIA("4", "Devolução de mercadoria"),;

    private final String tipo;

    private final String descricao;

    EnumFinalidadeNF(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumFinalidadeNF retornaEnumSelecionado(String enumSelecionado) {
        for (EnumFinalidadeNF en : EnumFinalidadeNF.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
