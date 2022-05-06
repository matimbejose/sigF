package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaProdutoRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private ProdutoDTO produto;

    private Double quantidade;

    private Double valorVenda;

    private String detalhesItem;

    private Double desconto;

    private Double pontos;

    private Integer ncm;

    private Integer cfop;

    private Integer cst;

    private Integer csosn;

}
