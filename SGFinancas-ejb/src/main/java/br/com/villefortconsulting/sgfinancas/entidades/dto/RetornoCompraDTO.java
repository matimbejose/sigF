/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetornoCompraDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Status status;

    private Integer idCompra;

    private List<CompraProdutoDTO> produtos;

    public enum Status {
        SALVO, PRODUTO_PENDENTE

    }

}
