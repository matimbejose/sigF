package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class FluxoCaixaDTO extends ValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mes;

    private String nomeMes;

    private Double saldoInicial;

    private Double receita;

    private Double despesa;

    private Double lucroPrejuizo;

    private Double acumulado;

    private Double lucratividade;

    private String tipo;

    public Double getValorByTipo(final String tipo) {
        switch (tipo) {
            case "si":
                return saldoInicial;
            case "r":
                return receita;
            case "d":
                return despesa;
            case "lp":
                return lucroPrejuizo;
            case "a":
                return acumulado;
            case "l":
                return lucratividade;
            default:
                return null;
        }
    }

}
