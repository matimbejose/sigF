package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
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
@Table(name = "CTISS")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Ctiss extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 500, message = "Máximo de 500 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe a descrição")
    @Column(name = "SUBITEM")
    @Size(max = 10, message = "Máximo de 10 caracteres para subitem")
    private String subitem;

    @NotNull(message = "Informe o código de tributação")
    @Column(name = "CODIGO")
    @Size(max = 20, message = "Máximo de 20 caracteres para código")
    private String codigo;

    @NotNull(message = "Informe a alíquota")
    @Column(name = "ALIQUOTA")
    private Double aliquota;

    @NotNull(message = "Informe a cidade")
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cidade idCidade;

    @Override
    public String toString() {
        return "Ctiss{" + "id=" + id + '}';
    }

}
