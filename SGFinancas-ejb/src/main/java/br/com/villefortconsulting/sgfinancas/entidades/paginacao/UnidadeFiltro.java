package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Bloco;
import br.com.villefortconsulting.sgfinancas.entidades.Unidade;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnidadeFiltro extends BasicFilter<Unidade> {

    private static final long serialVersionUID = 1L;

    private Bloco bloco;

}
