package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.OrigemMercadoria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.OrigemMercadoriaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.OrigemMercadoriaService;
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
public class OrigemMercadoriaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private OrigemMercadoriaService origemMercadoriaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private OrigemMercadoria origemMercadoriaSelecionado;

    private LazyDataModel<OrigemMercadoria> model;

    private OrigemMercadoriaFiltro filtro = new OrigemMercadoriaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, origemMercadoriaService::quantidadeRegistrosFiltrados, origemMercadoriaService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_ORIGEM_MERCADORIA.getChave()).getDescricao());
        return "cadastroOrigemMercadoria.xhtml";
    }

    public List<OrigemMercadoria> getOrigemMercadorias() {
        return origemMercadoriaService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setOrigemMercadoriaSelecionado(new OrigemMercadoria());
        return "cadastroOrigemMercadoria.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroOrigemMercadoria.xhtml";
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(origemMercadoriaSelecionado)) {
            return "listaOrigemMercadoria.xhtml";
        }

        origemMercadoriaService.salvar(origemMercadoriaSelecionado);

        createFacesSuccessMessage("Origem mercadoria salva com sucesso!");
        return "listaOrigemMercadoria.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaOrigemMercadoria.xhtml";
    }

    public String doFinishExcluir() {
        origemMercadoriaService.remover(origemMercadoriaSelecionado);
        return "listaOrigemMercadoria.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return origemMercadoriaService.getDadosAuditoriaByID(origemMercadoriaSelecionado);
    }

}
