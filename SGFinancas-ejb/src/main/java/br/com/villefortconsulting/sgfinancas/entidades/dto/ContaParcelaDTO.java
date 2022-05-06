package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaParcelaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String planoConta;

    private String cliente;

    private String fornecedor;

    private Date dataVencimento;

    private Date dataEmissao;

    private Double valorOriginal;

    private Double valorCorrigido;

    private Double valorTotal;

    private String observacao;

}
