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
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "UNIDADE_MEDIDA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class UnidadeMedida extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @Column(name = "SIGLA", nullable = false, length = 6)
    @Size(max = 6, message = "Máximo de 6 caracteres para descrição")
    private String sigla;

    @Override
    public String toString() {
        return "UnidadeMedida{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
