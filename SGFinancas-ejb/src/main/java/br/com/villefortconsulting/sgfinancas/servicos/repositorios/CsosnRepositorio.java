package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import java.util.List;
import javax.persistence.EntityManager;

public class CsosnRepositorio extends BasicRepository<Csosn> {

    public CsosnRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Csosn buscarPorCodigo(Integer codigo) {
        String jpql = "select c from Csosn c where c.codigo = ?1";
        return getPurePojoTop1(jpql, codigo);
    }

    public List<Csosn> listar() {
        String jpql = "select c from Csosn c order by c.codigo";
        return getPureList(jpql);
    }

    public List<Csosn> listar(String descricao) {
        String jpql = "select c from Csosn c where c.descricao like ?1 order by c.descricao ";
        return getPureList(jpql, "%" + descricao + "%");
    }

    public List<Csosn> listar(Integer codigo) {
        String jpql = "select c from Csosn c where c.codigo = ?1 order by c.codigo ";
        return getPureList(jpql, codigo);
    }

}
