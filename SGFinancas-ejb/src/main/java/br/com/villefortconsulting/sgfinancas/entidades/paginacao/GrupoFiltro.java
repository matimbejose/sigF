package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GrupoFiltro extends BasicFilter<Grupo> {

    private static final long serialVersionUID = 1L;

    private Empresa empresa;

    private Usuario usuario;

    private String visualizaGestaoInterna;

    private Usuario usuarioLogado;

}
