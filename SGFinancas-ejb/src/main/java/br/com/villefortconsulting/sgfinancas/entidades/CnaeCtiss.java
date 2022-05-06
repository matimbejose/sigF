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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CNAE_CTISS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class CnaeCtiss extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CNAE", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Cnae idCnae;

    @JoinColumn(name = "ID_CTISS", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Ctiss idCtiss;

    @Size(max = 9, message = "Código CTISS deve possuir no máximo 9 caracteres")
    @Column(name = "CODIGO_CNAE")
    private String codigoCnae;

    @Size(max = 12, message = "Código CTISS deve possuir no máximo 12 caracteres")
    @Column(name = "CODIGO_CTISS")
    private String codigoCtiss;

    @Override
    public String toString() {
        return "CnaeCtiss{" + "id=" + id + '}';
    }

}
