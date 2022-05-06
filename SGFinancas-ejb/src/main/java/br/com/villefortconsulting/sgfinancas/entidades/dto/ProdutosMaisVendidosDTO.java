package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutosMaisVendidosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Produtos mais vendidos
    String descricaoProduto;

    String unidadeMedida;

    Double quantidade;

    Double valor;

}
