package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Importavel(nome = "conta")
public class ImportacaoContaDTO extends DtoId implements Serializable, Comparable<ImportacaoContaDTO> {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Cliente", tipo = EnumTipoDadoImportacao.STRING)
    private String clienteStr;

    @Importavel(nome = "Fornecedor", tipo = EnumTipoDadoImportacao.STRING)
    private String fornecedorStr;

    @Importavel(nome = "Data de vencimento", tipo = EnumTipoDadoImportacao.DATE, obrigatorio = true)
    private Date dataVencimento;

    @Importavel(nome = "Data de emissão", tipo = EnumTipoDadoImportacao.DATE)
    private Date dataEmissao;

    @Importavel(nome = "Data de pagamento", tipo = EnumTipoDadoImportacao.DATE)
    private Date dataPagamento;

    @Importavel(nome = "Valor", tipo = EnumTipoDadoImportacao.DOUBLE, obrigatorio = true)
    private Double valor;

    @Importavel(nome = "Valor pago", tipo = EnumTipoDadoImportacao.DOUBLE)
    private Double valorPago;

    @Importavel(nome = "Plano de contas (Descrição)", tipo = EnumTipoDadoImportacao.STRING)
    private String planoContaStr;

    @Importavel(nome = "Plano de contas (Codigo)", tipo = EnumTipoDadoImportacao.STRING)
    private String codPlanoContaStr;

    @Importavel(nome = "Conta bancária", tipo = EnumTipoDadoImportacao.STRING, obrigatorio = true)
    private String contaBancariaStr;

    @Importavel(nome = "Centro de custo", tipo = EnumTipoDadoImportacao.STRING)
    private String centroCustoStr;

    @Importavel(nome = "Observações", tipo = EnumTipoDadoImportacao.STRING)
    private String observacao;

    @Importavel(nome = "Forma de pagamento", tipo = EnumTipoDadoImportacao.STRING)
    private String formaPagamentoStr;

    @Importavel(nome = "Parcelas", tipo = EnumTipoDadoImportacao.INTEGER, obrigatorio = true)
    private Integer qteParcelas;

    /*
        Campos auxiliares
     */
    private Integer numParcela;

    private Cliente cliente;

    private Fornecedor fornecedor;

    private PlanoConta planoConta;

    private ContaBancaria contaBancaria;

    private CentroCusto centroCusto;

    private FormaPagamento formaPagamento;

    public ImportacaoContaDTO updateFields(ClienteService clienteService, FornecedorService fornecedorService, PlanoContaService planoContaService, ContaBancariaService contaBancariaService, CentroCustoService centroCustoService, FormaPagamentoService formaPagamentoService, String tenantID) {
        if (!clienteStr.isEmpty()) {
            cliente = clienteService.findByCpfCnpjNome("", clienteStr);
        }
        if (!fornecedorStr.isEmpty()) {
            fornecedor = fornecedorService.findByRazaoSocial(fornecedorStr);
        }
        if (codPlanoContaStr != null) {
            if (!codPlanoContaStr.isEmpty()) {
                planoConta = planoContaService.obterPlanoContaPorCodigo(codPlanoContaStr, tenantID);
            }
        }
        if (planoConta == null) {
            planoConta = planoContaService.obterPlanoContaPorDescricao(planoContaStr.replaceFirst(" \\(\\d+/\\d+\\)", ""), tenantID);
        }
        contaBancaria = contaBancariaService.findByDescricao(contaBancariaStr);
        centroCusto = centroCustoService.findByDescricao(centroCustoStr);
        formaPagamento = formaPagamentoService.findByDescricao(formaPagamentoStr);

        numParcela = 1;
        if (qteParcelas == null) {
            qteParcelas = 1;
        }
        String[] configParcelas = planoContaStr.replaceAll(".* \\(|\\).*", "").split("/");
        if (configParcelas.length == 2) {
            if (configParcelas[0].matches("\\d+")) {
                numParcela = Integer.parseInt(configParcelas[0].trim());
            }
            if (configParcelas[1].matches("\\d+")) {
                qteParcelas = Integer.parseInt(configParcelas[1].trim());
            }
        }
        return this;
    }

    public boolean hasMissingFields() {
        return dataVencimento == null || valor == null
                || qteParcelas == null || numParcela == null
                || planoConta == null || contaBancaria == null
                || (cliente == null && fornecedor == null);
    }

    public String beginErrorString() {
        StringBuilder sb = new StringBuilder();
        sb.append("A linha com o ");
        if (cliente != null) {
            sb.append("cliente ").append(cliente.getNome());
        } else if (fornecedor != null) {
            sb.append("fornecedor ").append(fornecedor.getRazaoSocial());
        } else {
            sb.append("cliente e fornecedor vazios ");
        }
        sb.append(", com a data de vencimento ").append(DataUtil.dateToString(dataVencimento))
                .append(" e a conta bancária ").append(contaBancariaStr)
                .append(" não pode ser processada pois ");
        return sb.toString();
    }

    public String toErrorString() {
        StringBuilder sb = new StringBuilder();
        sb.append(beginErrorString())
                .append(" o ");
        final String difere = " difere nas linhas de uma mesma conta.";
        if (cliente != null) {
            sb.append("cliente").append(difere);
        } else if (fornecedor != null) {
            sb.append("fornecedor").append(difere);
        } else {
            sb.append("cliente ou o fornecedor devem ser preenchidos e se manterem em todas as linhas de uma mesma conta. ");
        }

        return sb.toString();
    }

    @Override
    public int compareTo(ImportacaoContaDTO o) {
        int order;
        if (!this.planoConta.equals(o.planoConta)) {
            order = this.planoConta.getDescricao().compareTo(o.planoConta.getDescricao());
            if (order != 0) {
                return order;
            }
        }
        if (this.fornecedor != null && !this.fornecedor.equals(o.fornecedor)) {
            order = this.fornecedor.getRazaoSocial().compareTo(o.fornecedor.getRazaoSocial());
            if (order != 0) {
                return order;
            }
        }
        if (this.cliente != null && !this.cliente.equals(o.cliente)) {
            order = this.cliente.getNome().compareTo(o.cliente.getNome());
            if (order != 0) {
                return order;
            }
        }
        return this.numParcela.compareTo(o.numParcela);
    }

}
