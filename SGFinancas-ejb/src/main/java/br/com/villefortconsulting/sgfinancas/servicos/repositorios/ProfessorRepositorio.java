package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Professor;
import java.util.List;
import javax.persistence.EntityManager;

public class ProfessorRepositorio extends BasicRepository<Professor> {

    public ProfessorRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ProfessorRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Professor> listar() {
        String jpql = "select b from Professor b order by b.nome";
        return getPureList(jpql);
    }

}
