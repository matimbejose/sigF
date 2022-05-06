package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitanteFiltro extends BasicFilter<Solicitante> {

    private static final long serialVersionUID = 1L;

    private String status;

}
