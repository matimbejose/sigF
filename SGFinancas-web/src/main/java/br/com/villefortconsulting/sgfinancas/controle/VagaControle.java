package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Vaga;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VagaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.VagaService;
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
public class VagaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VagaService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Vaga vagaSelecionada;

    private LazyDataModel<Vaga> model;

    private VagaFiltro filtro = new VagaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_UNIDADE_MEDIDA.getChave()).getDescricao());
        return "cadastroVaga.xhtml";
    }

    public List<Vaga> getVagas() {
        return service.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setVagaSelecionada(new Vaga());
        return "cadastroVaga.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroVaga.xhtml";
    }

    public String doFinishAdd() {
        service.salvar(vagaSelecionada);

        createFacesSuccessMessage("Vaga salva com sucesso!");
        return "listaVaga.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(vagaSelecionada);
        return "listaVaga.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaVaga.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(vagaSelecionada);
    }

}
