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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "MODELO_INFORMACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class ModeloInformacao extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_MODELO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Modelo idModelo;

    @NotNull
    @Column(name = "ANO")
    private Integer ano;

    @NotNull
    @Column(name = "PRECO")
    private Double preco;

    @NotNull
    @Column(name = "FIPE_CODIGO")
    @Size(max = 50)
    private String fipeCodigo;

    @NotNull
    @Column(name = "FIPE_ID")
    @Size(max = 50)
    private String fipeId;

    @NotNull
    @Column(name = "TIPO")
    @Size(max = 10)
    private String tipo;

}
