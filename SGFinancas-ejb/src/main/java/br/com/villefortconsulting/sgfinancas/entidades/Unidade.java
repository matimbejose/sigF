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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "UNIDADE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Unidade extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO", nullable = false, length = 200)
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO", nullable = false, length = 1)
    @Size(max = 1, message = "Máximo de 1 caracteres para situação")
    private String situacao;

    @NotNull(message = "Informe o tipo da unidade")
    @Column(name = "TIPO_UNIDADE", nullable = false, length = 1)
    @Size(max = 1, message = "Máximo de 1 caracteres para tipo da unidade")
    private String tipoUnidade;

    @NotNull(message = "Informe a fração ideal")
    @Column(name = "FRACAO_IDEAL")
    private Double fracaoIdeal;

    @NotNull(message = "Informe o bloco")
    @JoinColumn(name = "ID_BLOCO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Bloco idBloco;

    @NotNull(message = "Informe o dono")
    @JoinColumn(name = "ID_DONO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idDono;

    @NotNull(message = "Informe a data de aquisição")
    @Column(name = "DATA_AQUISICAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataAquisicao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUnidade")
    private List<UnidadeOcupacao> unidadeOcupacaoList = new LinkedList<>();

    public void addUnidadeOcupacao(UnidadeOcupacao unidadeOcupacao) {
        unidadeOcupacao.setIdUnidade(this);
        unidadeOcupacaoList.add(unidadeOcupacao);
    }

    @Override
    public String toString() {
        return "Unidade{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
