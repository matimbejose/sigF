package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoNfDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Produto
    private String produtoDescricao;

    private Double produtoQnt;

    private String produtoUniComercial;

    private Double produtoValor;

    private String produtoCodigo;

    private String produtoNCM;

    private String produtoCest;

    private String produtoCFOP;

    private Double produtoValorDesconto;

    private Double produtoValorFrete;

    private String produtoIndicadorCompValorNFE;

    private String produtoCodigoEANComercial;

    private Double produtoValorUnitarioTributacao;

    private String produtoNumPedidoCompra;

    private String produtoItemPedidoCompra;

    private String produtoOrigemMercadoria;

    private String produtoICMS;

    private Double produtoICMSValorBase;

    private Double produtoValorICMS;

    private Double protudoICMSValorAliquota;

    private String cst;

    private Double produtoPisValorBase;

    private Double produtoValorPis;

    private Double protudoPisValorAliquota;

    private Double produtoCofinsValorBase;

    private Double produtoValorCofins;

    private Double protudoCofinsValorAliquota;

}
