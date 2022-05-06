package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoFiltro extends BasicFilter<Boleto> {

    private static final long serialVersionUID = 1L;

    public BoletoFiltro() {
        data = new MinMax<>();
    }

    private ContaBancaria contaBancaria;

    private MinMax<Date> data;

}
