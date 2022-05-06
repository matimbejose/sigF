package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaixaContaParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean pagamentoIntegral;

    private Integer idFormaPagamento;

    private Integer contaBancaria;

    private Date dataPagamento;

    private Double valor;

    private Double valorJuros;

    private Double valorMulta;

    private Double valorEncargos;

    private Double valorIR;

    private Double valorPIS;

    private Double valorCSLL;

    private Double valorINSS;

    private Double valorCOFINS;

    private Double valorISS;

    private Double valorICMS;

    private Double valorOutrosCustos;

    private Double valorDescontos;

    private String numeroDocumento;

    public Double getValorPagamento() {
        return valor;
    }

    public void setValorPagamento(Double valorPagamento) {
        this.valor = valorPagamento;
    }

}
