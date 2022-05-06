package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import java.util.List;
import javax.persistence.EntityManager;

public class CtissRepositorio extends BasicRepository<Ctiss> {

    public CtissRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Ctiss> listar() {
        String jpql = "select c from Ctiss c order by c.descricao";
        return getPureList(jpql);
    }

    public List<Ctiss> listar(Cidade cidade) {
        String jpql = "select c from Ctiss c where c.idCidade = ?";
        return getPureList(jpql, cidade);
    }

    public List<Ctiss> listar(Cidade cidade, List<Cnae> cnaes) {
        String jpql = "select distinct c.idCtiss from CnaeCtiss c where c.idCtiss.idCidade = ?1 and c.idCnae in (?2) and c.idCtiss is not null";
        return getPureList(jpql, cidade, cnaes);
    }

    public boolean empresaPossuiCtiss(List<Cnae> cnaes) {
        String jpql = "select count(c.idCtiss) from CnaeCtiss c where c.idCnae in (?1) and c.idCtiss is not null";
        return getPurePojo(Long.class, jpql, cnaes) > 0;
    }

}
