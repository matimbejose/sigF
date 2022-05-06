package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.Producao;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusProducao;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProducaoFiltro extends BasicFilter<Producao> {

    private static final long serialVersionUID = 1L;

    public ProducaoFiltro() {
        dataPedido = new MinMax<>();
        dataProducao = new MinMax<>();
    }

    private Produto idProduto;

    private Double quantidade;

    private MinMax<Date> dataPedido;

    private MinMax<Date> dataProducao;

    private Integer numero;

    private List<EnumStatusProducao> status;

    private Usuario usuarioLogado;

}
