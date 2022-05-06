package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AnaliseContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String situacao;

    private Date dataEmissao;

    private Date dataVencimento;

    private Date dataPagamento;

    private Double valor;

    private Double valorPago;
    
    private Double valorJuros;
    
    private Double valorTarifa;

    private String fornecedor;

    private String cliente;

    private Integer diasAtraso;

    private String numDocumento;

    private String obsParcela;

    public AnaliseContaDTO(String planoConta, Double valor, Double valorPago, String fornecedor, String cliente, String situacao) {
        this.descricao = planoConta;
        this.valor = valor;
        this.valorPago = valorPago;
        this.fornecedor = fornecedor;
        this.cliente = cliente;
        this.situacao = situacao;
    }

    public AnaliseContaDTO(String descricao, String situacao, Date dataEmissao, Date dataVencimento, Date dataPagamento, Double valor, Double valorPago, String fornecedor, String cliente, String numDocumento, String obsParcela, String obsConta, Double valorJuros, Double valorTarifa) {
        this.descricao = descricao;
        this.situacao = situacao;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.valor = valor;
        this.valorPago = valorPago;
        this.valorJuros = valorJuros != null? valorJuros: 0d;
        this.valorTarifa = valorTarifa != null? valorTarifa: 0d;
        this.fornecedor = fornecedor;
        this.cliente = cliente;
        Date aux = dataPagamento;
        if (aux == null) {
            aux = new Date();
        }
        this.diasAtraso = Math.max(DataUtil.diferencaEntreDias(dataVencimento, aux), 0);
        this.numDocumento = numDocumento;
        this.obsParcela = (obsParcela != null ? obsParcela : "") + ((obsParcela != null && obsConta != null ? " - " : "")) + (obsConta != null ? obsConta : "");
    }

    public boolean isEmAtraso() {
        return (this.dataPagamento == null || this.valorPago == null || this.valorPago < 0.01d) && DataUtil.diferencaEntreDias(this.dataVencimento, new Date()) > 0;
    }

    public boolean isAVencer() {
        return (this.dataPagamento == null || this.valorPago == null || this.valorPago < 0.01d) && DataUtil.diferencaEntreDias(this.dataVencimento, new Date()) <= 0;
    }

}
