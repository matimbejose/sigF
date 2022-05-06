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
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "VENDA_FORMA_PAGAMENTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class VendaFormaPagamento extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Venda idVenda;

    @JoinColumn(name = "ID_FORMA_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private FormaPagamento idFormaPagamento;

    @Size(max = 3)
    @Column(name = "CONDICAO_PAGAMENTO")
    private String condicaoPagamento;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "FORMA_ESCOLHIDA", length = 1)
    private String formaEscolhida;

    @Column(name = "BANDEIRA_CARTAO", length = 10)
    private String bandeiraCartao;

    public VendaFormaPagamento(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VendaFormaPagamento{" + "id=" + id + "}";
    }

    @Override
    public VendaFormaPagamento clone() {
        try {
            VendaFormaPagamento novo = (VendaFormaPagamento) super.clone();
            novo.setId(null);
            novo.setIdVenda(null);
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new VendaFormaPagamento();
        }
    }

    public static Optional<VendaFormaPagamento> contains(List<VendaFormaPagamento> list, VendaFormaPagamento item) {
        return list.stream()
                .filter(vfp -> vfp.getIdVenda().equals(item.getIdVenda()) && vfp.getIdFormaPagamento().equals(item.getIdFormaPagamento()))
                .findAny();
    }

}
