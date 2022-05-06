package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoItemVenda;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoServicoDTO implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String idProdutoServico;

    private String descricao;

    private Double valor;

    private Double custo;

    private EnumTipoItemVenda tipo;

    private String tipoProduto;

    private String foto;

    private String codigo;

    public ProdutoServicoDTO(Produto prod) {
        this.idProdutoServico = "P-" + prod.getId();
        this.descricao = prod.getDescricao();
        this.valor = NumeroUtil.somar(0d, prod.getValorVenda());
        this.tipo = EnumTipoItemVenda.PRODUTO;
        this.tipoProduto = prod.getComposto();
        this.codigo = prod.getCodigo();
    }

    public ProdutoServicoDTO(Servico serv) {
        this.idProdutoServico = "S-" + serv.getId();
        this.descricao = serv.getDescricao();
        this.valor = NumeroUtil.somar(0d, serv.getValorVenda());
        this.tipo = EnumTipoItemVenda.SERVICO;
        this.tipoProduto = "N";
        this.codigo = "";
    }

    public ProdutoServicoDTO(String idProduto, String descricao, Double valor, EnumTipoItemVenda tipo, String tipoProduto, String codigo) {
        this.idProdutoServico = idProduto;
        this.descricao = descricao;
        this.valor = valor;
        this.tipo = tipo;
        this.tipoProduto = tipoProduto;
        this.codigo = codigo;
    }

    public ProdutoServicoDTO(PagamentoMensalidadeModulo pmm) {
        this.idProdutoServico = "Módulo " + pmm.getIdModulo().getNome();
        this.descricao = this.idProdutoServico;
        this.valor = pmm.getIdModulo().getValorMensalidade();
        this.tipo = EnumTipoItemVenda.PRODUTO;
        this.tipoProduto = "N";
        this.codigo = "";
    }

    public void setIdProdutoServico(String idProdutoServico) {
        this.idProdutoServico = idProdutoServico;
        if (idProdutoServico == null) {
            return;
        }
        if (idProdutoServico.startsWith("P-")) {
            this.tipo = EnumTipoItemVenda.PRODUTO;
        } else if (idProdutoServico.startsWith("S-")) {
            this.tipo = EnumTipoItemVenda.SERVICO;
        }
    }

    @Override
    public ProdutoServicoDTO clone() {
        try {
            return (ProdutoServicoDTO) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new ProdutoServicoDTO();
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ProdutoServicoDTO) {
            return ((ProdutoServicoDTO) other).getIdProdutoServico().equals(this.idProdutoServico);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return 59 * hash + Objects.hashCode(this.idProdutoServico);
    }

}
