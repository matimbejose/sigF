package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstatisticaContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Double valorPendente;

    private Long qtdPendente;

    private Double valorEmAtraso;

    private Long qtdEmAtraso;

    private Double valorPago;

    private Long qtdPago;

    private Double valorTotal;

    private Long qtdTotal;

    public EstatisticaContaDTO(Double valorTotal, Long qtdTotal) {
        this.valorTotal = valorTotal;
        this.qtdTotal = qtdTotal;
    }

    public EstatisticaContaDTO(Double valorPendente, Double valorEmAtraso, Double valorPago, Double valorTotal) {
        this.valorPendente = valorPendente;
        this.valorEmAtraso = valorEmAtraso;
        this.valorPago = valorPago;
        this.valorTotal = valorTotal;
    }

    public Double getValorPendente() {
        return NumeroUtil.somar(0d, valorPendente);
    }

    public Double getValorEmAtraso() {
        return NumeroUtil.somar(0d, valorEmAtraso);
    }

    public Double getValorPago() {
        return NumeroUtil.somar(0d, valorPago);
    }

    public Double getValorTotal() {
        return NumeroUtil.somar(0d, valorTotal);
    }

}
