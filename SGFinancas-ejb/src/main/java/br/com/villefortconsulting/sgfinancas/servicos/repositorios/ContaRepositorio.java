package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaSinteticaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoItemDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FechamentoContabilDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.LancamentoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MediaPrazoRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResultadoPlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TimelineDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AnaliseContaFiltro;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoReceitaDespesa;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import br.com.villefortconsulting.util.sql.QueryBuilder;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class ContaRepositorio extends BasicRepository<Conta> {

    public ContaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ContaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public ContaParcela adicionarContaParcela(ContaParcela contaParcela) {
        addEntity(contaParcela);
        return contaParcela;
    }

    public ContaParcelaParcial adicionarContaParcelaParcial(ContaParcelaParcial contaParcelaParcial) {
        addEntity(contaParcelaParcial);
        return contaParcelaParcial;
    }

    public ContaParcela alterarContaParcela(ContaParcela contaParcela) {
        return setEntity(contaParcela);
    }

    public void removerContaParcela(ContaParcela contaParcela) {
        removeEntity(contaParcela);
    }

    public void removerContaParcelaParcial(ContaParcelaParcial contaParcelaParcial) {
        removeEntity(contaParcelaParcial);
    }

    public ContaParcela buscarContaParcela(int id) {
        return getEntity(ContaParcela.class, id);
    }

    public ContaParcela buscarContaParcelaPorNumNF(String numero) {
        String jpql = "select b from ContaParcela b where b.numNf = ?1 ";
        List<ContaParcela> list = getPureList(jpql, numero);
        if (list.size() == 1) {
            return list.get(0);
        }
        list.removeIf(item -> item.getSituacao().equals("CC"));
        return !list.isEmpty() ? list.get(0) : null;
    }

    public List<Conta> listarContaPagar() {
        String jpql = "select b from Conta b where b.tipoTransacao = ?1 ";
        return getPureList(jpql, EnumTipoTransacao.PAGAR.getTipo());
    }

    public List<ContaParcela> listarParcelasPagamentoAtraso(String tenatID) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select a from ContaParcela a ");
        jpql.append(" where a.idConta.tipoTransacao = ?1 and a.tenatID = ?2  ");
        jpql.append(" and a.dataVencimento < ?3 ");
        jpql.append(" and a.dataPagamento is null ");
        jpql.append(" and a.idConta.situacao = ?4 ");
        return getPureList(jpql.toString(), EnumTipoTransacao.PAGAR.getTipo(), tenatID, DataUtil.getHoje(), EnumSituacaoConta.NAO_PAGA.getSituacao());
    }

    public List<ContaParcela> listarParcelasPagamentoVencimentoHoje(String tenatID) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select a from ContaParcela a ");
        jpql.append(" where a.idConta.tipoTransacao = ?1 and a.tenatID = ?2 ");
        jpql.append(" and a.dataVencimento = ?3 ");
        jpql.append(" and a.idConta.situacao = ?4 ");
        return getPureList(jpql.toString(), EnumTipoTransacao.PAGAR.getTipo(), tenatID, DataUtil.getHoje(), EnumSituacaoConta.NAO_PAGA.getSituacao());
    }

    public List<ContaParcela> listarParcelasPagamentoVencendoEm(Integer prazoNotificacao, String tenatID) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select a from ContaParcela a ");
        jpql.append(" where a.idConta.tipoTransacao = ?1 and a.tenatID = ?2 ");
        jpql.append(" and a.dataVencimento <= ?3  and a.dataVencimento > ?4 ");
        jpql.append(" and a.idConta.situacao = ?5 ");
        return getPureList(jpql.toString(), EnumTipoTransacao.PAGAR.getTipo(), tenatID, DataUtil.adicionarDias(DataUtil.getHoje(), prazoNotificacao), new Date(), EnumSituacaoConta.NAO_PAGA.getSituacao());
    }

    public List<Conta> listarContaReceber() {
        String jpql = "select b from Conta b where b.tipoTransacao = ?1 ";
        return getPureList(jpql, EnumTipoTransacao.RECEBER.getTipo());
    }

    public List<ContaParcela> listarContaParcela(Conta conta) {
        String jpql = " select cpp from ContaParcela cpp where cpp.idConta = ?1 order by cpp.numParcela ";
        return getPureList(jpql, conta);
    }

    public List<ContaParcela> listarContaParcelaSemTenat(Conta conta) {
        String jpql = "select cp from ContaParcela cp where cp.idConta = ?1 and cp.tenatID is not null order by cp.numParcela";
        return getPureList(jpql, conta);
    }

    public List<ContaParcela> listarParcelasPagasSemExtrato() {
        String jpql = " select p from ContaParcela p where p.situacao = ?1 and p.tenatID = ?2 and p.id not in ( ";
        jpql += " select e.idContaParcela.id from ExtratoContaCorrente e where e.idContaParcela is not null and e.tenatID = ?2) ";
        jpql += " order by p.idContaBancaria.descricao";
        return getPureList(jpql, EnumSituacaoConta.PAGA.getSituacao(), adHocTenant.getTenantID());
    }

    public List<ContaParcela> listarParcelasNaoPagasComExtrato() {
        String jpql = " select p from ContaParcela p where p.situacao = ?1 and p.tenatID = ?2 and p.id in ( ";
        jpql += " select e.idContaParcela.id from ExtratoContaCorrente e where e.idContaParcela is not null and e.tenatID = ?2) ";
        jpql += " order by p.idContaBancaria.descricao";
        return getPureList(jpql, EnumSituacaoConta.NAO_PAGA.getSituacao(), adHocTenant.getTenantID());
    }

    public ContaParcelaParcial buscarUltimaParcial(ContaParcela parcela) {
        String jpql = " select a from ContaParcelaParcial a where a.idContaParcela = ?1 order by a.id desc";
        return getPurePojoTop1(jpql, parcela);
    }

    public List<ContaParcelaParcial> listarParciais(ContaParcela parcela) {
        String jpql = " select a from ContaParcelaParcial a where a.idContaParcela = ?1 ";
        return getPureList(jpql, parcela);
    }

    public Double somaTotalPago(ContaParcela parcela) {
        String jpql = " select sum(a.valorPago) from ContaParcelaParcial a where a.idContaParcela = ?1 ";
        return getPurePojo(Double.class, jpql, parcela);
    }

    public boolean existeParcelasEmAberto(Conta conta) {
        String jpql = " select count(a.id) from ContaParcela a where a.idConta = ?1 and a.situacao = ?2 ";
        return getPurePojo(Long.class, jpql, conta, EnumSituacaoConta.NAO_PAGA.getSituacao()) > 0;
    }

    public Date buscarUltimaDataPagamento(Conta conta) {
        String jpql = " select a.dataPagamento from ContaParcela a where a.idConta = ?1 order by a.dataPagamento desc ";
        return getPurePojoTop1(Date.class, jpql, conta);
    }

    public EstatisticaContaDTO buscarEstatisticasContaReceber(Date dataInicio, Date dataFim) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(  ");
        jpql.append(" sum(a.valor) - isNull(sum(a.valorPago), 0.0), ");
        jpql.append(" count(a.valor) - isNull(count(a.valorPago), 0.0), ");
        jpql.append(" (select sum(b.valor) - isNull(sum(b.valorPago), 0.0) from ContaParcela b where b.dataVencimento < ?6 and b.tenatID = ?1 and b.idConta.tipoTransacao = ?4 and b.idConta.situacao <> ?5), ");
        jpql.append(" (select count(b.valor) - isNull(count(b.valorPago), 0.0) from ContaParcela b where b.dataVencimento < ?6 and b.tenatID = ?1 and b.idConta.tipoTransacao = ?4 and b.idConta.situacao <> ?5), ");
        jpql.append(" sum(a.valorPago), ");
        jpql.append(" count(a.valorPago), ");
        jpql.append(" sum(a.valor),");
        jpql.append(" count(a.valor) ) ");
        jpql.append(" from ContaParcela a ");
        jpql.append(" where a.dataVencimento >= ?2 ");
        jpql.append(" and a.dataVencimento <= ?3 ");
        jpql.append(" and a.tenatID = ?1 ");
        jpql.append(" and a.idConta.tipoTransacao = ?4 ");
        jpql.append(" and a.situacao <> ?5 ");
        jpql.append(" and a.situacao <> ?7 ");
        return getPurePojo(jpql.toString(), adHocTenant.getTenantID(), dataInicio, dataFim, EnumTipoTransacao.RECEBER.getTipo(), EnumSituacaoConta.CANCELADO.getSituacao(), new Date(), EnumSituacaoConta.PAGA.getSituacao());
    }

    public EstatisticaContaDTO buscarEstatisticasContaPagar(Date dataInicio, Date dataFim) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(  ");
        jpql.append(" sum(a.valor) - isNull(sum(a.valorPago), 0.0), ");
        jpql.append(" count(a.valor) - isNull(count(a.valorPago), 0.0), ");
        jpql.append(" (select sum(b.valor) - isNull(sum(b.valorPago), 0.0) from ContaParcela b where b.dataVencimento < ?6 and b.tenatID = ?1 and b.idConta.tipoTransacao = ?4 and b.idConta.situacao <> ?5), ");
        jpql.append(" (select count(b.valor) - isNull(count(b.valorPago), 0.0) from ContaParcela b where b.dataVencimento < ?6 and b.tenatID = ?1 and b.idConta.tipoTransacao = ?4 and b.idConta.situacao <> ?5), ");
        jpql.append(" sum(a.valorPago), ");
        jpql.append(" count(a.valorPago), ");
        jpql.append(" sum(a.valor),");
        jpql.append(" count(a.valor) ) ");
        jpql.append(" from ContaParcela a ");
        jpql.append(" where a.dataVencimento >= ?2 ");
        jpql.append(" and a.dataVencimento <= ?3 ");
        jpql.append(" and a.tenatID = ?1 ");
        jpql.append(" and a.idConta.tipoTransacao = ?4 ");
        jpql.append(" and a.situacao <> ?5 ");
        return getPurePojo(jpql.toString(), adHocTenant.getTenantID(), dataInicio, dataFim, EnumTipoTransacao.PAGAR.getTipo(), EnumSituacaoConta.CANCELADO.getSituacao(), new Date());
    }

    public boolean possuiOutrasParcelasEmAberto(ContaParcela cp) {
        if (cp == null) {
            return false;
        }
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select par from ContaParcela par  ")
                .append(" where par.situacao = ?1 ")
                .append(" and par.idConta = ?2 ")
                .append(" and par.id <> ?3 ");
        return getPurePojoTop1(jpql.toString(), EnumSituacaoConta.NAO_PAGA.getSituacao(), cp.getIdConta(), cp.getId()) != null;
    }

    public List<ContaParcela> listarParcelasEmAberto(ContaParcela conta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select par from ContaParcela par  ");
        jpql.append(" where par.situacao = ?1 ");
        jpql.append(" and par.idConta = ?2 ");
        jpql.append(" and par.id <> ?3 ");
        return getPureList(jpql.toString(), EnumSituacaoConta.NAO_PAGA.getSituacao(), conta.getIdConta(), conta.getId());
    }

    public List<ContaParcela> listarParcelasMes(Date competencia, boolean apenasPagos) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select cp from ContaParcela cp ")
                .append(" inner join cp.idConta c ");
        if (apenasPagos) {
            jpql.append(" where month(cp.dataPagamento) = ?1 ")
                    .append(" and year(cp.dataPagamento) = ?2 ")
                    .append(" and cp.dataPagamento is not null")
                    .append(" order by c.tipoTransacao, cp.dataPagamento ");
        } else {
            jpql.append(" where month(cp.dataVencimento) = ?1 ")
                    .append(" and year(cp.dataVencimento) = ?2 ")
                    .append(" order by cp.dataVencimento desc ");
        }
        return getPureList(jpql.toString(), DataUtil.getMes(competencia), DataUtil.getAno(competencia));
    }

    public List<ContaParcela> listarParcelasMes(Integer mes, Integer ano, String fechada) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select par from ContaParcela par  ");
        jpql.append(" where month(par.dataVencimento) = ?1 ");
        jpql.append(" and year(par.dataVencimento) = ?2 ");
        jpql.append(" and fechada = ?3 ");
        return getPureList(jpql.toString(), mes, ano, fechada);
    }

    public List<ContaParcela> listarParcelasEmAberto(Conta conta) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select par from ContaParcela par  ");
        jpql.append(" where par.situacao = ?1 ");
        jpql.append(" and par.idConta = ?2 ");
        return getPureList(jpql.toString(), EnumSituacaoConta.NAO_PAGA.getSituacao(), conta);
    }

    public boolean existeParcelaPaga(Conta conta) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(c.id) from ContaParcela c ");
        sql.append(" where c.situacao in (?1 ,?2) ");
        sql.append(" and c.idConta = ?3 ");

        return getPurePojo(Long.class, sql.toString(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao(),
                EnumSituacaoConta.PAGA.getSituacao(),
                conta
        ) > 0;
    }

    public Conta buscarConta(Compra compra) {
        String jpql = "select c.idConta from Compra c where c = ?1 ";
        return getPurePojo(jpql, compra);
    }

    public Conta buscarContaByClienteMovimentacao(ClienteMovimentacao cm) {
        String jpql = "select c from Conta c where c.idClienteMovimentacao = ?1 ";
        return getPurePojo(jpql, cm);
    }

    public Conta buscarConta(Venda venda) {
        String jpql = "select v.idConta from Venda v where v = ?1 ";
        return getPurePojo(jpql, venda);
    }

    public Double obterValorLancamento(PlanoConta planoConta, Date data) {
        String jpql = "select sum(cp.valorPago) from Conta cp where cp.idPlanoConta = ?1 and month(cp.dataVencimento) = ?2 and year(cp.dataVencimento) = ?3 and cp.dataPagamento is not null";
        return getPurePojo(Double.class, jpql, planoConta, DataUtil.getMes(data), DataUtil.getAno(data));
    }

    public EstatisticaContaDTO obterValorReceber(Date dataInicio, Date dataFim) {
        String jpql = "select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(sum(cp.valorTotal), count(cp.valorTotal)) from ContaParcela cp where cp.dataVencimento between ?1 and ?2 and cp.dataPagamento is null and cp.idConta.tipoTransacao = ?3 and cp.situacao <> ?4 and cp.situacao <> ?5";
        return getPurePojo(jpql, dataInicio, dataFim, EnumTipoTransacao.RECEBER.getTipo(), EnumSituacaoConta.CANCELADO.getSituacao(), EnumSituacaoConta.NAO_PAGA.getSituacao());
    }

    public EstatisticaContaDTO obterValorPagar(Date dataInicio, Date dataFim) {
        String jpql = "select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(sum(cp.valorTotal), count(cp.valorTotal)) from ContaParcela cp where cp.dataVencimento between ?1 and ?2 and cp.dataPagamento is null and cp.idConta.tipoTransacao = ?3 and cp.situacao <> ?4 and cp.idConta.tipoConta <> ?5 and cp.idConta.tipoConta <> ?6 ";
        return getPurePojo(jpql,
                dataInicio,
                dataFim,
                EnumTipoTransacao.PAGAR.getTipo(),
                EnumSituacaoConta.CANCELADO.getSituacao(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public EstatisticaContaDTO obterValorReceberAtraso(Date dataInicio, Date dataFim) {
        String jpql = "select new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(sum(cp.valorTotal), count(cp.valorTotal)) from ContaParcela cp where cp.dataVencimento > ?3 and cp.dataVencimento < ?4 and cp.dataPagamento is null and cp.idConta.tipoTransacao = ?1 and cp.situacao <> ?2 and cp.situacao <> ?5  and cp.idConta.tipoConta <> ?6 and cp.idConta.tipoConta <> ?7";
        return getPurePojo(jpql,
                EnumTipoTransacao.RECEBER.getTipo(),
                EnumSituacaoConta.CANCELADO.getSituacao(),
                dataInicio,
                dataFim,
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public EstatisticaContaDTO obterValorPagarAtraso(Date dataInicio, Date dataFim) {
        String jpql = "select "
                + "new br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO(sum(cp.valorTotal), count(cp.valorTotal)) "
                + "from ContaParcela cp "
                + "where"
                + " cp.dataVencimento between ?3 and ?4 and "
                + " cp.dataPagamento is null and "
                + " cp.idConta.tipoTransacao = ?1 "
                + " and cp.situacao <> ?2 and "
                + " cp.situacao <> ?5 and "
                + " cp.idConta.tipoConta <> ?6 and "
                + " cp.idConta.tipoConta <> ?7";
        return getPurePojo(jpql,
                EnumTipoTransacao.PAGAR.getTipo(),
                EnumSituacaoConta.CANCELADO.getSituacao(),
                dataInicio,
                dataFim,
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public List<ValorLancamentoDTO> obterTodosValorLancamentoRecebimento(Date data) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(");
        jpql.append("cp.idPlanoConta.id, sum(cp.valorPago) ");
        jpql.append(")  ");
        jpql.append("from ContaParcela cp   ");
        jpql.append("where cp.tenatID = ?3 and month(cp.dataVencimento) = ?1 and year(cp.dataVencimento) = ?2 and cp.dataVencimento is not null  and cp.idConta.tipoTransacao = ?4 and cp.idConta.tipoConta <> ?5 and cp.idConta.tipoConta <> ?6 ");
        jpql.append("group by cp.idPlanoConta.id  ");

        return getPureList(jpql.toString(),
                DataUtil.getMes(data),
                DataUtil.getAno(data),
                adHocTenant.getTenantID(),
                EnumTipoTransacao.RECEBER.getTipo(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public List<ValorLancamentoDTO> obterTodosValorLancamentoPagamento(Date data) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(");
        jpql.append("cp.idPlanoConta.id, sum(cp.valorPago) ");
        jpql.append(")  ");
        jpql.append("from ContaParcela cp   ");
        jpql.append("where cp.tenatID = ?3 and month(cp.dataVencimento) = ?1 and year(cp.dataVencimento) = ?2 and cp.dataVencimento is not null   and cp.idConta.tipoTransacao = ?4 and cp.idConta.tipoConta <> ?5 and cp.idConta.tipoConta <> ?6  ");
        jpql.append("group by cp.idPlanoConta.id  ");

        return getPureList(jpql.toString(),
                DataUtil.getMes(data),
                DataUtil.getAno(data),
                adHocTenant.getTenantID(),
                EnumTipoTransacao.PAGAR.getTipo(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public List<ValorLancamentoDTO> obterValorReceitaFluxoCaixa(Date dataInicio, Date dataFim, CentroCusto centro, List<String> planosTransferencia) {
        String[] listaPlanos = new String[planosTransferencia.size() + 1];
        listaPlanos[0] = EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo() + ".%";
        for (int i = 0; i < planosTransferencia.size(); i++) {
            listaPlanos[1 + i] = planosTransferencia.get(i) + "%";
        }
        return obterValorFluxoCaixa(dataInicio, dataFim, EnumTipoTransacao.RECEBER, listaPlanos, centro);
    }

    public List<ValorLancamentoDTO> obterValorDespesaFluxoCaixa(Date dataInicio, Date dataFim, CentroCusto centro, List<String> planosTransferencia) {
        String[] listaPlanos = new String[planosTransferencia.size() + 3];
        listaPlanos[0] = EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo() + ".%";
        listaPlanos[1] = "1.2.3%";
        listaPlanos[2] = "1.1.3%";
        for (int i = 0; i < planosTransferencia.size(); i++) {
            listaPlanos[3 + i] = planosTransferencia.get(i) + "%";
        }

        return obterValorFluxoCaixa(dataInicio, dataFim, EnumTipoTransacao.PAGAR, listaPlanos, centro);
    }

    private List<ValorLancamentoDTO> obterValorFluxoCaixa(Date dataInicio, Date dataFim, EnumTipoTransacao tipo, String[] planoConta, CentroCusto centro) {
        int qteParam = 7 + planoConta.length + (centro != null ? 1 : 0);
        Object[] paramList = new Object[qteParam];
        StringBuilder sql = new StringBuilder();
        String caseDataPagamento = "(case when cp.dataPagamento is not null then cp.dataPagamento else cp.dataVencimento end)";
        String yearDataPagamento = "year" + caseDataPagamento;
        String monthDataPagamento = "month" + caseDataPagamento;

        paramList[0] = dataInicio;
        paramList[1] = dataFim;
        paramList[2] = adHocTenant.getTenantID();
        paramList[3] = tipo.getTipo();
        paramList[4] = EnumTipoConta.LANCAMENTO_CONTABIL.getTipo();
        paramList[5] = EnumSituacaoConta.PAGA.getSituacao();
        paramList[6] = EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao();
        System.arraycopy(planoConta, 0, paramList, 7, planoConta.length);

        sql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(")
                .append(yearDataPagamento)
                .append(", ")
                .append(monthDataPagamento)
                .append(", sum(case when cp.dataPagamento is not null and cp.idConta.tipoTransacao = ?4 then cp.valorPago when cp.dataPagamento is not null and cp.idConta.tipoTransacao != ?4 then -cp.valorPago else 0 end)")
                .append(")")
                .append(" from ContaParcela cp ")
                .append(" where cp.tenatID = ?3 and ");// É da empresa selecionada,
        if (tipo == EnumTipoTransacao.PAGAR) {
            sql.append(" cp.idConta.tipoTransacao = ?4 and ");// é uma conta a receber/pagar,
        } else {
            sql.append(" (cp.idConta.tipoTransacao = ?4 or (cp.idConta.tipoTransacao = 'P' and cp.idConta.idPlanoConta.codigo like '4.1.2.%')) and ");// é uma conta a receber/pagar,
        }
        sql.append(" ( ")// já foi pago ou pago parcialmente e a data está dento do intervalo,
                .append(" (cp.dataPagamento is not null and (cp.idConta.situacao = ?6 or cp.idConta.situacao = ?7) and (cp.dataPagamento >= ?1 and cp.dataPagamento <= ?2)) ")
                .append(" or ")// não foi pago e a data de vencimento está dentro do prazo,
                .append(" (cp.dataPagamento is null and (cp.dataVencimento >= ?1 and cp.dataVencimento <= ?2)) ")
                .append(" ) and ")// a conta não é de transferência ou um lançamento contábil
                .append(" (cp.idConta.tipoConta <> ?5)");
        if (planoConta.length > 0) {
            sql.append(" and (");
            String or = "";
            for (int i = 0; i < planoConta.length; i++) {
                sql.append(or)// e os códigos estão dentro da lista de códigos a buscar
                        .append("cp.idConta.idPlanoConta.codigo like ?")
                        .append(8 + i);
                or = " or ";
            }
            sql.append(") ");
        }
        if (centro != null) {
            paramList[7 + planoConta.length] = centro;
            sql.append(" and cp.idConta.idCentroCusto = ?")
                    .append(8 + planoConta.length);
        }
        sql.append(" group by ")
                .append(yearDataPagamento)
                .append(", ")
                .append(monthDataPagamento);

        return getPureList(sql.toString(), paramList);
    }

    public Double obterValorReceitaFluxoCaixa(Date data, CentroCusto centro, List<String> planosTransferencia) {
        StringBuilder jpql = new StringBuilder("select ");
        jpql.append("sum(cp.valorPago) ");
        jpql.append("from ContaParcela cp   ");
        jpql.append("where cp.tenatID = ?1 and cp.dataPagamento <= ?2 ");
        if (!planosTransferencia.isEmpty()) {
            jpql.append(" and (");
            String or = "";
            for (String plano : planosTransferencia) {
                jpql.append(or).append("cp.idConta.idPlanoConta.codigo like '").append(plano).append("%'");
                or = " or ";
            }
            jpql.append("or cp.idConta.idPlanoConta.codigo like ?3 ");
            jpql.append(" ) ");
            jpql.append(" and cp.idConta.tipoTransacao = ?4 ");

        } else {
            jpql.append("and cp.idConta.idPlanoConta.codigo like ?3 and cp.idConta.tipoTransacao = ?4 ");
        }
        jpql.append("and cp.idConta.tipoConta <> ?5 ");
        jpql.append("and (cp.idConta.situacao = ?6 or cp.idConta.situacao = ?7) ");
        if (centro != null) {
            jpql.append(" and cp.idConta.idCentroCusto = ?8");
            return getPurePojo(jpql.toString(),
                    adHocTenant.getTenantID(),
                    data,
                    EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo() + ".%",
                    EnumTipoTransacao.RECEBER.getTipo(),
                    EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                    EnumSituacaoConta.PAGA.getSituacao(),
                    EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao(),
                    centro);
        }
        return getPurePojo(jpql.toString(),
                adHocTenant.getTenantID(),
                data,
                EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo() + ".%",
                EnumTipoTransacao.RECEBER.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
    }

    public Double obterValorDespesaFluxoCaixa(Date data, CentroCusto centro, List<String> planosTransferencia) {
        StringBuilder jpql = new StringBuilder("select ");
        jpql.append("sum(cp.valorPago) ")
                .append("from ContaParcela cp   ")
                .append("where cp.tenatID = ?1 and cp.dataPagamento <= ?2 ")
                .append("and cp.idConta.tipoTransacao = ?3 ")
                .append(" and (cp.idConta.idPlanoConta.codigo like ?4 or cp.idConta.idPlanoConta.codigo like ?5 or cp.idConta.idPlanoConta.codigo like ?6 or cp.idConta.idPlanoConta.codigo like ?7 ");
        if (planosTransferencia != null && !planosTransferencia.isEmpty()) {
            planosTransferencia.forEach(plano -> jpql.append(" or cp.idConta.idPlanoConta.codigo like '").append(plano).append("%'"));
        }
        jpql.append(") and cp.idConta.tipoConta <> ?8 and (cp.idConta.situacao = ?9 or cp.idConta.situacao = ?10) ");
        if (centro != null) {
            jpql.append(" and cp.idConta.idCentroCusto = ?11");

            return getPurePojo(jpql.toString(),
                    adHocTenant.getTenantID(),
                    data,
                    EnumTipoTransacao.PAGAR.getTipo(),
                    EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo() + ".%",
                    "1.2.3%",
                    "1.1.3%",
                    "4.1.2%",
                    EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                    EnumSituacaoConta.PAGA.getSituacao(),
                    EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao(),
                    centro);
        }
        return getPurePojo(jpql.toString(),
                adHocTenant.getTenantID(),
                data,
                EnumTipoTransacao.PAGAR.getTipo(),
                EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo() + ".%",
                "1.2.3%",
                "1.1.3%",
                "4.1.2%",
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
    }

    public List<ValorLancamentoDTO> obterValorMensal(Date dataInicio, Date dataFim, EnumTipoTransacao tipo) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(")
                .append("year(cp.dataPagamento), month(cp.dataPagamento), sum(cp.valorPago)")
                .append(") ")
                .append("from ContaParcela cp ")
                .append("where cp.dataPagamento >= ?1 and cp.dataPagamento <= ?2 and cp.tenatID = ?3 and cp.idConta.tipoTransacao = ?4 ")
                .append("and cp.idConta.tipoConta not in (?5, ?6) ")
                .append("and cp.idConta.situacao in (?7, ?8) ")
                .append("group by year(cp.dataPagamento), month(cp.dataPagamento)");

        return getPureList(jpql.toString(), dataInicio, dataFim, adHocTenant.getTenantID(), tipo.getTipo(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
    }

    public List<ValorLancamentoDTO> obterValorDreMensal(Date dataInicio, Date dataFim, String codigoConta, EnumTipoTransacao tipo) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(")
                .append("year(cp.dataPagamento), month(cp.dataPagamento), sum(cp.valor)")
                .append(") ")
                .append("from ContaParcela cp ")
                .append("where cp.tenatID = ?3 and cp.dataPagamento >= ?1 and cp.dataPagamento <= ?2 ")
                .append("and cp.idConta.idPlanoConta.codigo like ?4 and cp.idConta.tipoTransacao = ?5 ")
                .append("and cp.idConta.tipoConta <> ?6 and cp.idConta.tipoConta <> ?7 ")
                .append("and (cp.idConta.situacao = ?8 or cp.idConta.situacao = ?9) ")
                .append("group by year(cp.dataPagamento), month(cp.dataPagamento)");

        return getPureList(jpql.toString(),
                dataInicio,
                dataFim,
                adHocTenant.getTenantID(),
                codigoConta + "%",
                tipo.getTipo(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
    }

    public List<ValorLancamentoDTO> obterValorDreMensal(Date dataInicio, Date dataFim, EnumTipoTransacao tipo, CentroCusto centro, boolean ehFluxo) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO(");
        jpql.append("year(cp.dataPagamento), month(cp.dataPagamento), sum(cp.valorPago), cp.idConta.idPlanoConta.codigo");
        jpql.append(") ");
        jpql.append("from ContaParcela cp ");
        jpql.append("where cp.dataPagamento >= ?1 and cp.dataPagamento <= ?2 and cp.tenatID = ?3 ");
        jpql.append("and cp.idConta.tipoTransacao = ?4 ");
        if (centro != null) {
            if (ehFluxo) {
                jpql.append("and cp.idConta.tipoConta not in (?5) ");
            } else {
                jpql.append("and cp.idConta.tipoConta not in (?5, ?6) ");
            }
            jpql.append("and cp.idConta.situacao in (?6, ?7) ");

            jpql.append(" and cp.idConta.idCentroCusto = ?8 group by year(cp.dataPagamento), month(cp.dataPagamento), cp.idConta.idPlanoConta.codigo");
            return getPureList(jpql.toString(),
                    dataInicio,
                    dataFim,
                    adHocTenant.getTenantID(),
                    tipo.getTipo(),
                    EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                    EnumSituacaoConta.PAGA.getSituacao(),
                    EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao(),
                    centro);

        } else {
            jpql.append("and cp.idConta.tipoConta not in (?5, ?6) ");
            jpql.append("and cp.idConta.situacao in (?7, ?8) ");
            if (centro != null) {
                jpql.append(" and cp.idConta.idCentroCusto = ?9 group by year(cp.dataPagamento), month(cp.dataPagamento), cp.idConta.idPlanoConta.codigo");
                return getPureList(jpql.toString(),
                        dataInicio,
                        dataFim,
                        adHocTenant.getTenantID(),
                        tipo.getTipo(),
                        EnumTipoConta.TRANSFERENCIA.getTipo(),
                        EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                        EnumSituacaoConta.PAGA.getSituacao(),
                        EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao(),
                        centro);
            }
        }
        jpql.append("group by year(cp.dataPagamento), month(cp.dataPagamento), cp.idConta.idPlanoConta.codigo");
        return getPureList(jpql.toString(),
                dataInicio,
                dataFim,
                adHocTenant.getTenantID(),
                tipo.getTipo(),
                EnumTipoConta.TRANSFERENCIA.getTipo(),
                EnumTipoConta.LANCAMENTO_CONTABIL.getTipo(),
                EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
    }

    public List<AnaliseContaSinteticaDTO> obterAnaliseContaSinteticoPorPlanoContas(AnaliseContaFiltro filtro) {
        StringBuilder jpql = new StringBuilder();
        jpql.append("  select new br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaSinteticaDTO( ")
                .append(" planoConta.descricao, ")
                .append(" sum(case when parcela.dataVencimento >= current_date() and parcela.dataPagamento is null then parcela.valorTotal else 0 end), ")
                .append(" sum(case when parcela.dataVencimento < current_date() and parcela.dataPagamento is null then parcela.valorTotal else 0 end), ")
                .append(" sum(case when parcela.dataPagamento is not null then parcela.valorTotal else 0 end), ")
                .append(" sum(case when parcela.dataVencimento >= current_date() and parcela.dataPagamento is null then 1 else 0 end), ")
                .append(" sum(case when parcela.dataVencimento < current_date() and parcela.dataPagamento is null then 1 else 0 end), ")
                .append(" sum(case when parcela.dataPagamento is not null then 1 else 0 end), ")
                .append(" 'Resumo por plano de contas', ")
                .append(" 'Plano de contas' ")
                .append(" ) ")
                .append(" from ContaParcela parcela ")
                .append(" inner join parcela.idConta conta ")
                .append(" inner join conta.idPlanoConta planoConta ")
                .append(" left join parcela.idCentroCusto centroCusto ")
                .append(" left join conta.idFornecedor fornecedor ")
                .append(" left join conta.idCliente cliente ")
                .append(" where parcela.tenatID = ?1 ")
                .append(" and conta.tipoTransacao = ?2 ")
                .append(" and coalesce(parcela.dataPagamento, parcela.dataVencimento) between ?3 and ?4 ");

        if (filtro.getPlanoConta() != null) {
            jpql.append(" and conta.idPlanoConta.codigo like '").append(filtro.getPlanoConta().getCodigo()).append("%'");
        }
        if (filtro.getFornecedor() != null) {
            jpql.append(" and fornecedor.id = ").append(filtro.getFornecedor().getId());
        }
        if (filtro.getCliente() != null) {
            jpql.append(" and cliente.id = ").append(filtro.getCliente().getId());
        }
        if (filtro.getContaBancaria() != null) {
            jpql.append(" and parcela.idContaBancaria.id = ").append(filtro.getContaBancaria().getId());
        }
        if (filtro.getCentroCusto() != null) {
            jpql.append(" and centroCusto.id = ").append(filtro.getCentroCusto().getId());
        }

        jpql.append(" and parcela.situacao <> '").append(EnumSituacaoConta.CANCELADO.getSituacao()).append("'")
                .append(" group by planoConta.descricao order by conta.idPlanoConta.descricao ");

        return getPureList(jpql.toString(), adHocTenant.getTenantID(), filtro.getTipoConta(), filtro.getData().getMin(), filtro.getData().getMax());
    }

    public List<AnaliseContaSinteticaDTO> obterAnaliseContaSinteticoPorContaBancaria(AnaliseContaFiltro filtro) {

        StringBuilder jpql = new StringBuilder();
        jpql.append(
                "  select new br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaSinteticaDTO( ");
        jpql.append(
                " contaBancaria.descricao, "
                + " sum(case when parcela.dataVencimento >= current_date() and parcela.dataPagamento is null then parcela.valorTotal else 0 end), "
                + " sum(case when parcela.dataVencimento < current_date() and parcela.dataPagamento is null then parcela.valorTotal else 0 end), "
                + " sum(case when parcela.dataPagamento is not null then parcela.valorTotal else 0 end), "
                + " sum(case when parcela.dataVencimento >= current_date() and parcela.dataPagamento is null then 1 else 0 end), "
                + " sum(case when parcela.dataVencimento < current_date() and parcela.dataPagamento is null then 1 else 0 end), "
                + " sum(case when parcela.dataPagamento is not null then 1 else 0 end), "
                + " 'Resumo por conta bancária', "
                + " 'Conta bancária' ");
        jpql.append(" ) ");
        jpql.append(" from ContaParcela parcela ");
        jpql.append(" inner join parcela.idConta conta ");
        jpql.append(" inner join conta.idPlanoConta planoConta ");
        jpql.append(" inner join parcela.idContaBancaria contaBancaria ");
        jpql.append(" left join parcela.idCentroCusto centroCusto ");
        jpql.append(" left join conta.idFornecedor fornecedor ");
        jpql.append(" left join conta.idCliente cliente ");
        jpql.append(" where ");
        jpql.append(" parcela.tenatID = ?1 ");
        jpql.append(" and conta.tipoTransacao = ?2 ");
        jpql.append(" and coalesce(parcela.dataPagamento, parcela.dataVencimento) between ?3 and ?4 ");

        if (filtro.getPlanoConta() != null) {
            jpql.append(" and conta.idPlanoConta.codigo like '").append(filtro.getPlanoConta().getCodigo()).append("%'");
        }
        if (filtro.getFornecedor() != null) {
            jpql.append(" and fornecedor.id = ").append(filtro.getFornecedor().getId());
        }
        if (filtro.getCliente() != null) {
            jpql.append(" and cliente.id = ").append(filtro.getCliente().getId());
        }
        if (filtro.getContaBancaria() != null) {
            jpql.append(" and parcela.idContaBancaria.id = ").append(filtro.getContaBancaria().getId());
        }
        if (filtro.getCentroCusto() != null) {
            jpql.append(" and centroCusto.id = ").append(filtro.getCentroCusto().getId());
        }

        jpql.append(" and parcela.situacao <> '").append(EnumSituacaoConta.CANCELADO.getSituacao()).append("'");

        jpql.append(" group by contaBancaria.descricao order by parcela.idContaBancaria.descricao ");

        return getPureList(jpql.toString(), adHocTenant.getTenantID(), filtro.getTipoConta(), filtro.getData().getMin(), filtro.getData().getMax());
    }

    public List<AnaliseContaDTO> obterAnaliseConta(AnaliseContaFiltro filtro) {
        QueryBuilder qb = new QueryBuilder();
        qb.select(AnaliseContaDTO.class, "pc.descricao, cp.situacao, cp.dataEmissao, cp.dataVencimento, cp.dataPagamento, cp.valor, cp.valorPago, f.razaoSocial, cli.nome, cp.numNf, cp.observacao, c.observacao, cp.juros, cp.tarifa")
                .from(ContaParcela.class)
                .innerJoin("cp.idConta")
                .innerJoin("c.idPlanoConta")
                .leftJoin("cp.idCentroCusto")
                .leftJoin("c.idFornecedor")
                .leftJoin("c.idCliente", "cli")
                .where("cp.tenatID = ?1 and c.tipoTransacao = ?2 and coalesce(cp.dataPagamento, cp.dataVencimento) between ?3 and ?4 and c.tipoConta not in (?5, ?6) ")
                .orderBy("c.idPlanoConta.descricao, cp.dataPagamento, cp.dataVencimento, cp.valorTotal");

        if (filtro.getPlanoConta() != null) {
            qb.addWhere("and pc.codigo like '" + filtro.getPlanoConta().getCodigo() + "%'");
        }
        if (filtro.getFornecedor() != null) {
            qb.addWhere("and f.id = " + filtro.getFornecedor().getId());
        }
        if (filtro.getCliente() != null) {
            qb.addWhere("and cli.id = " + filtro.getCliente().getId());
        }
        if (filtro.getContaBancaria() != null) {
            qb.addWhere("and cp.idContaBancaria.id = " + filtro.getContaBancaria().getId());
        }
        if (filtro.getCentroCusto() != null) {
            qb.addWhere("and c.idCentroCusto = " + filtro.getCentroCusto().getId());
        }

        if (filtro.getSituacaoConta() != null && !"T".equals(filtro.getSituacaoConta())) {
            switch (EnumSituacaoConta.retornaEnumSelecionado(filtro.getSituacaoConta())) {
                case PAGA:
                    qb.addWhere("and cp.situacao = '" + EnumSituacaoConta.PAGA.getSituacao() + "'");
                    break;
                case PAGA_PARCIALMENTE:
                    qb.addWhere("and cp.situacao in ('" + EnumSituacaoConta.PAGA.getSituacao() + "'" + ",'" + EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao() + "')");
                    break;
                case NAO_PAGA:
                    qb.addWhere("and cp.situacao = '" + EnumSituacaoConta.NAO_PAGA.getSituacao() + "'");
                    break;
                default:
                    qb.addWhere("and cp.situacao <> '" + EnumSituacaoConta.CANCELADO.getSituacao() + "'");
                    break;
            }
        } else {
            qb.addWhere("and cp.situacao <> '" + EnumSituacaoConta.CANCELADO.getSituacao() + "'");
        }

        return getPureList(qb, adHocTenant.getTenantID(), filtro.getTipoConta(), filtro.getData().getMin(), filtro.getData().getMax(), EnumTipoConta.TRANSFERENCIA.getTipo(), EnumTipoConta.LANCAMENTO_CONTABIL.getTipo());
    }

    public Conta obterContaImportada(Conta conta) {

        StringBuilder sql = new StringBuilder("select c from Conta c ");
        sql
                .append("where ")
                .append("c.idPlanoConta = ?1 and ")
                .append("c.idContaBancaria = ?2 and ")
                .append("c.dataVencimento = ?3 and ")
                .append("c.dataPagamento = ?3 and ")
                .append("c.informarPagamento = ?4 and ")
                .append("c.numeroParcelas = ?5 and ")
                .append("c.repetirConta = ?6 and ")
                .append("c.tipoTransacao = ?7 and ")
                .append("c.tipoConta = ?8 ")
                .append(" ");

        return getPurePojo(sql.toString(),
                conta.getIdPlanoConta(),
                conta.getIdContaBancaria(),
                conta.getDataVencimento(),
                conta.getInformarPagamento(),
                conta.getNumeroParcelas(),
                conta.getRepetirConta(),
                conta.getTipoTransacao(),
                conta.getTipoConta()
        );
    }

    public ContaParcela removeContaParcelaPorConta(Conta conta) {
        String jpql = "select c from ContaParcela c where c.idConta = ?1 and c.situacao = ?2 order by desc";
        return getPurePojoTop1(jpql, conta, EnumSituacaoConta.NAO_PAGA.getSituacao());
    }

    public List<Conta> contasPorFornecedor(Fornecedor fornecedor) {
        String jpql = "select c from Conta c where c.idFornecedor =?1 and c.tenatID = ?2";
        return getPureList(jpql, fornecedor, adHocTenant.getTenantID());
    }

    public List<Conta> contasPorCliente(Cliente cliente) {
        String jpql = "select c from Conta c where c.idCliente =?1 and c.tenatID = ?2";
        return getPureList(jpql, cliente, adHocTenant.getTenantID());
    }

    public List<ContaParcela> parcelasPorCliente(Cliente cliente) {
        String jpql = "select c from ContaParcela c where c.idConta.idCliente =?1 and c.tenatID = ?2";
        return getPureList(jpql, cliente, adHocTenant.getTenantID());
    }

    public Conta contaPorNFS(NFS nfs) {
        String jpql = "select cp.idConta from ContaParcela cp where cp.idNFS = ?1 and cp.tenatID = ?2";
        return getPurePojo(jpql, nfs, adHocTenant.getTenantID());
    }

    public List<ContaParcela> parcelasPorNFS(NFS nfs) {
        return getPureList("select c from ContaParcela c where c.idNFS = ?1", nfs);
    }

    public List<Conta> contasPorNFS(NFS nfs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select cp.idConta ");
        sql.append(" from ContaParcela cp ");
        sql.append(" join cp.idConta c ");
        sql.append(" where co.idNFS = ?1 ");
        sql.append(" and c.tenatID = ?2 ");
        return getPureList(sql.toString(), nfs, adHocTenant.getTenantID());
    }

    public boolean parcelaPossuiNFServico(ContaParcela parcela) {
        String jpq1 = "select cp.idNFS.id from ContaParcela cp where cp = ?1";
        return getPurePojoTop1(Integer.class, jpq1, parcela) != null;
    }

    public List<Conta> contaPorContrato(Contrato contrato) {
        String jpql = "select c from Conta c where c.idContrato =?1 and c.tenatID = ?2";
        return getPureList(jpql, contrato, adHocTenant.getTenantID());
    }

    public List<PlanoContaLancamentoDTO> listarValorPrevisao(Date dataInicio, Date dataFim, CentroCusto centro) {
        QueryBuilder qb = new QueryBuilder();

        qb.select(PlanoContaLancamentoDTO.class, "cp.idConta.idPlanoConta.id, sum(cp.valorTotal)")
                .from(ContaParcela.class)
                .where("cp.dataVencimento between ?1 and ?2 and cp.tenatID = ?3")
                .addGroupBy("cp.idConta.idPlanoConta.id");

        if (centro != null) {
            qb.addWhere("and cp.idCentroCusto = ?4");
            return getPureList(qb, dataInicio, dataFim, adHocTenant.getTenantID(), centro);
        }

        return getPureList(qb, dataInicio, dataFim, adHocTenant.getTenantID());
    }

    public List<ContaParcela> buscarParcela(TransacaoIntegracaoBancaria t, ContaBancaria contaBancaria) {
        String jpql = " select cpp from ContaParcela cpp where cpp.idContaBancaria = ?1 and cpp.valorTotal = ?2 ";
        jpql += " and cpp.idConta.tipoTransacao = ?3 ";
        jpql += " and cpp.tenatID = ?4 ";
        jpql += " and cpp.id not in (select a.idContaParcela.id from TransacaoIntegracaoBancaria a where a.idContaParcela is not null and a.tenatID = ?4) ";
        return getPureList(jpql, contaBancaria, Math.abs(t.getValor()), t.getValor() < 0 ? "P" : "R", adHocTenant.getTenantID());
    }

    public List<PlanoContaLancamentoDTO> listarValorRealizado(Date dataInicio, Date dataFim, CentroCusto centro) {
        QueryBuilder qb = new QueryBuilder();

        qb.select(PlanoContaLancamentoDTO.class, "cp.idConta.idPlanoConta.id, sum(cp.valorPago)")
                .from(ContaParcela.class)
                .where("cp.dataVencimento between ?1 and ?2 and cp.tenatID = ?3 and cp.dataPagamento is not null and cp.valorPago is not null")
                .addGroupBy("cp.idConta.idPlanoConta.id");

        if (centro != null) {
            qb.addWhere("and cp.idCentroCusto = ?4");
            return getPureList(qb, dataInicio, dataFim, adHocTenant.getTenantID(), centro);
        }

        return getPureList(qb, dataInicio, dataFim, adHocTenant.getTenantID());
    }

    public List<LancamentoCaixaDTO> listaLancamentoNoCaixa(Empresa empresa, PlanoConta planoConta, Date dataInicio, Date dataFinal, ContaBancaria contaBancaria, CentroCusto centroCusto) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.LancamentoCaixaDTO( ");
        jpql.append("lc.idConta.idPlanoConta.descricao, lc.dataPagamento, lc.valorPago ");
        jpql.append(") ");
        jpql.append("from ContaParcela lc ");
        jpql.append(" left join lc.idCentroCusto centroCusto ");
        jpql.append("where ");
        jpql.append("lc.tenatID =?1 and lc.dataPagamento between ?2 and ?3 ");
        jpql.append("and lc.valorPago is not null ");
        jpql.append("and lc.dataPagamento is not null ");

        if (contaBancaria != null) {
            jpql.append(" and lc.idContaBancaria.id = ");
            jpql.append(contaBancaria.getId());
        }

        if (centroCusto != null) {
            jpql.append(" and centroCusto.id = ");
            jpql.append(centroCusto.getId());
        }

        if (planoConta != null) {
            jpql.append(" and lc.idConta.idPlanoConta.codigo like ?4 ");
            jpql.append(" order by lc.idConta.idPlanoConta.descricao, lc.dataPagamento ");

            return getPureList(jpql.toString(), empresa.getTenatID(), dataInicio, dataFinal, planoConta.getCodigo() + "%");
        }
        jpql.append("order by lc.idConta.idPlanoConta.descricao, lc.dataPagamento ");

        return getPureList(jpql.toString(), empresa.getTenatID(), dataInicio, dataFinal);

    }

    public String getPrimeiraDataDeFechamento() {
        try {
            Date mesAnterior = DataUtil.subtrairMeses(DataUtil.getHoje(), 1);
            Date ultimoDiaMesAnterior = DataUtil.getUltimoDiaDoMes(mesAnterior);
            StringBuilder jpql = new StringBuilder();
            jpql.append(" select ")
                    .append(" new br.com.villefortconsulting.sgfinancas.entidades.dto.FechamentoContabilDTO( ")
                    .append(" month(dataVencimento), ")
                    .append(" year(dataVencimento), ")
                    .append(" count(id), ")
                    .append(" fechada ")
                    .append(" ) ")
                    .append(" from ContaParcela ")
                    .append(" where tenatID = ?1 ")
                    .append(" and dataVencimento <= ?2 ")
                    .append(" group by month(dataVencimento), year(dataVencimento), fechada ")
                    .append(" order by year(dataVencimento) asc ")
                    .append(" , month(dataVencimento) asc ");

            FechamentoContabilDTO dto = getPurePojoTop1(jpql.toString(), adHocTenant.getTenantID(), ultimoDiaMesAnterior);
            return StringUtil.adicionarCaracterEsquerda(dto.getMesCompetencia().toString(), "0", 2) + "/" + dto.getAnoCompetencia();
        } catch (Exception ex) {
            return "";
        }
    }

    public List<FechamentoContabilDTO> listarFechamentoContabil(Date dataInicio, Date dataFim) {
        Date mesAnterior = DataUtil.subtrairMeses(DataUtil.getHoje(), 1);
        Date ultimoDiaMesAnterior = DataUtil.getUltimoDiaDoMes(mesAnterior);
        Date dataFinal = ultimoDiaMesAnterior;
        if (dataFim != null) {
            dataFinal = dataFim;
        }

        StringBuilder jpql = new StringBuilder();
        jpql.append(" select new br.com.villefortconsulting.sgfinancas.entidades.dto.FechamentoContabilDTO( ");
        jpql.append(" month(dataVencimento), ");
        jpql.append(" year(dataVencimento), ");
        jpql.append(" count(id), ");
        jpql.append(" fechada ");
        jpql.append(" ) ");
        jpql.append(" from ContaParcela ");
        jpql.append(" where tenatID = ?1 ");
        jpql.append(" and dataVencimento <= ?2 ");
        if (dataInicio != null) {
            jpql.append(" and dataVencimento >= ?3 ");
        }
        jpql.append(" group by month(dataVencimento), year(dataVencimento), fechada ");
        jpql.append(" order by year(dataVencimento) desc , month(dataVencimento) desc ");

        if (dataInicio != null) {
            return getPureList(jpql.toString(), adHocTenant.getTenantID(), dataFinal, dataInicio);
        }
        return getPureList(jpql.toString(), adHocTenant.getTenantID(), dataFinal);
    }

    public ContaParcela buscarParcelaPorNFS(NFS nfs) {
        String jpql = "select cp from ContaParcela cp where cp.idNFS = ?1 and cp.tenatID = ?2";
        return getPurePojoTop1(jpql, nfs, adHocTenant.getTenantID());
    }

    public Conta buscarContaPorIdDaParcela(Integer id) {
        String jpql = " select cp.idConta from ContaParcela cp where cp.id = ?1 ";
        return getPurePojo(jpql, id);
    }

    public List<Object> getDadosAuditoriaByID(Class<ContaParcela> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

    public List<TimelineDTO> getTimeline(Date dataInicio, Date dataFim, boolean pago) {
        StringBuilder jpql = new StringBuilder();
        String data = pago ? "dataPagamento" : "dataVencimento";
        jpql.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.TimelineDTO")
                .append("(cp.dataVencimento, cp.dataPagamento, c.tipoTransacao, cp.valor, cp.valorPago, cliente, fornecedor, cp.observacao, c.idPlanoConta) ")
                .append("from ContaParcela cp ")
                .append("inner join cp.idConta c ")
                .append("left join c.idCliente cliente ")
                .append("left join c.idFornecedor fornecedor ")
                .append("where cp.").append(data).append(" between ?1 and ?2 ")
                .append("and cp.situacao not in (?3, ?4) ");
        if (pago) {
            jpql.append("and cp.dataPagamento is not null order by c.tipoTransacao, cp.dataPagamento ");
        } else {
            jpql.append("and cp.dataPagamento is null and cp.valor > 0 order by cp.dataVencimento asc ");
        }
        return getPureList(jpql.toString(), dataInicio, dataFim, EnumSituacaoConta.CANCELADO.getSituacao(), EnumSituacaoConta.INTERROMPIDO.getSituacao());
    }

    public List<MediaPrazoRelatorioDTO> listaMediaPrazoRecebimentoPagamento(Date dataInicio, Date dataFim) {
        String sql = "select f.razao_social, cli.nome, sum(coalesce(datediff(day, cp.data_Emissao, cp.data_pagamento), 0)) as mediaDias, count(cp.id) as count "
                + " from Conta_Parcela cp "
                + " join CONTA c ON cp.id_conta = c.id "
                + " left join FORNECEDOR f ON f.id = c.ID_FORNECEDOR "
                + " left join Cliente cli  ON cli.ID = c.ID_CLIENTE "
                + " where cp.data_pagamento >= '" + DataUtil.dateToString(dataInicio, "yyyy-MM-dd")
                + "' and cp.data_pagamento <= '" + DataUtil.dateToString(dataFim, "yyyy-MM-dd")
                + "' and cp.data_emissao is not null "
                + " and cp.tenat_ID = " + adHocTenant.getTenantID()
                + " and cp.data_pagamento is not null "
                + " and (f.id is not null or cli.id is not null) "
                + " group by f.razao_social, cli.nome"
                + "--tenatID";
        List<Object[]> listaMedias = createNativeQuery(sql).getResultList();
        return listaMedias.stream()
                .map(lista -> {
                    MediaPrazoRelatorioDTO dto = new MediaPrazoRelatorioDTO();
                    if (lista[0] != null) {
                        dto.setNome((String) lista[0]);
                        dto.setTipoPessoa("F");
                    } else {
                        dto.setNome((String) lista[1]);
                        dto.setTipoPessoa("C");
                    }
                    dto.setMediaDias(NumeroUtil.dividir((Integer) lista[2] * 1.0d, (Integer) lista[3]));
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Conta> listarContaPagarParcialmentePagasByFornecedor(Fornecedor fornecedor) {
        StringBuilder sql = new StringBuilder();
        sql.append("select c from Conta c where c.tipoTransacao = ?1 and (c.situacao = 'PP' or c.situacao = 'NP') and c.idFornecedor is not null ");
        if (fornecedor != null) {
            sql.append(" and c.idFornecedor = ?2 order by c.idFornecedor ");
            return getPureList(sql.toString(), EnumTipoTransacao.PAGAR.getTipo(), fornecedor);
        }
        sql.append(" order by c.idFornecedor , c.dataVencimento");
        return getPureList(sql.toString(), EnumTipoTransacao.PAGAR.getTipo());
    }

    public List<ContaParcela> listarContaParcelaRelatorio(Conta conta) {
        String jpql = " select cpp from ContaParcela cpp where cpp.idConta = ?1 and cpp.dataCancelamento is null order by cpp.dataVencimento ";
        return getPureList(jpql, conta);
    }

    public List<ControlePagamentoItemDTO> listarParcelasPor(Fornecedor fornecedor, MinMax<Date> data) {
        QueryBuilder qb = new QueryBuilder();
        qb.select(ControlePagamentoItemDTO.class, "cp.id, cp.observacao, cp.dataEmissao, cp.dataVencimento, cp.valor")
                .from(ContaParcela.class)
                .where("cp.idConta.idFornecedor = ?1 and cp.dataCancelamento is null");
        if (data.getMin() != null && data.getMax() != null) {
            qb.addWhere("and cp.idConta.dataEmissao >= ?2 and cp.idConta.dataEmissao <= ?3");
            return getPureList(qb, fornecedor, data.getMin(), data.getMax());
        } else if (data.getMin() != null) {
            qb.addWhere("and cp.idConta.dataEmissao >= ?2");
            return getPureList(qb, fornecedor, data.getMin());
        } else if (data.getMax() != null) {
            qb.addWhere("and cp.idConta.dataEmissao <= ?3");
            return getPureList(qb, fornecedor, data.getMax());
        }
        return getPureList(qb, fornecedor);
    }

    public ResultadoPlanoContaLancamentoDTO obterResultado(PlanoConta aux, Date dataInicio, Date dataFim) {
        return getPurePojo(QueryBuilder.builder()
                .select(ResultadoPlanoContaLancamentoDTO.class, "sum(cp.valorTotal), sum(cp.valorPago), c.idPlanoConta")
                .from(ContaParcela.class)
                .join("cp.idConta")
                .where("cp.dataVencimento between ?1 and ?2 and c.idPlanoConta = ?3 and cp.tenatID = ?4")
                .addGroupBy("c.idPlanoConta")
                .build(), dataInicio, dataFim, aux, adHocTenant.getTenantID());
    }

    public List<Conta> listaTransferencias(Empresa empresa) {
        String jpql = " select c from Conta c where c.tipoConta = 'T' and c.tenatID = ?1";
        return getPureList(jpql, empresa.getTenatID());
    }

}
