package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.MotivoCancelamentoConta;
import java.util.List;
import javax.persistence.EntityManager;

public class MotivoCancelamentoContaRepositorio extends BasicRepository<MotivoCancelamentoConta> {

    public MotivoCancelamentoContaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public MotivoCancelamentoContaRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<MotivoCancelamentoConta> listar() {
        String jpql = "select b from MotivoCancelamentoConta b ";
        return getPureList(jpql);
    }

    public MotivoCancelamentoConta findByDescricao(String descricao) {
        String jpql = "select b from MotivoCancelamentoConta b where b.ativo = 'S' and upper(b.descricao) = ?1";
        return getPurePojo(jpql, descricao);
    }

}
