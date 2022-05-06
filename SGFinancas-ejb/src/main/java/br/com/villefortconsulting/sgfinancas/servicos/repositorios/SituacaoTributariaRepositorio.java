package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.SituacaoTributaria;
import java.util.List;
import javax.persistence.EntityManager;

public class SituacaoTributariaRepositorio extends BasicRepository<SituacaoTributaria> {

    public SituacaoTributariaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<SituacaoTributaria> listar() {
        String jpql = "select b from SituacaoTributaria b order by b.descricao";
        return getPureList(jpql);
    }

}
