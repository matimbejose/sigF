package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "ORIGEM_MERCADORIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class OrigemMercadoria extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o código")
    @Column(name = "COD_ORIGEM_MERCADORIA")
    private Integer codOrigemMercadoria;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    private String descricao;

    @Override
    public String toString() {
        return "OrigemMercadoria{" + "id=" + id + '}';
    }

}
