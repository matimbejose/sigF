package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CondicaoPagamento;
import javax.persistence.EntityManager;

public class CondicaoPagamentoRepositorio extends BasicRepository<CondicaoPagamento> {

    public CondicaoPagamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public CondicaoPagamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

}
