package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoAdiantamento;
import javax.persistence.EntityManager;

public class SolicitacaoAdiantamentoRepositorio extends BasicRepository<SolicitacaoAdiantamento> {

    public SolicitacaoAdiantamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public SolicitacaoAdiantamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

}
