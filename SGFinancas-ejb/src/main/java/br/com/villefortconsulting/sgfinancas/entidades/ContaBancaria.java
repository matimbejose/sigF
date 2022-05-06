package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoContaBancaria;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
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
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.springframework.util.StringUtils;

@Audited
@Entity
@Table(name = "CONTA_BANCARIA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class ContaBancaria extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o banco")
    @JoinColumn(name = "ID_BANCO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Banco idBanco;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(cascade = CascadeType.ALL)
    private PlanoConta idPlanoConta;
    
    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @NotNull(message = "Informe a descrição")
    @Size(message = "Descrição com no máximo 10 caracteres")
    @Column(name = "DESCRICAO")
    private String descricao;

    @NotNull(message = "Informe a agência")
    @Size(message = "Agencia com no máximo 10 caracteres")
    @Column(name = "AGENCIA")
    private String agencia;

    @NotNull(message = "Informe a conta")
    @Size(message = "Conta com no máximo 10 caracteres")
    @Column(name = "CONTA")
    private String conta;

    @NotNull(message = "Informe a agência")
    @Size(message = "Tipo da conta com no máximo 1 caracter")
    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @Column(name = "SALDO_INICIAL")
    private Double saldoInicial;

    @Column(name = "DATA_SALDO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataSaldo;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @Column(name = "DIGITO_AGENCIA")
    @Size(max = 1, message = "Dígito da agencia com no máximo 1 caracter")
    private String digitoAgencia;

    @Column(name = "DIGITO_CONTA")
    @Size(max = 1, message = "Dígito da conta com no máximo 1 caracter")
    private String digitoConta;

    @Column(name = "OPERACAO")
    @Size(max = 3, message = "Dígito da conta com no máximo 3 caracteres")
    private String operacao;

    @NotNull
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Informe ao menos 1 carácter para a situação da conta bancária")
    private String situacao;

    @Column(name = "CARTEIRA")
    private Integer carteira;

    @Column(name = "CEDENTE")
    @Size(message = "Cedente com no máximo 20 caracteres")
    private String cedente;

    public ContaBancaria(Integer id) {
        this.id = id;
    }

    public String getAgenciaCompleta() {
        if (agencia != null) {
            return (!StringUtils.isEmpty(this.digitoAgencia)) ? this.agencia + "-" + this.digitoAgencia : agencia;
        }
        return agencia;
    }

    public String getContaCompleta() {
        if (conta != null) {
            return (!StringUtils.isEmpty(this.digitoConta)) ? this.conta + "-" + this.digitoConta : conta;
        }
        return conta;
    }

    public String getSituacaoDescricao() {
        if (situacao != null) {
            return EnumSituacaoContaBancaria.retornaEnumSelecionado(situacao).getDescricaoSituacao();
        }
        return "";
    }

    public String getTipoContaDescricao() {
        switch (tipoConta) {
            case "C":
                return "Corrente";
            case "P":
                return "Poupança";
            case "S":
                return "Salário";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "ContaBancaria{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
