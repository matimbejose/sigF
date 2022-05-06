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
import javax.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "MODULO_PERMISSAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ModuloPermissao extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID")
    @ManyToOne
    private Permissao idPermissao;

    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private Modulo idModulo;

    @Transient
    private boolean nova = false;

    public ModuloPermissao(Modulo modulo) {
        this.idModulo = modulo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ModuloPermissao)) {
            return false;
        }
        ModuloPermissao mp = (ModuloPermissao) o;
        return mp.idModulo.equals(this.idModulo) && mp.idPermissao.equals(this.idPermissao);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.idPermissao);
        hash = 23 * hash + Objects.hashCode(this.idModulo);
        return hash;
    }

}
