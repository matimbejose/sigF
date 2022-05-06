package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Patrimonio;
import java.util.List;
import javax.persistence.EntityManager;

public class PatrimonioRepositorio extends BasicRepository<Patrimonio> {

    public PatrimonioRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PatrimonioRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Patrimonio buscar(String descricao) {
        String jpql = "select b from Patrimonio b where b.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Patrimonio> listar() {
        String jpql = "select b from Patrimonio b order by b.descricao";
        return getPureList(jpql);
    }

}
