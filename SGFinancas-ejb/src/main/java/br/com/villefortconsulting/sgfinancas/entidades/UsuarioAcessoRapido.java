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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "USUARIO_ACESSO_RAPIDO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioAcessoRapido extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuario;

    @JoinColumn(name = "ID_ACESSO_RAPIDO", referencedColumnName = "ID")
    @ManyToOne
    private AcessoRapido idAcessoRapido;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idUsuario);
        return 79 * hash + Objects.hashCode(this.idAcessoRapido);
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
        final UsuarioAcessoRapido other = (UsuarioAcessoRapido) obj;
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        return Objects.equals(this.idAcessoRapido, other.idAcessoRapido);
    }

    @Override
    public String toString() {
        return "UsuarioAcessoRapido{" + "id=" + id + '}';
    }

}
