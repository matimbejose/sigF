package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicReport;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.RelatorioException;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.ExtratoContaCorrente;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.ItensOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.OrdemDeServico;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaLancamentoContabil;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.Turma;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaFormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AcessoPorEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnaliseContaSinteticaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CardContaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioAnaliticoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteSaldoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaPagarReceberDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControleFinanceiroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ControlePagamentoItemDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.DreDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EstoqueDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FluxoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FluxoCaixaRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.LancamentoCaixaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.LancamentoContabilDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.MediaPrazoRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NFSeRelatorioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrcamentoClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.OrcamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PontoEntradaSaidaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ReciboDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoPorClienteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicosMaisVendidosDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitanteDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SolicitantePanaromaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ValorLancamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.VendaPorVendedorDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFeMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AnaliseContaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ExtratoContaCorrenteFiltro;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemSolicitante;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOS;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitante;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoExtrato;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoListagemConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoVenda;
import br.com.villefortconsulting.sgfinancas.util.TreeNodeItem;
import br.com.villefortconsulting.sgfinancas.util.TreeNodeList;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.operator.In;
import br.com.villefortconsulting.util.operator.MinMax;
import br.com.villefortconsulting.util.report.Cell;
import br.com.villefortconsulting.util.report.CellFormat;
import br.com.villefortconsulting.util.report.Page;
import br.com.villefortconsulting.util.report.Row;
import br.com.villefortconsulting.util.report.Section;
import br.com.villefortconsulting.util.report.Style;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.jrimum.utilix.Objects;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.model.UploadedFile;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatorioService extends BasicReport implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Constantes">
    public static final Style HEADER_STYLE_CENTER = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.CENTER, CellFormat.TEXT);

    public static final Style HEADER_STYLE_LEFT = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);

    public static final Style HEADER_STYLE_RIGHT = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);

    public static final Style ZERO_NUMBER = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.NUMBER);

    public static final Style POSITIVE_NUMBER = new Style(Color.BLUE, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.NUMBER);

    public static final Style NEGATIVE_NUMBER = new Style(Color.RED, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.NUMBER);

    public static final Style ZERO_MONEY = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);

    public static final Style POSITIVE_MONEY = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);

    public static final Style NEGATIVE_MONEY = new Style(Color.RED, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);

    public static final Style ZERO_PERCENT = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.PERCENT);

    public static final Style POSITIVE_PERCENT = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.PERCENT);

    public static final Style NEGATIVE_PERCENT = new Style(Color.RED, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.PERCENT);

    public static final Style DATE = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.DATE);
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="EJBs">
    @Inject
    transient NFeMapper nfeMapper;

    @EJB
    private AnalisesService analisesService;

    @EJB
    private SolicitanteService solicitanteService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ContaService contaService;

    @EJB
    private PontoService pontoService;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    private PlanoContaLancamentoService planoContaLancamentoService;

    @EJB
    private VendaService vendaService;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    private ContratoService contratoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private FinanceiroService financeiroService;

    @EJB
    private OrdemDeServicoService ordemDeServicoService;

    @EJB
    private ExtratoContaCorrenteService extratoContaCorrenteService;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Util">
    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    private transient EntityManager em;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    private static Map<String, Object> createParams(Empresa empresa) {
        Map<String, Object> params = new HashMap<>();
        if (empresa == null) {
            return params;
        }
        params.put("nomeQuebra", "");
        params.put("nomeEmpresa", empresa.getNomeFantasia());
        params.put("cidade", empresa.getEndereco().getIdCidade().getDescricao());
        params.put("enderecoLinha1", empresa.getEndereco().getEndereco() + ", Nº " + empresa.getEndereco().getNumero() + " - " + empresa.getEndereco().getBairro());
        params.put("enderecoLinha2", empresa.getEndereco().getIdCidade().getDescricao() + " - " + empresa.getEndereco().getIdCidade().getIdUF().getDescricao() + " - " + empresa.getEndereco().getCep());
        if (empresa.getLogo() != null) {
            InputStream image = new ByteArrayInputStream(empresa.getLogo());
            params.put("logo", image);
        }
        return params;
    }

    private static Map<String, Object> createParams(Empresa empresa, Date dataInicial, Date dataFinal) {
        Map<String, Object> params = createParams(empresa);
        params.put("data", dataFinal);
        params.put("periodo", DataUtil.dateToString(dataInicial) + " até " + DataUtil.dateToString(dataFinal));
        return params;
    }

    private Map<String, Object> createParams() {
        return createParams(empresaService.getEmpresa());
    }

    private static Style getCorValor(Double val, boolean percent) {
        if (percent) {
            if (val == null || val == 0) {
                return ZERO_PERCENT;
            } else if (val < 0) {
                return NEGATIVE_PERCENT;
            }
            return POSITIVE_PERCENT;
        }
        if (val == null || val == 0) {
            return ZERO_MONEY;
        } else if (val < 0) {
            return NEGATIVE_MONEY;
        }
        return POSITIVE_MONEY;
    }

    private static <T> List<T> validaTamanho(List<T> lista) throws RelatorioException {
        return validaTamanho(lista, "Não há dados para gerar o relatório. Reveja os parâmetros e tente novamente.");
    }

    private static <T> List<T> validaTamanho(List<T> lista, String msg) throws RelatorioException {
        if (lista == null || lista.isEmpty()) {
            throw new RelatorioException(msg, null);
        }
        return lista;
    }

    @Override
    public Class getCurrentClass() {
        return getClass();
    }
    //</editor-fold>

    public byte[] gerarRelatorioFluxoCaixa(Integer ano) throws JRException, RelatorioException {
        Date dataInicial = DataUtil.converterStringParaDate("01/01/" + ano);
        Date dataFinal = DataUtil.converterStringParaDate("31/12/" + ano);

        return gerarRelatorioFluxoCaixa(analisesService.obterFluxoCaixa(dataInicial, dataFinal, true, null), new ArrayList<>(), ano);
    }

    public byte[] gerarRelatorioFluxoCaixaExcel(TreeNode fluxoCaixa, Integer ano, CentroCusto centroCusto) throws IOException {
        Row meses = new Row(new Cell());
        DataUtil.getMesesCompletos().forEach((k, v) -> meses.addCell(new Cell(v)));
        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell("Fluxo de caixa", HEADER_STYLE_CENTER, 14)),
                new Row(new Cell("Competência"), new Cell(ano.toString(), 13))
        );

        header.getRows().add(new Row(new Cell("Centro de custo"), new Cell(centroCusto == null ? "Todos os centros de custo" : centroCusto.getDescricao(), 13)));
        header.getRows().add(meses.addCell(new Cell("Total")));

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .fixHeader(true)
                .header(header)
                .body(fluxoCaixaToRows(new Section(), fluxoCaixa, " "))
                .build());
    }

    private static Section fluxoCaixaToRows(Section body, TreeNode node, String spacer) {
        Row linha = new Row();
        if (node.getData() != null) {
            boolean first = true;
            for (TreeNodeItem item : ((TreeNodeList) node.getData()).getItems()) {
                if (first) {
                    linha.addCell(new Cell(spacer + item.getValue()));
                    first = false;
                    continue;
                }
                String value = item.getValue().replace("%", "");
                if ("-".equals(value)) {
                    linha.addCell(new Cell());
                } else {
                    Double val = NumeroUtil.converterStringParaDouble(value);
                    if (val == null) {
                        val = 0d;
                    }
                    if (item.getValue().contains("%")) {
                        val /= 100;
                    }
                    linha.addCell(new Cell(val, getCorValor(val, item.getValue().contains("%"))));
                }
            }
        }
        body.getRows().add(linha);
        if (node.getChildCount() > 0) {
            node.getChildren().forEach(child -> fluxoCaixaToRows(body, child, spacer + " "));
        }
        return body;
    }

    public byte[] gerarRelatorioFluxoCaixa(List<FluxoCaixaDTO> listDadosFluxoCaixa, List<ContaPagarReceberDTO> listContaPagarReceber, Integer ano) throws JRException, RelatorioException {
        List<FluxoCaixaRelatorioDTO> fluxoCaixaRelatorioDTOs = converterRelatorioFluxoCaixaDTO(listDadosFluxoCaixa);

        if (listContaPagarReceber != null) {
            fluxoCaixaRelatorioDTOs.addAll(converterFluxoCaixaRelatorioDTO(listContaPagarReceber));
        }

        validaTamanho(fluxoCaixaRelatorioDTOs);

        Map<String, Object> params = createParams();
        params.put("data", ano);
        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(fluxoCaixaRelatorioDTOs);

        return gerarArrayBytePDF(params, "ANÁLISE DE FLUXO DE CAIXA (" + ano + ")", "fluxoCaixa.jrxml", beanDataSource);
    }

    private static List<FluxoCaixaRelatorioDTO> converterRelatorioFluxoCaixaDTO(List<FluxoCaixaDTO> listDadosFluxoCaixa) {
        List<FluxoCaixaRelatorioDTO> fluxoCaixaRelatorioDTOs = new ArrayList<>();
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Saldo inicial (R$)", "S", "F"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Receita (R$)", "R", "F"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Despesa (R$)", "D", "F"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Lucro/Prejuízo (R$)", "LP", "F"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Acumulado (R$)", "A", "F"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Lucratividade ", "L", "F"));

        for (int i = 0; i < 13; i++) {
            FluxoCaixaDTO fluxoCaixaDTO = listDadosFluxoCaixa.get(i);

            Integer referencialTotal = 13;
            int mes = !fluxoCaixaDTO.getMes().equals("tot") ? Integer.parseInt(fluxoCaixaDTO.getMes()) : referencialTotal;

            ValorLancamentoDTO lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getSaldoInicial(), mes);
            fluxoCaixaRelatorioDTOs.get(0).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getReceita(), mes);
            fluxoCaixaRelatorioDTOs.get(1).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getDespesa(), mes);
            fluxoCaixaRelatorioDTOs.get(2).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getLucroPrejuizo(), mes);
            fluxoCaixaRelatorioDTOs.get(3).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getAcumulado(), mes);
            fluxoCaixaRelatorioDTOs.get(4).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(fluxoCaixaDTO.getLucratividade(), mes);
            fluxoCaixaRelatorioDTOs.get(5).addValorLancamento(lancamentoDTO);
        }
        return fluxoCaixaRelatorioDTOs;
    }

    private static List<FluxoCaixaRelatorioDTO> converterFluxoCaixaRelatorioDTO(List<ContaPagarReceberDTO> listDadosContaReceber) {
        List<FluxoCaixaRelatorioDTO> fluxoCaixaRelatorioDTOs = new ArrayList<>();
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("A receber (R$)", "R", "C"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("A pagar (R$)", "R", "C"));
        fluxoCaixaRelatorioDTOs.add(new FluxoCaixaRelatorioDTO("Necessidade de caixa (R$)", "D", "C"));

        for (int i = 0; i < 13; i++) {
            ContaPagarReceberDTO contaPagarReceberDTO = listDadosContaReceber.get(i);

            Integer referencialTotal = 13;
            int mes = !contaPagarReceberDTO.getMes().equals("tot") ? Integer.parseInt(contaPagarReceberDTO.getMes()) : referencialTotal;

            ValorLancamentoDTO lancamentoDTO = new ValorLancamentoDTO(contaPagarReceberDTO.getValorReceber(), mes);
            fluxoCaixaRelatorioDTOs.get(0).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(contaPagarReceberDTO.getValorPagar(), mes);
            fluxoCaixaRelatorioDTOs.get(1).addValorLancamento(lancamentoDTO);

            lancamentoDTO = new ValorLancamentoDTO(contaPagarReceberDTO.getNecessidadeCaixa(), mes);
            fluxoCaixaRelatorioDTOs.get(2).addValorLancamento(lancamentoDTO);

        }
        return fluxoCaixaRelatorioDTOs;
    }

    public byte[] gerarRelatorioLancamentoContabil(Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<PlanoContaLancamentoContabil> listPlanoContaLancamento = validaTamanho(planoContaLancamentoService.buscarLancamentoAcordoEmpresaLogada(empresa, dataInicio, dataFinal));

        List<LancamentoContabilDTO> lancamentoContabilDTOs = listPlanoContaLancamento.stream().map(lancamentoContabil -> {
            LancamentoContabilDTO lancamento = new LancamentoContabilDTO();
            lancamento.setDataLancamento(lancamentoContabil.getData());
            if (lancamentoContabil.getIdPlanoContaCredito() != null) {
                lancamento.setContaCredito(lancamentoContabil.getIdPlanoContaCredito().getDescricao());
            }
            if (lancamentoContabil.getIdPlanoContaDebito() != null) {
                lancamento.setContaDebito(lancamentoContabil.getIdPlanoContaDebito().getDescricao());
            }
            lancamento.setValor(lancamentoContabil.getValor());
            if ("C".equals(lancamentoContabil.getSituacao())) {
                lancamento.setSituacao("Cancelada");
            } else {
                lancamento.setSituacao("Ativa");
            }
            return lancamento;
        }).collect(Collectors.toList());

        Map<String, Object> params = createParams();
        params.put("data", dataFinal);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(lancamentoContabilDTOs);
        return gerarArrayBytePDF(params, "LANÇAMENTO CONTÁBIL (" + empresa.getDescricao() + ")", "lancamentoContabil.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioProdutosPorCliente(Cliente cliente, Empresa empresa, Date dataInicio, Date dataFinal) throws RelatorioException, JRException {
        List<VendaPorVendedorDTO> dados = validaTamanho(vendaService.obterVendaPorCliente(empresa, cliente, dataInicio, dataFinal));

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(createParams(empresa, dataFinal, dataInicio), "PRODUTOS POR CLIENTE", "vendaPorVendedor.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioServicoCliente(Cliente cliente, Empresa empresa, Date dataInicio, Date dataFinal) throws RelatorioException, JRException {
        List<ServicoPorClienteDTO> dados = validaTamanho(vendaService.obterServicoPorCliente(empresa, cliente, dataInicio, dataFinal));

        Map<String, Object> params = createParams();
        params.put("data", dataFinal);
        params.put("titulo", "SERVIÇO POR CLIENTE");

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "SERVIÇO POR CLIENTE", "servicoPorCliente.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioLancamentoCaixa(Empresa empresa, PlanoConta planoConta, Date dataInicio, Date dataFinal, ContaBancaria contaBancaria, CentroCusto centroCusto) throws RelatorioException, JRException {
        List<LancamentoCaixaDTO> dados = validaTamanho(contaService.obterLancamentoNoCaixa(empresa, planoConta, dataInicio, dataFinal, contaBancaria, centroCusto));

        Map<String, Object> params = createParams(empresa);
        params.put("data", dataFinal);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(createParams(), "LANÇAMENTO NO CAIXA", "lancamentoCaixa.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioProdutosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFinal) throws RelatorioException, JRException {
        List<ProdutosMaisVendidosDTO> dados = validaTamanho(vendaService.listarProdutosMaisVendidos(empresa, dataInicio, dataFinal));

        Map<String, Object> params = createParams(empresa);
        params.put("data", DataUtil.dateToString(dataFinal, "dd/MM/yyyy"));
        params.put("dataInicio", DataUtil.dateToString(dataInicio, "dd/MM/yyyy"));

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "PRODUTOS VENDIDOS PELA EMPRESA (" + empresa.getDescricao() + ")", "produtosMaisVendidos.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioServicosMaisVendidos(Empresa empresa, Date dataInicio, Date dataFinal) throws RelatorioException, JRException {
        List<ServicosMaisVendidosDTO> dados = validaTamanho(vendaService.listarServicosMaisVendidos(empresa, dataInicio, dataFinal));

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(createParams(empresa), "SERVIÇOS PRESTADOS PELA EMPRESA (" + empresa.getDescricao() + ")", "servicosMaisVendidos .jrxml", beanDataSource);

    }

    public byte[] gerarRelatorioVendasVendedor(Usuario usuario, Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<VendaPorVendedorDTO> dados = validaTamanho(vendaService.obterVendaPorVendedor(empresa, usuario, dataInicio, dataFinal));

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(createParams(empresa, dataInicio, dataFinal), "PRODUTOS POR VENDEDOR", "vendaPorVendedor.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioPonto(Funcionario funcionario, Empresa empresa, Date data) throws RelatorioException, JRException {
        List<PontoEntradaSaidaDTO> dados = validaTamanho(pontoService.listarRegistroEntradaSaidaFuncionario(funcionario, data));

        String mes = pontoService.retornaMes(DataUtil.getMes(data));
        String ano = String.valueOf(DataUtil.getAno(data));
        String periodo = mes.toUpperCase() + " / " + ano;

        if (StringUtils.isEmpty(periodo)) {
            throw new RelatorioException("Erro ao informar a data recorrente do relatório informado.", null);
        }

        Map<String, Object> params = createParams(empresa);
        params.put("data", data);
        params.put("periodo", periodo);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados, false);
        return gerarArrayBytePDF(params, "RELATÓRIO DE PONTO FUNCIONÁRIOS", "relatorioPonto.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioAnaliseRecebimento(Empresa empresa, AnaliseContaFiltro filtro) throws JRException, RelatorioException {
        List<AnaliseContaDTO> dados = validaTamanho(contaService.listarDadosAnaliseRecebimentoAnalitico(filtro));

        dados.stream().forEach(p -> p.setSituacao(EnumSituacaoConta.retornaDescricaoPorSituacao(p.getSituacao())));

        Map<String, Object> params = createParams(empresa);
        params.put("dataFim", filtro.getData().getMax());
        params.put("dataInicio", filtro.getData().getMin());
        if (filtro.getCentroCusto() == null) {
            params.put("centro", "Todos os centros de custo");
        } else {
            params.put("centro", filtro.getCentroCusto().getDescricao());
        }
        if (filtro.getContaBancaria() == null) {
            params.put("contaBancaria", "Todos as contas bancárias");
        } else {
            params.put("contaBancaria", filtro.getContaBancaria().getDescricao());
        }
        if (filtro.getCliente() == null) {
            params.put("filtroCliente", "Todos os clientes");
        } else {
            params.put("filtroCliente", filtro.getCliente().getNome());
        }
        if (filtro.getPlanoConta() == null) {
            params.put("filtroPlanoConta", "Todos os planos de contas");
        } else {
            params.put("filtroPlanoConta", filtro.getPlanoConta().getDescricao());
        }
        if (filtro.getSituacaoConta().equals("T")) {
            params.put("filtroSituacao", "Todos as situações");
        } else if (filtro.getSituacaoConta().equals("PG")) {
            params.put("filtroSituacao", "Pagos");
        } else if (filtro.getSituacaoConta().equals("PP")) {
            params.put("filtroSituacao", "Pagas parcialmente e totalmente");
        } else if (filtro.getSituacaoConta().equals("NP")) {
            params.put("filtroSituacao", "Não pagas");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "ANÁLISE DE RECEBIMENTO ANALÍTICO", "analiseRecebimento.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioAnaliseRecebimentoSintetico(Empresa empresa, AnaliseContaFiltro filtro) throws JRException, RelatorioException {
        List<AnaliseContaSinteticaDTO> dados = validaTamanho(contaService.listarDadosAnaliseRecebimentoSintetico(filtro));

        ContaParcelaFiltro filtroParcelas = new ContaParcelaFiltro();
        filtroParcelas.setDataInicio(filtro.getData().getMin());
        filtroParcelas.setTipoTransacao("R");
        filtroParcelas.setDataFim(filtro.getData().getMax());
        filtroParcelas.setCentroCusto(filtro.getCentroCusto());
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.ATRASO.name());
        CardContaDTO emAtraso = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.ATRASO);
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.RECEBIDO.name());
        CardContaDTO recebido = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.RECEBIDO);
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.RECEBER.name());
        CardContaDTO aReceber = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.RECEBER);

        Map<String, Object> params = createParams(empresa);
        params.put("quantidadeEmAtraso", emAtraso.getQuantidade());
        params.put("quantidadeRecebido", recebido.getQuantidade());
        params.put("quantidadeAReceber", aReceber.getQuantidade());
        params.put("valorEmAtraso", emAtraso.getValor());
        params.put("valorRecebidoTotal", recebido.getValor());
        params.put("valorAReceber", aReceber.getValor());
        params.put("dataFim", filtro.getData().getMax());
        params.put("dataInicio", filtro.getData().getMin());
        if (filtro.getCentroCusto() == null) {
            params.put("centro", "Todos os centros de custo");
        } else {
            params.put("centro", filtro.getCentroCusto().getDescricao());
        }
        if (filtro.getContaBancaria() == null) {
            params.put("contaBancaria", "Todos as contas bancárias");
        } else {
            params.put("contaBancaria", filtro.getContaBancaria().getDescricao());
        }
        if (filtro.getCliente() == null) {
            params.put("filtroCliente", "Todos os clientes");
        } else {
            params.put("filtroCliente", filtro.getCliente().getNome());
        }
        if (filtro.getPlanoConta() == null) {
            params.put("filtroPlanoConta", "Todos os planos de contas");
        } else {
            params.put("filtroPlanoConta", filtro.getPlanoConta().getDescricao());
        }
        if (filtro.getSituacaoConta().equals("T")) {
            params.put("filtroSituacao", "Todos as situações");
        } else if (filtro.getSituacaoConta().equals("PG")) {
            params.put("filtroSituacao", "Pagos");
        } else if (filtro.getSituacaoConta().equals("PP")) {
            params.put("filtroSituacao", "Pagas parcialmente e totalmente");
        } else if (filtro.getSituacaoConta().equals("NP")) {
            params.put("filtroSituacao", "Não pagas");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "ANÁLISE DE RECEBIMENTO SINTÉTICO", "analiseRecebimentoSintetico.jrxml", beanDataSource);
    }

    public byte[] gerarExcelRelatorioAnaliseRecebimento(AnaliseContaFiltro filtro) throws IOException {
        return getExcelAnalisePagamentoRecebimento(false, filtro);
    }

    private byte[] getExcelAnalisePagamentoRecebimento(boolean isPagamento, AnaliseContaFiltro filtro) throws IOException {
        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        Style normal = new Style(Color.BLACK, 10, "Arial", false, Color.lightGray, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        Style normalBranco = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.CENTER, CellFormat.TEXT);
        Style data = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.DATE);
        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell("Análise de " + (isPagamento ? "pagamento" : "recebimento"), HEADER_STYLE_CENTER, 11)),
                new Row(new Cell("Período de apuração"), new Cell(DataUtil.dateToString(filtro.getData().getMin()) + " até " + DataUtil.dateToString(filtro.getData().getMax()), normal, 10)),
                new Row(new Cell("Plano de contas"), new Cell(filtro.getPlanoConta() == null ? "Todos os plano de contas" : filtro.getPlanoConta().getDescricao(), normal, 10)),
                new Row(new Cell(isPagamento ? "Fornecedor" : "Cliente"), new Cell(isPagamento ? filtro.getFornecedor() == null ? "Todos os fornecedores" : filtro.getFornecedor().getRazaoSocial() : filtro.getCliente() == null ? "Todos os clientes" : filtro.getCliente().getNome(), normal, 10)),
                new Row(new Cell("Centro de custo"), new Cell(filtro.getCentroCusto() == null ? "Todos os centros de custo" : filtro.getCentroCusto().getDescricao(), normal, 10)),
                new Row(new Cell("Conta bancaria"), new Cell(filtro.getContaBancaria() == null ? "Todos as contas bancárias" : filtro.getContaBancaria().getDescricao(), normal, 10)),
                new Row(new Cell("Situação da Conta"), new Cell(filtro.getSituacaoConta().equals("T") ? "Todos as situações" : filtro.getSituacaoConta().equals("PG") ? "Pagas" : filtro.getSituacaoConta().equals("PP") ? "Pagas parcialmente ou totalmente" : "Não pagas", normal, 10)),
                new Row(
                        new Cell("Nº documento"),
                        new Cell(isPagamento ? "Fornecedor" : "Cliente"),
                        new Cell("Plano de contas"),
                        new Cell("Observação"),
                        new Cell("Valor"),
                        new Cell("Juros"),
                        new Cell("Valor " + (isPagamento ? "pago" : "recebido")),
                        new Cell("Emissão"),
                        new Cell("Vencimento"),
                        new Cell(isPagamento ? "Pagamento" : "Recebimento"),
                        new Cell("Situação")
                ));

        Section body = new Section();
        Double valorTotal = 0d;
        Double valorPagoTotal = 0d;
        Integer quantContas = 0;
        Integer quantContasPagas = 0;
        Integer quantContasEmAtraso = 0;
        Integer quantContasAVencer = 0;
        Double contasAVencer = 0d;
        Double contasEmAtraso = 0d;

        List<AnaliseContaDTO> lista;
        if (isPagamento) {
            lista = contaService.listarDadosAnalisePagamentoAnalitico(filtro);
            lista.addAll(contaService.listarDadosAnaliseRecebimentoAnalitico(filtro).stream()
                    .peek(item -> {
                        item.setValor(-item.getValor());
                        item.setValorPago(-item.getValorPago());
                    })
                    .collect(Collectors.toList()));
        } else {
            lista = contaService.listarDadosAnaliseRecebimentoAnalitico(filtro);
            lista.addAll(contaService.listarDadosAnalisePagamentoAnalitico(filtro).stream()
                    .peek(item -> {
                        item.setValor(-item.getValor());
                        item.setValorPago(-item.getValorPago());
                    })
                    .collect(Collectors.toList()));
        }
        for (AnaliseContaDTO dado : lista) {
            body.getRows().add(new Row(
                    new Cell(dado.getNumDocumento()),
                    new Cell(isPagamento ? dado.getFornecedor() : dado.getCliente()),
                    new Cell(dado.getDescricao()),
                    new Cell(dado.getObsParcela()),
                    new Cell(dado.getValor(), valor),
                    new Cell(dado.getValorJuros(), valor),
                    new Cell(dado.getValorPago(), valor),
                    new Cell(dado.getDataEmissao(), data),
                    new Cell(dado.getDataVencimento(), data),
                    new Cell(dado.getDataPagamento(), data),
                    new Cell(EnumSituacaoConta.retornaDescricaoPorSituacao(dado.getSituacao()))
            ));
            quantContas++;
            if (EnumSituacaoConta.retornaDescricaoPorSituacao(dado.getSituacao()).equals("Pago")
                    || EnumSituacaoConta.retornaDescricaoPorSituacao(dado.getSituacao()).equals("Pago parcialmente")) {
                quantContasPagas++;
            } else if (dado.isAVencer()) {
                quantContasAVencer++;
                contasAVencer += dado.getValor();
            } else if (dado.isEmAtraso()) {
                quantContasEmAtraso++;
                contasEmAtraso += dado.getValor();
            }
            valorTotal = NumeroUtil.somar(valorTotal, dado.getValor());
            valorPagoTotal = NumeroUtil.somar(valorPagoTotal, dado.getValorPago());
        }
        body.getRows().add(new Row());

        Section footer = new Section(HEADER_STYLE_CENTER, new Row(new Cell("Resumo"), new Cell("Quantidade"), new Cell("Valor")),
                new Row(new Cell("Total de Contas", HEADER_STYLE_LEFT), new Cell(quantContas, normalBranco), new Cell(valorTotal, valor)),
                new Row(new Cell(isPagamento ? "Total contas pagas" : "Total contas recebidas", HEADER_STYLE_LEFT), new Cell(quantContasPagas, normalBranco), new Cell(valorPagoTotal, valor)),
                new Row(new Cell("Total conta a vencer", HEADER_STYLE_LEFT), new Cell(quantContasAVencer, normalBranco), new Cell(contasAVencer, valor)),
                new Row(new Cell("Total conta em atraso", HEADER_STYLE_LEFT), new Cell(quantContasEmAtraso, normalBranco), new Cell(contasEmAtraso, valor))
        );
        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .fixHeader(true)
                .header(header)
                .body(body)
                .footer(footer)
                .build());
    }

    public byte[] gerarRelatorioAnalisePagamento(Empresa empresa, AnaliseContaFiltro filtro) throws JRException, RelatorioException {
        List<AnaliseContaDTO> dados = validaTamanho(contaService.listarDadosAnalisePagamentoAnalitico(filtro));

        dados.stream().forEach(p -> p.setSituacao(EnumSituacaoConta.retornaDescricaoPorSituacao(p.getSituacao())));

        Map<String, Object> params = createParams(empresa);
        params.put("dataFim", filtro.getData().getMax());
        params.put("dataInicio", filtro.getData().getMin());
        if (filtro.getCentroCusto() == null) {
            params.put("centro", "Todos os centros de custo");
        } else {
            params.put("centro", filtro.getCentroCusto().getDescricao());
        }
        if (filtro.getContaBancaria() == null) {
            params.put("contaBancaria", "Todos as contas bancárias");
        } else {
            params.put("contaBancaria", filtro.getContaBancaria().getDescricao());
        }
        if (filtro.getFornecedor() == null) {
            params.put("filtroFornecedor", "Todos os fornecedores");
        } else {
            params.put("filtroFornecedor", filtro.getFornecedor().getRazaoSocial());
        }
        if (filtro.getPlanoConta() == null) {
            params.put("filtroPlanoConta", "Todos os planos de contas");
        } else {
            params.put("filtroPlanoConta", filtro.getPlanoConta().getDescricao());
        }
        if (filtro.getSituacaoConta().equals("T")) {
            params.put("filtroSituacao", "Todos as situações");
        } else if (filtro.getSituacaoConta().equals("PG")) {
            params.put("filtroSituacao", "Pagos");
        } else if (filtro.getSituacaoConta().equals("PP")) {
            params.put("filtroSituacao", "Pagas parcialmente e totalmente");
        } else if (filtro.getSituacaoConta().equals("NP")) {
            params.put("filtroSituacao", "Não pagas");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "ANÁLISE DE PAGAMENTO ANALÍTICO", "analisePagamento.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioAnalisePagamentoSintetico(Empresa empresa, AnaliseContaFiltro filtro) throws JRException, RelatorioException {
        List<AnaliseContaSinteticaDTO> dados = validaTamanho(contaService.listarDadosAnalisePagamentoSintetico(filtro));

        ContaParcelaFiltro filtroParcelas = new ContaParcelaFiltro();
        filtroParcelas.setDataInicio(filtro.getData().getMin());
        filtroParcelas.setTipoTransacao("P");
        filtroParcelas.setDataFim(filtro.getData().getMax());
        filtroParcelas.setCentroCusto(filtro.getCentroCusto());
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.ATRASO.name());
        CardContaDTO emAtraso = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.ATRASO);
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.PAGO.name());
        CardContaDTO pago = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.PAGO);
        filtroParcelas.setTipoListagem(EnumTipoListagemConta.PAGAR.name());
        CardContaDTO aPagar = contaService.buscarEstatisticasConta(filtroParcelas, EnumTipoListagemConta.PAGAR);

        Map<String, Object> params = createParams(empresa);
        params.put("quantidadeEmAtraso", emAtraso.getQuantidade());
        params.put("quantidadePago", pago.getQuantidade());
        params.put("quantidadeAPagar", aPagar.getQuantidade());
        params.put("valorEmAtraso", emAtraso.getValor());
        params.put("valorPagoTotal", pago.getValor());
        params.put("valorAPagar", aPagar.getValor());
        params.put("dataFim", filtro.getData().getMax());
        params.put("dataInicio", filtro.getData().getMin());
        if (filtro.getCentroCusto() == null) {
            params.put("centro", "Todos os centros de custo");
        } else {
            params.put("centro", filtro.getCentroCusto().getDescricao());
        }
        if (filtro.getContaBancaria() == null) {
            params.put("contaBancaria", "Todos as contas bancárias");
        } else {
            params.put("contaBancaria", filtro.getContaBancaria().getDescricao());
        }
        if (filtro.getFornecedor() == null) {
            params.put("filtroFornecedor", "Todos os fornecedores");
        } else {
            params.put("filtroFornecedor", filtro.getFornecedor().getRazaoSocial());
        }
        if (filtro.getPlanoConta() == null) {
            params.put("filtroPlanoConta", "Todos os planos de contas");
        } else {
            params.put("filtroPlanoConta", filtro.getPlanoConta().getDescricao());
        }
        if (filtro.getSituacaoConta().equals("T")) {
            params.put("filtroSituacao", "Todos as situações");
        } else if (filtro.getSituacaoConta().equals("PG")) {
            params.put("filtroSituacao", "Pagos");
        } else if (filtro.getSituacaoConta().equals("PP")) {
            params.put("filtroSituacao", "Pagas parcialmente e totalmente");
        } else if (filtro.getSituacaoConta().equals("NP")) {
            params.put("filtroSituacao", "Não pagas");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dados);
        return gerarArrayBytePDF(params, "ANÁLISE DE PAGAMENTO SINTÉTICO", "analisePagamentoSintetico.jrxml", beanDataSource);
    }

    public byte[] gerarExcelRelatorioAnalisePagamento(AnaliseContaFiltro filtro) throws IOException {
        return getExcelAnalisePagamentoRecebimento(true, filtro);
    }

    public String[][] criarArquivoExcelAnalisePagamento(List<AnaliseContaDTO> dados, AnaliseContaFiltro filtro) {
        String[][] texto = new String[dados.size() + 1][11];
        texto[0][0] = "NUMERO DOCUMENTO";
        texto[0][1] = "FORNECEDOR";
        texto[0][2] = "PLANO DE CONTAS";
        texto[0][3] = "OBSERVAÇÃO";
        texto[0][4] = "VALOR";
        texto[0][5] = "VALOR PAGO";
        texto[0][6] = "VENCIMENTO";
        texto[0][7] = "PAGAMENTO";
        texto[0][8] = "SITUAÇÃO";
        texto[0][9] = "DATA INICIO APURAÇÃO";
        texto[0][10] = "DATA FIM APURAÇÃO";

        for (int i = 1; i <= dados.size(); i++) {
            AnaliseContaDTO dadosPagamento = dados.get(i - 1);

            texto[i][0] = "";
            texto[i][1] = "";
            texto[i][2] = dadosPagamento.getDescricao();
            texto[i][3] = "";
            texto[i][4] = NumeroUtil.formatarMonetario(dadosPagamento.getValor(), 2, false);
            texto[i][5] = "";
            texto[i][6] = "";
            texto[i][7] = "";
            texto[i][8] = "";
            if (dadosPagamento.getNumDocumento() != null) {
                texto[i][0] = dadosPagamento.getNumDocumento();
            }
            if (dadosPagamento.getFornecedor() != null) {
                texto[i][1] = dadosPagamento.getFornecedor();
            }
            if (dadosPagamento.getObsParcela() != null) {
                texto[i][3] = dadosPagamento.getFornecedor();
            }
            if (dadosPagamento.getValorPago() != null) {
                texto[i][5] = NumeroUtil.formatarMonetario(dadosPagamento.getValorPago(), 2, false);
            }
            if (dadosPagamento.getDataVencimento() != null) {
                texto[i][6] = DataUtil.dateToString(dadosPagamento.getDataVencimento(), "dd/MM/yyyy");
            }
            if (dadosPagamento.getDataPagamento() != null) {
                texto[i][7] = DataUtil.dateToString(dadosPagamento.getDataPagamento(), "dd/MM/yyyy");
            }
            if (dadosPagamento.getSituacao() != null) {
                texto[i][8] = dadosPagamento.getSituacao();
            }
            texto[i][9] = DataUtil.dateToString(filtro.getData().getMin(), "dd/MM/yyyy");
            texto[i][10] = DataUtil.dateToString(filtro.getData().getMax(), "dd/MM/yyyy");

        }

        return texto;
    }

    public byte[] gerarRelatorioExtratoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<Estoque> estoqueList;
        if (produto != null) {
            estoqueList = estoqueService.listarMovimentoEstoque(produto, empresa, dataInicio, dataFinal);
        } else {
            estoqueList = estoqueService.listarMovimentoEstoque(empresa, dataInicio, dataFinal);
        }

        validaTamanho(estoqueList);

        Map<String, Object> params = createParams(empresa);
        params.put("dataInicio", dataInicio);
        params.put("dataFinal", dataFinal);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(estoqueList.stream()
                .map(EstoqueDTO::new)
                .collect(Collectors.toList()));

        return gerarArrayBytePDF(params, "EXTRATO DE ESTOQUE", "ExtratoEstoque.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioMovimentoEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<Estoque> estoqueList;
        if (produto != null) {
            estoqueList = estoqueService.listarMovimentoEstoque(produto, empresa, dataInicio, dataFinal);
        } else {
            estoqueList = estoqueService.listarMovimentoEstoque(empresa, dataInicio, dataFinal);
        }

        validaTamanho(estoqueList);

        Map<String, Object> params = createParams(empresa);

        params.put("dataFinal", dataFinal);
        params.put("dataInicial", dataInicio);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(estoqueList.stream()
                .map(EstoqueDTO::new)
                .collect(Collectors.toList()));

        return gerarArrayBytePDF(params, "MOVIMENTO DE ESTOQUE", "movimentoEstoque.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioGiroEstoque(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<Estoque> estoqueList;
        if (produto != null) {
            estoqueList = estoqueService.listarGiroEstoque(produto, empresa, dataInicio, dataFinal);
        } else {
            estoqueList = estoqueService.listarGiroEstoque(empresa, dataInicio, dataFinal);
        }

        validaTamanho(estoqueList);

        Map<String, Object> params = createParams(empresa);

        params.put("data", dataFinal);
        params.put("nomeQuebra", "PRODUTO");

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(estoqueList.stream()
                .map(EstoqueDTO::new)
                .collect(Collectors.toList()));

        return gerarArrayBytePDF(params, "GIRO DE ESTOQUE", "giroEstoque.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioGiroEstoqueSintetico(Produto produto, Empresa empresa, Date dataInicio, Date dataFinal) throws JRException, RelatorioException {
        List<Estoque> estoqueList;
        if (produto != null) {
            estoqueList = estoqueService.listarGiroEstoque(produto, empresa, dataInicio, dataFinal);
        } else {
            estoqueList = estoqueService.listarGiroEstoque(empresa, dataInicio, dataFinal);
        }

        validaTamanho(estoqueList);

        Map<String, Object> params = createParams(empresa);

        params.put("data", dataFinal);
        params.put("nomeQuebra", "PRODUTO");

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(estoqueList.stream()
                .map(EstoqueDTO::new)
                .collect(Collectors.toList()));

        return gerarArrayBytePDF(params, "GIRO DE ESTOQUE SINTÉTICO", "giroEstoqueSintetico.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioPosicaoEstoque(Produto produto, Empresa empresa, String tipoRelatorio) throws JRException, RelatorioException {
        List<EstoqueDTO> dados = validaTamanho(estoqueService.listarPosicaoEstoque(produto, empresa));
        if (tipoRelatorio.equals("semValores")) {
            return gerarArrayBytePDF(createParams(empresa), "Posições de estoque atual", "posicaoEstoqueSemCustos.jrxml", new JRBeanCollectionDataSource(dados));
        }
        return gerarArrayBytePDF(createParams(empresa), "Posições de estoque atual", "posicaoEstoque.jrxml", new JRBeanCollectionDataSource(dados));
    }

    public StreamedContent downloadModeloProdutosEstoque() throws FileException {
        List<Produto> listProduto = produtoService.listarProdutos();

        String[][] texto = criarArquivoExcelProduto(listProduto);

        String[] registrosConvertidos = converterRegistrosEmLinhasComSeparadorDeCampos(texto, ";");
        StringBuilder arquivo = new StringBuilder();
        for (String linha : registrosConvertidos) {
            arquivo.append(linha).append("\r\n");
        }

        return FileUtil.downloadFile(arquivo.toString().getBytes(StandardCharsets.UTF_8), "text/csv", "produtos estoque.csv");
    }

    public String[][] criarArquivoExcelProduto(List<Produto> produtos) {
        String[][] texto = new String[produtos.size() + 1][3];
        texto[0][0] = "#";
        texto[0][1] = "DESCRIÇÃO";
        texto[0][2] = "ESTOQUE DISPONÍVEL";

        for (int i = 1; i <= produtos.size(); i++) {
            Produto produto = produtos.get(i - 1);
            for (int j = 0; j < 3; j++) {
                switch (j) {
                    case 0:
                        texto[i][j] = produto.getId().toString();
                        break;
                    case 1:
                        texto[i][j] = produto.getDescricao();
                        break;
                    case 2:
                        texto[i][j] = produto.getEstoqueDisponivel() != null ? NumeroUtil.formatarCasasDecimais(produto.getEstoqueDisponivel(), 2, false) : "";
                        break;
                    default:
                        break;
                }
            }
        }

        return texto;
    }

    public StreamedContent downloadModeloContratoCliente() throws FileException {
        List<Contrato> listContratoCliente = contratoService.listarContratoCliente();

        String[][] texto = criarArquivoExcelContrato(listContratoCliente);

        String[] registrosConvertidos = converterRegistrosEmLinhasComSeparadorDeCampos(texto, ";");
        StringBuilder arquivo = new StringBuilder();
        for (String linha : registrosConvertidos) {
            arquivo.append(linha).append("\r\n");
        }
        return FileUtil.downloadFile(arquivo.toString().getBytes(StandardCharsets.UTF_8), "text/csv", "contratosClientes.csv");
    }

    public StreamedContent downloadModeloContratoFornecedor() throws FileException {
        List<Contrato> listContratoFornecedor = contratoService.listarContratoFornecedor();

        String[][] texto = criarArquivoExcelContrato(listContratoFornecedor);

        String[] registrosConvertidos = converterRegistrosEmLinhasComSeparadorDeCampos(texto, ";");
        StringBuilder arquivo = new StringBuilder();
        for (String linha : registrosConvertidos) {
            arquivo.append(linha).append("\r\n");
        }
        return FileUtil.downloadFile(arquivo.toString().getBytes(StandardCharsets.UTF_8), "text/csv", "contratosFornecedores.csv");
    }

    public String[][] criarArquivoExcelContrato(List<Contrato> contratos) {
        String[][] texto = new String[contratos.size() + 1][7];
        texto[0][0] = "#";
        texto[0][1] = "CLIENTE/FORNECEDOR";
        texto[0][2] = "DATA DE INÍCIO";
        texto[0][3] = "DATA DE TÉRMINO";
        texto[0][4] = "OBSERVAÇÃO";
        texto[0][5] = "VALOR DO CONTRATO";
        texto[0][6] = "NÚMERO DE PARCELAS";

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        for (int i = 1; i <= contratos.size(); i++) {
            Contrato contrato = contratos.get(i - 1);
            for (int j = 0; j < 7; j++) {
                switch (j) {
                    case 0:
                        texto[i][j] = contrato.getId().toString();
                        break;
                    case 1:
                        if (contrato.getIdCliente() != null) {
                            texto[i][j] = contrato.getIdCliente().getNome();
                        } else if (contrato.getIdFornecedor() != null) {
                            texto[i][j] = contrato.getIdFornecedor().getRazaoSocial();
                        }
                        break;
                    case 2:
                        texto[i][j] = df.format(contrato.getDataInicio());
                        break;
                    case 3:
                        texto[i][j] = df.format(contrato.getDataFim());
                        break;
                    case 4:
                        texto[i][j] = contrato.getObservacao();
                        break;
                    case 5:
                        texto[i][j] = contrato.getValorContrato() != null ? NumeroUtil.formatarCasasDecimais(contrato.getValorContrato(), 2, false) : "";
                        break;
                    case 6:
                        texto[i][j] = contrato.getNumeroParcelas().toString();
                        break;
                    default:
                        break;
                }
            }
        }

        return texto;
    }

    public void lerArquivoImportacaoEstoque(UploadedFile uploadedFile) throws IOException, FileException {
        try {
            byte[] conteudo = FileUtil.convertUploadedFileToBytes(uploadedFile);
            File arquivoLeitura = FileUtil.convertByteToFile(conteudo, "xxxx");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(arquivoLeitura), StandardCharsets.UTF_8))) {
                String linha;
                int i = 0;

                while ((linha = br.readLine()) != null) {
                    if (i > 0) {
                        String[] tableLine = linha.split(";");
                        if (tableLine != null && tableLine.length >= 3) {
                            String id = tableLine[0];
                            String estoque = tableLine[2];

                            atualizaEstoqueDisponivel(id, estoque);
                        }
                    }
                    i++;
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RelatorioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaEstoqueDisponivel(String id, String estoque) {
        try {
            Produto produto = produtoService.buscar(Integer.parseInt(id));
            produtoService.atualizarEstoqueImportacaoESalvarProduto(produto, Double.parseDouble(estoque));
        } catch (Exception ex) {
            Logger.getLogger(RelatorioService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public byte[] gerarRelatorioSolicitantes(Empresa empresa) throws JRException, RelatorioException {
        List<SolicitanteDTO> solicitantes = validaTamanho(solicitanteService.listarSolicitanteDTO());

        solicitantes.stream().forEach(solicitante -> {
            solicitante.setOrigem(EnumOrigemSolicitante.buscarDescricao(solicitante.getOrigem()));
            solicitante.setStatus(EnumStatusSolicitante.buscarDescricao(solicitante.getStatus()));
            if (solicitante.getCidade() != null) {
                solicitante.setCidade(solicitante.getCidade() + "/" + solicitante.getUf());
            }
        });

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(solicitantes);
        return gerarArrayBytePDF(createParams(empresa), "Relatório solicitantes", "solicitantes.jrxml", beanDataSource);
    }

    public byte[] gerarPanorama(Empresa empresa, Date dataInicio, Date dataFim) throws JRException, RelatorioException {
        List<SolicitantePanaromaDTO> panoramas = validaTamanho(solicitanteService.listarSolicitantePanoramaDTO(dataInicio, dataFim));

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(panoramas);
        return gerarArrayBytePDF(createParams(empresa), "PANORAMA", "panorama.jrxml", beanDataSource);
    }

    public byte[] gerarListaPresenca(Empresa empresa, Turma turma) throws JRException, RelatorioException {
        List<SolicitanteDTO> solicitantes = validaTamanho(solicitanteService.listaPresencaAlunos(turma));

        Map<String, Object> params = createParams(empresa);
        params.put("local", turma.getIdCidade().getDescricao() + "/" + turma.getIdCidade().getIdUF().getDescricao());

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(solicitantes);
        return gerarArrayBytePDF(params, "Lista de presença - " + turma.getIdCurso().getDescricao(), "listaPresenca.jrxml", beanDataSource);
    }

    public byte[] gerarRelacaoProfissoes(Empresa empresa, Turma turma) throws JRException, RelatorioException {
        List<SolicitanteDTO> solicitantes = validaTamanho(solicitanteService.gerarRelacaoProfissoes(turma));

        Map<String, Object> params = createParams(empresa);
        params.put("local", turma.getIdCidade().getDescricao() + "/" + turma.getIdCidade().getIdUF().getDescricao());
        params.put("listaResumo", solicitanteService.gerarRelacaoProfissoesResumo(turma));

        solicitantes.stream().forEach(solicitante -> {
            if (StringUtils.isEmpty(solicitante.getCargo())) {
                solicitante.setCargo("Não informado");
            }
            if (StringUtils.isEmpty(solicitante.getAreaAtuacao())) {
                solicitante.setAreaAtuacao("Não informado");
            }
            if (StringUtils.isEmpty(solicitante.getEmpresa())) {
                solicitante.setEmpresa("Não informado");
            }
        });

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(solicitantes);
        return gerarArrayBytePDF(params, "Relação das profissões - " + turma.getIdCurso().getDescricao(), "relacaoProfissoes.jrxml", "resumoRelacaoProfissoes.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioDre(Integer ano, CentroCusto centro) throws JRException, RelatorioException {
        List<DreDTO> listDadosDre = validaTamanho(analisesService.obterDre(ano, centro));

        Map<String, Object> params = createParams();
        if (centro != null) {
            params.put("centro", centro.getDescricao());
        } else {
            params.put("centro", "Todos os centros de custo");
        }
        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDadosDre);
        return gerarArrayBytePDF(params, "Demonstração do Resultado do Exercício (" + ano + ")", "dre.jrxml", beanDataSource);
    }

    public String[] converterRegistrosEmLinhasComSeparadorDeCampos(String[][] registros, String separadorCampo) {
        String[] linhas = new String[registros.length];

        // Transformando matriz em array com registros intermediados com separadoees
        for (int i = 0; i < linhas.length; i++) {
            linhas[i] = "";

            for (String campo : registros[i]) {
                if (campo != null) {
                    linhas[i] += campo + separadorCampo;
                }
            }
            int tamanhoLinha = linhas[i].length();
            if (linhas[i].substring(tamanhoLinha - 1).equals(separadorCampo)) {
                tamanhoLinha--;
            }

            linhas[i] = linhas[i].substring(0, tamanhoLinha);
        }
        return linhas;
    }

    public byte[] gerarImpressaoOSCusto(Venda os, FormaPagamento formaDePagamento) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(os));

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOS", os.getId().toString());
        params.put("statusOS", EnumStatusOS.getDescricao(os.getStatusOS()));
        params.put("seguradoraParticular", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "Particular");
        params.put("clienteNome", os.getIdCliente().getNome());
        if (os.getIdCliente().getEndereco() != null) {
            params.put("clienteEndereco", os.getIdCliente().getEndereco().getEnderecoCompleto());
        } else {
            params.put("clienteEndereco", "");
        }
        params.put("observacao", os.getObservacao());
        params.put("clienteTelefone", os.getIdCliente().getTelefoneCelular());
        params.put("clienteEmail", os.getIdCliente().getEmail());
        params.put("formaPagamento", formaDePagamento.getDescricao());
        params.put("condicaoPagamento", os.getCondicaoPagamento());
        params.put("km", os.getKm() != null ? os.getKm() : "");
        if (os.getIdClienteVeiculo() != null) {
            params.put("veiculoPlaca", os.getIdClienteVeiculo().getPlaca());
            params.put("veiculoDescricao", os.getIdClienteVeiculo().getNome());
            params.put("veiculoChassi", os.getIdClienteVeiculo().getChassi());
        } else {
            params.put("veiculoPlaca", "");
            params.put("veiculoDescricao", "");
            params.put("veiculoChassi", "");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "OS - Custo " + os.getId(), "osCusto.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoOrcamentoParaCliente(Venda orcamento, FormaPagamento formaDePagamento) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(orcamento));

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOrcamento", orcamento.getId().toString());
        params.put("statusOrcamento", EnumTipoVenda.retornaEnumSelecionado(orcamento.getStatusNegociacao()).getDescricao());
        params.put("seguradoraParticular", orcamento.getIdVendaSeguradora() != null ? orcamento.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "Particular");
        params.put("clienteNome", orcamento.getIdCliente().getNome());
        if (orcamento.getIdCliente().getEndereco() != null) {
            params.put("clienteEndereco", orcamento.getIdCliente().getEndereco().getEnderecoCompleto());
        } else {
            params.put("clienteEndereco", "");
        }
        params.put("observacao", orcamento.getObservacao());
        params.put("clienteTelefone", orcamento.getIdCliente().getTelefoneCelular());
        params.put("clienteEmail", orcamento.getIdCliente().getEmail());
        if (orcamento.getOrigem().equals("AP") && orcamento.getFormaPagamento() != null) {
            params.put("formaPagamento", orcamento.getFormaPagamento());
        } else {
            params.put("formaPagamento", formaDePagamento != null ? formaDePagamento.getDescricao() : "");
        }
        params.put("condicaoPagamento", orcamento.getCondicaoPagamento());
        params.put("km", orcamento.getKm() != null ? orcamento.getKm() : "");
        if (orcamento.getIdClienteVeiculo() != null) {
            params.put("veiculoPlaca", orcamento.getIdClienteVeiculo().getPlaca());
            params.put("veiculoDescricao", orcamento.getIdClienteVeiculo().getNome());
            params.put("veiculoChassi", orcamento.getIdClienteVeiculo().getChassi());
        } else {
            params.put("veiculoPlaca", "");
            params.put("veiculoDescricao", "");
            params.put("veiculoChassi", "");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Orçamento " + orcamento.getId(), "orcamentoCliente.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoOSVeiculo(Venda os) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(os), "Não há produtos, serviços ou itens para ordem de serviço salvos nesta ordem de serviço.");

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOS", os.getId().toString());
        params.put("orcamentista", os.getIdUsuarioVendedor().getNome());
        params.put("orcamentoOrigem", os.getIdOrcamentoOSOrigem() != null ? os.getIdOrcamentoOSOrigem().toString() : "OS");
        params.put("statusOS", EnumStatusOS.retornaEnumSelecionado(os.getStatusOS()).getDescricao());
        params.put("dataAprovacao", os.getDataVenda());
        params.put("dataExecucao", os.getDataInicioExecucao() != null ? os.getDataInicioExecucao() : null);
        params.put("seguradoraParticular", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "Particular");
        params.put("clienteNome", os.getIdCliente().getNome());
        params.put("clienteEndereco", os.getIdCliente().getEndereco().getEnderecoCompleto());
        params.put("clienteTelefone", os.getIdCliente().getTelefoneCelular());
        params.put("clienteEmail", os.getIdCliente().getEmail());
        params.put("observacaoGeral", os.getObservacao() != null ? os.getObservacao() : "");
        params.put("km", os.getKm() != null ? os.getKm() : "");
        if (os.getIdClienteVeiculo() != null) {
            params.put("veiculoPlaca", os.getIdClienteVeiculo().getPlaca());
            params.put("veiculoDescricao", os.getIdClienteVeiculo().getNome());
            params.put("veiculoChassi", os.getIdClienteVeiculo().getChassi());
            params.put("anoFabricacao", os.getIdClienteVeiculo().getAnoFabricacao().toString());
            params.put("anoModelo", os.getIdClienteVeiculo().getAnoModelo().toString());
            params.put("veiculoCor", os.getIdClienteVeiculo().getIdCorVeiculo() != null ? os.getIdClienteVeiculo().getIdCorVeiculo().getDescricao() : "");
        } else {
            params.put("veiculoPlaca", "");
            params.put("veiculoDescricao", "");
            params.put("veiculoChassi", "");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Ordem de serviço " + os.getId(), "ordemDeServico.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoOS(Venda os) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(os), "Não há produtos, serviços ou itens para ordem de serviço salvos nesta ordem de serviço.");

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOS", os.getId().toString());
        params.put("orcamentista", os.getIdUsuarioVendedor().getNome());
        params.put("orcamentoOrigem", os.getIdOrcamentoOSOrigem() != null ? os.getIdOrcamentoOSOrigem().toString() : "OS");
        params.put("statusOS", EnumStatusOS.retornaEnumSelecionado(os.getStatusOS()).getDescricao());
        params.put("dataAprovacao", os.getDataVenda());
        params.put("dataExecucao", os.getDataInicioExecucao() != null ? os.getDataInicioExecucao() : null);
        params.put("seguradoraParticular", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "Particular");
        params.put("clienteNome", os.getIdCliente().getNome());
        params.put("clienteEndereco", os.getIdCliente().getEndereco() != null ? os.getIdCliente().getEndereco().getEnderecoCompleto() : "");
        params.put("clienteTelefone", os.getIdCliente().getTelefoneCelular());
        params.put("clienteEmail", os.getIdCliente().getEmail());
        params.put("observacaoGeral", os.getObservacao() != null ? os.getObservacao() : "");

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Ordem de serviço " + os.getId(), "ordemDeServicoPadrao.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoGarantia(Venda os) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(os), "Não há produtos, serviços ou itens para ordem de serviço salvos nesta ordem de serviço.");

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        String servicos = StringUtil.listaParaTexto(os.getVendaServicoList().stream().map(VendaServico::getIdServico).map(Servico::getDescricao).collect(Collectors.toList()));
        String produtos = StringUtil.listaParaTexto(os.getVendaProdutoList().stream().map(VendaProduto::getDadosProduto).map(DadosProduto::getIdProduto).map(Produto::getDescricao).collect(Collectors.toList()));
        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOS", os.getId().toString());
        params.put("orcamentista", os.getIdUsuarioVendedor().getNome());
        params.put("orcamentoOrigem", os.getIdOrcamentoOSOrigem() != null ? os.getIdOrcamentoOSOrigem().toString() : "OS");
        params.put("statusOS", EnumStatusOS.retornaEnumSelecionado(os.getStatusOS()).getDescricao());
        params.put("dataAprovacao", os.getDataVenda());
        params.put("dataExecucao", os.getDataInicioExecucao() != null ? os.getDataInicioExecucao() : null);
        params.put("seguradoraParticular", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "Particular");
        params.put("clienteNome", os.getIdCliente().getNome());
        params.put("clienteEndereco", os.getIdCliente().getEndereco().getEnderecoCompleto());
        params.put("clienteTelefone", os.getIdCliente().getTelefoneCelular());
        params.put("clienteEmail", os.getIdCliente().getEmail());
        params.put("servicos", servicos);
        params.put("produtos", produtos);
        if (os.getIdClienteVeiculo() != null) {
            params.put("veiculoPlaca", os.getIdClienteVeiculo().getPlaca());
            params.put("veiculoDescricao", os.getIdClienteVeiculo().getNome());
            params.put("veiculoChassi", os.getIdClienteVeiculo().getChassi());
            params.put("anoFabricacao", os.getIdClienteVeiculo().getAnoFabricacao().toString());
            params.put("anoModelo", os.getIdClienteVeiculo().getAnoModelo().toString());
            params.put("veiculoCor", os.getIdClienteVeiculo().getIdCorVeiculo() != null ? os.getIdClienteVeiculo().getIdCorVeiculo().getDescricao() : "");
        } else {
            params.put("veiculoPlaca", "");
            params.put("veiculoDescricao", "");
            params.put("veiculoChassi", "");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Termo de Garantia " + os.getId(), "termoDeGarantia.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoPermanencia(Venda os) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(os), "Não há produtos, serviços ou itens para ordem de serviço salvos nesta ordem de serviço.");

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        String servicos = StringUtil.listaParaTexto(os.getVendaServicoList().stream().map(VendaServico::getIdServico).map(Servico::getDescricao).collect(Collectors.toList()));
        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("nroOS", os.getId().toString());
        params.put("orcamentista", os.getIdUsuarioVendedor().getNome());
        params.put("orcamentoOrigem", os.getIdOrcamentoOSOrigem() != null ? os.getIdOrcamentoOSOrigem().toString() : "OS");
        params.put("statusOS", EnumStatusOS.retornaEnumSelecionado(os.getStatusOS()).getDescricao());
        params.put("dataAprovacao", DataUtil.dateToString(os.getDataVenda(), "dd/MM/yyyy"));
        params.put("dataFim", DataUtil.dateToString(os.getDataFinalizacao(), "dd/MM/yyyy"));
        params.put("seguradora", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getIdClienteSeguradora().getNome() : "");
        params.put("clienteNome", os.getIdCliente().getNome());
        params.put("clienteEndereco", os.getIdCliente().getEndereco().getEnderecoCompleto());
        params.put("clienteTelefone", os.getIdCliente().getTelefoneCelular());
        params.put("numSinistro", os.getIdVendaSeguradora() != null ? os.getIdVendaSeguradora().getNumeroSinistro() : "");
        params.put("clienteEmail", os.getIdCliente().getEmail());
        params.put("servicos", servicos);
        if (os.getIdClienteVeiculo() != null) {
            params.put("veiculoPlaca", os.getIdClienteVeiculo().getPlaca());
            params.put("veiculoDescricao", os.getIdClienteVeiculo().getNome());
            params.put("veiculoChassi", os.getIdClienteVeiculo().getChassi());
            params.put("anoFabricacao", os.getIdClienteVeiculo().getAnoFabricacao().toString());
            params.put("anoModelo", os.getIdClienteVeiculo().getAnoModelo().toString());
            params.put("veiculoCor", os.getIdClienteVeiculo().getIdCorVeiculo() != null ? os.getIdClienteVeiculo().getIdCorVeiculo().getDescricao() : "");
        } else {
            params.put("veiculoPlaca", "");
            params.put("veiculoDescricao", "");
            params.put("veiculoChassi", "");
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Declaração de permanência " + os.getId(), "declaracaoDePermanencia.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoOrcamento(Venda orcamento, FormaPagamento formaDePagamento) throws JRException, RelatorioException {
        List<OrcamentoClienteDTO> listDados = validaTamanho(vendaService.listaItensOrcamentoCliente(orcamento), "Não há produtos, serviços ou itens para ordem de serviço salvos neste orçemanto.");

        Empresa empresa = empresaService.getEmpresa();
        if (empresa.getEndereco() == null || empresa.getEndereco().isBlank()) {
            throw new RelatorioException("Preencha os dados de endereço da empresa", null);
        }

        Optional<VendaFormaPagamento> vendaFormaPagamento = vendaService.listarVendaFormaPagamento(orcamento).stream()
                .filter(vfp -> vfp.getIdFormaPagamento().equals(formaDePagamento))
                .findFirst();

        Map<String, Object> params = createParams(empresa);

        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("inscricaoEstadual", empresa.getInscricaoEstadual() != null ? empresa.getInscricaoEstadual() : "");
        params.put("inscricaoMunicipal", empresa.getInscricaoMunicipal() != null ? empresa.getInscricaoMunicipal() : "");
        params.put("statusOrcamento", EnumTipoVenda.retornaEnumSelecionado(orcamento.getStatusNegociacao()).getDescricao());
        params.put("clienteNome", orcamento.getIdCliente().getNome());
        if (orcamento.getIdCliente().getEndereco() != null) {
            params.put("clienteEndereco", orcamento.getIdCliente().getEndereco().getEnderecoCompleto());
        } else {
            params.put("clienteEndereco", "");
        }
        String telefone = orcamento.getIdCliente().getTelefoneCelular();
        if (telefone == null || telefone.trim().isEmpty()) {
            telefone = orcamento.getIdCliente().getTelefoneResidencial();
        }
        if (telefone == null || telefone.trim().isEmpty()) {
            telefone = orcamento.getIdCliente().getTelefoneComercial();
        }
        params.put("observacao", orcamento.getObservacao());
        params.put("clienteTelefone", telefone != null ? telefone : "");
        params.put("clienteEmail", orcamento.getIdCliente().getEmail());
        params.put("formaPagamento", formaDePagamento != null ? formaDePagamento.getDescricao() : "");
        params.put("condicaoPagamento", "");
        vendaFormaPagamento.ifPresent(forma -> params.put("condicaoPagamento", EnumFormaPagamento.retornaEnumSelecionado(forma.getCondicaoPagamento()).getDescricao()));

        params.put("nroOrcamento", orcamento.getId().toString());
        params.put("dataVencimento", orcamento.getDataVencimento());
        params.put("valorDesconto", orcamento.getDesconto());
        params.put("observacaoSistema", parametroSistemaService.getParametro().getObservacaoImpressaoOrcamento());
        params.put("valorOrcamento", orcamento.getValor());
        params.put("vendedor", orcamento.getIdUsuarioVendedor().getNome());
        params.put("validade", orcamento.getDataValidadeOrcamento() != null ? DataUtil.dateToString(orcamento.getDataValidadeOrcamento()) : "");

        // Empresa
        String cep = empresa.getEndereco().getCep() != null ? empresa.getEndereco().getCep() : "";
        params.put("enderecoEmpresa", empresa.getEndereco().getEnderecoCompleto() + (cep.isEmpty() ? "" : " - ") + cep);
        Calendar dataAtual = Calendar.getInstance();

        String dataOrcamento = new SimpleDateFormat("dd/MM/yyyy").format(dataAtual.getTime());
        params.put("dataGeracao", dataOrcamento);
        params.put("horarioOrcamento", new SimpleDateFormat("HH:mm").format(dataAtual.getTime()));
        params.put("anoOrcamento", Integer.toString(dataAtual.get(Calendar.YEAR)));

        params.put("emailEmpresa", empresa.getEmail());

        if (vendaFormaPagamento.isPresent()) {
            Integer parcelas = 1;
            if (vendaFormaPagamento.get().getCondicaoPagamento().contains("x")) {
                parcelas = Integer.parseInt(vendaFormaPagamento.get().getCondicaoPagamento().replace("x", ""));
            }
            params.put("parcelas", parcelas);
            params.put("descontoOrcamento", vendaFormaPagamento.get().getDesconto());
        } else {
            params.put("parcelas", null);
            params.put("descontoOrcamento", 0d);
        }

        // Cliente
        Cliente cliente = orcamento.getIdCliente();
        String nomePDF = cliente.getNome();
        if (orcamento.getRepresentanteEmpresa() != null) {
            nomePDF += " - " + orcamento.getRepresentanteEmpresa();
        }
        params.put("nomeCliente", nomePDF);

        String enderecoCliente = cliente.getEndereco() != null ? cliente.getEndereco().getEndereco() : null;

        if (enderecoCliente != null && cliente.getEndereco().getNumero() != null) {
            enderecoCliente = enderecoCliente + ", " + cliente.getEndereco().getNumero();
        }

        if (enderecoCliente != null && cliente.getEndereco().getComplemento() != null) {
            enderecoCliente = enderecoCliente + " " + cliente.getEndereco().getComplemento();
        }

        params.put("cidadeCliente", "");
        params.put("ufCliente", "");
        params.put("cepCliente", "");
        if (enderecoCliente != null && cliente.getEndereco().getIdCidade() != null) {
            params.put("cidadeCliente", cliente.getEndereco().getIdCidade().getDescricao());
            params.put("ufCliente", cliente.getEndereco().getIdCidade().getIdUF().getDescricao());
            if (cliente.getEndereco().getCep() != null) {
                params.put("cepCliente", cliente.getEndereco().getCep());
            }
        }
        params.put("enderecoCliente", enderecoCliente);
        String telefoneCliente = "";
        if (cliente.getTelefoneCelular() != null) {
            telefoneCliente = cliente.getTelefoneCelular() + "   ";
        }
        if (cliente.getTelefoneComercial() != null) {
            telefoneCliente += cliente.getTelefoneComercial() + "   ";
        }
        if (cliente.getTelefoneResidencial() != null) {
            telefoneCliente += cliente.getTelefoneResidencial() + "   ";
        }
        params.put("telefoneCliente", cliente.getTelefoneCelular() != null ? cliente.getTelefoneCelular() : "");
        params.put("emailCliente", cliente.getEmail());
        params.put("cpfCNPJCliente", cliente.getCpfCNPJ());

        Collections.sort(listDados);

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);

        if (formaDePagamento != null) {
            params.put("formaPagamento", formaDePagamento.getDescricao());
        } else {
            params.put("formaPagamento", "Não informada");
        }
        return gerarArrayBytePDF(params, "Orçamento " + orcamento.getId(), "orcamento.jrxml", beanDataSource);
    }

    public byte[] gerarImpressaoReciboVenda(Venda venda, byte[] imagemRecibo) throws JRException {
        List<ReciboDTO> recibosDto = new ArrayList<>();
        Double valorPago = contaService.listarContaParcela(venda.getIdConta()).stream()
                .map(ContaParcela::getValorPago)
                .filter(java.util.Objects::nonNull)
                .reduce(0d, (a, b) -> a + b);

        ReciboDTO reciboDTO = new ReciboDTO();
        reciboDTO.setDescricao(venda.getIdPlanoConta().getDescricao());
        reciboDTO.setValorFormatado(NumeroUtil.formatarCasasDecimais(valorPago, 2, false));
        recibosDto.add(reciboDTO);

        Map<String, Object> params = createParams();
        params.put("termosCondicoes", venda.getTermosCondicoes());
        // Cliente
        Cliente cliente = venda.getIdCliente();
        String nomePDF = cliente.getNome();
        if (venda.getRepresentanteEmpresa() != null) {
            nomePDF += " - " + venda.getRepresentanteEmpresa();
        }
        params.put("nomeCliente", nomePDF);

        params.put("valor", "R$ " + reciboDTO.getValorFormatado());
        params.put("numero", venda.getId());

        return gerarArrayBytePDF(params, "Recibo", "reciboVenda.jrxml", new JRBeanCollectionDataSource(recibosDto));
    }

    public byte[] gerarImpressaoVenda(Venda venda) throws JRException, RelatorioException {
        List<OrcamentoDTO> listDados = validaTamanho(vendaService.listaItensOrcamento(venda), "Não há produtos, serviços ou itens para ordem de serviço salvos neste orçemanto.");
        Integer parcelas = 1;
        if (venda.getFormaPagamento().contains("x")) {
            parcelas = Integer.parseInt(venda.getFormaPagamento().replace("x", ""));
        }

        Map<String, Object> params = createParams();
        // Orçamento
        params.put("numero", "" + venda.getId());
        params.put("dataVencimento", venda.getDataVencimento());
        params.put("valorDesconto", venda.getDesconto());
        params.put("parcelas", parcelas);
        params.put("observacao", venda.getObservacao());
        params.put("observacaoSistema", parametroSistemaService.getParametro().getObservacaoImpressaoOrcamento());
        params.put("valorOrcamento", venda.getValor());
        params.put("nomeVendedor", venda.getIdUsuarioVendedor().getNome());

        // Empresa
        Empresa empresa = empresaService.getEmpresa();
        params.put("enderecoEmpresa", empresa.getEndereco().getEnderecoCompleto());
        params.put("enderecoEmpresaComplemento", "CEP: " + empresa.getEndereco().getCep());
        params.put("telefoneEmpresa", empresa.getFone());
        params.put("emailEmpresa", empresa.getEmail());

        // Cliente
        Cliente cliente = venda.getIdCliente();
        String nomePDF = cliente.getNome();
        if (venda.getRepresentanteEmpresa() != null) {
            nomePDF += " - " + venda.getRepresentanteEmpresa();
        }
        params.put("nomeCliente", nomePDF);

        params.put("enderecoCliente", cliente.getEndereco() == null ? "" : cliente.getEndereco().getEnderecoCompleto());

        params.put("telefoneCliente", cliente.getTelefoneCelular() == null ? "" : cliente.getTelefoneCelular());
        params.put("emailCliente", cliente.getEmail() == null ? "" : cliente.getEmail());

        Collections.sort(listDados);

        Integer numeroLinha = 1;
        for (OrcamentoDTO dto : listDados) {
            dto.setNumeroLinha(numeroLinha);
            numeroLinha++;
        }

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(listDados);
        return gerarArrayBytePDF(params, "Venda " + venda.getId(), "venda.jrxml", beanDataSource);
    }

    public byte[] gerarRecibo(int mes, int ano, Cliente cliente) throws JRException {
        List<ControleFinanceiroDTO> modelRecebimento = financeiroService.listarRecebimentos(mes, ano, cliente.getNome());
        ParametroSistema ps = parametroSistemaService.getParametro();
        Double valotTotal = modelRecebimento.stream()
                .mapToDouble(ControleFinanceiroDTO::getValor)
                .sum();

        Map<String, Object> params = createParams();
        params.put("nome", cliente.getNome());
        params.put("valor", "R$ " + NumeroUtil.formatarCasasDecimais(valotTotal, 2, false));
        params.put("header", ps.getTituloRecibo());
        params.put("numero", ps.getNumeroRecibo());

        return gerarArrayBytePDF(params, "Recibo", "recibo.jrxml", new JRBeanCollectionDataSource(modelRecebimento));
    }

    public byte[] imprimeNfse(ContaParcelaFiltro filtro) throws IOException {
        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresaService.getEmpresa().getDescricao(), 29)),
                new Row(new Cell("Período"), new Cell(DataUtil.dateToString(filtro.getDataInicio()) + " até " + DataUtil.dateToString(filtro.getDataFim()), 28)),
                new Row(HEADER_STYLE_CENTER,
                        new Cell("Número"), new Cell("Status"), new Cell("Data de emissão"), new Cell("Data de vencimento"), new Cell("Tomador"), new Cell("Convênio"), new Cell("Valor bruto"),
                        new Cell("ISS", 3), new Cell("IRPJ", 3), new Cell("PIS", 3), new Cell("COFINS", 3), new Cell("CSLL", 3), new Cell("Impostos", 3),
                        new Cell("A pagar"), new Cell("A receber"), new Cell("Gestão", 2)
                ),
                new Row(HEADER_STYLE_CENTER,
                        new Cell(""), new Cell(""), new Cell(""), new Cell(""), new Cell(""), new Cell(""), new Cell(""),
                        new Cell("Retido"), new Cell("Total"), new Cell("A recolher"),// ISS
                        new Cell("Retido"), new Cell("Total"), new Cell("A recolher"),// IRPJ
                        new Cell("Retido"), new Cell("Total"), new Cell("A recolher"),// PIS
                        new Cell("Retido"), new Cell("Total"), new Cell("A pagar"),// COFINS
                        new Cell("Retido"), new Cell("Total"), new Cell("A pagar"),// CSLL
                        new Cell("Total impostos federais a pagar"), new Cell("Total em R$"), new Cell("Total em %"),// Impostos
                        new Cell("Total"), new Cell("Líquido após retenções"), new Cell("Líquido após impostos"), new Cell("Situação")
                )
        );
        filtro.setTemNFS(true);
        List<NFSeRelatorioDTO> nfses = contaService.getListaParcelaFiltrada(filtro).stream()
                .map(nfeMapper::toDTO)
                .collect(Collectors.toList());
        Section body = new Section();
        body.getRows().addAll(nfses.stream()
                .map(dto -> dto.getReportRow(valor, POSITIVE_PERCENT))
                .collect(Collectors.toList()));

        Row footer = new Row(valor,
                new Cell(), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(), new Cell(0d),
                new Cell(0d), new Cell(0d), new Cell(0d),// ISS
                new Cell(0d), new Cell(0d), new Cell(0d),// IRPJ
                new Cell(0d), new Cell(0d), new Cell(0d),// PIS
                new Cell(0d), new Cell(0d), new Cell(0d),// COFINS
                new Cell(0d), new Cell(0d), new Cell(0d),// CSLL
                new Cell(0d), new Cell(0d), new Cell(0d, POSITIVE_PERCENT),// Impostos
                new Cell(0d), new Cell(0d), new Cell(0d), new Cell()
        );

        for (Row bodyRow : body.getRows()) {
            for (int i = 6; i < 28; i++) {
                Cell cell = footer.getCells().get(i);
                Double acumulador = (Double) cell.getContent();
                Double value = (Double) bodyRow.getCells().get(i).getContent();
                cell.setContent(acumulador + value);
            }
        }
        int qte = body.getRows().size();
        footer.getCells().stream()
                .filter(cell -> POSITIVE_PERCENT.equals(cell.getStyle()))
                .forEach(cell -> {
                    Double value = (Double) cell.getContent();
                    cell.setContent(value / qte);
                });

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .fixHeader(true)
                .header(header)
                .body(body)
                .footer(new Section(footer))
                .build());
    }

    public byte[] gerarOrdemServico(OrdemDeServico os) throws JRException {
        List<ItensOrdemDeServico> model = ordemDeServicoService.listarItens(os);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        List<String> dadosEndereco = new ArrayList<>();

        Map<String, Object> params = createParams();
        params.put("dataPrevisaoInicio", os.getDataInicio() != null ? dateFormat.format(os.getDataInicio()) : "Não informado.");
        params.put("dataPrevisaoTermino", os.getDataTermino() != null ? dateFormat.format(os.getDataTermino()) : "Não informado.");

        params.put("nomeCliente", os.getIdCliente().getNome());
        params.put("cpfCnpjCliente", os.getIdCliente().getCpfCNPJ());
        params.put("emailCliente", os.getIdCliente().getEmail());

        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && StringUtils.isNotBlank(os.getIdCliente().getEndereco().getEndereco()))) {
            dadosEndereco.add(os.getIdCliente().getEndereco().getEndereco());
        }
        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && StringUtils.isNotBlank(os.getIdCliente().getEndereco().getNumero()))) {
            dadosEndereco.add(os.getIdCliente().getEndereco().getNumero());
        }
        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && StringUtils.isNotBlank(os.getIdCliente().getEndereco().getComplemento()))) {
            dadosEndereco.add(os.getIdCliente().getEndereco().getComplemento());
        }
        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && Objects.isNotNull(os.getIdCliente().getEndereco().getIdCidade()) && Objects.isNotNull(os.getIdCliente().getEndereco().getIdCidade().getDescricao()))) {
            dadosEndereco.add((!dadosEndereco.isEmpty()) ? ", ".concat(os.getIdCliente().getEndereco().getIdCidade().getDescricao()) : os.getIdCliente().getEndereco().getIdCidade().getDescricao());
        }
        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && Objects.isNotNull(os.getIdCliente().getEndereco().getIdCidade()) && Objects.isNotNull(os.getIdCliente().getEndereco().getIdCidade().getIdUF()) && Objects.isNotNull(os.getIdCliente().getEndereco().getIdCidade().getIdUF().getDescricao()))) {
            dadosEndereco.add(os.getIdCliente().getEndereco().getIdCidade().getIdUF().getDescricao());
        }
        if ((Objects.isNotNull(os.getIdCliente().getEndereco()) && StringUtils.isNotBlank(os.getIdCliente().getEndereco().getCep()))) {
            dadosEndereco.add((!dadosEndereco.isEmpty()) ? "- CEP: ".concat(os.getIdCliente().getEndereco().getCep()) : "CEP: ".concat(os.getIdCliente().getEndereco().getCep()));
        }

        String enderecoCliente = String.join(dadosEndereco.size() > 1 ? " " : "", dadosEndereco);
        params.put("enderecoCliente", enderecoCliente);

        params.put("observacao", os.getObservacao() != null ? os.getObservacao() : "");
        params.put("numOS", os.getId());
        Empresa empresa = empresaService.getEmpresa();
        params.put("responsavelOS", empresa.getNomeFantasia().split(" ")[0]);
        String cep = empresa.getEndereco().getCep() != null ? empresa.getEndereco().getCep() : "";
        params.put("enderecoEmpresa", empresa.getEndereco() + (cep.isEmpty() ? "" : " CEP: ") + cep);
        params.put("cnpjEmpresa", empresa.getCnpj());
        params.put("emailEmpresa", empresa.getEmail());
        params.put("telefoneEmpresa", empresa.getFone());
        params.put("inscricaoEstadualEmpresa", (empresa.getInscricaoEstadual() != null && (!empresa.getInscricaoEstadual().isEmpty())) ? empresa.getInscricaoEstadual() : "ISENTO");

        return gerarArrayBytePDF(params, "OS", "OS.jrxml", new JRBeanCollectionDataSource(model));
    }

    public StreamedContent gerarExcelRelatorioIndicacao(Date dataInicio, Date dataFim) throws FileException {
        List<Empresa> empresas = empresaService.listaEmpresaCadastradaPorPeriodo(dataInicio, dataFim);

        String[][] texto = criarArquivoExcelRelatorioIndicacao(empresas);

        String[] registrosConvertidos = converterRegistrosEmLinhasComSeparadorDeCampos(texto, ";");
        StringBuilder arquivo = new StringBuilder();
        for (String linha : registrosConvertidos) {
            arquivo.append(linha).append("\r\n");
        }
        return FileUtil.downloadFile(arquivo.toString().getBytes(StandardCharsets.ISO_8859_1), "text/csv", "Relatorio Indicação.csv");
    }

    public String[][] criarArquivoExcelRelatorioIndicacao(List<Empresa> dados) {
        String[][] texto = new String[dados.size() + 1][11];
        texto[0][0] = "DATA CREDENCIAMENTO";
        texto[0][1] = "NOME FANTASIA";
        texto[0][2] = "ONDE CONHECEU";

        for (int i = 1; i <= dados.size(); i++) {
            Empresa empresa = dados.get(i - 1);

            texto[i][0] = empresa.getDataCredenciamento().toString();
            texto[i][1] = empresa.getNomeFantasia();
            texto[i][2] = empresa.getOndeConheceu();
        }

        return texto;
    }

    public byte[] gerarRelatorioClienteMovimentacaoAnalitico(Cliente cliente, MinMax<Date> datas, CentroCusto centro) throws JRException, RelatorioException {
        Empresa empresa = empresaService.getEmpresa();
        List<ClienteMovimentacaoRelatorioAnaliticoDTO> movimentacoes = validaTamanho(clienteMovimentacaoService.listaMovimentacaoRelatorio(cliente, datas.getMin(), datas.getMax(), empresa, centro), "Não há movimentações para os filtros selecionados.");

        Map<String, Object> params = createParams();
        params.put("telefone", empresa.getTelefone() != null ? empresa.getTelefone() : "");
        params.put("cnpj", empresa.getCnpj());
        params.put("dataGeracao", new Date());
        params.put("dataInicio", datas.getMin());
        params.put("dataFim", datas.getMax());

        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(movimentacoes);
        return gerarArrayBytePDF(params, "Cliente movimentacao analitico ", "relatorioMovimentoAnalitico.jrxml", beanDataSource);
    }

    public byte[] gerarRelatorioAnaliticoSaldoCliente(Cliente cliente, MinMax<Date> datas, CentroCusto centro) throws RelatorioException, IOException {
        Empresa empresa = empresaService.getEmpresa();
        List<ClienteMovimentacaoRelatorioAnaliticoDTO> movimentacoes = validaTamanho(clienteMovimentacaoService.listaMovimentacaoRelatorio(cliente, datas.getMin(), datas.getMax(), empresa, centro), "Não há movimentações para os filtros selecionados.");

        Style titulo = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        Style normal = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresa.getNomeFantasia(), HEADER_STYLE_CENTER, 8)),
                new Row(new Cell("ANALÍTICO DE MOVIMENTAÇÕES DE CLIENTES ", HEADER_STYLE_CENTER, 8)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell("", 6), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT)),
                new Row(new Cell("PERÍODO DE APURAÇÃO"), new Cell("", 5), new Cell(DataUtil.dateToString(datas.getMin()), HEADER_STYLE_RIGHT), new Cell(DataUtil.dateToString(datas.getMax()), HEADER_STYLE_RIGHT))
        );
        Section body = new Section();
        String lastCliente = "";

        Cell celulaSaldoAtual = new Cell("");
        for (ClienteMovimentacaoRelatorioAnaliticoDTO dto : movimentacoes) {
            if (!lastCliente.equals(dto.getNome())) {
                if (!lastCliente.isEmpty()) {
                    body.getRows().add(new Row());
                }
                celulaSaldoAtual = new Cell("", 6);
                body.getRows().add(new Row(titulo, new Cell("CLIENTE", titulo), new Cell(dto.getNome(), normal, 6)));
                body.getRows().add(new Row(titulo, new Cell("SALDO ANTERIOR", titulo), new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorSaldoAnterior(), 2), getCorValor(dto.getValorSaldoAnterior(), false), 6)));
                body.getRows().add(new Row(titulo, new Cell("SALDO ATUAL", titulo), celulaSaldoAtual));
                body.getRows().add(new Row(new Cell("DATA MOVIMENTAÇÃO"), new Cell("HISTORICO"), new Cell("DATA VENCIMENTO"), new Cell("JUROS"), new Cell("VALOR DO TÍTULO"), new Cell("DATA PAGAMENTO"), new Cell("VALOR SEM ENCARGOS"), new Cell("VALOR COM ENCARGOS")));
                lastCliente = dto.getNome();
            }
            celulaSaldoAtual.setContent(NumeroUtil.formatarCasasDecimais(dto.getValorSaldo(), 2));
            celulaSaldoAtual.setStyle(getCorValor(dto.getValorSaldo(), false));
            int i = 1;
            if (dto.getOrigem().equals("Saída lançada") || dto.getOrigem().equals("Lançamento no IUGU")) {
                i = -1;
            }
            Double valorComEncargos = (dto.getValorPrevisto() + dto.getValorJuros()) * i;
            body.getRows().add(new Row(
                    new Cell(DataUtil.dateToString(dto.getDataMovimentacao())),
                    new Cell(dto.getOrigem()),
                    new Cell(dto.getDataVencimento() != null ? DataUtil.dateToString(dto.getDataVencimento()) : ""),
                    new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorJuros() * i, 2), getCorValor(dto.getValorJuros() * i, false)),
                    new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorPrevisto() * i, 2), getCorValor(dto.getValorPrevisto() * i, false)),
                    new Cell(dto.getDataPagamento() != null ? DataUtil.dateToString(dto.getDataPagamento()) : ""),
                    new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorPago() * i, 2), getCorValor(dto.getValorPago() * i, false)),
                    new Cell(NumeroUtil.formatarCasasDecimais(valorComEncargos, 2), getCorValor(valorComEncargos, false))
            ));
        }

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] gerarRelatorioSineticoSaldoCliente(List<ClienteSaldoDTO> dados, Double saldoAnterior, MinMax<Date> datas) throws IOException {
        Empresa empresa = empresaService.getEmpresa();
        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        Style valorTitulo = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);
        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresa.getNomeFantasia(), HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("SINTÉTICO DE MOVIMENTAÇÕES DE CLIENTES ", HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT, 2)),
                new Row(new Cell("PERÍODO DE APURAÇÃO"), new Cell(DataUtil.dateToString(datas.getMin()), HEADER_STYLE_RIGHT), new Cell(DataUtil.dateToString(datas.getMax()), HEADER_STYLE_RIGHT)),
                new Row(new Cell("SALDO ANTERIOR"), new Cell(NumeroUtil.formatarCasasDecimais(saldoAnterior, 2), valorTitulo, 2)),
                new Row(new Cell("SALDO ATUAL"), new Cell(NumeroUtil.formatarCasasDecimais(dados.stream().mapToDouble(ClienteSaldoDTO::getSaldo).sum(), 2), valorTitulo, 2)),
                new Row(new Cell("NOME", HEADER_STYLE_CENTER), new Cell("SALDO", HEADER_STYLE_CENTER, 2))
        );
        Section body = new Section();
        dados.forEach(dto -> body.getRows().add(new Row(new Cell(dto.getNome()),
                new Cell(NumeroUtil.formatarCasasDecimais(dto.getSaldo(), 2), valor, 2))));

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] gerarRelatorioMediaPrazoPagamentoRecebimento(MinMax<Date> datas) throws RelatorioException, IOException {
        Empresa empresa = empresaService.getEmpresa();
        List<MediaPrazoRelatorioDTO> mediaPrazo = validaTamanho(contaService.listaMediaPrazoPagamentoRecebimento(datas.getMin(), datas.getMax()), "Não há dados para os filtros selecionados.");

        Style titulo = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresa.getNomeFantasia(), HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("MEDIA PRAZO DE PAGAMENTO E REBIMENTO ", HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell(""), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT)),
                new Row(new Cell("PERÍODO DE APURAÇÃO"), new Cell(DataUtil.dateToString(datas.getMin()), HEADER_STYLE_RIGHT), new Cell(DataUtil.dateToString(datas.getMax()), HEADER_STYLE_RIGHT)),
                new Row(new Cell("NOME"), new Cell("MEDIA PRAZO PAGAMENTO (DIAS)"), new Cell("MEDIA PRAZO RECEBIMENTO (DIAS)")));
        Section body = new Section();

        Double mediaPagamento = 0d;
        Double mediaRecebimento = 0d;
        int countFornecedor = 0;
        int countCliente = 0;
        for (MediaPrazoRelatorioDTO dto : mediaPrazo) {
            if (dto.getTipoPessoa().equals("C")) {
                countCliente++;
                mediaRecebimento += dto.getMediaDias();
                body.getRows().add(new Row(new Cell(dto.getNome()), new Cell(""), new Cell(NumeroUtil.formatarCasasDecimais(dto.getMediaDias(), 2))));
            } else {
                countFornecedor++;
                mediaPagamento += dto.getMediaDias();
                body.getRows().add(new Row(new Cell(dto.getNome()), new Cell(NumeroUtil.formatarCasasDecimais(dto.getMediaDias(), 2)), new Cell("")));
            }
        }

        Section footer = new Section();
        Double totalMediaPagamento = 0d;
        Double totalMediaRecebimento = 0d;
        if (countFornecedor != 0) {
            totalMediaPagamento = NumeroUtil.dividir(mediaPagamento, countFornecedor);
        }
        if (countCliente != 0) {
            totalMediaRecebimento = NumeroUtil.dividir(mediaRecebimento, countCliente);
        }
        footer.getRows().add(new Row(new Cell("", 3)));
        footer.getRows().add(new Row(new Cell(""), new Cell("TOTAL MEDIA PAGAMENTO", titulo), new Cell(NumeroUtil.formatarCasasDecimais(totalMediaPagamento, 2))));
        footer.getRows().add(new Row(new Cell(""), new Cell("TOTAL MEDIA RECEBIMENTO", titulo), new Cell(NumeroUtil.formatarCasasDecimais(totalMediaRecebimento, 2))));

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .footer(footer)
                .build());
    }

    public byte[] gerarRelatorioClientesComMovimentacaoSemIugu(MinMax<Date> datas) throws RelatorioException, IOException {
        Empresa empresa = empresaService.getEmpresa();
        List<ClienteMovimentacaoRelatorioAnaliticoDTO> movimentacoes = new ArrayList<>();
        List<Cliente> clientesIUGU = clienteMovimentacaoService.clientesComIuguNoPeriodo(datas.getMin(), datas.getMax(), empresa.getTenatID());
        List<Cliente> clientesNota = clienteMovimentacaoService.clientesComNotasNoPeriodo(datas.getMin(), datas.getMax(), empresa.getTenatID());
        clientesNota.removeIf(clientesIUGU::contains);
        validaTamanho(clientesNota, "Não há clientes sem faturas do IUGU e movimentações de nota no periodo selecionado.");

        for (Cliente cliente : clientesNota) {
            List<ClienteMovimentacaoRelatorioAnaliticoDTO> aux = clienteMovimentacaoService.listaMovimentacaoSemIUGURelatorio(datas.getMin(), datas.getMax(), empresa, cliente);
            movimentacoes.addAll(aux);
        }
        validaTamanho(movimentacoes, "Não há movimentações para os filtros selecionados.");

        Style titulo = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        Style normal = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);
        Style valorNegrito = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.MONEY);

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresa.getNomeFantasia(), HEADER_STYLE_CENTER, 4)),
                new Row(new Cell("CONTROLE DE COBRANÇA", HEADER_STYLE_CENTER, 4)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell("", 1), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT, 2)),
                new Row(new Cell("PERÍODO DE APURAÇÃO"), new Cell("", 1), new Cell(DataUtil.dateToString(datas.getMin()), HEADER_STYLE_RIGHT, 1), new Cell(DataUtil.dateToString(datas.getMax()), HEADER_STYLE_RIGHT, 1))
        );
        Section body = new Section();
        String lastCliente = "";

        for (ClienteMovimentacaoRelatorioAnaliticoDTO dto : movimentacoes) {
            if (!lastCliente.equals(dto.getNome())) {
                if (!lastCliente.isEmpty()) {
                    body.getRows().add(new Row());
                }
                body.getRows().add(new Row(titulo, new Cell("CLIENTE", titulo), new Cell(dto.getNome(), normal, 3)));
                body.getRows().add(new Row(new Cell("DATA MOVIMENTAÇÃO", titulo), new Cell("HISTORICO", titulo), new Cell("DATA VENCIMENTO", titulo), new Cell("VALOR", valorNegrito)));
                lastCliente = dto.getNome();
            }
            body.getRows().add(new Row(
                    new Cell(DataUtil.dateToString(dto.getDataMovimentacao())),
                    new Cell(dto.getOrigem()),
                    new Cell(dto.getDataVencimento() != null ? DataUtil.dateToString(dto.getDataVencimento()) : ""),
                    new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorPrevisto(), 2), getCorValor(dto.getValorPrevisto(), false))
            ));
        }
        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] gerarRelatorioAcumuladoPorTipoMovimentacao(MinMax<Date> datas) throws RelatorioException, IOException {
        Empresa empresa = empresaService.getEmpresa();
        List<ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO> movimentacoes = validaTamanho(clienteMovimentacaoService.acumuladoPorTipoMovimentacao(datas.getMin(), datas.getMax(), empresa.getTenatID()));

        Style titulo = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresa.getNomeFantasia(), HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("VALOR ACUMULADO POR TIPO DE MOVIMENTACAO", HEADER_STYLE_CENTER, 3)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT, 2)),
                new Row(new Cell("PERÍODO DE APURAÇÃO"), new Cell(DataUtil.dateToString(datas.getMin()), HEADER_STYLE_RIGHT, 1), new Cell(DataUtil.dateToString(datas.getMax()), HEADER_STYLE_RIGHT, 1))
        );
        Section body = new Section();
        String lastCliente = "";

        for (ClienteMovimentacaoRelatorioSinteticoTipoMovimentacaoDTO dto : movimentacoes) {
            if (!lastCliente.equals(dto.getOrigem())) {
                if (!lastCliente.isEmpty()) {
                    body.getRows().add(new Row());
                }
                body.getRows().add(new Row(new Cell("ORIGEM", titulo), new Cell("TIPO MOVIMENTAÇÃO", titulo), new Cell("VALOR ACUMULADO", titulo)));
                lastCliente = dto.getOrigem();
            }
            body.getRows().add(new Row(
                    new Cell(EnumTipoClienteMovimentacao.retornaEnumSelecionado(dto.getOrigem()).getDescricao()),
                    new Cell(dto.getTipoMovimentacao() != null ? dto.getTipoMovimentacao() : ""),
                    new Cell(NumeroUtil.formatarCasasDecimais(dto.getValorTotal(), 2), valor)
            ));
        }
        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] gerarRelatorioSaldoDevedorPorFornecedor(Fornecedor fornecedor, MinMax<Date> data) throws RelatorioException, IOException {
        List<ControlePagamentoDTO> extrato = validaTamanho(extratoContaCorrenteService.getControlePagamentoList(fornecedor, data), "Não há contas com saldo devedor para os filtros selecionados.");

        Style titulo = new Style(Color.BLACK, 10, "Arial", true, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        Style nomeFornecedor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);
        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        String periodo = "";
        if (data.getMin() != null) {
            if (data.getMax() != null) {
                periodo += "a partir ";
            }
            periodo += "de " + DataUtil.dateToString(data.getMin());
        }
        if (data.getMax() != null) {
            periodo += " até " + DataUtil.dateToString(data.getMax());
        }

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell(empresaService.getEmpresa().getNomeFantasia(), HEADER_STYLE_CENTER, 8)),
                new Row(new Cell("CONTROLE DE PAGAMENTOS", HEADER_STYLE_CENTER, 8)),
                new Row(new Cell("DATA DA GERAÇÃO"), new Cell(DataUtil.dateToString(new Date()), HEADER_STYLE_RIGHT, 7)),
                new Row(new Cell("PERÍODO"), new Cell(periodo, HEADER_STYLE_RIGHT, 7))
        );
        Section body = new Section();

        for (ControlePagamentoDTO dto : extrato) {
            body.getRows().add(new Row(new Cell("FORNECEDOR", titulo), new Cell(dto.getNome(), nomeFornecedor, 7)));
            body.getRows().add(new Row(new Cell("VALOR TOTAL", titulo), new Cell(dto.getValorTotal(), valor, 7)));
            body.getRows().add(new Row(new Cell("VALOR PAGO", titulo), new Cell(dto.getValorPago(), valor, 7)));
            body.getRows().add(new Row(new Cell("VALOR A PAGAR", titulo), new Cell(dto.getValorAPagar(), valor, 7)));
            body.getRows().add(ControlePagamentoItemDTO.headerRow());

            dto.getItens().forEach(row -> body.getRows().add(row.toRow(DATE, valor)));
            body.getRows().add(new Row());
        }
        body.getRows().remove(body.getRows().size() - 1);

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] acessosPorEmpresa(Boolean showSuporte, Boolean showContabilidade, boolean showPagamento, MinMax<Date> periodo) throws IOException {
        int headerSize = 3 + (showPagamento ? 1 : 0);

        Section header = new Section(HEADER_STYLE_CENTER,
                new Row(new Cell("Acessos por empresa", headerSize)),
                new Row(new Cell("Período", HEADER_STYLE_LEFT), new Cell(DataUtil.dateToString(periodo.getMin()) + " até " + DataUtil.dateToString(periodo.getMax()), HEADER_STYLE_RIGHT, headerSize - 1)),
                new Row(new Cell("Usuário"), new Cell("Perfil"), new Cell("Acessos"))
        );
        if (showPagamento) {
            header.getRows().get(2).addCell(new Cell("Valor pago"));
        }

        return gerarArrayByteExcel(
                Page.builder()
                        .name("Empresas com status PAGO")
                        .header(header)
                        .body(createAcessoPage(showSuporte, showContabilidade, In.fromList(Collections.singletonList("P")), periodo, showPagamento))
                        .fixHeader(true)
                        .build(),
                Page.builder()
                        .name("Empresas com status EXPIRADO")
                        .header(header)
                        .body(createAcessoPage(showSuporte, showContabilidade, In.fromList(Collections.singletonList("E")), periodo, showPagamento))
                        .fixHeader(true)
                        .build(),
                Page.builder()
                        .name("Empresas com status GRÁTIS")
                        .header(header)
                        .body(createAcessoPage(showSuporte, showContabilidade, In.fromList(Collections.singletonList("G")), periodo, showPagamento))
                        .fixHeader(true)
                        .build());
    }

    private Section createAcessoPage(Boolean showSuporte, Boolean showContabilidade, In<String> tipoPagamento, MinMax<Date> periodo, boolean showPagamento) {
        List<AcessoPorEmpresaDTO> listaAcesso = empresaService.listaAcessosPorEmpresa(showSuporte, showContabilidade, tipoPagamento, periodo);
        for (AcessoPorEmpresaDTO aux1 : listaAcesso) {
            for (AcessoPorEmpresaDTO aux2 : listaAcesso) {
                if (aux1.getEmpresa().equals(aux2.getEmpresa()) && aux1.getUsuario().equals(aux2.getUsuario()) && aux1 != aux2) {
                    aux1.setQuantidade(aux1.getQuantidade() + aux2.getQuantidade());
                    aux2.setQuantidade(0L);
                }
            }
        }
        listaAcesso.removeIf(dto -> !showPagamento && dto.getQuantidade() == 0);
        Section body = new Section();
        Row empresaHeader = new Row(new Cell(), new Cell(), new Cell());
        int ultimaEmpresa = -1;
        long acessos = 0;
        for (AcessoPorEmpresaDTO dto : listaAcesso) {
            if (dto.getIdEmpresa() != ultimaEmpresa) {
                empresaHeader.getCells().get(2).setContent((int) acessos);
                empresaHeader = new Row(HEADER_STYLE_LEFT, new Cell(dto.getEmpresa()), new Cell(), new Cell());

                ultimaEmpresa = dto.getIdEmpresa();
                acessos = 0;

                body.getRows().add(new Row());
                body.getRows().add(empresaHeader);
            }
            acessos += dto.getQuantidade();
            Row row = new Row(new Cell(dto.getUsuario()), new Cell(dto.getPerfil()), new Cell((int) dto.getQuantidade(), 1));
            if (showPagamento) {
                row.addCell(new Cell(dto.getValor()));
            }
            body.getRows().add(row);
        }
        empresaHeader.getCells().get(1).setContent((int) acessos);
        return body;
    }

    public byte[] extratoContaSintetico(ExtratoContaCorrenteFiltro filtro) throws RelatorioException, IOException {
        filtro.addPropriedadeOrdenacao("cp1.dataPagamento:ASCENDING");
        List<ExtratoContaCorrente> extrato = validaTamanho(extratoContaCorrenteService.getListaFiltrada(filtro), "Não há contas com saldo devedor para os filtros selecionados.");

        Style valor = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        Style valorHeader = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.MONEY);
        String periodo = "";
        if (filtro.getData().getMin() != null) {
            if (filtro.getData().getMax() != null) {
                periodo += "a partir ";
            }
            periodo += "de " + DataUtil.dateToString(filtro.getData().getMin());
        }
        if (filtro.getData().getMax() != null) {
            periodo += " até " + DataUtil.dateToString(filtro.getData().getMax());
        }

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell("Extrato de conta bancária sintético", HEADER_STYLE_CENTER, 7)),
                new Row(new Cell("Período de apuração"), new Cell(periodo, 6)),
                new Row(new Cell("Conta bancária"), new Cell(filtro.getContaBancaria() != null ? filtro.getContaBancaria().getDescricao() : "", 6)),
                new Row(new Cell("Saldo atual"), new Cell(extrato.get(extrato.size() - 1).getSaldo(), valorHeader, 6)),
                new Row(new Cell("Filtros"), new Cell(filtro.toString(), 6)),
                new Row(new Cell("Plano de contas"), new Cell("Origem"), new Cell("Tipo"), new Cell("Data"), new Cell("Saldo anterior"), new Cell("Valor"), new Cell("Saldo"))
        );
        Section body = new Section();
        extrato.forEach(ex -> body.getRows().add(new Row(
                new Cell(ex.getDescricao()),
                new Cell(ex.getOrigem()),
                new Cell(EnumTipoExtrato.getDescricao(ex.getTipo())),
                new Cell(ex.getIdContaParcela().getDataPagamento(), DATE),
                new Cell(ex.getSaldoAnterior(), valor),
                new Cell(ex.getTipo().equals("D") ? ex.getValor() * -1 : ex.getValor(), valor),
                new Cell(ex.getSaldo(), valor)
        )));

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

    public byte[] extratoContaAnaltico(ExtratoContaCorrenteFiltro filtro) throws RelatorioException, IOException {
        filtro.getPropriedadeOrdenacao().clear();
        filtro.getPropriedadeOrdenacao().add(new SortMeta(null, "cp1.dataPagamento", SortOrder.ASCENDING, null));
        //filtro.getPropriedadeOrdenacao().add(new SortMeta(null, "id", SortOrder.ASCENDING, null));
        filtro.setQuantidadeRegistros(0);
        Date dataInicial = filtro.getData().getMin();
        filtro.getData().setMin(null);
        List<ExtratoContaCorrente> extrato = validaTamanho(extratoContaCorrenteService.getListaFiltrada(filtro), "Não há contas com saldo devedor para os filtros selecionados.");
        filtro.getData().setMin(dataInicial);

        Style valorStyle = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.MONEY);
        Style valorHeader = new Style(Color.BLACK, 10, "Arial", true, Color.LIGHT_GRAY, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.MONEY);
        Style corpoTexto = new Style(Color.BLACK, 10, "Arial", false, Color.WHITE, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.RIGHT, CellFormat.TEXT);
        Style subcabecalhoData = new Style(Color.BLACK, 10, "Arial", true, Color.white, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.DATE);
        Style subcabecalhoValor = new Style(Color.BLACK, 10, "Arial", true, Color.white, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.MONEY);
        Style subcabecalhoTexto = new Style(Color.BLACK, 10, "Arial", true, Color.white, Color.BLACK, BorderStyle.THIN, HorizontalAlignment.LEFT, CellFormat.TEXT);
        String periodo = "";
        if (filtro.getData().getMin() != null) {
            if (filtro.getData().getMax() != null) {
                periodo += "a partir ";
            }
            periodo += "de " + DataUtil.dateToString(filtro.getData().getMin());
        }
        if (filtro.getData().getMax() != null) {
            periodo += " até " + DataUtil.dateToString(filtro.getData().getMax());
        }

        Section header = new Section(HEADER_STYLE_LEFT,
                new Row(new Cell("Extrato de conta bancária analítico", HEADER_STYLE_CENTER, 14)),
                new Row(new Cell("Período de apuração"), new Cell(periodo, 13)),
                new Row(new Cell("Conta bancária"), new Cell(filtro.getContaBancaria() != null ? filtro.getContaBancaria().getDescricao() : "", 13)),
                new Row(new Cell("Saldo atual"), new Cell(extrato.get(extrato.size() - 1).getSaldo(), valorHeader, 13)),
                new Row(new Cell("Filtros"), new Cell(filtro.toString(), 13))
        );

        Date dataAtual = new Date();
        int i = 0;
        boolean init = true;
        Section body = new Section();
        Cell saldoAtualCell = new Cell();
        Double saldo = 0d;
        saldo = filtro.getContaBancaria().getSaldoInicial();
        for (ExtratoContaCorrente ex : extrato) {
            Date dataPagamento = null;
            Date dataEmissao = null;
            Double valor = null;
            Double valorPago = null;
            Double valorTotal = null;
            Double juros = null;
            Double tarifa = null;
            Double desconto = null;
            String observacao = "";
            Conta conta = null;
            if (ex.getIdContaParcela() != null) {
                dataPagamento = ex.getIdContaParcela().getDataPagamento();
                dataEmissao = ex.getIdContaParcela().getDataEmissao();
                valor = ex.getIdContaParcela().getValor();
                valorPago = ex.getIdContaParcela().getValorPago();
                valorTotal = ex.getIdContaParcela().getValorTotal();
                juros = ex.getIdContaParcela().getJuros();
                tarifa = ex.getIdContaParcela().getTarifa();
                desconto = ex.getIdContaParcela().getDesconto();
                conta = ex.getIdContaParcela().getIdConta();

                if (ex.getIdContaParcela().getIdConta().getObservacao() != null) {
                    observacao += ex.getIdContaParcela().getIdConta().getObservacao().trim();
                }
                if (ex.getIdContaParcela().getObservacao() != null && !ex.getIdContaParcela().getObservacao().trim().isEmpty()) {
                    if (!observacao.isEmpty()) {
                        observacao += " - ";
                    }
                    observacao += ex.getIdContaParcela().getObservacao().trim();
                }
            }
            if (dataPagamento == null && ex.getIdContaParcelaParcial() != null) {
                dataPagamento = ex.getIdContaParcelaParcial().getDataPagamento();
                valor = ex.getIdContaParcelaParcial().getValorPago();
                valorPago = ex.getIdContaParcelaParcial().getValorPago();
                valorTotal = ex.getIdContaParcelaParcial().getValorTotal();
                juros = ex.getIdContaParcelaParcial().getJuros();
                tarifa = 0d;// ex.getIdContaParcelaParcial().getTarifa();
                desconto = ex.getIdContaParcelaParcial().getDesconto();
                conta = ex.getIdContaParcelaParcial().getIdContaParcela().getIdConta();
            }
            if (dataPagamento == null) {
                valor = ex.getValor();
                valorPago = ex.getValor();
                valorTotal = ex.getValor();
                juros = 0d;
                tarifa = 0d;
                desconto = 0d;
            }
            if (dataPagamento != null && dataPagamento.before(dataInicial)) {
                saldo += NumeroUtil.somar(valorPago) * (ex.getTipo().equals("C") ? 1 : -1);
                continue;
            }
            init = false;
            if (dataPagamento != null && !dataPagamento.equals(dataAtual) || i == 0) {
                saldoAtualCell = new Cell(0d, subcabecalhoValor, 13);
                body.getRows().add(new Row());
                body.getRows().add(new Row(new Cell("Data", subcabecalhoTexto), new Cell(dataPagamento, subcabecalhoData, 13)));
                body.getRows().add(new Row(new Cell("Saldo anterior", subcabecalhoTexto), new Cell(saldo, subcabecalhoValor, 13)));
                body.getRows().add(new Row(new Cell("Saldo do dia", subcabecalhoTexto), saldoAtualCell));
                body.getRows().add(new Row(new Cell("Plano de contas", HEADER_STYLE_CENTER),
                        new Cell("Origem", HEADER_STYLE_CENTER),
                        new Cell("Fornecedor/Cliente", HEADER_STYLE_CENTER),
                        new Cell("Tipo", HEADER_STYLE_CENTER),
                        new Cell("Observação", HEADER_STYLE_CENTER),
                        new Cell("Data de Emissão", HEADER_STYLE_CENTER),
                        new Cell("Data da operação", HEADER_STYLE_CENTER),
                        new Cell("Saldo anterior", HEADER_STYLE_CENTER),
                        new Cell("Valor bruto", HEADER_STYLE_CENTER),
                        new Cell("Juros", HEADER_STYLE_CENTER),
                        new Cell("Tarifa", HEADER_STYLE_CENTER),
                        new Cell("Descontos", HEADER_STYLE_CENTER),
                        new Cell("Valor", HEADER_STYLE_CENTER),
                        new Cell("Saldo", HEADER_STYLE_CENTER)
                ));
            }

            String nome = "";
            if (conta != null) {
                if (conta.getIdFornecedor() != null) {
                    nome = conta.getIdFornecedor().getRazaoSocial();
                } else if (conta.getIdCliente() != null) {
                    nome = conta.getIdCliente().getNome();
                }
            }
            Double valorPago2 = NumeroUtil.somar(valorPago * (ex.getTipo().equals("C") ? 1 : -1));
            Double valorTotal2 = NumeroUtil.somar(valorTotal * (ex.getTipo().equals("C") ? 1 : -1));
            Double valor2 = NumeroUtil.somar(valor * (ex.getTipo().equals("C") ? 1 : -1));
            Double valorJuros = NumeroUtil.somar(juros) * (ex.getTipo().equals("C") ? 1 : -1);
            Double valorTarifa = NumeroUtil.somar(tarifa) * (ex.getTipo().equals("C") ? 1 : -1);
            Double valorDesconto = NumeroUtil.somar(desconto) * (ex.getTipo().equals("C") ? 1 : -1);
            body.getRows().add(new Row(
                    new Cell(ex.getDescricao(), corpoTexto),// Plano de contas
                    new Cell(ex.getOrigem(), corpoTexto),// Origem
                    new Cell(nome),// Fornecedor/Cliente
                    new Cell(ex.getTipo() != null ? EnumTipoExtrato.getDescricao(ex.getTipo()) : "", corpoTexto),// Tipo
                    new Cell(observacao, corpoTexto),// Observação
                    new Cell(dataEmissao, DATE),// Data de Emissão
                    new Cell(dataPagamento != null ? dataPagamento : "", DATE),// Data da operação
                    new Cell(saldo, valorStyle),// Saldo anterior
                    new Cell(valor2, valorStyle),// Valor bruto
                    new Cell(valorJuros, valorStyle),// Juros
                    new Cell(valorTarifa, valorStyle),// Tarifa
                    new Cell(valorDesconto, valorStyle),// Descontos
                    new Cell(valorTotal2, valorStyle),// Valor
                    new Cell(saldo + valorPago2, valorStyle)// Saldo
            ));
            if (dataPagamento != null) {
                dataAtual = dataPagamento;
            }
            saldo += valorPago2;
            saldoAtualCell.setContent(saldo);
            i++;
        }

        return gerarArrayByteExcel(Page.builder()
                .name("Aba 1")
                .header(header)
                .body(body)
                .build());
    }

}
