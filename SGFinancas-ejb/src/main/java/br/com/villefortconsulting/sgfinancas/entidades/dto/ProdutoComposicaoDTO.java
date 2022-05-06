package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoComposicaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer idProduto;

    private Double quantidade;

    private Double custo;

    private Double preco;

}
