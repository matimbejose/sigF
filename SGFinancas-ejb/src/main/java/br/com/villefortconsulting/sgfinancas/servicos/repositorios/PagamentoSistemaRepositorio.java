package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoSistema;
import javax.persistence.EntityManager;

public class PagamentoSistemaRepositorio extends BasicRepository<PagamentoSistema> {

    public PagamentoSistemaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

}
