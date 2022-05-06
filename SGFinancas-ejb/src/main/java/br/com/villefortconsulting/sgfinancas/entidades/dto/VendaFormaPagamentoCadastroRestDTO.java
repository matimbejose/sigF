package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaFormaPagamentoCadastroRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer formaPagamento;

    private String condicaoPagamento;

    private Double desconto;

    private String bandeiraCartao;

}
