package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.ContabilidadePlanoConta;
import java.util.List;
import javax.persistence.EntityManager;

public class ContabilidadePlanoContaRepositorio extends BasicRepository<ContabilidadePlanoConta> {

    public ContabilidadePlanoContaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<ContabilidadePlanoConta> listarContabilidadePlanoConta(Contabilidade contabilidade) {
        String jpql = "select c from ContabilidadePlanoConta c where c.idContabilidade = ?1";
        return getPureList(jpql, contabilidade);
    }

    public List<ContabilidadePlanoConta> listar() {
        String jpql = "select c from ContabilidadePlanoConta c order by c.codigo";
        return getPureList(jpql);
    }

}
