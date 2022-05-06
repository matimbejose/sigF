package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.NaturezaOperacao;
import java.util.List;
import javax.persistence.EntityManager;

public class NaturezaOperacaoRepositorio extends BasicRepository<NaturezaOperacao> {

    public NaturezaOperacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<NaturezaOperacao> listar() {
        String jpql = "select b from NaturezaOperacao b order by b.descricao";
        return getPureList(jpql);
    }

}
