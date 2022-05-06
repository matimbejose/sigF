package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamentoParcela;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoPagamentoService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoPagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PlanoPagamentoService planoPagamentoService;

    @EJB
    private EmpresaService empresaService;

    private PlanoPagamento planoPagamentoSelecionado;

    private LazyDataModel<PlanoPagamento> model;

    private PlanoPagamentoFiltro filtro = new PlanoPagamentoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, planoPagamentoService::quantidadeRegistrosFiltrados, planoPagamentoService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        return "cadastroPlanoPagamento.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return planoPagamentoService.getDadosAuditoriaByID(planoPagamentoSelecionado);
    }

    public String doStartAdd() {
        cleanCache();
        PlanoPagamento pg = new PlanoPagamento();

        pg.setPossuiEntrada("S");
        pg.setQuantidadeParcelas(1);
        pg.setTenatID(empresaService.getEmpresa().getTenatID());
        pg.setPlanoPagamentoParcelaList(new ArrayList<>());
        PlanoPagamentoParcela ppp = new PlanoPagamentoParcela();
        ppp.setIdPlanoPagamento(planoPagamentoSelecionado);
        ppp.setTenatID(pg.getTenatID());
        pg.getPlanoPagamentoParcelaList().add(ppp);

        setPlanoPagamentoSelecionado(pg);
        return "cadastroPlanoPagamento.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        List<PlanoPagamentoParcela> pppList = planoPagamentoService.buscarParcelas(planoPagamentoSelecionado);
        planoPagamentoSelecionado.setPlanoPagamentoParcelaList(pppList);
        return "cadastroPlanoPagamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            PlanoPagamento planoPagamentoCadastrado = planoPagamentoService.findByDescricao(planoPagamentoSelecionado.getDescricao());
            if (Objects.nonNull(planoPagamentoCadastrado)
                    && (!planoPagamentoCadastrado.getId().equals(planoPagamentoSelecionado.getId()))) {
                createFacesErrorMessage("Existe um plano de pagamento cadastrado para a descrição informada.");
                return "cadastroPlanoPagamento.xhtml";
            }
            planoPagamentoSelecionado.setAtivo("S");
            planoPagamentoService.salvar(planoPagamentoSelecionado);
            createFacesSuccessMessage("Plano de pagamento salvo com sucesso!");
            return "listaPlanoPagamento.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "cadastroPlanoPagamento.xhtml";
        }
    }

    public String doFinishExcluir() {
        planoPagamentoService.remover(planoPagamentoSelecionado);
        return "listaPlanoPagamento.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaPlanoPagamento.xhtml";
    }

    public void updateQuantidade() {
        List<PlanoPagamentoParcela> pppList = planoPagamentoSelecionado.getPlanoPagamentoParcelaList();
        for (int i = pppList.size() - 1; i > planoPagamentoSelecionado.getQuantidadeParcelas(); i--) {
            pppList.remove(i);
        }
        while (pppList.size() < planoPagamentoSelecionado.getQuantidadeParcelas()) {
            PlanoPagamentoParcela ppp = new PlanoPagamentoParcela();
            ppp.setIdPlanoPagamento(planoPagamentoSelecionado);
            ppp.setTenatID(planoPagamentoSelecionado.getTenatID());
            pppList.add(ppp);
        }
        planoPagamentoSelecionado.setPlanoPagamentoParcelaList(pppList);
    }

}
