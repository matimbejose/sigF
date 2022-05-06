package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.sgfinancas.entidades.OrigemMercadoria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.OrigemMercadoriaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.OrigemMercadoriaRepositorio;
import javax.ejb.LocalBean;
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
public class OrigemMercadoriaService extends BasicService<OrigemMercadoria, OrigemMercadoriaRepositorio, OrigemMercadoriaFiltro> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new OrigemMercadoriaRepositorio(em);
    }

    @Override
    public Criteria getModel(OrigemMercadoriaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "codOrigemMercadoria", filter.getCodigo());

        return criteria;
    }

}
