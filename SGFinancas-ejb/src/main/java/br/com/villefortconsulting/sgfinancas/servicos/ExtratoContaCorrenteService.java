package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ExtratoContaCorrenteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ExtratoContaCorrenteRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoExtrato;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ExtratoContaCorrenteService extends BasicService<ExtratoContaCorrente, ExtratoContaCorrenteRepositorio, ExtratoContaCorrenteFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService contaService;

    @EJB
    private FornecedorService fornecedorService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ExtratoContaCorrenteRepositorio(em, adHocTenant);
    }

    @Override
    public Criteria getModel(ExtratoContaCorrenteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idContaParcela", "cp1", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("cp1.idConta", "c1", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("c1.idPlanoConta", "p1", JoinType.LEFT_OUTER_JOIN);

        criteria.createAlias("idContaParcelaParcial", "cpp", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("cpp.idContaParcela", "cp2", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("cp2.idConta", "c2", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("c2.idPlanoConta", "p2", JoinType.LEFT_OUTER_JOIN);

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion c1 = Restrictions.ilike("p1.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion c2 = Restrictions.ilike("p2.descricao", filter.getDescricao(), MatchMode.ANYWHERE);

            criteria.add(Restrictions.disjunction(c1, c2));
        }

        addRestrictionTo(criteria, "cp1.dataPagamento", filter.getData());

        criteria.add(Restrictions.eq("idContaBancaria", filter.getContaBancaria()));

        return criteria;
    }

    public void criarExtratoSaldoInicial(ContaBancaria contaBancaria) {
        ExtratoContaCorrente extrato = new ExtratoContaCorrente();
        extrato.setIdContaBancaria(contaBancaria);
        extrato.setDataMovimentacao(contaBancaria.getDataSaldo());
        extrato.setSaldo(contaBancaria.getSaldoInicial());
        extrato.setValor(contaBancaria.getSaldoInicial());
        extrato.setSaldoAnterior(0d);
        extrato.setDataCadastro(DataUtil.getHoje());
        extrato.setTipo(EnumTipoExtrato.RECEITA.getTipo());

        adicionar(extrato);
    }

    public int atualizarSaldoContaBancaria(ContaBancaria contaBancaria) {
        List<ExtratoContaCorrente> lancamentos = repositorio.listarExtratoPorContaBancariaAsc(contaBancaria);

        int numeRegistroAtualizados = 0;
        double saldoAnterior = 0d;

        for (int i = 0; i < lancamentos.size(); i++) {
            ExtratoContaCorrente lancamento = lancamentos.get(i);
            if (i == 0) {
                lancamento.setSaldoAnterior(0d);
                lancamento.setSaldo(lancamento.getValor());

                saldoAnterior = lancamento.getSaldo();

                alterar(lancamento);
            } else {
                double diferenca = saldoAnterior - lancamento.getSaldoAnterior();

                // Saldo anterior esta errado
                if (diferenca != 0d) {
                    numeRegistroAtualizados++;

                    lancamento.setSaldoAnterior(lancamento.getSaldoAnterior() + diferenca);

                    // atualiza o saldo
                    if (EnumTipoExtrato.DESPESA.getTipo().equals(lancamento.getTipo())) {
                        lancamento.setSaldo(lancamento.getSaldoAnterior() - lancamento.getValor());
                    } else if (EnumTipoExtrato.RECEITA.getTipo().equals(lancamento.getTipo())) {
                        lancamento.setSaldo(lancamento.getSaldoAnterior() + lancamento.getValor());
                    }

                    alterar(lancamento);
                }
                saldoAnterior = lancamento.getSaldo();
            }
        }

        return numeRegistroAtualizados;
    }

    public void lancarOperacaoExtrato(Object lancamento) {
        ExtratoContaCorrente extrato = null;

        if (lancamento instanceof ContaParcela) {
            ContaParcela conta = (ContaParcela) lancamento;

            if (EnumTipoTransacao.PAGAR.getTipo().equals(conta.getIdConta().getTipoTransacao())) {
                extrato = criarExtrato(conta.getDataPagamento(), conta.getValorPago(), EnumTipoExtrato.DESPESA.getTipo(), conta.getIdContaBancaria());
            } else if (EnumTipoTransacao.RECEBER.getTipo().equals(conta.getIdConta().getTipoTransacao())) {
                extrato = criarExtrato(conta.getDataPagamento(), conta.getValorPago(), EnumTipoExtrato.RECEITA.getTipo(), conta.getIdContaBancaria());
            }
            if (extrato == null) {
                return;
            }

            extrato.setIdContaParcela(conta);
        } else if (lancamento instanceof ContaParcelaParcial) {
            ContaParcelaParcial conta = (ContaParcelaParcial) lancamento;

            if (EnumTipoTransacao.PAGAR.getTipo().equals(conta.getIdContaParcela().getIdConta().getTipoTransacao())) {
                extrato = criarExtrato(conta.getDataPagamento(), conta.getValorPago(), EnumTipoExtrato.DESPESA.getTipo(), conta.getIdContaBancaria());
            } else if (EnumTipoTransacao.RECEBER.getTipo().equals(conta.getIdContaParcela().getIdConta().getTipoTransacao())) {
                extrato = criarExtrato(conta.getDataPagamento(), conta.getValorPago(), EnumTipoExtrato.RECEITA.getTipo(), conta.getIdContaBancaria());
            }
            if (extrato == null) {
                return;
            }

            extrato.setIdContaParcelaParcial(conta);
        }
        if (extrato == null) {
            return;
        }

        Double valor = null;
        if (EnumTipoExtrato.RECEITA.getTipo().equals(extrato.getTipo())) {
            valor = extrato.getValor();
        } else if (EnumTipoExtrato.DESPESA.getTipo().equals(extrato.getTipo())) {
            valor = extrato.getValor() * -1d;
        }
        if (valor != null) {
            repositorio.atualizarLancamentosPosterioresData(extrato.getDataMovimentacao(), extrato.getIdContaBancaria(), valor);
        }

        adicionar(extrato);
    }

    public void cancelarOperacaoExtrato(Object lancamento) {
        ExtratoContaCorrente extrato = null;

        if (lancamento instanceof ContaParcela) {
            ContaParcela conta = (ContaParcela) lancamento;
            extrato = repositorio.buscarLancamento(conta);
        } else if (lancamento instanceof ContaParcelaParcial) {
            ContaParcelaParcial conta = (ContaParcelaParcial) lancamento;
            extrato = repositorio.buscarLancamento(conta);
        }

        if (extrato == null) {
            return;
        }

        List<ExtratoContaCorrente> lancamentos = repositorio.buscarLancamentosCancelamento(extrato);
        if (EnumTipoExtrato.DESPESA.getTipo().equals(extrato.getTipo())) {
            atualizarLancamentos(extrato.getValor(), lancamentos);
        } else if (EnumTipoExtrato.RECEITA.getTipo().equals(extrato.getTipo())) {
            atualizarLancamentos(extrato.getValor() * -1d, lancamentos);
        }

        remover(extrato);
    }

    public void alterarSaldoInicialExtrato(ContaBancaria contaBancaria) {
        ExtratoContaCorrente ext = buscarExtratoSaldoInicial(contaBancaria);
        if (ext == null) {
            criarExtratoSaldoInicial(contaBancaria);
        } else if (contaBancaria.getDataSaldo() != null && contaBancaria.getSaldoInicial() != null) {// Fix para empresas antigas
            ext.setDataMovimentacao(contaBancaria.getDataSaldo());
            ext.setSaldoAnterior(0d);
            ext.setValor(contaBancaria.getSaldoInicial());
            ext.setSaldo(contaBancaria.getSaldoInicial());

            alterar(ext);
        }
        atualizarSaldoContaBancaria(contaBancaria);
    }

    private void atualizarLancamentos(Double valor, List<ExtratoContaCorrente> lancamentos) {
        lancamentos.forEach(lancamento -> {
            lancamento.setSaldo(lancamento.getSaldo() + valor);
            lancamento.setSaldoAnterior(lancamento.getSaldoAnterior() + valor);

            alterar(lancamento);
        });
    }

    private ExtratoContaCorrente criarExtrato(Date dataMovimentacao, Double valor, String tipo, ContaBancaria contaBancaria) {
        Double saldo = buscarSaldo(dataMovimentacao, contaBancaria);

        ExtratoContaCorrente ext = new ExtratoContaCorrente();
        ext.setDataCadastro(DataUtil.getHoje());
        ext.setIdContaBancaria(contaBancaria);
        ext.setDataMovimentacao(dataMovimentacao);
        ext.setValor(valor);
        ext.setTipo(tipo);
        ext.setSaldoAnterior(saldo);

        if (EnumTipoExtrato.RECEITA.getTipo().equals(tipo)) {
            ext.setSaldo(saldo + valor);
        } else if (EnumTipoExtrato.DESPESA.getTipo().equals(tipo)) {
            ext.setSaldo(saldo - valor);
        }

        return ext;
    }

    public Double buscarSaldo(Date dataMovimentacao, ContaBancaria contaBancaria) {
        Double saldo = repositorio.buscarSaldo(dataMovimentacao, contaBancaria);
        return NumeroUtil.somar(saldo, 0d);
    }

    public ExtratoContaCorrente buscarUltimoLancamento(ContaBancaria contaBancaria) {
        return repositorio.buscarUltimoLancamento(contaBancaria);
    }

    public ExtratoContaCorrente buscarExtratoSaldoInicial(ContaBancaria contaBancaria) {
        return repositorio.buscarExtratoSaldoInicial(contaBancaria);
    }

    public List<ExtratoContaCorrente> getListExtratoPorContaBancaria(ContaBancaria contaBancaria) {
        return repositorio.listaExtratoPorContaBancaria(contaBancaria);
    }

    public List<ExtratoContaCorrente> verificarRegistrosDuplicados() {
        return repositorio.verificarRegistrosDuplicados();
    }

    public ExtratoContaCorrente buscarExtratoPorParcela(ContaParcela parcela) {
        return repositorio.buscarExtratoPorParcela(parcela);
    }

    public void verificarAtualizarExtratoParcelas(List<ContaParcela> parcelas) {
        if (parcelas == null) {
            return;
        }
        parcelas.forEach(parcela -> {
            //  verifica se ja houve pagamento parcial da parcela
            List<ContaParcelaParcial> parciais = contaService.listarParciais(parcela);

            // caso sim: verificar se houve lancamento para todas as parciais no extrato e se houve alteração de conta bancaria
            if (!parciais.isEmpty()) {
                parciais.forEach(parcial -> {
                    ExtratoContaCorrente extrato = repositorio.buscarExtratoPorParcelaParcial(parcial);
                    if (extrato == null) {
                        lancarOperacaoExtrato(parcial);
                        return;
                    }
                    // caso estorno de baixa: remover do extrato todas os lancamentos de pagamento parcial
                    if (EnumSituacaoConta.NAO_PAGA.getSituacao().equals(parcela.getSituacao())) {
                        cancelarOperacaoExtrato(parcial);
                        // caso alteração de conta bancaria: alterar os lançamentos do extrato
                    } else if (!extrato.getIdContaBancaria().equals(parcial.getIdContaBancaria())) {
                        cancelarOperacaoExtrato(parcial);
                        lancarOperacaoExtrato(parcial);
                    }
                }); // caso não: verificar se houve lancamento para todas as parciais e se houve alteração de conta bancaria
                return;
            }
            // caso parcela paga: verificar se houve lancamento no extrato
            if (EnumSituacaoConta.PAGA.getSituacao().equals(parcela.getSituacao())) {
                ExtratoContaCorrente extrato = repositorio.buscarExtratoPorParcela(parcela);
                if (extrato == null) {
                    lancarOperacaoExtrato(parcela);
                    // caso alteração de conta bancaria: alterar os lançamentos do extrato
                } else if (!extrato.getIdContaBancaria().equals(parcela.getIdContaBancaria())) {
                    cancelarOperacaoExtrato(parcela);
                    lancarOperacaoExtrato(parcela);
                }
            }

            // remover do extrato caso baixa desfeita
            if (EnumSituacaoConta.NAO_PAGA.getSituacao().equals(parcela.getSituacao()) && repositorio.buscarExtratoPorParcela(parcela) != null) {
                cancelarOperacaoExtrato(parcela);
            }
        });
    }

    public ExtratoContaCorrente buscarExtratoPorParcelaParcial(ContaParcelaParcial parcelaParcial) {
        return repositorio.buscarExtratoPorParcelaParcial(parcelaParcial);
    }

    public List<ControlePagamentoDTO> getControlePagamentoList(Fornecedor fornecedor, MinMax<Date> data) {
        if (fornecedor == null) {
            return fornecedorService.listar().stream()
                    .map(f -> getControlePagamentoDTO(f, data))
                    .filter(dto -> !dto.getItens().isEmpty())
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(getControlePagamentoDTO(fornecedor, data));
    }

    private ControlePagamentoDTO getControlePagamentoDTO(Fornecedor f, MinMax<Date> data) {
        if (data == null) {
            data = new MinMax<>();
        }
        return new ControlePagamentoDTO(f.getRazaoSocial(), repositorio.listarExtratoPorFornecedor(f, data), contaService.listarParcelasPor(f, data)).sort();
    }

}
