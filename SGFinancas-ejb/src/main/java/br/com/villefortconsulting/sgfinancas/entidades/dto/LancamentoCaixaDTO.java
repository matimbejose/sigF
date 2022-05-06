package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoCaixaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    //Produtos mais vendidos
    String descricaoProduto;

    Date data;

    Double valor;

}
