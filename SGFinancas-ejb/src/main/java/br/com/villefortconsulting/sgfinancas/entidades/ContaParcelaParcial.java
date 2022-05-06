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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTA_PARCELA_PARCIAL")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContaParcelaParcial extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_VENCIMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "DATA_PAGAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;

    @NotNull(message = "Informe o valor que foi pago")
    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @NotNull(message = "Informe o valor total")
    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "MULTA")
    private Double multa;

    @Column(name = "JUROS")
    private Double juros;

    @Column(name = "ENCARGOS")
    private Double encargos;

    @Column(name = "IMPOSTO_FEDERAL")
    private Double impostoFederal;

    @Column(name = "IMPOSTO_MUNICIPAL")
    private Double impostoMunicipal;

    @NotNull(message = "Informe a parcela")
    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @OneToOne(optional = false)
    private ContaParcela idContaParcela;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Documento idDocumento;

    @JoinColumn(name = "ID_FORMA_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private FormaPagamento idFormaPagamento;

    @Override
    public String toString() {
        return "ContaParcelaParcial{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
