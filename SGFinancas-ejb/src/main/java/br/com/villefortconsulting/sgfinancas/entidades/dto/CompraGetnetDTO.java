package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraGetnetDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idCompra;

    private Double imposto;

    private String tipoProduto;

}
