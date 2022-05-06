package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import java.util.List;
import javax.persistence.EntityManager;

public class ContaBancariaRepositorio extends BasicRepository<ContaBancaria> {

    public ContaBancariaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ContaBancariaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ContaBancaria> listarAtivas() {
        String jpql = "select c from ContaBancaria c where c.situacao = 'A' order by c.descricao";
        return getPureList(jpql);
    }

    public List<ContaBancaria> listarContasBancarias() {
        String jpql = "select c from ContaBancaria c "
                + " left join c.idPlanoConta c1 "
                + " left join c.idBanco c2 "
                + " order by c.descricao";
        return getPureList(jpql);
    }

    public boolean existeContaCorrenteInformada() {
        String jpql = "select count(c) from ContaBancaria c";
        return getPurePojo(Long.class, jpql) > 0;
    }

    public List<ContaBancaria> listarPorEmpresa(Empresa empresa) {
        String jpql = "select c from ContaBancaria c  where c.tenatID = ?1 and c.situacao='A' order by c.descricao";
        return getPureList(jpql, empresa.getTenatID());
    }

    public ContaBancaria obterContaBancaria() {
        String jql = "select c from ContaBancaria c where c.tenatID =?1";
        return getPurePojoTop1(jql, adHocTenant.getTenantID());
    }

    public ContaBancaria buscarContaBancaria(String agencia, String conta) {
        String jql = "select c from ContaBancaria c where c.agencia = ?1 and c.conta = ?2 ";
        return getPurePojoTop1(jql, agencia, conta);
    }

    public List<ContaBancaria> listarContasByBanco(Banco banco) {
        String jql = "select c from ContaBancaria c where c.idBanco = ?1 ";
        return getPureList(jql, banco);
    }

    public ContaBancaria buscarContaBancaria(String agencia, String conta, String tenant) {
        String jql = "select c from ContaBancaria c where c.agencia = ?1 and c.conta = ?2 and c.tenatID = ?3 ";
        return getPurePojoTop1(jql, agencia, conta, tenant);
    }

    public ContaBancaria findByDescricao(String descricao) {
        String jpql = "select f from ContaBancaria f where f.descricao = ?1";
        return getPurePojo(jpql, descricao);
    }

    public ContaBancaria buscarContaBancariaById(Integer id) {
        String jpql = "select f from ContaBancaria f where f.id = ?1";
        return getPurePojo(jpql, id);
    }

    public List<ContaBancaria> listarContasByCentroCusto(CentroCusto centro) {
        String jpql = "select c from ContaBancaria c where c.idCentroCusto = ?1";
        return getPureList(jpql, centro);
    }

}
