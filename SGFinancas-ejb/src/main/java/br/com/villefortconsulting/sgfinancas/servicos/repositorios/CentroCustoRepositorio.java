package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import java.util.List;
import javax.persistence.EntityManager;

public class CentroCustoRepositorio extends BasicRepository<CentroCusto> {

    public CentroCustoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public CentroCustoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<CentroCusto> listar() {
        String jpql = "select c from CentroCusto c where c.tenatID = ?1 order by c.descricao";
        return getPureList(jpql, adHocTenant.getTenantID());
    }
    
    public List<CentroCusto> listarAtivos() {
        String jpql = "select c from CentroCusto c where c.tenatID = ?1 and c.ativo='S' order by c.descricao";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public List<String> getEmpresaComIUGU() {
        String sql = "select c.tenatID from CentroCusto c "
                + " where c.token is not null"
                + " group by c.tenatID";
        return getPureList(sql);
    }

    @Override
    public List<CentroCusto> list() {
        String jpql = "select c from CentroCusto c where c.tenatID = ?1 and c.ativo='S' order by c.descricao";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public CentroCusto findByDescricao(String descricao) {
        String jpql = "select f from CentroCusto f where f.descricao = ?1";
        return getPurePojo(jpql, descricao);
    }

    public CentroCusto findByCNPJ(String cnpj) {
        String jpql = "select f from CentroCusto f where f.cnpj = ?1";
        return getPurePojo(jpql, cnpj);
    }

}
