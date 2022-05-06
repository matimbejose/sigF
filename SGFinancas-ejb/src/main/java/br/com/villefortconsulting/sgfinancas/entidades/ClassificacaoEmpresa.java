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
@Table(name = "CLASSIFICACAO_EMPRESA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ClassificacaoEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe uma empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idEmpresa;

    @NotNull(message = "Informe uma classificação")
    @JoinColumn(name = "ID_CLASSIFICACAO", referencedColumnName = "ID")
    @ManyToOne
    private Classificacao idClassificacao;

    public ClassificacaoEmpresa(Empresa idEmpresa, Classificacao idClassificacao) {
        this.idEmpresa = idEmpresa;
        this.idClassificacao = idClassificacao;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.idEmpresa);
        return 89 * hash + Objects.hashCode(this.idClassificacao);
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
        final ClassificacaoEmpresa other = (ClassificacaoEmpresa) obj;
        if (!Objects.equals(this.idEmpresa, other.idEmpresa)) {
            return false;
        }
        return Objects.equals(this.idClassificacao, other.idClassificacao);
    }

    @Override
    public String toString() {
        return "ClassificacaoEmpresa{" + "id=" + id + '}';
    }

}
