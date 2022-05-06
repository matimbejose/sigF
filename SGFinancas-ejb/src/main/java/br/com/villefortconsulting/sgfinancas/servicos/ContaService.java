package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoServico;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.PrevisaoOrcamentaria;
import br.com.villefortconsulting.sgfinancas.entidades.TipoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaSinteticaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoItemDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstatisticaContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FaturaCallbackDto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FechamentoContabilDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImportacaoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ImpostosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.LancamentoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MediaPrazoRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResultadoPlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResumoContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.TimelineDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AnaliseContaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoListagemConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.operator.MinMax;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.Transformers;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaService extends BasicService<Conta, ContaRepositorio, ContaFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ExtratoContaCorrenteService extratoService;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private ContratoService contratoService;

    @EJB
    private VendaService vendaService;

    @EJB
    private CompraService compraService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private TipoPagamentoService tipoPagamentoService;

    @EJB
    private ExtratoContaCorrenteService extratoContaCorrenteService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private PlanoContaPadraoService planoContaPadraoService;

    @EJB
    private PrevisaoOrcamentariaService previsaoOrcamentariaService;

    @EJB
    private CentroCustoService centroCustoService;

    @EJB
    private EmpresaService empresaService;

    private static final String REGEX_DIGITS = "[^0-9]";

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ContaRepositorio(em, adHocTenant);
    }

    public Double somaTotalPago(ContaParcela parcela) {
        return repositorio.somaTotalPago(parcela);
    }

    public Double valorRestanteSerRecebido(ContaParcela parcela) {
        Double valorPago = repositorio.somaTotalPago(parcela);
        if (valorPago != null) {
            return parcela.getValor() - valorPago;
        }
        return parcela.getValor();
    }

    public Double valorRestanteSerPago(ContaParcela parcela) {
        return valorRestanteSerRecebido(parcela);
    }

    public Conta alterarConta(Conta conta) {
        Conta contaAlterada = alterar(conta);

        //busca contrato pela conta
        Contrato contrato = contratoPorConta(contaAlterada);

        //Se ele estiver preenchido, altera.
        if (contrato != null && ("S").equalsIgnoreCase(contrato.getUsaAtualizacaoAutomatica())) {
            contratoService.alteraContratoPorConta(contrato, contaAlterada);
        }

        vendaPorConta(contaAlterada).ifPresent(venda -> vendaService.alteraVendaPorConta(venda, contaAlterada));
        compraPorConta(contaAlterada).ifPresent(compra -> compraService.alteraCompraPorConta(compra, contaAlterada));

        return contaAlterada;
    }

    public Contrato contratoPorConta(Conta conta) {
        return contratoService.buscaContratoPorConta(conta);
    }

    public Optional<Venda> vendaPorConta(Conta conta) {
        return Optional.ofNullable(vendaService.getVendaPorConta(conta));
    }

    public Optional<Compra> compraPorConta(Conta conta) {
        return Optional.ofNullable(compraService.buscaCompraPorConta(conta));
    }

    public void removerContaParcela(ContaParcela contaParcela) {
        repositorio.removerContaParcela(contaParcela);
    }

    public void removerContaParcelaParcial(ContaParcelaParcial contaParcelaParcial) {
        repositorio.removerContaParcelaParcial(contaParcelaParcial);
    }

    public void removerParcela(ContaParcela contaParcela, ContaParcelaParcial contaParcelaParcial) {
        if (contaParcela.getId() != null) {
            repositorio.removerContaParcela(contaParcela);
        } else if (contaParcelaParcial.getId() != null) {
            repositorio.removerContaParcelaParcial(contaParcelaParcial);
        }
    }

    public void cancelarParcelas(Conta conta, List<ContaParcela> parcelas) {
        cancelarConta(conta);
        parcelas.stream()
                .forEach(this::cancelarContaParcela);
        alterar(conta);
    }

    public ContaParcela cancelarContaParcela(ContaParcela contaParcela, List<ContaParcela> parcelasAtribuidas) {
        contaParcela.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
        contaParcela.setDataCancelamento(DataUtil.getHoje());

        List<ContaParcela> parcelas = new LinkedList<>();
        parcelas.add(contaParcela);

        // Atribuindo uma ou mais parcelas a serem salvas caso tenha sido selecionado na tela
        if ("S".equals(contaParcela.getResponsavelCancelamento())) {
            for (ContaParcela parcelasAtribuida : parcelasAtribuidas) {
                parcelasAtribuida.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
                parcelasAtribuida.setIdMotivoCancelamentoConta(contaParcela.getIdMotivoCancelamentoConta());
                parcelasAtribuida.setDataCancelamento(DataUtil.getHoje());
                parcelas.add(parcelasAtribuida);
            }
        }

        // Verificar se todas parcelas estao pagas ou canceladas, caso sim cancelar a conta
        verificarFecharContaDeAcordoComSituacaoDasParcelas(contaParcela.getIdConta(), parcelas);

        for (ContaParcela parcela : parcelas) {
            repositorio.alterarContaParcela(parcela);
            extratoService.cancelarOperacaoExtrato(parcela);
        }

        return contaParcela;
    }

    public ContaParcela reativarContaParcela(ContaParcela contaParcela, List<ContaParcela> parcelasAtribuidas) {
        contaParcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        contaParcela.setDataCancelamento(null);
        contaParcela.setIdMotivoCancelamentoConta(null);

        List<ContaParcela> parcelas = new LinkedList<>();
        parcelas.add(contaParcela);

        for (ContaParcela parcela : parcelas) {
            repositorio.alterarContaParcela(parcela);
        }

        return contaParcela;
    }

    public void pagarParcelasEConta(Conta conta) throws ContaException {
        List<ContaParcela> listParcela = listarContaParcela(conta);

        if (listParcela != null && !listParcela.isEmpty()) {
            for (ContaParcela contaParcela : listParcela) {
                pagarParcelaIntegral(contaParcela);
            }
        }
    }

    public Conta pagarParcelasImportacaoIntegralAndParcial(Conta conta) {
        try {

            List<ContaParcela> listParcela = listarContaParcela(conta);

            if (listParcela != null && !listParcela.isEmpty()) {
                for (ContaParcela contaParcela : listParcela) {
                    if (contaParcela.getValorPago() > 0 && contaParcela.getValorPago() < contaParcela.getValor()) {
                        pagarParcelaParcial(contaParcela, contaParcela.getValorPago());
                    }
                    if (contaParcela.getValorPago() > 0 && contaParcela.getValorPago().compareTo(contaParcela.getValor()) >= 0) {
                        pagarParcelaIntegral(contaParcela);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
        return conta;
    }

    public Conta buscarContaByCliemteMovimentacao(ClienteMovimentacao cm) {
        return repositorio.buscarContaByClienteMovimentacao(cm);
    }

    public void verificarFecharContaDeAcordoComSituacaoDasParcelas(Conta conta, List<ContaParcela> parcelasAlteradas) {
        boolean pagarConta = false;

        List<ContaParcela> parcelas = repositorio.listarContaParcela(conta);

        // Remove da lista as parcelas canceladas pelo usuario na tela
        parcelas.removeIf(parcelasAlteradas::contains);

        // Verifica se existe parcelas em aberto ou se todas estao pagas
        for (ContaParcela parcela : parcelas) {
            if (EnumSituacaoConta.NAO_PAGA.getSituacao().equals(parcela.getSituacao())) {
                return;
            } else if (EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao().equals(parcela.getSituacao())) {
                return;
            } else if (EnumSituacaoConta.PAGA.getSituacao().equals(parcela.getSituacao())) {
                pagarConta = true;
            }
        }

        // Paga conta caso exista ao menos uma parcela paga
        if (pagarConta) {
            pagarConta(conta); // Cancela conta pois todas as parcelas foram canceladas
        } else {
            cancelarConta(conta);
        }
    }

    public Conta pagarConta(Conta conta) {
        conta.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
        return alterar(conta);
    }

    public Conta cancelarConta(Conta conta) {
        conta.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
        conta.setDataCancelamento(DataUtil.getHoje());

        return alterar(conta);
    }

    public ContaParcela cancelarContaParcela(ContaParcela conta) {
        conta.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
        conta.setDataCancelamento(DataUtil.getHoje());
        return repositorio.alterarContaParcela(conta);
    }

    public Conta buscarContaPorIdDaParcela(Integer id) {
        return repositorio.buscarContaPorIdDaParcela(id);
    }

    public List<Conta> listarContaPagar() {
        return repositorio.listarContaPagar();
    }

    public List<Conta> listarContaReceber() {
        return repositorio.listarContaReceber();
    }

    @Override
    public Criteria getModel(ContaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idPlanoConta", "idPlanoConta");

        criteria.add(Restrictions.eq("idConta.tenatID", adHocTenant.getTenantID()));

        addIlikeRestrictionTo(criteria, "idPlanoConta.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "idPlanoConta.tipoConta", filter.getOrigem());

        return criteria;
    }

    public int quantidadeParcelasFiltradas(ContaParcelaFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getContaParcelaModel(filtro));
    }

    public List<ContaParcela> getListaParcelaFiltrada(ContaParcelaFiltro filtro) {
        return repositorio.getListaFiltrada(getContaParcelaModel(filtro), filtro);
    }

    public int quantidadeParcelasAntecipacaoFiltradas(ContaParcelaFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getContaParcelaModelAntecipacao(filtro));
    }

    public List<ContaParcela> getListaParcelaAntecipacaoFiltrada(ContaParcelaFiltro filtro) {
        return repositorio.getListaFiltrada(getContaParcelaModelAntecipacao(filtro), filtro);
    }

    public List<ResumoContaParcelaDTO> obterContaParcelaResumo(ContaParcelaFiltro filtro, String tipoListagem) {
        String tipoListagemOld = filtro.getTipoListagem();
        filtro.setTipoListagem(tipoListagem);
        Criteria criteria = getContaParcelaModel(filtro);
        filtro.setTipoListagem(tipoListagemOld);

        criteria.createAlias("idPlanoConta.idContaPai", "idContaPai", JoinType.LEFT_OUTER_JOIN);

        criteria.setProjection(Projections.projectionList()
                .add(Projections.groupProperty("idPlanoConta.descricao"), "descricao")
                .add(Projections.groupProperty("idPlanoConta.codigo"), "codigo")
                .add(Projections.groupProperty("idContaPai.descricao"), "descricaoPai")
                .add(Projections.groupProperty("idContaPai.codigo"), "codigoPai")
                .add(Projections.sum("valorTotal"), "valor")
                .add(Projections.sum("valorPago"), "valorPago")
        ).setResultTransformer(Transformers.aliasToBean(ResumoContaParcelaDTO.class));

        List<ResumoContaParcelaDTO> lista = new ArrayList<>();

        repositorio.getLista(criteria).stream()
                .map(ResumoContaParcelaDTO.class::cast)
                .map(ResumoContaParcelaDTO::update)
                .forEach(contaParcela -> {
                    ResumoContaParcelaDTO dto = lista.stream()
                            .filter(resumo -> resumo.getCodigo().equals(contaParcela.getCodigoPai()))
                            .findFirst()
                            .orElse(null);
                    if (dto == null) {
                        dto = new ResumoContaParcelaDTO(contaParcela.getDescricaoPai(), contaParcela.getCodigoPai(), 0d);
                        lista.add(dto);
                    }
                    dto.addValor(contaParcela.getValor(), contaParcela.getValorPago());
                    dto.getChildren().add(contaParcela);
                });

        Collections.sort(lista, (p1, p2) -> p1.getValor().compareTo(p2.getValor()));

        return lista;

    }

    public Criteria getContaParcelaModel(ContaParcelaFiltro filtro) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ContaParcela.class);

        criteria.createAlias("idDocumento", "idDocumento", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idMotivoCancelamentoConta", "idMotivoCancelamentoConta", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idBoleto", "idBoleto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idContaBancaria", "idContaBancaria", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCentroCusto", "idCentroCusto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idFormaPagamento", "idFormaPagamento", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idNFS", "idNFS", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta", "idConta");
        criteria.createAlias("idConta.idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idFornecedor", "idFornecedor", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idPlanoConta", "idPlanoConta", JoinType.LEFT_OUTER_JOIN);

        criteria.createAlias("idConta.idContrato", "idConta.idContrato", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.contrato", "idConta.contrato", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.compra", "idConta.compra", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.venda", "idConta.venda", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idUnidadeOcupacao", "idConta.idUnidadeOcupacao", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idContaBancaria", "idConta.idContaBancaria", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idCentroCusto", "idConta.idCentroCusto", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idConta.idDocumento", "idConta.idDocumento", JoinType.LEFT_OUTER_JOIN);

        if (filtro.getId() != null) {
            criteria.add(Restrictions.eq("id", filtro.getId()));
        }

        if (StringUtils.isNotEmpty(filtro.getTipoConta())) {
            criteria.add(Restrictions.eq("idConta.tipoConta", filtro.getTipoConta()));
        }

        if (!filtro.isMostraTransferencia()){
            criteria.add(Restrictions.ne("idConta.tipoConta", EnumTipoConta.TRANSFERENCIA.getTipo()));
        }
        criteria.add(Restrictions.ne("idConta.tipoConta", EnumTipoConta.LANCAMENTO_CONTABIL.getTipo()));

        if (StringUtils.isNotEmpty(filtro.getTipoTransacao())) {
            criteria.add(Restrictions.eq("idConta.tipoTransacao", filtro.getTipoTransacao()));
        }

        if (filtro.isEhConciliacao() && filtro.getSituacao()!= null) {
            if(filtro.getSituacao().equals(EnumSituacaoConta.PAGA.getSituacao())) {
                if (filtro.getDataInicio() != null) {
                    criteria.add(Restrictions.ge("dataPagamento", DataUtil.removerHoras(filtro.getDataInicio())));
                }

                if (filtro.getDataFim() != null) {
                    criteria.add(Restrictions.le("dataPagamento", DataUtil.removerHoras(filtro.getDataFim())));
                }
            }
        }else {
            if (filtro.getDataInicio() != null) {
                criteria.add(Restrictions.ge("dataVencimento", DataUtil.removerHoras(filtro.getDataInicio())));
            }

            if (filtro.getDataFim() != null) {
                criteria.add(Restrictions.le("dataVencimento", DataUtil.removerHoras(filtro.getDataFim())));
            }
        }

        if (filtro.getContaBancaria() != null) {
            criteria.add(Restrictions.eq("idContaBancaria", filtro.getContaBancaria()));
        }

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            Criterion cri = Restrictions.ilike("idPlanoConta.descricao", filtro.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri2 = Restrictions.ilike("idCliente.nome", filtro.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri3 = Restrictions.ilike("idFornecedor.razaoSocial", filtro.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri4 = Restrictions.ilike("idConta.observacao", filtro.getDescricao(), MatchMode.ANYWHERE);

            criteria.add(Restrictions.disjunction(cri, cri2, cri3, cri4));
        }

        if (filtro.getValor() != null) {
            criteria.add(Restrictions.eq("valor", filtro.getValor()));
        }

        if (filtro.getValorTotal() != null) {
            criteria.add(Restrictions.eq("valorTotal", filtro.getValorTotal()));
        }

        if (filtro.getValorPago() != null) {
            criteria.add(Restrictions.eq("valorPago", filtro.getValorPago()));
        }

        if (filtro.getPlanoConta() != null) {
            criteria.add(Restrictions.eq("idConta.idPlanoConta", filtro.getPlanoConta()));
        }

        if (filtro.getCentroCusto() != null) {
            criteria.add(Restrictions.eq("idConta.idCentroCusto", filtro.getCentroCusto()));
        }

        if (StringUtils.isNotEmpty(filtro.getObservacao())) {
            criteria.add(Restrictions.ilike("idConta.observacao", "%" + filtro.getObservacao() + "%"));
        }

        if (filtro.getCliente() != null) {
            filtro.setCliente(clienteService.buscar(filtro.getCliente().getId()));
            criteria.add(Restrictions.eq("idConta.idCliente", filtro.getCliente()));
        }

        if (filtro.getFornecedor() != null) {
            filtro.setFornecedor(fornecedorService.buscar(filtro.getFornecedor().getId()));
            criteria.add(Restrictions.eq("idConta.idFornecedor", filtro.getFornecedor()));
        }

        if (filtro.getFormaPagamento() != null) {
            criteria.add(Restrictions.eq("idFormaPagamento", filtro.getFormaPagamento()));
        }

        if (filtro.getTipoListagem() != null) {
            switch (filtro.getTipoListagem()) {
                case "receber":
                case "pagar":
                    criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.INTERROMPIDO.getSituacao()));
                    criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.PAGA.getSituacao()));
                    criteria.add(Restrictions.ge("dataVencimento", DataUtil.getStartDate(DataUtil.getHoje())));
                    break;
                case "atraso":
                    criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.INTERROMPIDO.getSituacao()));
                    criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.PAGA.getSituacao()));
                    criteria.add(Restrictions.lt("dataVencimento", DataUtil.getStartDate(DataUtil.getHoje())));
                    break;
                case "recebido":
                case "pago":
                    criteria.add(Restrictions.isNotNull("dataPagamento"));
                    break;
                case "hoje":
                    criteria.add(Restrictions.eq("dataVencimento", DataUtil.getHoje()));
                    criteria.add(Restrictions.isNull("dataPagamento"));
                    break;
                default:
                    break;
            }
        } else if (StringUtils.isEmpty(filtro.getSituacao())) {
            criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.CANCELADO.getSituacao()));
            criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.INTERROMPIDO.getSituacao()));
        }

        if (StringUtils.isNotEmpty(filtro.getOrigem())) {
            criteria.add(Restrictions.eq("idConta.tipoConta", filtro.getOrigem()));
        }

        if (StringUtils.isNotEmpty(filtro.getSituacao())) {
            criteria.add(Restrictions.eq("situacao", filtro.getSituacao()));
        } else {
            criteria.add(Restrictions.ne("situacao", "CC"));
        }

        if (filtro.isTemNFS()) {
            criteria.add(Restrictions.isNotNull("idNFS"))
                    .addOrder(Order.asc("idNFS.numeroNotaFiscal"));
            if (filtro.getDataInicio() != null) {
                criteria.add(Restrictions.ge("idNFS.dataEmissao", DataUtil.removerHoras(filtro.getDataInicio())));
            }

            if (filtro.getDataFim() != null) {
                criteria.add(Restrictions.le("idNFS.dataEmissao", DataUtil.removerHoras(filtro.getDataFim())));
            }
        }

        return criteria;
    }

    public Double retornaValorPesquisa(String s) {
        String aPartir = null;
        char[] c = s.toCharArray();

        //Preenche a posição do último valor sem ser número
        for (int i = 0; i < c.length; i++) {
            if (Character.isDigit(c[i])) {
                aPartir = String.valueOf(c[i - 1]);
                break;
            }
        }

        String valor = StringUtils.substringAfterLast(s, aPartir);
        valor = StringUtils.replace(valor, ",", ".");

        return Double.parseDouble(valor);
    }

    public String retornaDescricaoSeparada(String s) {
        String primeiroNumero = null;
        char[] c = s.toCharArray();

        //Preenche a posição do primeiro número
        for (int i = 0; i < c.length; i++) {
            if (Character.isDigit(c[i])) {
                primeiroNumero = String.valueOf(c[i]);
                break;
            }
        }
        return StringUtils.substringBeforeLast(StringUtils.substringBeforeLast(s, primeiroNumero), " ");
    }

    public boolean descricaoComValorNumerico(String s) {
        char[] c = s.toCharArray();
        boolean d = false;

        //Se conter algum número na string, ele é uma descrição com número. Caso contrário, não..
        for (int i = 0; i < c.length; i++) {
            if (Character.isDigit(c[i]) && !Character.isDigit(c[0])) {
                d = true;
                break;
            }
        }
        return d;
    }

    public boolean ehNumero(String s) {
        // cria um array de char
        char[] c = s.toCharArray();
        boolean d;

        // verifica se o char não é um dígito
        if (!Character.isDigit(c[0])) {

            String t = StringUtils.substring(s, 2);

            if (t.length() == 0) {
                t = " ";
            }

            char[] z = t.toCharArray();

            d = Character.isDigit(z[0]);
        } else {
            d = true;
        }

        return d;
    }

    public List<Object> getDadosAuditoriaContaParcelaByID(ContaParcela obj) {
        return repositorio.getDadosAuditoriaByID(ContaParcela.class, obj.getId());
    }

    public ContaParcela salvarContaParcela(ContaParcela parcela) {
        if (parcela.getId() == null) {
            return repositorio.adicionarContaParcela(parcela);
        } else {
            return repositorio.alterarContaParcela(parcela);
        }
    }

    public void atualizarParcelas(List<ContaParcela> parcelas) {
        for (ContaParcela parcela : parcelas) {
            repositorio.alterarContaParcela(parcela);
        }
    }

    public void desfazerBaixa(ContaParcela contaParcela) {
        contaParcela.setValorPago(null);
        contaParcela.setDataPagamento(null);
        contaParcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        contaParcela.setPagamentoParcial("N");

        repositorio.alterarContaParcela(contaParcela);

        Conta conta = contaParcela.getIdConta();
        conta.setValorPago(listarContaParcela(conta).stream()
                .map(ContaParcela::getValorPago)
                .filter(Objects::nonNull)
                .reduce(0d, (a, v) -> a + v));
        if (conta.getValorPago() < conta.getValor()) {
            conta.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
            if (conta.getValorPago() == 0d) {
                conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                conta.setDataPagamento(null);
            }
        }
        alterarConta(conta);

        List<ContaParcelaParcial> listaContaParcelaParcial = repositorio
                .listarParciais(contaParcela);

        for (ContaParcelaParcial contaParcelaParcial : listaContaParcelaParcial) {
            extratoService.cancelarOperacaoExtrato(contaParcelaParcial);
            repositorio.removerContaParcelaParcial(contaParcelaParcial);
        }

        extratoService.cancelarOperacaoExtrato(contaParcela);
    }

    public ContaParcela alterarContaParcela(ContaParcela contaParcela) {
        preencherTotalMaisTributosMaisRestanteParcela(contaParcela, contaParcela.getValorPago());

        if (EnumSituacaoConta.PAGA.getSituacao().equals(contaParcela.getSituacao())) {
            contaParcela.setValorPago(contaParcela.getValorTotal());
        }

        return repositorio.alterarContaParcela(contaParcela);
    }

    public ContaParcela alterarParcelaDiretoRepositorio(ContaParcela contaParcela) {
        return repositorio.alterarContaParcela(contaParcela);
    }

    @Override
    public void remover(Conta conta) {
        //Faz as verificações para poder remover, caso o contrário, lança a exception de acordo com o caso.
        if (EnumTipoConta.NORMAL.getTipo().equals(conta.getTipoConta())
                && (EnumSituacaoConta.NAO_PAGA.getSituacao().equals(conta.getSituacao()) || EnumSituacaoConta.CANCELADO.getSituacao().equals(conta.getSituacao()))) {

            List<ContaParcela> listParcela = listarContaParcela(conta);

            if (listParcela != null && !listParcela.isEmpty()) {
                //Depois remove a parcela.
                listParcela.forEach(contaParcela -> removerParcela(contaParcela, null));

                //remove lancamento contabil
                PlanoContaLancamentoContabil lancamento = planoContaLancamentoService.buscarLancamentoContabilPorConta(conta);
                if (lancamento != null) {
                    planoContaLancamentoService.remover(lancamento);
                }

                //Por fim, a conta.
                super.remover(conta);
            } else {
                throw new CadastroException("Falha ao remover sua conta.", null);
            }
        } else if (EnumSituacaoConta.NAO_PAGA.getSituacao().equals(conta.getSituacao())) {
            throw new CadastroException("Não foi possível remover a conta. A mesma se encontra com alguma parcela paga integral ou parcialmente.", null);
        } else {
            throw new CadastroException("Não foi possível remover a conta. A mesma tem como origem uma compra, venda ou contrato.", null);
        }
    }

    public ContaParcela buscarContaParcela(int id) {
        return repositorio.buscarContaParcela(id);
    }

    public List<ContaParcela> listarContaParcela(Conta conta) {
        return repositorio.listarContaParcela(conta);
    }

    public List<ContaParcela> listarContaParcelaSemTenat(Conta conta) {
        return repositorio.listarContaParcelaSemTenat(conta);
    }

    public List<ContaParcela> listarParcelasPagasSemExtrato() {
        return repositorio.listarParcelasPagasSemExtrato();
    }

    public List<ContaParcela> listarParcelasNaoPagasComExtrato() {
        return repositorio.listarParcelasNaoPagasComExtrato();
    }

    public List<ParcelaDTO> obterParcelaDTO(Conta conta) {
        List<ContaParcela> listContaReceber = listarContaParcela(conta);
        List<ParcelaDTO> listParcelaDTO = new LinkedList<>();

        for (ContaParcela contaReceberParcela : listContaReceber) {
            ParcelaDTO parcelaDTO = new ParcelaDTO();
            parcelaDTO.setDataVencimento(contaReceberParcela.getDataVencimento());
            parcelaDTO.setDesconto(contaReceberParcela.getDesconto());
            parcelaDTO.setNumParcela(contaReceberParcela.getNumParcela());
            parcelaDTO.setValor(contaReceberParcela.getValor());
            parcelaDTO.setDataPagamento(contaReceberParcela.getDataPagamento());
            parcelaDTO.setValorPago(contaReceberParcela.getValorPago());
            parcelaDTO.setObservacao(contaReceberParcela.getObservacao());
            parcelaDTO.setFechada(contaReceberParcela.getFechada());
            parcelaDTO.setSituacao(EnumSituacaoConta.retornaDescricaoPorSituacao(contaReceberParcela.getSituacao()));

            listParcelaDTO.add(parcelaDTO);
        }
        return listParcelaDTO;
    }

    public List<ContaParcelaParcial> listarParciais(ContaParcela parcela) {
        if (parcela.getId() != null) {
            List<ContaParcelaParcial> parciais = repositorio.listarParciais(parcela);
            if (parciais != null) {
                return parciais;
            }
        }
        return new LinkedList<>();
    }

    public void salvarMensalidade(Conta conta, List<UnidadeOcupacao> unidades) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Conta salvarConta(Conta conta) {
        conta = calcularValorTotalConta(conta);
        // Salvar conta
        if (conta.getId() == null) {
            return adicionarConta(conta);
        }
        return alterarConta(conta);
    }

    public List<ContaParcela> verificarPagamentoParcelas(List<ContaParcela> parcelas) {
        StringBuilder msgErro = new StringBuilder();
        if (parcelas == null) {
            throw new CadastroException("Lista de parcelas não informada", null);
        }
        for (ContaParcela parcela : parcelas) {
            EnumSituacaoConta situacao = EnumSituacaoConta.retornaEnumSelecionado(parcela.getSituacao());
            if (situacao != null && (situacao == EnumSituacaoConta.NAO_PAGA || situacao == EnumSituacaoConta.CANCELADO || situacao == EnumSituacaoConta.INTERROMPIDO)) {
                parcela.setDataPagamento(null);
                parcela.setIdFormaPagamento(null);
                parcela.setValorPago(null);
            } else {
                if (parcela.getDataPagamento() == null) {
                    msgErro.append("Informe a data de pagamento da parcela nº ").append(parcela.getNumParcela()).append("\n");
                }
                if (parcela.getIdFormaPagamento() == null) {
                    msgErro.append("Informe a forma de pagamento da parcela nº ").append(parcela.getNumParcela()).append("\n");
                }
            }
        }

        if (StringUtils.isNotEmpty(msgErro)) {
            throw new CadastroException(msgErro.toString(), null);
        }

        return parcelas;
    }

    public Conta salvarConta(Conta conta, Date dataPagamento, Double valorPago) throws ContaException {
        if (conta.getValor() < 0 && !"25.228.539/0001-23".equals(empresaService.getEmpresa().getCnpj())) {
            throw new ContaException("O valor da conta deve ser positivo", null);
        }
        conta.getContaParcelaList().stream()
                .filter(cp -> Boolean.TRUE.equals(cp.getCanceladoAgora()))
                .map(this::listarParciais)
                .flatMap(Collection::stream)
                .forEach(cpp -> {
                    ExtratoContaCorrente ecc = extratoContaCorrenteService.buscarExtratoPorParcelaParcial(cpp);
                    if (ecc != null) {
                        extratoContaCorrenteService.remover(ecc);
                    }
                    removerContaParcelaParcial(cpp);
                });
        conta = calcularValorTotalConta(conta);
        conta.setInformarPagamento(conta.getValorPago() != null ? "S" : "N");
        conta.setPagamentoParcial(conta.getValorPago().compareTo(conta.getValorTotal()) >= 0 ? "N" : "S");
        if (dataPagamento != null && valorPago == null) {
            valorPago = conta.getValorTotal();
        }

        // Salvar conta
        if (conta.getId() == null) {
            conta = adicionarConta(conta, dataPagamento, valorPago);
        } else {
            conta = alterarConta(conta);
        }

        Double valorPagoTotal = conta.getContaParcelaList().stream()
                .map(ContaParcela::getValorPago)
                .filter(Objects::nonNull)
                .reduce(0d, (a, v) -> a + v);
        conta.setValorPago(valorPagoTotal);

        List<EnumSituacaoConta> pagamentos = conta.getContaParcelaList().stream()
                .map(ContaParcela::getSituacao)
                .map(EnumSituacaoConta::retornaEnumSelecionado)
                .distinct()
                .collect(Collectors.toList());

        boolean todasParcelasPagas = pagamentos.size() == 1 && pagamentos.get(0) == EnumSituacaoConta.PAGA;
        boolean naoPago = pagamentos.stream().allMatch(tipo -> EnumSituacaoConta.NAO_PAGA == tipo || EnumSituacaoConta.CANCELADO == tipo || EnumSituacaoConta.INTERROMPIDO == tipo);
        if (todasParcelasPagas) {
            conta.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
        } else if (naoPago) {
            conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        } else {
            conta.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
        }
        return alterarConta(conta);
    }

    public Contrato parcelarContrato(Contrato contrato) {
        if (contrato.getDataVencimentoPagamento() == null) {
            return contrato;
        }

        Date dataVencimento = contrato.getListParcelaDTO().stream()
                .map(ParcelaDTO::getDataVencimento)
                .max(Date::compareTo).orElse(DataUtil.subtrairMeses(contrato.getDataVencimentoPagamento(), 1));

        Integer numeroParcelasInformadas = contrato.getNumeroParcelas();
        Integer numeroParcelasDoContrato = contrato.getListParcelaDTO().size();

        Integer diferencaParcelasInformadasContrato = numeroParcelasInformadas - numeroParcelasDoContrato;

        Double valor = contrato.getContratoServicoList().stream()
                .filter(cs -> "S".equals(cs.getAtivo()))
                .mapToDouble(ContratoServico::getValorTotal)
                .sum();

        if (diferencaParcelasInformadasContrato > 0) {
            List<ParcelaDTO> parcelasAdicionar = contrato.getListParcelaDTO();
            for (int i = 1; i <= diferencaParcelasInformadasContrato; i++) {
                ParcelaDTO parcelaDTO = new ParcelaDTO();
                parcelaDTO.setValor(0d);
                parcelaDTO.setDesconto(0d);
                parcelaDTO.setDataVencimento(DataUtil.adicionarMesVerficandoUltimoDia(dataVencimento, i));
                parcelaDTO.setDataEmissao(DataUtil.getHoje());
                parcelaDTO.setNumParcela(numeroParcelasDoContrato + i);
                parcelaDTO.setFechada("N");
                parcelasAdicionar.add(parcelaDTO);
            }
            contrato.setListParcelaDTO(parcelasAdicionar);
        } else if (diferencaParcelasInformadasContrato < 0) {
            List<ParcelaDTO> parcelasRemover = contrato.getListParcelaDTO();
            parcelasRemover
                    .removeIf(parcela -> parcela.getNumParcela() > contrato.getNumeroParcelas());
            contrato.setListParcelaDTO(parcelasRemover);
        }

        if (valor != 0) {
            contrato.getListParcelaDTO().stream()
                    .forEach(parcela -> parcela.setValor(valor));
        }

        return contrato;
    }

    public Compra parcelarCompra(Compra compra) {
        Double valorTotal = compra.getValorTotal();
        Double valorDesconto = compra.getValorDesconto();
        Date dataVencimento = compra.getPrazoRecebimento();

        if (EnumFormaPagamento.AVISTA.getForma().equals(compra.getFormaPagamento())) {
            compra.setCondicaoPagamento(EnumTipoPagamento.DEBITO.getTipo());
            compra.setNumParcela(1);
            compra.setListParcelaDTO(criarParcela(valorTotal, valorDesconto, dataVencimento));
        } else if (compra.getFormaPagamento() != null) {
            Integer numParcelas = Integer.parseInt(compra.getFormaPagamento().replace("x", ""));
            compra.setCondicaoPagamento(EnumTipoPagamento.CREDITO.getTipo());
            compra.setNumParcela(numParcelas);
            List<ParcelaDTO> parcelasPagas = compra.getListParcelaDTO().stream()
                    .filter(parcela -> parcela.getDataPagamento() != null)
                    .collect(Collectors.toList());
            valorTotal -= parcelasPagas.stream().mapToDouble(ParcelaDTO::getValorPago).sum();
            List<ParcelaDTO> parcelamento = parcelasPagas;
            parcelamento.addAll(parcelarTransacao(parcelasPagas, numParcelas, valorTotal, valorDesconto, dataVencimento));
            for (int i = 0; i < parcelamento.size(); i++) {
                parcelamento.get(i).setNumParcela(i + 1);
            }
            compra.setListParcelaDTO(parcelamento);
        }

        return compra;
    }

    public Venda parcelarVenda(Venda venda) {
        Double valorTotal = venda.getValor();
        Double valorDesconto = NumeroUtil.somar(venda.getValorDesconto());
        Date dataVencimento = venda.getDataVencimento();
        List<ParcelaDTO> parcelas = new LinkedList<>();
        if (venda.getListParcelaDTO() != null) {
            parcelas = venda.getListParcelaDTO()
                    .stream()
                    .filter(parcela -> parcela.getValorPago() != null && parcela.getValorPago() > 0d)
                    .collect(Collectors.toList());
        }

        if (EnumFormaPagamento.AVISTA.getForma().equals(venda.getFormaPagamento())) {
            venda.setCondicaoPagamento(EnumTipoPagamento.DEBITO.getTipo());
            venda.setNumParcela(1);
            venda.setListParcelaDTO(criarParcela(valorTotal + valorDesconto, valorDesconto, dataVencimento));
        } else if (venda.getFormaPagamento() != null) {
            Integer numParcelas = Integer.parseInt(venda.getFormaPagamento().replace("x", ""));
            venda.setCondicaoPagamento(EnumTipoPagamento.CREDITO.getTipo());
            venda.setNumParcela(numParcelas);
            venda.setListParcelaDTO(parcelarTransacaoParcelasPagas(parcelas, numParcelas, valorTotal + valorDesconto, valorDesconto, dataVencimento));
        }

        return venda;
    }

    private static List<ParcelaDTO> criarParcela(Double valorTotal, Double valorDesconto, Date dataVencimento) {
        List<ParcelaDTO> parcelasDTO = new LinkedList<>();
        ParcelaDTO parcelaDTO = new ParcelaDTO();
        parcelaDTO.setValor(valorTotal);
        parcelaDTO.setDesconto(valorDesconto);
        parcelaDTO.setDataVencimento(dataVencimento);
        parcelaDTO.setNumParcela(1);
        parcelasDTO.add(parcelaDTO);

        return parcelasDTO;
    }

    private List<ParcelaDTO> parcelarTransacao(List<ParcelaDTO> parcelasVencidas, Integer numParcelas, Double valorTotal, Double valorDesconto, Date dataVencimento) {
        List<ParcelaDTO> parcelasDTO = new LinkedList<>();

        if (parcelasVencidas == null || parcelasVencidas.isEmpty()) {
            Double valorParcela = NumeroUtil.dividir(valorTotal, numParcelas);
            Double valorDescontoParcela = NumeroUtil.dividir(valorDesconto, numParcelas);

            for (int i = 1; i <= numParcelas; i++) {
                ParcelaDTO parcelaDTO = new ParcelaDTO();
                parcelaDTO.setValor(valorParcela);
                parcelaDTO.setDesconto(valorDescontoParcela);
                parcelaDTO.setDataVencimento(DataUtil.adicionarMesVerficandoUltimoDia(dataVencimento, i - 1));
                parcelaDTO.setNumParcela(i);
                parcelaDTO.setFechada("N");

                parcelasDTO.add(parcelaDTO);

                if (i == numParcelas) {
                    Double valorFinal = corrigirValorParcelaFinal(parcelasDTO, valorTotal, valorParcela);
                    parcelaDTO.setValor(valorFinal);

                    Double descontoFinal = corrigirDescontoFinal(parcelasDTO, valorDesconto, valorDescontoParcela);
                    parcelaDTO.setDesconto(descontoFinal);
                }
            }
        } else {
            List<Integer> nroParcelas = parcelasVencidas.stream().map(ParcelaDTO::getNumParcela).collect(Collectors.toList());
            Integer qteParcelasNaoPagas = numParcelas - nroParcelas.size();
            Double valorParcela = NumeroUtil.dividir(valorTotal, qteParcelasNaoPagas);
            Double valorDescontoParcela = NumeroUtil.dividir(valorDesconto, qteParcelasNaoPagas);

            int n = nroParcelas.size();
            for (int i = 0; i < qteParcelasNaoPagas; i++) {
                ParcelaDTO parcelaDTO = new ParcelaDTO();
                parcelaDTO.setValor(valorParcela);
                parcelaDTO.setDesconto(valorDescontoParcela);
                parcelaDTO.setDataVencimento(DataUtil.adicionarMesVerficandoUltimoDia(dataVencimento, i));
                parcelaDTO.setNumParcela(++n);
                parcelaDTO.setFechada("N");

                parcelasDTO.add(parcelaDTO);

                if ((i + 1) == qteParcelasNaoPagas) {
                    Double valorFinal = corrigirValorParcelaFinal(parcelasDTO, valorTotal, valorParcela);
                    parcelaDTO.setValor(valorFinal);

                    Double descontoFinal = corrigirDescontoFinal(parcelasDTO, valorDesconto, valorDescontoParcela);
                    parcelaDTO.setDesconto(descontoFinal);
                }
            }
        }

        return parcelasDTO;
    }

    public List<ParcelaDTO> parcelarTransacaoParcelasPagas(List<ParcelaDTO> parcelasPagas, Integer numParcelas, Double valorTotal, Double valorDesconto, Date dataVencimento) {
        List<ParcelaDTO> parcelasDTO = new LinkedList<>();

        if (parcelasPagas == null || parcelasPagas.isEmpty()) {
            Double valorParcela = NumeroUtil.dividir(valorTotal, numParcelas);
            Double valorDescontoParcela = NumeroUtil.dividir(valorDesconto, numParcelas);

            for (int i = 1; i <= numParcelas; i++) {
                ParcelaDTO parcelaDTO = new ParcelaDTO();
                parcelaDTO.setValor(valorParcela);
                parcelaDTO.setDesconto(valorDescontoParcela);
                parcelaDTO.setDataVencimento(DataUtil.adicionarMes(dataVencimento, i - 1));
                parcelaDTO.setNumParcela(i);
                parcelasDTO.add(parcelaDTO);

                if (i == numParcelas) {
                    Double valorFinal = corrigirValorParcelaFinal(parcelasDTO, valorTotal, valorParcela);
                    parcelaDTO.setValor(valorFinal);

                    Double descontoFinal = corrigirDescontoFinal(parcelasDTO, valorDesconto, valorDescontoParcela);
                    parcelaDTO.setDesconto(descontoFinal);
                }
            }
        } else {
            List<Integer> nroParcelas = parcelasPagas.stream()
                    .map(ParcelaDTO::getNumParcela)
                    .collect(Collectors.toList());
            Double valorPago = parcelasPagas.stream().mapToDouble(ParcelaDTO::getValorPago).sum();
            List<Integer> nroParcelasNaoPagas = new ArrayList<>();
            for (int i = 1; i <= numParcelas; i++) {
                if (!nroParcelas.contains(i)) {
                    nroParcelasNaoPagas.add(i);
                }
            }
            Integer qteParcelasNaoPagas = numParcelas - nroParcelas.size();
            Double valorParcela = NumeroUtil.dividir(valorTotal - valorPago, qteParcelasNaoPagas);
            Double valorDescontoParcela = NumeroUtil.dividir(valorDesconto, qteParcelasNaoPagas);

            int n = nroParcelas.size();
            int parcelaAtual = nroParcelasNaoPagas.get(0);
            for (int i = 1; i <= qteParcelasNaoPagas; i++) {
                ParcelaDTO parcelaDTO = new ParcelaDTO();
                parcelaDTO.setValor(valorParcela);
                parcelaDTO.setDesconto(valorDescontoParcela);
                parcelaDTO.setDataVencimento(DataUtil.adicionarMes(dataVencimento, parcelaAtual - 1));
                parcelaDTO.setNumParcela(++n);
                parcelasDTO.add(parcelaDTO);

                if (i == qteParcelasNaoPagas) {
                    Double valorFinal = corrigirValorParcelaFinal(parcelasDTO, valorTotal - valorPago, valorParcela);
                    parcelaDTO.setValor(valorFinal);

                    Double descontoFinal = corrigirDescontoFinal(parcelasDTO, valorDesconto, valorDescontoParcela);
                    parcelaDTO.setDesconto(descontoFinal);
                }
                if (nroParcelasNaoPagas.size() > i) {
                    parcelaAtual = nroParcelasNaoPagas.get(i);
                }
            }
        }
        parcelasDTO.addAll(parcelasPagas);

        return parcelasDTO
                .stream().sorted((p1, p2) -> p1.getNumParcela().compareTo(p2.getNumParcela()))
                .collect(Collectors.toList());
    }

    public Double corrigirValorParcelaFinal(final List<ParcelaDTO> parcelasDTO, final Double valorTotal, final Double valorParcela) {
        Double valorASerCorrigido = parcelasDTO.stream()
                .mapToDouble(ParcelaDTO::getValor)
                .sum();
        return calcularValorFinal(valorASerCorrigido, valorTotal, valorParcela);
    }

    public Double calcularValorFinal(final Double valorASerCorrigido, final Double valorTotal, final Double valorParcela) {
        return BigDecimal.valueOf(valorParcela - (valorASerCorrigido - valorTotal))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Double corrigirDescontoFinal(final List<ParcelaDTO> parcelasDTO, Double valorDesconto, Double valorDescontoParcela) {
        Double valorASerCorrigido = parcelasDTO.stream()
                .map(ParcelaDTO::getDesconto)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);
        if (valorDesconto == null) {
            valorDesconto = 0d;
        }
        if (valorDescontoParcela == null) {
            valorDescontoParcela = 0d;
        }
        return calcularValorFinal(valorASerCorrigido, valorDesconto, valorDescontoParcela);
    }

    public Conta adicionarConta(Conta conta, List<ParcelaDTO> parcelas) {
        int i = 1;
        for (ParcelaDTO parcelaDTO : parcelas) {
            ContaParcela parcela = preencherParcelaNova(conta, i, parcelaDTO, null);

            parcela.setValor(parcelaDTO.getValor() + (parcelaDTO.getDesconto() != null ? parcelaDTO.getDesconto() : 0d));
            parcela.setValorTotal(BigDecimal.valueOf(parcela.getValor() + parcela.getTributosTotais()).setScale(2, RoundingMode.HALF_UP).doubleValue());
            parcela.setDesconto(parcelaDTO.getDesconto());
            parcela.setJuros(parcelaDTO.getJuros());
            parcela.setMulta(parcelaDTO.getMulta());
            parcela.setEncargos(parcelaDTO.getEncargos());
            parcela.setOutrosCustos(NumeroUtil.somar(conta.getOutrosCustos(), parcelaDTO.getOutrosCustos()));
            parcela.setDataVencimento(parcelaDTO.getDataVencimento());
            parcela.setIdCentroCusto(conta.getIdCentroCusto());

            conta.addContaParcela(parcela);

            i++;
        }
        return adicionar(conta);
    }

    public List<ContaParcela> alterarParcelaCompra(Compra compra, Conta conta, List<ContaParcela> parcelas, ContaParcela contaParcela, Contrato contrato) {
        List<ContaParcela> parcelasProcessadas = alterarParcela(conta, parcelas, contaParcela, contrato);

        List<ContaParcela> parcelasAbertas = parcelasProcessadas.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());
        for (ContaParcela parcela : parcelasAbertas) {

            if (compra != null) {
                ParcelaDTO parcelaDTO = compra.getListParcelaDTO().get(parcela.getNumParcela() - 1);

                parcela.setValor(parcelaDTO.getValor());
                parcela.setDataVencimento(parcelaDTO.getDataVencimento());
                parcela.setObservacao(compra.getObservacao());
                parcela.setDesconto(parcelaDTO.getDesconto());
                parcela.setAdvemContrato("S");
                parcela = calcularImpostosParcelaPorCliente(parcela);
                parcela = preencherTotalMaisTributosMaisRestanteParcela(parcela, null);
            }

            parcela.setIdContaBancaria(conta.getIdContaBancaria());
        }

        return parcelasAbertas;
    }

    public List<ContaParcela> alterarParcela(Conta conta, List<ContaParcela> parcelas, ContaParcela contaParcela, Contrato contrato) {
        List<ContaParcela> parcelasPagasECanceladas = parcelas.stream()
                .filter(p -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao()))
                .collect(Collectors.toList());

        List<ContaParcela> parcelasAbertas = parcelas.stream()
                .filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao()))
                .collect(Collectors.toList());

        // Alterar quantidade de parcelas da conta
        int diferenca = conta.getNumeroParcelas() - parcelas.size();
        if (diferenca > 0) {
            for (int i = 0; i < diferenca; i++) {
                ContaParcela parcela;
                if (contrato != null) {
                    Conta contaBanco = buscar(contrato.getIdConta().getId());
                    parcela = preencherParcelaNova(conta, contaBanco.getNumeroParcelas() + i + 1, contrato);
                } else {
                    parcela = preencherParcelaNova(conta, conta.getNumeroParcelas() + i, null);
                }
                parcelasAbertas.add(parcela);
            }
        } else if (diferenca < 0) {
            if (conta.getNumeroParcelas() <= parcelasPagasECanceladas.size()) {
                conta.setNumeroParcelas(conta.getNumeroParcelas() + 1);
                return parcelas;
            } else {
                parcelasAbertas.remove(contaParcela);
            }
        }

        // Alterar valores das parcelas em aberto de acordo com a lista temporaria
        for (ContaParcela parcela : parcelasAbertas) {
            ContaBancaria contaBancariaContrato = null;
            if (contrato != null) {
                Integer numParcela = parcela.getNumParcela();
                ParcelaDTO parcelaDTO = contrato.getListParcelaDTODesordenada().stream()
                        .filter(dto -> dto.getNumParcela().equals(numParcela))
                        .findAny()
                        .orElse(null);

                if (parcelaDTO != null) {
                    parcela.setValor(parcelaDTO.getValor());
                    parcela.setDataVencimento(parcelaDTO.getDataVencimento());
                }
                parcela.setObservacao(contrato.getObservacao());
                parcela.setAdvemContrato("S");
                parcela = calcularImpostosParcelaPorCliente(parcela);
                parcela = preencherTotalMaisTributosMaisRestanteParcela(parcela, null);
                parcela.setIdDocumento(conta.getIdDocumento());

                contaBancariaContrato = contrato.getIdContaBancaria();
            }

            parcela.setIdContaBancaria(parcela.getIdContaBancaria() != null ? parcela.getIdContaBancaria() : contaBancariaContrato);
        }

        List<ContaParcela> parcelasAlteradas = new LinkedList<>();

        parcelasAlteradas.addAll(parcelasPagasECanceladas);
        parcelasAlteradas.addAll(parcelasAbertas);

        parcelasAlteradas = parcelasAlteradas
                .stream()
                .sorted((p1, p2) -> p1.getDataVencimento().compareTo(p2.getDataVencimento()))
                .collect(Collectors.toList());

        return listParcelaAlteraNumeroParcelas(parcelasAlteradas);
    }

    public List<ContaParcela> listParcelaAlteraNumeroParcelas(List<ContaParcela> contaParcelas) {
        int i = 1;
        for (ContaParcela contaParcela : contaParcelas) {
            contaParcela.setNumParcela(i);
            i++;
        }

        return contaParcelas;
    }

    private ContaParcela preencherParcelaNova(Conta conta, Integer numParcela, ParcelaDTO parcelaDto, Contrato contrato) {
        return preencherParcelaNova(conta, numParcela, parcelaDto.getValor(), contrato);
    }

    private ContaParcela preencherParcelaNova(Conta conta, Integer numParcela, Contrato contrato) {
        return preencherParcelaNova(conta, numParcela, conta.getValor(), contrato);
    }

    private ContaParcela preencherParcelaNova(Conta conta, Integer numParcela, Double valorParcela, Contrato contrato) {
        ContaParcela parcela = new ContaParcela();

        if (contrato != null) {
            parcela.setValor(contrato.getListParcelaDTO().get(numParcela - 1).getValor());
            parcela.setDataVencimento(contrato.getListParcelaDTO().get(numParcela - 1).getDataVencimento());
            parcela.setDataEmissao(contrato.getDataInicio());
        } else {
            parcela.setValor(valorParcela);
            parcela.setDataVencimento(DataUtil.adicionarMesVerficandoUltimoDia(conta.getDataVencimento(), numParcela - 1));
            parcela.setDataEmissao(conta.getDataEmissao());
        }

        parcela.setIdConta(conta);
        if (parcela.getIdContaBancaria() == null) {
            parcela.setIdContaBancaria(conta.getIdContaBancaria());
        }
        parcela.setNumParcela(numParcela);

        calcularImpostos(parcela);

        parcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        parcela.setTenatID(adHocTenant.getTenantID());

        if (conta.getIdDocumento() != null) {
            parcela.setIdDocumento(conta.getIdDocumento());
        }

        return parcela;
    }

    public ContaParcela calcularImpostos(ContaParcela parcela) {
        // preencher valores IR, PIS, CSLL, INSS, COFINS e ISS da parcela
        parcela = calcularImpostosParcelaPorCliente(parcela);
        return preencherTotalMaisTributosMaisRestanteParcela(parcela, null);
    }

    public Conta adicionarConta(Conta conta) {
        Double valorParcela = conta.getValor() / conta.getNumeroParcelas();
        ParcelaDTO dto = new ParcelaDTO();
        dto.setValor(valorParcela);
        for (int i = 1; i <= conta.getNumeroParcelas(); i++) {
            if (i == conta.getNumeroParcelas()) {
                valorParcela = conta.getValor() - ((i - 1) * valorParcela);
            }
            ContaParcela contaParcela = preencherParcelaNova(conta, i, dto, null);
            contaParcela.setAdvemContrato(conta.getAdvemContrato());
            contaParcela.setObservacao(conta.getObservacao());
            contaParcela.setPagamentoParcial(conta.getPagamentoParcial());
            conta.addContaParcela(contaParcela);
        }

        return adicionar(conta);
    }

    public Conta calcularImpostosContaPorCliente(Conta conta) {
        ImpostosDTO impostos = clienteService.calcularImpostosPorCliente(conta.getValor(), conta.getIdCliente());

        conta.setValorIR(impostos.getIr());
        conta.setValorPIS(impostos.getPis());
        conta.setValorCSLL(impostos.getCsll());
        conta.setValorINSS(impostos.getInss());
        conta.setValorCOFINS(impostos.getCofins());
        conta.setValorISS(impostos.getIss());

        return conta;
    }

    public ContaParcela calcularImpostosParcelaPorCliente(ContaParcela contaParcela) {
        ImpostosDTO impostos = clienteService.calcularImpostosPorCliente(contaParcela.getValor(), contaParcela.getIdConta().getIdCliente());

        contaParcela.setValorIR(impostos.getIr());
        contaParcela.setValorPIS(impostos.getPis());
        contaParcela.setValorCSLL(impostos.getCsll());
        contaParcela.setValorINSS(impostos.getInss());
        contaParcela.setValorCOFINS(impostos.getCofins());
        contaParcela.setValorISS(impostos.getIss());

        return contaParcela;
    }

    public Conta adicionarContaContratoComParcela(Conta conta, Contrato contrato) {
        // Todas as contas adicionadas então no lista temporaria do contrato
        int numParcela = 1;
        for (ParcelaDTO parcelaDTO : contrato.getListParcelaDTO()) {

            ContaParcela contaParcela = new ContaParcela();
            contaParcela.setIdConta(conta);
            contaParcela.setIdContaBancaria(conta.getIdContaBancaria());
            contaParcela.setIdCentroCusto(conta.getIdCentroCusto());
            contaParcela.setTenatID(adHocTenant.getTenantID());
            contaParcela.setNumParcela(numParcela);
            contaParcela.setValor(parcelaDTO.getValor());
            contaParcela.setDataVencimento(parcelaDTO.getDataVencimento());
            contaParcela.setDataEmissao(conta.getDataEmissao());
            contaParcela.setAdvemContrato("S");
            contaParcela.setObservacao(conta.getObservacao());

            if (contaParcela.getDataPagamento() == null) {
                contaParcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
            } else {
                contaParcela.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
            }

            // preencher valores IR, PIS, CSLL, INSS, COFINS e ISS da parcela
            contaParcela = calcularImpostosParcelaPorCliente(contaParcela);
            contaParcela = preencherTotalMaisTributosMaisRestanteParcela(contaParcela, null);

            conta.addContaParcela(contaParcela);

            numParcela++;
        }

        return adicionar(conta);
    }

    public Conta adicionarContaContrato(Conta conta, Contrato contrato) {
        Double descontoConta = 0d;
        for (int i = 0; i < conta.getNumeroParcelas(); i++) {
            // Criar e preencher parcela
            ContaParcela contaParcela = preencherParcelaNova(conta, i + 1, contrato);
            contaParcela.setAdvemContrato("S");

            // preencher valores IR, PIS, CSLL, INSS, COFINS e ISS da parcela
            contaParcela = calcularImpostosParcelaPorCliente(contaParcela);

            // Calcular desconto da parcela e somar ao desconto total da conta
            Double descontoParcela = null;
            if (contrato != null && contrato.getListParcelaDTO() != null && contrato.getListParcelaDTO().get(i) != null) {
                descontoParcela = contrato.getListParcelaDTO().get(i).getDesconto();
            }
            if (descontoParcela != null) {
                contaParcela.setDesconto(descontoParcela);
                contaParcela.setValorTotal(BigDecimal.valueOf(contaParcela.getValor() - descontoParcela)
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
                descontoConta += descontoParcela;
            }

            conta.addContaParcela(contaParcela);
        }

        // preencher valores IR, PIS, CSLL, INSS, COFINS e ISS do contrato
        conta = calcularImpostosContaPorCliente(conta);

        conta.setDesconto(descontoConta);
        conta.setValorTotal(
                BigDecimal.valueOf(conta.getValor() - descontoConta).setScale(2, RoundingMode.HALF_UP)
                        .doubleValue());
        return adicionar(conta);
    }

    private ContaParcela preencherParcela(Conta conta, Double valorParcela) {
        // Calculos valores da parcela
        if (valorParcela == null) {
            valorParcela = NumeroUtil.dividir(conta.getValor(), conta.getNumeroParcelas());
        }

        Double percentualParcela = valorParcela / conta.getValor();
        ContaParcela parcela = new ContaParcela();

        parcela.setIdConta(conta);
        parcela.setIdContaBancaria(conta.getIdContaBancaria());
        parcela.setNumParcela(1);
        parcela.setDataVencimento(conta.getDataVencimento());
        parcela.setDataEmissao(conta.getDataEmissao());
        parcela.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        parcela.setTenatID(adHocTenant.getTenantID());
        parcela.setValor(valorParcela);
        parcela.setJuros(NumeroUtil.somar(conta.getJuros()) * percentualParcela);
        parcela.setEncargos(NumeroUtil.somar(conta.getEncargos()) * percentualParcela);
        parcela.setOutrosCustos(NumeroUtil.somar(conta.getOutrosCustos()) * percentualParcela);
        parcela.setMulta(NumeroUtil.somar(conta.getMulta()) * percentualParcela);
        parcela.setDesconto(NumeroUtil.somar(conta.getDesconto()) * percentualParcela);
        parcela.setValorIR(NumeroUtil.somar(conta.getValorIR()) * percentualParcela);
        parcela.setValorPIS(NumeroUtil.somar(conta.getValorPIS()) * percentualParcela);
        parcela.setValorCSLL(NumeroUtil.somar(conta.getValorCSLL()) * percentualParcela);
        parcela.setValorINSS(NumeroUtil.somar(conta.getValorINSS()) * percentualParcela);
        parcela.setValorCOFINS(NumeroUtil.somar(conta.getValorCOFINS()) * percentualParcela);
        parcela.setValorISS(NumeroUtil.somar(conta.getValorISS()) * percentualParcela);
        parcela.setValorICMS(NumeroUtil.somar(conta.getValorICMS()) * percentualParcela);
        parcela.setTarifa(NumeroUtil.somar(conta.getTarifa()) * percentualParcela);
        parcela.setValorTotal(NumeroUtil.somar(conta.getValorTotal()) * percentualParcela);
        parcela.setIdCentroCusto(conta.getIdCentroCusto());

        return parcela;
    }

    public Date getDataVencimentoParaParcela(Conta conta, int numParcela) {
        switch (conta.getTipoRepeticaoParcelas()) {
            case "S":
                return DataUtil.adicionarDias(conta.getDataVencimento(), 7 * (numParcela - 1));
            case "Q":
                return DataUtil.adicionarDias(conta.getDataVencimento(), 15 * (numParcela - 1));
            case "API":// Recebendo a listagem de datas pela API
                Date data = null;
                if (conta.getListaValorParcela() != null && conta.getListaValorParcela().size() >= numParcela) {
                    data = conta.getListaValorParcela().get(numParcela - 1).getVencimento();
                }
                if (data != null) {
                    return data;
                }
            case "M":
            default:
                return DataUtil.adicionarMesVerficandoUltimoDia(conta.getDataVencimento(), numParcela - 1);
        }
    }

    public Conta adicionarContaEParcela(Conta conta) {
        boolean buscaDaListaDto = conta.getListaValorParcela() != null && conta.getListaValorParcela().size() == conta.getNumeroParcelas();
        for (int i = 1; i <= conta.getNumeroParcelas(); i++) {
            Double valorParcela = null;
            if (buscaDaListaDto) {
                valorParcela = conta.getListaValorParcela().get(i - 1).getValor();
            }
            ContaParcela contaParcela = preencherParcela(conta, valorParcela);
            contaParcela.setDataVencimento(getDataVencimentoParaParcela(conta, i));
            contaParcela.setNumParcela(i);
            conta.addContaParcela(contaParcela);
            if (conta.getTributosTotais() == 0) {
                calcularImpostos(contaParcela);
            }
        }

        return adicionar(conta);
    }

    public Conta adicionarConta(Conta conta, Date dataPagamento, Double valorPago) throws ContaException {
        conta = adicionarContaEParcela(conta);
        // Pagar parcela
        if ("S".equals(conta.getInformarPagamento()) && dataPagamento != null) {
            Double valorRestante = valorPago;
            int count = 1;
            while (valorRestante > 0 && conta.getContaParcelaList().size() >= count) {
                ContaParcela parcela = conta.getContaParcelaList().get(count - 1);
                parcela.setDataPagamento(dataPagamento);
                parcela.setIdFormaPagamento(conta.getFormaPagamento());
                if (valorRestante >= parcela.getValorTotal()) {
                    pagarParcela(parcela, parcela.getValorTotal());
                    valorRestante -= parcela.getValorTotal();
                } else {
                    parcela.setPagamentoParcial(conta.getPagamentoParcial());
                    pagarParcela(parcela, valorRestante);
                    valorRestante = 0d;
                }
                count++;
            }
        }

        // Repetir conta e adicionar a lista de contas a serem pagas
        if ("S".equals(conta.getRepetirConta()) && conta.getQtdRepeticao() != null) {
            repetirConta(conta).forEach(this::adicionarConta);
        }

        return conta;
    }

    private List<Conta> repetirConta(Conta contaOrigem) {
        List<Conta> contas = new LinkedList<>();
        for (int i = 1; i <= contaOrigem.getQtdRepeticao(); i++) {
            Date dataVencimento = DataUtil.calcularDataPorPeriodoDeRepeticao(contaOrigem.getDataVencimento(), contaOrigem.getTipoRepeticao(), i);

            contas.add(preencherReplicaConta(contaOrigem, dataVencimento));
        }

        return contas;
    }

    private Conta preencherReplicaConta(Conta contaOrigem, Date dataVencimento) {
        Conta contaRepetida = new Conta();
        contaRepetida.setDataVencimento(dataVencimento);
        contaRepetida.setValor(contaOrigem.getValor());
        contaRepetida.setValorTotal(contaOrigem.getValorTotal());
        contaRepetida.setIdContaBancaria(contaOrigem.getIdContaBancaria());
        contaRepetida.setRepetirConta("N");
        contaRepetida.setInformarPagamento("N");
        contaRepetida.setIdCliente(contaOrigem.getIdCliente());
        contaRepetida.setIdFornecedor(contaOrigem.getIdFornecedor());
        contaRepetida.setIdCentroCusto(contaOrigem.getIdCentroCusto());
        contaRepetida.setObservacao(contaOrigem.getObservacao());
        contaRepetida.setIdDocumento(contaOrigem.getIdDocumento());
        contaRepetida.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        contaRepetida.setIdPlanoConta(contaOrigem.getIdPlanoConta());
        contaRepetida.setTipoConta(contaOrigem.getTipoConta());
        contaRepetida.setTipoTransacao(contaOrigem.getTipoTransacao());
        contaRepetida.setJuros(contaOrigem.getJuros());
        contaRepetida.setEncargos(contaOrigem.getEncargos());
        contaRepetida.setOutrosCustos(contaOrigem.getOutrosCustos());
        contaRepetida.setMulta(contaOrigem.getMulta());
        contaRepetida.setDesconto(contaOrigem.getDesconto());
        contaRepetida.setValorIR(contaOrigem.getValorIR());
        contaRepetida.setValorPIS(contaOrigem.getValorPIS());
        contaRepetida.setValorCSLL(contaOrigem.getValorCSLL());
        contaRepetida.setValorINSS(contaOrigem.getValorINSS());
        contaRepetida.setValorCOFINS(contaOrigem.getValorCOFINS());
        contaRepetida.setValorISS(contaOrigem.getValorISS());
        contaRepetida.setTenatID(adHocTenant.getTenantID());

        return contaRepetida;
    }

    public ContaParcela pagarParcela(ContaParcela parcela, Double valorPago) throws ContaException {
        // Pagar parcialmente valores abaixo do valor restante total
        if ("S".equals(parcela.getPagamentoParcial())) {
            parcela.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
            parcela.setValorPago(valorPago);
            return pagarParcelaParcial(parcela, valorPago);
        }
        return pagarParcelaIntegral(parcela);
    }

    public ContaParcela pagarParcelaIntegral(ContaParcela parcela) throws ContaException {
        ContaParcela parcelaAnterior = parcela;

        // preenchendo e pagando parcela integral
        parcela = salvarContaParcela(prencherRecebimentoParcelaIntegral(parcela));

        // Baixar conta
        verificarBaixarConta(parcela);

        // Informar valor recebido conta
        receberConta(parcela.getIdConta(), parcela.getValorPago());

        // enviar para extrato
        extratoService.lancarOperacaoExtrato(parcela);

        //Se o valor total da parcela advir de um contrato
        if ("S".equals(parcelaAnterior.getAdvemContrato())) {

            List<ContaParcela> listParcelaAberto = listarParcelasEmAberto(parcela);
            List<ContaParcela> listParcelas = listarContaParcela(parcela.getIdConta());
            listParcelas = listParcelas.stream().filter(p -> EnumSituacaoConta.PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());

            //Executará o método somente se o mesmo usar atualização automatica (condição dentro do outro método)
            contratoService.alteraContratoValorParcelaMaior(parcela.getIdConta(), listParcelaAberto.size(), listParcelas, parcela);
        }

        return parcela;
    }

    public ContaParcela pagarParcelaParcial(ContaParcela parcela, Double valorPago) {
        // preechendo parcela parcial
        parcela = salvarContaParcela(preencherRecebimentoParcelaParcial(parcela, valorPago));

        // Baixar parcela caso seja ultima parcial
        verificarBaixarParcela(parcela);

        // Informar valor recebido conta
        receberConta(parcela.getIdConta(), valorPago);

        // Baixar conta caso seja ultima parcela
        verificarBaixarConta(parcela);

        // enviar para extrato
        ContaParcelaParcial parcial = repositorio.buscarUltimaParcial(parcela);

        extratoService.lancarOperacaoExtrato(parcial);

        return parcela;
    }

    public void verificarBaixarConta(ContaParcela parcela) {
        Conta conta = parcela.getIdConta();
        // Vericar se todas parcelas da conta estao pagas e baixar
        if (!repositorio.existeParcelasEmAberto(conta) && EnumSituacaoConta.PAGA.getSituacao().equals(parcela.getSituacao())) {

            Date dataRecebimento = repositorio.buscarUltimaDataPagamento(conta);

            conta.setDataPagamento(dataRecebimento);
            conta.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
            alterarConta(conta);
        }
    }

    public void verificarBaixarParcela(ContaParcela parcela) {
        Double diff = parcela.getValorTotal() - parcela.getValorPago();
        if ((parcela.getValorTotal() > 0 && diff <= 0d) || (parcela.getValorTotal() < 0 && diff > 0d)) {
            repositorio.alterarContaParcela(prencherRecebimentoParcelaIntegral(parcela));
        }
    }

    private static ContaParcela prencherRecebimentoParcelaIntegral(ContaParcela parcela) {
        parcela.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
        parcela.setValorPago(parcela.getValorTotal());

        if (parcela.getDataPagamento() != null) {
            parcela.setDataPagamento(parcela.getDataPagamento());
        } else {//Caso seja advindo de uma transferência entre contas.
            parcela.setDataPagamento(parcela.getIdConta().getDataVencimento());
        }

        return parcela;
    }

    private ContaParcela preencherRecebimentoParcelaParcial(ContaParcela parcela, Double valorPago) {
        // Preenchendo pagamento parcial
        ContaParcelaParcial parcial = new ContaParcelaParcial();
        parcial.setIdContaBancaria(parcela.getIdContaBancaria());
        parcial.setIdContaParcela(parcela);
        parcial.setIdFormaPagamento(parcela.getIdFormaPagamento());
        parcial.setDataPagamento(parcela.getDataPagamento());
        parcial.setDataVencimento(parcela.getDataVencimento());
        parcial.setValorTotal(parcela.getValorTotal());
        parcial.setValorPago(valorPago);
        parcial.setTenatID(adHocTenant.getTenantID());

        // Adicionando a lista de pagamento parciais da parcela
        List<ContaParcelaParcial> parciais = listarParciais(parcela);
        parciais.add(parcial);

        // Salvando novos valores da parcela
        parcela.setValorPago(parciais.stream()
                .mapToDouble(ContaParcelaParcial::getValorPago)
                .sum());
        parcela.setContaParcelaParcialList(parciais);
        parcela.setValorRestante(parcela.getValorTotal() - valorPago);
        if ((parcela.getValorTotal() > 0d && parcela.getValorRestante() > 0) || (parcela.getValorTotal() < 0d && parcela.getValorRestante() < 0)) {
            parcela.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
        }

        return parcela;
    }

    private Conta receberConta(Conta conta, Double valor) {
        double valorPago = conta.getContaParcelaList().stream()
                .map(ContaParcela::getValorPago)
                .filter(Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);

        conta.setValorPago(valorPago);
        if (valorPago == 0d) {
            conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        } else if (valorPago >= conta.getValorTotal()) {
            conta.setSituacao(EnumSituacaoConta.PAGA.getSituacao());
        } else {
            conta.setSituacao(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao());
        }

        return alterarConta(conta);
    }

    public boolean existeParcelasEmAberto(Conta conta) {
        return repositorio.existeParcelasEmAberto(conta);
    }

    public CardContaDTO buscarEstatisticasConta(Date min, Date max, EnumTipoListagemConta tipoListagem, EnumTipoTransacao tipoTransacao) {
        ContaParcelaFiltro filtro = new ContaParcelaFiltro();
        filtro.setDataInicio(min);
        filtro.setDataFim(max);
        filtro.setTipoTransacao(tipoTransacao.getTipo());
        return buscarEstatisticasConta(filtro, tipoListagem);
    }

    public EstatisticaContaDTO buscarEstatistica(EnumTipoTransacao tipo, ContaParcelaFiltro filtro) {
        EstatisticaContaDTO estatisticaContaDTO = new EstatisticaContaDTO();

        CardContaDTO cardRecebido = buscarEstatisticasConta(filtro.getDataInicio(), filtro.getDataFim(), EnumTipoListagemConta.RECEBIDO, tipo);
        estatisticaContaDTO.setQtdPago(cardRecebido.getQuantidade());
        estatisticaContaDTO.setValorPago(cardRecebido.getValor());

        CardContaDTO cardCreditoAtraso = buscarEstatisticasConta(filtro.getDataInicio(), filtro.getDataFim(), EnumTipoListagemConta.ATRASO, tipo);
        estatisticaContaDTO.setValorEmAtraso(cardCreditoAtraso.getValor());
        estatisticaContaDTO.setQtdEmAtraso(cardCreditoAtraso.getQuantidade());

        CardContaDTO cardReceber = buscarEstatisticasConta(filtro.getDataInicio(), filtro.getDataFim(), EnumTipoListagemConta.RECEBER, tipo);
        estatisticaContaDTO.setValorPendente(cardReceber.getValor());
        estatisticaContaDTO.setQtdPendente(cardReceber.getQuantidade());

        estatisticaContaDTO.setValorTotal(cardReceber.getValor() + cardRecebido.getValor() + cardCreditoAtraso.getValor());
        estatisticaContaDTO.setQtdTotal(cardReceber.getQuantidade() + cardRecebido.getQuantidade() + cardCreditoAtraso.getQuantidade());

        return estatisticaContaDTO;
    }

    public boolean possuiOutrasParcelasEmAberto(ContaParcela contaParcela) {
        return repositorio.possuiOutrasParcelasEmAberto(contaParcela);
    }

    public List<ContaParcela> listarParcelasEmAberto(ContaParcela contaParcela) {
        return repositorio.listarParcelasEmAberto(contaParcela);
    }

    public List<ContaParcela> listarParcelasMes(Date competencia, boolean apenasPagos) {
        return repositorio.listarParcelasMes(competencia, apenasPagos);
    }

    public boolean existeParcelaPaga(Venda venda) {
        if (venda.getId() != null) {
            return repositorio.existeParcelaPaga(venda.getIdConta());
        }
        return false;
    }

    public boolean existeParcelaPaga(OrdemDeServico os) {
        if (os.getId() != null) {
            return repositorio.existeParcelaPaga(os.getIdConta());
        }
        return false;
    }

    public boolean existeParcelaPaga(Contrato contrato) {
        if (contrato.getId() != null) {
            return repositorio.existeParcelaPaga(contrato.getIdConta());
        }
        return false;
    }

    public boolean existeParcelaPaga(Compra compra) {
        if (compra.getId() != null) {
            return repositorio.existeParcelaPaga(compra.getIdConta());
        }
        return false;
    }

    public Conta buscarConta(Venda venda) {
        return repositorio.buscarConta(venda);
    }

    public Conta buscarConta(Compra compra) {
        return repositorio.buscarConta(compra);
    }

    public Double obterValorLancamento(PlanoConta planoConta, Date data) {
        return repositorio.obterValorLancamento(planoConta, data);
    }

    public List<ValorLancamentoDTO> obterTodosValorLancamentoRecebimento(Date data) {
        return repositorio.obterTodosValorLancamentoRecebimento(data);
    }

    public List<ValorLancamentoDTO> obterTodosValorLancamentoPagamento(Date data) {
        return repositorio.obterTodosValorLancamentoRecebimento(data);
    }

    public EstatisticaContaDTO obterValorReceber() {
        return repositorio.obterValorReceber(new Date(), new Date());
    }

    public EstatisticaContaDTO obterValorPagar() {
        return repositorio.obterValorPagar(new Date(), new Date());
    }

    public EstatisticaContaDTO obterValorReceberAtraso() {
        return repositorio.obterValorReceberAtraso(new Date(), new Date());
    }

    public EstatisticaContaDTO obterValorPagarAtraso() {
        return repositorio.obterValorPagarAtraso(new Date(), new Date());
    }

    public EstatisticaContaDTO obterValorReceber(Date dataInicio, Date dataFim) {
        return repositorio.obterValorReceber(dataInicio, dataFim);
    }

    public EstatisticaContaDTO obterValorPagar(Date dataInicio, Date dataFim) {
        return repositorio.obterValorPagar(dataInicio, dataFim);
    }

    public EstatisticaContaDTO obterValorReceberAtraso(Date dataInicio, Date dataFim) {
        return repositorio.obterValorReceberAtraso(dataInicio, dataFim);
    }

    public EstatisticaContaDTO obterValorPagarAtraso(Date dataInicio, Date dataFim) {
        return repositorio.obterValorPagarAtraso(dataInicio, dataFim);
    }

    public List<ValorLancamentoDTO> obterValorReceitaFluxoCaixa(Date dataInicio, Date dataFim, CentroCusto centro, List<String> planosTransferencia) {
        return repositorio.obterValorReceitaFluxoCaixa(dataInicio, dataFim, centro, planosTransferencia);
    }

    public List<ValorLancamentoDTO> obterValorDespesaFluxoCaixa(Date dataInicio, Date dataFim, CentroCusto centro, List<String> planosTransferencia) {
        return repositorio.obterValorDespesaFluxoCaixa(dataInicio, dataFim, centro, planosTransferencia);
    }

    public Double obterValorReceitaFluxoCaixa(Date data, CentroCusto centro, List<String> planosTransferencia) {
        return NumeroUtil.somar(repositorio.obterValorReceitaFluxoCaixa(data, centro, planosTransferencia));
    }

    public Double obterValorDespesaFluxoCaixa(Date data, CentroCusto centro, List<String> planosTransferencia) {
        return NumeroUtil.somar(repositorio.obterValorDespesaFluxoCaixa(data, centro, planosTransferencia));
    }

    public List<ValorLancamentoDTO> obterValorReceitaMensal(Date dataInicio, Date dataFim) {
        return repositorio.obterValorMensal(dataInicio, dataFim, EnumTipoTransacao.RECEBER);
    }

    public List<ValorLancamentoDTO> obterValorDespesaMensal(Date dataInicio, Date dataFim) {
        return repositorio.obterValorMensal(dataInicio, dataFim, EnumTipoTransacao.PAGAR);
    }

    public List<ValorLancamentoDTO> obterValorDespesaDreMensalByCodigo(Date dataInicio, Date dataFim, String codigoConta) {
        return repositorio.obterValorDreMensal(dataInicio, dataFim, codigoConta, EnumTipoTransacao.PAGAR);
    }

    public List<ValorLancamentoDTO> obterValorReceitaDreMensalByCodigo(Date dataInicio, Date dataFim, String codigoConta) {
        return repositorio.obterValorDreMensal(dataInicio, dataFim, codigoConta, EnumTipoTransacao.RECEBER);
    }

    public List<ValorLancamentoDTO> obterValorDespesaDreMensal(Date dataInicio, Date dataFim, CentroCusto centro, boolean ehFluxo) {
        return repositorio.obterValorDreMensal(dataInicio, dataFim, EnumTipoTransacao.PAGAR, centro, ehFluxo);
    }

    public List<ValorLancamentoDTO> obterValorReceitaDreMensal(Date dataInicio, Date dataFim, CentroCusto centro, boolean ehFluxo) {
        return repositorio.obterValorDreMensal(dataInicio, dataFim, EnumTipoTransacao.RECEBER, centro, ehFluxo);
    }

    public List<AnaliseContaDTO> listarDadosAnaliseRecebimentoAnalitico(AnaliseContaFiltro analiseContaFiltro) {
        analiseContaFiltro.setTipoConta(EnumTipoTransacao.RECEBER.getTipo());
        return repositorio.obterAnaliseConta(analiseContaFiltro);

    }

    public List<AnaliseContaSinteticaDTO> listarDadosAnaliseRecebimentoSintetico(AnaliseContaFiltro analiseContaFiltro) {
        analiseContaFiltro.setTipoConta(EnumTipoTransacao.RECEBER.getTipo());
        List<AnaliseContaSinteticaDTO> listDados = repositorio.obterAnaliseContaSinteticoPorContaBancaria(analiseContaFiltro);
        listDados.addAll(repositorio.obterAnaliseContaSinteticoPorPlanoContas(analiseContaFiltro));
        return listDados;

    }

    public List<AnaliseContaDTO> listarDadosAnalisePagamentoAnalitico(AnaliseContaFiltro analiseContaFiltro) {
        analiseContaFiltro.setTipoConta(EnumTipoTransacao.PAGAR.getTipo());
        return repositorio.obterAnaliseConta(analiseContaFiltro);
    }

    public List<AnaliseContaSinteticaDTO> listarDadosAnalisePagamentoSintetico(AnaliseContaFiltro analiseContaFiltro) {
        analiseContaFiltro.setTipoConta(EnumTipoTransacao.PAGAR.getTipo());
        List<AnaliseContaSinteticaDTO> listDados = repositorio.obterAnaliseContaSinteticoPorContaBancaria(analiseContaFiltro);
        listDados.addAll(repositorio.obterAnaliseContaSinteticoPorPlanoContas(analiseContaFiltro));
        return listDados;
    }

    public Conta calcularValoresContaPorParcelas(Conta conta, List<ContaParcela> parcelas) {
        conta.setValor(0d);
        conta.setValorTotal(0d);
        conta.setJuros(null);
        conta.setDesconto(null);
        conta.setEncargos(null);
        conta.setOutrosCustos(null);
        conta.setMulta(null);
        conta.setValorIR(0d);
        conta.setValorPIS(0d);
        conta.setValorCSLL(0d);
        conta.setValorINSS(0d);
        conta.setValorCOFINS(0d);
        conta.setValorISS(0d);

        for (ContaParcela parcela : parcelas) {
            if (parcela.getJuros() != null) {
                if (conta.getJuros() != null) {
                    conta.setJuros(BigDecimal.valueOf(conta.getJuros() + parcela.getJuros())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setJuros(parcela.getJuros());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() + parcela.getJuros())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getEncargos() != null) {
                if (conta.getEncargos() != null) {
                    conta.setEncargos(BigDecimal.valueOf(conta.getEncargos() + parcela.getEncargos())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setEncargos(parcela.getEncargos());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() + parcela.getEncargos())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getOutrosCustos() != null) {
                if (conta.getOutrosCustos() != null) {
                    conta.setOutrosCustos(
                            BigDecimal.valueOf(conta.getOutrosCustos() + parcela.getOutrosCustos())
                                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setOutrosCustos(parcela.getOutrosCustos());
                }
                conta.setValorTotal(conta.getValorTotal() + parcela.getOutrosCustos());
            }
            if (parcela.getValorIR() != null) {
                if (conta.getValorIR() != null) {
                    conta.setValorIR(BigDecimal.valueOf(conta.getValorIR() + parcela.getValorIR())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorIR(parcela.getValorIR());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorIR())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValorPIS() != null) {
                if (conta.getValorPIS() != null) {
                    conta.setValorPIS(BigDecimal.valueOf(conta.getValorPIS() + parcela.getValorPIS())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorPIS(parcela.getValorPIS());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorPIS())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValorCSLL() != null) {
                if (conta.getValorCSLL() != null) {
                    conta.setValorCSLL(BigDecimal.valueOf(conta.getValorCSLL() + parcela.getValorCSLL())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorPIS(parcela.getValorCSLL());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorCSLL())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValorINSS() != null) {
                if (conta.getValorINSS() != null) {
                    conta.setValorINSS(BigDecimal.valueOf(conta.getValorINSS() + parcela.getValorINSS())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorINSS(parcela.getValorINSS());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorINSS())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValorCOFINS() != null) {
                if (conta.getValorCOFINS() != null) {
                    conta.setValorCOFINS(
                            BigDecimal.valueOf(conta.getValorPIS() + parcela.getValorCOFINS())
                                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorCOFINS(parcela.getValorCOFINS());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorCOFINS())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValorISS() != null) {
                if (conta.getValorISS() != null) {
                    conta.setValorISS(BigDecimal.valueOf(conta.getValorISS() + parcela.getValorISS())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setValorISS(parcela.getValorISS());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getValorISS())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getDesconto() != null) {
                if (conta.getDesconto() != null) {
                    conta.setDesconto(BigDecimal.valueOf(conta.getDesconto() + parcela.getDesconto())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setDesconto(parcela.getDesconto());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() - parcela.getDesconto())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getMulta() != null) {
                if (conta.getMulta() != null) {
                    conta.setMulta(BigDecimal.valueOf(conta.getMulta() + parcela.getMulta())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
                } else {
                    conta.setMulta(parcela.getMulta());
                }
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() + parcela.getMulta())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
            if (parcela.getValor() != null && conta.getValor() != null) {
                conta.setValor(conta.getValor() + parcela.getValor());
                conta.setValorTotal(BigDecimal.valueOf(conta.getValorTotal() + parcela.getValor())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue());
            }
        }

        return conta;
    }

    public Conta calcularValorTotalConta(Conta conta) {
        conta.setValorTotal(NumeroUtil.somar(conta.getValor(), calcularTributosTotaisConta(conta)));
        return conta;
    }

    public Double calcularTributosTotaisConta(Conta conta) {
        Double tributos = 0d;
        if (conta.getJuros() != null) {
            tributos += conta.getJuros();
        }
        if (conta.getEncargos() != null) {
            tributos += conta.getEncargos();
        }
        if (conta.getMulta() != null) {
            tributos += conta.getMulta();
        }
        if (conta.getOutrosCustos() != null) {
            tributos += conta.getOutrosCustos();
        }
        if (conta.getValorIR() != null) {
            tributos -= conta.getValorIR();
        }
        if (conta.getValorPIS() != null) {
            tributos -= conta.getValorPIS();
        }
        if (conta.getValorCSLL() != null) {
            tributos -= conta.getValorCSLL();
        }
        if (conta.getValorINSS() != null) {
            tributos -= conta.getValorINSS();
        }
        if (conta.getValorCOFINS() != null) {
            tributos -= conta.getValorCOFINS();
        }
        if (conta.getValorISS() != null) {
            tributos -= conta.getValorISS();
        }
        if (conta.getDesconto() != null) {
            tributos -= conta.getDesconto();
        }
        if (conta.getValorICMS() != null) {
            tributos -= conta.getValorICMS();
        }
        if (conta.getTarifa() != null) {
            tributos -= conta.getTarifa();
        }

        return BigDecimal.valueOf(tributos).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public ContaParcela preencherTotalMaisTributosMaisRestanteParcela(ContaParcela parcela, Double valorTotalPago) {
        // Preenchendo total tributos
        parcela = preencherTributosParcela(parcela);

        // Preenchendo valor restante e o valor total
        if (valorTotalPago != null && valorTotalPago > 0) {
            parcela.setValorTotal(BigDecimal.valueOf(valorTotalPago + parcela.getTributosTotais())
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
            parcela.setValorRestante(BigDecimal.valueOf(parcela.getValorTotal() - valorTotalPago)
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            parcela.setValorTotal(BigDecimal.valueOf(parcela.getValor() + parcela.getTributosTotais())
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
            parcela.setValorRestante(
                    BigDecimal.valueOf(parcela.getValorTotal() - parcela.getValorPago())
                            .setScale(2, RoundingMode.HALF_UP).doubleValue());
        }

        return parcela;
    }

    public ContaParcela preencherTributosParcela(ContaParcela parcela) {
        Double tributos = 0d;
        if (parcela.getJuros() != null) {
            tributos += parcela.getJuros();
        }
        if (parcela.getEncargos() != null) {
            tributos += parcela.getEncargos();
        }
        if (parcela.getMulta() != null) {
            tributos += parcela.getMulta();
        }
        if (parcela.getOutrosCustos() != null) {
            tributos += parcela.getOutrosCustos();
        }
        if (parcela.getValorIR() != null) {
            tributos -= parcela.getValorIR();
        }
        if (parcela.getValorPIS() != null) {
            tributos -= parcela.getValorPIS();
        }
        if (parcela.getValorCSLL() != null) {
            tributos -= parcela.getValorCSLL();
        }
        if (parcela.getValorINSS() != null) {
            tributos -= parcela.getValorINSS();
        }
        if (parcela.getValorCOFINS() != null) {
            tributos -= parcela.getValorCOFINS();
        }
        if (parcela.getValorISS() != null) {
            tributos -= parcela.getValorISS();
        }
        if (parcela.getDesconto() != null) {
            tributos -= parcela.getDesconto();
        }
        if (parcela.getAcrescimo() != null) {
            tributos += parcela.getAcrescimo();
        }
        if (parcela.getValorICMS() != null) {
            tributos -= parcela.getValorICMS();
        }
        if (parcela.getTarifa() != null && "R".equals(parcela.getIdConta().getTipoTransacao())) {
            tributos -= parcela.getTarifa();
        } else {
            if (parcela.getTarifa() != null) {
                tributos += parcela.getTarifa();
            }
        }
        parcela.setTributosTotais(
                BigDecimal.valueOf(tributos).setScale(2, RoundingMode.HALF_UP).doubleValue());

        return parcela;
    }

    public void pagarParcelas(List<ContaParcela> listParcela) throws ContaException {
        //Soma o valor que será pago em todas as parcelas e depois as paga, desde que, não estejam canceladas ou pagas.
        for (ContaParcela contaParcela : listParcela) {
            if (!EnumSituacaoConta.PAGA.getSituacao().equals(contaParcela.getSituacao()) || !EnumSituacaoConta.CANCELADO.getSituacao().equals(contaParcela.getSituacao())) {

                Double valorPago = BigDecimal.valueOf(contaParcela.getValorTotal() - contaParcela.getValorPago())
                        .setScale(2, RoundingMode.HALF_UP).doubleValue();

                if (contaParcela.getPagamentoParcial() == null) {
                    contaParcela.setPagamentoParcial("N");
                }

                contaParcela.setDataPagamento(DataUtil.getHoje());

                pagarParcela(contaParcela, valorPago);
            }
        }
    }

    public Conta obterContaImportada(Conta conta) {
        return repositorio.obterContaImportada(conta);
    }

    public ContaParcela adicionarParcela(ContaParcela parcela) {
        return repositorio.adicionarContaParcela(parcela);
    }

    public ContaParcela removerParcelaPorConta(Conta conta) {
        return repositorio.removeContaParcelaPorConta(conta);
    }

    public List<Conta> contasPorFornecedor(Fornecedor fornecedor) {
        return repositorio.contasPorFornecedor(fornecedor);
    }

    public List<Conta> contasPorCliente(Cliente cliente) {
        return repositorio.contasPorCliente(cliente);
    }

    public List<ContaParcela> parcelasPorCliente(Cliente cliente) {
        return repositorio.parcelasPorCliente(cliente);
    }

    public Optional<Conta> contaPorNFS(NFS nfs) {
        return Optional.ofNullable(repositorio.contaPorNFS(nfs));
    }

    public List<ContaParcela> parcelasPorNFS(NFS nfs) {
        return repositorio.parcelasPorNFS(nfs);
    }

    public List<ContaParcela> parcelasEmAberto(Conta conta) {
        return repositorio.listarParcelasEmAberto(conta);
    }

    public List<ContaParcela> listarParcelasMes(Integer mes, Integer ano, String fechada) {
        return repositorio.listarParcelasMes(mes, ano, fechada);
    }

    public boolean parcelaPossuiNFServico(ContaParcela parcela) {
        return repositorio.parcelaPossuiNFServico(parcela);
    }

    public List<Conta> contaPorContrato(Contrato contrato) {
        return repositorio.contaPorContrato(contrato);
    }

    public void preencheContaPorTx(Contrato contrato, String tipoTaxa, String tipoTransacao) {
        Conta conta = new Conta();

        conta.setAdvemContrato("S");
        conta.setIdCliente(contrato.getIdCliente());
        conta.setIdFornecedor(contrato.getIdFornecedor());
        conta.setIdContaBancaria(contrato.getIdContaBancaria());
        conta.setIdDocumento(contrato.getIdDocumento());
        conta.setIdPlanoConta(contrato.getIdPlanoConta());
        conta.setInformarPagamento("N");
        conta.setNumeroParcelas(1);
        conta.setRepetirConta("N");
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setTipoTransacao(tipoTransacao);
        conta.setTenatID(adHocTenant.getTenantID());
        conta.setIdContrato(contrato);

        if (contrato.getIdDocumento() != null) {
            conta.setIdDocumento(contrato.getIdDocumento());
        }

        if (EnumTipoConta.TAXA_ADESAO.getTipo().equals(tipoTaxa)) {
            conta.setValor(contrato.getTaxaAdesao());
            conta.setValorTotal(contrato.getTaxaAdesao());
            conta.setDataVencimento(contrato.getDataVencimentoAdesao());
            conta.setTipoConta(EnumTipoConta.TAXA_ADESAO.getTipo());
            conta.setObservacao("Taxa de adesão de contrato");
        } else {
            conta.setValor(contrato.getTaxaInstalacao());
            conta.setValorTotal(contrato.getTaxaInstalacao());
            conta.setDataVencimento(contrato.getDataVencimentoInstalacao());
            conta.setTipoConta(EnumTipoConta.TAXA_INSTALACAO.getTipo());
            conta.setObservacao("Taxa de instalação de contrato");
        }

        adicionarConta(conta);
    }

    public void alteraContaPorTx(Contrato contrato) {
        List<Conta> listConta = contaPorContrato(contrato);

        if (listConta == null || listConta.isEmpty()) {
            String tipo = null;
            if (contrato.getTaxaAdesao() != null && contrato.getDataVencimentoAdesao() != null) {
                tipo = EnumTipoConta.TAXA_ADESAO.getTipo();
            }
            if (contrato.getTaxaInstalacao() != null && contrato.getDataVencimentoInstalacao() != null) {
                tipo = EnumTipoConta.TAXA_INSTALACAO.getTipo();
            }
            if (tipo != null) {
                preencheContaPorTx(contrato, tipo, (EnumTipoContrato.FORNECEDOR.getTipo().equals(contrato.getTipoContrato()))
                        ? EnumTipoTransacao.PAGAR.getTipo() : EnumTipoTransacao.RECEBER.getTipo());
            }
            return;
        }
        listConta.stream()
                .filter(conta -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(conta.getSituacao()))
                .forEachOrdered(conta -> {
                    if (EnumTipoConta.TAXA_ADESAO.getTipo().equals(conta.getTipoConta()) && contrato.getTaxaAdesao() != null) {
                        conta.setValor(contrato.getTaxaAdesao());
                        conta.setValorTotal(contrato.getTaxaAdesao());
                        conta.setDataVencimento(contrato.getDataVencimentoAdesao());

                        listarContaParcela(conta).stream()
                                .filter(contaParcela -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(contaParcela.getSituacao()) && conta.getValor() != null)
                                .map(contaParcela -> processaParcelaParaAlterarContaPorTx(contrato, conta, contaParcela))
                                .forEachOrdered(this::alterarContaParcela);
                    } else if (EnumTipoConta.TAXA_INSTALACAO.getTipo().equals(conta.getTipoConta()) && contrato.getTaxaInstalacao() != null) {
                        conta.setValor(contrato.getTaxaInstalacao());
                        conta.setValorTotal(contrato.getTaxaInstalacao());
                        conta.setDataVencimento(contrato.getDataVencimentoInstalacao());

                        listarContaParcela(conta).stream()
                                .filter(contaParcela -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(contaParcela.getSituacao()))
                                .map(contaParcela -> processaParcelaParaAlterarContaPorTx(contrato, conta, contaParcela))
                                .forEachOrdered(this::alterarContaParcela);
                    }
                    conta.setIdDocumento(contrato.getIdDocumento());
                    alterar(conta);
                });
    }

    private static ContaParcela processaParcelaParaAlterarContaPorTx(Contrato contrato, Conta conta, ContaParcela contaParcela) {
        Double valorParcela = conta.getValor() / conta.getNumeroParcelas();
        contaParcela.setValor(valorParcela);
        contaParcela.setValorTotal(valorParcela);

        if (contrato.getIdDocumento() != null) {
            contaParcela.setIdDocumento(contrato.getIdDocumento());
        } else {
            conta.setIdDocumento(null);
        }
        return contaParcela;
    }

    public Conta contaPorCodigoBarraServico(String codBoleto, Conta conta) {
        //Seta apenas os campos cábiveis do boleto
        if (!codBoleto.substring(0, 6).contains(".")) {
            conta.setDataVencimento(dataVencimentoContaServico(codBoleto, "semMascara"));
            conta.setValor(valorContaCodBarServico(codBoleto, "semMascara"));
        } else {
            conta.setDataVencimento(dataVencimentoContaServico(codBoleto, "comMascara"));
            conta.setValor(valorContaCodBarServico(codBoleto, "comMascara"));
        }

        return conta;
    }

    public Date dataVencimentoContaServico(String codBoleto, String tipo) {
        if ("comMascara".equals(tipo)) {
            //Pega somente os valores depois do espaço.
            String ultValor = StringUtils.substringAfterLast(codBoleto, " ");

            //Pega os quatro primeiros valores
            String valor = StringUtils.substring(ultValor, 0, 4);

            //Passa os dias para inteiros
            int dias = Integer.parseInt(valor);

            //Converte a data somando os dias conforme padrão do boleto.
            if (dias != 0) {
                return DataUtil.adicionarDias(DataUtil.converterStringParaDate("07/10/1997"), dias);
            } else {
                return null;
            }
        } else {
            //00190000090141561000301609950173770990000033859
            //Pega somente os últimos valores.
            String ultValor = StringUtils.substring(codBoleto, 33, 48);

            //Pega os quatro primeiros valores
            String valor = StringUtils.substring(ultValor, 0, 4);

            //Passa os dias para inteiros
            int dias = Integer.parseInt(valor);

            //Converte a data somando os dias conforme padrão do boleto.
            return DataUtil.adicionarDias(DataUtil.converterStringParaDate("07/10/1997"), dias);
        }
    }

    public Double valorContaCodBarServico(String codBoleto, String tipo) {
        String ultValor;
        if ("comMascara".equals(tipo)) {
            ultValor = StringUtils.substringAfterLast(codBoleto, " ");//Pega somente os valores depois do espaço.
        } else {
            ultValor = StringUtils.substring(codBoleto, 33, 48);//Pega somente os últimos valores.
        }
        String valor = StringUtils.substring(ultValor, 5);//Pega o valor a partir daquele inicio.
        return (Integer.parseInt(valor) + 0d) / 100;
    }

    public Conta contaPorCodigoBarraConcessionaria(String codBoleto, Conta conta) {
        //Seta apenas os campos cábiveis do boleto
        conta.setValor(valorContaCodBarConcessionaria(codBoleto.replaceAll("\\D", "")));
        return conta;
    }

    public Double valorContaCodBarConcessionaria(String codBoleto) {
        //Pega somente os valores depois do código da data e antes do espaço
        String valorASerSomado = codBoleto.substring(4, 10);

        //Transoforma o mesmo em inteiro para remover os zeros a esquerda
        int valor = Integer.parseInt(valorASerSomado);

        //Passa o mesmo para String em seguida antes da soma com o valor anterior.
        String valorInicial = valor + "";

        //Pega o resto do valor depois do espaço
        String restoValor = codBoleto.substring(12, 16);

        //Soma as Strings, gerando assim, o valor a ser dividido.
        String valorTotal = valorInicial + restoValor;

        return (Long.parseLong(valorTotal) + 0d) / 100;
    }

    public String calculaCodigoBoletoServico(String barra) {
        //Remover o que não for número
        String linha = barra.replaceAll(REGEX_DIGITS, "");

        if (linha.length() != 44) {
            return null; //A linha do Código de Barras está incompleta!
        }
        String campo1 = linha.substring(0, 4) + linha.substring(19, 20) + '.' + linha.substring(20, 24);
        String campo2 = linha.substring(24, 29) + '.' + linha.substring(29, 34);
        String campo3 = linha.substring(34, 39) + '.' + linha.substring(39, 44);
        String campo4 = linha.substring(4, 5); // Digito verificador
        String campo5 = linha.substring(5, 19); // Vencimento + Valor

        return campo1 + modulo10(campo1)
                + ' '
                + campo2 + modulo10(campo2)
                + ' '
                + campo3 + modulo10(campo3)
                + ' '
                + campo4
                + ' '
                + campo5;
    }

    public int modulo10(String numero) {
        numero = numero.replaceAll(REGEX_DIGITS, "");
        int soma = 0;
        int peso = 2;
        int contador = numero.length() - 1;
        while (contador >= 0) {
            int multiplicacao = Integer.parseInt(numero.substring(contador, contador + 1)) * peso;
            if (multiplicacao >= 10) {
                multiplicacao = 1 + (multiplicacao - 10);
            }
            soma += multiplicacao;
            if (peso == 2) {
                peso = 1;
            } else {
                peso = 2;
            }
            contador -= 1;
        }
        int digito = 10 - (soma % 10);
        if (digito == 10) {
            digito = 0;
        }

        return digito;
    }

    public String calculaCodigoBoletoConcessionaria(String barra) {
        // Remover caracteres não numéricos.
        String linha = barra.replaceAll(REGEX_DIGITS, "");

        if (linha.length() != 44) {
            return null; // A linha do Código de Barras está incompleta!
        }
        String campo1 = linha.substring(0, 11) + ' ' + linha.substring(4, 5);
        String campo2 = linha.substring(11, 22) + ' ' + calculaCodigoVerificadorConcessionaria(linha.substring(11, 22));
        String campo3 = linha.substring(22, 33) + ' ' + calculaCodigoVerificadorConcessionaria(linha.substring(22, 33));
        String campo4 = linha.substring(33, 44) + ' ' + calculaCodigoVerificadorConcessionaria(linha.substring(33, 44));

        return campo1
                + ' '
                + campo2
                + ' '
                + campo3
                + ' '
                + campo4;
    }

    public String calculaCodigoVerificadorConcessionaria(String sequencia) {
        int valorSerConvertido = 0;
        int j = 1;

        for (int i = 0; i < sequencia.length(); i++) {

            int valor = Integer.parseInt(String.valueOf(sequencia.charAt(i)));

            if (j % 2 == 0) {
                valor *= 1;
            } else {
                valor *= 2;
            }

            j++;

            String valorString = String.valueOf(valor);
            for (int k = 0; k < valorString.length(); k++) {
                valorSerConvertido += Integer.parseInt(valorString.charAt(k) + "");
            }
        }

        return (10 - (valorSerConvertido % 10)) + "";
    }

    public List<ContaParcela> buscarParcela(TransacaoIntegracaoBancaria transacaoIntegracaoBancaria, ContaBancaria contaBancaria) {
        return repositorio.buscarParcela(transacaoIntegracaoBancaria, contaBancaria);
    }

    public List<PlanoContaLancamentoDTO> listarValorPrevisao(Date dataInicio, Date dataFim, CentroCusto centro) {
        List<PrevisaoOrcamentaria> listaPrevisao = previsaoOrcamentariaService.listarPorPeriodo(dataInicio, centro);
        List<PlanoContaLancamentoDTO> lista = new ArrayList<>();
        for (PrevisaoOrcamentaria previsao : listaPrevisao) {
            PlanoContaLancamentoDTO prev = new PlanoContaLancamentoDTO();
            prev.setIdPlanoConta(previsao.getIdPlanoConta());
            prev.setIdPlano(previsao.getIdPlanoConta().getId());
            prev.setPrevisao(previsao.getPrevisao());
            prev.setValor(previsao.getPrevisao());
            lista.add(prev);
        }

        return lista;
    }

    public List<PlanoContaLancamentoDTO> listarValorRealizado(Date dataInicio, Date dataFim, CentroCusto centro) {
        return repositorio.listarValorRealizado(dataInicio, dataFim, centro);
    }

    public List<LancamentoCaixaDTO> obterLancamentoNoCaixa(Empresa empresa, PlanoConta planoConta, Date dataInicio, Date dataFinal, ContaBancaria contaBancaria, CentroCusto centroCusto) {
        return repositorio.listaLancamentoNoCaixa(empresa, planoConta, dataInicio, dataFinal, contaBancaria, centroCusto);
    }

    public String getPrimeiraDataDeFechamento() {
        return repositorio.getPrimeiraDataDeFechamento();
    }

    public List<FechamentoContabilDTO> listarFechamentoContabil(Date dataInicio, Date dataFim) {
        return repositorio.listarFechamentoContabil(dataInicio, dataFim);
    }

    public List<ContaParcela> listarParcelasPagamentoAtraso(String tenatID) {
        return repositorio.listarParcelasPagamentoAtraso(tenatID);
    }

    public List<ContaParcela> listarParcelasPagamentoVencimentoHoje(String tenatID) {
        return repositorio.listarParcelasPagamentoVencimentoHoje(tenatID);
    }

    public String[][] criarArquivoExcelFechamentoContabil(List<ContaParcela> parcelas) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

        String[][] texto = new String[parcelas.size() + 1][21];
        texto[0][0] = "PLANO CONTA";
        texto[0][1] = "OBSERVACAO";
        texto[0][2] = "CONTA BANCÁRIA";
        texto[0][3] = "NRO PARCELA";
        texto[0][4] = "QTD PARCELAS";
        texto[0][5] = "SITUACAO";
        texto[0][6] = "VALOR";
        texto[0][7] = "VALOR PAGO";
        texto[0][8] = "DESCONTO";
        texto[0][9] = "JUROS";
        texto[0][10] = "ENCARGOS";
        texto[0][11] = "MULTA";
        texto[0][12] = "OUTROS CUSTOS";
        texto[0][13] = "IR";
        texto[0][14] = "PIS";
        texto[0][15] = "CSLL";
        texto[0][16] = "INSS";
        texto[0][17] = "COFINS";
        texto[0][18] = "ISS";
        texto[0][19] = "DATA VENCIMENTO";
        texto[0][20] = "DATA PAGAMENTO";

        for (int i = 1; i <= parcelas.size(); i++) {
            ContaParcela p = parcelas.get(i - 1);
            texto[i][0] = p.getIdConta().getIdPlanoConta().getDescricao();
            texto[i][1] = StringUtil.nullCoalesce("", p.getObservacao());
            texto[i][2] = (p.getIdConta().getIdContaBancaria() != null) ? p.getIdConta().getIdContaBancaria().getDescricao() : "";
            texto[i][3] = p.getNumParcela().toString();
            texto[i][4] = p.getIdConta().getNumeroParcelas().toString();
            texto[i][5] = EnumSituacaoConta.getDescricao(p.getSituacao());
            texto[i][6] = NumeroUtil.converterDecimalParaString(p.getValor(), 2);
            texto[i][7] = NumeroUtil.converterDecimalParaString(p.getValorPago(), 2);
            texto[i][8] = NumeroUtil.converterDecimalParaString(p.getDesconto(), 2);
            texto[i][9] = NumeroUtil.converterDecimalParaString(p.getJuros(), 2);
            texto[i][10] = NumeroUtil.converterDecimalParaString(p.getEncargos(), 2);
            texto[i][11] = NumeroUtil.converterDecimalParaString(p.getMulta(), 2);
            texto[i][12] = NumeroUtil.converterDecimalParaString(p.getOutrosCustos(), 2);
            texto[i][13] = NumeroUtil.converterDecimalParaString(p.getValorIR(), 2);
            texto[i][14] = NumeroUtil.converterDecimalParaString(p.getValorPIS(), 2);
            texto[i][15] = NumeroUtil.converterDecimalParaString(p.getValorCSLL(), 2);
            texto[i][16] = NumeroUtil.converterDecimalParaString(p.getValorINSS(), 2);
            texto[i][17] = NumeroUtil.converterDecimalParaString(p.getValorCOFINS(), 2);
            texto[i][18] = NumeroUtil.converterDecimalParaString(p.getValorISS(), 2);
            texto[i][19] = format.format(p.getDataVencimento());
            texto[i][20] = (p.getDataPagamento() != null) ? format.format(p.getDataPagamento()) : "";
        }
        return texto;
    }

    public ContaParcela buscarParcelaPorNFS(NFS nfs) {
        return repositorio.buscarParcelaPorNFS(nfs);
    }

    public EstatisticaContaDTO obterValorContaAtrasoPeriodo(ContaParcelaFiltro filtro) {
        Criteria criteria = setUpCriteria(filtro, "valorTotal");
        criteria.add(Restrictions.isNull("dataPagamento"));
        criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.INTERROMPIDO.getSituacao()));
        criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.PAGA.getSituacao()));
        Date ontem = DataUtil.subtrairDias(new Date(), 1);
        criteria.add(Restrictions.le("dataVencimento", ontem));
        return (EstatisticaContaDTO) repositorio.getLista(criteria).stream().findFirst().orElse(new EstatisticaContaDTO());
    }

    public CardContaDTO buscarValorContaAtrasoPeriodo(ContaParcelaFiltro filtro) {
        return buscarEstatisticasContaParcela(filtro);
    }

    public CardContaDTO buscarEstatisticasConta(ContaParcelaFiltro filtro, EnumTipoListagemConta tipoListagemConta) {
        filtro.setTipoListagem(tipoListagemConta.getTipo());
        CardContaDTO cardContaDTO = buscarEstatisticasContaParcela(filtro);
        if (cardContaDTO == null) {
            cardContaDTO = new CardContaDTO();
        }

        return cardContaDTO;
    }

    public CardContaDTO buscarEstatisticasContaParcela(ContaParcelaFiltro filtro) {
        Criteria criteria = criarCriteriaCardContaDto(filtro);
        criteria.add(Restrictions.eq("tenatID", adHocTenant.getTenantID()));

        return (CardContaDTO) criteria.uniqueResult();
    }

    public Criteria criarCriteriaCardContaDto(ContaParcelaFiltro filtro) {
        if (filtro.getTipoListagem() == null) {
            return null;
        }
        Constructor c;
        ProjectionList projection;
        try {
            switch (filtro.getTipoListagem()) {
                case "receber":
                case "pagar":
                case "atraso":
                    c = CardContaDTO.class.getConstructor(Double.class, Double.class, Long.class);
                    projection = Projections.projectionList()
                            .add(Projections.sum("valorTotal"), "valorTotal")
                            .add(Projections.sum("valorPago"), "valorPago")
                            .add(Projections.count("valorTotal"), "qtdTotal");
                    break;
                case "recebido":
                case "pago":
                    c = CardContaDTO.class.getConstructor(Double.class, Long.class);
                    projection = Projections.projectionList()
                            .add(Projections.sum("valorPago"), "valorTotal")
                            .add(Projections.count("valorTotal"), "qtdTotal");
                    break;
                default:
                    return null;
            }
        } catch (SecurityException | NoSuchMethodException ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }

        return getContaParcelaModel(filtro)
                .setProjection(projection)
                .setResultTransformer(new AliasToBeanConstructorResultTransformer(c))
                .add(Restrictions.ne("situacao", EnumSituacaoConta.CANCELADO.getSituacao()));
    }

    private Criteria setUpCriteria(ContaParcelaFiltro filtro, String campo) {
        return getContaParcelaModel(filtro)
                .setProjection(Projections.projectionList()
                        .add(Projections.sum(campo), "valorTotal")
                        .add(Projections.count(campo), "qtdTotal"))
                .setResultTransformer(Transformers.aliasToBean(EstatisticaContaDTO.class))
                .add(Restrictions.ne("situacao", EnumSituacaoConta.CANCELADO.getSituacao()));

    }

    public Criteria getContaParcelaModelAntecipacao(ContaParcelaFiltro filtro) {
        filtro.setOrigem(EnumTipoConta.VENDA.getTipo());
        filtro.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        return getContaParcelaModel(filtro);
    }

    public List<TimelineDTO> getTimeline(Date dataInicio, Date dataFim, boolean pago) {
        return repositorio.getTimeline(dataInicio, dataFim, pago);
    }

    public Conta preencheContaIUGU(Cliente cliente, FaturaCallbackDto fatura, CentroCusto centro, ClienteMovimentacao clienteMovimentacao) throws ContaException {
        return preencheContaIUGU(cliente, fatura, centro, clienteMovimentacao, null);
    }

    public Conta preencheContaIUGU(Cliente cliente, FaturaCallbackDto fatura, CentroCusto centro, ClienteMovimentacao clienteMovimentacao, PlanoConta planoConta) throws ContaException {
        if (planoConta == null) {
            planoConta = planoContaService.getPlanoContaPadrao((Integer) null);
        }
        ContaParcela parcela = null;
        if (fatura.getIdFatura() != null) {
            parcela = repositorio.buscarContaParcelaPorNumNF(fatura.getIdFatura());
        }
        Conta conta = new Conta();
        if (parcela == null) {
            conta = repositorio.buscarContaByClienteMovimentacao(clienteMovimentacao);
        }
        if (parcela != null) {
            conta = parcela.getIdConta();
        } else if (conta == null) {
            conta = new Conta();
            conta.setIdCentroCusto(centro);
            conta.setDataEmissao(fatura.getDataCriacao());
            conta.setDataVencimento(fatura.getDataVencimento());
            if (centro.getIdContaBancaria() != null) {
                conta.setIdContaBancaria(centro.getIdContaBancaria());
            }
            List<TipoPagamento> tipoPagamento = tipoPagamentoService.findByDescricao("À vista");
            conta.setIdPlanoConta(planoConta);
            conta.setTipoConta(EnumTipoConta.NORMAL.getTipo());
            conta.setIdTipoPagamento(tipoPagamento.isEmpty() ? null : tipoPagamento.get(0));
            conta.setIdCliente(cliente);
            conta.setIdClienteMovimentacao(clienteMovimentacao);
            conta.setNumeroParcelas(1);
            conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
            conta.setTipoRepeticao("M");
            if (fatura.getTotalDesconto() != null) {
                conta.setDesconto(fatura.getTotalDesconto());
            }
            conta.setValor(fatura.getTotalFatura());
            if (fatura.getQuantidadeParcelas() != null) {
                conta.setNumeroParcelas(fatura.getQuantidadeParcelas());
                conta.setDataVencimento(DataUtil.converterStringParaDate(fatura.getParcelas().get(0).getDataVencimento(), "dd/MM/yy HH:mm"));
                for (int i = 0; i < fatura.getParcelas().size(); i++) {
                    ValorParcelaDTO vp = new ValorParcelaDTO();
                    vp.setValor(Double.parseDouble(fatura.getParcelas().get(i).getValorParcela()) + Double.parseDouble(fatura.getParcelas().get(i).getValorTaxa()));
                    conta.getListaValorParcela().add(vp);
                }
            }
        }
        if (fatura.getDataPagamento() != null) {
            if (fatura.getTotalTaxaPaga() != null) {
                conta.setTarifa(fatura.getTotalTaxaPaga());
            }
            conta.setValorPago(fatura.getTotalPago());
            if (conta.getTarifa() != null) {
                conta.setValorPago(fatura.getTotalPago() - conta.getTarifa());
            }
            if (fatura.getTotalJuros() != null) {
                conta.setJuros(fatura.getTotalJuros());
            }
            if (fatura.getFormaPagamento() != null) {
                switch (fatura.getFormaPagamento().toLowerCase()) {
                    case "iugu_credit_card":
                    case "iugu_credit_card_test":
                        conta.setFormaPagamento(formaPagamentoService.findByDescricao("Cartão de Crédito"));
                        break;
                    case "iugu_bank_slip":
                        conta.setFormaPagamento(formaPagamentoService.findByDescricao("Boleto Bancário"));
                        break;
                    case "iugu_pix":
                        conta.setFormaPagamento(formaPagamentoService.findByDescricao("Pix"));
                        break;
                    default:
                        conta.setFormaPagamento(formaPagamentoService.findByDescricao("Outros"));
                        break;
                }
            }
            if (fatura.getDataLiquidacao() != null) {
                conta.setDataPagamento(fatura.getDataLiquidacao());
                return salvarConta(conta, fatura.getDataLiquidacao(), fatura.getTotalPago());
            }
            if (fatura.getDataLiquidacao() == null && (fatura.getQuantidadeParcelas() != null
                    || (fatura.getFormaPagamento() != null && (fatura.getFormaPagamento().equalsIgnoreCase("iugu_pix") || fatura.getFormaPagamento().equalsIgnoreCase("iugu_bank_slip"))))) {
                conta.setValorPago(0d);
                return salvarConta(conta, null, null);
            }
            return salvarConta(conta, fatura.getDataPagamento(), fatura.getTotalPago());
        }
        return salvarConta(conta);

    }

    public ContaParcela buscarContaParcelaPorNumNF(String numero) {
        return repositorio.buscarContaParcelaPorNumNF(numero);
    }

    public void cancelarContaEBaixa(FaturaCallbackDto fatura) {
        try {
            ContaParcela contaParcela = buscarContaParcelaPorNumNF(fatura.getIdFatura());
            if (contaParcela != null) {
                contaParcela.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
                contaParcela.setCanceladoAgora(true);
                contaParcela.setDataCancelamento(fatura.getDataCancelamentoSG());
                salvarContaParcela(contaParcela);

                Conta conta = contaParcela.getIdConta();
                verificarPagamentoParcelas(conta.getContaParcelaList());
                salvarConta(conta, null, null);
                conta.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());
                conta.setDataCancelamento(fatura.getDataCancelamentoSG());
                alterar(conta);
                extratoContaCorrenteService.verificarAtualizarExtratoParcelas(conta.getContaParcelaList());
            }
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public void cancelamentoBaixaEConta(ContaParcela contaParcela) {
        try {
            desfazerBaixa(contaParcela);
            salvarContaParcela(contaParcela);

            Conta conta = contaParcela.getIdConta();
            verificarPagamentoParcelas(conta.getContaParcelaList());
            salvarConta(conta, null, null);
            cancelarConta(conta);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public boolean estaPago(Conta conta) {
        return conta != null && conta.getSituacao().equals("PG");
    }

    public List<MediaPrazoRelatorioDTO> listaMediaPrazoPagamentoRecebimento(Date dataInicio, Date dataFim) {
        return repositorio.listaMediaPrazoRecebimentoPagamento(dataInicio, dataFim);
    }

    public List<Conta> listarContaPagarParcialmentePagaByFornecedor(Fornecedor fornecedor) {
        return repositorio.listarContaPagarParcialmentePagasByFornecedor(fornecedor);
    }

    public List<ContaParcela> listarContaParcelaRelatorio(Conta conta) {
        return repositorio.listarContaParcelaRelatorio(conta);
    }

    public List<EntityId> importDto(List<DtoId> importacaoLista, String tipoTransacao, String tenantID) throws MessageListException {
        boolean processando = false;
        int parcelaAtual = 0;
        int qteParcelas = 0;
        List<String> listaErros = new ArrayList<>();
        List<List<ImportacaoContaDTO>> matrixDto = new ArrayList<>();
        for (int i = 0; i < importacaoLista.size(); i++) {
            ImportacaoContaDTO dto = (ImportacaoContaDTO) importacaoLista.get(i);
            dto.updateFields(clienteService, fornecedorService, planoContaService, contaBancariaService, centroCustoService, formaPagamentoService, tenantID);
            if (!processando) {
                parcelaAtual = dto.getNumParcela();
                qteParcelas = dto.getQteParcelas();
                processando = parcelaAtual == 1;
                if (!processando) {
                    listaErros.add(getErrorPrefix(i) + "a numeração não começa na primeira parcela.");
                } else {
                    matrixDto.add(new ArrayList<>());
                }
            }
            if (parcelaAtual != dto.getNumParcela()) {
                listaErros.add(getErrorPrefix(i) + "pulou a numeração.");
            }
            if (dto.hasMissingFields()) {
                if (dto.getDataVencimento() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Data preenchido corretamente.");
                }
                if (dto.getValor() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Valor preenchido corretamente.");
                }
                if (dto.getQteParcelas() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo quantidade de parcelas preenchido corretamente.");
                }
                if (dto.getNumParcela() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Numero de parcelas preenchido corretamente.");
                }
                if (dto.getPlanoConta() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Plano de contas preenchido corretamente, favor preencher a descrição ou o codigo do plano de contas desejado.");
                }
                if (dto.getContaBancaria() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Conta bancária preenchido corretamente.");
                }
                if (dto.getCliente() == null && dto.getFornecedor() == null) {
                    listaErros.add(getErrorPrefix(i) + "não contém o campo Cliente/Fornecedor preenchido corretamente.");
                }

            }
            if (parcelaAtual == qteParcelas) {
                processando = false;
            }
            parcelaAtual++;
            matrixDto.get(matrixDto.size() - 1).add(dto);
        }
        matrixDto.forEach(listaDto -> {
            Cliente c = listaDto.get(0).getCliente();
            Fornecedor f = listaDto.get(0).getFornecedor();
            if ((c == null && f == null) || listaDto.stream().anyMatch(dto -> (c != null && !c.equals(dto.getCliente())) || (f != null && !f.equals(dto.getFornecedor())))) {
                listaErros.add(listaDto.get(0).toErrorString());
            }
            listaDto.forEach(dto -> {
                boolean temValorPago = dto.getValorPago() != null;
                boolean temDataPagamento = dto.getDataPagamento() != null;

                String erro = "";
                if ((temValorPago && !temDataPagamento) || (temDataPagamento && !temValorPago)) {
                    erro = " o valor pago e a data de pagamento devem ser informados.";
                }
                if (dto.getFormaPagamento() == null && temValorPago) {
                    erro += " a forma de pagamento deve ser informada se houver baixa.";
                }
                if (dto.getDataEmissao() != null && dto.getDataPagamento() != null) {
                    if (dto.getDataEmissao().after(dto.getDataPagamento())) {
                        erro += " a data de pagamento deve ser posterior à data de emissão.";
                    }
                }
                if (!erro.isEmpty()) {
                    listaErros.add(dto.beginErrorString() + erro);
                }
            });
        });
        MessageListException.throwIfNotEmpty(listaErros);
        return matrixDto.stream()
                .map(listaDto -> {
                    Double valor = listaDto.stream()
                            .mapToDouble(ImportacaoContaDTO::getValor).sum();
                    Conta conta = new Conta();

                    conta.setTipoConta(EnumTipoConta.NORMAL.getTipo());
                    conta.setTipoTransacao(tipoTransacao);
                    conta.setDataVencimento(listaDto.get(0).getDataVencimento());
                    conta.setDataEmissao(listaDto.get(0).getDataEmissao());
                    conta.setValorPago(0d);
                    conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                    conta.setRepetirConta("N");
                    conta.setInformarPagamento("N");
                    conta.setAdvemContrato("N");
                    conta.setValor(valor);
                    conta.setValorTotal(valor);
                    conta.setNumeroParcelas(listaDto.size());
                    conta.setIdCliente(listaDto.get(0).getCliente());
                    conta.setIdFornecedor(listaDto.get(0).getFornecedor());
                    conta.setIdContaBancaria(listaDto.get(0).getContaBancaria());
                    conta.setIdPlanoConta(listaDto.get(0).getPlanoConta());
                    conta.setIdCentroCusto(listaDto.get(0).getCentroCusto());
                    conta.setJuros(0d);
                    conta.setContaParcelaList(listaDto.stream()
                            .map(dto -> {
                                ContaParcela cp = new ContaParcela();

                                cp.setObservacao(dto.getObservacao());
                                cp.setDataVencimento(dto.getDataVencimento());
                                cp.setDataEmissao(dto.getDataEmissao());
                                cp.setNumParcela(dto.getNumParcela());
                                cp.setValor(dto.getValor());
                                cp.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
                                cp.setValorPago(0d);
                                cp.setPagamentoParcial("N");
                                cp.setJuros(0d);
                                cp.setValorTotal(dto.getValor());
                                if (dto.getValorPago() != null) {
                                    if (dto.getDataPagamento() != null) {
                                        cp.setDataPagamento(dto.getDataPagamento());
                                    }
                                    if (dto.getValorPago() > dto.getValor()) {
                                        cp.setJuros(dto.getValorPago() - cp.getValor());
                                        cp.setValorTotal(dto.getValorPago());
                                    }
                                    cp.setValorPago(dto.getValorPago());
                                    conta.setValorPago(conta.getValorPago() + cp.getValorPago());
                                    conta.setJuros(conta.getJuros() + cp.getJuros());
                                }
                                cp.setFechada("N");
                                cp.setAdvemContrato("N");
                                cp.setIdConta(conta);
                                cp.setIdContaBancaria(dto.getContaBancaria());
                                cp.setIdCentroCusto(dto.getCentroCusto());
                                cp.setIdFormaPagamento(dto.getFormaPagamento());
                                cp.setTenatID(tenantID);

                                return cp;
                            })
                            .collect(Collectors.toList()));

                    return conta;
                })
                .map(this::calcularValorTotalConta)
                .map(this::salvar)
                .map(this::pagarParcelasImportacaoIntegralAndParcial)
                .collect(Collectors.toList());
    }

    private static String getErrorPrefix(int line) {
        return "A linha " + (line + 1) + " não pôde ser processada pois ";
    }

    public List<ContaParcela> buscarParcelasByIdClienteMovimentacao(ClienteMovimentacao cm) {
        return repositorio.listarContaParcela(repositorio.buscarContaByClienteMovimentacao(cm));
    }

    public List<ControlePagamentoItemDTO> listarParcelasPor(Fornecedor fornecedor, MinMax<Date> data) {
        return repositorio.listarParcelasPor(fornecedor, data);
    }

    public ResultadoPlanoContaLancamentoDTO obterResultado(PlanoConta aux, Date dataInicio, Date dataFim) {
        ResultadoPlanoContaLancamentoDTO dto = repositorio.obterResultado(aux, dataInicio, dataFim);
        if (dto == null) {
            dto = new ResultadoPlanoContaLancamentoDTO(1d, 0d, aux);
        }
        return dto;
    }

    public List<ContaParcela> listarParcelasPagamentoVencendoEm(Integer prazoNotificacao, String tenatID) {
        return repositorio.listarParcelasPagamentoVencendoEm(prazoNotificacao, tenatID);
    }

    public void listarTransferenciasSemPlanoContaTransferencia(Empresa empresa) {
        if (empresa.getTenatID() != null) {
            List<Conta> contas = repositorio.listaTransferencias(empresa);
            for (Conta conta : contas) {
                PlanoConta planoContaPai = conta.getIdContaBancaria().getIdPlanoConta();
                PlanoConta plano;
                if (conta.getTipoTransacao().equals("R")) {
                    plano = planoContaService.buscarPlanoContaTransferencia("C", planoContaPai);
                } else {
                    plano = planoContaService.buscarPlanoContaTransferencia("D", planoContaPai);
                }
                if (plano != null) {
                    if (!conta.getIdPlanoConta().equals(plano)) {
                        conta.setIdPlanoConta(plano);
                    }
                    if (conta.getIdContaBancaria().getIdCentroCusto() != null) {
                        conta.setIdCentroCusto(conta.getIdContaBancaria().getIdCentroCusto());
                    }
                }
            }
        }
    }

}
