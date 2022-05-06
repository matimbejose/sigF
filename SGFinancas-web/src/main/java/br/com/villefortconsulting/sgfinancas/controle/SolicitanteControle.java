package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.CursoInteresse;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Solicitante;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitanteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.CursoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.SolicitanteService;
import br.com.villefortconsulting.sgfinancas.servicos.TurmaService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemSolicitante;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitante;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
public class SolicitanteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SolicitanteService solicitanteService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private UFService ufService;

    @EJB
    private CursoService cursoService;

    @EJB
    private TurmaService turmaService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private RelatorioService relatorioService;

    private List<Curso> cursosDisponiveis = new LinkedList<>();

    private Solicitante solicitanteSelecionado;

    private CursoInteresse cursoInteresseSelecionado;

    private Curso cursoSelecionado;

    private UF ufSelecionado;

    private String descricaoCurso;

    private Date dataInicio;

    private Date dataFim;

    private LazyDataModel<Solicitante> model;

    private SolicitanteFiltro filtro = new SolicitanteFiltro();

    private String mostraImportacao = "N";

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                solicitanteService::quantidadeRegistrosFiltrados,
                solicitanteService::getListaFiltrada,
                filter -> filter.addPropriedadeOrdenacao("nome"));
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroSolicitante.xhtml";
    }

    public List<Solicitante> getSolicitantes() {
        return solicitanteService.listar();
    }

    public List<Solicitante> getSolicitantesIndicou() {
        return solicitanteService.listarSolicitantesIndicou(solicitanteSelecionado);
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

    public List<Turma> getTurmas() {
        if (solicitanteSelecionado.getIdCurso() != null) {
            return turmaService.listar(solicitanteSelecionado.getIdCurso());
        }
        return new ArrayList<>();
    }

    public String buscarStatusSolicitante(String situacao) {
        return EnumStatusSolicitante.buscarDescricao(situacao);
    }

    public Set<Map.Entry<String, String>> listarOrigemSolicitante() {
        return EnumOrigemSolicitante.listarOrigemSolicitante();
    }

    public void doPesquisaCurso() {

        cursosDisponiveis = cursoService.listar();

        // Removendo das cursos disponiveis os cursos ja atribuidos
        cursosDisponiveis.removeAll(solicitanteSelecionado.getCursosInteresse().stream().map(CursoInteresse::getIdCurso).distinct().collect(Collectors.toList()));

        // Ordenando curos por descricao
        cursosDisponiveis = cursosDisponiveis.stream().sorted((c1, c2) -> c1.getDescricao().compareTo(c2.getDescricao())).collect(Collectors.toList());

    }

    public void doAdicionaCurso() {

        // Atribui curso ao solicitante
        this.solicitanteSelecionado.addCurso(cursoSelecionado);

        // Remove curso da lista de disponiveis
        this.cursosDisponiveis.remove(cursoSelecionado);
    }

    public void doRemoveCurso() {
        cursosDisponiveis.add(cursoInteresseSelecionado.getIdCurso());
        solicitanteSelecionado.removeCurso(cursoInteresseSelecionado);
    }

    public String gerarRelatorioSolicitantes() {
        try {

            Empresa empresa = empresaService.getEmpresa();

            gerarPdf(relatorioService.gerarRelatorioSolicitantes(empresa), "Relatório solicitantes - " + empresa.getDescricao());

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
        }
        return "listaSolicitante.xhtml";
    }

    public String doStartGerarPanorama() {

        if (this.dataInicio == null || this.dataFim == null) {
            this.dataInicio = DataUtil.getPrimeiroDiaDoMes(new Date());
            this.dataFim = DataUtil.getUltimoDiaDoMes(new Date());
        }

        return "panorama.xhtml";
    }

    public String doFinishGerarPanorama() {
        try {

            Empresa empresa = empresaService.getEmpresa();

            gerarPdf(relatorioService.gerarPanorama(empresa, dataInicio, dataFim), "Panorama - " + empresa.getDescricao());

            return "listaSolicitante.xhtml";

        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "panorama.xhtml";
        }
    }

    public String doStartAdd() {
        this.solicitanteSelecionado = new Solicitante();
        this.solicitanteSelecionado.setStatus(EnumStatusSolicitante.SOLICITANTE.getChave());

        this.cursosDisponiveis = new LinkedList<>();
        this.descricaoCurso = "";
        this.ufSelecionado = null;

        return "cadastroSolicitante.xhtml";
    }

    public String doStartAlterar() {

        List<CursoInteresse> cursosAtribuidos = cursoService.listarCursoInteresse(solicitanteSelecionado)
                .stream()
                .sorted((c1, c2) -> c1.getIdCurso().getDescricao().compareTo(c2.getIdCurso().getDescricao()))
                .collect(Collectors.toList());

        solicitanteSelecionado.setCursosInteresse(cursosAtribuidos);

        doPesquisaCurso();

        this.ufSelecionado = null;

        return "cadastroSolicitante.xhtml";
    }

    public String doFinishAdd() {
        try {
            solicitanteService.salvar(solicitanteSelecionado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage("Não foi possível salvar o curso!");
            return "listaCurso.xhtml";
        }

        createFacesSuccessMessage("Solicitante salvo com sucesso!");
        return "listaSolicitante.xhtml";
    }

    public String doFinishExcluir() {
        solicitanteService.remover(solicitanteSelecionado);
        return "listaSolicitante.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return solicitanteService.getDadosAuditoriaByID(solicitanteSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaSolicitante.xhtml";
    }

}
