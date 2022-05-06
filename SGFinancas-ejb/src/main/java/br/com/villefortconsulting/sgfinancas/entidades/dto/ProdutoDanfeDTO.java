package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDanfeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Produto
    private Integer prodCodigo;

    private String prodDescricao;

    private Integer prodNCM;

    private String prodCFOP;

    private Integer prodUnidade;

    private Double prodQuantidade;

    private Double prodValor;

    private Double prodTotal;

    private Double prodCST;

    private Double prodCSOSN;

    private Double prodBC;

    private Double prodAliqICMS;

    private Double prodAliqIPI;

    private Double prodValorICMS;

    private Double prodValorIPI;

}
