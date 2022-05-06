package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Bloco;
import java.util.List;
import javax.persistence.EntityManager;

public class BlocoRepositorio extends BasicRepository<Bloco> {

    public BlocoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public BlocoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Bloco> listar() {
        String jpql = "select b from Bloco b order by b.descricao";
        return getPureList(jpql);
    }

}
