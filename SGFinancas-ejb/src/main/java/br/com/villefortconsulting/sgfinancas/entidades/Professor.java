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
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "PROFESSOR")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Professor extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @NotNull(message = "Informe o nome do funcionário")
    @Column(name = "NOME")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome")
    private String nome;

    @NotNull(message = "Informe se o professor ativo")
    @Column(name = "ATIVO")
    @Size(max = 1, message = "Máximo de 1 caracter para o ativo")
    private String ativo;

    @Column(name = "CPF")
    @Size(max = 20, message = "Máximo de 20 caracteres para o cpf")
    private String cpf;

    @Column(name = "EMAIL")
    @Size(max = 50, message = "Máximo de 50 caracteres para o email")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;

    @Column(name = "TELEFONE_RESIDENCIAL")
    @Size(max = 50, message = "Máximo de 50 caracteres para o telefone residencial.")
    private String telefoneResidencial;

    @Column(name = "TELEFONE_CELULAR")
    @Size(max = 50, message = "Máximo de 50 caracteres para o telefone celular.")
    private String telefoneCelular;

    @Column(name = "CEP")
    @Size(max = 20, message = "Máximo de 20 caracteres para o cep.")
    private String cep;

    @Column(name = "NUMERO")
    @Size(max = 10, message = "Número deverá ter entre 10 caracteres.")
    private String numero;

    @Column(name = "ENDERECO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o endereço.")
    private String endereco;

    @Column(name = "BAIRRO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o bairro.")
    private String bairro;

    @Column(name = "COMPLEMENTO")
    @Size(max = 200, message = "Máximo de 200 caracteres para o complemento.")
    private String complemento;

    public Professor() {
        this.ativo = "S";
    }

    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
