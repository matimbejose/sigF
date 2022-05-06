package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ControleMenu;
import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.NF;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CompraFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.CompraService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FormaPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoPagamentoService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeMedidaService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFormaPagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumMeioDePagamento;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CompraService compraService;

    @EJB
    private ContaService contaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private UFService uFService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private PlanoPagamentoService planoPagamentoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @Inject
    private ControleMenu controleMenu;

    private Compra compraSelecionado;

    private Produto produtoSelecionado;

    private Conta contaSelecionada;

    private List<CompraProduto> listCompraProduto = new LinkedList<>();

    private List<ParcelaDTO> listParcelaDTO = new LinkedList<>();

    private LazyDataModel<Compra> model;

    private CompraFiltro filtro = new CompraFiltro();

    private transient UploadedFile file;

    private CompraProduto compraProdutoNaoLocalizadoSelecionado;

    private NF nfSelecionado;

    private String controleEdicao;

    private CompraProduto itemSelecionado;

    private FormaPagamento formaPagamento;

    private Boolean cadastroCompraXml;

    private Boolean adicionaProduto;

    protected Produto addProduto;

    protected String addDescricao;

    protected Double precoCusto;

    protected Double precoVenda;

    protected Double estoqueInicial;

    protected UnidadeMedida addUnidade;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, compraService::quantidadeRegistrosFiltrados, compraService::getListaFiltrada);
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getData().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_COMPRA.getChave()).getDescricao());
        return "cadastroBanco.xhtml";
    }

    @Override
    public void cleanCache() {
        file = null;
        this.itemSelecionado = null;
        this.listCompraProduto = new LinkedList<>();
        cleanProduto();
    }

    public List<Compra> getCompras() {
        return compraService.listar();
    }

    public EnumFormaPagamento[] getFormasPagamento() {
        return EnumFormaPagamento.values();
    }

    public List<PlanoPagamento> getPlanosPagamento() {
        return planoPagamentoService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        cadastroCompraXml = false;
        this.compraSelecionado = new Compra();
        compraSelecionado.setSituacao("A");
        adicionaProduto = false;
        return "cadastroCompra.xhtml";
    }

    public String doStartImportarXML() {
        cleanCache();
        this.compraSelecionado = new Compra();
        return "importarCadastroCompra.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        cadastroCompraXml = false;
        listCompraProduto = compraService.listarCompraProduto(compraSelecionado);
        listCompraProduto.stream().forEach(compraProduto -> compraProduto.setControle(StringUtil.gerarStringAleatoria(8)));
        contaSelecionada = contaService.buscarConta(compraSelecionado);

        compraSelecionado.setListParcelaDTO(contaService.listarContaParcela(contaSelecionada).stream()
                .map(contaReceberParcela
                        -> ParcelaDTO.builder()
                        .dataVencimento(contaReceberParcela.getDataVencimento())
                        .desconto(contaReceberParcela.getDesconto())
                        .numParcela(contaReceberParcela.getNumParcela())
                        .valor(contaReceberParcela.getValor())
                        .valorPago(contaReceberParcela.getValorPago())
                        .dataPagamento(contaReceberParcela.getDataPagamento())
                        .situacao(EnumSituacaoConta.retornaDescricaoPorSituacao(contaReceberParcela.getSituacao()))
                        .build())
                .collect(Collectors.toList()));

        atualizarValorTotal();

        adicionaProduto = false;
        return "cadastroCompra.xhtml";
    }

    public String doFinishAdd() {
        try {
            ParametroSistema parametroSistemaSelecionado = parametroSistemaService.getParametro();
            compraSelecionado.setIdPlanoConta(parametroSistemaSelecionado.getIdPlanoContaCompraPadrao());
            compraSelecionado.setIdContaBancaria(contaBancariaService.getContaBancariaPadrao());
            if (compraSelecionado.getIdPlanoConta() == null) {
                createFacesErrorMessage("Paramentro de plano de contas padrão não foi definido. Não será possivel salvar compra.");
                return "cadastroCompra.xhtml";
            }

            String chave = compraSelecionado.getNReferencia();
            if (chave != null && !chave.isEmpty()) {
                if (chave.length() != 44) {
                    createFacesErrorMessage("Chave da NFe inválida.");
                    return "cadastroCompra.xhtml";
                }
                int dv = NumeroUtil.modulo11(chave.substring(0, 43));
                if (!chave.endsWith(dv + "")) {
                    createFacesErrorMessage("Chave da NFe inválida.");
                    return "cadastroCompra.xhtml";
                }
            }
            if (!listCompraProduto.isEmpty() && !compraSelecionado.getListParcelaDTO().isEmpty()) {
                compraSelecionado.setListCompraProduto(listCompraProduto);
                compraService.salvar(compraSelecionado);

                createFacesSuccessMessage("Compra salva com sucesso!");
                return "listaCompra.xhtml";
            } else {
                createFacesErrorMessage("Informe no mínimo um item para salvar a compra.");
                return "cadastroCompra.xhtml";
            }
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroCompra.xhtml";
        }
    }

    public void atualizarValorTotal() {
        compraSelecionado.setValorTotal(compraService.calcularValorTotal(listCompraProduto, compraSelecionado.getListParcelaDTO()));
    }

    public void doStartAddProduto() {
        cleanProduto();
        itemSelecionado.setDevolvido("N");
        listCompraProduto.add(itemSelecionado);

    }

    public void calcularValorTotal() {
        calcularDescontoTotal();
        Double valorTotal = compraService.calcularValorTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());
        compraSelecionado.setValorTotal(valorTotal);
    }

    public void calcularDescontoTotal() {
        Double descontoTotal = compraService.calcularDescontoTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());
        compraSelecionado.setValorDesconto(descontoTotal);
    }

    public String doStartVincular() {
        if (compraProdutoNaoLocalizadoSelecionado == null) {
            createFacesErrorMessage("Selecione o produto a ser associado.");
            return "listaProdutoNaoLocalizado.xhtml";
        }

        if (produtoSelecionado == null) {
            createFacesErrorMessage("Selecione o produto que será associado.");
            return "listaProdutoNaoLocalizado.xhtml";
        }

        // vincula o produto
        compraProdutoNaoLocalizadoSelecionado.getDadosProduto().setIdProduto(produtoSelecionado);

        compraProdutoNaoLocalizadoSelecionado = null;
        return "listaProdutoNaoLocalizado.xhtml";
    }

    public String podeContinuarProcessamento() {
        // verifica se existe algum produto que ainda nao esta cadastrado
        if (compraSelecionado.getListCompraProduto() == null) {
            compraSelecionado.setListCompraProduto(listCompraProduto);
        }
        return compraSelecionado.getListCompraProduto().stream()
                .noneMatch(compraProduto -> compraProduto.getDadosProduto().getIdProduto() == null) ? "cadastroCompra.xhtml" : "listaProdutoNaoLocalizado.xhtml";
    }

    public String doStartProcessamento() {
        if (file == null || file.getFileName() == null) {
            createFacesErrorMessage("Ocorreu um erro ao enviar o arquivo, caso o problema ocorra novamente, entre em contato com o suporte!");
            return "importarCadastroCompra.xhtml";
        }
        if (!file.getFileName().toLowerCase().endsWith(".xml")) {
            createFacesErrorMessage("Atenção, é necessário enviar um arquivo XML.");
            return "importarCadastroCompra.xhtml";
        }

        if (StringUtils.isNotEmpty(file.getFileName())) {
            try {
                cadastroCompraXml = true;
                compraProdutoNaoLocalizadoSelecionado = null;
                produtoSelecionado = null;
                compraSelecionado = compraService.processarXML(file.getContents());
                listCompraProduto = compraSelecionado.getListCompraProduto();
                listCompraProduto.stream().forEach(cp -> cp.setControle(StringUtil.gerarStringAleatoria(8)));

                // verifica se existe algum produto que ainda nao esta cadastrado
                boolean temProdutoNaoCadastrado = listCompraProduto.stream()
                        .anyMatch(prod -> prod.getDadosProduto().getIdProduto() == null);
                if (temProdutoNaoCadastrado) {
                    return "listaProdutoNaoLocalizado.xhtml";
                }
                return "cadastroCompra.xhtml";
            } catch (NumberFormatException | JAXBException ex) {
                createFacesErrorMessage("Erro ao processar arquivo XML da nota fiscal.");
                return "importarCadastroCompra.xhtml";
            }
        } else {
            createFacesErrorMessage("Informe o arquivo XML da nota fiscal antes de antes prosseguir.");
            return "importarCadastroCompra.xhtml";
        }
    }

    public String doStartEmitirNotaDevolucao() {

        Empresa empresa = empresaService.getEmpresa();
        Cidade cidade = empresa.getEndereco().getIdCidade();
        UF uf = cidade.getIdUF();

        nfSelecionado = new NF();

        nfSelecionado.setDataEmissao(DataUtil.getHoje());
        nfSelecionado.setIdUfPrestacao(uf);
        nfSelecionado.setIdMunicipioPrestacao(cidade);

        if (compraSelecionado.getNumeroNf() != null) {
            nfSelecionado.setNumeroNotaFiscal(Integer.parseInt(compraSelecionado.getNumeroNf()));
        }

        return "cadastroNotaDevolucao";
    }

    public String cancelarCompra() {
        try {
            compraService.cancelarCompra(compraSelecionado);
            createFacesSuccessMessage("Compra cancelada com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "listaCompra.xhtml";
    }

    public boolean existeParcelaPaga(Compra compra) {
        return contaService.existeParcelaPaga(compra);
    }

    public boolean existeParcelaPaga() {
        if (compraSelecionado.getId() != null) {
            return contaService.existeParcelaPaga(compraSelecionado);
        } else {
            return false;
        }
    }

    public void preencherValorTotal() {
        if (EnumFormaPagamento.AVISTA.getForma().equals(compraSelecionado.getFormaPagamento())
                && !compraSelecionado.getListParcelaDTO().isEmpty()) {
            compraSelecionado.getListParcelaDTO().get(0).setValor(compraSelecionado.getValorTotal());
        }
    }

    public void preencherValorDesconto() {
        if (EnumFormaPagamento.AVISTA.getForma().equals(compraSelecionado.getFormaPagamento()) && !compraSelecionado.getListParcelaDTO().isEmpty()) {
            compraSelecionado.getListParcelaDTO().get(0).setDesconto(compraSelecionado.getValorDesconto());
        }
    }

    public void preencherPlanoPagamento() {
        if (compraSelecionado.getIdPlanoPagamento() != null) {
            int quantidade = compraSelecionado.getIdPlanoPagamento().getQuantidadeParcelas();
            if (quantidade < 12) {
                compraSelecionado.setFormaPagamento(EnumFormaPagamento.retornaEnumSelecionado(quantidade + "x").getForma());
                if (quantidade == 1 && compraSelecionado.getIdPlanoPagamento().getPossuiEntrada().equals("S")) {
                    compraSelecionado.setFormaPagamento(EnumFormaPagamento.AVISTA.getForma());
                }
            }
            preencherFormaPagamento();
        }
    }

    public void preencherFormaPagamento() {
        calcularValorTotal();
        compraSelecionado = contaService.parcelarCompra(compraSelecionado);
    }

    public void cleanProduto() {
        produtoSelecionado = null;
    }

    public void carregarProduto() {
        itemSelecionado.setIdCompra(compraSelecionado);
        itemSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
        itemSelecionado.getDadosProduto().setQuantidade(1d);
        itemSelecionado.getDadosProduto().setValor(itemSelecionado.getDadosProduto().getIdProduto().getValorCusto());
        itemSelecionado.getDadosProduto().setDesconto(0d);
    }

    public void removeProduto() {
        listCompraProduto.remove(itemSelecionado);
        calcularValorTotal();
    }

    public void cadastrarProduto() {
        Produto produto = produtoService.adicionarPorCompraProduto(compraProdutoNaoLocalizadoSelecionado);
        compraProdutoNaoLocalizadoSelecionado.getDadosProduto().setIdProduto(produto);
    }

    public String getDescontoTotal() {
        // somando os descontos dos produtos da lista
        Double desconto = compraService.calcularDescontoTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(desconto);
    }

    public String getSubTotal() {
        // somando o total dos produtos da lista
        Double total = compraService.calcularValorTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(total);
    }

    public String getValorTotal() {
        // somando o total dos produtos da lista
        Double desconto = compraService.calcularDescontoTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());
        Double total = compraService.calcularValorTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());

        return "R$ " + super.converterValorParaMonetario(total - desconto);
    }

    public Double getValorT() {
        Double desconto = compraService.calcularDescontoTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());
        Double total = compraService.calcularValorTotal(listCompraProduto, compraSelecionado.getListParcelaDTO());

        return total - desconto;
    }

    public void carregarProduto(CompraProduto compraProduto) {
        if (compraProduto != null && compraProduto.getDadosProduto().getIdProduto() != null) {
            compraProduto.setIdCompra(compraSelecionado);
            compraProduto.getDadosProduto().setValor(compraProduto.getDadosProduto().getIdProduto().getValorCusto());
            if (compraProduto.getDadosProduto().getValor() == null) {
                compraProduto.getDadosProduto().setValor(compraProduto.getDadosProduto().getIdProduto().getValorVenda());
            }
            compraProduto.getDadosProduto().setQuantidade(1d);
            compraProduto.getDadosProduto().setDesconto(0d);
            compraService.atualizaTenatIDCompraProduto(compraProduto);
        }

        calcularValorTotal();
        calcularDescontoTotal();
    }

    public List<Produto> getProdutosDisponiveis() {
        List<Produto> produtosDisponiveis = produtoService.listarParaCompra();
        List<Produto> produtosAtribuidos = listCompraProduto.stream().map(CompraProduto::getDadosProduto).map(DadosProduto::getIdProduto).distinct().collect(Collectors.toList());
        if (produtosAtribuidos != null) {
            produtosDisponiveis.stream()
                    .filter(prod -> produtosAtribuidos.stream().anyMatch(aux -> prod.getId().equals(aux.getId())))
                    .forEach(prod -> prod.setSelecionavel(false));
        }
        return produtosDisponiveis;
    }

    public List<Produto> getProdutos() {
        return produtoService.listar();
    }

    public String doShowAuditoria() {
        return "listaAuditoriaCompra.xhtml";
    }

    public String doFinishExcluir() {
        compraService.remover(compraSelecionado);
        return "listaCompra.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return compraService.getDadosAuditoriaByID(compraSelecionado);
    }

    public boolean compraPossuiNF(Compra compra) {
        if (compra != null) {
            return compraService.compraPossuiNF(compra);
        }
        return false;
    }

    public EnumMeioDePagamento[] getEnumMeioDePagamento() {
        return EnumMeioDePagamento.values();
    }

    public List<Cidade> getMunicipios() {
        if (nfSelecionado.getIdUfPrestacao() != null) {
            return cidadeService.listar(nfSelecionado.getIdUfPrestacao());
        }
        return new LinkedList<>();
    }

    public List<UF> getUFs() {
        return uFService.listar();
    }

    public String getEnumMeioDePagamentoDesc(String cod) {
        if (cod == null) {
            return "";
        }
        return EnumMeioDePagamento.retornaEnumSelecionado(cod).getDescricao();
    }

    public String getSituacaoVenda(String sigla) {
        return "icon-" + (controleMenu.isCadastroOk(sigla) ? "like font-blue-madison" : "dislike font-red-mint");
    }

    public void limpaControle() {
        controleEdicao = null;
        itemSelecionado = new CompraProduto();
        itemSelecionado.setDevolvido("N");
        adicionaProduto = false;
    }

    public void addItem() {
        try {
            if (adicionaProduto) {
                addProduto.setDescricao(addDescricao);
                addProduto.setDataEntrada(new Date());
                addProduto.setEstoqueDisponivel(estoqueInicial);
                addProduto.setIdUnidadeMedida(addUnidade);
                addProduto.setValorCusto(precoCusto);
                addProduto.setValorVenda(precoVenda);
                addProduto.setComposto("N");
                addProduto = produtoService.salvarProduto(addProduto);
                itemSelecionado.getDadosProduto().setValor(precoCusto);
                itemSelecionado.getDadosProduto().setIdProduto(addProduto);
            }
            if (controleEdicao == null) {
                itemSelecionado.setControle(StringUtil.gerarStringAleatoria(8));
                listCompraProduto.add(itemSelecionado);
            } else {
                for (int i = 0; i < listCompraProduto.size(); i++) {
                    CompraProduto item = listCompraProduto.get(i);
                    if (item.getControle().equals(controleEdicao)) {
                        compraSelecionado.getListCompraProduto().set(i, itemSelecionado);
                        controleEdicao = null;
                        break;
                    }
                }
            }
            PrimeFaces.current().executeScript("PF('pnlItens').hide();");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void adicionarProduto() {
        adicionaProduto = adicionaProduto == false;
        addUnidade = unidadeMedidaService.buscarPorSigla("UN");
        addProduto = new Produto();
        addDescricao = "";
        estoqueInicial = 0d;
        precoCusto = 0d;
        precoVenda = 0d;
    }

    public void editItem(CompraProduto item) {
        controleEdicao = item.getControle();
        itemSelecionado = item;
    }

    public List<FormaPagamento> listaFormasPagamentoAtiva() {
        return formaPagamentoService.listarFormaDePagamentoAtivo();
    }

}
