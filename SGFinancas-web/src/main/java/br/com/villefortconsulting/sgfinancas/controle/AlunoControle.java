package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitanteTurma;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteTurmaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.SolicitanteService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumOpcaoPagamento;
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
public class AlunoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SolicitanteService solicitanteService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    private Turma turmaSelecionada;

    private SolicitanteTurma solicitanteTurmaSelecionado;

    private LazyDataModel<SolicitanteTurma> model;

    private SolicitanteTurmaFiltro filtro = new SolicitanteTurmaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                solicitanteService::quantidadeRegistrosSolicitanteTurmaFiltrados,
                solicitanteService::getListaSolicitanteTurmaFiltrada,
                filter -> filter.setTurma(turmaSelecionada));

    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_TURMA.getChave()).getDescricao());
        return "cadastroAluno.xhtml";
    }

    public String doStartListarAluno() {
        return "/restrito/aluno/listaAluno.xhtml?faces-redirect=true";
    }

    public String doFinishListarAluno() {
        return "/restrito/turma/listaTurma.xhtml?faces-redirect=true";
    }

    public List<Solicitante> getAlunos() {
        return solicitanteService.listarAlunosDisponiveis(turmaSelecionada);
    }

    public String doStartAdd() {
        solicitanteTurmaSelecionado = new SolicitanteTurma();
        solicitanteTurmaSelecionado.setIdTurma(turmaSelecionada);
        solicitanteTurmaSelecionado.setOpcaoPagamento(EnumOpcaoPagamento.CONTRATO.getOpcao());

        return "cadastroAluno.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroAluno.xhtml";
    }

    public String gerarListaPresenca() {
        try {

            Empresa empresa = empresaService.getEmpresa();

            gerarPdf(relatorioService.gerarListaPresenca(empresa, turmaSelecionada), "Lista de presença - " + turmaSelecionada.getIdCurso().getDescricao());

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
        return "listaAluno.xhtml";
    }

    public String gerarRelacaoProfissoes() {
        try {

            Empresa empresa = empresaService.getEmpresa();

            gerarPdf(relatorioService.gerarRelacaoProfissoes(empresa, turmaSelecionada), "Relação das profissões - " + turmaSelecionada.getIdCurso().getDescricao());

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
        return "listaAluno.xhtml";
    }

    public String doFinishAdd() {

        try {
            solicitanteService.salvarSolicitanteTurma(solicitanteTurmaSelecionado, getUsuarioLogado());

            createFacesSuccessMessage("Aluno salvo com sucesso!");
            return "listaAluno.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroAluno.xhtml";
        }
    }

    public String doFinishExcluir() {
        try {
            solicitanteService.removerSolicitanteTurma(solicitanteTurmaSelecionado);

            createFacesSuccessMessage("Aluno excluído com sucesso!");
            return "listaAluno.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroAluno.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return solicitanteService.getDadosAuditoriaSolicitanteTurmaByID(solicitanteTurmaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAluno.xhtml";
    }

}
