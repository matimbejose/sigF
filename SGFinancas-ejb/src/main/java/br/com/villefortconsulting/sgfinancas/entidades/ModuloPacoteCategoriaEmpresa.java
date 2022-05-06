package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "MODULO_PACOTE_CATEGORIA_EMPRESA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ModuloPacoteCategoriaEmpresa extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_MODULO_PACOTE", referencedColumnName = "ID")
    @ManyToOne
    private ModuloPacote idModuloPacote;

    @JoinColumn(name = "ID_CATEGORIA_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private CategoriaEmpresa idCategoriaEmpresa;

    public ModuloPacoteCategoriaEmpresa(ModuloPacote idModuloPacote) {
        this.idModuloPacote = idModuloPacote;
    }

}
