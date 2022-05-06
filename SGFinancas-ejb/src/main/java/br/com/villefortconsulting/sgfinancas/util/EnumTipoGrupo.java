package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoGrupo {

    SUPORTE("AD", "Suporte"),
    VENDEDOR("V", "Vendedor"),
    MASTER_VENDEDOR("MV", ""),
    USUARIO_MASTER("MU", "Usuário master"),
    USUARIO_COMUM("UC", "Usuário comum"),
    FUNCIONARIO("FR", "Funcionário"),
    MASTER_CONTABILIDADE("MC", "Master contabilidade"),
    CONTABILIDADE("C", "Contabilidade"),
    ANTECIPADOR("AN", "Antecipador");

    private final String tipo;

    private final String descricaoTipo;

    public static String getDescricao(String tipo) {
        EnumTipoGrupo e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricaoTipo();
    }

    public static EnumTipoGrupo retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
