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
@Table(name = "SOLICITACAO_ADIANTAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SolicitacaoAdiantamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CREDOR", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idCredor;

    @NotNull(message = "Informe da data de inicio")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_INICIO")
    private Date dataInicio;

    @NotNull(message = "Informe da data de fim")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_FIM")
    private Date dataFim;

    @NotNull(message = "Informe o status da solicitação")
    @Column(name = "STATUS")
    private String status;

    @JoinColumn(name = "ID_CONTA_BANCARIA_ESTABELECIMENTO", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancariaEstabelecimento;

    @JoinColumn(name = "ID_CONTA_BANCARIA_CREDOR", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancariaCredor;

}
