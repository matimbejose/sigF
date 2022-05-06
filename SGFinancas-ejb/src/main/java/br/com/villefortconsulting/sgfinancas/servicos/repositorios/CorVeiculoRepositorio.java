package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.CorVeiculo;
import javax.persistence.EntityManager;

public class CorVeiculoRepositorio extends BasicRepository<CorVeiculo> {

    public CorVeiculoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

}
