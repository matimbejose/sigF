package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.CondicaoPagamento;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CondicaoPagamentoFiltro extends BasicFilter<CondicaoPagamento> {

    private static final long serialVersionUID = 1L;

    private Integer qtdeParcelas;

    private String parcelasIguais;

    private Integer diasCarenciaParcela;

    private Integer intervaloDiasParcela;

    private Double fatorParcela;

    private Integer diaMesForaParcela;

}
