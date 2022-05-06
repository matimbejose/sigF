package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Retorno;
import java.util.List;
import javax.persistence.EntityManager;

public class RetornoRepositorio extends BasicRepository<Retorno> {

    public RetornoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public RetornoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Retorno> listar() {
        String jpql = "select b from Retorno b";
        return getPureList(jpql);
    }

}
