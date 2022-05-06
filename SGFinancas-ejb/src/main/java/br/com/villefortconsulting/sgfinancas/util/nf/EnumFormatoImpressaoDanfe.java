package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumFormatoImpressaoDanfe {

    SEM_GERACAO("0", "Sem geração de DANFE"),
    NORMAL("1", "DANFE normal, Retrato"),
    RETRATO("2", "DANFE normal, Paisagem"),
    SIMPLIFICADO("3", "DANFE Simplificado"),
    NFC_E("4", "DANFE NFC-e"),
    NFC_E_MENSAGEM_ELETRONICA("5", "DANFE NFC-e em mensagem eletrônica"),;

    private final String tipo;

    private final String descricao;

    EnumFormatoImpressaoDanfe(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumFormatoImpressaoDanfe retornaEnumSelecionado(String enumSelecionado) {
        for (EnumFormatoImpressaoDanfe en : EnumFormatoImpressaoDanfe.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
