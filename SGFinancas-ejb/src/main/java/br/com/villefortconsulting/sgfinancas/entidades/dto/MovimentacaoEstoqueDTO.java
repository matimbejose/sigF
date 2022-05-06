package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEntradaProduto;
import br.com.villefortconsulting.util.DataUtil;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoqueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricaoProduto;

    private String descricaoUnidade;

    private Double saldoAnterior = 0d;

    private Double quantidade = 0d;

    private Double saldo = 0d;

    private String origem;

    private String numNF;

    private String numNFCompra;

    private String fornecedorCliente;

    private Double custo = 0d;

    private Double valor = 0d;

    private Date dataMovimentacao;

    private String tipoOperacao;

    private String unidMedida;

    private Double qteCusto = 0d;

    private Double qteValor = 0d;

    private Double estoqueMinimo = 0d;

    private Integer idProduto;

    public MovimentacaoEstoqueDTO(String descricaoProduto, String descricaoUnidade, Integer idProduto, double saldo, String origem, double custo, double valor, double qteCusto, double qteValor, double estoqueMinimo) {
        this.descricaoProduto = descricaoProduto;
        this.descricaoUnidade = descricaoUnidade;
        this.idProduto = idProduto;
        this.saldo = saldo;
        this.origem = origem;
        this.custo = custo;
        this.valor = valor;
        this.qteCusto = qteCusto;
        this.qteValor = qteValor;
        this.estoqueMinimo = estoqueMinimo;
    }

    public MovimentacaoEstoqueDTO(Estoque estoque) {
        descricaoProduto = estoque.getIdProduto().getDescricao();
        quantidade = estoque.getQuantidade();
        saldo = estoque.getSaldo();
        saldoAnterior = estoque.getSaldoAnterior();
        custo = estoque.getIdProduto().getValorCusto();
        dataMovimentacao = DataUtil.removerHoras(estoque.getData());
        tipoOperacao = estoque.getTipoOperacao();
        unidMedida = estoque.getIdProduto().getIdUnidadeMedida().getSigla();
        if (estoque.getIdCompraProduto() == null && estoque.getIdVendaProduto() == null && "S".equals(estoque.getPrimeiroCadastro())) {
            origem = "Estoque inicial";
        } else if (estoque.getIdCompraProduto() == null && estoque.getIdVendaProduto() == null && estoque.getPrimeiroCadastro() == null && EnumTipoEntradaProduto.IMPORTACAO_ENTRADA.getTipo().equals(estoque.getIdProduto().getUltimaEntrada())) {
            origem = "Inventário";
        } else if (estoque.getIdCompraProduto() == null && estoque.getIdVendaProduto() == null && estoque.getPrimeiroCadastro() == null && EnumTipoEntradaProduto.IMPORTACAO_SAIDA.getTipo().equals(estoque.getIdProduto().getUltimaEntrada())) {
            origem = "Saída lançada";
        } else if (estoque.getIdCompraProduto() == null && estoque.getIdVendaProduto() != null) {
            List<NF> nfs = estoque.getIdVendaProduto().getIdVenda().getNfList();
            if (nfs != null && !nfs.isEmpty()) {
                numNF = String.valueOf(nfs.get(nfs.size() - 1).getNumeroNotaFiscal());
            }
            fornecedorCliente = estoque.getIdVendaProduto().getIdVenda().getIdCliente().getNome();
            valor = estoque.getIdVendaProduto().getDadosProduto().getValor();
            custo = 0d;
            origem = "Venda";
        } else if (estoque.getIdCompraProduto() != null && estoque.getIdVendaProduto() == null) {
            if (estoque.getIdCompraProduto().getIdCompra().getNumeroNf() != null) {
                numNF = estoque.getIdCompraProduto().getIdCompra().getNumeroNf();
            }
            fornecedorCliente = estoque.getIdCompraProduto().getIdCompra().getIdFornecedor().getRazaoSocial();
            custo = estoque.getIdCompraProduto().getDadosProduto().getValor();
            origem = "Compra";
        } else {
            if (tipoOperacao.equals("SA")) {
                origem = "Lançamento saída avulsa";
                valor = estoque.getIdProduto().getValorVenda();
                custo = 0d;
            } else {
                origem = "Lançamento entrada avulsa";
            }
        }
    }

    public Double getCustoUnitario() {
        return custo;
    }

    public Double getValorUnitario() {
        return valor;
    }

}
