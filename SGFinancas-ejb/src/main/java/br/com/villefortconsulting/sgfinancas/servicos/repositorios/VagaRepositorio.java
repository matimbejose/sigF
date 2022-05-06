package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Vaga;
import java.util.List;
import javax.persistence.EntityManager;

public class VagaRepositorio extends BasicRepository<Vaga> {

    public VagaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public VagaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Vaga buscar(String descricao) {
        String jpql = "select b from Vaga b where b.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Vaga> listar() {
        String jpql = "select b from Vaga b order by b.descricao";
        return getPureList(jpql);
    }

}
