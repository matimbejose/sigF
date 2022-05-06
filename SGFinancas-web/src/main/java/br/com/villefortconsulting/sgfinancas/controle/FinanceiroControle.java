package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControleFinanceiroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FinanceiroService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JRException;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
public class FinanceiroControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FinanceiroService financeiroService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    private static final String[] MESES = new String[]{
        "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
    };

    private Integer ano;

    private Integer mes;

    private Cliente cliente;

    private List<ControleFinanceiroDTO> modelRecebimento = new ArrayList<>();

    private List<ControleFinanceiroDTO> modelGasto = new ArrayList<>();

    private transient ScheduleModel model;

    private ContaParcela contaParcelaSelecionada;

    private ContaParcelaFiltro filtro;

    private String action;

    public FinanceiroControle() {
        mes = DataUtil.getMes(DataUtil.getHoje()) - 1;
        ano = DataUtil.getAno(DataUtil.getHoje());
    }

    @PostConstruct
    protected void postConstruct() {
        updateTables();
        filtro = new ContaParcelaFiltro();
        action = "today";
        navigate();
    }

    private DefaultScheduleEvent contaParcelaToDefaultScheduleEvent(ContaParcela item) {
        boolean isPagar = item.getIdConta().getTipoTransacao().equals(EnumTipoTransacao.PAGAR.getTipo());
        boolean isSameMonth = DataUtil.getMes(filtro.getDataReferencia()).equals(DataUtil.getMes(item.getDataVencimento()));
        String desc = item.getIdConta().getIdPlanoConta().getDescricao()
                + "\r\n"
                + "R$ " + NumeroUtil.converterValorParaMonetario(item.getValor(), 2)
                + " - "
                + EnumSituacaoConta.retornaEnumSelecionado(item.getSituacao()).getDescricaoSituacao()
                + "\r\n"
                + "Conta a " + (isPagar ? "pagar" : "receber");
        DefaultScheduleEvent evt = new DefaultScheduleEvent();
        evt.setAllDay(true);
        evt.setData(item);
        evt.setDescription(desc);
        evt.setEditable(false);
        evt.setEndDate(item.getDataVencimento());
        evt.setStartDate(item.getDataVencimento());
        evt.setStyleClass((isPagar ? "pagar" : "receber") + (isSameMonth ? "" : " other-month") + (" sit-" + item.getSituacao()));
        evt.setTitle(evt.getDescription());
        return evt;
    }

    private static boolean compareEvents(ScheduleEvent evt, ContaParcela cp) {
        if (evt == null || evt.getData() == null || cp == null || !(evt.getData() instanceof ContaParcela)) {
            return false;
        }
        ContaParcela doEvento = (ContaParcela) evt.getData();
        return cp.getId().equals(doEvento.getId());
    }

    public ScheduleModel getModel() {
        if (model == null) {
            model = new DefaultScheduleModel();
        }
        List<ContaParcela> listaItens = contaService.getListaParcelaFiltrada(filtro);
        for (int i = model.getEventCount() - 1; i >= 0; i--) {
            ScheduleEvent evt = model.getEvents().get(i);
            if (listaItens.stream().noneMatch(cp -> compareEvents(evt, cp))) {
                model.deleteEvent(evt);
            }
        }
        listaItens.forEach(cp -> {
            DefaultScheduleEvent evt = contaParcelaToDefaultScheduleEvent(cp);
            ScheduleEvent temEvent = model.getEvents().stream().filter(aux -> compareEvents(aux, cp)).findAny().orElse(null);
            if (temEvent == null) {
                model.addEvent(evt);
            } else {
                evt.setId(temEvent.getId());
                model.updateEvent(evt);
            }
        });
        return model;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        ScheduleEvent se = (ScheduleEvent) selectEvent.getObject();
        contaParcelaSelecionada = null;
        if (se != null) {
            contaParcelaSelecionada = (ContaParcela) se.getData();
        }
    }

    public void navigate() {
        Date data;
        switch (action) {
            case "prev":
                data = DataUtil.subtrairDias(filtro.getDataInicio(), 1);
                break;
            case "next":
                data = DataUtil.adicionarDias(filtro.getDataFim(), 1);
                break;
            case "today":
                data = new Date();
                break;
            default:
                return;
        }
        filtro.setDataReferencia(data);
        filtro.setDataInicio(DataUtil.getPrimeiroDiaDaSemana(DataUtil.getPrimeiroDiaDoMes(data)));
        filtro.setDataFim(DataUtil.getUltimoDiaDaSemana(DataUtil.getUltimoDiaDoMes(data)));
    }

    public void updateTables() {
        if (cliente != null) {
            modelRecebimento = financeiroService.listarRecebimentos(mes, ano, cliente.getNome());
            modelGasto = financeiroService.listarGastos(mes, ano, cliente.getNome());
        }
    }

    public void setNomeMes(String mesStr) {
        if (mesStr != null) {
            for (int i = 0; i < MESES.length; i++) {
                if (mesStr.equals(MESES[i])) {
                    mes = i;
                    return;
                }
            }
            mes = -1;
        }
    }

    public String getNomeMes() {
        if (mes != null && mes >= 0 && mes < MESES.length) {
            return MESES[mes];
        }
        return "Mês não informado";
    }

    public Double getTotalRecebimento() {
        return modelRecebimento.stream()
                .mapToDouble(ControleFinanceiroDTO::getValor).sum();
    }

    public Double getTotalGasto() {
        return modelGasto.stream()
                .mapToDouble(ControleFinanceiroDTO::getValor).sum();
    }

    public Double getTotalRecebimentoPlanoConta(ControleFinanceiroDTO cfDTO) {
        return modelRecebimento.stream()
                .filter(item -> item.getNomePlanoConta().equals(cfDTO.getNomePlanoConta()))
                .mapToDouble(ControleFinanceiroDTO::getValor).sum();
    }

    public Double getTotalGastoPlanoConta(ControleFinanceiroDTO cfDTO) {
        return modelGasto.stream()
                .filter(item -> item.getNomePlanoConta().equals(cfDTO.getNomePlanoConta()))
                .mapToDouble(ControleFinanceiroDTO::getValor).sum();
    }

    public void doPrintRecibo() {
        if (cliente == null) {
            createFacesErrorMessage("Selecione um cliente.");
            return;
        }
        try {
            gerarPdf(relatorioService.gerarRecibo(mes, ano, cliente), "Recibo");
            createFacesSuccessMessage("Relatório gerado com sucesso!");
            ParametroSistema ps = parametroSistemaService.getParametro().incrementNumeroRecibo();
            parametroSistemaService.salvar(ps);
        } catch (CadastroException | JRException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao gerar o pdf");
        }
    }

    public String[] getMeses() {
        return MESES;
    }

}
