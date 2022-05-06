package br.com.villefortconsulting.sgfinancas.util;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoPerfil {

    SUPORTE("AD", "Suporte"),
    CLIENTE("CL", "Cliente"),
    VENDEDOR("V", "Vendedor"),
    MASTER_VENDEDOR("MV", ""),
    USUARIO_MASTER("MU", "Usuário master"),
    USUARIO_COMUM("UC", "Usuário comum"),
    FUNCIONARIO("FR", "Funcionário"),
    MASTER_CONTABILIDADE("MC", "Master contabilidade"),
    CONTABILIDADE("C", "Contabilidade");

    private final String tipo;

    private final String descricaoTipo;

    public static String getDescricao(String tipo) {
        EnumTipoPerfil e = retornaEnumSelecionado(tipo);
        if (e == null) {
            return null;
        }
        return e.getDescricaoTipo();
    }

    public static EnumTipoPerfil retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

}
