package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private FornecedorDTO fornecedor;

    private ContaBancariaDTO contaBancaria;

    private TransportadoraDTO transportadora;

    private PlanoContaDTO planoConta;

    private CentroCustoDTO centroCusto;

    private Date dataCompra;

    private Date dataCancelamento;

    private Date prazoRecebimento;

    private Date dataPedido;

    private Integer numParcela;

    private String condicaoPagamento;

    private String formaPagamento;

    private String numeroPedido;

    private Double valorTotal;

    private String situacao;

    private String statusPagamento;

    private Double valorDesconto;

    private String observacao;

    private String serie;

    private String numeroNf;

    private String numCompra;

    private Double valorFrete;

    private String nReferencia;

    private Integer idConta;

    private List<CompraProdutoDTO> listCompraProduto;

}
