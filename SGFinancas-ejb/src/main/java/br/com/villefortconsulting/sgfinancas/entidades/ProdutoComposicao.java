package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUTO_COMPOSICAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ProdutoComposicao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PRODUTO_ORIGEM", referencedColumnName = "ID")
    @ManyToOne
    private Produto idProdutoOrigem;

    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne
    private Produto idProduto;

    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @Column(name = "CUSTO")
    private Double custo;

    @Column(name = "PRECO")
    private Double preco;

    @Transient
    private Date dataProducao;

    @Transient
    private Double quantidadeFinal;

    public ProdutoComposicao() {
        quantidade = 1d;
        custo = 0d;
        preco = 0d;
    }

    @Override
    public String toString() {
        return "ProdutoComposicao{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    @PrePersist
    public void prePersist() {
        tenatID = idProdutoOrigem.getTenatID();
    }

}
