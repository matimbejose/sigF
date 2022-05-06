package br.com.villefortconsulting.sgfinancas.util;

import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTipoUsuario {

    ADMINISTRADOR("AD", "Suporte"),
    CLIENTE("CL", "Cliente"),
    MASTER_VENDEDOR("MV", "Master vendedor"),
    VENDEDOR("V", "Vendedor"),
    MASTER_USUARIO("MU", "Master usuário"),
    USUARIO_COMUM("UC", "Usuário comum"),
    FUNCIONARIO("FR", "Funcionário"),
    MASTER_CONTABILIDADE("MC", "Master contabilidade"),
    CONTABILIDADE("C", "Contabilidade"),
    ANTECIPADOR("AN", "Antecipador");

    public final String tipo;

    public final String descricao;

    public static EnumTipoUsuario retornaEnumSelecionado(String enumSelecionado) {
        return Arrays.asList(values()).stream()
                .filter(item -> item.tipo.contains(enumSelecionado))
                .findAny()
                .orElse(null);
    }

    public Perfil getPerfil() {
        return new Perfil(tipo);
    }

    public Grupo getGrupo() {
        return new Grupo(tipo);
    }

}
