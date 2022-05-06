package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.PontoObservacao;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class PontoObservacaoRepositorio extends BasicRepository<PontoObservacao> {

    public PontoObservacaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PontoObservacaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public PontoObservacao pegaObservacao(Date competencia, Funcionario funcionario) {
        String jpql = "select b from PontoObservacao b where  month(b.referencia) = ?1 and year(b.referencia) = ?2 and b.idFuncionario = ?3 and b.tenatID = ?4";
        return getPurePojo(jpql, DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, adHocTenant.getTenantID());
    }

    public String buscarObservacao(Date competencia, Funcionario funcionario) {
        String jpql = "select b.observacao from PontoObservacao b where  month(b.referencia) = ?1 and year(b.referencia) = ?2 and b.idFuncionario = ?3 and b.tenatID = ?4";
        return getPurePojo(jpql, DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, adHocTenant.getTenantID());
    }

    public List<PontoObservacao> listaPorPeriodo(Date competencia, Funcionario funcionario) {

        StringBuilder jpql = new StringBuilder();
        jpql.append("  select b from PontoObservacao b ");
        jpql.append(" where ");
        jpql.append(" day(b.referencia) = ?1 ");
        jpql.append(" and month(b.referencia) = ?2 ");
        jpql.append(" and year(b.referencia) = ?3 ");
        jpql.append(" and b.tenatID = ?4 ");

        if (funcionario != null) {
            jpql.append(" and b.idFuncionario = ?5 ");
            return getPureList(jpql.toString(), DataUtil.getDia(competencia), DataUtil.getMes(competencia), DataUtil.getAno(competencia), adHocTenant.getTenantID(), funcionario);
        } else {
            return getPureList(jpql.toString(), DataUtil.getDia(competencia), DataUtil.getMes(competencia), DataUtil.getAno(competencia), adHocTenant.getTenantID());
        }
    }

}
