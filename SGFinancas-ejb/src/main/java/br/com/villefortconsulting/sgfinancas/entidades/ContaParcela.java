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
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "CONTA_PARCELA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ContaParcela extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "OBSERVACAO", length = 1000)
    @Size(max = 1000, message = "Máximo de 1000 caracteres para observacao")
    private String observacao;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_VENCIMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;

    @NotNull(message = "Informe o número de parcelas")
    @Column(name = "NUM_PARCELA")
    private Integer numParcela;

    @NotNull(message = "Informe valor da parcela")
    @Column(name = "VALOR")
    private Double valor;

    @NotNull(message = "Informe total da parcela")
    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @Column(name = "ENCARGO")
    private Double encargo;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCancelamento;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @Column(name = "FECHADA")
    private String fechada;

    @Column(name = "DATA_PAGAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "PAGAMENTO_PARCIAL")
    @Size(max = 1, message = "Máximo de 1 caracter para pagamento parcial")
    private String pagamentoParcial;

    @Column(name = "RESPONSAVEL_CANCELAMENTO")
    private String responsavelCancelamento;

    @Column(name = "ADVEM_CONTRATO")
    @Size(max = 1, message = "Máximo de 1 caracter para saber se advem de contrato")
    private String advemContrato;

    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @Column(name = "OUTROS_CUSTOS")
    private Double outrosCustos;

    @Column(name = "JUROS")
    private Double juros;

    @Column(name = "DESCONTO")
    private Double desconto;

    @Column(name = "MULTA")
    private Double multa;

    @Column(name = "ENCARGOS")
    private Double encargos;

    @Column(name = "VALOR_IR")
    private Double valorIR;

    @Column(name = "VALOR_PIS")
    private Double valorPIS;

    @Column(name = "VALOR_CSLL")
    private Double valorCSLL;

    @Column(name = "VALOR_INSS")
    private Double valorINSS;

    @Column(name = "VALOR_COFINS")
    private Double valorCOFINS;

    @Column(name = "VALOR_ISS")
    private Double valorISS;

    @Column(name = "NUM_NF")
    private String numNf;

    @Column(name = "VALOR_ICMS")
    private Double valorICMS;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @JoinColumn(name = "ID_MOTIVO_CANCELAMENTO_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private MotivoCancelamentoConta idMotivoCancelamentoConta;

    @NotNull(message = "Informe uma conta.")
    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Conta idConta;

    @JoinColumn(name = "ID_BOLETO", referencedColumnName = "ID")
    @ManyToOne
    private Boleto idBoleto;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @JoinColumn(name = "ID_FORMA_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private FormaPagamento idFormaPagamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idContaParcela")
    private List<ContaParcelaParcial> contaParcelaParcialList = new LinkedList<>();

    @Transient
    private Double valorRestante;

    @Transient
    private Double tributosTotais;

    @Transient
    private Boolean canceladoAgora = false;

    @JoinColumn(name = "ID_NFS", referencedColumnName = "ID")
    @ManyToOne
    private NFS idNFS;

    @Column(name = "ACRESCIMO")
    private Double acrescimo;

    @Column(name = "TARIFA")
    private Double tarifa;

    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @Transient
    private Double valorConciliado = 0d;

    @Transient
    private boolean selecionada = false;

    public ContaParcela() {
        this.fechada = "N";
    }

    public ContaParcela(Date dataVencimento, Double valorTotal, Double desconto, Double juros, Integer id) {
        this.fechada = "N";
        this.dataVencimento = dataVencimento;
        this.valorTotal = valorTotal;
        this.desconto = desconto;
        this.juros = juros;
        this.id = id;
    }

    public Double getValorPago() {
        if (valorPago == null) {
            return 0d;
        }
        return valorPago;
    }

    public Double getValorPendente() {
        if (valor != null && valorPago != null) {
            return valor - valorPago;
        }
        return valor;
    }

    public Double getPorcentagem() {
        return Math.abs(valorConciliado / valorTotal);
    }

    public Double getValorRestante() {
        return Math.max(valorTotal - Math.abs(valorConciliado), 0);
    }

    public String getIcone() {
        int porcentagem = (int) Math.ceil((getPorcentagem() * 10000));
        if (porcentagem == 0) {
            return "fa fa-times text-danger";
        } else if (porcentagem < 10000) {
            return "fa fa-check text-primary";
        }
        return "fa fa-check text-success";
    }

    public String getTitle() {
        int porcentagem = (int) Math.ceil((getPorcentagem() * 10000));
        if (porcentagem == 0) {
            return "Não conciliada";
        } else if (porcentagem < 10000) {
            return "Parcialmente conciliada";
        }
        return "Conciliada";
    }

    public ContaParcela appendObservacao(String obs) {
        this.observacao += obs;
        return this;
    }

    public Double getInputNumberMaxValue() {
        return valor > 0 ? valorRestante : 0d;
    }

    public Double getInputNumberMimValue() {
        return valor > 0 ? 0d : valorRestante;
    }

    @Override
    public String toString() {
        return "ContaParcela{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
