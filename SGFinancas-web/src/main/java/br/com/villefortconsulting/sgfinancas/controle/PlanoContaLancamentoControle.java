package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaLancamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaLancamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
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
public class PlanoContaLancamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private PlanoContaService planoContaService;

    private PlanoContaLancamentoContabil planoContaLancamentoContabilSelecionado;

    private LazyDataModel<PlanoContaLancamentoContabil> model;

    private PlanoContaLancamentoFiltro filtro = new PlanoContaLancamentoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, planoContaLancamentoService::quantidadeRegistrosFiltrados, planoContaLancamentoService::getListaFiltrada);
    }

    @Override
    public void cleanCache() {
        this.planoContaLancamentoContabilSelecionado = new PlanoContaLancamentoContabil();
    }

    public String doStartAdd() {
        cleanCache();
        return "cadastroLancamento.xhtml";
    }

    public String doFinishAdd() {
        planoContaLancamentoService.salvar(planoContaLancamentoContabilSelecionado);

        createFacesSuccessMessage("Lançamento salvo com sucesso!");
        return "listaLancamento.xhtml";
    }

    public String dataMaxima() {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        return out.format(DataUtil.getHoje());
    }

    public List<PlanoContaLancamentoContabil> getPlanoContaLancamentoContabils() {
        return planoContaLancamentoService.listar();
    }

    public List<PlanoConta> listarPlanoContaTipoCredito() {
        return planoContaService.listarPlanosContaTipoCredito();
    }

    public List<PlanoConta> listarPlanoContaTipoDebito() {
        return planoContaService.listarPlanosContaTipoDebito();
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_LANCAMENTO.getChave()).getDescricao());
        return "cadastroLancamento.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaLancamento.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return planoContaLancamentoService.getDadosAuditoriaByID(planoContaLancamentoContabilSelecionado);
    }

}
