package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResultadoPlanoContaLancamentoDTO extends DesempenhoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean receita;

    public ResultadoPlanoContaLancamentoDTO(Double previsao, Double realisado, PlanoConta planoConta) {
        this.previsao = previsao;
        this.realizado = realisado;
        this.idPlanoConta = planoConta;
    }

    public ResultadoPlanoContaLancamentoDTO(PlanoContaLancamentoDTO planoContaLancamento) {
        this.idPlanoConta = planoContaLancamento.getIdPlanoConta();
        this.previsao = planoContaLancamento.getPrevisao();
        this.realizado = planoContaLancamento.getRealizado();

        this.receita = planoContaLancamento.getIdPlanoConta().getTipo().equals("C");
    }

    public Double getPrevisaoFix() {
        return this.previsao != null ? this.previsao : 0d;
    }

}
