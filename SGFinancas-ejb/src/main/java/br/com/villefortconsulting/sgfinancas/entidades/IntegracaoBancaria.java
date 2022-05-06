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
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "INTEGRACAO_BANCARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class IntegracaoBancaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "DATA_PROCESSAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataProcessamento;

    @NotNull(message = "Informe a competencia")
    @Column(name = "COMPETENCIA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date competencia;

    @NotNull(message = "Informe a data de criação")
    @Column(name = "DATA_CRIACAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @NotNull(message = "Informe o extrato")
    @JoinColumn(name = "ID_DOCUMENTO_ANEXO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private DocumentoAnexo idDocumentoAnexo;

    @NotNull(message = "Informe o valor total")
    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @Column(name = "AGENCIA")
    private String agencia;

    @Column(name = "CONTA")
    private String conta;

    @Column(name = "BANCO")
    private String banco;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idIntegracaoBancaria", orphanRemoval = true)
    private List<TransacaoIntegracaoBancaria> transacaoIntegracaoBancariaList = new LinkedList<>();

    public IntegracaoBancaria(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IntegracaoBancaria{" + "id=" + id + '}';
    }

}
