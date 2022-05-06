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
@Table(name = "PREVISAO_ORCAMENTARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PrevisaoOrcamentaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @NotNull(message = "Informe a sua previsão")
    @Column(name = "PREVISAO")
    private Double previsao;

    @NotNull(message = "Associe a um plano de conta")
    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoConta;
    
    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @Override
    public String toString() {
        return "PrevisaoOrcamentaria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    public PrevisaoOrcamentaria(Date data, Double previsao, PlanoConta idPlanoConta) {
        this.data = data;
        this.previsao = previsao;
        this.idPlanoConta = idPlanoConta;
    }
    
    public PrevisaoOrcamentaria(Date data, Double previsao, PlanoConta idPlanoConta, CentroCusto idCentroCusto) {
        this.data = data;
        this.previsao = previsao;
        this.idPlanoConta = idPlanoConta;
        this.idCentroCusto = idCentroCusto;
    }
    
    public PrevisaoOrcamentaria() {
    }
    

}
