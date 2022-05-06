package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnaliseContaFiltro extends BasicFilter<EntityId> {

    private static final long serialVersionUID = 1L;

    public AnaliseContaFiltro() {
        data = new MinMax<>();
    }

    private MinMax<Date> data;

    private Fornecedor fornecedor;

    private Cliente cliente;

    private PlanoConta planoConta;

    private CentroCusto centroCusto;

    private ContaBancaria contaBancaria;

    private String situacaoConta;

    private String tipoConta;

    private String tipoRelatorio;

}
