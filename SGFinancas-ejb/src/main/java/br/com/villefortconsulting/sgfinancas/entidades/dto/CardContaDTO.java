package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import lombok.Data;

@Data
public class CardContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double valor;

    private Long quantidade;

    public CardContaDTO() {
        this.valor = 0d;
        this.quantidade = 0l;
    }

    public CardContaDTO(Double valor, Long quantidade) {
        this.valor = valor;
        this.quantidade = quantidade;
    }

    public CardContaDTO(Double valorTotal, Double valorPago, Long quantidade) {
        if (valorTotal == null) {
            valorTotal = 0d;
        }
        if (valorPago == null) {
            valorPago = 0d;
        }
        this.valor = valorTotal - valorPago;
        this.quantidade = quantidade;
    }

    public Double getValor() {
        return NumeroUtil.somar(0d, valor);
    }

    public Long getQuantidade() {
        if (quantidade != null) {
            return quantidade;
        }

        return 0L;
    }

    public CardContaDTO sum(CardContaDTO other) {
        this.valor += other.valor;
        this.quantidade += other.quantidade;
        return this;
    }

    public CardContaDTO sub(CardContaDTO other) {
        this.valor -= other.valor;
        this.quantidade += other.quantidade;
        return this;
    }

    public void add(Double valor) {
        this.valor += valor;
        ++this.quantidade;
    }

    public void remove(Double valor) {
        this.valor -= valor;
        ++this.quantidade;
    }

}
