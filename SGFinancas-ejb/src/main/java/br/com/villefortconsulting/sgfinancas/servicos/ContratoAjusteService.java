package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoAjuste;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoParcelaAjuste;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContratoAjusteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ContratoAjusteRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.util.DataUtil;
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
public class ContratoAjusteService extends BasicService<ContratoAjuste, ContratoAjusteRepositorio, ContratoAjusteFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ContaService contaService;

    @EJB
    private ContratoService contratoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ContratoAjusteRepositorio(em, adHocTenant);
    }

    public ContratoAjuste salvar(ContratoAjuste contrato, List<ContaParcelaDTO> listParcela) {
        if (contrato.getId() == null) {
            contrato.setTenatID(adHocTenant.getTenantID());
            contrato.setData(DataUtil.getHoje());
            contrato.setNumParcelas(listParcela.size());

            adicionar(contrato);

            preencheAdicionaContratoParcelaAjuste(contrato, listParcela);

            return contrato;
        }
        return alterar(contrato);
    }

    public void preencheAdicionaContratoParcelaAjuste(ContratoAjuste contrato, List<ContaParcelaDTO> list) {

        //Pega a conta que tem no contrato.
        Conta conta = contrato.getIdContrato().getIdConta();

        List<ContaParcela> listParcelas = contaService.listarContaParcela(conta);

        //Filtra a lista, pegando somenta as que não estão pagas e que a data de vencimento seja maior que hoje.
        List<ContaParcela> parcelasAnteriores = listParcelas.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())
                && p.getDataVencimento().compareTo(DataUtil.getHoje()) > 0).collect(Collectors.toList());

        int i = 0;
        Double valorOriginal = 0d;
        Double valorAtualizado = 0d;

        for (ContaParcelaDTO contaParcelaDTO : list) {
            //Preenche contratoParcela
            ContratoParcelaAjuste contratoParcela = new ContratoParcelaAjuste();

            ContaParcela parcela = parcelasAnteriores.get(i);

            contratoParcela.setIdContratoAjuste(contrato);
            contratoParcela.setIdContaParcela(parcela);
            contratoParcela.setValorAtualizado(contaParcelaDTO.getValorCorrigido());
            contratoParcela.setValorOriginal(parcela.getValor());
            contratoParcela.setTenatID(adHocTenant.getTenantID());

            //Preenche os valores
            valorOriginal += contratoParcela.getValorOriginal();
            valorAtualizado += contratoParcela.getValorAtualizado();

            //Altera a contaParcela e adiciona o contratoParcela
            contaService.alterarContaParcela(preencheContaParcela(contaParcelaDTO, parcela));
            repositorio.adicionarContratoParcelaAjuste(contratoParcela);
            i++;
        }

        //altera o contrato de acordo com os valores
        alteraContrato(contrato.getIdContrato(), valorOriginal, valorAtualizado);
    }

    public ContaParcela preencheContaParcela(ContaParcelaDTO parcelaDTO, ContaParcela parcela) {

        parcela.setValor(parcelaDTO.getValorCorrigido());
        parcela.setValorTotal(parcelaDTO.getValorTotal());

        return parcela;
    }

    public void alteraContrato(Contrato contrato, Double valorOriginal, Double valorAtualizado) {
        Double valorContrato = (contrato.getValorContrato() - valorOriginal) + valorAtualizado;

        contrato.setValorContrato(valorContrato);

        contratoService.alterar(contrato);
    }

    public List<ContaParcelaDTO> listaComTaxaCalculada(ContratoAjuste contratoAjuste) {
        List<ContaParcelaDTO> listRetorno = new LinkedList<>();
        if (contratoAjuste.getIdContrato() != null) {
            return listRetorno;
        }
        //Pega a conta que tem no contrato.
        Conta conta = contratoAjuste.getIdContrato().getIdConta();

        List<ContaParcela> listParcelas = contaService.listarContaParcela(conta);

        //Filtra a lista, pegando somenta as que não estão pagas e que a data de vencimento seja maior que hoje.
        List<ContaParcela> parcelasPodemSerAlteradas = listParcelas.stream().filter(p -> EnumSituacaoConta.NAO_PAGA.getSituacao().equals(p.getSituacao())
                && p.getDataVencimento().compareTo(DataUtil.getHoje()) > 0).collect(Collectors.toList());

        //Se o contratoAjuste já tiver com a taxa preenchida, ele faz todos os devidos cálculos.
        if (contratoAjuste.getTaxa() == null) {
            parcelasPodemSerAlteradas.stream()
                    .map(parcela -> {
                        ContaParcelaDTO parcelaDTO = preencheParcelaDTO(parcela);
                        parcelaDTO.setValorOriginal(parcela.getValor());
                        parcelaDTO.setValorCorrigido(0d);
                        parcelaDTO.setValorTotal(parcela.getValorTotal());
                        return parcelaDTO;
                    })
                    .forEachOrdered(listRetorno::add);

            return listRetorno;
        }
        return parcelasPodemSerAlteradas.stream()
                .map(parcela -> {
                    ContaParcelaDTO parcelaDTO = preencheParcelaDTO(parcela);
                    Double valorCorrigido = parcela.getValor() + ((parcela.getValor() / 100) * contratoAjuste.getTaxa());
                    Double valorTotal = valorCorrigido;

                    if (parcela.getJuros() != null) {
                        valorTotal += parcela.getJuros();
                    }
                    if (parcela.getDesconto() != null) {
                        valorTotal -= parcela.getDesconto();
                    }
                    if (parcela.getEncargos() != null) {
                        valorTotal += parcela.getEncargos();
                    }
                    if (parcela.getOutrosCustos() != null) {
                        valorTotal += parcela.getOutrosCustos();
                    }
                    if (parcela.getMulta() != null) {
                        valorTotal += parcela.getMulta();
                    }
                    if (parcela.getValorIR() != null) {
                        valorTotal += parcela.getValorIR();
                    }
                    if (parcela.getValorPIS() != null) {
                        valorTotal += parcela.getValorPIS();
                    }
                    if (parcela.getValorCSLL() != null) {
                        valorTotal += parcela.getValorCSLL();
                    }
                    if (parcela.getValorINSS() != null) {
                        valorTotal += parcela.getValorINSS();
                    }
                    if (parcela.getValorCOFINS() != null) {
                        valorTotal += parcela.getValorCOFINS();
                    }
                    if (parcela.getValorISS() != null) {
                        valorTotal += parcela.getValorISS();
                    }

                    parcelaDTO.setValorOriginal(parcela.getValor());
                    parcelaDTO.setValorCorrigido(valorCorrigido);
                    parcelaDTO.setValorTotal(valorTotal);
                    return parcelaDTO;
                })
                .collect(Collectors.toList());
    }

    public ContaParcelaDTO preencheParcelaDTO(ContaParcela parcela) {

        ContaParcelaDTO parcelaDTO = new ContaParcelaDTO();

        parcelaDTO.setDataVencimento(parcela.getDataVencimento());
        parcelaDTO.setPlanoConta(parcela.getIdConta().getIdPlanoConta().getDescricao());

        if (parcela.getIdConta().getIdCliente() != null) {
            parcelaDTO.setCliente(parcela.getIdConta().getIdCliente().getNome());
        } else {
            parcelaDTO.setFornecedor(parcela.getIdConta().getIdFornecedor().getRazaoSocial());
        }

        if (parcela.getObservacao() != null) {
            parcelaDTO.setObservacao(parcela.getObservacao());
        } else {
            parcelaDTO.setObservacao(" ");
        }

        return parcelaDTO;
    }

    public int quantidadeRegistrosFiltradosEntrada(ContratoAjusteFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getModel(filtro));
    }

    public List<ContratoAjuste> getListaFiltradaEntrada(ContratoAjusteFiltro filtro) {
        return repositorio.getListaFiltrada(getModel(filtro), filtro);
    }

    @Override
    public Criteria getModel(ContratoAjusteFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.createAlias("idContrato.idPlanoConta", "idPlanoConta");
        criteria.createAlias("idContrato", "idContrato");
        criteria.createAlias("idContrato.idCliente", "idCliente", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idContrato.idFornecedor", "idFornecedor", JoinType.LEFT_OUTER_JOIN);

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion cri = Restrictions.ilike("idPlanoConta.descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri2 = Restrictions.ilike("observacao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri3 = Restrictions.ilike("idCliente.nome", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion cri4 = Restrictions.ilike("idFornecedor.razaoSocial", filter.getDescricao(), MatchMode.ANYWHERE);

            criteria.add(Restrictions.disjunction(cri, cri2, cri3, cri4));
        }

        if (StringUtils.isNotEmpty(filter.getDataInicio())) {
            Date dataInicio = DataUtil.converterStringParaDate(filter.getDataInicio());
            criteria.add(Restrictions.ge("data", dataInicio));
        }

        if (StringUtils.isNotEmpty(filter.getDataFim())) {
            Date dataFim = DataUtil.converterStringParaDate(filter.getDataFim());
            criteria.add(Restrictions.le("data", dataFim));
        }

        criteria.add(Restrictions.eq("tipoContrato", filter.getTipoContrato()));

        return criteria;
    }

    public int quantidadeRegistrosFiltradosSaida(ContratoAjusteFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getModel(filtro));
    }

    public List<ContratoAjuste> getListaFiltradaSaida(ContratoAjusteFiltro filtro) {
        return repositorio.getListaFiltrada(getModel(filtro), filtro);
    }

}
