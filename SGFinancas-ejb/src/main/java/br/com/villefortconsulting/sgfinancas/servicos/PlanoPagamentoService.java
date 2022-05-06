package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamentoParcela;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PlanoPagamentoRepositorio;
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
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanoPagamentoService extends BasicService<PlanoPagamento, PlanoPagamentoRepositorio, PlanoPagamentoFiltro> {

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
        repositorio = new PlanoPagamentoRepositorio(em, adHocTenant);
    }

    @Override
    public PlanoPagamento salvar(PlanoPagamento planoPagamento) {
        boolean parcelaSemPorcentagem = planoPagamento.getPlanoPagamentoParcelaList().stream()
                .anyMatch(ppp -> ppp.getPorcentagem() == null);
        if (parcelaSemPorcentagem) {
            throw new CadastroException("Informe a porcentagem de todas as parcelas.", null);
        }

        planoPagamento.getPlanoPagamentoParcelaList().stream()
                .forEach(ppp -> ppp.setIdPlanoPagamento(planoPagamento));

        if (planoPagamento.getId() == null) {
            return adicionar(planoPagamento);
        }
        return alterar(planoPagamento);
    }

    @Override
    public Criteria getModel(PlanoPagamentoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        addEqRestrictionTo(criteria, "possuiEntrada", filter.getPossuiEntrada());
        if (filter.getAtivo() == null) {
            criteria.add(Restrictions.eq("ativo", "S"));
        }

        return criteria;
    }

    public List<PlanoPagamentoParcela> buscarParcelas(PlanoPagamento obj) {
        return repositorio.buscarParcelas(obj);
    }

    public PlanoPagamento findByDescricao(String descricao) {
        return repositorio.findByDescricao(descricao);
    }

}
