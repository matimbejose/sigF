package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITANTE_HISTORICO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class SolicitanteHistorico extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o solicitante")
    @JoinColumn(name = "ID_SOLICITANTE", referencedColumnName = "ID")
    @ManyToOne
    private Solicitante idSolicitante;

    @NotNull(message = "Informe o curso")
    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID")
    @ManyToOne
    private Curso idCurso;

    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID")
    @ManyToOne
    private Turma idTurma;

    @NotNull(message = "Informe o usuário contato")
    @JoinColumn(name = "ID_USUARIO_CONTATO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario idUsuarioContato;

    @NotNull(message = "Informe a data")
    @Column(name = "DATA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date data;

    @NotNull(message = "Informe o comentário")
    @Column(name = "COMENTARIO")
    @Size(max = 500, message = "Você precisa especificar um comentário com no máximo 5000 caracteres")
    private String comentario;

    @NotNull(message = "Informe a situação do solicitante")
    @Column(name = "SITUACAO_SOLICITANTE")
    @Size(max = 1, message = "Situação solicitante deve possuir apenas 1 caracter")
    private String situacaoSolicitante;

    @Override
    public String toString() {
        return "SolicitanteHistorico{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
