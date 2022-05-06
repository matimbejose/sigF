package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendaProdutoFiltro extends BasicFilter<VendaProduto> {

    private static final long serialVersionUID = 1L;

    private String dataInicio;

    private String dataFim;

}
