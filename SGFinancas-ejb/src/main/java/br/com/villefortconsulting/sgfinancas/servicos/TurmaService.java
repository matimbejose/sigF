package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Curso;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TurmaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TurmaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTurma;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TurmaService extends BasicService<Turma, TurmaRepositorio, TurmaFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private CentroCustoService centroCustoService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new TurmaRepositorio(em, adHocTenant);
    }

    @Override
    public Turma salvar(Turma turma) {
        if (turma.getId() == null) {
            turma.setIdCentroCusto(centroCustoService.addCentroCustoPorTurma(turma));
            return adicionar(turma);
        } else {
            if (turma.getIdCentroCusto() == null) {
                turma.setIdCentroCusto(centroCustoService.addCentroCustoPorTurma(turma));
            } else {
                turma.getIdCentroCusto().setDescricao("Turma: " + turma.getDescricao());
            }

            return alterar(turma);
        }
    }

    public Turma buscar(String descricao) {
        if (descricao != null) {
            return repositorio.buscar(descricao);
        }
        return null;
    }

    public List<Turma> listar(Curso curso) {
        return repositorio.listar(curso);
    }

    @Override
    public Criteria getModel(TurmaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "numero", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public boolean verificarTurmaPossuiAlunos(Turma turma) {
        return repositorio.verificarTurmaPossuiAlunos(turma);
    }

    public Turma finalizarTurma(Turma turma) {
        turma.setSituacao(EnumSituacaoTurma.FECHADA.getSituacao());
        return alterar(turma);
    }

    public Turma reabrirTurma(Turma turma) {
        turma.setSituacao(EnumSituacaoTurma.ABERTA.getSituacao());
        return alterar(turma);
    }

}
