package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import javax.persistence.EntityManager;

public class ParametroGeralRepositorio extends BasicRepository<ParametroGeral> {

    public ParametroGeralRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

}
