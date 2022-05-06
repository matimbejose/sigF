package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import java.util.List;
import javax.persistence.EntityManager;

public class CidadeRepositorio extends BasicRepository<Cidade> {

    public CidadeRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Cidade buscar(String descricao) {
        String jpql = " select c from Cidade c where c.descricao = ?1 ";
        return getPurePojoTop1(jpql, descricao);
    }

    public Cidade buscarPeloCodigoIBGE(String codigoIBGE) {
        String jpql = " select c from Cidade c where c.codigoIBGE = ?1 ";
        return getPurePojoTop1(jpql, codigoIBGE);
    }

    public List<Cidade> listar(String descricao) {
        String jpql = "select b from Cidade b where b.descricao like ?1 ";
        return getPureList(jpql, '%' + descricao + '%');
    }

    public List<Cidade> listar() {
        String jpql = "select b from Cidade b order by b.descricao";
        return getPureList(jpql);
    }

    public List<Cidade> listar(UF uf) {
        String jpql = " select c from Cidade c where c.idUF = ?1 order by c.idUF.descricao ";
        return getPureList(jpql, uf);
    }

}
