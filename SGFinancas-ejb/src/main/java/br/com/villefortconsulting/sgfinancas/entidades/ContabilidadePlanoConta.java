package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
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
@Table(name = "CONTABILIDADE_PLANO_CONTA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContabilidadePlanoConta extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CONTABILIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Contabilidade idContabilidade;

    @JoinColumn(name = "ID_PLANO_CONTA_PADRAO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoContaPadrao idPlanoContaPadrao;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO")
    private String codigo;

    @Override
    public String toString() {
        return "ContabilidadePlanoConta{" + "id=" + id + '}';
    }

}
