package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PLANO_PAGAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class PlanoPagamento extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 250)
    @Column(name = "DESCRICAO")
    private String descricao;

    @Size(max = 1)
    @NotNull(message = "Informe se o plano de pagamento possui entrada")
    @Column(name = "POSSUI_ENTRADA")
    private String possuiEntrada;

    @Column(name = "QUANTIDADE_PARCELAS")
    private Integer quantidadeParcelas;

    @JoinColumn(name = "ID_FORMA_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private FormaPagamento idFormaPagamento;

    @OneToMany(mappedBy = "idPlanoPagamento", cascade = CascadeType.ALL)
    private List<PlanoPagamentoParcela> planoPagamentoParcelaList;

    @Size(max = 1)
    @Column(name = "ATIVO")
    private String ativo;

    public PlanoPagamento() {
        this.ativo = "S";
    }

    public PlanoPagamento(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return descricao;
    }

}
