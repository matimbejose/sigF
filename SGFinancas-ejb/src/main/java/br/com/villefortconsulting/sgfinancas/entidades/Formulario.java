package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
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
@Table(name = "FORMULARIO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class Formulario extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 200, message = "Máximo de 200 caracteres para descrição")
    private String descricao;

    @Column(name = "OBSERVACOES")
    @Size(max = 2048, message = "Máximo de 200 caracteres para descrição")
    private String observacoes;

    @NotNull(message = "Informe a situação do formulário")
    @Column(name = "ATIVO")
    @Size(max = 1)
    private String ativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormulario", orphanRemoval = true)
    private List<Secao> formularioSecaoList = new LinkedList<>();

    @Override
    public String toString() {
        return "Formulario{" + "id=" + id + ", tenatID=" + tenatID + "}";
    }

}
