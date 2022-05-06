package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BancoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.BancoRepositorio;
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
public class BancoService extends BasicService<Banco, BancoRepositorio, BancoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new BancoRepositorio(em);
    }

    @Override
    public Criteria getModel(BancoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "site", filter.getSite(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "numero", filter.getNumero());

        return criteria;
    }

    public Banco buscarBancoByDescricao(String descricao) {
        return repositorio.buscarBancoByDescricao(descricao);
    }

    public Banco buscarBancoByNumero(String numero) {
        if (numero.length() > 3) {
            if (numero.substring(0, 1).equals("0")) {
                return repositorio.buscarBancoByNumero(numero.substring(1, numero.length()));
            }
        }
        return repositorio.buscarBancoByNumero(numero);
    }

}
