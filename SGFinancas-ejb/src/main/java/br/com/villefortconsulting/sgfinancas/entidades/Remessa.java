package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
@Table(name = "REMESSA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Remessa extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a data de emissão")
    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @NotNull(message = "Informe a quantidade de parcelas")
    @Column(name = "QTD_PARCELAS")
    private Integer qtdParcelas;

    @NotNull(message = "Informe o documento")
    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Documento idDocumento;

    @NotNull(message = "Informe o usuário responsável")
    @JoinColumn(name = "ID_USUARIO_RESPONSAVEL", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuarioResponsavel;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @NotNull(message = "Informe se o boleto foi processado")
    @Column(name = "PROCESSADA")
    private String processada;

    public Remessa() {
        this.processada = "N";
    }

    @Override
    public String toString() {
        return "Remessa{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
