package br.com.villefortconsulting.sgfinancas.nfe;

public enum EnumNFe {

    HOMOLOGACAO("Homologação", "H"),
    PRODUCAO("Produção", "P");

    private final String nome;

    private final String sigla;

    EnumNFe(String nome, String sigla) {
        this.nome = nome;
        this.sigla = sigla;
    }

    public String getNome() {
        return nome;
    }

    public String getSigla() {
        return sigla;
    }

    public static EnumNFe retornaEnumSelecionado(String enumSelecionado) {
        for (EnumNFe en : EnumNFe.values()) {
            if (en.getNome().contains(enumSelecionado) || en.getSigla().contains(enumSelecionado)) {
                return en;
            }
        }
        return null;
    }

}
