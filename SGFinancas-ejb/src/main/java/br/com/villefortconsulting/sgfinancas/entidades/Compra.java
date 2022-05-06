package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
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
import javax.persistence.TemporalType;
import javax.persistence.Transient;
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
@Table(name = "COMPRA")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@NoArgsConstructor
@Getter
@Setter
@Inheritance
public class Compra extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe o fornecedor")
    @JoinColumn(name = "ID_FORNECEDOR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Fornecedor idFornecedor;

    @NotNull(message = "Informe uma conta Bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_NATUREZA_OPERACAO", referencedColumnName = "ID")
    @ManyToOne
    private NaturezaOperacao idNaturezaOperacao;

    @JoinColumn(name = "ID_TRANSPORTADORA", referencedColumnName = "ID")
    @ManyToOne
    private Transportadora idTransportadora;

    @NotNull(message = "Informe um plano de conta")
    @JoinColumn(name = "ID_PLANO_CONTA", referencedColumnName = "ID")
    @ManyToOne
    private PlanoConta idPlanoConta;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto idCentroCusto;

    @NotNull(message = "Informe a data da compra")
    @Column(name = "DATA_COMPRA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCompra;

    @Column(name = "DATA_CANCELAMENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCancelamento;

    @NotNull(message = "Informe a data vencimento")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "PRAZO_RECEBIMENTO")
    private Date prazoRecebimento;

    @NotNull(message = "Informe a data pedido")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATA_PEDIDO")
    private Date dataPedido;

    @NotNull(message = "Informe a quantidade de parcelas")
    @Column(name = "NUM_PARCELA")
    private Integer numParcela;

    @NotNull(message = "Informe a condiçao de pagamento")
    @Size(max = 1, message = "Condição de pagamento com no máximo um caracter")
    @Column(name = "CONDICAO_PAGAMENTO")
    private String condicaoPagamento;

    @NotNull(message = "Informe a forma de pagamento")
    @Size(max = 3, message = "Forma de pagamento com no máximo três caracteres")
    @Column(name = "FORMA_PAGAMENTO")
    private String formaPagamento;

    @NotNull(message = "Informe o número")
    @Size(max = 10, message = "Informe o número do pedido")
    @Column(name = "NUMERO_PEDIDO")
    private String numeroPedido;

    @NotNull(message = "Informe o valor da compra")
    @Column(name = "VALOR_TOTAL")
    private Double valorTotal;

    @NotNull(message = "Informe a situação")
    @Column(name = "SITUACAO")
    @Size(max = 1, message = "Máximo de 1 caracter para situação")
    private String situacao;

    @Column(name = "VALOR_DESCONTO")
    private Double valorDesconto;

    @Column(name = "OBSERVACAO")
    @Size(max = 500, message = "Observação deverá ter entre 500 caracteres.")
    private String observacao;

    @Column(name = "SERIE")
    @Size(max = 50, message = "Série deverá ter entre 50 caracteres.")
    private String serie;

    @Column(name = "NUMERO_NF")
    @Size(max = 50, message = "Número NF deverá ter entre 50 caracteres.")
    private String numeroNf;

    @Column(name = "NUM_COMPRA")
    @Size(max = 50, message = "Número de compra deverá ter entre 50 caracteres.")
    private String numCompra;

    @Column(name = "PAGAR_FRETE")
    @Size(max = 1, message = "Pagamento de frete deverá ter entre 1 caracteres.")
    private String pagarFrete;

    @Column(name = "VALOR_FRETE")
    private Double valorFrete;

    @NotNull(message = "Informe ao menos 1 produto para compra")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCompra", orphanRemoval = true)
    private List<CompraProduto> listCompraProduto = new LinkedList<>();

    @NotNull(message = "Informe a conta")
    @JoinColumn(name = "ID_CONTA", referencedColumnName = "ID")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private Conta idConta;

    @Transient
    private List<ParcelaDTO> listParcelaDTO = new LinkedList<>();

    @Column(name = "N_REFERENCIA")
    private String nReferencia;

    // @NotNull(message = "Informe o plano de pagamento")
    @JoinColumn(name = "ID_PLANO_PAGAMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    private PlanoPagamento idPlanoPagamento;

    @Transient
    private Date dataPedidoImportacaoNFe;

    public void addProduto(CompraProduto compraProduto) {
        compraProduto.setIdCompra(this);
        listCompraProduto.add(compraProduto);
    }

    public void removeProduto(CompraProduto compraProduto) {
        listCompraProduto.remove(compraProduto);
    }

    @Override
    public String toString() {
        return "Compra{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
