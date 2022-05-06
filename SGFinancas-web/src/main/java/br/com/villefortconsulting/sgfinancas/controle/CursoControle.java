package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CursoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CursoService;
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
public class CursoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CursoService cursoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Curso cursoSelecionado;

    private LazyDataModel<Curso> model;

    private CursoFiltro filtro = new CursoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, cursoService::quantidadeRegistrosFiltrados, cursoService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroCurso.xhtml";
    }

    public List<Curso> getCursos() {
        return cursoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setCursoSelecionado(new Curso());
        return "cadastroCurso.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroCurso.xhtml";
    }

    public String doFinishAdd() {
        try {
            cursoService.salvar(cursoSelecionado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o curso!");
            return "listaCurso.xhtml";
        }

        createFacesSuccessMessage("Curso salvo com sucesso!");
        return "listaCurso.xhtml";
    }

    public String doFinishExcluir() {
        try {
            cursoService.remover(cursoSelecionado);
            createFacesSuccessMessage("Curso excluído com sucesso!");
            return "listaCurso.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage("Este curso não pode ser excluído");
            return "listaCurso.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return cursoService.getDadosAuditoriaByID(cursoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaCurso.xhtml";
    }

}
