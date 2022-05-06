package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioAtendimento;
import java.util.List;
import javax.persistence.EntityManager;

public class FuncionarioAtendimentoRepositorio extends BasicRepository<FuncionarioAtendimento> {

    public FuncionarioAtendimentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FuncionarioAtendimentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<FuncionarioAtendimento> getListaByFuncionario(Funcionario funcionario) {
        String hql = "select fa from FuncionarioAtendimento fa where fa.idFuncionario = ?1";
        return getPureList(hql, funcionario);
    }

}
