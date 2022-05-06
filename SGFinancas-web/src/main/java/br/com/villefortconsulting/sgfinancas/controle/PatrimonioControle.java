package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Patrimonio;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PatrimonioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.PatrimonioService;
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
public class PatrimonioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PatrimonioService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Patrimonio patrimonioSelecionada;

    private LazyDataModel<Patrimonio> model;

    private PatrimonioFiltro filtro = new PatrimonioFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_UNIDADE_MEDIDA.getChave()).getDescricao());
        return "cadastroPatrimonio.xhtml";
    }

    public List<Patrimonio> getPatrimonios() {
        return service.listar();
    }

    public String doStartAdd() {
        cleanCache();
        patrimonioSelecionada = new Patrimonio();
        return "cadastroPatrimonio.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroPatrimonio.xhtml";
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(patrimonioSelecionada)) {
            return "listaPatrimonio.xhtml";
        }

        service.salvar(patrimonioSelecionada);

        createFacesSuccessMessage("Patrimônio salvo com sucesso!");
        return "listaPatrimonio.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(patrimonioSelecionada);
        return "listaPatrimonio.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaPatrimonio.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(patrimonioSelecionada);
    }

}
