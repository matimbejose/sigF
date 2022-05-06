package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTRATO_PARCELA_AJUSTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContratoParcelaAjuste extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_CONTRATO_AJUSTE", referencedColumnName = "ID")
    @ManyToOne
    private ContratoAjuste idContratoAjuste;

    @NotNull
    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idContaParcela;

    @NotNull(message = "Favor informar o valor original da parcela")
    @Column(name = "VALOR_ORIGINAL")
    private Double valorOriginal;

    @NotNull(message = "Favor informar o valor atualizado da parcela")
    @Column(name = "VALOR_ATUALIZADO")
    private Double valorAtualizado;

    @Override
    public String toString() {
        return "ContratoParcelaAjuste{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
