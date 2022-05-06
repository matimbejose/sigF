/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.PontoVenda;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PontoVendaFiltro extends BasicFilter<PontoVenda> {

    private static final long serialVersionUID = 1L;

    private String modelo;

    private String serieECF;

    private String numeroLoja;

    private Date dataAbertura;

    private Date dataCupom;

    private String coo;

    private String serieNF;

    private String status;

    private String utilizaBalanca;

}
