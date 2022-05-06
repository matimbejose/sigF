package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioSuspensaoFiltro extends BasicFilter<Usuario> {

    private static final long serialVersionUID = 1L;

    private String ativas;

}
