package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "LOGIN_ACESSO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Builder
@Inheritance
@NoArgsConstructor
@AllArgsConstructor
public class LoginAcesso extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o usuário")
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @NotNull(message = "Informe a empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    @Column(name = "DATA_ACESSO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAcesso;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idUsuario);
        return 97 * hash + Objects.hashCode(this.idEmpresa);
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
        final LoginAcesso other = (LoginAcesso) obj;
        if (!Objects.equals(this.idUsuario, other.idUsuario)) {
            return false;
        }
        return Objects.equals(this.idEmpresa, other.idEmpresa);
    }

    @Override
    public String toString() {
        return "LoginAcesso{" + "id=" + id + '}';
    }

}
