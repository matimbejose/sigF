package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoItemDTO;
import br.com.villefortconsulting.util.operator.MinMax;
import br.com.villefortconsulting.util.sql.QueryBuilder;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class ExtratoContaCorrenteRepositorio extends BasicRepository<ExtratoContaCorrente> {

    public ExtratoContaCorrenteRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ExtratoContaCorrenteRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ExtratoContaCorrente> listar() {
        String jpql = "select b from ExtratoContaCorrente b order by b.dataMovimentacao";
        return getPureList(jpql);
    }

    public Double buscarValorSaldoInicial(ContaBancaria contaBancaria) {
        String jpql = "select b.valor from ExtratoContaCorrente b where b.idContaBancaria = ?1 order by b.id ";
        return getPurePojoTop1(jpql, contaBancaria);
    }

    public Double buscarSaldo(Date dataMovimentacao, ContaBancaria contaBancaria) {
        String jpql = "select b.saldo from ExtratoContaCorrente b where b.dataMovimentacao <= ?1 and b.idContaBancaria = ?2 order by b.dataMovimentacao desc, b.id desc";
        return getPurePojoTop1(jpql, dataMovimentacao, contaBancaria);
    }

    public ExtratoContaCorrente buscarUltimoLancamento(ContaBancaria contaBancaria) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?1 order by b.dataMovimentacao desc";
        return getPurePojoTop1(jpql, contaBancaria);
    }

    public ExtratoContaCorrente buscarLancamento(ContaBancaria conta) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?1 ";
        return getPurePojoTop1(jpql, conta);
    }

    public ExtratoContaCorrente buscarLancamento(ContaParcela conta) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaParcela = ?1 ";
        return getPurePojoTop1(jpql, conta);
    }

    public ExtratoContaCorrente buscarLancamento(ContaParcelaParcial conta) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaParcelaParcial = ?1 ";
        return getPurePojoTop1(jpql, conta);
    }

    public List<ExtratoContaCorrente> buscarLancamentosCancelamento(ExtratoContaCorrente extratoContaCorrente) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?2 and (b.dataMovimentacao > ?1 or (b.dataMovimentacao = ?1 and  b.id > ?3 )) ";
        return getPureList(jpql, extratoContaCorrente.getDataMovimentacao(), extratoContaCorrente.getIdContaBancaria(), extratoContaCorrente.getId());
    }

    public List<ExtratoContaCorrente> buscarLancamentosPosterioresData(Date dataMovimentacao, ContaBancaria contaBancaria) {
        String jpql = "select b "
                + "from ExtratoContaCorrente b "
                + "join b.idContaBancaria cb "
                + "join b.idContaParcela cp "
                + "join b.idContaParcelaParcial cpp "
                + "where b.dataMovimentacao > ?1 and b.idContaBancaria = ?2 "
                + "order by b.dataMovimentacao desc, b.id desc ";
        return getPureList(jpql, dataMovimentacao, contaBancaria);
    }

    public void atualizarLancamentosPosterioresData(Date dataMovimentacao, ContaBancaria contaBancaria, Double valor) {
        String jpql = "update ExtratoContaCorrente b "
                + "set b.saldo = b.saldo + ?3, b.saldoAnterior = b.saldoAnterior + ?3 "
                + "where b.dataMovimentacao > ?1 and b.idContaBancaria = ?2 and b.tenatID = ?4 ";
        executeCommand(jpql, dataMovimentacao, contaBancaria, valor, adHocTenant.getTenantID());
    }

    public ExtratoContaCorrente buscarExtratoSaldoInicial(ContaBancaria contaBancaria) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?1 and b.idContaParcela is null and b.idContaParcelaParcial is null ";
        return getPurePojo(jpql, contaBancaria);
    }

    public List<ExtratoContaCorrente> listaExtratoPorContaBancaria(ContaBancaria contaBancaria) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?1 order by b.dataMovimentacao desc, b.id desc ";
        return getPureList(jpql, contaBancaria);
    }

    public List<ExtratoContaCorrente> listarExtratoPorContaBancariaAsc(ContaBancaria contaBancaria) {
        String jpql = "select b from ExtratoContaCorrente b where b.idContaBancaria = ?1 order by b.dataMovimentacao, b.id ";
        return getPureList(jpql, contaBancaria);
    }

    public List<ExtratoContaCorrente> verificarRegistrosDuplicados() {
        String jpql = " select a from ExtratoContaCorrente a ";
        jpql += " where a.tenatID = ?1 and a.idContaParcela in   ";
        jpql += " (select b.idContaParcela.id from ExtratoContaCorrente b where b.tenatID = ?1 ";
        jpql += " group by b.idContaParcela.id having count(b.idContaParcela.id) > 1)   ";
        jpql += " and a.id not in   ";
        jpql += " (select min(c.id) from ExtratoContaCorrente c where c.tenatID = ?1 ";
        jpql += " group by c.idContaParcela.id having count(c.idContaParcela.id) > 1)   ";
        jpql += " order by a.idContaBancaria.id, a.idContaParcela ";
        return getPureList(jpql, adHocTenant.getTenantID());
    }

    public ExtratoContaCorrente buscarExtratoPorParcela(ContaParcela parcela) {
        String jpql = " select e from ExtratoContaCorrente e where e.idContaParcela = ?1 order by e.id ";
        return getPurePojoTop1(jpql, parcela);
    }

    public ExtratoContaCorrente buscarExtratoPorParcelaParcial(ContaParcelaParcial parcial) {
        String jpql = " select e from ExtratoContaCorrente e where e.idContaParcelaParcial = ?1 order by e.id ";
        return getPurePojoTop1(jpql, parcial);
    }

    public List<ControlePagamentoItemDTO> listarExtratoPorFornecedor(Fornecedor fornecedor, MinMax<Date> data) {
        QueryBuilder qb = new QueryBuilder();
        qb.select(ControlePagamentoItemDTO.class, "ecc.idContaParcela.id, ecc.idContaParcela.observacao, ecc.dataMovimentacao, ecc.valor")
                .from(ExtratoContaCorrente.class)
                .where("ecc.idContaParcela.idConta.idFornecedor = ?1 and ecc.idContaParcela.dataCancelamento is null");
        if (data.getMin() != null && data.getMax() != null) {
            qb.addWhere("and ecc.idContaParcela.idConta.dataEmissao >= ?2 and ecc.idContaParcela.idConta.dataEmissao <= ?3 ");
            return getPureList(qb, fornecedor, data.getMin(), data.getMax());
        } else if (data.getMin() != null) {
            qb.addWhere("and ecc.idContaParcela.idConta.dataEmissao >= ?2");
            return getPureList(qb, fornecedor, data.getMin());
        } else if (data.getMax() != null) {
            qb.addWhere("and ecc.idContaParcela.idConta.dataEmissao <= ?3");
            return getPureList(qb, fornecedor, data.getMax());
        }
        return getPureList(qb, fornecedor);
    }

}
