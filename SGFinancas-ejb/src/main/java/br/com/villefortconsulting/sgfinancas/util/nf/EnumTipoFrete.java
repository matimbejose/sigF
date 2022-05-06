package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumTipoFrete {

    POR_CONTA_EMITENTE("0", "Por conta do emitente"),
    POR_CONTA_REMETENTE("1", "Por conta do destinatário/remetente"),
    POR_CONTA_TERCEIROS("2", "Por conta de terceiros"),
    PROPRIO_PELO_REMETENTE("3", "Próprio por conta do remetente"),
    PROPRIO_PELO_DESTINATARIO("4", "Próprio por conta do destinatario"),
    SEM_FRETE("9", "Sem frete"),;

    private final String tipo;

    private final String descricao;

    EnumTipoFrete(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumTipoFrete retornaEnumSelecionado(String enumSelecionado) {
        for (EnumTipoFrete en : EnumTipoFrete.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
