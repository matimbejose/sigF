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
@Table(name = "NCM")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
@Getter
@Setter
@Inheritance
public class Ncm extends EntityId implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o código")
    @Column(name = "CODIGO")
    @Size(max = 10, message = "Máximo de 10 caracteres para o código")
    private String codigo;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 2000, message = "Máximo de 2000 caracteres para descrição")
    private String descricao;

    @NotNull(message = "Informe a descrição")
    @Column(name = "TIPO")
    @Size(max = 2, message = "Máximo de 2 caracteres para descrição")
    private String tipo;

    @Column(name = "CODIGO_PAI")
    @Size(max = 10, message = "Máximo de 10 caracteres para descrição")
    private String codigoPai;

    @JoinColumn(name = "ID_NCM_PAI", referencedColumnName = "ID")
    @ManyToOne
    private Ncm idNcmPai;

    @Column(name = "MVA")
    private Double mva;

    @Column(name = "CEST")
    private String cest;

    @Override
    public String toString() {
        return "Ncm{" + "id=" + id + '}';
    }

}
