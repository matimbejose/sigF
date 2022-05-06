package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorParcelaDTO;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.Valid;
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
@Table(name = "CONTA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Conta extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o tipo de conta")
    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @NotNull(message = "Informe o tipo de transação")
    @Column(name = "TIPO_TRANSACAO")
    private String tipoTransacao;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_VENCIMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @Column(name = "DATA_PAGAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    private String situacao;

    @NotNull(message = "Informe se deseja a repetição da conta")
    @Column(name = "REPETIR_CONTA")
    private String repetirConta;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCancelamento;

    @NotNull(message = "Informe se deseja informar contas recebidas")
    @Column(name = "INFORMAR_PAGAMENTO")
    @Size(max = 1, message = "Máximo de 1 caracter para informar pagamento")
    private String informarPagamento;

    @Column(name = "TIPO_REPETICAO")
    @Size(max = 1, message = "Máximo de 1 caracter para tipo de repetição")
    private String tipoRepeticao;

    @Column(name = "ADVEM_CONTRATO")
    @Size(max = 1, message = "Máximo de 1 caracter para saber se advem de contrato")
    private String advemContrato;

    @Column(name = "QTD_REPETICAO")
    private Integer qtdRepeticao;

    @Column(name = "OBSERVACAO")
    @Size(max = 500, message = "Máximo de 500 caracteres para observação")
    private String observacao;

    @JoinColumn(name = "ID_CONTRATO", referencedColumnName = "ID")
    @ManyToOne
    private Contrato idContrato;

    @NotNull(message = "Informe o valor da conta")
    @Column(name = "VALOR")
    private Double valor;

    @NotNull(message = "Informe o valor total da conta")
    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @NotNull(message = "Informe o numero de parcelas")
    @Column(name = "NUMERO_PARCELAS")
    private Integer numeroParcelas;

    @Column(name = "MOTIVO_CANCELAMENTO")
    @Size(max = 500, message = "Máximo de 500 caracteres para motivo do cancelamento")
    private String motivoCancelamento;

    @Column(name = "OUTROS_CUSTOS")
    private Double outrosCustos;

    @Column(name = "JUROS")
    private Double juros;

    @Column(name = "MULTA")
    private Double multa;

    @Column(name = "DESCONTO")
    private Double desconto;

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

    @Column(name = "VALOR_ICMS")
    private Double valorICMS;

    @Column(name = "TARIFA")
    private Double tarifa;

    @OneToOne(mappedBy = "idConta")
    private Contrato contrato;

    @OneToOne(mappedBy = "idConta")
    private Compra compra;

    @OneToOne(mappedBy = "idConta")
    private Venda venda;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne
    private Cliente idCliente;

    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne
    private Fornecedor idFornecedor;

    @JoinColumn(name = "ID_UNIDADE_OCUPACAO", referencedColumnName = "ID")
    @ManyToOne
    private UnidadeOcupacao idUnidadeOcupacao;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private PlanoConta idPlanoConta;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @JoinColumn(name = "ID_CLIENTE_MOVIMENTACAO", referencedColumnName = "ID")
    @ManyToOne
    private ClienteMovimentacao idClienteMovimentacao;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne
    private Documento idDocumento;

    @JoinColumn(name = "ID_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne
    private TipoPagamento idTipoPagamento;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idConta", orphanRemoval = true)
    private List<ContaParcela> contaParcelaList = new LinkedList<>();

    @Transient
    private String pagamentoParcial;

    @Transient
    private FormaPagamento formaPagamento;

    @Transient
    private String tipoRepeticaoParcelas = "";

    @Transient
    private List<ValorParcelaDTO> listaValorParcela = new ArrayList<>();

    public Conta(Integer id) {
        this.id = id;
    }

    public Conta() {
        this.tipoConta = EnumTipoConta.NORMAL.getTipo();
        this.situacao = EnumSituacaoConta.NAO_PAGA.getSituacao();
        this.repetirConta = "N";
        this.informarPagamento = "N";
        this.numeroParcelas = 1;
    }

    public Conta(String tipoTransacao) {
        this.tipoConta = EnumTipoConta.NORMAL.getTipo();
        this.situacao = EnumSituacaoConta.NAO_PAGA.getSituacao();
        this.repetirConta = "N";
        this.informarPagamento = "N";
        this.numeroParcelas = 1;
        this.tipoTransacao = tipoTransacao;
    }

    public Double getValorPago() {
        if (valorPago == null) {
            return 0d;
        }
        return valorPago;
    }

    public void setRepetirConta(String repetirConta) {
        if ("false".equals(repetirConta)) {
            repetirConta = "N";
        } else if ("true".equals(repetirConta)) {
            repetirConta = "S";
        }
        this.repetirConta = repetirConta;
    }

    public void setInformarPagamento(String informarPagamento) {
        if ("false".equals(informarPagamento)) {
            informarPagamento = "N";
        } else if ("true".equals(informarPagamento)) {
            informarPagamento = "S";
        }
        this.informarPagamento = informarPagamento;
    }

    public void addContaParcela(ContaParcela parcela) {
        parcela.setIdConta(this);
        contaParcelaList.add(parcela);
    }

    public void addContaParcela(ContaParcela parcela, String tenatID) {
        parcela.setIdConta(this);
        parcela.setTenatID(tenatID);
        contaParcelaList.add(parcela);
    }

    public void removeContaParcela(ContaParcela parcela) {
        contaParcelaList.remove(parcela);
    }

    public Double getTributosTotais() {
        return NumeroUtil.somar(
                juros,
                encargos,
                multa,
                outrosCustos,
                valorIR,
                valorPIS,
                valorCSLL,
                valorINSS,
                valorCOFINS,
                valorISS,
                valorICMS);
    }

    public Boolean getNaoPodeSelecionarQteParcelas() {
        return idTipoPagamento != null && "N".equals(idTipoPagamento.getPermiteParcelamento());
    }

    @Override
    public String toString() {
        return "Conta{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
