package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.TipoPatrimonio;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TipoPatrimonioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.TipoPatrimonioService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
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
public class TipoPatrimonioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TipoPatrimonioService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private TipoPatrimonio vagaSelecionada;

    private LazyDataModel<TipoPatrimonio> model;

    private TipoPatrimonioFiltro filtro = new TipoPatrimonioFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_UNIDADE_MEDIDA.getChave()).getDescricao());
        return "cadastroTipoPatrimonio.xhtml";
    }

    public List<TipoPatrimonio> getTipoPatrimonios() {
        return service.listar();
    }

    public String doStartAdd() {
        cleanCache();
        vagaSelecionada = new TipoPatrimonio();
        return "cadastroTipoPatrimonio.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroTipoPatrimonio.xhtml";
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(vagaSelecionada)) {
            return "listaTipoPatrimonio.xhtml";
        }

        service.salvar(vagaSelecionada);

        createFacesSuccessMessage("Tipo de patrimònio salvo com sucesso!");
        return "listaTipoPatrimonio.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(vagaSelecionada);
        return "listaTipoPatrimonio.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaTipoPatrimonio.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(vagaSelecionada);
    }

}
