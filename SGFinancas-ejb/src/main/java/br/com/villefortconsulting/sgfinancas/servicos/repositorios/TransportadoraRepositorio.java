package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Transportadora;
import java.util.List;
import javax.persistence.EntityManager;

public class TransportadoraRepositorio extends BasicRepository<Transportadora> {

    public TransportadoraRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TransportadoraRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Cidade buscarCidade(int id) {
        return getEntity(Cidade.class, id);
    }

    public List<Transportadora> listar() {
        String jpql = "select t from Transportadora t order by t.descricao";
        return getPureList(jpql);
    }

    public List<Cidade> listarCidade() {
        String jpql = "select c from Cidade c order by c.descricao";
        return getPureList(jpql);
    }

    public Transportadora findByCnpjDescricao(String cnpj, String descricao) {
        String jpql = "select t from Transportadora t where t.cnpj =?1 and upper(t.descricao) = ?2";
        return getPurePojo(jpql, cnpj, descricao.toUpperCase());
    }

}
