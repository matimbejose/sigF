package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContabilidadeFiltro extends BasicFilter<Contabilidade> {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

}
