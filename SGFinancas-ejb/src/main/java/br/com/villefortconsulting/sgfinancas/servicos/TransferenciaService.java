package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.*;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TransferenciaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.TransferenciaContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.TransferenciaContaBancariaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransferenciaService extends BasicService<TransferenciaContaBancaria, TransferenciaContaBancariaRepositorio, TransferenciaContaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ExtratoContaCorrenteService extratoContaCorrenteService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new TransferenciaContaBancariaRepositorio(em, adHocTenant);
    }

    public void transfereConta(TransferenciaDTO transferencia) throws ContaException {
        if (transferencia != null) {
            Conta contaOrigem = criaContaPagarPorTransferencia(transferencia);
            Conta contaDestino = criaContaReceberPorTransferencia(transferencia);

            if (contaOrigem != null && contaDestino != null) {
                addTransferenciaContaBancaria(contaOrigem, contaDestino);
            } else {
                throw new CadastroException("Falha ao efetuar a transferência.", null);
            }
        }
    }

    public void addTransferenciaContaBancaria(Conta contaOrigem, Conta contaDestino) {
        TransferenciaContaBancaria transferencia = new TransferenciaContaBancaria();

        transferencia.setData(contaOrigem.getDataVencimento());
        transferencia.setValor(contaOrigem.getValor());
        transferencia.setObservacao(contaOrigem.getObservacao());
        transferencia.setTenatID(contaOrigem.getTenatID());

        List<ContaParcela> parcelaOrigem = contaService.listarContaParcela(contaOrigem);
        List<ContaParcela> parcelaDestino = contaService.listarContaParcela(contaDestino);

        if (!parcelaOrigem.isEmpty() && !parcelaDestino.isEmpty()) {
            parcelaOrigem.forEach(transferencia::setIdParcelaOrigem);
            parcelaDestino.forEach(transferencia::setIdParcelaDestino);
        }

        adicionar(transferencia);
    }

    public Conta criaContaPagarPorTransferencia(TransferenciaDTO transferencia) throws ContaException {
        Conta conta = new Conta();

        conta.setDataVencimento(transferencia.getDataTransferencia());
        conta.setIdContaBancaria(transferencia.getIdContaOrigem());
        CentroCusto centroCusto = transferencia.getIdContaOrigem().getIdCentroCusto();
        if (centroCusto != null) {
            conta.setIdCentroCusto(centroCusto);
        }
        conta.setValor(transferencia.getValorTransferencia());
        conta.setValorTotal(transferencia.getValorTransferencia());
        conta.setTipoConta(EnumTipoConta.TRANSFERENCIA.getTipo());
        PlanoConta planoConta = planoContaService.buscarPlanoContaTransferencia("D",transferencia.getIdContaOrigem().getIdPlanoConta());
        /*if(planoConta == null){
            throw new ContaException("Conta bancária não possui plano de contas de transferência. Acesse o cadastro da conta e realize o salvamento.", null);
        }*/
        conta.setIdPlanoConta(planoConta);
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
        conta.setRepetirConta("N");
        conta.setInformarPagamento("N");
        conta.setNumeroParcelas(1);

        if (transferencia.getObservacao() != null) {
            conta.setObservacao(transferencia.getObservacao());
        }

        contaService.adicionarContaEParcela(conta);

        contaService.pagarParcelasEConta(conta);

        return conta;

    }

    public Conta criaContaReceberPorTransferencia(TransferenciaDTO transferencia) throws ContaException {
        Conta conta = new Conta();

        conta.setDataVencimento(transferencia.getDataTransferencia());
        conta.setIdContaBancaria(transferencia.getIdContaDestino());
        CentroCusto centroCusto = transferencia.getIdContaDestino().getIdCentroCusto();
        if (centroCusto != null) {
            conta.setIdCentroCusto(centroCusto);
        }
        conta.setValor(transferencia.getValorTransferencia());
        conta.setValorTotal(transferencia.getValorTransferencia());
        conta.setTipoConta(EnumTipoConta.TRANSFERENCIA.getTipo());
        PlanoConta planoConta = planoContaService.buscarPlanoContaTransferencia("C",transferencia.getIdContaDestino().getIdPlanoConta());
        /*if(planoConta == null){
            throw new ContaException("Conta bancária não possui plano de contas de transferência. Acesse o cadastro da conta e realize o salvamento.", null);
        }*/
        conta.setIdPlanoConta(planoConta);
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
        conta.setRepetirConta("N");
        conta.setInformarPagamento("N");
        conta.setNumeroParcelas(1);

        if (transferencia.getObservacao() != null) {
            conta.setObservacao(transferencia.getObservacao());
        }

        contaService.adicionarContaEParcela(conta);

        contaService.pagarParcelasEConta(conta);

        return conta;
    }

    @Override
    public Criteria getModel(TransferenciaContaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addEqRestrictionTo(criteria, "observacao", filter.getDescricao());

        return criteria;
    }

    public List<Object> getDadosAuditoriaContaParcelaByID(ContaParcela obj) {
        return repositorio.getDadosAuditoriaByID(ContaParcela.class, obj.getId());
    }

    public boolean cancelarTransferencia(TransferenciaContaBancaria tcb) {
        try {
            Arrays.asList(tcb.getIdParcelaDestino(), tcb.getIdParcelaOrigem()).forEach(cp -> {
                cp.getIdConta().setValorPago(0d);
                cp.getIdConta().setContaParcelaList(contaService.listarContaParcela(cp.getIdConta()));
                cp.getIdConta().getContaParcelaList().forEach(aux -> {
                    cp.getIdConta().getContaParcelaList().get(0).setValorPago(null);
                    aux.setDataPagamento(null);
                    aux.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                    aux.setCanceladoAgora(true);
                });

                contaService.salvar(cp.getIdConta());
                extratoContaCorrenteService.verificarAtualizarExtratoParcelas(cp.getIdConta().getContaParcelaList());
            });

            repositorio.removeEntity(tcb);

            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            return false;
        }
    }

}
