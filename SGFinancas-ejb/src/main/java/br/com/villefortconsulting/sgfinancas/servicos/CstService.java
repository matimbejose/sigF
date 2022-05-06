package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CstFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CstRepositorio;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.LocalBean;
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
public class CstService extends BasicService<Cst, CstRepositorio, CstFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CstRepositorio(em);
    }

    public Cst buscarPorCodigo(Integer codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    public List<Cst> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<Cst> listar(Integer codigo) {
        return repositorio.listar(codigo);
    }

    public List<Cst> listarCst(String descricao) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(descricao);

        if (matcher.find()) {
            return listar(Integer.parseInt(descricao));
        }
        return listar(descricao);
    }

    @Override
    public Criteria getModel(CstFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);

        return criteria;
    }

}
