package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Combustivel;
import javax.persistence.EntityManager;

public class CombustivelRepositorio extends BasicRepository<Combustivel> {

    public CombustivelRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

}
