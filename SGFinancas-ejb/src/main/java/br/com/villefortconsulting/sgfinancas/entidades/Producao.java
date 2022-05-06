package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusProducao;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PRODUCAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Inheritance
public class Producao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private Venda idVenda;

    @NotNull
    @Column(name = "DATA_PEDIDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPedido;

    @Column(name = "DATA_PRODUCAO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataProducao;

    @Column(name = "DATA_LIMITE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataLimite;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @Column(name = "NUMERO")
    private Integer numero;

    @NotNull
    @Column(name = "STATUS", length = 1)
    private String status;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idProducao", fetch = FetchType.LAZY)
    private List<ProducaoProduto> producaoProdutoList;

    public String getStatusName() {
        return EnumStatusProducao.retornaEnumSelecionado(status).getDescricao();
    }

    public Boolean getFinalizado() {
        return status.equals(EnumStatusProducao.PRODUZIDO.getTipo()) || status.equals(EnumStatusProducao.CANCELADO.getTipo());
    }

    @Override
    public String toString() {
        return "Producao{" + "id=" + id + '}';
    }

}
