package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Data
@Entity
@Audited
@Cacheable
@Table(name = "TIPO_SOCIAL_EMPRESA")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TipoSocialEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "COD_ACRONIMO")
    private String codigoAcronimo;

    @Column(name = "NOME")
    private String nomeCompleto;

    @Column(name = "ACRONIMO")
    private String acronimo;

}
