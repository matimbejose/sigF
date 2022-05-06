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
@Table(name = "USUARIO_GRUPO_EMPRESA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class UsuarioGrupoEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o usuário")
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @NotNull(message = "Informe o grupo empresa")
    @JoinColumn(name = "ID_GRUPO_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private GrupoEmpresa idGrupoEmpresa;

    public UsuarioGrupoEmpresa(Usuario idUsuario, GrupoEmpresa idGrupoEmpresa) {
        this.idUsuario = idUsuario;
        this.idGrupoEmpresa = idGrupoEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.idUsuario);
        return 23 * hash + Objects.hashCode(this.idGrupoEmpresa);
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
        final UsuarioGrupoEmpresa other = (UsuarioGrupoEmpresa) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        return Objects.equals(this.idGrupoEmpresa, other.idGrupoEmpresa);
    }

    @Override
    public String toString() {
        return "UsuarioGrupoEmpresa{" + "id=" + id + '}';
    }

}
