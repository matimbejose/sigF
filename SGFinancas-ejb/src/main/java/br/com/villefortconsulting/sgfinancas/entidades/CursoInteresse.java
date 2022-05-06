package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CURSO_INTERESSE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class CursoInteresse extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o solicitante")
    @JoinColumn(name = "ID_SOLICITANTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Solicitante idSolicitante;

    @NotNull(message = "Informe o curso")
    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Curso idCurso;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @Override
    public String toString() {
        return "CursoInteresse{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
