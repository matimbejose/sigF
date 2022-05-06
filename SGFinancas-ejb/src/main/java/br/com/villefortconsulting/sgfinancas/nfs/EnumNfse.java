package br.com.villefortconsulting.sgfinancas.nfs;

public enum EnumNfse {

    BH_MG("Belo Horizonte", "http://ws.bhiss.pbh.gov.br"),
    FA_PA("Fazenda Rio Grande", "http://www.betha.com.br/e-nota-contribuinte-ws"),;

    private final String cidade;

    private final String url;

    EnumNfse(String cidade, String url) {
        this.cidade = cidade;
        this.url = url;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUrl() {
        return url;
    }

    public static EnumNfse retornaEnumSelecionado(String enumSelecionado) {
        for (EnumNfse en : EnumNfse.values()) {
            if (en.getCidade().contains(enumSelecionado)) {
                return en;
            }
        }
        return null;
    }

}
