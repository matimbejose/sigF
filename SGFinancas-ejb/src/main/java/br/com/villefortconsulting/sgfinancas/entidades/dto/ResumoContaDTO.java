package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ResumoContaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private EstatisticaContaDTO pagarAtraso;

    private EstatisticaContaDTO receberAtraso;

    private List<FluxoCaixaDTO> fluxoDeCaixa;

    private List<ContaPagarReceberDTO> necessidadeDeCaixa;

    private List<VendaDTO> vendas;

    private EstatisticaContaDTO pagarHoje;

    private EstatisticaContaDTO receberHoje;

    private EstatisticaContaDTO pagar;

    private EstatisticaContaDTO receber;

}
