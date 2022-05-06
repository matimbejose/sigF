package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrcamentoDTO implements Serializable, Comparable<OrcamentoDTO> {

    private static final long serialVersionUID = 1L;

    private int numeroLinha;

    private String nome;

    private String observacao;

    private Double quantidade;

    private Double valorUnitario;

    private Double desconto;

    private Double valorTotal;

    private String tipo;

    private String unidade;

    public OrcamentoDTO(String nome, String observacao, Double quantidade, Double valorUnitario, Double desconto, Double valorTotal, String tipo) {
        this.nome = nome;
        this.observacao = observacao;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
        this.desconto = desconto;
        this.valorTotal = valorTotal;
        this.tipo = tipo;
    }

    public OrcamentoDTO(VendaProduto vendaProduto) {
        this.nome = vendaProduto.getDadosProduto().getIdProduto().getDescricao();
        this.quantidade = vendaProduto.getDadosProduto().getQuantidade();
        this.valorUnitario = vendaProduto.getDadosProduto().getValor();
        this.desconto = vendaProduto.getDadosProduto().getDesconto();
        if (this.desconto == null) {
            this.desconto = 0d;
        }
        this.valorTotal = this.valorUnitario * this.quantidade - this.desconto;
        this.tipo = "P";
        this.unidade = vendaProduto.getDadosProduto().getIdProduto().getIdUnidadeMedida().getDescricao();
    }

    public OrcamentoDTO(VendaServico vendaServico) {
        this.nome = vendaServico.getIdServico().getDescricao();
        this.quantidade = vendaServico.getQuantidade().doubleValue();
        this.valorUnitario = vendaServico.getValorVenda();
        this.desconto = vendaServico.getDesconto();
        if (this.desconto == null) {
            this.desconto = 0d;
        }
        this.valorTotal = this.valorUnitario * this.quantidade - this.desconto;
        this.tipo = "S";
    }

    public OrcamentoDTO(VendaItemOrdemDeServico vendaOS) {
        this.nome = vendaOS.getNomeItem();
        this.observacao = vendaOS.getObservacao();
        this.quantidade = 1d;
        this.valorUnitario = vendaOS.getValor();
        this.desconto = vendaOS.getDesconto();
        if (this.desconto == null) {
            this.desconto = 0d;
        }
        this.valorTotal = this.valorUnitario * this.quantidade - this.desconto;
        this.tipo = "O";
    }

    @Override
    public int compareTo(OrcamentoDTO that) {
        return this.tipo.compareTo(that.tipo);
    }

}
