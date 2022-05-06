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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "TOKEN_APP")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NamedQueries({
    @NamedQuery(name = "findByToken", query = "select ta from TokenApp ta where ta.token = ?1"),
    @NamedQuery(name = "findByUsuario", query = "select ta from TokenApp ta where ta.idUsuario = ?1 and ta.deviceUuid is null"),
    @NamedQuery(name = "findByUsuarioAndUuid", query = "select ta from TokenApp ta where ta.idUsuario = ?1 and ta.deviceUuid = ?2"),
    @NamedQuery(name = "findByUsuarioAndUuidAndEmpresaIsNull", query = "select ta from TokenApp ta where ta.idUsuario = ?1 and ta.idEmpresa is null and ta.deviceUuid = ?2"),
    @NamedQuery(name = "findByUsuarioAndEmpresaAndUuid", query = "select ta from TokenApp ta where ta.idUsuario = ?1 and ta.idEmpresa = ?2 and ta.deviceUuid = ?3"),
    @NamedQuery(name = "listByUsuario", query = "select ta from TokenApp ta where ta.idUsuario = ?1"),
    @NamedQuery(name = "listByEmpresa ", query = "select ta from TokenApp ta where ta.idEmpresa = ?1"),
    @NamedQuery(name = "listBy", query = "select ta from TokenApp ta where ta.idUsuario = ?1 and ta.idEmpresa = ?2")
})
public class TokenApp extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuario;

    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Empresa idEmpresa;

    @Size(max = 32)
    @Column(name = "DEVICE_UUID")
    private String deviceUuid;

    @Size(max = 64)
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "DATA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Override
    public String toString() {
        return "TokenApp{id=" + id + "}";
    }

}
