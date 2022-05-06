package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoAjuste;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoParcelaAjuste;
import javax.persistence.EntityManager;

public class ContratoAjusteRepositorio extends BasicRepository<ContratoAjuste> {

    public ContratoAjusteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ContratoAjusteRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public ContratoParcelaAjuste adicionarContratoParcelaAjuste(ContratoParcelaAjuste contratoAjuste) {
        addEntity(contratoAjuste);
        return contratoAjuste;
    }

    public void removerContratoParcelaAjuste(ContratoParcelaAjuste contratoAjuste) {
        removeEntity(contratoAjuste);
    }

}
