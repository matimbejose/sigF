package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.IntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.IntegracaoParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntegracaoBancariaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntegracaoParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.BancoService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.IntegracaoBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoTransacaoIntegracaoBancaria;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntegracaoBancariaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private IntegracaoBancariaService integracaoBancariaService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private BancoService bancoService;

    @EJB
    private ContaService contaService;

    @Inject
    private ContaReceberControle contaReceberControle;

    @Inject
    private ContaPagarControle contaPagarControle;

    private IntegracaoBancaria integracaoBancariaSelecionada;

    private TransacaoIntegracaoBancaria transacaoIntegracaoBancariaSelecionada;

    private IntegracaoParcelaDTO integracaoParcelaDTO;

    private transient Part part;

    private LazyDataModel<IntegracaoBancaria> model;

    private List<IntegracaoParcelaDTO> listIntegracaoParcela = new LinkedList<>();

    private IntegracaoBancariaFiltro filtro = new IntegracaoBancariaFiltro();

    private IntegracaoParcelaFiltro filtroIntegracaoParcela = new IntegracaoParcelaFiltro();

    private List<ContaParcela> parcelasSelecionaveis;

    private List<ContaBancaria> contasBancarias;

    private ContaBancaria contaBancaria;

    private MinMax<Date> dataFiltro;

    private FormaPagamento formaPagamento;

    private CentroCusto centroCusto;

    private String situacaoConta = "";

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, integracaoBancariaService::quantidadeRegistrosFiltrados, integracaoBancariaService::getListaFiltrada);
    }

    public String doStartAdd() {
        integracaoBancariaSelecionada = new IntegracaoBancaria();
        integracaoBancariaSelecionada.setDataCriacao(DataUtil.getHoje());
        integracaoBancariaSelecionada.setSituacao(EnumSituacaoIntegracaoBancaria.NOVO.getSituacao());

        return "importarIntegracaoBancaria";
    }

    public String uploadArquivo() {
        try {
            String nomeArquivo = part.getSubmittedFileName();

            if (!nomeArquivo.toUpperCase().endsWith(".OFX")) {
                createFacesErrorMessage("Informe um arquivo de extensão .ofx");
                return "importarIntegracaoBancaria.xhtml";
            }

            integracaoBancariaSelecionada.setIdDocumentoAnexo(documentoService.criarDocumento(getUsuarioLogado(), "IB_" + nomeArquivo, part).getDocumentoAnexoList().get(0));

            integracaoBancariaSelecionada = integracaoBancariaService.importarArquivoIntegracao(part, integracaoBancariaSelecionada);

            createFacesSuccessMessage("Upload de arquivo realizado com sucesso!");
            return "listaIntegracaoBancaria.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "importarIntegracaoBancaria.xhtml";
        }
    }

    public String vincularConta() {
        try {
            contasBancarias = integracaoBancariaService.listarContasVinculacao(integracaoBancariaSelecionada);
            return "vincularContaBancaria.xhtml";
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaIntegracaoBancaria.xhtml";
        }
    }

    public String buscarBancoByNumero(String numero) {
        Banco banco = bancoService.buscarBancoByNumero(numero);
        if (banco == null) {
            return numero;
        }
        return banco.getNumero() + " - " + banco.getDescricao();
    }

    public void updateData() {
        parcelasSelecionaveis = null;
        getParcelasSelecionaveis();
    }

    public String doStartConciliarIntegracaoParcela() {
        try {
            listIntegracaoParcela = integracaoBancariaService.conciliarIntegracaoParcela(integracaoBancariaSelecionada, contaBancaria);
            integracaoParcelaDTO = new IntegracaoParcelaDTO();
            parcelasSelecionaveis = null;
            if (dataFiltro == null) {
                dataFiltro = new MinMax<>();
                dataFiltro.setMin(DataUtil.getPrimeiroDiaDoMes(integracaoBancariaSelecionada.getCompetencia()));
                dataFiltro.setMax(DataUtil.getUltimoDiaDoMes(integracaoBancariaSelecionada.getCompetencia()));
            }
            return "listaIntegracaoParcela.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaIntegracaoBancaria.xhtml";
        }
    }

    public List<ContaParcela> getParcelasSelecionaveis() {
        if (parcelasSelecionaveis == null) {
            ContaParcelaFiltro filter = new ContaParcelaFiltro();
            filter.setPropriedadeOrdenacao(Arrays.asList(new SortMeta(null, "dataPagamento", SortOrder.DESCENDING, null), new SortMeta(null, "dataVencimento", SortOrder.DESCENDING, null)));
            filter.setDataInicio(dataFiltro.getMin());
            filter.setDataFim(dataFiltro.getMax());
            filter.setCentroCusto(centroCusto);
            filter.setMostraTransferencia(true);
            filter.setEhConciliacao(true);
            if ("PG".equals(situacaoConta) || "NP".equals(situacaoConta)) {
                filter.setSituacao(situacaoConta);
            }
            if (integracaoParcelaDTO.getTransacaoIntegracaoBancaria() != null) {
                filter.setTipoTransacao(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor() < 0 ? "P" : "R");
            }
            parcelasSelecionaveis = contaService.getListaParcelaFiltrada(filter);
            parcelasSelecionaveis.forEach(integracaoBancariaService::updateValorConciliado);
            if (integracaoParcelaDTO.getParcelas() != null) {
                integracaoParcelaDTO.getParcelas()
                        .forEach(cp -> parcelasSelecionaveis.stream().filter(aux -> cp != null && cp.getId().equals(aux.getId())).forEach(aux -> aux.setSelecionada(true)));
            }
        }
        return parcelasSelecionaveis;
    }

    public void doCancelEscolherParcelaIntegracao() {
        vincularParcelas(new IntegracaoParcelaDTO());
        createFacesInfoMessage("Conciliação cancelada.");
    }

    public void doFinishEscolherParcelaIntegracao() {
        integracaoBancariaService.associarParcelaTransacaoBancaria(integracaoParcelaDTO, integracaoParcelaDTO.getParcelas(), formaPagamento);
        if (integracaoParcelaDTO.getPorcentagem().compareTo(1d) == 0) {
            createFacesSuccessMessage("Os valores foram conciliados com sucesso!");
        } else if (integracaoParcelaDTO.getPorcentagem().compareTo(1d) < 0) {
            Double valorTotal = Math.abs(integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor());
            Double valorRestante = integracaoParcelaDTO.getValorRestante();
            Double valorPago = valorTotal - valorRestante;

            createFacesInfoMessage(NumeroUtil.formatarCasasDecimais(valorPago / valorTotal * 100, 2, true)
                    + " do item do extrato foi conciliado, faltam " + NumeroUtil.formatarMonetario(valorRestante, 2, false));
        }
        doStartConciliarIntegracaoParcela();
        vincularParcelas(new IntegracaoParcelaDTO());
    }

    public String criarParcelaNova() {
        if (integracaoParcelaDTO.getTransacaoIntegracaoBancaria().getValor() > 0) {
            return contaReceberControle.doStartAddContaConciliacao(integracaoParcelaDTO.getTransacaoIntegracaoBancaria());
        }
        return contaPagarControle.doStartAddContaConciliacao(integracaoParcelaDTO.getTransacaoIntegracaoBancaria());
    }

    public String criarTodasParcelasNaoConciliadas() {
        try {

            integracaoBancariaService.criarTodasParcelasNaoConciliadas(listIntegracaoParcela);

            createFacesSuccessMessage("Todas as parcelas faltantes foram criadas!");

            return doStartConciliarIntegracaoParcela();
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaIntegracaoParcela.xhtml";
        }
    }

    public boolean verificarSeExisteParcelasNaoEncontradas() {

        for (IntegracaoParcelaDTO i : listIntegracaoParcela) {
            if (EnumSituacaoTransacaoIntegracaoBancaria.PARCELA_NAO_ENCONTRADA.getSituacao().equals(i.getTransacaoIntegracaoBancaria().getSituacao())) {
                return true;
            }
        }

        return false;
    }

    public StreamedContent downloadArquivo(IntegracaoBancaria integracaoBancaria) {
        try {
            return FileUtil.downloadFile(integracaoBancaria.getIdDocumentoAnexo().readFromFile(), integracaoBancaria.getIdDocumentoAnexo().getContentType(), integracaoBancaria.getIdDocumentoAnexo().getNomeArquivo());
        } catch (FileException e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

    public String doFinishExcluir() {
        try {
            integracaoBancariaService.remover(integracaoBancariaSelecionada);
            createFacesSuccessMessage("Arquivo excluído com sucesso!");
        } catch (Exception e) {
            createFacesErrorMessage("Falha ao excluir arquivo! " + e.getMessage());
        }
        return "listaIntegracaoBancaria.xhtml";
    }

    public String buscarSituacao(String situacao) {
        return EnumSituacaoTransacaoIntegracaoBancaria.getDescricaoPorSituacao(situacao);
    }

    public String buscarIconeSituacao(String situacao) {
        return EnumSituacaoTransacaoIntegracaoBancaria.retornaEnumSelecionado(situacao).getIcone();
    }

    public String doShowAuditoria() {
        return "listaAuditoriaIntegracaoBancaria.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return integracaoBancariaService.getDadosAuditoriaByID(integracaoBancariaSelecionada);
    }

    public void vincularParcelas(IntegracaoParcelaDTO dto) {
        listIntegracaoParcela.forEach(aux -> aux.setSelected(false));
        integracaoParcelaDTO = dto;
        integracaoParcelaDTO.setSelected(true);
        parcelasSelecionaveis = null;
    }

    public List<IntegracaoParcelaDTO> getListIntagracaoParcelaDTOFiltrada() {
        List<IntegracaoParcelaDTO> list = listIntegracaoParcela;

        if (StringUtils.isNotEmpty(filtroIntegracaoParcela.getDescricao())) {
            list.removeIf(pc -> !pc.getTransacaoIntegracaoBancaria().getDescricao().toUpperCase().contains(filtroIntegracaoParcela.getDescricao().toUpperCase()));
        }

        return list;
    }

    public boolean isDisabled() {
        return integracaoParcelaDTO.getTransacaoIntegracaoBancaria() == null;
    }

    public String vinculaContaCriada(Conta contaCriada) {
        integracaoParcelaDTO.getParcelas().addAll(contaCriada.getContaParcelaList());
        return "/restrito/integracaoBancaria/listaIntegracaoParcela.xhtml?faces-redirect=true";
    }

}
