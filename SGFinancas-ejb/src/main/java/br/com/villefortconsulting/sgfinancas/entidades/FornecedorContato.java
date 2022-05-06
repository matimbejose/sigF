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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FORNECEDOR_CONTATO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FornecedorContato extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nome")
    @Size(max = 200, message = "Nome deverá ter entre 200 caracteres.")
    @Column(name = "NOME")
    private String nome;

    @Column(name = "CARGO")
    @Size(max = 50, message = "Cargo deverá ter entre 50 caracteres.")
    private String cargo;

    @Column(name = "EMAIL")
    @Size(max = 50, message = "Email deverá ter entre 50 caracteres.")
    private String email;

    @Column(name = "TELEFONE")
    @Size(max = 50, message = "Telefone deverá ter entre 50 caracteres.")
    private String telefone;

    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Fornecedor idFornecedor;

    @Transient
    private String controle;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FornecedorContato)) {
            return false;
        }
        FornecedorContato other = (FornecedorContato) object;
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
        return 83 * hash + Objects.hashCode(this.idFornecedor);
    }

    @Override
    public String toString() {
        return "FornecedorContato{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
