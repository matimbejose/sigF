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
@Table(name = "PLANO_CONTA_LANCAMENTO_CONTABIL")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PlanoContaLancamentoContabil extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CONTA_CREDITO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaCredito;

    @JoinColumn(name = "ID_CONTA_DEBITO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaDebito;

    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private Conta idConta;

    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @NotNull(message = "Informe a data")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA")
    private Date data;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Favor informar uma situação com no máximo 1 caractere")
    private String situacao;

    @NotNull(message = "Informe um valor")
    @Column(name = "VALOR")
    private Double valor;

    @Override
    public String toString() {
        return "PlanoContaLancamentoContabil{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
