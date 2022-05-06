package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
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
import javax.persistence.Temporal;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TRANSACAO_INTEGRACAO_BANCARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class TransacaoIntegracaoBancaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a integração bancária")
    @JoinColumn(name = "ID_INTEGRACAO_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private IntegracaoBancaria idIntegracaoBancaria;

    @NotNull(message = "Informe a descricao")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe o tipo")
    @Column(name = "TIPO")
    private String tipo;

    @NotNull(message = "Informe o codigo")
    @Column(name = "CODIGO")
    private String codigo;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @NotNull(message = "Informe o valor")
    @Column(name = "VALOR")
    private Double valor;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @Column(name = "VALOR_CONCILIADO")
    private Double valorConciliado;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTransacaoIntegracaoBancaria", orphanRemoval = true)
    private List<TransacaoIntegracaoBancariaContaParcela> transacaoIntegracaoBancariaContaParcelaList = new LinkedList<>();

    @Override
    public String toString() {
        return "TransacaoIntegracaoBancaria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
