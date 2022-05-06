package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Sindico;
import br.com.villefortconsulting.sgfinancas.entidades.SindicoConselho;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SindicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.SindicoRepositorio;
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
public class SindicoService extends BasicService<Sindico, SindicoRepositorio, SindicoFiltro> {

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
        repositorio = new SindicoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(SindicoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public boolean existeSindicoAtivo() {
        return repositorio.existeSindicoAtivo();
    }

    public List<SindicoConselho> obterConselheiros(Sindico sindico) {
        return repositorio.obterConselheiros(sindico);
    }

    public void atualizaTenat(SindicoConselho sindicoConselho) {
        repositorio.atualizaTenat(sindicoConselho);
    }

}
