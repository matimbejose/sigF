package br.com.villefortconsulting.sgfinancas.entidades.embeddable;

import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DadosProduto implements Serializable {

    private static final long serialVersionUID = 1L;

    // @NotNull(message = "Informe o produto")
    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto idProduto;

    @NotNull(message = "Informe o valor")
    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "DESCONTO")
    private Double desconto;

    @NotNull(message = "Informe a quantidade")
    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @Column(name = "DETALHES_ITEM")
    private String detalhesItem;

    @JoinColumn(name = "ID_NCM", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Ncm idNcm;

    @JoinColumn(name = "ID_CFOP", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Cfop idCfop;

    @JoinColumn(name = "ID_CSOSN", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Csosn idCsosn;

    @JoinColumn(name = "ID_CST", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Cst idCst;

    public Double getValorTotal() {
        Double valorL = NumeroUtil.somar(this.valor);
        Double quantidadeL = NumeroUtil.somar(this.quantidade);
        Double descontoL = NumeroUtil.somar(this.desconto);

        return (valorL * quantidadeL) - descontoL;
    }

}
