package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
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
@Table(name = "CATEGORIA_EMPRESA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class CategoriaEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "DESCRICAO")
    @Size(max = 100, message = "Máximo de 100 caracteres para a descricao")
    private String descricao;

    @Override
    public String toString() {
        return "CategoriaEmpresa{" + "id=" + id + '}';
    }

}
