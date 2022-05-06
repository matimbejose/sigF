package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExtratoContaCorrenteFiltro extends BasicFilter<ExtratoContaCorrente> {

    private static final long serialVersionUID = 1L;

    public ExtratoContaCorrenteFiltro() {
        data = new MinMax<>();
    }

    private ContaBancaria contaBancaria;

    private MinMax<Date> data;

    @Override
    public String toString() {
        List<String> filtros = new ArrayList<>();

        if (this.descricao != null && !this.descricao.trim().isEmpty()) {
            filtros.add("Descrição: " + this.descricao);
        }
        if (filtros.isEmpty()) {
            return "N/A";
        }
        return StringUtil.listaParaTexto(filtros);
    }

}
