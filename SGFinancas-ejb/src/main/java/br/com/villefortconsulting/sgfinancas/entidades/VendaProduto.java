package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.AttributeOverride;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "VENDA_PRODUTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance
public class VendaProduto extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a venda")
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @Embedded
    @AttributeOverride(name = "valor", column = @Column(name = "VALOR_VENDA"))
    private DadosProduto dadosProduto = new DadosProduto();

    @Column(name = "PONTOS")
    private Double pontos;

    @Transient
    private String controle;

    @Column(name = "FORNECIDO_TERCEIRO")
    private String fornecidoTerceiro;

    @OneToOne(mappedBy = "idVendaProduto", cascade = CascadeType.ALL)
    private Estoque estoque;

    public VendaProduto(Integer id) {
        this.id = id;
    }

    public boolean checkFornecidoTerceiro() {
        return !"S".equals(fornecidoTerceiro);
    }

    @Override
    public VendaProduto clone() {
        try {
            VendaProduto novo = (VendaProduto) super.clone();
            novo.setId(null);
            novo.setIdVenda(null);
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new VendaProduto();
        }
    }

    @Override
    public String toString() {
        return "ValorProduto{" + "id=" + id + '}';
    }

    public static Optional<VendaProduto> contains(List<VendaProduto> list, VendaProduto item) {
        return list.stream()
                .filter(vp -> vp.getIdVenda().equals(item.getIdVenda()) && vp.getDadosProduto().getIdProduto().equals(item.getDadosProduto().getIdProduto()))
                .findAny();
    }

}
