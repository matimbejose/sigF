package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContratacao;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "FUNCIONARIO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Funcionario extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoConta;

    @NotNull(message = "Informe o nome do funcionário")
    @Column(name = "NOME")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome do funcionário")
    private String nome;

    @Column(name = "NOME_MAE")
    @Size(max = 200, message = "Máximo de 200 caracteres para o nome da mãe")
    private String nomeMae;

    @NotNull(message = "Informe o cpf")
    @Column(name = "CPF", nullable = false, length = 200)
    @Size(max = 20, message = "Máximo de 20 caracteres para o cpf")
    private String cpf;

    @Size(max = 100, message = "Senha do funcionário deve possuir no máximo 100 caracteres")
    @Column(name = "SENHA")
    private String senha;

    @NotNull(message = "Informe um e-mail.")
    @Column(name = "EMAIL")
    @Size(max = 50, message = "Máximo de 50 caracteres para o email")
    private String email;

    @Size(max = 50, message = "Matricula deve possuir no máximo 50 caracteres")
    @Column(name = "MATRICULA")
    private String matricula;

    @NotNull(message = "Informe a data de nascimento")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_DEMISSAO")
    private Date dataDemissao;

    @NotNull(message = "Informe a data da contratação")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_CONTRATACAO")
    private Date dataContratacao;

    @NotNull(message = "Informe o tipo de contratação")
    @Column(name = "TIPO_CONTRATACAO")
    @Size(max = 2, message = "Máximo de 1 caracteres para o tipo de contratação")
    private String tipoContratacao;

    @Column(name = "SALARIO_BASE")
    private Double salarioBase;

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

    @NotNull(message = "Informe se o funcionário tem horário especial.")
    @Column(name = "TEM_HORARIO_ESPECIAL")
    @Size(max = 1, message = "Máximo de 1 caracteres para o horário especial.")
    private String temHorarioEspecial;

    @Column(name = "HORA_ESPECIAL")
    @Size(max = 5, message = "Máximo de 5 caracteres para o horário especial.")
    private String horaEspecial;

    @Column(name = "HORA_ESPECIAL_FINAL")
    @Size(max = 5, message = "Máximo de 5 caracteres para o horário especial.")
    private String horaEspecialFinal;

    @Lob
    @Column(name = "FOTO")
    private byte[] foto;

    @Column(name = "LIMITE_DE_DESCONTO")
    private Double limiteDeDesconto;

    @Column(name = "ATIVO")
    private String ativo;

    @NotNull(message = "Informe se o funcionario é vendendor")
    @Column(name = "EORCAMENTISTA")
    @Size(max = 1, message = "Máximo de 1 caracteres informar se é orçamentista/vendedor")
    private String ehOrcamentista;

    @OneToMany(mappedBy = "idFuncionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuncionarioAtendimento> funcionarioAtendimentoList = new LinkedList<>();

    @OneToMany(mappedBy = "idFuncionario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FuncionarioServico> funcionarioServicoList = new LinkedList<>();

    @Column(name = "CARGO")
    @Size(max = 100, message = "Máximo de 100 caracteres para o cargo.")
    private String cargo;

    public Funcionario() {
        this.tipoContratacao = EnumTipoContratacao.FUNCIONARIO.getTipo();
        this.ehOrcamentista = "N";
    }

    public Funcionario(Integer id) {
        this.id = id;
        this.ehOrcamentista = "N";
    }

    public String getFoto64() {
        if (foto == null) {
            return "";
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(foto);
    }

    public FuncionarioAtendimento getFuncionarioAtendimentoDoDia(int i) {
        if (funcionarioAtendimentoList == null || funcionarioAtendimentoList.isEmpty()) {
            return null;
        }
        return funcionarioAtendimentoList.stream()
                .filter(fa -> fa.getDiaSemana().equals(i))
                .findAny()
                .orElse(null);
    }

    @Override
    public String toString() {
        return "Funcionario{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
