package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
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
@Table(name = "PAGAMENTO_SISTEMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class PagamentoSistema extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 20)
    @Column(name = "RECORRENCIA_PAGAMENTO")
    private String recorrenciaPagamento;

    @NotNull
    @Column(name = "PLANO")
    private Integer plano;

    @NotNull
    @Column(name = "VALOR")
    private Double valor;

    @Override
    public String toString() {
        return "PagamentoSistema{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
