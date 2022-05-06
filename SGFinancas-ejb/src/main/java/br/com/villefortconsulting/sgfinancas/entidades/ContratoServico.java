package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTRATO_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContratoServico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    @NotNull
    private Contrato idContrato;

    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID")
    @ManyToOne
    @NotNull
    private Servico idServico;

    @NotNull
    @Min(0)
    @Column(name = "QUANTIDADE")
    private Double quantidade;

    @NotNull
    @Min(0)
    @Column(name = "VALOR")
    private Double valor;

    @NotNull
    @Min(0)
    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "OBSERVACAO")
    private String observacao;

    @NotNull
    @Size(max = 1, min = 1)
    @Column(name = "ATIVO")
    private String ativo;

    @Transient
    private boolean edicao = false;

    public Double getValorTotal() {
        if (valor == null || quantidade == null) {
            return 0d;
        }
        return valor * quantidade - NumeroUtil.somar(this.desconto);
    }

}
