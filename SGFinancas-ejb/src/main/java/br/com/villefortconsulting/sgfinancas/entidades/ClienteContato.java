package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CLIENTE_CONTATO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ClienteContato extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nome")
    @Column(name = "NOME")
    private String nome;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "TELEFONE")
    private String telefone;

    @Column(name = "CARGO")
    private String cargo;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @Transient
    private String controle;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ClienteContato)) {
            return false;
        }
        ClienteContato other = (ClienteContato) object;
        if (this.controle != null) {
            return this.controle.equalsIgnoreCase(other.controle);
        }
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return super.hashCode();
        }
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.nome);
        hash = 83 * hash + Objects.hashCode(this.cargo);
        hash = 83 * hash + Objects.hashCode(this.email);
        hash = 83 * hash + Objects.hashCode(this.telefone);
        return 83 * hash + Objects.hashCode(this.idCliente);
    }

    @Override
    public String toString() {
        return "ClienteContato{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
