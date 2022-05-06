package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumTipoProcessamento {

    ASSINCRONO("0", "Empresa solicita processamento assíncrono"),
    SINCRONO("1", "Empresa solicita processamento síncrono"),;

    private final String tipo;

    private final String descricao;

    EnumTipoProcessamento(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumTipoProcessamento retornaEnumSelecionado(String enumSelecionado) {
        for (EnumTipoProcessamento en : EnumTipoProcessamento.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
