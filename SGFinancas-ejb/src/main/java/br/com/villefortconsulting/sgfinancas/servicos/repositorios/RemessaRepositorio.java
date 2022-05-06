package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Remessa;
import java.util.List;
import javax.persistence.EntityManager;

public class RemessaRepositorio extends BasicRepository<Remessa> {

    public RemessaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public RemessaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Remessa> listar() {
        String jpql = "select b from Remessa b";
        return getPureList(jpql);
    }

}
