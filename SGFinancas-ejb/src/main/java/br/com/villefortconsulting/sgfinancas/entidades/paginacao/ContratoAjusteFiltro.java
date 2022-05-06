package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoAjuste;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContratoAjusteFiltro extends BasicFilter<ContratoAjuste> {

    private static final long serialVersionUID = 1L;

    private String dataInicio;

    private String dataFim;

    private String tipoContrato;

}
