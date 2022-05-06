package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PrevisaoOrcamentaria;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class PrevisaoOrcamentariaRepositorio extends BasicRepository<PrevisaoOrcamentaria> {

    public PrevisaoOrcamentariaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PrevisaoOrcamentariaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<PrevisaoOrcamentaria> listar() {
        String jpql = "select c from PrevisaoOrcamentaria c where c.tenatID = ?1 order by c.descricao";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public List<PrevisaoOrcamentaria> listarPorPeriodo(Date data) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select new PrevisaoOrcamentaria(c.data, sum(c.previsao), c.idPlanoConta) from PrevisaoOrcamentaria c where c.tenatID = ?1 and month(c.data) = ?2 and year(c.data) = ?3");

        jpql.append(" group by c.idPlanoConta, c.data");
        return getPureList(jpql.toString(), adHocTenant.getTenantID(), DataUtil.getMes(data), DataUtil.getAno(data));
    }

    public List<PrevisaoOrcamentaria> listarPorPeriodo(Date data, CentroCusto centroCusto) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select c from PrevisaoOrcamentaria c where c.tenatID = ?1 and month(c.data) = ?2 and year(c.data) = ?3");
        jpql.append(" and idCentroCusto=?4");
        return getPureList(jpql.toString(), adHocTenant.getTenantID(), DataUtil.getMes(data), DataUtil.getAno(data), centroCusto);
    }

    public PrevisaoOrcamentaria listarPorIDPlanoConta(Date data, PlanoConta planoConta) {
        String jpql = "select c from PrevisaoOrcamentaria c where c.tenatID = ?1 and month(c.data) = ?2 and year(c.data) = ?3 and c.idPlanoConta = ?4";
        return getPurePojo(jpql, adHocTenant.getTenantID(), DataUtil.getMes(data), DataUtil.getAno(data), planoConta);
    }

    public List<PrevisaoOrcamentaria> obterlistaPrevisaoOrcamentaria(Date dataInicio, Date dataFim) {
        String jpql = "select c from PrevisaoOrcamentaria c where c.tenatID = ?1 and c.data between ?2 and ?3 ";
        return getPureList(jpql, adHocTenant.getTenantID(), dataInicio, dataFim);
    }

}
