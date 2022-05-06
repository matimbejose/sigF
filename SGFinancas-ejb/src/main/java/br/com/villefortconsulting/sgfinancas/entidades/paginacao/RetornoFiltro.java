/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Retorno;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RetornoFiltro extends BasicFilter<Retorno> {

    private static final long serialVersionUID = 1L;

    public RetornoFiltro() {
        data = new MinMax<>();
    }

    private MinMax<Date> data;

    private ContaBancaria contaBancaria;

}
