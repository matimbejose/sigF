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
@Table(name = "VENDA_PARCELA_BAIXA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class VendaParcelaBaixa extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Documento idDocumento;

    @NotNull(message = "Informe a venda")
    @JoinColumn(name = "ID_VENDA_PARCELA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @Column(name = "DATA_RECEBIMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRecebimento;

    @Column(name = "VALOR_JUROS")
    private Double valorJuros;

    @Column(name = "VALOR_MULTA")
    private Double valorMulta;

    @Column(name = "ENCARGOS")
    private Double encargos;

    @Column(name = "VALOR_RECEBIDO")
    private Double valorRecebido;

    @Override
    public String toString() {
        return "VendaParcelaBaixa{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
