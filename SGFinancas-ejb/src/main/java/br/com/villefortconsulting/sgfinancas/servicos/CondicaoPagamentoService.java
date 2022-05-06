package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CondicaoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CondicaoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.CondicaoPagamentoRepositorio;
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
public class CondicaoPagamentoService extends BasicService<CondicaoPagamento, CondicaoPagamentoRepositorio, CondicaoPagamentoFiltro> {

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
        repositorio = new CondicaoPagamentoRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(CondicaoPagamentoFiltro filtro) {

        Criteria criteria = super.getModel(filtro);

        addIlikeRestrictionTo(criteria, "descricao", filtro.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "qtdeParcelas", filtro.getQtdeParcelas());
        addEqRestrictionTo(criteria, "parcelasIguais", filtro.getParcelasIguais());
        addEqRestrictionTo(criteria, "diasCarenciaParcela", filtro.getDiasCarenciaParcela());
        addEqRestrictionTo(criteria, "intervaloDiasParcela", filtro.getIntervaloDiasParcela());
        addEqRestrictionTo(criteria, "fatorParcela", filtro.getFatorParcela());
        addEqRestrictionTo(criteria, "diaMesForaParcela", filtro.getDiaMesForaParcela());

        return criteria;

    }

}
