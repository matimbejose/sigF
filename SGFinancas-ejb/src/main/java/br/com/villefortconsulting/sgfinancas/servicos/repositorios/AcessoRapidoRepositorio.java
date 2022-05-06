package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.AcessoRapido;
import java.util.List;
import javax.persistence.EntityManager;

public class AcessoRapidoRepositorio extends BasicRepository<AcessoRapido> {

    public AcessoRapidoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<AcessoRapido> listar() {
        String jpql = " select ar from AcessoRapido ar ";
        return getPureList(jpql);
    }

}
