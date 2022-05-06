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
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SINDICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Sindico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o síndico")
    @JoinColumn(name = "ID_PESSOA_SINDICO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idSindico;

    @NotNull(message = "Informe a data de início do mandato")
    @Column(name = "DATA_INICIO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicio;

    @Column(name = "DATA_FIM")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFim;

    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "idSindico", orphanRemoval = true)
    private List<SindicoConselho> sindicoConselhoList = new LinkedList<>();

    public void addConselheiro(SindicoConselho sindicoConselho) {
        sindicoConselho.setIdSindico(this);
        sindicoConselhoList.add(sindicoConselho);
    }

    public void removeConselheiro(SindicoConselho sindicoConselho) {
        sindicoConselhoList.remove(sindicoConselho);
    }

    @Override
    public String toString() {
        return "Sindico{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
