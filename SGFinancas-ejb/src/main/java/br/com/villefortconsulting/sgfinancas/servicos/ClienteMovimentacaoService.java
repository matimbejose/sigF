package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioAnaliticoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteSaldoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FaturaCallbackDto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoMovimentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteMovimentacaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ClienteMovimentacaoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.ArrayList;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClienteMovimentacaoService extends BasicService<ClienteMovimentacao, ClienteMovimentacaoRepositorio, ClienteMovimentacaoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ContaService contaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ClienteMovimentacaoRepositorio(em, adHocTenant);
    }

    @Override
    public ClienteMovimentacao salvar(ClienteMovimentacao cm) {
        if (cm.getAtivo() == null) {
            cm.setAtivo("S");
        }
        return super.salvar(cm);
    }

    @Override
    public void remover(ClienteMovimentacao cm) {
        if (cm.getDataPagamento() != null && "IUGU".equals(cm.getOrigem())) {
            Conta conta = contaService.buscarContaByCliemteMovimentacao(cm);
            List<ContaParcela> parcelas = contaService.listarContaParcela(conta);
            parcelas.forEach(contaService::cancelamentoBaixaEConta);
            conta = contaService.buscarContaByCliemteMovimentacao(cm);
            contaService.remover(conta);
        }

        cm.setAtivo("N");
        salvar(cm);
    }

    public boolean temMovimentacoes(Cliente cliente) {
        return !repositorio.listaTodasMovimentacoesPor(cliente).isEmpty();
    }

    public boolean temMovimentacoes(Cliente cliente, String idIntegracao) {
        if (idIntegracao == null || idIntegracao.trim().isEmpty()) {
            return false;
        }
        return !repositorio.listaTodasMovimentacoesPor(cliente, idIntegracao).isEmpty();
    }

    public ClienteMovimentacao buscaMovimentobyIdIntegracao(Cliente cliente, String idIntegracao) {
        return repositorio.findMovimentacaobyIdIntegracao(cliente, idIntegracao);
    }

    public ClienteMovimentacao buscaMovimentobyIdIntegracao(String idIntegracao) {
        return repositorio.findMovimentacaobyIdIntegracao(idIntegracao);
    }

    public List<ClienteMovimentacao> listaTodasMovimentacoesPor(Cliente cliente) {
        return repositorio.listaTodasMovimentacoesPor(cliente);
    }

    public ClienteMovimentacao realizaMovimentacaoInicial(Double valor, Cliente cliente) {
        ClienteMovimentacao cm = new ClienteMovimentacao();
        cm.setIdCliente(cliente);
        cm.setOrigem(EnumTipoClienteMovimentacao.SALDO_INICIAL.getTipo());
        cm.setDataMovimentacao(DataUtil.converterStringParaDate("01/01/2018"));
        cm.setDataVencimento(DataUtil.converterStringParaDate("01/01/2018"));
        cm.setValorPrevisto(valor);
        cm.setValorSaldo(valor);
        cm.setValorSaldoAnterior(0d);
        cm.setTenatID(adHocTenant.getTenantID());
        cm.setStatus("Movimentação inicial");
        cm.setAtivo("S");

        if (cliente.getClienteMovimentacaoList() == null) {
            cliente.setClienteMovimentacaoList(new ArrayList<>());
        }
        cliente.getClienteMovimentacaoList().add(cm);
        cliente.setSaldoInicial(valor);
        clienteService.salvar(cliente);
        return cm;
    }

    public ClienteMovimentacao realizaCorrecaoDeSaldo(Double valor, Cliente cliente) {
        Double saldoAtual = cliente.getClienteMovimentacaoList().get(cliente.getClienteMovimentacaoList().size() - 1).getValorSaldo();
        if (valor - saldoAtual == 0) {
            return null;
        }
        ClienteMovimentacao cm = new ClienteMovimentacao();
        cm.setIdCliente(cliente);
        cm.setOrigem(EnumTipoClienteMovimentacao.CORRECAO.getTipo());
        cm.setDataMovimentacao(new Date());
        cm.setDataVencimento(new Date());
        cm.setValorPrevisto(valor - saldoAtual);
        cm.setTenatID(cliente.getTenatID());
        cm.setValorSaldo(valor);
        cm.setValorSaldoAnterior(cliente.getSaldoAtual());
        cm.setStatus("Correção de saldo");

        cliente.getClienteMovimentacaoList().add(cm);
        cliente.setSaldoAtual(valor);
        return cm;
    }

    public ClienteMovimentacao lancarMovimentacao(Cliente cliente, ImportacaoMovimentoDTO im) {
        return lancarMovimentacao(cliente, (im.getValor() - im.getValorDesconto()), im.getDataMovimentacao(), im.getOrigem(), im.getIdIntegracao(), im.getDataMovimentacao(), im.getCentroCusto());
    }

    public ClienteMovimentacao lancarMovimentacao(Cliente cliente, FaturaCallbackDto fatura, EnumTipoClienteMovimentacao origem, CentroCusto centro) {
        return lancarMovimentacao(cliente, fatura.getTotalFatura(), fatura.getDataVencimento(), origem, fatura.getIdFatura(), fatura.getDataCriacao(), centro);
    }

    public ClienteMovimentacao lancarMovimentacao(Cliente cliente, Double valor, Date vencimento, EnumTipoClienteMovimentacao origem, String idIntegracao, Date dataMovimentacao, CentroCusto centroCusto) {
        return lancarMovimentacao(cliente, valor, vencimento, origem, idIntegracao, dataMovimentacao, centroCusto, null);
    }

    public ClienteMovimentacao lancarMovimentacao(Cliente cliente, Double valor, Date vencimento, EnumTipoClienteMovimentacao origem, String idIntegracao, Date dataMovimentacao, CentroCusto centroCusto, String tipoMovimento) {
        if (!temMovimentacoes(cliente)) {
            realizaMovimentacaoInicial(0d, cliente);
        }
        if (temMovimentacoes(cliente, idIntegracao)) {
            throw new CadastroException("Movimentação já lançada", null);
        }
        ClienteMovimentacao cm = new ClienteMovimentacao();
        cm.setAtivo("S");
        cm.setIdCliente(cliente);
        cm.setOrigem(origem.getTipo());
        cm.setDataMovimentacao(dataMovimentacao);
        cm.setDataVencimento(vencimento);
        cm.setValorPrevisto(valor);
        cm.setValorPago(0d);
        cm.setAtivo("S");
        cm.setStatus("Pendente");
        cm.setIdIntegracao(idIntegracao);
        cm.setTipoMovimentacao(tipoMovimento);
        cm.setValorSaldo(cliente.getSaldoAtual() + cm.getValor());
        cm.setValorSaldoAnterior(cliente.getSaldoAtual());
        cm.setTenatID(adHocTenant.getTenantID());
        cm.setIdCentroCusto(centroCusto);
        cm = salvar(cm);
        cliente = clienteService.buscar(cliente.getId());
        cliente.setSaldoAtual(cliente.getSaldoAtual() + cm.getValor());
        clienteService.salvar(cliente);
        return cm;
    }

    public ClienteMovimentacao editarMovimentacao(ClienteMovimentacao cm, Double valor) {
        cm.setValorPrevisto(valor);
        return salvar(cm);
    }

    public ClienteMovimentacao pagar(ClienteMovimentacao cm, Date dataPagamento, Double valorPago, Double juros, Double taxa, EnumTipoClienteMovimentacao tipo) {
        EnumTipoClienteMovimentacao tipoAtual = EnumTipoClienteMovimentacao.retornaEnumSelecionado(cm.getOrigem());
        if (!tipoAtual.isPermiteAlterar()) {
            throw new CadastroException("Não é possível alterar uma movimentação com o tipo " + tipoAtual.getDescricao(), null);
        } else if (!tipo.isTipoPagamento()) {
            throw new CadastroException("O tipo " + tipoAtual.getDescricao() + " não pode ser utilizado na operação 'pagar'.", null);
        }
        Double saldo = cm.getIdCliente().getSaldoAtual() + valorPago;
        cm.setDataPagamento(dataPagamento);
        cm.setValorJuros(juros);
        cm.setValorTaxa(taxa);
        cm.setValorPago(valorPago);
        cm.setOrigem(tipo.getTipo());
        cm.setStatus("Pago");
        salvar(cm);
        List<ClienteMovimentacao> movimentacoes = listaTodasMovimentacoesPor(cm.getIdCliente());
        Double saldoNovo = 0d;
        for (ClienteMovimentacao aux : movimentacoes) {
            aux.setValorSaldoAnterior(saldoNovo);
            saldoNovo += aux.getValor();
            aux.setValorSaldo(saldoNovo);
        }
        cm.getIdCliente().setSaldoAtual(saldo);
        cm.getIdCliente().getClienteMovimentacaoList().clear();
        cm.getIdCliente().getClienteMovimentacaoList().addAll(movimentacoes);
        clienteService.salvar(cm.getIdCliente());
        return listaTodasMovimentacoesPor(cm.getIdCliente()).stream()
                .filter(m -> m.getId().equals(cm.getId()))
                .findAny().orElse(cm);
    }

    public Cliente atualizarSaldo(Cliente cliente) {
        List<ClienteMovimentacao> lista = repositorio.listaTodasMovimentacoesPor(cliente);
        Double saldo = cliente.getSaldoInicial();
        if (saldo == null) {
            saldo = 0d;
        }
        for (ClienteMovimentacao cm : lista) {
            cm.setValorSaldoAnterior(saldo);
            saldo += cm.getValor();
            cm.setValorSaldo(saldo);
        }
        cliente.getClienteMovimentacaoList().clear();
        cliente.getClienteMovimentacaoList().addAll(lista);
        cliente.setSaldoAtual(saldo);
        return clienteService.salvar(cliente);
    }

    public boolean cancelarMovimentacao(ImportacaoMovimentoDTO im) {
        return cancelarMovimentacao(im.getIdIntegracao(), im.getDataMovimentacao());
    }

    public boolean cancelarMovimentacao(String idIntegracao, Date dataCancelamento) {
        ClienteMovimentacao cm = buscaMovimentobyIdIntegracao(idIntegracao);
        if (cm == null) {
            return false;
        }
        cm.setDataCancelamento(dataCancelamento);
        cm.setStatus("Cancelado");
        alterar(cm);
        atualizarSaldo(cm.getIdCliente());
        return true;
    }

    public List<ClienteMovimentacaoRelatorioAnaliticoDTO> listaMovimentacaoRelatorio(Cliente cliente, Date inicio, Date fim, Empresa empresa, CentroCusto centro) {
        return repositorio.listaMovimentacoesRelatorio(cliente, inicio, fim, empresa, centro);
    }

    public List<ClienteMovimentacaoRelatorioAnaliticoDTO> listaMovimentacaoSemIUGURelatorio(Date inicio, Date fim, Empresa empresa, Cliente cliente) {
        return repositorio.listaMovimentacoesSemIuguRelatorio(cliente, inicio, fim, empresa);
    }

    public List<ClienteSaldoDTO> listaSaldo(MinMax<Date> datas, CentroCusto centro) {
        return listaSaldo(datas.getMin(), datas.getMax(), centro);
    }

    public List<ClienteSaldoDTO> listaSaldo(Date dataInicial, Date dataFinal, CentroCusto centro) {
        return repositorio.listaSaldo(dataInicial, dataFinal, centro);
    }

    public Double getSaldoAnterior(Date dataInicial) {
        return repositorio.getSaldoAnterior(dataInicial);
    }

    public List<Cliente> clientesComIuguNoPeriodo(Date inicio, Date fim, String tenat) {
        return repositorio.clientesComIuguNoPeriodo(inicio, fim, tenat);
    }

    public List<Cliente> clientesComNotasNoPeriodo(Date inicio, Date fim, String tenat) {
        return repositorio.clientesComNotasNoPeriodo(inicio, fim, tenat);
    }

    public List<ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO> acumuladoPorTipoMovimentacao(Date inicio, Date fim, String tenat) {
        return repositorio.acumuladoPorTipoMovimentacao(inicio, fim, tenat);
    }

}
