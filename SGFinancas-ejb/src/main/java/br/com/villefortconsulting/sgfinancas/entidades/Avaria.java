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
@Table(name = "AVARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class Avaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe o código da avaría")
    @Column(name = "CODIGO")
    private Integer codigo;

    @NotNull(message = "Informe a situação da avaría")
    @Column(name = "ATIVO")
    @Size(max = 1)
    private String ativo;

    @Override
    public String toString() {
        return "Avaria{" + "id=" + id + ", tenatID=" + tenatID + "}";
    }

}
