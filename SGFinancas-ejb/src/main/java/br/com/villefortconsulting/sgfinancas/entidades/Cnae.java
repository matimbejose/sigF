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
@Table(name = "CNAE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Cnae extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o codigo")
    @Column(name = "CODIGO")
    private String codigo;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 500, message = "Máximo de 500 caracteres para descrição")
    private String descricao;

    @NotNull
    @JoinColumn(name = "ID_CLASSIFICACAO", referencedColumnName = "ID")
    @ManyToOne
    private Classificacao idClassificacao;

    @Override
    public String toString() {
        return "Cnae{" + "id=" + id + '}';
    }

}
