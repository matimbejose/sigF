package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class PlanoContaLancamentoRepositorio extends BasicRepository<PlanoContaLancamentoContabil> {

    public PlanoContaLancamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PlanoContaLancamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<PlanoContaLancamentoContabil> listar() {
        String jpql = "select c from PlanoContaLancamentoContabil c order by c.data";
        return getPureList(jpql);
    }

    public List<PlanoContaLancamentoContabil> buscarPlanoContaLancamento(PlanoConta planoConta) {
        String jpql = "select c from PlanoContaLancamentoContabil c where c.idPlanoContaDebito =?1 or c.idPlanoContaCredito =?1";
        return getPureList(jpql, planoConta);
    }

    public List<PlanoContaLancamentoContabil> buscarPlanoContaLancamentoAcordoEmpresa(Empresa empresa, Date dataInicio, Date dataFinal) {
        String jpql = "select c from PlanoContaLancamentoContabil c where c.tenatID =?1 and c.data between ?2 and ?3";
        return getPureList(jpql, empresa.getTenatID(), DataUtil.getStartDate(dataInicio), DataUtil.getEndDate(dataFinal));
    }

    public PlanoContaLancamentoContabil buscarPlanoContaLancamentoContabilPorConta(Conta conta) {
        String jpql = "select c from PlanoContaLancamentoContabil c where c.idConta = ?1";
        return getPurePojo(jpql, conta);
    }

}
