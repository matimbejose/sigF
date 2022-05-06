package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumPresencaComprador {

    NAO_APLICA("0", "Não se aplica"),
    PRESENCIAL("1", "Operação presencial"),
    NAO_PRESENCIAL_INTERNET("2", "Operação não presencial, pela Internet"),
    NAO_PRESENCIAL_TELEATENDIMENTO("3", "Operação não presencial, Teleatendimento"),
    NFCE_ENTREGA_DOMICILIO("4", "NFC-e em operação com entrega a domicílio"),
    NAO_PRESENCIAL_OUTROS("9", "Operação não presencial, outros."),;

    private final String tipo;

    private final String descricao;

    EnumPresencaComprador(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumPresencaComprador retornaEnumSelecionado(String enumSelecionado) {
        for (EnumPresencaComprador en : EnumPresencaComprador.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
