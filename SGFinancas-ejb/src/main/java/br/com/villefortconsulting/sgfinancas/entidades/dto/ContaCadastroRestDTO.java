package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContaCadastroRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer idParcela;

    private String tipoConta;

    private String tipoContaDescricao;

    private String tipoTransacao;

    private String tipoTransacaoDescricao;

    private Date dataVencimento;

    private String situacao;

    private String repetirConta;

    private String intervaloRepeticao;

    private Integer qteRepeticoes;

    private Date dataCancelamento;

    private String observacao;

    private Integer numeroParcelas;

    private Integer idCliente;

    private Integer idFornecedor;

    private Integer idContaBancaria;

    private Integer idPlanoConta;

    private Integer idCentroCusto;

    private Integer idTipoPagamento;

    private boolean temDocumento;

    private boolean informarRecebimento;

    private Double valorPago;

    private BaixaContaParcelaDTO dadosParcela;

    private List<ValorParcelaDTO> detalheParcela;

}
