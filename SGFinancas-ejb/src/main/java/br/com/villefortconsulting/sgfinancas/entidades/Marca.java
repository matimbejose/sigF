package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityId;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
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
@Table(name = "MARCA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class Marca extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "NOME")
    @Size(max = 50)
    private String nome;

    @NotNull
    @Column(name = "FIPE_NOME")
    @Size(max = 50)
    private String fipeNome;

    @NotNull
    @Column(name = "FIPE_ID")
    private Integer fipeId;

    @Size(max=20)
    @Column(name = "FIPE_ORDER")
    private String fipeOrder;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idMarca", orphanRemoval = true)
    private List<Modelo> modeloList = new LinkedList<>();

}
