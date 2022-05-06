package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import java.util.List;
import javax.persistence.EntityManager;

public class UnidadeMedidaRepositorio extends BasicRepository<UnidadeMedida> {

    public UnidadeMedidaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public UnidadeMedidaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public UnidadeMedida buscar(String descricao) {
        String jpql = "select b from UnidadeMedida b where b.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public UnidadeMedida buscarPorSigla(String sigla) {
        String jpql = "select b from UnidadeMedida b where b.sigla = ?1";
        return getPurePojoTop1(jpql, sigla);
    }

    public List<UnidadeMedida> listar() {
        String jpql = "select b from UnidadeMedida b order by b.descricao";
        return getPureList(jpql);
    }

    public UnidadeMedida findByDescricao(String descricao) {
        String jpql = "select b from UnidadeMedida b where upper(b.descricao) = ?1";
        return getPurePojo(jpql, descricao.toUpperCase());
    }

}
