package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TurmaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.TurmaService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTurma;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
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
public class TurmaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TurmaService turmaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private UFService uFService;

    @EJB
    private CidadeService cidadeService;

    private Turma turmaSelecionado;

    private UF ufSelecionado;

    private LazyDataModel<Turma> model;

    private TurmaFiltro filtro = new TurmaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, turmaService::quantidadeRegistrosFiltrados, turmaService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TURMA.getChave()).getDescricao());
        return "cadastroTurma.xhtml";
    }

    public List<UF> getUFs() {
        return uFService.listar();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public List<Turma> getTurmas() {
        return turmaService.listar();
    }

    public String doStartAdd() {
        ufSelecionado = null;
        setTurmaSelecionado(new Turma());
        turmaSelecionado.setSituacao(EnumSituacaoTurma.ABERTA.getSituacao());

        return "cadastroTurma.xhtml";
    }

    public String doStartAlterar() {
        ufSelecionado = turmaSelecionado.getIdCidade().getIdUF();
        return "cadastroTurma.xhtml";
    }

    public String doFinishAdd() {
        try {

            turmaService.salvar(turmaSelecionado);

            createFacesSuccessMessage("Turma salva com sucesso!");
            return "listaTurma.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "cadastroTurma.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            turmaService.remover(turmaSelecionado);

            createFacesSuccessMessage("Turma excluída com sucesso!");
            return "listaTurma.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "listaTurma.xhtml";
        }
    }

    public String doFinalizarTurma() {
        try {
            turmaService.finalizarTurma(turmaSelecionado);

            createFacesSuccessMessage("Turma finalizada com sucesso!");
            return "listaTurma.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "listaTurma.xhtml";
        }
    }

    public String doReabrirTurma() {
        try {
            turmaService.reabrirTurma(turmaSelecionado);

            createFacesSuccessMessage("Turma reaberta com sucesso!");
            return "listaTurma.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "listaTurma.xhtml";
        }
    }

    public boolean verificarTurmaPossuiAlunos(Turma turma) {
        return turmaService.verificarTurmaPossuiAlunos(turma);
    }

    public List<Object> getDadosAuditoria() {
        return turmaService.getDadosAuditoriaByID(turmaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaTurma.xhtml";
    }

}
