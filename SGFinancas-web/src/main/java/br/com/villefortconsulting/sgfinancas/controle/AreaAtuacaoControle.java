package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.AreaAtuacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AreaAtuacaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AreaAtuacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class AreaAtuacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AreaAtuacaoService areaAtuacaoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private AreaAtuacao areaAtuacaoSelecionado;

    private LazyDataModel<AreaAtuacao> model;

    private AreaAtuacaoFiltro filtro = new AreaAtuacaoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, areaAtuacaoService::quantidadeRegistrosFiltrados, areaAtuacaoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_AREA_ATUACAO.getChave()).getDescricao());
        return "cadastroAreaAtuacao.xhtml";
    }

    public List<AreaAtuacao> getAreaAtuacaos() {
        return areaAtuacaoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setAreaAtuacaoSelecionado(new AreaAtuacao());
        return "cadastroAreaAtuacao.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroAreaAtuacao.xhtml";
    }

    public String doFinishAdd() {
        try {
            areaAtuacaoService.salvar(areaAtuacaoSelecionado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o curso!");
            return "listaCurso.xhtml";
        }

        createFacesSuccessMessage("Área de atuação salva com sucesso!");
        return "listaAreaAtuacao.xhtml";
    }

    public String doFinishExcluir() {
        createFacesSuccessMessage("Área de atuação excluída com sucesso!");
        areaAtuacaoService.remover(areaAtuacaoSelecionado);
        return "listaAreaAtuacao.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return areaAtuacaoService.getDadosAuditoriaByID(areaAtuacaoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAreaAtuacao.xhtml";
    }

}
