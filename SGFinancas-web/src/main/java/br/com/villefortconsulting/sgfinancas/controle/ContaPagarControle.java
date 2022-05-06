package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ContaControleBase;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoListagemConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaPagarControle extends ContaControleBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FornecedorService fornecedorService;

    private Compra compraSelecionada;

    private Fornecedor fornecedorSelecionado;

    private EstatisticaContaDTO estisticaContaDTO = new EstatisticaContaDTO();

    private List<ContaParcela> parcelasAPagar = new LinkedList<>();

    private Integer pageParcCount = 1;

    private Integer pageParcEdit = 1;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contaService::quantidadeParcelasFiltradas,
                contaService::getListaParcelaFiltrada,
                filter -> {
                    filter.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
                    filter.setTipoListagem(tipoListagem);
                });
    }

    public String mostrarAjudaPagamentoParcial() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_PAGAR_CONTA_PARCELA_PARCIAL.getChave()).getDescricao());
        return "cadastroContaPagar.xhtml";
    }

    public String mostrarAjudaCancelamento() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CANCELAMENTO_PARCELA_PAGAR.getChave()).getDescricao());
        return "cancelarParcela.xhtml";
    }

    @Override
    public void preencherEstatisticaDTO() {
        cardAtraso = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.ATRASO);
        cardRecebido = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.PAGO);
        cardReceber = contaService.buscarEstatisticasConta(filtro, EnumTipoListagemConta.PAGAR);

        Long quantidadeTotal = cardReceber.getQuantidade() + cardRecebido.getQuantidade() + cardAtraso.getQuantidade();
        Double valorTotal = cardReceber.getValor() + cardRecebido.getValor() + cardAtraso.getValor();
        cardTotal = new CardContaDTO(valorTotal, quantidadeTotal);
        createPieModel2();
    }

    @Override
    public List<Conta> getListaConta() {
        return contaService.listarContaPagar();
    }

    @Override
    public String doStartAlterarConta() {
        cleanCodigoBarras();
        calcularTributosTotais(contaParcelaSelecionada);

        String retorno = super.doStartAlterarConta();
        this.pageParcCount = parcelas.size();
        this.pageParcEdit = contaParcelaSelecionada.getNumParcela();

        return retorno;
    }

    public void valorSerPago() {
        if ("N".equals(contaParcelaSelecionada.getPagamentoParcial())) {
            valorRecebido = contaParcelaSelecionada.getValor();
        } else {
            this.valorRecebido = (valorTotalPago == null) ? null : contaParcelaSelecionada.getValor() - valorTotalPago;
        }
    }

    public String doShowPagarParcela() {
        return "pagarParcela.xhtml";
    }

    public String pagarParcelasSelecionadas() {

        try {
            if (parcelasAPagar != null && !parcelasAPagar.isEmpty()) {
                contaService.pagarParcelas(parcelasAPagar);
                createFacesSuccessMessage("Parcela(s) paga(s) com sucesso.");
                parcelasAPagar = new LinkedList<>();
            } else {
                createFacesErrorMessage("Selecione ao menos uma parcela para ser paga!");
            }
        } catch (ContaException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "listaContaPagar.xhtml";
    }

    @Override
    public EnumConta getTipoConta() {
        return EnumConta.PAGAR;
    }

}
