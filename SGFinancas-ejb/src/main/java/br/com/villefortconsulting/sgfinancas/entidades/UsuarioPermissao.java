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
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "USUARIO_PERMISSAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@XmlRootElement
@NoArgsConstructor
@Inheritance
public class UsuarioPermissao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Permissao idPermissao;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    public UsuarioPermissao(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof UsuarioPermissao)) {
            return false;
        }
        UsuarioPermissao other = (UsuarioPermissao) object;
        return (this.idUsuario != null && this.idPermissao != null && other.idUsuario != null && other.idPermissao != null) && this.idUsuario.equals(other.idUsuario) && this.idPermissao.equals(other.idPermissao);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return super.hashCode();
        }
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idPermissao);
        return 79 * hash + Objects.hashCode(this.idUsuario);
    }

    @Override
    public String toString() {
        return "UsuarioPermissao{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
