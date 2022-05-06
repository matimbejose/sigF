package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import java.util.List;
import javax.persistence.EntityManager;

public class TipoPagamentoRepositorio extends BasicRepository<TipoPagamento> {

    public TipoPagamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TipoPagamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<TipoPagamento> listar() {
        String jpql = "select b from TipoPagamento b where b.ativo = 'S' order by b.descricao";
        return getPureList(jpql);
    }

    public List<TipoPagamento> findByDescricao(String descricao) {
        return getPureList("select f from TipoPagamento f where f.descricao = ?1 and f.ativo = 'S' ", descricao);
    }

}
