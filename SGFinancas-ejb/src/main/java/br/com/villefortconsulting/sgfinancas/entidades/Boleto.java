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
@Table(name = "BOLETO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class Boleto extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o nosso número")
    @Column(name = "NOSSO_NUMERO")
    private String nossoNumero;

    @NotNull(message = "Informe se o boleto foi processado")
    @Column(name = "PROCESSADA")
    private String processada;

    @NotNull(message = "Informe o numero da via")
    @Column(name = "NUMERO_VIA")
    private Integer numeroVia;

    @NotNull(message = "Informe a data de emissão")
    @Column(name = "DATA_EMISSAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEmissao;

    @Column(name = "DATA_PAGAMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataPagamento;

    @Column(name = "DATA_DESCONTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataDesconto;

    @NotNull(message = "Informe a data de vencimento")
    @Column(name = "DATA_VENCIMENTO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataVencimento;

    @Column(name = "DATA_MORA")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataMora;

    @NotNull(message = "Informe o valor")
    @Column(name = "VALOR")
    private Double valor;

    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @Column(name = "VALOR_DESCONTO")
    private Double valorDesconto;

    @Column(name = "VALOR_IOF")
    private Double valorIof;

    @Column(name = "VALOR_ABATIMENTO")
    private Double valorAbatimento;

    @Column(name = "JUROS")
    private Double juros;

    @Column(name = "DIAS_PROTESTO")
    private Integer diasProtesto;

    @NotNull(message = "Informe se boleto ativo")
    @Size(max = 1, message = "Máximo de 1 caracter para o campo ativo")
    @Column(name = "ATIVO")
    private String ativo;

    @NotNull(message = "Informe se boleto pago")
    @Size(max = 1, message = "Máximo de 1 caracter para o campo pago")
    @Column(name = "PAGO")
    private String pago;

    @NotNull(message = "Informe se boleto gerou remessa")
    @Size(max = 1, message = "Máximo de 1 caracter para o campo gerou remessa")
    @Column(name = "GEROU_REMESSA")
    private String gerouRemessa;

    @NotNull(message = "Informe o sacado")
    @JoinColumn(name = "ID_CLIENTE_SACADO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idClienteSacado;

    @NotNull(message = "Informe a conta")
    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaParcela idContaParcela;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_REMESSA", referencedColumnName = "ID")
    @ManyToOne
    private Remessa idRemessa;

    @NotNull(message = "Informe o documento")
    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Documento idDocumento;

    public Boleto() {
        this.ativo = "N";
        this.pago = "N";
        this.gerouRemessa = "N";
        this.processada = "N";
        this.numeroVia = 1;
    }

    public Double getPercentualJuros() {
        if (getJuros() != null) {
            return (getJuros() * 100) / getValor();
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "Boleto{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
