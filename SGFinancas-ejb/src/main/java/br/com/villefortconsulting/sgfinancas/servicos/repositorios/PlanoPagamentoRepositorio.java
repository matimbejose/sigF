package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamentoParcela;
import java.util.List;
import javax.persistence.EntityManager;

public class PlanoPagamentoRepositorio extends BasicRepository<PlanoPagamento> {

    public PlanoPagamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PlanoPagamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<PlanoPagamento> listar() {
        return getPureList(" select pp from PlanoPagamento pp where pp.ativo = 'S' ");
    }

    public List<PlanoPagamentoParcela> buscarParcelas(PlanoPagamento obj) {
        return getPureList(" select ppp from PlanoPagamentoParcela ppp where ppp.idPlanoPagamento = ?1 ", obj);
    }

    public PlanoPagamento findByDescricao(String descricao) {
        String jpql = "select p from PlanoPagamento p where upper(p.descricao) =?1 and p.ativo = 'S'";
        return getPurePojo(jpql, descricao.toUpperCase());
    }

}
