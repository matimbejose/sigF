package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CategoriaEmpresa;
import java.util.List;
import javax.persistence.EntityManager;

public class CategoriaEmpresaRepositorio extends BasicRepository<CategoriaEmpresa> {

    public CategoriaEmpresaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public CategoriaEmpresaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<CategoriaEmpresa> listar() {
        String jpql = "select b from CategoriaEmpresa b order by b.descricao";
        return getPureList(jpql);
    }

    public CategoriaEmpresa buscarPorNome(String nome) {
        return getPurePojoTop1("select ce from CategoriaEmpresa ce where ce.descricao = ?1", nome);
    }

}
