package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTRATO_CLIENTE_VEICULO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ContratoClienteVeiculo extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @OneToOne
    private Contrato idContrato;

    @JoinColumn(name = "ID_CLIENTE_VEICULO", referencedColumnName = "ID")
    @OneToOne
    private ClienteVeiculo idClienteVeiculo;

}
