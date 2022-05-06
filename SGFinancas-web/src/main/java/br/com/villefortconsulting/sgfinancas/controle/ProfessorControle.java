package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Professor;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProfessorFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProfessorService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
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
public class ProfessorControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProfessorService professorService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private UFService ufService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private UF ufSelecionado;

    private Professor professorSelecionado;

    private LazyDataModel<Professor> model;

    private ProfessorFiltro filtro = new ProfessorFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, professorService::quantidadeRegistrosFiltrados, professorService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_PROFESSOR.getChave()).getDescricao());
        return "cadastroProfessor.xhtml";
    }

    public void buscarEnderecoPorCep() {
        if (professorSelecionado.getCep() != null) {
            CepDTO cepDTO = cidadeService.getEnderecoPorCep(professorSelecionado.getCep());

            ufSelecionado = cepDTO.getUf();
            professorSelecionado.setIdCidade(cepDTO.getCidade());
            professorSelecionado.setEndereco(cepDTO.getEndereco());
            professorSelecionado.setBairro(cepDTO.getBairro());
        }
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public List<Professor> getProfessors() {
        return professorService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setProfessorSelecionado(new Professor());
        return "cadastroProfessor.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroProfessor.xhtml";
    }

    public String doFinishAdd() {
        try {
            professorService.salvar(professorSelecionado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o curso!");
            return "listaCurso.xhtml";
        }

        createFacesSuccessMessage("Professor salvo com sucesso!");
        return "listaProfessor.xhtml";
    }

    public String doFinishExcluir() {
        professorService.remover(professorSelecionado);
        return "listaProfessor.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return professorService.getDadosAuditoriaByID(professorSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaProfessor.xhtml";
    }

}
