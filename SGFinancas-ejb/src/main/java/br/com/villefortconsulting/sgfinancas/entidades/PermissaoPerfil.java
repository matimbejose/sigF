package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
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
@Table(name = "PERMISSAO_PERFIL")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class PermissaoPerfil extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a permissão.")
    @JoinColumn(name = "ID_PERMISSAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Permissao idPermissao;

    @NotNull(message = "Informe o perfil.")
    @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Perfil idPerfil;

    public PermissaoPerfil(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PermissaoPerfil{" + "id=" + id + '}';
    }

}
