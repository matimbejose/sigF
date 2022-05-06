package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PrevisaoOrcamentaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ResultadoPlanoContaLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PlanoContaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.PrevisaoOrcamentariaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoBalanco;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamento;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanoContaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private PrevisaoOrcamentariaService previsaoOrcamentariaService;

    @Inject
    private CadastroControle cadastroControle;

    private List<PlanoConta> listaVendaCompra;

    private Date dataLacamento = new Date();

    private PlanoConta planoContaSelecionado;

    private PlanoConta planoContaPaiSelecionado;

    private LazyDataModel<PlanoConta> model;

    private PlanoContaFiltro filtro = new PlanoContaFiltro();

    private String codigo;

    private List<PrevisaoOrcamentaria> lancamentos;

    private String descricao;

    private String grupoContabil;

    private ContaBancaria contaBancariaSelecionada;

    private List<ResultadoPlanoContaLancamentoDTO> listaLancamentos;

    private PlanoContaCadastroDTO dtoCadastro;

    private CentroCusto centroSelecionado;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, planoContaService::quantidadeRegistrosFiltrados, planoContaService::getListaFiltrada);
    }

    public List<PlanoConta> getPlanoContas() {
        return planoContaService.listar();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoes() {
        return planoContaService.listarPlanosContaParaTransacoes();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaReceber() {
        return planoContaService.listarPlanosContaParaTransacoesContaReceber();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesCompra() {
        return planoContaService.listarPlanosContaParaTransacoesCompra();
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaPagar() {
        return planoContaService.listarPlanosContaParaTransacoesContaPagar();
    }

    public List<PlanoConta> listarPlanosContaParaContrapartida() {
        return planoContaService.listarPlanosContaParaContrapartida(empresaService.getEmpresa(), planoContaSelecionado);
    }

    public List<PlanoConta> getPlanoContasFilho() {
        return planoContaService.listarPlanoContaOrderCodigo();
    }

    public List<PlanoConta> listarPlanosContaQuePodemTerFilhos() {
        return planoContaService.listarPlanosContaQuePodemTerFilhos(dtoCadastro.getCodigoOrigem());
    }

    public void buscaProximoCodigoLivre(PlanoConta planoConta) {
        String cod = planoContaService.obterPlanoContaByCodigoPai(planoConta);
        String aux = cod.substring(planoConta.getCodigo().length() + 1, cod.length());
        Integer aux1 = Integer.parseInt(aux);
        aux1++;
        dtoCadastro.setCodigo(aux1.toString());
    }

    public List<PrevisaoOrcamentaria> obterListaLancamentoConta() {
        return lancamentos;
    }

    public void atualizaLista() {
        lancamentos = obterPrevisaoOrcamentariaRealizado();
    }

    public List<PrevisaoOrcamentaria> obterPrevisaoOrcamentariaRealizado() {
        if (dataLacamento != null) {
            lancamentos = previsaoOrcamentariaService.obterPrevisaoOrcamentaria(dataLacamento, centroSelecionado);
        }

        if (StringUtils.isNotEmpty(grupoContabil) && !grupoContabil.equals("T")) {
            lancamentos.removeIf(pc -> !pc.getIdPlanoConta().getCodigo().substring(0, 1).equals(grupoContabil));
        }

        if (StringUtils.isNotEmpty(descricao)) {
            if (descricao.equals("!=0")) {
                lancamentos.removeIf(pc -> pc.getPrevisao() == null || pc.getPrevisao() == 0);
            } else {
                lancamentos.removeIf(pc -> !pc.getIdPlanoConta().getDescricao().toUpperCase().contains(descricao.toUpperCase()));
            }
        }

        if (StringUtils.isNotEmpty(descricao)) {
            lancamentos.removeIf(pc -> !pc.getIdPlanoConta().getDescricao().toUpperCase().contains(descricao.toUpperCase()));
        }

        return lancamentos;
    }

    public String removeLancamento() {
        try {
            planoContaService.remover(planoContaSelecionado);
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "/restrito/planoConta/listaPlanoConta.xhtml";
    }

    public String doStartAdd() {
        if (planoContaPaiSelecionado.getPodeTerFilho().equals("N")) {
            createFacesErrorMessage("Inclusão permitida apenas para os últimos níveis do plano de conta");
            return "/restrito/planoConta/listaPlanoConta.xhtml";
        }

        planoContaSelecionado = new PlanoConta();
        planoContaSelecionado.setCodigo(planoContaService.obterUltimoPlanoContaGerado(planoContaPaiSelecionado));
        planoContaSelecionado.setTipo(planoContaPaiSelecionado.getTipo());
        planoContaSelecionado.setIdContaPai(planoContaPaiSelecionado);
        planoContaSelecionado.setRegistroPadrao("N");
        planoContaSelecionado.setTipoIndicador("S");  // ultimo nivel sempre sera o simples
        planoContaSelecionado.setMostraTelaInicial("N");  // não mostra na tela inicial
        planoContaSelecionado.setTipoBalanco(planoContaPaiSelecionado.getTipoBalanco());
        planoContaSelecionado.setPodeTerFilho("N");

        codigo = org.apache.commons.lang3.StringUtils.substringAfterLast(planoContaSelecionado.getCodigo(), ".");

        return "/restrito/planoConta/cadastroPlanoConta.xhtml";
    }

    public String doStartAlterar() {
        codigo = planoContaService.getCodigoFilho(planoContaSelecionado);

        return "/restrito/planoConta/cadastroPlanoConta.xhtml";
    }

    public void addCodigo() {
        if (codigo != null) {
            String codigoFinal = StringUtil.adicionarCaracterEsquerda(codigo, "0", 3);
            planoContaSelecionado.setCodigo(planoContaSelecionado.getIdContaPai().getCodigo() + "." + codigoFinal);
        }
    }

    public String doFinishAdd() {
        try {
            planoContaService.salvar(planoContaSelecionado);
            createFacesSuccessMessage("Item do plano de conta salvo com sucesso!");
            return "/restrito/planoConta/listaPlanoConta.xhtml";
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
        }

        return "/restrito/planoConta/cadastroPlanoConta.xhtml";
    }

    public String doFinishSalvarLancamento() {
        try {
            if (lancamentos != null && !lancamentos.isEmpty()) {
                if (centroSelecionado != null) {
                    lancamentos.forEach((lancamento) -> {
                        lancamento.setIdCentroCusto(centroSelecionado);
                    });
                }
                previsaoOrcamentariaService.salvarLancamentos(lancamentos);
            }

            createFacesSuccessMessage("Lançamentos salvos com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage("Falha ao salvar os lançamentos!");
        }
        return "/restrito/planoConta/previsaoOrcamentaria.xhtml?faces-redirect=true";
    }

    public String doFinishImportarLancamento() {
        planoContaService.importarLancamentos(lancamentos, contaBancariaSelecionada);

        createFacesSuccessMessage("Importação realizada com sucesso!");
        return "/restrito/planoConta/importacaoConta.xhtml";
    }

    public String doFinishExcluir() {
        try {
            planoContaService.remover(planoContaSelecionado);

            createFacesSuccessMessage("Item do plano de conta removido com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "/restrito/planoConta/listaPlanoConta.xhtml";
    }

    public String buscarTipo(String tipo) {
        return EnumTipoPagamento.getDescricao(tipo);
    }

    public String buscarTipoBalanco(String tipo) {
        return EnumTipoBalanco.getDescricao(tipo);
    }

    public List<PlanoConta> listarPlanoContaPai() {
        return planoContaService.listarPlanoContaPai();
    }

    public List<Object> getDadosAuditoria() {
        return planoContaService.getDadosAuditoriaByID(planoContaSelecionado);
    }

    public String doShowAuditoria() {
        return "/restrito/planoConta/listaAuditoriaPlanoConta.xhtml";
    }

    public List<PlanoConta> planosContaPorEmpresa() {
        return planoContaService.listaPlanosEmpresa(empresaService.getEmpresa());
    }

    public String getCorFonte(ResultadoPlanoContaLancamentoDTO item) {
        if (item == null || item.getIdPlanoConta().getCodigo().matches("^[12](\\..*|$)")) {
            return "#000";
        }
        if (item.isReceita()) {
            if (item.getDesempenho() <= 0) {
                return "#bfbf13";
            }
            return "#5143ff";
        }
        if (item.getDesempenho() <= 0) {
            return "#bfbf13";
        }
        return "#ff4343";
    }

    public void obterPlanoContaLancamentoResultado() {
        List<ResultadoPlanoContaLancamentoDTO> list = planoContaService.obterPlanoContaLancamentoResultado(DataUtil.getPrimeiroDiaDoMes(dataLacamento), DataUtil.getUltimoDiaDoMes(dataLacamento), centroSelecionado);

        if (StringUtils.isNotEmpty(grupoContabil) && !grupoContabil.equals("T")) {
            list.removeIf(pc -> !pc.getIdPlanoConta().getCodigo().substring(0, 1).equals(grupoContabil));
        }

        if (StringUtils.isNotEmpty(descricao)) {
            if (descricao.equals("!=0")) {
                list.removeIf(pc -> (pc.getPrevisao() == null || pc.getPrevisao() == 0) && (pc.getRealizado() == null || pc.getRealizado() == 0));
            } else {
                list.removeIf(pc -> !pc.getIdPlanoConta().getDescricao().toUpperCase().contains(descricao.toUpperCase()));
            }
        }

        listaLancamentos = list;
    }

    public void atualizaValor(PrevisaoOrcamentaria previsao) {
        if (previsao.getPrevisao() == null) {
            previsao.setPrevisao(0d);
        }
    }

    public String obterNomesDosPais(PlanoConta planoConta) {
        return planoContaService.obterNomesDosPais(planoConta);
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("PLANO_CONTA_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Plano de conta",
                    planoContaService.hasAny(),
                    true,
                    null,
                    PlanoContaDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(planoConta -> planoContaService.importDto((PlanoContaCadastroDTO) planoConta, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    @Override
    public void initDtoCadastro(Object context) {
        String tipo = (String) context;
        String contaOrigem;
        ParametroSistema ps = parametroSistemaService.getParametro();
        switch (tipo) {
            case "_contaBancaria":
                contaOrigem = ps.getIdPlanoContaContaBancaria().getCodigo();
                break;
            case "_cliente":
                contaOrigem = ps.getIdPlanoContaCliente().getCodigo();
                break;
            case "_fornecedor":
                contaOrigem = ps.getIdPlanoContaFornecedor().getCodigo();
                break;
            case "_produto":
                contaOrigem = ps.getIdPlanoContaProduto().getCodigo();
                break;
            case "_servico":
                contaOrigem = ps.getIdPlanoContaServico().getCodigo();
                break;
            case "_transportadora":
                contaOrigem = ps.getIdPlanoContaTransportadora().getCodigo();
                break;
            case "venda":
                contaOrigem = ps.getIdPlanoContaVendaPadrao().getCodigo();
                break;
            case "compra":
                contaOrigem = ps.getIdPlanoContaCompraPadrao().getCodigo();
                break;
            default:
                contaOrigem = "skip";
                Logger.getLogger(getClass().getName()).log(Level.INFO, () -> "O cadastro rápido de plano de conta não foi configurado para a situação: " + tipo);
                break;
        }
        if (!"skip".equals(contaOrigem)) {
            contaOrigem = contaOrigem.replaceFirst("\\d+\\.\\d+$", "") + "%";
        }
        dtoCadastro = new PlanoContaCadastroDTO(contaOrigem);
    }

}
