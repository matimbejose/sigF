package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteMovimentacaoRelatorioAnaliticoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer idCliente;

    private String nome;

    private String origem;

    private Date dataMovimentacao;

    private Date dataVencimento;

    private Date dataPagamento;

    private Double valorPrevisto;

    private Double valorPago;

    private Double valorJuros;

    private Double valorSaldo;

    private Double valorSaldoAnterior;

    private CentroCusto idCentroCusto;

    public ClienteMovimentacaoRelatorioAnaliticoDTO(Integer id, Cliente idCliente, String origem, Date dataMovimentacao, Date dataVencimento,
            Date dataPagamento, Double valorPrevisto, Double valorPago, Double valorJuros, Double valorSaldo, Double valorSaldoAnterior) {
        this.id = id;
        this.idCliente = idCliente.getId();
        this.nome = idCliente.getNome();
        this.origem = EnumTipoClienteMovimentacao.retornaEnumSelecionado(origem).getDescricao();
        this.dataMovimentacao = dataMovimentacao;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.valorPrevisto = valorPrevisto;
        this.valorPago = valorPago != null ? valorPago : 0d;
        this.valorJuros = valorJuros != null ? valorJuros : 0d;
        this.valorSaldo = valorSaldo;
        this.valorSaldoAnterior = valorSaldoAnterior;
    }

    public ClienteMovimentacaoRelatorioAnaliticoDTO(Integer id, Cliente idCliente, String origem, Date dataMovimentacao, Date dataVencimento,
            Double valorPrevisto) {
        this.id = id;
        this.idCliente = idCliente.getId();
        this.nome = idCliente.getNome();
        this.origem = EnumTipoClienteMovimentacao.retornaEnumSelecionado(origem).getDescricao();
        this.dataMovimentacao = dataMovimentacao;
        this.dataVencimento = dataVencimento;
        this.valorPrevisto = valorPrevisto;
    }

}
