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
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "EMPRESA_USUARIO_ACESSO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class EmpresaUsuarioAcesso extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Favor informar ao menos um usuário")
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuario;

    @NotNull(message = "Favor informar ao menos uma empresa")
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idEmpresa;

    @NotNull(message = "Informe a data de acesso")
    @Column(name = "DATA_ACESSO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAcesso;

    @Column(name = "IP")
    private String ip;

    @Column(name = "ORIGEM")
    private String origem;

    @Override
    public String toString() {
        return "EmpresaUsuarioAcesso{" + "id=" + id + '}';
    }

}
