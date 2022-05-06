package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CsosnFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CsosnRepositorio;
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
public class CsosnService extends BasicService<Csosn, CsosnRepositorio, CsosnFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new CsosnRepositorio(em);
    }

    public Csosn buscarPorCodigo(Integer codigo) {
        return repositorio.buscarPorCodigo(codigo);
    }

    public List<Csosn> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<Csosn> listar(Integer codigo) {
        return repositorio.listar(codigo);
    }

    public List<Csosn> listarCsosn(String descricao) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(descricao);

        if (matcher.find()) {
            return listar(Integer.parseInt(descricao));
        }
        return listar(descricao);
    }

    @Override
    public Criteria getModel(CsosnFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.ANYWHERE);

        return criteria;
    }

}
