package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntegracaoParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private TransacaoIntegracaoBancaria transacaoIntegracaoBancaria;

    private List<ContaParcela> parcelas;

    private ContaBancaria contaBancaria;

    private boolean selected = false;

    public String getRowStyle() {
        return selected ? "ui-state-highlight" : "";
    }

    public Double getPorcentagem() {
        return Math.abs(parcelas.stream()
                .map(ContaParcela::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b) / transacaoIntegracaoBancaria.getValor());
    }

    public Double getValorRestante() {
        return Math.max(Math.abs(transacaoIntegracaoBancaria.getValor()) - Math.abs(parcelas.stream()
                .map(ContaParcela::getValorTotal)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b)),0);
    }

    public String getIcone() {
        int porcentagem = (int) Math.ceil((getPorcentagem() * 10000));
        if (porcentagem == 0) {
            return "fa fa-times text-danger";
        } else if (porcentagem < 10000) {
            return "fa fa-check text-primary";
        }
        return "fa fa-check text-success";
    }

    public String getTitle() {
        int porcentagem = (int) Math.ceil((getPorcentagem() * 10000));
        if (porcentagem == 0) {
            return "Não conciliada";
        } else if (porcentagem < 10000) {
            return "Parcialmente conciliada";
        }
        return "Conciliada";
    }

}
