package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PONTO_OBSERVACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PontoObservacao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o funcionário")
    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne
    private Funcionario idFuncionario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REFERENCIA")
    private Date referencia;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @Override
    public String toString() {
        return "PontoObservacao(" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
