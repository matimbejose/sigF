package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContratoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContratoException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContratoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
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
public class ContratoService extends BasicService<Contrato, ContratoRepositorio, ContratoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ContratoRepositorio(em, adHocTenant);
    }

    public Contrato salvarContrato(Contrato contrato, EnumTipoContrato tipoContrato) throws ContratoException {
        contrato.setTipoContrato(tipoContrato.getTipo());
        if (contrato.getIdConta() != null) {
            contrato.getIdConta().setContaParcelaList(new ArrayList<>());
        }

        if (contrato.getDataFim() == null) {
            contrato.setDataFim(contrato.getListParcelaDTO().stream()
                    .map(ParcelaDTO::getDataVencimento)
                    .max(Date::compareTo).orElseThrow(() -> new ContratoException("Informe a data de vencimento de pelo menos uma parcela.", null)));
        }

        if (contrato.getContratoClienteVeiculoList() != null) {
            contrato.getContratoClienteVeiculoList().stream()
                    .forEach(ccv -> ccv.setTenatID(adHocTenant.getTenantID()));
        }

        if (contrato.getContratoServicoList() != null) {
            contrato.getContratoServicoList().stream()
                    .forEach(cs -> cs.setTenatID(adHocTenant.getTenantID()));
        }

        if (contrato.getId() == null) {
            contrato.setIdConta(contaService.adicionarContaContratoComParcela(preencherConta(contrato, tipoContrato.getTipoTransacao().getTipo()), contrato));
        }

        return salvarContrato(contrato);
    }

    public Contrato salvarContrato(Contrato contrato) throws ContratoException {
        if (contrato.getId() == null) {
            contrato.setSituacao(contrato.getIdConta().getSituacao());
            contrato.setTenatID(adHocTenant.getTenantID());

            return adicionar(contrato);
        } else {
            alterarConta(contrato);

            return alterar(contrato);
        }
    }

    private static Conta preencherConta(Contrato contrato, String tipoTransacao) {

        //Se o contrato for do tipo ENTRADA, cria uma conta à receber.
        Conta conta = new Conta();

        conta.setIdPlanoConta(contrato.getIdPlanoConta());
        conta.setIdContaBancaria(contrato.getIdContaBancaria());
        conta.setIdCentroCusto(contrato.getIdCentroCusto());
        conta.setDataVencimento(contrato.getDataFim());
        conta.setSituacao(EnumSituacaoConta.NAO_PAGA.getSituacao());
        conta.setValor(contrato.getValorContrato());
        conta.setValorTotal(contrato.getValorContrato());
        conta.setRepetirConta("N");
        conta.setInformarPagamento("N");
        conta.setTipoTransacao(tipoTransacao);
        conta.setTenatID(contrato.getTenatID());
        conta.setNumeroParcelas(contrato.getNumeroParcelas());
        conta.setIdCliente(contrato.getIdCliente());
        conta.setIdFornecedor(contrato.getIdFornecedor());
        conta.setAdvemContrato("S");
        conta.setIdContrato(contrato);
        conta.setObservacao(contrato.getObservacao());
        conta.setDataEmissao(contrato.getDataInicio());

        if (EnumTipoTransacao.RECEBER.getTipo().equals(tipoTransacao)) {
            conta.setTipoConta(EnumTipoConta.CONTRATO_CLIENTE.getTipo());
        } else {
            conta.setTipoConta(EnumTipoConta.CONTRATO_FORNECEDOR.getTipo());
        }

        return conta;
    }

    public void alteraContratoValorParcelaMaior(Conta conta, Integer numParcelaAberto, List<ContaParcela> parcelasPagas, ContaParcela parcela) throws ContaException {
        //Busca o contrato por conta.
        Contrato contrato = buscaContratoPorConta(conta);
        Double valorParcelaAtual = parcela.getValorTotal();

        //Só executa o método se o contrato utilizar atualização automática e estiver preenchido.
        if (contrato != null && "S".equals(contrato.getUsaAtualizacaoAutomatica()) && parcelasPagas.size() == 1) {
            parcela.setValor(valorParcelaAtual);
            contaService.alterarParcelaDiretoRepositorio(parcela);

            contrato.setValorContrato(valorParcelaAtual + (valorParcelaAtual * numParcelaAberto));

            try {
                salvarContrato(contrato);
            } catch (Exception ex) {
                throw new ContaException(ex.getMessage(), null);
            }
        } else if (contrato != null && "S".equals(contrato.getUsaAtualizacaoAutomatica()) && parcelasPagas.size() > 1) {
            Double somaValorPago = 0d;
            somaValorPago = parcelasPagas.stream()
                    .map(ContaParcela::getValorPago)
                    .reduce(somaValorPago, (accumulator, item) -> accumulator + item);

            parcela.setValor(valorParcelaAtual);
            contaService.alterarParcelaDiretoRepositorio(parcela);

            contrato.setValorContrato(somaValorPago + (valorParcelaAtual * numParcelaAberto));

            try {
                salvarContrato(contrato);
            } catch (Exception ex) {
                throw new ContaException(ex.getMessage(), null);
            }
        }

    }

    public void alterarConta(Contrato contrato) throws ContratoException {
        Conta conta = contrato.getIdConta();
        conta.setIdContrato(contrato);
        conta = preencheContaAlterada(conta, contrato);

        List<ContaParcela> listParcela = contaService.listarContaParcela(conta);
        List<ContaParcela> parcelasPagas = listParcela.stream().filter(p -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());
        Double valorTotalPago = parcelasPagas.stream()
                .mapToDouble(ContaParcela::getValorPago)
                .sum();

        if ((parcelasPagas.size() == contrato.getNumeroParcelas() && (!contrato.getSituacao().equals(EnumSituacaoConta.INTERROMPIDO.getSituacao()))
                || parcelasPagas.size() > contrato.getNumeroParcelas()) && valorTotalPago < contrato.getValorContrato()) {
            throw new ContratoException("Impossível reparcelar a conta. Número de parcelas informado é menor que o número de parcelas abertas. Favor colocar o número de parcelas acima que " + parcelasPagas.size() + ".", null);
        }

        List<ContaParcela> parcelasAlteradas;
        int diferenca = conta.getNumeroParcelas() - listParcela.size();
        ContaParcela contaParcela = null;
        if (diferenca < 0) {
            int y = 1;
            List<ContaParcela> parcelasAbertas = listParcela.stream()
                    .filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao()))
                    .collect(Collectors.toList());
            for (int i = 0; i > diferenca; i--) {
                contaParcela = parcelasAbertas.get(parcelasAbertas.size() - y);
                listParcela.remove(contaParcela);
                y++;
            }
        }
        parcelasAlteradas = contaService.alterarParcela(conta, listParcela, contaParcela, contrato);
        conta.getContaParcelaList().clear();
        conta.getContaParcelaList().addAll(parcelasAlteradas);
        if (contrato.getObservacao() != null) {
            conta.setObservacao(contrato.getObservacao());
        }
        contaService.alterarConta(conta);
    }

    public Conta preencheContaAlterada(Conta conta, Contrato contrato) {
        conta.setDataVencimento(contrato.getDataVencimentoPagamento());
        conta.setIdContaBancaria(contrato.getIdContaBancaria());
        conta.setIdDocumento(contrato.getIdDocumento());
        conta.setIdPlanoConta(contrato.getIdPlanoConta());
        conta.setNumeroParcelas(contrato.getNumeroParcelas());
        conta.setValor(contrato.getValorContrato());
        conta.setValorTotal(contrato.getValorContrato());
        conta.setObservacao(contrato.getObservacao());

        if (contrato.getIdFornecedor() != null) {
            conta.setIdFornecedor(contrato.getIdFornecedor());
        }

        if (contrato.getIdCliente() != null) {
            conta.setIdCliente(contrato.getIdCliente());
        }

        return conta;
    }

    public Contrato alterarParcelaDTO(Contrato contrato) throws ContratoException {
        Conta conta = contrato.getIdConta();

        conta = preencheContaAlterada(conta, contrato);

        List<ContaParcela> listParcela = contaService.listarContaParcela(conta);
        List<ContaParcela> parcelasPagas = listParcela.stream().filter(p -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());

        //Lança a exception, informando se poderá ou não reparcelar a conta.
        if (parcelasPagas.size() == contrato.getNumeroParcelas() || parcelasPagas.size() > contrato.getNumeroParcelas()) {
            throw new ContratoException("Impossível reparcelar a conta. Número de parcelas informado é menor que o número de parcelas abertas. Favor colocar o número de parcelas acima que " + parcelasPagas.size() + ".", null);
        }

        List<ContaParcela> parcelasAlteradas;
        List<ParcelaDTO> parcelasASerAdicionada;
        int diferenca = conta.getNumeroParcelas() - listParcela.size();
        if (diferenca >= 0) {
            parcelasAlteradas = contaService.alterarParcela(conta, listParcela, null, contrato);

            parcelasASerAdicionada = montaListParcelaDTO(parcelasAlteradas);

            contrato.setListParcelaDTO(parcelasASerAdicionada);

            return contrato;

        } else {
            int y = 1;
            ContaParcela contaParcela = null;
            List<ContaParcela> parcelasAbertas = listParcela.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())).collect(Collectors.toList());
            for (int i = 0; i > diferenca; i--) {
                int ultimaPosicao = parcelasAbertas.size() - y;
                contaParcela = parcelasAbertas.get(ultimaPosicao);
                listParcela.remove(contaParcela);
                y++;
            }

            parcelasAlteradas = contaService.alterarParcela(conta, listParcela, contaParcela, contrato);

            parcelasASerAdicionada = montaListParcelaDTO(parcelasAlteradas);

            contrato.setListParcelaDTO(parcelasASerAdicionada);

            return contrato;
        }
    }

    public List<ParcelaDTO> montaListParcelaDTO(List<ContaParcela> listParcelaAberta) {

        List<ParcelaDTO> listRetorno = new LinkedList<>();
        List<ContaParcela> listParcela = new LinkedList<>();

        listParcela.addAll(listParcelaAberta);

        Collections.sort(listParcela, (p1, p2) -> p1.getDataVencimento().compareTo(p2.getDataVencimento()));

        for (ContaParcela contaParcela : listParcela) {

            ParcelaDTO parcela = new ParcelaDTO();

            parcela.setNumParcela(contaParcela.getNumParcela());
            parcela.setValor(contaParcela.getValor());

            parcela.setDataVencimento(listParcela.get(parcela.getNumParcela() - 1).getDataVencimento());

            if (contaParcela.getDesconto() != null) {
                parcela.setDesconto(contaParcela.getDesconto());
            }

            listRetorno.add(parcela);

        }

        return listRetorno;
    }

    public void alteraContratoPorConta(Contrato contrato, Conta conta) {
        if ("S".equals(conta.getAdvemContrato()) && conta.getIdContrato() != null && EnumTipoConta.TAXA_ADESAO.getTipo().equals(conta.getTipoConta())) {
            contrato.setIdDocumento(conta.getIdDocumento());
            contrato.setSituacao(conta.getSituacao());

            contrato.setTaxaAdesao(conta.getValor());

            alterar(contrato);
        } else if ("S".equals(conta.getAdvemContrato()) && conta.getIdContrato() != null && EnumTipoConta.TAXA_INSTALACAO.getTipo().equals(conta.getTipoConta())) {
            if (conta.getIdFornecedor() != null) {
                contrato.setIdFornecedor(conta.getIdFornecedor());
            }

            if (conta.getIdCliente() != null) {
                contrato.setIdCliente(conta.getIdCliente());
            }

            contrato.setIdDocumento(conta.getIdDocumento());
            contrato.setSituacao(conta.getSituacao());

            contrato.setTaxaInstalacao(conta.getValor());

            alterar(contrato);
        } else {
            if (conta.getIdFornecedor() != null) {
                contrato.setIdFornecedor(conta.getIdFornecedor());
            }

            if (conta.getIdCliente() != null) {
                contrato.setIdCliente(conta.getIdCliente());
            }

            contrato.setIdDocumento(conta.getIdDocumento());
            contrato.setDataVencimentoPagamento(conta.getDataVencimento());
            contrato.setNumeroParcelas(conta.getNumeroParcelas());
            contrato.setObservacao(conta.getObservacao());
            contrato.setSituacao(conta.getSituacao());

            Double valorContrato = 0d;
            for (ContaParcela contaParcela : conta.getContaParcelaList()) {
                if (contaParcela.getSituacao().equals(EnumSituacaoConta.PAGA.getSituacao()) || contaParcela.getSituacao().equals(EnumSituacaoConta.PAGA_PARCIALMENTE.getSituacao())) {
                    valorContrato += contaParcela.getValorPago();
                } else {
                    valorContrato += contaParcela.getValor();
                }
            }
            contrato.setValorContrato(valorContrato);

            alterar(contrato);
        }
    }

    public Contrato buscaContratoPorConta(Conta conta) {
        Contrato c = repositorio.getContratoPorConta(conta);
        if (c != null && conta != null && conta.getContaParcelaList() != null && !conta.getContaParcelaList().isEmpty()) {
            c.setListParcelaDTO(montaListParcelaDTO(conta.getContaParcelaList()));
        }
        return c;
    }

    public Contrato cancelarContrato(Contrato contrato) {
        List<ContaParcela> parcelas = contaService.parcelasEmAberto(contrato.getIdConta());

        parcelas.stream().forEach(contaService::cancelarContaParcela);

        contaService.cancelarConta(contrato.getIdConta());

        contrato.setSituacao(EnumSituacaoConta.CANCELADO.getSituacao());

        return alterar(contrato);
    }

    public boolean verificarSeExisteMovimentacaoFinanceira(Contrato contrato) {
        return verificarSeExisteMovimentacaoFinanceira(contaService.listarContaParcela(contrato.getIdConta()));
    }

    public boolean verificarSeExisteMovimentacaoFinanceira(List<ContaParcela> parcelas) {
        return parcelas.stream().anyMatch(parcela -> !EnumSituacaoConta.NAO_PAGA.getSituacao().equals(parcela.getSituacao()));
    }

    @Override
    public void remover(Contrato contrato) {
        List<ContaParcela> listParcela = contaService.listarContaParcela(contrato.getIdConta());

        PlanoContaLancamentoContabil plano = planoContaLancamentoService.buscarLancamentoContabilPorConta(contrato.getIdConta());
        if (plano != null) {
            planoContaLancamentoService.remover(plano);
        }

        // Remove primeiro todas as parcelas .
        if (!listParcela.isEmpty()) {
            listParcela.forEach(contaService::removerContaParcela);
        }

        // Remove o contrato (OBS.: deleta conta por cascade)
        super.remover(contrato);
    }

    public Double calcularValorTotal(List<ParcelaDTO> parcelasDTO) {
        // Soma valores das parcelas
        if (parcelasDTO != null && !parcelasDTO.isEmpty()) {
            return parcelasDTO.stream()
                    .filter(parcelaDTO -> parcelaDTO.getValor() != null)
                    .mapToDouble(ParcelaDTO::getValor)
                    .sum();
        }
        return 0d;
    }

    public Double calcularDescontoTotal(List<ParcelaDTO> parcelasDTO) {
        if (parcelasDTO != null && !parcelasDTO.isEmpty()) {// Soma os descontos de todas parcelas
            return parcelasDTO.stream()
                    .filter(parcelaDTO -> parcelaDTO.getDesconto() != null)
                    .mapToDouble(ParcelaDTO::getDesconto)
                    .sum();
        }
        return 0d;
    }

    public Contrato buscarAnexos(Contrato contrato) {
        return repositorio.buscarAnexos(contrato);
    }

    @Override
    public Criteria getModel(ContratoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idPlanoConta", "idPlanoConta");
        criteria.createAlias("idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idFornecedor", "idFornecedor", JoinType.LEFT_OUTER_JOIN);
        addEqRestrictionTo(criteria, "idCliente", filter.getCliente());
        addEqRestrictionTo(criteria, "idFornecedor", filter.getFornecedor());
        addEqRestrictionTo(criteria, "idPlanoConta", filter.getPlanoConta());

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion cri = Restrictions.ilike("idPlanoConta.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri2 = Restrictions.ilike("observacao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri3 = Restrictions.ilike("idCliente.nome", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri4 = Restrictions.ilike("idFornecedor.razaoSocial", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri5 = Restrictions.eq("valorContrato", filter.getValor());

            criteria.add(Restrictions.disjunction(cri, cri2, cri3, cri4, cri5));
        }

        addRestrictionTo(criteria, "dataInicio", filter.getData());
        addEqRestrictionTo(criteria, "numeroParcelas", filter.getNumParcelas());
        addEqRestrictionTo(criteria, "valorContrato", filter.getValor());
        addEqRestrictionTo(criteria, "taxaAdesao", filter.getValorAdesao());
        addEqRestrictionTo(criteria, "taxaInstalacao", filter.getValorInstalacao());
        addEqRestrictionTo(criteria, "situacao", filter.getSituacao());

        if (filter.isExpirando()) {
            criteria.add(Restrictions.isNotNull("prazoParaNotificacao"));
            criteria.add(Restrictions.isNull("dataCancelamento"));
            criteria.add(Restrictions.ne("situacao", EnumSituacaoConta.INTERROMPIDO.getSituacao()));
            criteria.add(Restrictions.sqlRestriction("DATEDIFF(DAY, GETDATE(), DATA_FIM) > 0"));
            criteria.add(Restrictions.sqlRestriction("DATEDIFF(DAY, GETDATE(), DATA_FIM) <= PRAZO_PARA_NOTIFICACAO"));
        }

        criteria.add(Restrictions.eq("tipoContrato", filter.getTipoContrato()));

        return criteria;
    }

    public List<Contrato> listarContratoCliente() {
        return repositorio.listarContratoCliente();
    }

    public List<Contrato> listarContratoClienteComAtualizacaoAutomatica() {
        return repositorio.listarContratoClienteComAtualizacaoAutomatica();
    }

    public List<Contrato> listarContratoFornecedor() {
        return repositorio.listarContratoFornecedor();
    }

    public List<Contrato> listarContratoFornecedorComAtualizacaoAutomatica() {
        return repositorio.listarContratoFornecedorComAtualizacaoAutomatica();
    }

    public List<DocumentoAnexo> buscarDocumentosPorContrato(Contrato contrato) {
        return repositorio.buscarDocumentosPorContrato(contrato);
    }

    public List<DocumentoAnexo> buscarDocumentosPorDocumento(Documento documento) {
        return repositorio.buscarDocumentosPorDocumento(documento);
    }

    public Long getQteContratosVencendo(EnumTipoContrato tipoContrato) {
        return repositorio.getQteContratosVencendo(tipoContrato);
    }

    public List<Contrato> getContratosVencendo(EnumTipoContrato tipoContrato) {
        return repositorio.getContratosVencendo(tipoContrato);
    }

    public Conta buscarContaPorContrato(int numContrato) {
        return repositorio.buscarContaPorContrato(numContrato);
    }

    public boolean possuiListaServico(Contrato contrato) {
        return repositorio.possuiListaServico(contrato);
    }

}
