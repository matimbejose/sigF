package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValorParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("numParcela")
    private Integer numero;

    private Double valor;

    private Date vencimento;

    private boolean alterouValor = false;

    public void setValor(Double d) {
        this.valor = d;
        this.alterouValor = true;
    }

    public void reset(Double d) {
        if (!this.alterouValor) {
            this.valor = d;
        }
    }

}
