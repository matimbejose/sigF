package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String numero;

    private Integer cvv;

    private Date dataValidade;

    private String nome;

    private String token;

    private Double installment;

    private String cpfTitular;

    private Date nascimentoTitular;

}
