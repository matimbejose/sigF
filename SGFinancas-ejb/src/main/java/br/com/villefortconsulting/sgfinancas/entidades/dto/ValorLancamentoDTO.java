package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValorLancamentoDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Double valor;

    private Integer ano;

    private Integer mes;

    private String codigo;

    public ValorLancamentoDTO(Integer id, Double valor) {
        this.id = id;
        this.valor = valor;
    }

    public ValorLancamentoDTO(Integer ano, Integer mes, Double valor) {
        this.valor = valor;
        this.ano = ano;
        this.mes = mes;
    }

    public ValorLancamentoDTO(Integer ano, Integer mes, Double valor, String codigo) {
        this.valor = valor;
        this.ano = ano;
        this.mes = mes;
        this.codigo = codigo;
    }

    public ValorLancamentoDTO(Double valor, Integer mes) {
        this.valor = valor;
        this.mes = mes;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0d;
        }
        return valor;
    }

    @Override
    public ValorLancamentoDTO clone() {
        try {
            return (ValorLancamentoDTO) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new ValorLancamentoDTO();
        }
    }

}
