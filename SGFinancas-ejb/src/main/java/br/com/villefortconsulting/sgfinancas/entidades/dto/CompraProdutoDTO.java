package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private ProdutoDTO produto;

    private Double quantidade;

    private Double valorVenda;

    private String detalhesItem;

    private Double desconto;

    private Double controle;

}
