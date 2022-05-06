package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaParcelaRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String observacao;

    private Date dataVencimento;

    private Integer numParcela;

    private Double valor;

    private Double valorTotal;

    private Double encargo;

    private Date dataCancelamento;

    private String situacao;

    private String fechada;

    private Date dataPagamento;

    private Double valorPago;

    private Double outrosCustos;

    private Double juros;

    private Double desconto;

    private Double acrescimo;

    private Double multa;

    private Double encargos;

    private Double valorIR;

    private Double valorPIS;

    private Double valorCSLL;

    private Double valorINSS;

    private Double valorCOFINS;

    private Double valorISS;

    private ContaRestDTO conta;

    private ContaBancariaDTO contaBancaria;

    private CentroCustoDTO centroCusto;

    private FormaPagamentoDTO formaPagamento;

    private TipoPagamentoDTO tipoPagamento;

    private boolean temDocumento;

    private String numeroDocumento;

}
