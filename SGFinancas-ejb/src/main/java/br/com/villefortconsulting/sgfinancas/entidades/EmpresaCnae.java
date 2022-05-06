package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "EMPRESA_CNAE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class EmpresaCnae extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe ao menos um cnae")
    @JoinColumn(name = "ID_CNAE", referencedColumnName = "ID")
    @ManyToOne
    private Cnae idCnae;

    @NotNull(message = "informe ao menos uma empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idEmpresa;

    public EmpresaCnae(Cnae idCnae, Empresa idEmpresa) {
        this.idCnae = idCnae;
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.idCnae);
        return 67 * hash + Objects.hashCode(this.idEmpresa);
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
        final EmpresaCnae other = (EmpresaCnae) obj;
        if (!Objects.equals(this.idCnae, other.idCnae)) {
            return false;
        }
        return Objects.equals(this.idEmpresa, other.idEmpresa);
    }

    @Override
    public String toString() {
        return "EmpresaCnae{" + "id=" + id + '}';
    }

}
