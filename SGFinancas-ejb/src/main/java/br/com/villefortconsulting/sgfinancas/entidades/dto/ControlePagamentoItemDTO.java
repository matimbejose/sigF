package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.report.Cell;
import br.com.villefortconsulting.util.report.Row;
import br.com.villefortconsulting.util.report.Style;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;

@Getter
public class ControlePagamentoItemDTO implements Serializable, Comparable<ControlePagamentoItemDTO> {

    private static final long serialVersionUID = 1L;

    private final boolean lancamento;

    private final Integer id;

    private final String observacao;

    private final Date dataEmissao;

    private final Date dataPagamento;

    private final Date dataVencimento;

    private final Double valor;

    /**
     * Construtor para ser utilizado no repositório para preenchimento a partir de um ExtratoContaCorrente
     *
     * @param id
     * @param observacao
     * @param dataPagamento
     * @param valor
     */
    public ControlePagamentoItemDTO(Integer id, String observacao, Date dataPagamento, Double valor) {
        this.lancamento = false;
        this.id = id;
        this.observacao = observacao;
        this.dataEmissao = null;
        this.dataPagamento = dataPagamento;
        this.dataVencimento = null;
        this.valor = valor;
    }

    /**
     * Construtor para ser utilizado no repositório para preenchimento a partir de uma ContaParcela
     *
     * @param id
     * @param observacao
     * @param dataEmissao
     * @param dataVencimento
     * @param valor
     */
    public ControlePagamentoItemDTO(Integer id, String observacao, Date dataEmissao, Date dataVencimento, Double valor) {
        this.lancamento = true;
        this.id = id;
        this.observacao = observacao;
        this.dataEmissao = dataEmissao;
        this.dataPagamento = null;
        this.dataVencimento = dataVencimento;
        this.valor = valor;
    }

    public Double getValorFinal() {
        return valor * (lancamento ? 1 : -1);
    }

    public Row toRow(Style date, Style money) {
        if (lancamento) {
            return new Row(new Cell(dataEmissao, date), new Cell("Conta a pagar"), new Cell(id), new Cell(observacao), new Cell(dataVencimento, date), new Cell(valor, money), new Cell(), new Cell());
        }
        return new Row(new Cell(), new Cell("Pagamento"), new Cell(id), new Cell(observacao), new Cell(), new Cell(), new Cell(dataPagamento, date), new Cell(valor, money));
    }

    public static Row headerRow() {
        return new Row(new Cell("DATA DE EMISSÃO"),
                new Cell("HISTORICO"), new Cell("TÍTULO"), new Cell("OBSERVAÇÃO"),
                new Cell("DATA VENCIMENTO"), new Cell("VALOR A PAGAR"),
                new Cell("DATA PAGAMENTO"), new Cell("VALOR PAGO"));
    }

    @Override
    public int compareTo(ControlePagamentoItemDTO o) {
        Date d1 = this.dataEmissao;
        if (d1 == null) {
            d1 = this.dataVencimento;
        }
        if (d1 == null) {
            d1 = this.dataPagamento;
        }
        Date d2 = o.dataEmissao;
        if (d2 == null) {
            d2 = o.dataVencimento;
        }
        if (d2 == null) {
            d2 = o.dataPagamento;
        }
        return d1.compareTo(d2);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.valor);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ControlePagamentoItemDTO other = (ControlePagamentoItemDTO) obj;
        if (this.lancamento != other.lancamento) {
            return false;
        }
        if (!Objects.equals(this.observacao, other.observacao)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.dataEmissao, other.dataEmissao)) {
            return false;
        }
        if (!Objects.equals(this.dataPagamento, other.dataPagamento)) {
            return false;
        }
        if (!Objects.equals(this.dataVencimento, other.dataVencimento)) {
            return false;
        }
        return Objects.equals(this.valor, other.valor);
    }

}
