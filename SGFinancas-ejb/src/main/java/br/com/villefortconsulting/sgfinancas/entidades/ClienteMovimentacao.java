package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
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
import javax.validation.Valid;
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
@Table(name = "CLIENTE_MOVIMENTACAO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
@NoArgsConstructor
public class ClienteMovimentacao extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @JoinColumn(name = "ID_CLIENTE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Cliente idCliente;

    @Column(name = "ORIGEM")
    @NotNull(message = "Informe a origem")
    private String origem;

    @Column(name = "ID_INTEGRACAO")
    private String idIntegracao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_MOVIMENTACAO")
    @NotNull(message = "Informe a data de movimentação")
    private Date dataMovimentacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_VENCIMENTO")
    @NotNull(message = "Informe a data de vencimento")
    private Date dataVencimento;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_PAGAMENTO")
    private Date dataPagamento;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_CANCELAMENTO")
    private Date dataCancelamento;

    @Column(name = "VALOR_PREVISTO")
    @NotNull(message = "Informe o valor previsto")
    private Double valorPrevisto;

    @Column(name = "VALOR_JUROS")
    private Double valorJuros;

    @Column(name = "VALOR_MULTAS")
    private Double valorMultas;

    @Column(name = "VALOR_PAGO")
    private Double valorPago;

    @Column(name = "VALOR_SALDO")
    private Double valorSaldo;

    @Column(name = "VALOR_SALDO_ANTERIOR")
    private Double valorSaldoAnterior;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "TIPO_MOVIMENTACAO")
    private String tipoMovimentacao;

    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne(optional = true)
    private CentroCusto idCentroCusto;

    @Column(name = "VALOR_TAXA")
    private Double valorTaxa;

    @NotNull
    @Size(max = 1)
    @Column(name = "ATIVO")
    private String ativo = "S";

    @Column(name = "QUANTIDADE_PARCELAS")
    private Integer quantidadeParcelas;

    @Column(name = "DATA_LIQUIDACAO")
    private Date dataLiquidacao;

    @Valid
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idClienteMovimentacao", orphanRemoval = true)
    private List<ClienteMovimentacaoAlteracao> clienteMovimentacaoAlteracaoList = new LinkedList<>();

    public EnumTipoClienteMovimentacao getEnumOrigem() {
        return EnumTipoClienteMovimentacao.retornaEnumSelecionado(origem);
    }

    public Double getValorTitulo() {
        if (valorPrevisto == 0d) {
            return valorPrevisto;
        }
        return (getEnumOrigem().isInverteValor() ? -1 : 1) * valorPrevisto;
    }

    public Double getValorPagoCorrigido() {
        if (valorPago == null || valorPago == 0d) {
            return 0d;
        }
        return -valorPago;
    }

    public Double getValor() {
        if (!"S".equals(ativo)) {
            return 0d;
        }
        EnumTipoClienteMovimentacao tipo = getEnumOrigem();
        if (tipo == EnumTipoClienteMovimentacao.SALDO_INICIAL || tipo == EnumTipoClienteMovimentacao.CORRECAO) {
            return valorPrevisto;
        }
        if (dataCancelamento != null || valorPrevisto == null
                || (dataPagamento == null && (EnumTipoClienteMovimentacao.IUGU == tipo || EnumTipoClienteMovimentacao.SAIDA_LANCADA == tipo))) {
            return 0d;
        }
        return -(dataPagamento != null ? valorPrevisto : -valorPrevisto);
    }

}
