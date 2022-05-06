package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumCodigoModeloFiscal {

    NF_E("55", "NF-e emitida em substituição ao modelo 1 ou 1A"),
    NFC_E("65", "NFC-e utilizada nas operações de venda no varejo"),;

    private final String tipo;

    private final String descricao;

    EnumCodigoModeloFiscal(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumCodigoModeloFiscal retornaEnumSelecionado(String enumSelecionado) {
        for (EnumCodigoModeloFiscal en : EnumCodigoModeloFiscal.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
