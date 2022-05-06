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
@Table(name = "VENDA_PARCELA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class VendaParcela extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Documento idDocumento;

    @NotNull(message = "Informe a venda")
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_VENCIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Column(name = "DATA_RECEBIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecebimento;

    @Column(name = "VALOR_JUROS")
    private Double valorJuros;

    @Column(name = "VALOR_MULTA")
    private Double valorMulta;

    @Column(name = "ENCARGOS")
    private Double encargos;

    @NotNull(message = "Informe a situacao")
    @Column(name = "SITUACAO")
    private String situacao;

    @Column(name = "NUM_PARCELA")
    private Integer numParcela;

    @Column(name = "VALOR_RECEBIDO")
    private Double valorRecebido;

    @Override
    public String toString() {
        return "VendaParcela{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
