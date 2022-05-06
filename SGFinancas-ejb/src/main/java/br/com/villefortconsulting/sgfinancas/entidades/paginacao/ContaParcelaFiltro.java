package br.com.villefortconsulting.sgfinancas.entidades.paginacao;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Arrays;
import java.util.Date;
import javax.ws.rs.core.MultivaluedMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaParcelaFiltro extends BasicFilter<ContaParcela> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Date dataInicio;

    private Date dataFim;

    /**
     * Data usada como referência apenas para o calendário do financeiro
     */
    private Date dataReferencia;

    private String tipoConta;

    private boolean mostraTransferencia = false;

    private String tipoTransacao;

    private ContaBancaria contaBancaria;

    private Double valor;

    private Double valorTotal;

    private PlanoConta planoConta;

    private String observacao;

    private Cliente cliente;

    private FormaPagamento formaPagamento;

    private CentroCusto centroCusto;

    private String tipoListagem;

    private Fornecedor fornecedor;

    private Double valorPago;

    private String origem;

    private String situacao;

    private boolean ehConciliacao = false;

    private boolean temNFS = false;

    @Override
    public void applyUrlInfo(MultivaluedMap<String, String> urlInfo) {
        super.applyUrlInfo(urlInfo);
        MinMax<Date> mm = getMinMax(getArray(urlInfo, "data"), Date.class);
        dataInicio = mm.getMin();
        dataFim = mm.getMax();
        if (urlInfo.getFirst("tipoTransacao") != null) {
            tipoTransacao = EnumTipoTransacao.retornaEnumSelecionado(urlInfo.getFirst("tipoTransacao")).getTipo();
        }
        if (urlInfo.getFirst("tipoConta") != null) {
            tipoConta = EnumTipoConta.retornaEnumSelecionado(urlInfo.getFirst("tipoConta")).getTipo();
        }
        if (urlInfo.getFirst("valor") != null) {
            valor = Double.parseDouble(urlInfo.getFirst("valor"));
        }
        if (urlInfo.getFirst("valorTotal") != null) {
            valorTotal = Double.parseDouble(urlInfo.getFirst("valorTotal"));
        }
        if (urlInfo.getFirst("observacao") != null) {
            observacao = urlInfo.getFirst("observacao");
        }
        if (urlInfo.getFirst("tipoListagem") != null) {
            tipoListagem = urlInfo.getFirst("tipoListagem");
        }
        boolean tipoValido = Arrays.asList("receber", "pagar", "atraso", "recebido", "pago", "hoje").stream()
                .anyMatch(tl -> tl.equals(tipoListagem));
        if (!tipoValido) {
            tipoListagem = null;
        }
        if (urlInfo.getFirst("valorPago") != null) {
            valorPago = Double.parseDouble(urlInfo.getFirst("valorPago"));
        }
        contaBancaria = initId(urlInfo.getFirst("contaBancaria"), new ContaBancaria());
        planoConta = initId(urlInfo.getFirst("planoConta"), new PlanoConta());
        fornecedor = initId(urlInfo.getFirst("fornecedor"), new Fornecedor());
        formaPagamento = initId(urlInfo.getFirst("formaPagamento"), new FormaPagamento());
        cliente = initId(urlInfo.getFirst("cliente"), new Cliente());
        if (urlInfo.getFirst("origem") != null) {
            String ori = urlInfo.getFirst("origem");
            if (ori.length() == 1) {
                origem = ori;
            } else {
                origem = EnumTipoConta.valueOf(urlInfo.getFirst("origem")).getTipo();
            }
        }
        if (urlInfo.getFirst("situacao") != null) {
            String sit = urlInfo.getFirst("situacao");
            situacao = (sit.length() == 2) ? sit : EnumSituacaoConta.valueOf(sit).getSituacao();
        }
    }

}
