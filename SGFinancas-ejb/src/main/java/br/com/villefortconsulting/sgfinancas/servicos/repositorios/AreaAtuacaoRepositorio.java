package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.AreaAtuacao;
import java.util.List;
import javax.persistence.EntityManager;

public class AreaAtuacaoRepositorio extends BasicRepository<AreaAtuacao> {

    public AreaAtuacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public AreaAtuacaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public AreaAtuacao buscar(String descricao) {
        String jpql = "select b from AreaAtuacao b where b.descricao = ?1 ";
        return getPurePojo(jpql, descricao);
    }

    public List<AreaAtuacao> listar() {
        String jpql = "select b from AreaAtuacao b order by b.descricao";
        return getPureList(jpql);
    }

}
