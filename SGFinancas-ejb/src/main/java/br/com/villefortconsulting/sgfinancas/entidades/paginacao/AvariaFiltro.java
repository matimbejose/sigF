package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvariaFiltro extends BasicFilter<Avaria> {

    private static final long serialVersionUID = 1L;

    private String codigo;

}
