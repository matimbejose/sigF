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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TRANSFERENCIA_CONTA_BANCARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class TransferenciaContaBancaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @NotNull(message = "Informe o valor.")
    @Column(name = "VALOR")
    private Double valor;

    @NotNull(message = "Associe a uma parcela")
    @JoinColumn(name = "ID_PARCELA_ORIGEM", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idParcelaOrigem;

    @NotNull(message = "Associe a uma parcela")
    @JoinColumn(name = "ID_PARCELA_DESTINO", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idParcelaDestino;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @Override
    public String toString() {
        return "PrevisaoOrcamentaria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
