package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "VENDA_ITEM_ORDEM_DE_SERVICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class VendaItemOrdemDeServico extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @NotNull(message = "Informe a venda")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @Size(max = 100)
    @Column(name = "NOME_ITEM")
    private String nomeItem;

    @Size(max = 255)
    @Column(name = "OBSERVACAO")
    private String observacao;

    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "DESCONTO")
    private Double desconto;

    public VendaItemOrdemDeServico(Integer id) {
        this.id = id;
    }

    @Override
    public VendaItemOrdemDeServico clone() {
        try {
            VendaItemOrdemDeServico novo = (VendaItemOrdemDeServico) super.clone();
            novo.setId(null);
            novo.setIdVenda(null);
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new VendaItemOrdemDeServico();
        }
    }

    public Double getValorTotal() {
        if (valor == null || desconto == null) {
            return 0d;
        }
        return valor - desconto;
    }

    public static Optional<VendaItemOrdemDeServico> contains(List<VendaItemOrdemDeServico> list, VendaItemOrdemDeServico item) {
        return list.stream()
                .filter(vios -> vios.getIdVenda().equals(item.getIdVenda()))
                .findAny();
    }

}
