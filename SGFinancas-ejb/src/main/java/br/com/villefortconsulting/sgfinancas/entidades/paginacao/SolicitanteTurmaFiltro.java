package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteTurma;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitanteTurmaFiltro extends BasicFilter<SolicitanteTurma> {

    private static final long serialVersionUID = 1L;

    private Turma turma;

}
