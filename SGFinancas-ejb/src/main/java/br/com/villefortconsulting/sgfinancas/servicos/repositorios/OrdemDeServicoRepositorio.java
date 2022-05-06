package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ItensOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.StatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import java.util.List;
import javax.persistence.EntityManager;

public class OrdemDeServicoRepositorio extends BasicRepository<OrdemDeServico> {

    public OrdemDeServicoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public OrdemDeServicoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<OrdemDeServico> listar() {
        return getPureList(" select os from OrdemDeServico os ");
    }

    public List<OrdemDeServico> listarPorFunctionarioEntrada(Funcionario funcionario) {
        return getPureList(" select os from OrdemDeServico os where os.funcionarioEntrada = ?1", funcionario);
    }

    public List<OrdemDeServico> listarPorFunctionarioSaida(Funcionario funcionario) {
        return getPureList(" select os from OrdemDeServico os where os.funcionarioSaida = ?1", funcionario);
    }

    public List<OrdemDeServico> listarPorEmail(String email) {
        return getPureList(" select os from OrdemDeServico os where os.email = ?1 ", email);
    }

    public List<ItensOrdemDeServico> listarItens(OrdemDeServico os) {
        return getPureList(" select ios from ItensOrdemDeServico ios where ios.idOrdemDeServico = ?1 ", os);
    }

    public List<ItensOrdemDeServico> listarItensPorDescricao(OrdemDeServico os, String descricao) {
        return getPureList(" select ios from ItensOrdemDeServico ios where ios.idOrdemDeServico = ?1 and ios.nomeItem = ?2 ", os, descricao);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatus(OrdemDeServico os) {
        return getPureList(" select sos from StatusOrdemDeServico sos where sos.idOrdemDeServico = ?1 ", os);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorFuncionario(OrdemDeServico os, Funcionario funcionario) {
        return getPureList(" select sos from StatusOrdemDeServico sos where sos.idOrdemDeServico = ?1 and sos.idFuncionario = ?2 ", os, funcionario);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorStatus(OrdemDeServico os, String status) {
        return getPureList(" select sos from StatusOrdemDeServico sos where sos.idOrdemDeServico = ?1 and sos.status = ?2 ", os, status);
    }

    public List<StatusOrdemDeServico> listarMudancasDeStatusPorStatus(OrdemDeServico os, EnumStatusOrdemDeServico status) {
        return listarMudancasDeStatusPorStatus(os, status.getCodigo());
    }

}
