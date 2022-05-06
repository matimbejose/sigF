package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
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
@Table(name = "SINDICO_CONSELHO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SindicoConselho extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o sindico")
    @JoinColumn(name = "ID_SINDICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Sindico idSindico;

    @NotNull(message = "Informe a pessoa do conselho")
    @JoinColumn(name = "ID_PESSOA_CONSELHO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idPessoaConselho;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.idSindico);
        return 19 * hash + Objects.hashCode(this.idPessoaConselho);
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
        final SindicoConselho other = (SindicoConselho) obj;
        if (!Objects.equals(this.idSindico, other.idSindico)) {
            return false;
        }
        return Objects.equals(this.idPessoaConselho, other.idPessoaConselho);
    }

    @Override
    public String toString() {
        return "SindicoConselho{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
