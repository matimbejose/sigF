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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FUNCIONARIO_FERIAS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class FuncionarioFerias extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o funcionário")
    @JoinColumn(name = "ID_FUNCIONARIO", referencedColumnName = "ID")
    @ManyToOne
    private Funcionario idFuncionario;

    @NotNull(message = "Informe a data início")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;

    @NotNull(message = "Informe a data fim")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_FIM")
    private Date dataFim;

    @Column(name = "OBSERVACAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para observação ")
    private String observacao;

    @NotNull(message = "Informe o motivo do abono")
    @Column(name = "MOTIVO")
    @Size(max = 1, message = "Máximo de 1 caracter para motivo do abono ")
    private String motivo;

    @Override
    public String toString() {
        return "FuncionarioFerias(" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
