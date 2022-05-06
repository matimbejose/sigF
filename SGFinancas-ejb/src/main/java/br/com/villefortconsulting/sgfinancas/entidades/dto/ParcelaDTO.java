package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer numParcela;

    private Double juros;

    private Double encargos;

    private Double multa;

    private Double outrosCustos;

    private Double ir;

    private Double pis;

    private Double csll;

    private Double inss;

    private Double cofins;

    private Double iss;

    private Double valor;

    private Double valorLiquido;

    @Builder.Default
    private Double valorPago = 0d;

    private Double desconto;

    private Date dataVencimento;

    private Date dataPagamento;

    private Date dataEmissao;

    private Date dataCancelamento;

    private String observacao;

    private String fechada;

    @Builder.Default
    private String situacao = "Não pago";

}
