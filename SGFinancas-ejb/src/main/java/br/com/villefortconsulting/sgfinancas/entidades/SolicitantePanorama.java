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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITANTE_PANORAMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SolicitantePanorama extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o solicitante")
    @JoinColumn(name = "ID_SOLICITANTE", referencedColumnName = "ID")
    @ManyToOne
    private Solicitante idSolicitante;

    @NotNull(message = "Informe o status")
    @Column(name = "STATUS")
    @Size(max = 1, message = "Você precisa especificar uma situação com no máximo 1 caracter")
    private String status;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @Override
    public String toString() {
        return "SolicitantePanorama{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
