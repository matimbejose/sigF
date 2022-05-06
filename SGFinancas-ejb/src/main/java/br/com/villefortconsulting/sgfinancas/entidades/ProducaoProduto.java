package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUCAO_PRODUTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance
@Builder
public class ProducaoProduto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PRODUCAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Producao idProducao;

    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto idProduto;

    @NotNull
    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @Size(max = 200)
    @Column(name = "DETALHES_ITEM")
    private String detalhesItem;

    @Transient
    private String controle;

    @Override
    public String toString() {
        return "ProducaoProduto{" + "id=" + id + '}';
    }

}
