package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FORNECEDOR_PRODUTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FornecedorProduto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe se está ativo")
    @Column(name = "ATIVO")
    private String ativo;

    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Fornecedor idFornecedor;

    @JoinColumn(name = "ID_PRODUTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Produto idProduto;

    public FornecedorProduto() {
        super();
        this.ativo = "S";
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FornecedorProduto)) {
            return false;
        }
        FornecedorProduto other = (FornecedorProduto) object;

        if (this.idProduto == null || other.idProduto == null || this.idFornecedor == null || other.idFornecedor == null) {
            return false;
        }

        return this.idProduto.equals(other.idProduto) && this.idFornecedor.equals(other.idFornecedor);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return super.hashCode();
        }
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.idFornecedor);
        return 71 * hash + Objects.hashCode(this.idProduto);
    }

    @Override
    public String toString() {
        return "FornecedorProduto{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
