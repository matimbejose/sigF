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
@Table(name = "PARAMETRO_NOTIFICACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ParametroNotificacao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "EMAIL")
    @Size(max = 200, message = "Máximo de 200 caracteres para email")
    private String email;

    @NotNull(message = "Informe o tipo")
    @Column(name = "TIPO")
    @Size(max = 1, message = "Máximo de 1 caracter para tipo")
    private String tipo;

    @Override
    public String toString() {
        return "ParametroNotificacao{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
