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
@Table(name = "CONTRATO_AJUSTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContratoAjuste extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private Contrato idContrato;

    @Size(max = 1, message = "Tipo de contrato deve possuir no máximo 1 caracter")
    @Column(name = "TIPO_CONTRATO")
    private String tipoContrato;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Column(name = "OBSERVACAO")
    @Size(max = 500, message = "Máximo de 500 caracteres para a observação.")
    private String observacao;

    @NotNull(message = "Favor informar a taxa")
    @Column(name = "TAXA")
    private Double taxa;

    @NotNull(message = "Favor informar O número de parcelas alteradas")
    @Column(name = "NUM_PARCELAS")
    private int numParcelas;

    @Override
    public String toString() {
        return "ContratoAjuste{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
