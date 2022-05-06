package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "USUARIO_LEITURA_TERMO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLeituraTermo extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o usuário")
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @NotNull
    @Column(name = "VERSAO_TERMO")
    private Integer versaoTermo;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_ACEITE")
    private Date dataAceite;

    @NotNull
    @Column(name = "IP")
    private String ip;

    @Override
    public String toString() {
        return "UsuarioLeituraTermo{" + "id=" + id + '}';
    }

}
