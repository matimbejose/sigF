package br.com.villefortconsulting.sgfinancas.util.nf;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumAmbienteEmissaoNF {

    PRODUCAO("1", "Produção"),
    HOMOLOGACAO("2", "Homologação"),;

    private final String tipo;

    private final String descricao;

    public static String getDescricao(String opcao) {
        EnumAmbienteEmissaoNF e = retornaEnumSelecionado(opcao);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumAmbienteEmissaoNF retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
