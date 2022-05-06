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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PATRIMONIO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Patrimonio extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO", nullable = false, length = 200)
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe o tipo de patrimônio")
    @JoinColumn(name = "ID_TIPO_PATRIMONIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private TipoPatrimonio idTipoPatromonio;

    @NotNull(message = "Informe a quantidade")
    @Column(name = "QUANTIDADE")
    private Integer quantidade;

    @Override
    public String toString() {
        return "Patrimonio{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
