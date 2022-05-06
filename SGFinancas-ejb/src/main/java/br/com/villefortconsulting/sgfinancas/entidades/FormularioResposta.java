package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "FORMULARIO_RESPOSTA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class FormularioResposta extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_FORMULARIO", referencedColumnName = "ID")
    @ManyToOne
    private Formulario idFormulario;

    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuario;

    @JoinColumn(name = "ID_CLIENTE_VEICULO", referencedColumnName = "ID")
    @ManyToOne
    private ClienteVeiculo idClienteVeiculo;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @Column(name = "DATA_RESPOSTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataResposta;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormularioResposta", orphanRemoval = true)
    private List<FormularioRespostaItemSecao> formularioRespostaItemSecaoList = new LinkedList<>();

    @Override
    public String toString() {
        return "Formulario{" + "id=" + id + ", tenatID=" + tenatID + "}";
    }

}
