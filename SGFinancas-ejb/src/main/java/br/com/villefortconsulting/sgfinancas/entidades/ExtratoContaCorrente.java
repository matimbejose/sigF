package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
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
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "EXTRATO_CONTA_CORRENTE")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@Inheritance
public class ExtratoContaCorrente extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Informe a conta bancária")
    @JoinColumn(name = "ID_CONTA_BANCARIA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private ContaBancaria idContaBancaria;

    @JoinColumn(name = "ID_CONTA_PARCELA", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcela idContaParcela;

    @JoinColumn(name = "ID_CONTA_PARCELA_PARCIAL", referencedColumnName = "ID")
    @ManyToOne
    private ContaParcelaParcial idContaParcelaParcial;

    @NotNull(message = "Informe a data de cadastro")
    @Column(name = "DATA_CADASTRO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataCadastro;

    @NotNull(message = "Informe a data de movimentação")
    @Column(name = "DATA_MOVIMENTACAO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataMovimentacao;

    @NotNull(message = "Informe o valor")
    @Column(name = "VALOR")
    private Double valor;

    @NotNull(message = "Informe o saldo anterior")
    @Column(name = "SALDO_ANTERIOR")
    private Double saldoAnterior;

    @NotNull(message = "Informe o saldo")
    @Column(name = "SALDO")
    private Double saldo;

    @NotNull(message = "Informe o tipo")
    @Column(name = "TIPO")
    private String tipo;

    public String getDescricao() {
        if (this.idContaParcela != null) {
            if(this.idContaParcela.getIdConta().getIdPlanoConta() != null){
                return this.idContaParcela.getIdConta().getIdPlanoConta().getDescricao();
            }else{
                return "";
            }
        } else if (this.idContaParcelaParcial != null) {
            return this.idContaParcelaParcial.getIdContaParcela().getIdConta().getIdPlanoConta().getDescricao();
        }
        return "Saldo inicial";
    }

    public String getOrigem() {
        if (this.idContaParcela != null) {

            if (EnumTipoConta.TRANSFERENCIA.getTipo().equals(this.idContaParcela.getIdConta().getTipoConta())) {
                return "Transferência";
            } else if (EnumTipoConta.LANCAMENTO_CONTABIL.getTipo().equals(this.idContaParcela.getIdConta().getTipoConta())) {
                return "Lançamento";
            } else if (EnumTipoTransacao.PAGAR.getTipo().equals(this.idContaParcela.getIdConta().getTipoTransacao())) {
                return "Pagamento";
            } else if (EnumTipoTransacao.RECEBER.getTipo().equals(this.idContaParcela.getIdConta().getTipoTransacao())) {
                return "Recebimento";
            }
        } else if (this.idContaParcelaParcial != null) {
            if (EnumTipoConta.TRANSFERENCIA.getTipo().equals(this.idContaParcelaParcial.getIdContaParcela().getIdConta().getTipoConta())) {
                return "Transferência";
            } else if (EnumTipoConta.LANCAMENTO_CONTABIL.getTipo().equals(this.idContaParcelaParcial.getIdContaParcela().getIdConta().getTipoConta())) {
                return "Lançamento";
            } else if (EnumTipoTransacao.PAGAR.getTipo().equals(this.idContaParcelaParcial.getIdContaParcela().getIdConta().getTipoTransacao())) {
                return "Pagamento parcial";
            } else if (EnumTipoTransacao.RECEBER.getTipo().equals(this.idContaParcelaParcial.getIdContaParcela().getIdConta().getTipoTransacao())) {
                return "Recebimento parcial";
            }
        }
        return "Abertura de conta";
    }

    @Override
    public String toString() {
        return "ExtratoContaCorrente{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

}
