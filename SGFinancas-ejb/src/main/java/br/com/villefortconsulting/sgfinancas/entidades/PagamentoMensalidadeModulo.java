package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PAGAMENTO_MENSALIDADE_MODULO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
@RequiredArgsConstructor
public class PagamentoMensalidadeModulo extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @NonNull
    @JoinColumn(name = "ID_PAGAMENTO_MENSALIDADE", referencedColumnName = "ID")
    @ManyToOne
    private PagamentoMensalidade idPagamentoMensalidade;

    @NotNull
    @NonNull
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private Modulo idModulo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idPagamentoMensalidadeModulo", orphanRemoval = true)
    private List<PagamentoMensalidadeModuloPermissao> pagamentoMensalidadeModuloPermissaoList = new LinkedList<>();

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.idPagamentoMensalidade);
        hash = 79 * hash + Objects.hashCode(this.idModulo);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final PagamentoMensalidadeModulo other = (PagamentoMensalidadeModulo) obj;
        return Objects.equals(this.idPagamentoMensalidade, other.idPagamentoMensalidade) && Objects.equals(this.idModulo, other.idModulo);
    }

}
