package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.MoradorUnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.Unidade;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UnidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.UnidadeRepositorio;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
public class UnidadeService extends BasicService<Unidade, UnidadeRepositorio, UnidadeFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new UnidadeRepositorio(em, adHocTenant);
    }

    public List<MoradorUnidadeOcupacao> addTenantMoradorUnidadeOcupacao(List<MoradorUnidadeOcupacao> lista) {
        lista.forEach(morador -> morador.setTenatID(adHocTenant.getTenantID()));
        return lista;
    }

    public Unidade salvar(Unidade unidade, UnidadeOcupacao unidadeOcupacao) {
        if (unidadeOcupacao == null || unidadeOcupacao.getMoradorUnidadeOcupacaoList() == null || unidadeOcupacao.getMoradorUnidadeOcupacaoList().isEmpty()) {
            throw new CadastroException("Informe um morador. ", null);
        } else if (unidadeOcupacao.getMoradorUnidadeOcupacaoList().stream().allMatch(um -> um.getResponsavel().equals("N"))) {
            throw new CadastroException("Informe um morador responsável.", null);
        }
        unidadeOcupacao.setTenatID(adHocTenant.getTenantID());
        unidade.addUnidadeOcupacao(unidadeOcupacao);

        if (unidade.getId() == null) {
            return adicionar(unidade);
        }
        return alterar(unidade);
    }

    @Override
    public Criteria getModel(UnidadeFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "idBloco", filter.getBloco());

        return criteria;
    }

    public List<UnidadeOcupacao> listarMoradores(Unidade unidade) {
        return repositorio.listarOcupacoes(unidade);
    }

    public UnidadeOcupacao salvarUnidade(UnidadeOcupacao unidadeMorador) {
        if (unidadeMorador.getId() == null) {
            return repositorio.adicionarUnidadeOcupacao(unidadeMorador);
        } else {
            return repositorio.alterarUnidadeOcupacao(unidadeMorador);
        }
    }

    public UnidadeOcupacao adicionarUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        return repositorio.adicionarUnidadeOcupacao(unidadeMorador);
    }

    public UnidadeOcupacao alterarUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        return repositorio.alterarUnidadeOcupacao(unidadeMorador);
    }

    public void removerUnidadeOcupacao(UnidadeOcupacao unidadeMorador) {
        repositorio.removerUnidadeOcupacao(unidadeMorador);
    }

    public UnidadeOcupacao buscarUnidadeOcupacao(int id) {
        return repositorio.buscarUnidadeOcupacao(id);
    }

    public List<UnidadeOcupacao> listarUnidadeOcupacao() {
        return repositorio.listarUnidadeOcupacao();
    }

    public List<UnidadeOcupacao> listarUnidadeOcupacaoSemMensalidade(Date competencia) {
        return repositorio.listarUnidadeOcupacaoSemMensalidade(competencia, adHocTenant.getTenantID());
    }

    public UnidadeOcupacao buscarUnidadeOcupacao(Unidade unidade) {
        return repositorio.buscarUnidadeOcupacao(unidade);
    }

    public MoradorUnidadeOcupacao buscarMoradorResponsavel(UnidadeOcupacao unidadeOcupacao) {
        return repositorio.buscarMoradorResponsavel(unidadeOcupacao);
    }

    public List<MoradorUnidadeOcupacao> buscarListaMoradores(UnidadeOcupacao unidadeOcupacao) {
        return repositorio.buscarListaMoradores(unidadeOcupacao);
    }

}
