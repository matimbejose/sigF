package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "VENDA_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance
public class VendaServico extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a venda")
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @NotNull(message = "Informe o produto")
    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Servico idServico;

    @Column(name = "CUSTO")
    private Double custo;

    @NotNull(message = "Informe o valor da venda")
    @Column(name = "VALOR_VENDA")
    private Double valorVenda;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "PONTOS")
    private Double pontos;

    @Column(name = "DETALHES_ITEM")
    private String detalhesItem;

    @Column(name = "QUANTIDADE")
    private Integer quantidade;

    @Column(name = "FORNECIDO_TERCEIRO")
    private String fornecidoTerceiro;

    @Transient
    private String controle;

    public VendaServico(Integer id) {
        this.id = id;
    }

    public Double getValorTotal() {
        Double valorL = NumeroUtil.somar(this.valorVenda);
        Integer quantidadeL = this.quantidade == null ? 1 : this.quantidade;
        Double descontoL = NumeroUtil.somar(this.desconto);

        return (valorL * quantidadeL) - descontoL;
    }

    @Override
    public VendaServico clone() {
        try {
            VendaServico novo = (VendaServico) super.clone();
            novo.setId(null);
            novo.setIdVenda(null);
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new VendaServico();
        }
    }

    @Override
    public String toString() {
        return "ValorProduto{" + "id=" + id + '}';
    }

    public static Optional<VendaServico> contains(List<VendaServico> list, VendaServico item) {
        return list.stream()
                .filter(vs -> vs.getIdVenda().equals(item.getIdVenda()) && vs.getIdServico().equals(item.getIdServico()))
                .findAny();
    }

    public boolean checkFornecidoTerceiro() {
        return "N".equals(fornecidoTerceiro);
    }

}
