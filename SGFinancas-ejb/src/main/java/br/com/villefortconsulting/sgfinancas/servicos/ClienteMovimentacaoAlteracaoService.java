package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacaoAlteracao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteMovimentacaoAlteracaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ClienteMovimentacaoAlteracaoRepositorio;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClienteMovimentacaoAlteracaoService extends BasicService<ClienteMovimentacaoAlteracao, ClienteMovimentacaoAlteracaoRepositorio, ClienteMovimentacaoAlteracaoFiltro> {

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
        repositorio = new ClienteMovimentacaoAlteracaoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(ClienteMovimentacaoAlteracaoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addEqRestrictionTo(criteria, "status", filter.getStatus());

        return criteria;
    }

}
