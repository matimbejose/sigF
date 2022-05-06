package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import java.util.List;
import javax.persistence.EntityManager;

public class FuncionarioServicoRepositorio extends BasicRepository<FuncionarioServico> {

    public FuncionarioServicoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FuncionarioServicoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<FuncionarioServico> getListaByFuncionario(Funcionario funcionario) {
        String hql = "select fs from FuncionarioServico fs where fs.idFuncionario = ?1";
        return getPureList(hql, funcionario);
    }

    public List<FuncionarioServico> getListaByServico(Servico servico) {
        String hql = "select fs from FuncionarioServico fs where fs.idServico = ?1";
        return getPureList(hql, servico);
    }

}
