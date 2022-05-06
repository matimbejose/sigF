package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Remessa;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RemessaFiltro extends BasicFilter<Remessa> {

    private static final long serialVersionUID = 1L;

    public RemessaFiltro() {
        data = new MinMax<>();
    }

    private MinMax<Date> data;

    private ContaBancaria contaBancaria;

}
