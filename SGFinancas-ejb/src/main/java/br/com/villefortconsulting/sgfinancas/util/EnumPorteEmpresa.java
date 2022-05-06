package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPorteEmpresa {

    MEI("MEI", "Micro Empreendedor Individual", true),
    ME("ME", "Micro Empreendedor", true),
    EPP("EPP", "Empreendedor de Pequeno Porte", true),
    PROFISSIONAL_LIBERAL("PL", "Profissional Liberal", false),
    AUTONOMO("A", "Autônomo", false),
    SOCIEDADE_EMPRESARIA_LIMITADA("SEL", "Sociedade Empresária Limitada", true),
    SOCIEDADE_SIMPLES_LIMITADA("SSL", "Sociedade Simples Limitada", true),
    EIRELI("EIRL", " Empresa Individual de Responsabilidade Limitada", true),
    SA("SA", " Sociedade Anônima", true),
    ASSOCIACAO_PRIVADA("AP", "Associação Privada", true);

    private final String sigla;

    private final String descricao;

    private final boolean ehPJ;

    public static String getDescricao(String sigla) {
        EnumPorteEmpresa e = retornaEnumSelecionado(sigla);
        if (e == null) {
            return null;
        }
        return e.getDescricao();
    }

    public static EnumPorteEmpresa retornaEnumSelecionado(String sigla) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.sigla.contains(sigla))
                .findAny()
                .orElse(null);
    }

}
