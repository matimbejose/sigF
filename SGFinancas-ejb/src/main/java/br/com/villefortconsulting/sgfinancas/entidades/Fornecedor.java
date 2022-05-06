package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.Endereco;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Table(name = "FORNECEDOR")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Fornecedor extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Embedded
    private Endereco endereco = new Endereco();

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;

    @NotNull(message = "Informe o tipo pessoa")
    @Column(name = "TIPO_PESSOA")
    @Size(max = 2, message = "Máximo de 2 caracter para tipo pessoa")
    private String tipoPessoa;

    @Column(name = "RAZAO_SOCIAL")
    @Size(max = 200, message = "Razão social deverá ter entre 50 caracteres.")
    private String razaoSocial;

    @NotNull(message = "Informe o cpf ou cnpj")
    @Size(max = 30, message = "Cpf/cnpj deverá ter entre 30 caracteres.")
    @Column(name = "CPF_CNPJ")
    private String cpfCnpj;

    @NotNull(message = "Informe se usa inscrição estadual")
    @Column(name = "USA_INSCRICAO_ESTADUAL")
    private String usaInscricaoEstadual;

    @Column(name = "INSCRICAO_ESTADUAL")
    @Size(max = 50, message = "Inscrição estadual deverá ter entre 50 caracteres.")
    private String inscricaoEstadual;

    @Column(name = "INSCRICAO_MUNICIPAL")
    @Size(max = 50, message = "Inscrição municipal deverá ter entre 50 caracteres.")
    private String inscricaoMunicipal;

    @Column(name = "FONE_COMERCIAL")
    @Size(max = 50, message = "Telefone comercial deverá ter entre 50 caracteres.")
    private String foneComercial;

    @Column(name = "EMAIL")
    @Size(max = 50, message = "Email deverá ter entre 50 caracteres.")
    private String email;

    @Column(name = "FONE_RESIDENCIAL")
    @Size(max = 50, message = "Telefone residencial deverá ter entre 50 caracteres.")
    private String foneResidencial;

    @Column(name = "CONTATO")
    @Size(max = 50, message = "Contato deverá ter entre 50 caracteres.")
    private String contato;

    @Column(name = "CELULAR")
    @Size(max = 50, message = "Celular deverá ter entre 50 caracteres.")
    private String celular;

    @Column(name = "OBSERVACAO")
    @Size(max = 500, message = "Observação deverá ter entre 500 caracteres.")
    private String observacao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFornecedor", orphanRemoval = true)
    private List<FornecedorContato> fornecedorContatoList = new LinkedList<>();

    @Column(name = "ATIVO")
    private String ativo;

    public Fornecedor() {
        this.usaInscricaoEstadual = "N";
        this.ativo = "S";
    }

    public Fornecedor(Integer id) {
        this.id = id;
        this.usaInscricaoEstadual = "N";
        this.ativo = "S";
    }

    @Override
    public String toString() {
        return "Fornecedor {" + "id=" + id + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Fornecedor)) {
            return false;
        }
        Fornecedor other = (Fornecedor) object;
        return !((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id)));
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return super.hashCode();
        }
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.idPlanoConta);
        hash = 11 * hash + Objects.hashCode(this.cpfCnpj);
        return 11 * hash + Objects.hashCode(this.ativo);
    }

}
