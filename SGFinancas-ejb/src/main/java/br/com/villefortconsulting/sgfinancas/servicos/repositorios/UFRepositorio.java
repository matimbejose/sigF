package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import java.util.List;
import javax.persistence.EntityManager;

public class UFRepositorio extends BasicRepository<UF> {

    public UFRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public UF buscar(String descricao) {
        String jpql = "select u from UF u where u.descricao = ?1";
        return getPurePojo(jpql, descricao);
    }

    public UF buscarCUF(String descricao) {
        String jpql = "select u from UF u where u.Cuf = ?1";
        return getPurePojo(jpql, descricao);
    }

    public List<UF> listar() {
        String jpql = "select u from UF u order by u.descricao";
        return getPureList(jpql);
    }

}
