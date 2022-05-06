package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroCondominio;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ParametroCondominioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ParametroCondominioRepositorio;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
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

/**
 *
 * @author Christopher
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParametroCondominioService extends BasicService<ParametroCondominio, ParametroCondominioRepositorio, ParametroCondominioFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostConstruct
    @PostActivate
    protected void postConstruct() {
        repositorio = new ParametroCondominioRepositorio(em, adHocTenant);
    }

    protected ParametroCondominio buscar() {
        return buscar(1);
    }

    public ParametroCondominio getParametroCondominio() {
        return repositorio.getParametro();
    }

    public ParametroCondominio addParametroCondominio(ParametroCondominio parm) {
        return adicionar(parm);
    }

    public ParametroCondominio setParametroCondominio(ParametroCondominio parm) {
        return alterar(parm);
    }

    public ParametroCondominio getParametro() {
        return repositorio.getParametro();
    }

    @Override
    public Criteria getModel(ParametroCondominioFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "nome", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public Date alterarDataMensalidade(Date dataVencimento) {
        return DataUtil.alterarDia(buscar().getDiaVencimentoMensalidade(), dataVencimento);
    }

}
