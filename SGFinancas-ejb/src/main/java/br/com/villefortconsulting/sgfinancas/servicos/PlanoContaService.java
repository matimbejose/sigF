package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PrevisaoOrcamentaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CompraDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDreDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResultadoPlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaCadastroRestDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ContaMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PlanoContaRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoBalanco;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoTransacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumCodigoReceitaDespesa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PlanoContaService extends BasicService<PlanoConta, PlanoContaRepositorio, PlanoContaFiltro> {

    private static final long serialVersionUID = 1L;

    public static final String VENDA_MERCADO_INTERNO = "Venda de Produtos no Mercado Interno";

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    ContaMapper contaMapper;

    @EJB
    private ContaService contaService;

    @EJB
    private PlanoContaPadraoService planoContaPadraoService;

    @EJB
    private PrevisaoOrcamentariaService previsaoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private PrevisaoOrcamentariaService previsaoOrcamentariaService;

    @EJB
    private ContabilidadePlanoContaService contabilidadePlanoContaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PlanoContaRepositorio(em, adHocTenant);
    }

    @Override
    public PlanoConta salvar(PlanoConta planoConta) {
        if (existeCodigoPlanoConta(planoConta)) {
            throw new CadastroException("Existe um plano de conta com o código informado, digite outro código!", null);
        }

        if (planoConta.getId() == null) {
            return adicionar(planoConta);
        } else {
            return alterar(planoConta);
        }
    }

    public void importarLancamentos(List<PrevisaoOrcamentaria> lancamentos, ContaBancaria contaBancaria) {
        PrevisaoOrcamentaria previsao = null;
        Conta conta = null;
        Conta contaImportada = null;

        for (PrevisaoOrcamentaria previsaoLista : lancamentos) {

            if (previsaoLista.getPrevisao() != null && previsaoLista.getPrevisao() != 0) {

                previsao = previsaoService.salvar(previsaoLista);

                // se for uma conta de resultado, cria o conta a pagar/receber
                if (previsao.getIdPlanoConta().getTipoBalanco().equals(EnumTipoBalanco.RESULTADO.getTipo())) {

                    // tenta recuperar conta.
                    conta = new Conta();
                    conta.setTipoConta(EnumTipoConta.LANCADA.getTipo());
                    conta.setIdPlanoConta(previsao.getIdPlanoConta());
                    conta.setIdContaBancaria(contaBancaria);
                    conta.setDataPagamento(previsao.getData());
                    conta.setDataVencimento(previsao.getData());
                    conta.setInformarPagamento("S");
                    conta.setNumeroParcelas(1);
                    conta.setRepetirConta("N");
                    if (previsao.getIdPlanoConta().getTipo().equals("C")) {
                        conta.setTipoTransacao(EnumTipoTransacao.RECEBER.getTipo());
                    } else {
                        conta.setTipoTransacao(EnumTipoTransacao.PAGAR.getTipo());
                    }
                    conta.setValor(previsao.getPrevisao());
                    conta.setValorTotal(previsao.getPrevisao());
                    conta.setValorPago(previsao.getPrevisao());

                    contaImportada = contaService.obterContaImportada(conta);

                    if (contaImportada != null) {
                        // altera o valor da conta
                        contaImportada.setValor(conta.getValor());
                        contaImportada.setValorTotal(conta.getValorTotal());
                        contaImportada.setValorPago(conta.getValorPago());
                        contaService.alterarConta(contaImportada);

                    } else {
                        // não achou o lançamento
                        contaService.adicionarContaEParcela(conta);
                    }
                }
            }
        }
    }

    @Override
    public void remover(PlanoConta planoConta) {
        try {
            if ("S".equals(planoConta.getRegistroPadrao()) && "N".equals(planoConta.getTipoIndicador())) {
                throw new CadastroException("Somente novos itens e itens não utilizados podem ser excluídos.", null);
            }

            if (repositorio.planoContaUtilizado(planoConta)) {
                throw new CadastroException("Já existem transações associadas a este plano de contas, exclusão não foi possível.", null);
            }

            super.remover(planoConta);
        } catch (Exception ex) {
            throw new CadastroException("Já existem transações associadas a este plano de contas, exclusão não foi possível.", null);
        }
    }

    public List<PlanoConta> listarPlanosContaParaTransacoes() {
        return repositorio.listarPlanosContaParaTransacoes();
    }

    public List<PlanoConta> listarPlanosContaParaContrapartida(Empresa empresa, PlanoConta planoConta) {
        return repositorio.listarPlanosContaParaContrapartida(empresa, planoConta);
    }

    public List<PlanoConta> listarPlanosContaTipoCredito() {
        return repositorio.listarPlanosContaTipoCredito();
    }

    public List<PlanoConta> listarPlanosContaTipoDebito() {
        return repositorio.listarPlanosContaTipoDebito();
    }

    @Override
    public Criteria getModel(PlanoContaFiltro filter) {
        Criteria criteria = super.getModel(filter);

        if (filter.getSelecionaveis() != null) {
            addEqRestrictionTo(criteria, "tipoIndicador", filter.getSelecionaveis());
        }

        if (StringUtils.isNotEmpty(filter.getDescricao())) {
            Criterion c1 = Restrictions.ilike("descricao", filter.getDescricao(), MatchMode.ANYWHERE);
            Criterion c2 = Restrictions.ilike("codigo", filter.getDescricao(), MatchMode.START);

            criteria.add(Restrictions.or(c1, c2));
        }

        if (StringUtils.isNotEmpty(filter.getCodigo())) {
            addIlikeRestrictionTo(criteria, "codigo", filter.getCodigo(), MatchMode.START);
        }
        addEqRestrictionTo(criteria, "tipo", filter.getTipo());
        addEqRestrictionTo(criteria, "tipoBalanco", filter.getTipoBalanco());
        addEqRestrictionTo(criteria, "registroPadrao", filter.getPadrao());
        addEqRestrictionTo(criteria, "mostraTelaInicial", filter.getMostraNaTelaInicial());

        if (StringUtils.isNotEmpty(filter.getGrupoContabil()) && !filter.getGrupoContabil().equals("T")) {
            switch (filter.getGrupoContabil()) {
                case "A":
                    filter.setGrupoContabil("1");
                    break;
                case "P":
                    filter.setGrupoContabil("2");
                    break;
                case "D":
                    filter.setGrupoContabil(EnumCodigoReceitaDespesa.CODIGO_DESPESA.getTipo());
                    break;
                case "R":
                    filter.setGrupoContabil(EnumCodigoReceitaDespesa.CODIGO_RECEITA.getTipo());
                    break;
                default:
                    break;
            }
            criteria.add(Restrictions.ilike("codigo", filter.getGrupoContabil(), MatchMode.START));
        }

        if (filter.getCodigos() != null && !filter.getCodigos().getValue().isEmpty()) {
            Criterion[] listOr = new Criterion[filter.getCodigos().getValue().size()];
            for (int i = 0; i < filter.getCodigos().getValue().size(); i++) {
                listOr[i] = Restrictions.ilike("codigo", filter.getCodigos().getValue().get(i), MatchMode.START);
            }
            criteria.add(Restrictions.or(listOr));
        }

        return criteria;
    }

    public boolean existeCodigoPlanoConta(PlanoConta planoConta) {
        if (planoConta.getId() == null) {
            return repositorio.existeCodigoPlanoConta(planoConta);
        } else {
            return repositorio.existeCodigoOutroPlanoConta(planoConta);
        }
    }

    public List<PlanoConta> listarPlanoConta(PlanoConta planoConta) {
        return repositorio.listarPlanoConta(planoConta);
    }

    public List<PlanoContaDreDTO> listarPlanoContaDreDto(String codigo) {
        return repositorio.listarPlanoContaDreDto(codigo);
    }

    public List<PlanoConta> listarPlanoContaPai() {
        return repositorio.listarPlanoContaPai();
    }

    public List<PlanoConta> listarPlanoContaOrderCodigo() {
        return repositorio.listarPlanoContaOrderCodigo();
    }

    public List<ResultadoPlanoContaLancamentoDTO> obterPlanoContaLancamentoResultado(Date dataInicio, Date dataFim, CentroCusto centro) {
        List<PlanoContaLancamentoDTO> lancamentosPrevisao = contaService.listarValorPrevisao(dataInicio, dataFim, centro);

        lancamentosPrevisao.forEach(dto -> {
            dto.setPrevisao(dto.getValor());
            dto.setValor(null);
        });

        contaService.listarValorRealizado(dataInicio, dataFim, centro).forEach(dto -> lancamentosPrevisao.stream()
                .filter(p -> p.getIdPlano().equals(dto.getIdPlano()))
                .forEach(g -> g.setRealizado(dto.getValor())));

        List<ResultadoPlanoContaLancamentoDTO> listaLancamento = completaLancamentoDeAnalise(lancamentosPrevisao);

        listaLancamento = atualizaValorPrevistoAnaliseOrcamentaria(listaLancamento, dataInicio, dataFim, centro);

        Collections.sort(listaLancamento, (p1, p2) -> p1.getIdPlanoConta().getCodigo().compareTo(p2.getIdPlanoConta().getCodigo()));

        for (int i = listaLancamento.size() - 1; i >= 0; i--) {
            ResultadoPlanoContaLancamentoDTO dto = listaLancamento.get(i);
            String codigo = "^" + dto.getIdPlanoConta().getCodigo().replace(".", "\\.") + "\\.\\d+";
            listaLancamento.stream()
                    .filter(d -> d.getIdPlanoConta().getCodigo().matches(codigo))
                    .forEach(dto::add);
        }

        return listaLancamento;
    }

    public List<ResultadoPlanoContaLancamentoDTO> atualizaValorPrevistoAnaliseOrcamentaria(List<ResultadoPlanoContaLancamentoDTO> listLancamento, Date dataInicio, Date dataFim, CentroCusto centro) {
        List<PrevisaoOrcamentaria> listaPrevisao = previsaoOrcamentariaService.listarPorPeriodo(dataInicio, centro);

        listaPrevisao.forEach(previsaoOrcamentaria -> listLancamento.stream()
                .filter(p -> p.getIdPlanoConta().equals(previsaoOrcamentaria.getIdPlanoConta()))
                .forEach(g -> g.setPrevisao(previsaoOrcamentaria.getPrevisao())));

        return listLancamento;
    }

    public List<ResultadoPlanoContaLancamentoDTO> completaLancamentoDeAnalise(List<PlanoContaLancamentoDTO> listPlano) {
        List<PlanoConta> listPlanoContaFilho = listarPlanoContaOrderCodigo();
        PlanoContaLancamentoDTO planoContaLancamentoDTO;
        List<PlanoContaLancamentoDTO> listTratada = new LinkedList<>();
        List<PlanoContaLancamentoDTO> listaAtualizada = new ArrayList<>();
        List<ResultadoPlanoContaLancamentoDTO> listaRetorno = new ArrayList<>();

        for (int i = 0; i < listPlano.size(); i++) {
            planoContaLancamentoDTO = new PlanoContaLancamentoDTO();

            planoContaLancamentoDTO.setPrevisao(listPlano.get(i).getPrevisao());
            planoContaLancamentoDTO.setRealizado(listPlano.get(i).getRealizado());
            planoContaLancamentoDTO.setIdPlanoConta(buscar(listPlano.get(i).getIdPlano()));

            listTratada.add(planoContaLancamentoDTO);
        }

        for (PlanoConta planoConta : listPlanoContaFilho) {
            planoContaLancamentoDTO = listTratada.stream().filter(p -> p.getIdPlanoConta().equals(planoConta)).findFirst().orElse(null);

            // verifica se ja existe o lançamento
            if (planoContaLancamentoDTO != null) {
                listaAtualizada.add(planoContaLancamentoDTO);
            } else {
                // precisa criar o lançamento
                planoContaLancamentoDTO = new PlanoContaLancamentoDTO();
                planoContaLancamentoDTO.setIdPlanoConta(planoConta);
                listaAtualizada.add(planoContaLancamentoDTO);
            }
        }

        listaAtualizada.stream()
                .map(planoContaLancamento -> {
                    ResultadoPlanoContaLancamentoDTO resultado = new ResultadoPlanoContaLancamentoDTO(planoContaLancamento);
                    resultado.setRealizado(planoContaLancamento.getRealizado());
                    resultado.setPrevisao(planoContaLancamento.getPrevisao());
                    return resultado;
                })
                .forEachOrdered(listaRetorno::add);

        return listaRetorno;
    }

    public List<PlanoContaLancamentoDTO> obtendoDadosAnaliseOrcamentaria(Date data) {
        return repositorio.obterValorAnaliseOrcamentaria(data);
    }

    // busca todos os indicadores que deverão ser mostrados na tela inicial
    public List<ResultadoPlanoContaLancamentoDTO> obterIndicadorTelaInicial(Date dataInicio, Date dataFim) {
        return repositorio.listarPlanoContaTelaInicial().stream()
                .map(aux -> contaService.obterResultado(aux, dataInicio, dataFim))
                .collect(Collectors.toList());
    }

    public String getCodigoFilho(PlanoConta planoConta) {
        String[] codigosSeparados = planoConta.getCodigo().split("\\.");

        if (codigosSeparados.length != 0) {
            return codigosSeparados[codigosSeparados.length - 1];
        }
        return null;
    }

    public void salvarPlanoContaModeloPadrao(Empresa empresa) {
        planoContaPadraoService.aplicarPlanoContaAEmpresa(empresa.getTenatID());
    }

    public List<PlanoConta> obterPlanoContaDre() {

        ArrayList<String> codigos = obterCodigosDRE();
        return repositorio.obterPlanoContaPorCodigo(codigos);
    }

    public String obterCodigosDREString() {
        ArrayList<String> codigos = obterCodigosDRE();

        StringBuilder todosCodigo = new StringBuilder();
        codigos.forEach(codigo -> todosCodigo.append(todosCodigo).append("'").append(codigo).append("',"));
        todosCodigo.append(todosCodigo.substring(0, todosCodigo.length() - 1));

        return todosCodigo.toString();
    }

    private static ArrayList<String> obterCodigosDRE() {

        ArrayList<String> codigos = new ArrayList<>();

        // receitas
        codigos.add("4.1.1");
        codigos.add("4.1.3");
        codigos.add("4.1.2");
        codigos.add("4.1.4");
        codigos.add("4.1.5");

        // despesas
        codigos.add("3.1.1");
        codigos.add("3.1.2");
        codigos.add("3.1.3");
        codigos.add("3.1.4");
        codigos.add("3.2.1");
        codigos.add("3.2.2");

        // Custo de mercadoria e produto vendido
        codigos.add("5.1.1");
        codigos.add("5.1.2");
        codigos.add("5.1.3");

        // Receitas e depesas nao operacionais
        codigos.add("4.2");
        codigos.add("3.3");

        return codigos;
    }

    public List<PlanoConta> listarPlanoContaPorEmpresa() {
        return repositorio.listarPlanosContaParaTransacoes();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaReceber() {
        return repositorio.listarPlanosContaParaTransacoesContaReceber();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaPagar() {
        return repositorio.listarPlanosContaParaTransacoesContaPagar();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesCompra() {
        return repositorio.listarPlanosContaParaTransacoesCompra();
    }

    public List<PlanoConta> listaPlanosEmpresa(Empresa empresa) {
        return repositorio.listarPlanosContaPorEmpresa(empresa);
    }

    public List<PlanoConta> listarPlanosContaQuePodemTerFilhos(String codigo) {
        return repositorio.listarPlanosContaQuePodemTerFilhos(codigo);
    }

    public List<PlanoConta> listarPlanosContaQuePodemTerFilhos() {
        return repositorio.listarPlanosContaQuePodemTerFilhos();
    }

    public String obterNomesDosPais(PlanoConta planoConta) {

        String retorno = planoConta.getDescricao();

        if (planoConta.getIdContaPai() != null) {
            retorno = obterNomesDosPais(planoConta.getIdContaPai()) + " / " + retorno;
        }
        return retorno;
    }

    public PlanoConta criaPlanoContaCadastroBasico(String descricao, String tipo) {

        PlanoConta planoConta = new PlanoConta();
        planoConta.setDescricao(descricao.length() < 200 ? descricao : descricao.substring(0, 200));
        planoConta.setPodeTerFilho("S");
        planoConta.setRegistroPadrao("S");
        planoConta.setTipoIndicador("S");

        PlanoConta planoContaCadastro = null;
        ParametroSistema ps = parametroSistemaService.getParametro();
        if (ps != null) {
            if (EnumTipoCadastro.FORNECEDOR.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaFornecedor();
            } else if (EnumTipoCadastro.SERVICO.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaServico();
            } else if (EnumTipoCadastro.CLIENTE.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaCliente();
            } else if (EnumTipoCadastro.CONTA_BANCARIA.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaContaBancaria();
            } else if (EnumTipoCadastro.TRANSPORTADORA.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaTransportadora();
            } else if (EnumTipoCadastro.PRODUTO.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaProduto();
            } else if (EnumTipoCadastro.FUNCIONARIO.getTipo().equals(tipo)) {
                planoContaCadastro = ps.getIdPlanoContaFuncionario();
            }
        }

        if (planoContaCadastro != null) {
            planoConta.setCodigoPai(planoContaCadastro.getCodigo());
            planoConta.setIdContaPai(planoContaCadastro);
            planoConta.setMostraTelaInicial("N");
            planoConta.setTipo(planoContaCadastro.getTipo());
            planoConta.setTipoBalanco(planoContaCadastro.getTipoBalanco());
            planoConta.setCodigo(obterUltimoPlanoContaGerado(planoContaCadastro));

            return salvar(planoConta);
        }
        throw new CadastroException("Favor preencher os parâmetros do sistema para concluír o cadastro.", null);
    }

    public PlanoConta buscarPlanoContaTransferencia(String tipo, PlanoConta planoConta) {
        return repositorio.buscarPlanoContaTransferencia(tipo, planoConta);
    }

    public void criaPlanoContaTransferenciaCreditoDebito(PlanoConta planoPai) {
        if (planoPai == null) {
            throw new CadastroException("Não foi encontrado o plano de contas da conta bancária cadastrada.", null);
        }
        PlanoConta planoConta = new PlanoConta();
        planoConta.setDescricao("Transferência realizada");
        planoConta.setPodeTerFilho("N");
        planoConta.setRegistroPadrao("S");
        planoConta.setTipoIndicador("S");

        planoConta.setCodigoPai(planoPai.getCodigo());
        planoConta.setIdContaPai(planoPai);
        planoConta.setMostraTelaInicial("N");
        planoConta.setTipo("D");
        planoConta.setTipoBalanco(planoPai.getTipoBalanco());
        planoConta.setCodigo(planoPai.getCodigo() + ".001");

        salvar(planoConta);

        PlanoConta planoContaCredito = new PlanoConta();
        planoContaCredito.setDescricao("Transferência recebida");
        planoContaCredito.setPodeTerFilho("N");
        planoContaCredito.setRegistroPadrao("S");
        planoContaCredito.setTipoIndicador("S");

        planoContaCredito.setCodigoPai(planoPai.getCodigo());
        planoContaCredito.setIdContaPai(planoPai);
        planoContaCredito.setMostraTelaInicial("N");
        planoContaCredito.setTipo("C");
        planoContaCredito.setTipoBalanco(planoPai.getTipoBalanco());
        planoContaCredito.setCodigo(planoPai.getCodigo() + ".002");

        salvar(planoContaCredito);
    }

    public boolean verificaExistenciaPlanoContaTransferenciaECria(PlanoConta planoConta) {
        return repositorio.listarFilhosByPlanoContaPai(planoConta).stream()
                .filter(filho -> filho.getDescricao().equals("Transferência recebida") || filho.getDescricao().equals("Transferência realizada"))
                .count() == 2;
    }

    public String obterUltimoPlanoContaGerado(PlanoConta planoConta) {

        PlanoConta planoContaGerado = repositorio.obterUltimoPlanoContaGerado(planoConta);

        if (planoContaGerado != null) {

            String ultCodigo = StringUtils.substringAfterLast(planoContaGerado.getCodigo(), ".");
            int codigo = Integer.parseInt(ultCodigo);
            int quantidadeZeros = 0;

            for (int i = 0; i < ultCodigo.length(); i++) {
                char charAt = ultCodigo.charAt(i);
                if ('0' == charAt) {
                    quantidadeZeros++;
                } else {
                    break;
                }
            }

            // proximo numero
            String codigoRetorno = String.valueOf(codigo + 1);

            // trata a quantidade de zeros a esquerda
            if (codigoRetorno.length() > String.valueOf(codigo).length()) {
                codigoRetorno = StringUtils.repeat("0", quantidadeZeros - 1) + codigoRetorno;
            } else {
                codigoRetorno = StringUtils.repeat("0", quantidadeZeros) + codigoRetorno;
            }

            // remontando o codigo original
            return planoContaGerado.getCodigo().substring(0, StringUtils.substringBeforeLast(planoContaGerado.getCodigo(), ".").length() + 1) + codigoRetorno;

        } else {
            int codigo = Integer.parseInt(StringUtils.substringAfterLast(planoConta.getCodigo(), "."));

            codigo++;  // proximo numero
            String codigoRetorno = codigo + "";

            // trata a quantidade de zeros a esquerda
            codigoRetorno = StringUtils.repeat("0", 3 - codigoRetorno.length()) + codigoRetorno;

            // remontando o codigo original
            return planoConta.getCodigo() + "." + codigoRetorno;
        }

    }

    public PlanoConta obterPlanoContaPorCodigo(String codigo, String tenatID) {
        return repositorio.obterPlanoContaPorCodigo(codigo, tenatID);
    }

    public PlanoConta obterPlanoContaPorDescricao(String codigo, String tenatID) {
        return repositorio.obterPlanoContaPorDescricao(codigo, tenatID);
    }

    public PlanoConta getPlanoContaPadrao(CompraDTO obj) {
        return getPlanoContaPadrao(obj != null ? obj.getPlanoConta() : null);
    }

    public PlanoConta getPlanoContaPadrao(VendaCadastroRestDTO obj) {
        return getPlanoContaPadrao(obj != null ? obj.getPlanoConta() : null);
    }

    public PlanoConta getPlanoContaPadrao(PlanoContaDTO obj) {
        return getPlanoContaPadrao(obj != null ? obj.getId() : null);
    }

    public PlanoConta getPlanoContaPadrao(Integer id) {
        ParametroSistema ps = parametroSistemaService.getParametro();
        PlanoConta pc = null;
        if (id == null) {
            pc = ps.getIdPlanoContaVendaPadrao();
        } else {
            try {
                pc = buscar(id);
            } catch (Exception ex) {
                Logger.getLogger(ServicosWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (pc == null) {
            pc = obterPlanoContaPorDescricao(VENDA_MERCADO_INTERNO, adHocTenant.getTenantID());
        }
        return pc;
    }

    public PlanoConta importDto(PlanoContaCadastroDTO planoContaDTO, String tenat) {
        PlanoConta pc = contaMapper.toEntity(planoContaDTO);
        pc.setTenatID(tenat);
        pc.setCodigo(pc.getIdContaPai().getCodigo() + "." + pc.getCodigo());
        return salvar(pc);
    }

    public String obterPlanoContaByCodigoPai(PlanoConta planoConta) {
        return repositorio.buscarUltimoCodigoPlanoContaPai(planoConta);

    }

    public PlanoConta buscarPlanoDeContasByCodigo(String codigo) {
        return repositorio.buscarPlanoContaByCodigo(codigo);
    }

}
