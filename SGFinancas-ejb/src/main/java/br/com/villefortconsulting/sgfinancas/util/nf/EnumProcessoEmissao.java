package br.com.villefortconsulting.sgfinancas.util.nf;

public enum EnumProcessoEmissao {

    COM_APLICATIVO_CONTRIBUINTE("0", "Emissão de NF-e com aplicativo do contribuinte"),
    AVULSA_FISCO("1", "Emissão de NF-e avulsa pelo Fisco"),
    AVULSA_CONTRIBUINTE_SITE_FISCO("2", "Emissão de NF-e avulsa, pelo contribuinte com seu certificado digital, através do site do Fisco"),
    CONTRIBUINTE_APP_FISCOP("3", "Emissão NF-e pelo contribuinte com aplicativo fornecido pelo Fisco"),;

    private final String tipo;

    private final String descricao;

    EnumProcessoEmissao(String tipo, String descricao) {
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EnumProcessoEmissao retornaEnumSelecionado(String enumSelecionado) {
        for (EnumProcessoEmissao en : EnumProcessoEmissao.values()) {
            if (en.getTipo().contains(enumSelecionado)) {
                return en;
            }
        }

        return null;

    }

}
