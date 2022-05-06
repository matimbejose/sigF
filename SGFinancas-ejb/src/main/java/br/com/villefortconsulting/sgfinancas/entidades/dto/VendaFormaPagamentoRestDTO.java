package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaFormaPagamentoRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private FormaPagamentoDTO formaPagamento;

    private String condicaoPagamento;

    private Double desconto;

    private boolean formaEscolhida;

    private String bandeiraCartao;

}
