package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.IntroJSConfig;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntroJSConfigFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.IntroJSConfigRepositorio;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IntroJSConfigService extends BasicService<IntroJSConfig, IntroJSConfigRepositorio, IntroJSConfigFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new IntroJSConfigRepositorio(em);
    }

    public IntroJSConfig findByPageId(String pageId) {
        IntroJSConfig iJS = repositorio.findByPageId(pageId);
        return iJS == null ? new IntroJSConfig() : iJS;
    }

    @Override
    public IntroJSConfig salvar(IntroJSConfig ijsc) {
        if (ijsc.getPageId() == null || ijsc.getPageId().trim().isEmpty()) {
            throw new CadastroException("Informe o ID da página.", null);
        }
        boolean temConfigParaMesmoPageId = listar().stream()
                .anyMatch(item -> item.getPageId().equals(ijsc.getPageId()) && !item.getId().equals(ijsc.getId()));
        if (temConfigParaMesmoPageId) {
            throw new CadastroException("Já existe uma configuração para essa página.", null);
        }
        ijsc.setRevisao(ijsc.getRevisao() + 1);
        return super.salvar(ijsc);
    }

    @Override
    public Criteria getModel(IntroJSConfigFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "pageId", filter.getPageId(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "forcaExibicao", filter.getForcaExibicao());

        return criteria;
    }

}
