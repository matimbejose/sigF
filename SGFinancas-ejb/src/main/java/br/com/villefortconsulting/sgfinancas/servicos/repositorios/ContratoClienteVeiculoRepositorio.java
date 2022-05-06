package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import javax.persistence.EntityManager;

public class ContratoClienteVeiculoRepositorio extends BasicRepository<Banco> {

    public ContratoClienteVeiculoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

}
