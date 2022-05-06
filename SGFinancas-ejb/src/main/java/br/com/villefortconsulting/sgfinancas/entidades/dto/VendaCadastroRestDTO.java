package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaCadastroRestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String tipo;

    private Integer cliente;

    private String telefoneCliente;

    private boolean enviarLinkPagamento;

    // private Integer usuarioVendedor;// Vai ser o próprio usuário logado
    private Date dataVencimento;

    private Date dataVenda;

    private Date dataCancelamento;

    private Integer formaPagamento;

    private Double valor;

    private Double valorFrete;

    private Double valorDesconto;

    private String situacaoSigla;

    private Double desconto;

    private String condicaoPagamento;// Campo forma_pagamento (V, 1x, 2x, 3x, ...)

    private String observacao;

    private String observacaoCliente;

    private Integer contaBancaria;

    private Integer planoConta;

    private Integer conta;

    private Integer centroCusto;

    private List<VendaProdutoCadastroRestDTO> produtos;

    private List<VendaServicoCadastroRestDTO> servicos;

    private List<VendaItemOrdemServicoRestDTO> itensOS;

    private List<VendaFormaPagamentoCadastroRestDTO> formasPagamento;

    private VendaSeguradoraDTO vendaSeguradora;

}
