package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

@Getter
public class TimelineDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Date dataVencimento;

    private final Date dataPagamento;

    private final String tipoTransacao;

    private final Double valor;

    private final Double valorPago;

    private final String cliente;

    private final String fornecedor;

    private final String observacao;

    private final String planoConta;

    public TimelineDTO(Date dataVencimento, Date dataPagamento, String tipoTransacao, Double valor, Double valorPago, Cliente cliente, Fornecedor fornecedor, String observacao, PlanoConta idPlanoConta) {
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.tipoTransacao = tipoTransacao;
        this.valor = valor;
        this.valorPago = valorPago;
        this.cliente = cliente == null ? "" : cliente.getNome();
        this.fornecedor = fornecedor == null ? "" : fornecedor.getRazaoSocial();
        this.observacao = observacao;
        this.planoConta = " (" + idPlanoConta.getDescricao() + ")";
    }

    public Date getData() {
        return valorPago != null ? dataPagamento : dataVencimento;
    }

    public String getTitle() {
        return "Parcela a " + ("P".equals(tipoTransacao) ? "pagar" : "receber");
    }

    public Double getValor() {
        return valorPago != null ? valorPago : valor;
    }

    public String getNomeEvento() {
        final boolean foiPago = valorPago != null;
        final boolean aPagar = EnumTipoTransacao.PAGAR.getTipo().equals(tipoTransacao);
        final String valorStr = "R$ " + NumeroUtil.converterValorParaMonetario(getValor(), 2) + " ";

        String pessoa = null;
        if (StringUtils.isNotEmpty(cliente)) {
            pessoa = formatNomePessoa(cliente, "cliente", "cliente");
        } else if (StringUtils.isNotEmpty(fornecedor)) {
            pessoa = formatNomePessoa(fornecedor, "fornecedor", "fornecedora");
        }
        if (foiPago) {
            if (pessoa == null) {
                return valorStr + (aPagar ? "pago" : "recebido") + planoConta;
            }
            return valorStr + (aPagar ? "pago para " : "recebido d") + pessoa;
        }
        if (pessoa == null) {
            return valorStr + (aPagar ? "a pagar" : "a receber") + planoConta;
        }
        return valorStr + (aPagar ? "a pagar para " : "a receber d") + pessoa;
    }

    private static String formatNomePessoa(String nome, String masculino, String feminino) {
        return (nome.split(" ")[0].trim().endsWith("a") ? "a " + feminino : "o " + masculino) + " " + nome;
    }

}
