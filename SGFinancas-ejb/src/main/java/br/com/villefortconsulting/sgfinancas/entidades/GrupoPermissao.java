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
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "GRUPO_PERMISSAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@XmlRootElement
@Inheritance
public class GrupoPermissao extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Permissao idPermissao;

    @JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Grupo idGrupo;

    public GrupoPermissao(Integer id) {
        this.id = id;
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
        final GrupoPermissao other = (GrupoPermissao) obj;
        return (this.idGrupo != null && this.idPermissao != null && other.idGrupo != null && other.idPermissao != null) && (this.idGrupo.equals(other.idGrupo) && this.idPermissao.equals(other.idPermissao));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.idPermissao);
        return 79 * hash + Objects.hashCode(this.idGrupo);
    }

    @Override
    public String toString() {
        return "GrupoPermissao{" + "id=" + id + '}';
    }

}
