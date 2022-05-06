package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.IntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancariaContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.dto.IntegracaoParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntegracaoBancariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.IntegracaoBancariaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.util.ListUtil;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javax.servlet.http.Part;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.ResponseMessageSet;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.Transaction;
import net.sf.ofx4j.io.AggregateUnmarshaller;
import net.sf.ofx4j.io.OFXParseException;
import org.apache.commons.lang3.StringUtils;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class IntegracaoBancariaService extends BasicService<IntegracaoBancaria, IntegracaoBancariaRepositorio, IntegracaoBancariaFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService contaService;

    @EJB
    private BancoService bancoService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new IntegracaoBancariaRepositorio(em, adHocTenant);
    }

    public IntegracaoBancaria buscar(String id) {
        return buscar(Integer.parseInt(id));
    }

    public List<IntegracaoBancaria> getIntegracaoBancarias() {
        return repositorio.getIntegracaoBancarias();
    }

    public TransacaoIntegracaoBancaria addTransacaoIntegracaoBancaria(TransacaoIntegracaoBancaria obj) {
        return repositorio.addTransacaoIntegracaoBancaria(obj);
    }

    public TransacaoIntegracaoBancaria setTransacaoIntegracaoBancaria(TransacaoIntegracaoBancaria obj) {
        return repositorio.setTransacaoIntegracaoBancaria(obj);
    }

    public IntegracaoBancaria importarArquivoIntegracao(Part file, IntegracaoBancaria integracaoBancaria) {
        try {
            integracaoBancaria.setTenatID(adHocTenant.getTenantID());
            List<TransacaoIntegracaoBancaria> transacoes = new LinkedList<>();

            AggregateUnmarshaller a = new AggregateUnmarshaller(ResponseEnvelope.class);
            ResponseEnvelope re = (ResponseEnvelope) a.unmarshal(file.getInputStream());

            //como não existe esse get "BankStatementResponse bsr = re.getBankStatementResponse();"
            //fiz esse codigo para capturar a lista de transações
            MessageSetType type = MessageSetType.banking;
            ResponseMessageSet message = re.getMessageSet(type);

            if (message != null) {
                List<BankStatementResponseTransaction> bank = ((BankingResponseMessageSet) message).getStatementResponses();
                for (BankStatementResponseTransaction b : bank) {

                    if (StringUtils.isNotEmpty(b.getMessage().getAccount().getAccountNumber())) {
                        integracaoBancaria.setConta(b.getMessage().getAccount().getAccountNumber());
                    } else {
                        throw new CadastroException("Não há informações sobre numero da conta no arquivo", null);
                    }
                    if (StringUtils.isNotEmpty(b.getMessage().getAccount().getBranchId())) {
                        integracaoBancaria.setAgencia(b.getMessage().getAccount().getBranchId());
                    } else {
                        integracaoBancaria.setAgencia("");
                    }
                    integracaoBancaria.setValorTotal(b.getMessage().getLedgerBalance().getAmount());
                    integracaoBancaria.setCompetencia(b.getMessage().getLedgerBalance().getAsOfDate());
                    integracaoBancaria.setBanco(b.getMessage().getAccount().getBankId());

                    List<Transaction> list = b.getMessage().getTransactionList().getTransactions();
                    if (list == null) {
                        throw new CadastroException("Não há transações no extrato importado.Favor escolher outro arquivo", null);
                    }
                    for (Transaction transaction : list) {

                        TransacaoIntegracaoBancaria transacaoIntegracaoBancaria = new TransacaoIntegracaoBancaria();
                        transacaoIntegracaoBancaria.setTipo(transaction.getTransactionType().name());
                        transacaoIntegracaoBancaria.setCodigo(transaction.getId());
                        transacaoIntegracaoBancaria.setData(transaction.getDatePosted());
                        transacaoIntegracaoBancaria.setValor(transaction.getAmount());
                        transacaoIntegracaoBancaria.setDescricao(transaction.getMemo());
                        transacaoIntegracaoBancaria.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.NOVO.getSituacao());
                        transacaoIntegracaoBancaria.setTenatID(adHocTenant.getTenantID());
                        transacaoIntegracaoBancaria.setIdIntegracaoBancaria(integracaoBancaria);

                        transacoes.add(transacaoIntegracaoBancaria);
                    }
                }
            }

            integracaoBancaria.setTransacaoIntegracaoBancariaList(transacoes);

            return adicionar(integracaoBancaria);
        } catch (OFXParseException | IOException ex) {
            throw new CadastroException(ex.getMessage(), ex);
        }
    }

    public List<ContaBancaria> listarContasVinculacao(IntegracaoBancaria integracaoBancaria) {
        Banco banco = bancoService.buscarBancoByNumero(integracaoBancaria.getBanco());
        List<ContaBancaria> contas;
        String nomeBanco;
        if (banco != null) {
            contas = contaBancariaService.listarContasByBanco(banco);
            nomeBanco = banco.getDescricao();
        } else {
            contas = contaBancariaService.listarAtivas();
            nomeBanco = "selecionado";
        }

        if (contas.size() > 1) {
            boolean temConta = StringUtils.isNotEmpty(integracaoBancaria.getConta());
            boolean temAgencia = StringUtils.isNotEmpty(integracaoBancaria.getAgencia());
            List<ContaBancaria> contaExistente = contas.stream()
                    .filter(c -> (temConta && integracaoBancaria.getConta().equals(c.getConta()))
                    || (temAgencia && integracaoBancaria.getAgencia().equals(c.getAgencia())))
                    .collect(Collectors.toList());
            if (!contaExistente.isEmpty()) {
                return contaExistente;
            }
        } else if (contas.isEmpty()) {
            throw new CadastroException("Não foi encontrado no sistema uma conta bancária do banco " + nomeBanco + ".", null);
        }
        return contas;
    }

    public List<IntegracaoParcelaDTO> conciliarIntegracaoParcela(IntegracaoBancaria integracaoBancaria, ContaBancaria contaBancaria) {
        return repositorio.listarTransacaoIntegracaoBancaria(integracaoBancaria).stream()
                .map(transacao -> {
                    if (transacao.getTransacaoIntegracaoBancariaContaParcelaList() == null) {
                        transacao.setTransacaoIntegracaoBancariaContaParcelaList(new ArrayList<>());
                    }
                    switch (transacao.getTransacaoIntegracaoBancariaContaParcelaList().size()) {
                        case 0:
                            transacao.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_NAO_ENCONTRADA.getSituacao());
                            break;
                        case 1:
                            transacao.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_ENCONTRADA.getSituacao());
                            break;
                        default:
                            transacao.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.MAIS_PARCELA_ENCONTRADA.getSituacao());
                            break;
                    }

                    IntegracaoParcelaDTO integracaoParcela = new IntegracaoParcelaDTO();
                    integracaoParcela.setContaBancaria(contaBancaria);
                    integracaoParcela.setTransacaoIntegracaoBancaria(transacao);
                    integracaoParcela.setParcelas(transacao.getTransacaoIntegracaoBancariaContaParcelaList().stream()
                            .map(TransacaoIntegracaoBancariaContaParcela::getIdContaParcela)
                            .collect(Collectors.toList()));
                    repositorio.setTransacaoIntegracaoBancaria(transacao);
                    return integracaoParcela;
                }).collect(Collectors.toList());
    }

    public ContaParcela criarContaParcelaAtravesTransacaoBancaria(IntegracaoParcelaDTO integracaoParcelaDTO) throws ContaException {
        PlanoConta planoConta = planoContaService.obterPlanoContaPorDescricao(integracaoParcelaDTO.getContaBancaria().getDescricao(), adHocTenant.getTenantID());

        if (planoConta == null) {
            throw new CadastroException("Plano de conta não cadastrado para conta bancária informada", null);
        }

        Conta conta = new Conta();
        conta.setIdContaBancaria(integracaoParcelaDTO.getContaBancaria());
        conta.setIdPlanoConta(planoConta);
        conta.setValor(Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor()));
        conta.setValorTotal(conta.getValor());
        conta.setDataVencimento(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getData());
        conta.setNumeroParcelas(1);
        conta.setTipoConta(EnumTipoConta.NORMAL.getTipo());
        conta.setTipoTransacao(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor() < 0 ? "P" : "R");
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setObservacao(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getDescricao());

        conta = contaService.adicionarContaEParcela(conta);
        return contaService.pagarParcelaIntegral(conta.getContaParcelaList().get(0));
    }

    public void criarTodasParcelasNaoConciliadas(List<IntegracaoParcelaDTO> listaIntegracao) throws ContaException {

        for (IntegracaoParcelaDTO integracaoParcelaDTO : listaIntegracao) {
            if (EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_NAO_ENCONTRADA.getSituacao().equals(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getSituacao())) {
                criarContaParcelaAtravesTransacaoBancaria(integracaoParcelaDTO);
            }
        }
    }

    public void associarParcelaTransacaoBancaria(IntegracaoParcelaDTO integracaoParcelaDTO, List<ContaParcela> parcelas, FormaPagamento formaPagamento) {
        TransacaoIntegracaoBancaria transacao = integracaoParcelaDTO.getTransacaoIntegracaoBancaria();
        List<TransacaoIntegracaoBancariaContaParcela> listaParcelas = parcelas.stream()
                .map(parcela -> {
                    TransacaoIntegracaoBancariaContaParcela tibcp = new TransacaoIntegracaoBancariaContaParcela();
                    tibcp.setIdContaParcela(parcela);
                    tibcp.setIdTransacaoIntegracaoBancaria(transacao);
                    tibcp.setTenatID(adHocTenant.getTenantID());
                    tibcp.setProcessado(TransacaoIntegracaoBancariaContaParcela.contains(transacao.getTransacaoIntegracaoBancariaContaParcelaList(), tibcp).isPresent() ? "S" : "N");

                    return tibcp;
                })
                .collect(Collectors.toList());
        try {
            ListUtil.persist(transacao.getTransacaoIntegracaoBancariaContaParcelaList(), listaParcelas, TransacaoIntegracaoBancariaContaParcela::contains);
            transacao.getTransacaoIntegracaoBancariaContaParcelaList().forEach(item -> {
                if (item.getProcessado().equals("S")) {
                    return;
                }
                if (item.getIdContaParcela().getDataPagamento() == null) {
                    item.getIdContaParcela().setDataPagamento(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getData());
                    item.getIdContaParcela().setIdFormaPagamento(formaPagamento);
                    item.getIdContaParcela().setValorPago(Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor()));
                    if (item.getIdContaParcela().getValor() > Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor())) {
                        contaService.pagarParcelaParcial(item.getIdContaParcela(), item.getIdContaParcela().getValorPago());
                    }
                    if (item.getIdContaParcela().getValor() <= Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor())) {
                        try {
                            contaService.pagarParcelaIntegral(item.getIdContaParcela());
                        } catch (ContaException ex) {
                            Logger.getLogger(IntegracaoBancariaService.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    if (item.getIdContaParcela().getSituacao().equals(EnumSituacaoConta.PAGA.getSituacao())) {
                        contaService.desfazerBaixa(item.getIdContaParcela());
                        item.getIdContaParcela().setDataPagamento(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getData());
                        item.getIdContaParcela().setIdFormaPagamento(formaPagamento);
                        item.getIdContaParcela().setValorPago(Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor()));
                        try {
                            contaService.pagarParcelaIntegral(item.getIdContaParcela());
                        } catch (ContaException ex) {
                            Logger.getLogger(IntegracaoBancariaService.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                    if (item.getIdContaParcela().getSituacao().equals(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao())) {
                        if ((Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor()) > item.getIdContaParcela().getValorPago())) {
                            if (Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor()) > (item.getIdContaParcela().getValor() - item.getIdContaParcela().getValorPago())) {
                                item.getIdContaParcela().setValorPago(item.getIdContaParcela().getValor() - item.getIdContaParcela().getValorPago());
                                item.getIdContaParcela().setDataPagamento(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getData());
                                contaService.pagarParcelaParcial(item.getIdContaParcela(), item.getIdContaParcela().getValorPago());
                            }
                        }
                    }
                }
            });
        } catch (IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(IntegracaoBancariaService.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (transacao.getTransacaoIntegracaoBancariaContaParcelaList().isEmpty()) {
            transacao.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_NAO_ENCONTRADA.getSituacao());
        } else {
            transacao.setSituacao(EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_ENCONTRADA.getSituacao());
        }

        repositorio.setTransacaoIntegracaoBancaria(transacao);
    }

    public void updateValorConciliado(ContaParcela cp) {
        cp.setValorConciliado(repositorio.valorConciliadoPor(cp));
    }

}
