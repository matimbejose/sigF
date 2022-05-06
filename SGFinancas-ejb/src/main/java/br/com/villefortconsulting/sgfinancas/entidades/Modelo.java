package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVeiculoFipe;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "MODELO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class Modelo extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @JoinColumn(name = "ID_MARCA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Marca idMarca;

    @NotNull
    @Column(name = "NOME")
    @Size(max = 50, message = "Máximo de 200 caracteres para descrição")
    private String nome;

    @NotNull
    @Column(name = "FIPE_NOME")
    @Size(max = 50)
    private String fipeNome;

    @NotNull
    @Column(name = "FIPE_ID")
    private Integer fipeId;

    @NotNull
    @Column(name = "TIPO")
    @Size(max = 10)
    private String tipo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idModelo", orphanRemoval = true)
    private List<ModeloInformacao> anos = new LinkedList<>();

    public String getNomeTipo() {
        return EnumTipoVeiculoFipe.valueOf(tipo.toUpperCase()).getDescricao();
    }

}
