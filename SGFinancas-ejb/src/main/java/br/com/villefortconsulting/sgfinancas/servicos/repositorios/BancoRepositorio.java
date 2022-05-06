package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import java.util.List;
import javax.persistence.EntityManager;

public class BancoRepositorio extends BasicRepository<Banco> {

    public BancoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Banco> listar() {
        String jpql = "select b from Banco b order by b.descricao";
        return getPureList(jpql);
    }

    public Banco buscarBancoByDescricao(String descricao) {
        String jpql = "select b from Banco b where b.descricao=?1";
        return getPurePojo(jpql, descricao);
    }

    public Banco buscarBancoByNumero(String numero) {
        String jpql = "select b from Banco b where b.numero=?1";
        return getPurePojo(jpql, numero);
    }

}
