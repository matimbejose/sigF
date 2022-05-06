package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import java.util.List;
import javax.persistence.EntityManager;

public class CstRepositorio extends BasicRepository<Cst> {

    public CstRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Cst buscarPorCodigo(Integer codigo) {
        String jpql = "select c from Cst c where c.codigo = ?1";
        return getPurePojoTop1(jpql, codigo);
    }

    public List<Cst> listar() {
        String jpql = "select c from Cst c order by c.codigo";
        return getPureList(jpql);
    }

    public List<Cst> listar(String descricao) {
        String jpql = "select c from Cst c where c.descricao like ?1 order by c.descricao ";
        return getPureList(jpql, "%" + descricao + "%");
    }

    public List<Cst> listar(Integer codigo) {
        String jpql = "select c from Cst c where c.codigo = ?1 order by c.codigo ";
        return getPureList(jpql, codigo);
    }

}
