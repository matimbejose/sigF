package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PLANO_PAGAMENTO_PARCELA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class PlanoPagamentoParcela extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "INTERVALO")
    private Integer intervalo;

    @Min(value = 1, message = "O menor intervalo para uma parcela é de um dia")
    @Column(name = "PORCENTAGEM")
    private BigDecimal porcentagem;

    @JoinColumn(name = "ID_PLANO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoPagamento idPlanoPagamento;

    public PlanoPagamentoParcela(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Intervalo: " + intervalo + ", porcentagem: " + porcentagem;
    }

}
