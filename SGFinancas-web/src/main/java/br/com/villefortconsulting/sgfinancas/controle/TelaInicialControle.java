package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResultadoPlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TimelineDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AnalisesService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoListagemConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
public class TelaInicialControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ContaService contaService;

    @EJB
    private AnalisesService analisesService;

    @Inject
    private ContaReceberControle contaReceberControle;

    @Inject
    private ContaPagarControle contaPagarControle;

    private List<ResultadoPlanoContaLancamentoDTO> listaIndicadores;

    private String analiseFinanceira;

    private String contaPagarReceber;

    private String venda;

    private EstatisticaContaDTO pagamento;

    private EstatisticaContaDTO pagamentoTotal;

    private EstatisticaContaDTO recebimento;

    private CardContaDTO cardRecebimento;

    private CardContaDTO cardPagamento;

    private CardContaDTO cardCreditoAtraso;

    private CardContaDTO cardDebitoAtraso;

    private CardContaDTO cardSaldo;

    private CardContaDTO cardVendas;

    private CardContaDTO cardPagamentoAtraso;

    private CardContaDTO cardRecebimentoPendente;

    private List<TimelineDTO> timelineRecebido;

    private List<TimelineDTO> timelineReceber;

    private EstatisticaContaDTO recebimentoTotal;

    private MinMax<Date> data = new MinMax<>(true);

    private String competencia;

    private String nomeCompetencia;

    public TelaInicialControle() {
        listaIndicadores = new ArrayList<>();
        data.setMin(DataUtil.getPrimeiroDiaDoAnoCorrente());
        data.setMax(DataUtil.getUltimoDiaDoAnoCorrente());
    }

    public void obterValoresParaGrafico() {
        ContaParcelaFiltro filtro = new ContaParcelaFiltro();
        filtro.setDataInicio(data.getMin());
        filtro.setDataFim(data.getMax());

        // atualiza filtros nas telas de contas a pagar e contas a receber
        contaReceberControle.getFiltro().setDataInicio(data.getMin());
        contaReceberControle.getFiltro().setDataFim(data.getMax());

        contaPagarControle.getFiltro().setDataInicio(data.getMin());
        contaPagarControle.getFiltro().setDataFim(data.getMax());

        filtro.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());

        filtro.setTipoListagem(EnumTipoListagemConta.RECEBER.getTipo());
        cardRecebimento = contaService.buscarEstatisticasContaParcela(filtro);

        filtro.setTipoListagem(EnumTipoListagemConta.ATRASO.getTipo());
        cardCreditoAtraso = contaService.buscarValorContaAtrasoPeriodo(filtro);

        filtro.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
        filtro.setTipoListagem(EnumTipoListagemConta.RECEBER.getTipo());
        cardPagamento = contaService.buscarEstatisticasContaParcela(filtro);

        filtro.setTipoListagem(EnumTipoListagemConta.ATRASO.getTipo());
        cardDebitoAtraso = contaService.buscarValorContaAtrasoPeriodo(filtro);

        Double valorSaldo = cardRecebimento.getValor() - cardPagamento.getValor();

        cardSaldo = new CardContaDTO(valorSaldo, 0L);

        analiseFinanceira = analisesService.obterFluxoCaixaJSon(data.getMin(), data.getMax());
        contaPagarReceber = analisesService.obterAnaliseNecessidadeCaixaJson(data.getMin(), data.getMax());
        venda = analisesService.obterVendaJson(data.getMin(), data.getMax());

        listaIndicadores = planoContaService.obterIndicadorTelaInicial(data.getMin(), data.getMax());
    }

    public void obterValoresParaGraficoIndex() {
        ContaParcelaFiltro filtro = new ContaParcelaFiltro();
        if (competencia == null) {
            competencia = "M";
        }
        switch (competencia) {
            case "M":
                filtro.setDataInicio(DataUtil.getPrimeiroDiaDoMes(new Date()));
                filtro.setDataFim(DataUtil.getUltimoDiaDoMes(new Date()));
                nomeCompetencia = "do mês";
                break;
            case "S":
                filtro.setDataInicio(DataUtil.getPrimeiroDiaDaSemana(new Date()));
                filtro.setDataFim(DataUtil.getUltimoDiaDaSemana(new Date()));
                nomeCompetencia = "da semana";
                break;
            case "H":
                filtro.setDataInicio(new Date());
                filtro.setDataFim(new Date());
                nomeCompetencia = "de hoje";
                break;
            default:
                filtro.setDataInicio(DataUtil.getPrimeiroDiaDoMes(new Date()));
                filtro.setDataFim(DataUtil.getUltimoDiaDoMes(new Date()));
                break;
        }

        timelineRecebido = contaService.getTimeline(filtro.getDataInicio(), filtro.getDataFim(), true);
        timelineReceber = contaService.getTimeline(filtro.getDataInicio(), filtro.getDataFim(), false);

        cardVendas = new CardContaDTO();
        timelineRecebido.stream()
                .filter(tl -> "R".equals(tl.getTipoTransacao()))
                .forEach(tl -> cardVendas.add(tl.getValorPago()));

        timelineReceber.stream()
                .filter(tl -> "R".equals(tl.getTipoTransacao()))
                .forEach(tl -> cardVendas.add(tl.getValor()));

        cardSaldo = new CardContaDTO().sum(cardVendas);

        timelineRecebido.stream()
                .filter(tl -> "P".equals(tl.getTipoTransacao()))
                .forEach(tl -> cardSaldo.remove(tl.getValorPago()));

        timelineReceber.stream()
                .filter(tl -> "P".equals(tl.getTipoTransacao()))
                .forEach(tl -> cardSaldo.remove(tl.getValor()));
    }

}
