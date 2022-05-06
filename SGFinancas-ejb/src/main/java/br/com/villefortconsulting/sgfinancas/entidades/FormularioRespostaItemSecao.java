package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FORMULARIO_RESPOSTA_ITEM_SECAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class FormularioRespostaItemSecao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_FORMULARIO_RESPOSTA", referencedColumnName = "ID")
    @ManyToOne
    private FormularioResposta idFormularioResposta;

    @JoinColumn(name = "ID_ITEM_SECAO", referencedColumnName = "ID")
    @ManyToOne
    private ItemSecao idItemSecao;

    @JoinColumn(name = "ID_SECAO", referencedColumnName = "ID")
    @ManyToOne
    private Secao idSecao;

    @JoinColumn(name = "ID_AVARIA", referencedColumnName = "ID")
    @ManyToOne
    private Avaria idAvaria;

    @Column(name = "RESPOSTA")
    @Size(max = 10)
    private String resposta;

    @Override
    public String toString() {
        return "Formulario{" + "id=" + id + ", tenatID=" + tenatID + "}";
    }

}
