package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CENTRO_CUSTO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class CentroCusto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a descrição")
    @Size(max = 200, message = "O campo deverá estar entre 1 e 200 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @OneToOne(mappedBy = "idCentroCusto")
    private Turma idTurma;

    @Size(max = 20, message = "CNPJ deve possuir no máximo 20 caracteres")
    @Column(name = "CNPJ")
    private String cnpj;

    @Size(max = 40, message = "Token deve possuir no máximo 40 caracteres")
    @Column(name = "TOKEN")
    private String token;

    @Column(name = "ATIVO")
    private String ativo = "S";

    @Size(max = 10, message = "CEP deve possuir no máximo 10 caracteres")
    @Column(name = "CEP")
    private String cep;

    @Size(max = 300, message = "Logradouro deve possuir no máximo 300 caracteres")
    @Column(name = "LOGRADOURO")
    private String logradouro;

    @Size(max = 100, message = "Bairro deve possuir no máximo 100 caracteres")
    @Column(name = "BAIRRO")
    private String bairro;

    @Size(max = 10, message = "Número do endereço deve possuir no máximo 10 caracteres")
    @Column(name = "NUMERO_LOGRADOURO")
    private String numeroLogradouro;

    @Size(max = 60, message = "Complemento deve possuir no máximo 60 caracteres")
    @Column(name = "COMPLEMENTO_ENDERECO")
    private String complementoEndereco;

    @JoinColumn(name = "ID_CIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Cidade idCidade;

    @Size(max = 100, message = "Responsável deve possuir no máximo 100 caracteres")
    @Column(name = "RESPONSAVEL")
    private String responsavel;

    @Size(max = 100, message = "Telefone deve possuir no máximo 100 caracteres")
    @Column(name = "FONE")
    private String fone;

    @Size(max = 20, message = "Celular deve possuir no máximo 20 caracteres")
    @Column(name = "CELULAR")
    private String celular;

    @Size(max = 20, message = "Celular deve possuir no máximo 20 caracteres")
    @Column(name = "CELULAR_RESPONSAVEL")
    private String celularResponsavel;

    @Size(max = 100, message = "Email deve possuir no máximo 100 caracteres")
    @Column(name = "EMAIL")
    private String email;

    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_PLANO_CONTA_IUGU", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaIugu;

    @JoinColumn(name = "ID_PLANO_CONTA_REDE", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaRede;

    @JoinColumn(name = "ID_PLANO_CONTA_DINHEEIRO", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaDinheiro;

    @JoinColumn(name = "ID_PLANO_CONTA_OUTROS", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoContaOutros;

    public CentroCusto(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CentroCusto{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
