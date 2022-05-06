package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Ponto;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import javax.persistence.EntityManager;

public class PontoRepositorio extends BasicRepository<Ponto> {

    public PontoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PontoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Date buscarHoraEntradaSaida(Funcionario funcionario, Integer dia, Date competencia, String tipoHora) {
        String jpql = "select p.dataPonto from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2  and year(p.dataPonto) = ?3  and p.idFuncionario = ?4 and p.tipoHora = ?5 ";
        return getPurePojoTop1(jpql, dia, DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, tipoHora);
    }

    public Date buscarHoraEntradaSaida(Funcionario funcionario, String tipoHora) {
        String jpql = "select p.dataPonto from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2  and year(p.dataPonto) = ?3  and p.idFuncionario = ?4 and p.tipoHora = ?5 ";
        return getPurePojoTop1(jpql, DataUtil.getDia(DataUtil.getHoje()), DataUtil.getMes(DataUtil.getHoje()), DataUtil.getAno(DataUtil.getHoje()), funcionario, tipoHora);
    }

    public Long quantidadeRegistrosDia(Funcionario funcionario) {
        String jpql = "select count(p.id) from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2  and year(p.dataPonto) = ?3 and p.idFuncionario = ?4  ";
        return getPurePojo(Long.class, jpql, DataUtil.getDia(DataUtil.getHoje()), DataUtil.getMes(DataUtil.getHoje()), DataUtil.getAno(DataUtil.getHoje()), funcionario);
    }

    public Ponto buscarPeriodoDia(Funcionario funcionario, Integer competencia, String tipoHora) {
        String jpql = "select p from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2 and year(p.dataPonto) = ?3 and p.idFuncionario = ?4 and p.tipoHora = ?5 ";
        return getPurePojo(jpql, competencia, DataUtil.getMes(DataUtil.getHoje()), DataUtil.getAno(DataUtil.getHoje()), funcionario, tipoHora);
    }

    public Ponto buscarPontoPorTipo(Funcionario funcionario, Date competencia, String tipoHora) {
        String jpql = "select p from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2 and year(p.dataPonto) = ?3 and p.idFuncionario = ?4 and p.tipoHora = ?5 ";
        return getPurePojo(jpql, DataUtil.getDia(competencia), DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, tipoHora);
    }

    public Ponto buscarPonto(Funcionario funcionario, Date competencia, String tipoHora, Integer dia) {
        String jpql = "select p from Ponto p where day(p.dataPonto) = ?1 and month(p.dataPonto) = ?2 and year(p.dataPonto) = ?3 and p.idFuncionario = ?4 and p.tipoHora = ?5 ";
        return getPurePojo(jpql, dia, DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, tipoHora);
    }

    public void removePontoPorTipo(Funcionario funcionario, Date competencia) {
        String jpql = "delete Ponto p where month(p.dataPonto) = ?1 and year(p.dataPonto) = ?2 and p.idFuncionario = ?3 and tenatID = ?4 ";
        executeCommand(jpql, DataUtil.getMes(competencia), DataUtil.getAno(competencia), funcionario, adHocTenant.getTenantID());
    }

}
