package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.Time;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VendaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioAtendimentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.servicos.VendaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoCompraVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumToastType;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ServicoService servicoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private FuncionarioAtendimentoService funcionarioAtendimentoService;

    @EJB
    private VendaService vendaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    @Inject
    private OrcamentoControle orcamentoControle;

    private Servico servicoSelecionado;

    private Funcionario funcionarioSelecionado;

    private Venda vendaSelecionada;

    private transient ScheduleEvent event = new DefaultScheduleEvent();

    private DefaultScheduleModel model;

    public List<Servico> getListaServicos() {
        return servicoService.listar();
    }

    public List<Funcionario> getListaFuncionarios() {
        return funcionarioService.listarFuncionariosPorServico(servicoSelecionado);
    }

    public List<Funcionario> getListaFuncionariosDisponiveis() {
        if (servicoSelecionado == null) {
            return new ArrayList<>();
        }
        List<Funcionario> listaFuncionaros = funcionarioService.listarFuncionariosPorServico(servicoSelecionado);
        if (servicoSelecionado.getTempoExecucao() == null || event == null || event.getStartDate() == null) {
            return listaFuncionaros;
        }
        final Time tempoNecessario = new Time(servicoSelecionado.getTempoExecucao());
        VendaFiltro filtro = new VendaFiltro();
        filtro.setData(new MinMax<>());
        filtro.getData().setMin(DataUtil.removerHoras(event.getStartDate()));
        filtro.getData().setMax(DataUtil.adicionarDias(filtro.getData().getMin(), 1));
        List<Venda> listaVenda = vendaService.getListaFiltradaVenda(filtro);
        return listaFuncionaros.stream()
                .filter(funcionario -> {
                    funcionario.setFuncionarioAtendimentoList(funcionarioAtendimentoService.getListaByFuncionario(funcionario));
                    Time tempoPreenchido = listaVenda.stream()
                            .filter(venda -> venda.getIdUsuarioVendedor().getIdFuncionario().equals(funcionario))
                            .map(venda -> new Time(venda.getDataVenda()))
                            .reduce(new Time(), (o1, o2) -> o1.add(o2));
                    return funcionario.getFuncionarioAtendimentoDoDia(DataUtil.getDiaDaSemana(event.getStartDate())).getTempoDisponibilidade()
                            .sub(tempoPreenchido)
                            .sub(tempoNecessario)
                            .compareTo(new Time()) >= 0;
                })
                .collect(Collectors.toList());
    }

    private ScheduleModel initVendaFiltro(Funcionario funcionario) {
        if (model == null) {
            model = new DefaultScheduleModel();
        }
        VendaFiltro filtro = new VendaFiltro();
        filtro.setData(new MinMax<>());
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
        filtro.setFuncionario(funcionario);
        vendaService.getListaFiltrada(filtro).stream()
                .filter(venda -> EnumTipoVenda.ORCAMENTO.getSituacao().equals(venda.getStatusNegociacao()) || EnumTipoVenda.ORCAMENTO_APROVADO.getSituacao().equals(venda.getStatusNegociacao()))
                .filter(venda -> EnumOrigemVenda.AGENDAMENTO.getOrigem().equals(venda.getOrigem()) || EnumOrigemVenda.AGENDAMENTO_APROVADO.getOrigem().equals(venda.getOrigem()))
                .filter(venda -> model.getEvents().stream().map(data -> ((Venda) data.getData()).getId()).noneMatch(v1 -> v1.compareTo(venda.getId()) == 0))
                .map(this::getEvent)
                .forEach(model::addEvent);

        return model;
    }

    public ScheduleModel getModelFuncionario() {
        return initVendaFiltro(getUsuarioLogado().getIdFuncionario());
    }

    public ScheduleModel getModelGerencia() {
        return initVendaFiltro(funcionarioSelecionado);
    }

    public void addEvent(SelectEvent selectEvent) {
        Date dt = (Date) selectEvent.getObject();
        if (dt.compareTo(DataUtil.removerHoras(new Date())) < 0) {
            addToast(EnumToastType.ERROR, "Erro", "Não é possível realizar um agendamento para uma data anterior a hoje.");
            return;
        }
        if (servicoSelecionado == null) {
            addToast(EnumToastType.ERROR, "Erro", "Selecione um serviço antes.");
            return;
        }
        event = new DefaultScheduleEvent("", dt, dt);
        if (getListaFuncionariosDisponiveis().isEmpty()) {
            event = null;
            addToast(EnumToastType.ERROR, "Erro", "No momento nenhum funcionário está disponível para realizar o serviço no dia selecionado.");
            return;
        }
        vendaSelecionada = new Venda();
        PrimeFaces.current().executeScript("PF('eventDialog').show();");
    }

    public void seeEvent(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();

        PrimeFaces.current().executeScript("PF('eventDialog').show();");
    }

    private DefaultScheduleEvent getEvent(Venda venda) {
        Date dtInicial = venda.getDataVencimento();
        Date dtFinal = vendaService.listarVendaServico(venda).stream()
                .map(VendaServico::getIdServico)
                .map(Servico::getTempoExecucao)
                .filter(Objects::nonNull)
                .reduce(dtInicial, DataUtil::adicionarTempo);

        DefaultScheduleEvent evt = new DefaultScheduleEvent();
        evt.setAllDay(false);
        evt.setData(venda);
        evt.setDescription(venda.getIdUsuarioVendedor().getNome());
        evt.setEditable(false);
        evt.setEndDate(dtFinal);
        evt.setStartDate(dtInicial);
        evt.setTitle(evt.getDescription());
        evt.setData(venda);
        return evt;
    }

    public String getInicioMes() {
        return DataUtil.dateToString(DataUtil.getPrimeiroDiaDoMes(new Date()), "yyyy-MM-dd");
    }

    private static void addToast(EnumToastType tipo, String title, String message) {
        PrimeFaces.current().executeScript("new VillefortToast().showCustom(`" + tipo.toString().toLowerCase() + "`, `" + title + "`, `" + message + "`)");
    }

    public void doFinishAdd() {
        vendaSelecionada.setIdUsuarioVendedor(usuarioService.getUserByFuncionario(funcionarioSelecionado));
        vendaSelecionada.setDataVencimento(event.getStartDate());
        vendaSelecionada.setDataVenda(new Date());
        vendaSelecionada.setValor(servicoSelecionado.getValorVenda());
        vendaSelecionada.setSituacao(EnumSituacaoCompraVenda.ATIVO.getSituacao());
        vendaSelecionada.setStatusNegociacao(EnumTipoVenda.ORCAMENTO.getSituacao());
        vendaSelecionada.setNumParcela(1);
        vendaSelecionada.setFormaPagamento(EnumFormaPagamento.AVISTA.getForma());
        vendaSelecionada.setOrigem(EnumOrigemVenda.AGENDAMENTO.getOrigem());

        vendaService.salvar(vendaSelecionada);

        // Ativação automática
        if (parametroSistemaService.getParametro().getTipoConfirmacaoAgendamento().equals("A")) {
            orcamentoControle.setVendaSelecionada(vendaSelecionada);
            orcamentoControle.alteraVendaOrcamento();
            // Selecionar a forma de pagamento
            orcamentoControle.salvarVendaOrcamento();
        }
    }

    public Venda getVendaSelecionada() {
        if (event == null || event.getData() == null || !(event.getData() instanceof Venda)) {
            return null;
        }
        return (Venda) event.getData();
    }

    public List<VendaServico> getVendaServicoList() {
        if (getVendaSelecionada() == null) {
            return new ArrayList<>();
        }
        return vendaService.listarVendaServico(getVendaSelecionada());
    }

    public void resetModel() {
        model = null;
    }

}
