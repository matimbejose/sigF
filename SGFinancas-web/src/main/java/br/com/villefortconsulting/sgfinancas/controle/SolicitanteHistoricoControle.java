package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteHistorico;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteHistoricoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CursoService;
import br.com.villefortconsulting.sgfinancas.servicos.SolicitanteService;
import br.com.villefortconsulting.sgfinancas.servicos.TurmaService;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitante;
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
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitanteHistoricoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SolicitanteService solicitanteService;

    @EJB
    private CursoService cursoService;

    @EJB
    private TurmaService turmaService;

    private SolicitanteHistorico solicitanteHistoricoSelecionado;

    private Solicitante solicitanteSelecionado;

    private LazyDataModel<SolicitanteHistorico> model;

    private SolicitanteHistoricoFiltro filtro = new SolicitanteHistoricoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                solicitanteService::quantidadeRegistrosSolicitanteFiltrados,
                solicitanteService::getListaSolicitanteHistoricoFiltrada,
                filter -> filter.setSolicitante(solicitanteSelecionado));
    }

    public String doListarHistorico() {
        return "/restrito/solicitanteHistorico/listaSolicitanteHistorico.xhtml";
    }

    public String retornarSolicitante() {
        return "/restrito/solicitante/listaSolicitante.xhtml";
    }

    public boolean alunoJaFoiMatriculado() {
        return StringUtils.equals(solicitanteSelecionado.getStatus(), EnumStatusSolicitante.MATRICULA_EFETVADA.getChave())
                || StringUtils.equals(solicitanteSelecionado.getStatus(), EnumStatusSolicitante.EX_ALUNO.getChave())
                || StringUtils.equals(solicitanteSelecionado.getStatus(), EnumStatusSolicitante.ONLINE.getChave());
    }

    public String doStartAdd() {

        if (solicitanteSelecionado.getIdCurso() == null) {
            createFacesErrorMessage(" Informe um curso de interesse no cadastro de solicitante ");
            return "listaSolicitanteHistorico.xhtml";
        }

        solicitanteHistoricoSelecionado = new SolicitanteHistorico();
        solicitanteHistoricoSelecionado.setIdSolicitante(solicitanteSelecionado);
        solicitanteHistoricoSelecionado.setIdCurso(solicitanteSelecionado.getIdCurso());
        solicitanteHistoricoSelecionado.setIdTurma(solicitanteSelecionado.getIdTurma());
        solicitanteHistoricoSelecionado.setIdUsuarioContato(getUsuarioLogado());

        return "cadastroSolicitanteHistorico.xhtml";
    }

    public List<Turma> getTurmas() {
        return turmaService.listar(solicitanteSelecionado.getIdCurso());
    }

    public String doStartAlterar() {
        return "cadastroSolicitanteHistorico.xhtml";
    }

    public String doFinishAdd() {
        try {

            solicitanteService.salvarSolicitanteHistorico(solicitanteHistoricoSelecionado);

            createFacesSuccessMessage("Interesse salvo com sucesso!");
            return "listaSolicitanteHistorico.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "cadastroSolicitanteHistorico.xhtml";
        }
    }

    public List<Curso> getCursos() {
        return cursoService.listarCursosSemInteresse(solicitanteSelecionado);
    }

    public String doFinishExcluir() {
        try {
            solicitanteService.removerSolicitanteHistorico(solicitanteHistoricoSelecionado);
            createFacesSuccessMessage("Interesse excluído com sucesso!");
            return "listaSolicitanteHistorico.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "listaSolicitanteHistorico.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return solicitanteService.getDadosAuditoriaSolicitanteHistoricoByID(solicitanteHistoricoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaSolicitanteHistorico.xhtml";
    }

}
