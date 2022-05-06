package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaLancamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PlanoContaLancamentoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoPlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanoContaLancamentoService extends BasicService<PlanoContaLancamentoContabil, PlanoContaLancamentoRepositorio, PlanoContaLancamentoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PlanoContaLancamentoRepositorio(em, adHocTenant);
    }

    @Override
    public PlanoContaLancamentoContabil salvar(PlanoContaLancamentoContabil planoContaLancamentoContabil) {
        if (planoContaLancamentoContabil.getId() == null) {
            planoContaLancamentoContabil.setSituacao(EnumSituacaoPlanoContaLancamentoContabil.ATIVO.getSituacao());
            planoContaLancamentoContabil.setTenatID(adHocTenant.getTenantID());

            // Adicionando conta de credito
            if (planoContaLancamentoContabil.getIdPlanoContaCredito() != null) {
                try {
                    adicionaContaDebitoCreditoPorLancamentoContabilPaga(planoContaLancamentoContabil, EnumTipoTransacao.RECEBER.getTipo());
                } catch (ContaException ex) {
                    throw new CadastroException(ex.getMessage(), ex);
                }
            }

            // Adicionando conta de debito
            if (planoContaLancamentoContabil.getIdPlanoContaDebito() != null) {
                try {
                    adicionaContaDebitoCreditoPorLancamentoContabilPaga(planoContaLancamentoContabil, EnumTipoTransacao.PAGAR.getTipo());
                } catch (ContaException ex) {
                    throw new CadastroException(ex.getMessage(), ex);
                }
            }

            return adicionar(planoContaLancamentoContabil);
        }
        return alterar(planoContaLancamentoContabil);
    }

    public void adicionaContaDebitoCreditoPorLancamentoContabilPaga(PlanoContaLancamentoContabil lancamento, String tipoTransacao) throws ContaException {
        Conta conta = new Conta();

        conta.setIdContaBancaria(lancamento.getIdContaBancaria());
        conta.setDataVencimento(lancamento.getData());
        conta.setIdPlanoConta(lancamento.getIdPlanoContaDebito());
        conta.setValor(lancamento.getValor());
        conta.setValorTotal(lancamento.getValor());

        if (EnumTipoTransacao.PAGAR.getTipo().equals(tipoTransacao)) {
            conta.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
        } else {
            conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
        }

        conta.setTipoConta(EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
        conta.setNumeroParcelas(1);
        conta.setRepetirConta("N");
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setValorPago(lancamento.getValor());

        contaService.adicionarContaEParcela(conta);
        contaService.pagarParcelasEConta(conta);
    }

    public List<PlanoContaLancamentoContabil> buscarPlanoContaLancamento(PlanoConta planoConta) {
        return repositorio.buscarPlanoContaLancamento(planoConta);
    }

    public List<PlanoContaLancamentoContabil> buscarLancamentoAcordoEmpresaLogada(Empresa empresa, Date dataInicio, Date dataFinal) {
        return repositorio.buscarPlanoContaLancamentoAcordoEmpresa(empresa, dataInicio, dataFinal);
    }

    @Override
    public Criteria getModel(PlanoContaLancamentoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public PlanoContaLancamentoContabil buscarLancamentoContabilPorConta(Conta conta) {
        return repositorio.buscarPlanoContaLancamentoContabilPorConta(conta);
    }

}
