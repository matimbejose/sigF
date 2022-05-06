package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioFiltro extends BasicFilter<Usuario> {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    private Perfil perfil;

    private String tenant;

    private String nome;

    private String login;

    private String email;

    private String tipoPerfil;

}
