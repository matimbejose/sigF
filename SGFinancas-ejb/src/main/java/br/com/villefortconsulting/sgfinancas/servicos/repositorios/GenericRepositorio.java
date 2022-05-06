package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.EntityId;
import javax.persistence.EntityManager;

public class GenericRepositorio extends BasicRepository<EntityId> {

    public GenericRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Object buscar(String entity, int id) {
        String jpql = "select obj from " + entity + " obj where obj.id = ?1";
        return getPurePojoTop1(jpql, id);
    }

}
