package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FuncaoAjudaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.FuncaoAjudaRepositorio;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Christopher
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncaoAjudaService extends BasicService<FuncaoAjuda, FuncaoAjudaRepositorio, FuncaoAjudaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new FuncaoAjudaRepositorio(em);
    }

    public FuncaoAjuda getFuncaoAjuda(String chave) {
        return repositorio.getFuncaoAjuda(chave);
    }

    public List<FuncaoAjuda> getFuncaoAjudas() {
        return repositorio.getFuncaoAjudas();
    }

    @Override
    public Criteria getModel(FuncaoAjudaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            criteria.add(Restrictions.ilike("descricao", filter.getDescricao(), MatchMode.ANYWHERE));
        }

        return criteria;
    }

}
