package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import java.io.Serializable;
import lombok.Data;

@Data
public class ParametroProdutoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private EnumTipoProdutoVenda[] tiposVenda;

    private EnumTipoProdutoVenda[] tiposCompra;

    private boolean permiteVendaComEstoqueNegativo;

}
