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
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "MORADOR_UNIDADE_OCUPACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class MoradorUnidadeOcupacao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a unidade")
    @JoinColumn(name = "ID_UNIDADE_OCUPACAO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private UnidadeOcupacao idUnidadeOcupacao;

    @NotNull(message = "Informe o morador")
    @JoinColumn(name = "ID_MORADOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idMorador;

    @NotNull(message = "Informe se o morador é responsável pela unidade")
    @Column(name = "RESPONSAVEL")
    private String responsavel;

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.idUnidadeOcupacao);
        return 37 * hash + Objects.hashCode(this.idMorador);
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
        final MoradorUnidadeOcupacao other = (MoradorUnidadeOcupacao) obj;
        if (!Objects.equals(this.idUnidadeOcupacao, other.idUnidadeOcupacao)) {
            return false;
        }
        return Objects.equals(this.idMorador, other.idMorador);
    }

    @Override
    public String toString() {
        return "UnidadeMorador{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
