package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.PontoVenda;
import javax.persistence.EntityManager;

public class PontoVendaRepositorio extends BasicRepository<PontoVenda> {

    public PontoVendaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PontoVendaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

}
