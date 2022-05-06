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
@Table(name = "UNIDADE_OCUPACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class UnidadeOcupacao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a unidade")
    @JoinColumn(name = "ID_UNIDADE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Unidade idUnidade;

    @Column(name = "DATA_ENTRADA")
    @NotNull(message = "Informe a data de entrada.")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEntrada;

    @Column(name = "DATA_SAIDA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataSaida;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUnidadeOcupacao", orphanRemoval = true)
    private List<MoradorUnidadeOcupacao> moradorUnidadeOcupacaoList = new LinkedList<>();

    public void addMorador(MoradorUnidadeOcupacao morador) {
        morador.setIdUnidadeOcupacao(this);
        moradorUnidadeOcupacaoList.add(morador);
    }

    public void removerMorador(MoradorUnidadeOcupacao morador) {
        moradorUnidadeOcupacaoList.remove(morador);
    }

    @Override
    public String toString() {
        return "UnidadeMorador{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
