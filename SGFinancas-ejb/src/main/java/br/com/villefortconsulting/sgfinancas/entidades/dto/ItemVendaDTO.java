package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ItemVendaDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private ProdutoServicoDTO idProdutoServico;

    private Integer idVendaProduto;

    private Integer idVendaServico;

    private Double valor;

    private Double quantidade = 1d;

    private Double desconto = 0d;

    private String observacao;

    private String controle;

    private String fornecidoTerceiro;

    public ItemVendaDTO(ProdutoServicoDTO idProduto, Double valor) {
        this.idProdutoServico = idProduto;
        this.valor = valor;
    }

    public ItemVendaDTO(Integer id, ProdutoServicoDTO idProduto, Double valor, Double quantidade, Double desconto, String observacao, String fornecidoTerceiro) {
        this.idProdutoServico = idProduto;
        this.valor = valor;
        this.quantidade = quantidade;
        this.desconto = NumeroUtil.somar(desconto);
        this.controle = StringUtil.gerarStringAleatoria(8);
        this.observacao = observacao;
        if (idProduto.getTipo() == EnumTipoItemVenda.PRODUTO) {
            idVendaProduto = id;
        } else if (idProduto.getTipo() == EnumTipoItemVenda.SERVICO) {
            idVendaServico = id;
        }
        this.fornecidoTerceiro = fornecidoTerceiro;
    }

    public Double getValorTotal() {
        if (valor == null) {
            return 0d;
        }
        return valor * quantidade - desconto;
    }

    public void setValor(Double d) {
        if ("S".equals(fornecidoTerceiro)) {
            valor = 0d;
        } else {
            valor = d;
        }
    }

    public void setFornecidoTerceiro(String s) {
        fornecidoTerceiro = s;
        if ("S".equals(s)) {
            valor = 0d;
        }
    }

    public String getFornecidoTerceiro() {
        return fornecidoTerceiro != null ? fornecidoTerceiro : "N";
    }

    @Override
    public ItemVendaDTO clone() {
        try {
            return (ItemVendaDTO) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new ItemVendaDTO();
        }
    }

}
