package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanoPagamentoFiltro extends BasicFilter<PlanoPagamento> {

    private static final long serialVersionUID = 1L;

    private String possuiEntrada;

}
