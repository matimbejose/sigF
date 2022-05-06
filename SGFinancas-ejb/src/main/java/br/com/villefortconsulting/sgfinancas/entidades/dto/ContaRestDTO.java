package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String tipoConta;

    private String tipoContaDescricao;

    private String tipoTransacao;

    private String tipoTransacaoDescricao;

    private Date dataVencimento;

    private Date dataPagamento;

    private Double valorPago;

    private String situacao;

    private String repetirConta;

    private Date dataCancelamento;

    private String observacao;

    private Double valor;

    private Integer numeroParcelas;

    private ClienteDTO cliente;

    private FornecedorDTO fornecedor;

    private ContaBancariaDTO contaBancaria;

    private PlanoContaDTO planoConta;

    private CentroCustoDTO centroCusto;

    private boolean temDocumento;

}
