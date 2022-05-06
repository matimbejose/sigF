package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "TURMA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Turma extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o curso")
    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Curso idCurso;

    @NotNull(message = "Informe o professor")
    @JoinColumn(name = "ID_PROFESSOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Professor idProfessor;

    @NotNull(message = "Informe a descrição")
    @Column(name = "DESCRICAO")
    @Size(max = 100, message = "Máximo de 100 caracteres para descrição da turma")
    private String descricao;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @OneToOne(cascade = CascadeType.ALL)
    private CentroCusto idCentroCusto;

    @NotNull(message = "Informe a cidade")
    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cidade idCidade;

    @NotNull(message = "Informe a data inicial")
    @Column(name = "DATA_INICIAL")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataInicial;

    @NotNull(message = "Informe a data final")
    @Column(name = "DATA_FINAL")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataFinal;

    @Column(name = "HORARIO")
    @Size(max = 200, message = "Máximo de 200 caracteres para horário")
    private String horario;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String situacao;

    @Column(name = "LOCAL_REALIZACAO")
    @Size(max = 1000, message = "Máximo de 1000 caracteres para local de realização")
    private String localRealizacao;

    @Column(name = "NUMERO")
    @Size(max = 200, message = "Máximo de 200 caracteres para número")
    private String numero;

    @Override
    public String toString() {
        return "Turma{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
