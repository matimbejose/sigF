package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoContaLancamentoFiltro extends BasicFilter<PlanoContaLancamentoContabil> {

    private static final long serialVersionUID = 1L;

    private String dataInicio;

    private String dataFim;

}
