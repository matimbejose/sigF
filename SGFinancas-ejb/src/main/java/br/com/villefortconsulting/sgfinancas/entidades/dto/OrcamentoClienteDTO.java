package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.VendaItemOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Getter
public class OrcamentoClienteDTO implements Serializable, Comparable<OrcamentoClienteDTO> {

    private static final long serialVersionUID = 1L;

    private final String descricao;

    private final String categoria;

    private final String obsItem;

    private final Double quantidade;

    private final Double valorLiquido;

    private final Double desconto;

    private final Double valorTotal;

    private final String tipo;

    private final Boolean ativo;

    public OrcamentoClienteDTO(VendaProduto vendaProduto) {
        this.descricao = vendaProduto.getDadosProduto().getIdProduto().getDescricao();
        this.quantidade = vendaProduto.getDadosProduto().getQuantidade();
        this.valorLiquido = vendaProduto.getDadosProduto().getValor();
        this.desconto = vendaProduto.getDadosProduto().getDesconto();
        this.valorTotal = vendaProduto.getDadosProduto().getValorTotal();
        this.obsItem = vendaProduto.getDadosProduto().getDetalhesItem();
        this.tipo = "Produtos";
        this.ativo = true;
        ProdutoCategoria pc = vendaProduto.getDadosProduto().getIdProduto().getIdProdutoCategoria();
        this.categoria = pc != null ? pc.getDescricao() : "Sem categoria";
    }

    public OrcamentoClienteDTO(VendaServico vendaServico) {
        this.descricao = vendaServico.getIdServico().getDescricao();
        this.quantidade = vendaServico.getQuantidade() * 1d;
        this.valorLiquido = vendaServico.getValorVenda();
        this.desconto = vendaServico.getDesconto();
        this.valorTotal = vendaServico.getValorTotal();
        this.obsItem = vendaServico.getDetalhesItem();
        this.tipo = "Serviços";
        this.ativo = true;
        ProdutoCategoria pc = vendaServico.getIdServico().getIdProdutoCategoria();
        this.categoria = pc != null ? pc.getDescricao() : "Sem categoria";
    }

    public OrcamentoClienteDTO(VendaItemOrdemDeServico vendaItemOrdemServico) {
        this.descricao = vendaItemOrdemServico.getNomeItem();
        this.quantidade = 1d;
        this.valorLiquido = vendaItemOrdemServico.getValor();
        this.desconto = vendaItemOrdemServico.getDesconto();
        this.valorTotal = vendaItemOrdemServico.getValorTotal();
        this.obsItem = vendaItemOrdemServico.getObservacao();
        this.tipo = "Itens de OS";
        this.ativo = true;
        this.categoria = null;
    }

    public OrcamentoClienteDTO(String tipo) {
        this.descricao = "";
        this.quantidade = 0d;
        this.valorLiquido = 0d;
        this.desconto = 0d;
        this.valorTotal = 0d;
        this.tipo = tipo;
        this.ativo = false;
        this.obsItem = "";
        this.categoria = null;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.descricao);
        hash = 97 * hash + Objects.hashCode(this.quantidade);
        hash = 97 * hash + Objects.hashCode(this.valorLiquido);
        hash = 97 * hash + Objects.hashCode(this.desconto);
        hash = 97 * hash + Objects.hashCode(this.valorTotal);
        hash = 97 * hash + Objects.hashCode(this.tipo);
        hash = 97 * hash + Objects.hashCode(this.ativo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OrcamentoClienteDTO other = (OrcamentoClienteDTO) obj;
        if (!Objects.equals(this.descricao, other.descricao)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.quantidade, other.quantidade)) {
            return false;
        }
        if (!Objects.equals(this.valorLiquido, other.valorLiquido)) {
            return false;
        }
        if (!Objects.equals(this.desconto, other.desconto)) {
            return false;
        }
        if (!Objects.equals(this.valorTotal, other.valorTotal)) {
            return false;
        }
        return Objects.equals(this.ativo, other.ativo);
    }

    @Override
    public int compareTo(OrcamentoClienteDTO o) {
        return this.tipo.compareTo(o.tipo);
    }

}
