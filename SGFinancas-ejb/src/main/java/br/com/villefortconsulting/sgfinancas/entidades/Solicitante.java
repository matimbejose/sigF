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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "SOLICITANTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Solicitante extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private Cliente idCliente;

    @JoinColumn(name = "ID_SOLICITANTE_INDICOU", referencedColumnName = "ID")
    @ManyToOne
    private Solicitante idSolicitanteIndicou;

    @JoinColumn(name = "ID_AREA_ATUACAO", referencedColumnName = "ID")
    @ManyToOne
    private AreaAtuacao idAreaAtuacao;

    @JoinColumn(name = "ID_CURSO", referencedColumnName = "ID")
    @ManyToOne
    private Curso idCurso;

    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID")
    @ManyToOne
    private Turma idTurma;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @NotNull(message = "Informe o nome")
    @Column(name = "NOME")
    @Size(max = 200, message = "Você precisa especificar um nome com no máximo 200 caracteres")
    private String nome;

    @NotNull(message = "Informe o email")
    @Column(name = "EMAIL")
    @Size(max = 255, message = "Você precisa especificar um e-mail com no máximo 200 caracteres")
    private String email;

    @NotNull(message = "Informe o telefone celular")
    @Column(name = "TELEFONE_CELULAR")
    @Size(max = 50, message = "Você precisa especificar um telefone celular com no máximo 50 caracteres")
    private String telefoneCelular;

    @Column(name = "TELEFONE_FIXO")
    @Size(max = 50, message = "Você precisa especificar um telefone fixo com no máximo 50 caracteres")
    private String telefoneFixo;

    @Column(name = "CPF_CNPJ")
    @Size(max = 200, message = "Você precisa especificar um cpf/cnpj com no máximo 50 caracteres")
    private String cpfCnpj;

    @Column(name = "ORIGEM")
    @Size(max = 1, message = "Você precisa especificar uma origem com no máximo 1 caracter")
    private String origem;

    @NotNull(message = "Informe o status do solicitante")
    @Column(name = "STATUS")
    @Size(max = 1, message = "Você precisa especificar um status com no máximo 1 caracter")
    private String status;

    @Column(name = "TIPO_PESSOA")
    @Size(max = 2, message = "Você precisa especificar um tipo de pessoa com no máximo 2 caracteres")
    private String tipoPessoa;

    @Column(name = "OBSERVACAO")
    @Size(max = 2000, message = "Você precisa especificar uma observacao com no máximo 2000 caracteres")
    private String observacao;

    @Column(name = "CARGO")
    @Size(max = 200, message = "Você precisa especificar um cargo com no máximo 200 caracteres")
    private String cargo;

    @Column(name = "EMPRESA")
    @Size(max = 200, message = "Você precisa especificar uma empresa com no máximo 200 caracteres")
    private String empresa;

    @Column(name = "VALOR")
    @Size(max = 200, message = "Você precisa especificar um valor com no máximo 200 caracteres")
    private String valor;

    @Column(name = "TOTAL")
    @Size(max = 200, message = "Você precisa especificar um valor com no máximo 200 caracteres")
    private String total;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSolicitante", orphanRemoval = true)
    private List<CursoInteresse> cursosInteresse = new LinkedList<>();

    @OneToMany
    private List<SolicitantePanorama> panoramas = new LinkedList<>();

    public void addCurso(Curso curso) {
        CursoInteresse cursoInteresse = new CursoInteresse();
        cursoInteresse.setIdCurso(curso);
        cursoInteresse.setIdSolicitante(this);
        cursoInteresse.setTenatID(this.tenatID);

        if (!cursosInteresse.contains(cursoInteresse)) {
            cursosInteresse.add(cursoInteresse);
        }
    }

    public void removeCurso(CursoInteresse cursoInteresse) {
        cursosInteresse.remove(cursoInteresse);
    }

    public void addPanorama() {
        SolicitantePanorama sp = new SolicitantePanorama();
        sp.setData(new Date());
        sp.setStatus(this.status);
        sp.setIdSolicitante(this);
        sp.setTenatID(this.tenatID);

        if (!panoramas.contains(sp)) {
            panoramas.add(sp);
        }
    }

    public void removePanorama(SolicitantePanorama sp) {
        panoramas.remove(sp);
    }

    @Override
    public String toString() {
        return "Solicitante{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
