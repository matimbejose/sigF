package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "VENDA_SEGURADORA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance
public class VendaSeguradora extends EntityTenant implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    @Valid
    @NotNull(message = "Informe a seguradora")
    @JoinColumn(name = "ID_CLIENTE_SEGURADORA", referencedColumnName = "ID")
    @ManyToOne
    private Cliente idClienteSeguradora;

    @Valid
    @NotNull(message = "Informe a venda")
    @JoinColumn(name = "ID_VENDA", referencedColumnName = "ID")
    @ManyToOne
    private Venda idVenda;

    @Column(name = "VALOR_FRANQUIA")
    private Double valorFranquia;

    @Column(name = "NUMERO_SINISTRO")
    @Size(max = 30, message = "Informe o numero do sinistro.")
    private String numeroSinistro;

    @Override
    public VendaSeguradora clone() {
        try {
            VendaSeguradora novo = (VendaSeguradora) super.clone();

            novo.setId(null);
            novo.setIdVenda(null);
            return novo;
        } catch (CloneNotSupportedException ex) {
            return new VendaSeguradora();
        }
    }

}
