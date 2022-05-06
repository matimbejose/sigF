package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPatrimonio;
import java.util.List;
import javax.persistence.EntityManager;

public class TipoPatrimonioRepositorio extends BasicRepository<TipoPatrimonio> {

    public TipoPatrimonioRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TipoPatrimonioRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public TipoPatrimonio buscar(String descricao) {
        String jpql = "select b from TipoPatrimonio b where b.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<TipoPatrimonio> listar() {
        String jpql = "select b from TipoPatrimonio b order by b.descricao";
        return getPureList(jpql);
    }

}
