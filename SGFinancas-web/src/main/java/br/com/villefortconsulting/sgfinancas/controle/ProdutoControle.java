package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoriaSubcategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoComposicao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CompraService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoComposicaoProduto;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private FornecedorService fornecedorService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private NcmService ncmService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @Inject
    private CadastroControle cadastroControle;

    @Inject
    private DocumentoMapper documentoMapper;

    @Inject
    private VendaControle vendaControle;

    @EJB
    private CompraService compraService;

    private Produto produtoSelecionado;

    private ProdutoComposicao produtoComposicaoSelecionado;

    private Fornecedor fornecedorSelecionado;

    private FornecedorProduto fornecedorProdutoSelecionado;

    private List<FornecedorProduto> listFornecedorProduto;

    private LazyDataModel<Produto> model;

    private ProdutoFiltro filtro = new ProdutoFiltro();

    @NotNull(message = "Informe o CPF/CNPJ")
    @Temporal(TemporalType.TIMESTAMP)
    private String cpfCnpj;

    @NotNull(message = "Informe a descricao.")
    private String descricao;

    private String cadastraCategoria;

    private ProdutoCategoria categoriaCadastrada;

    private transient UploadedFile part;

    private Double estoqueAtual;

    private boolean precisaAtualizarEstoque;

    private transient List<AnexoDTO> listaAnexos;

    private ProdutoDTO dtoCadastro;

    private List<CompraProduto> compraProdutoList;

    private boolean usaPrecoCalculado = false;

    private Double custoCalculado;

    private Double vendaCalculado = 0d;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, produtoService::quantidadeRegistrosFiltrados, produtoService::getListaFiltrada);
    }

    @Override
    public void cleanCache() {
        precisaAtualizarEstoque = false;
        part = null;
        cleanFornecedor();
        compraProdutoList = new ArrayList<>();
        listaAnexos = new ArrayList<>();
        cadastraCategoria = "N";
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_PRODUTO.getChave()).getDescricao());
        return "/restrito/produto/cadastroProduto.xhtml";
    }

    public String dataMaxima() {
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        return out.format(DataUtil.getHoje());
    }

    public List<Produto> getProdutos() {
        return produtoService.listarProdutos();
    }

    public List<Produto> getProdutosCompostos() {
        return produtoService.listarProdutosCompostos();
    }

    public List<ProdutoCategoria> getProdutosCategoria() {
        return produtoService.listarCategoriaAtiva();
    }

    public String doStartAdd() {
        cleanCache();
        produtoSelecionado = new Produto();
        if (produtoSelecionado.getProdutoComposicaoList() == null) {
            produtoSelecionado.setProdutoComposicaoList(new ArrayList<>());
        }

        return "/restrito/produto/cadastroProduto.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        compraProdutoList = compraService.getComprasProdutoList(produtoSelecionado);
        // carregar fornecedores
        produtoSelecionado.setProdutoFornecedorList(fornecedorService.listarFornecedorProduto(produtoSelecionado));
        produtoSelecionado.setProdutoComposicaoList(produtoService.listarProdutosCompostosPorProduto(produtoSelecionado, true));

        if (produtoSelecionado.getIdDocumento() != null && produtoSelecionado.getIdDocumento().getId() != null) {
            produtoSelecionado.getIdDocumento().setDocumentoAnexoList(documentoAnexoService.listByDocumento(produtoSelecionado.getIdDocumento()));
        }

        estoqueAtual = produtoSelecionado.getEstoqueDisponivel();
        if (produtoSelecionado.getIdDocumento() != null && !produtoSelecionado.getIdDocumento().getDocumentoAnexoList().isEmpty()) {
            listaAnexos = produtoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                    .map(documentoMapper::toDTO)
                    .collect(Collectors.toList());
        }

        return "/restrito/produto/cadastroProduto.xhtml";
    }

    public String doFinishAdd() {
        try {
            if ("S".equals(produtoSelecionado.getUsaPrecoCalculado())) {
                produtoSelecionado.setValorCusto(getCustoCalculado());
                produtoSelecionado.setValorVenda(getVendaCalculado());
            }
            if (cadastraCategoria.equals("S")) {
                produtoSelecionado.setIdProdutoCategoria(produtoService.adicionarCategoria(categoriaCadastrada));
            }
            // verifica se informou o codigo de barras e valida-o
            if (StringUtils.isNotEmpty(produtoSelecionado.getCodigoBarra())) {
                if (!produtoSelecionado.getCodigoBarra().equals("SEM GTIN") && !NumeroUtil.isCodigoBarraEANValido(produtoSelecionado.getCodigoBarra())) {
                    createFacesErrorMessage("O código de barras informado não é válido! Favor informar com o formato EAN8 ou EAN13.");
                    return "/restrito/produto/cadastroProduto.xhtml";
                }
            } else {
                produtoSelecionado.setCodigoBarra("SEM GTIN");
            }

            if (listaAnexos != null) {
                Documento doc;
                if (produtoSelecionado.getIdDocumento() == null) {
                    doc = documentoService.criarDocumento(getUsuarioLogado(), "Imagens do produto");
                } else {
                    doc = documentoService.buscar(produtoSelecionado.getIdDocumento().getId());
                }
                produtoSelecionado.setIdDocumento(doc);
                documentoAnexoService.persistirAnexoDTO(doc, getUsuarioLogado(), listaAnexos);
            }

            produtoSelecionado.getProdutoComposicaoList()
                    .forEach(pc -> pc.setIdProdutoOrigem(produtoSelecionado));

            produtoService.salvarProduto(produtoSelecionado);

            vendaControle.setItensVendaDisponiveis(null);

            createFacesSuccessMessage("Produto salvo com sucesso!");
            return "/restrito/produto/listaProduto.xhtml";
        } catch (CadastroException e) {
            Logger.getLogger(ProdutoControle.class.getName()).log(Level.SEVERE, null, e);
            createFacesErrorMessage(e.getMessage());
            return "/restrito/produto/cadastroProduto.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(ProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/produto/cadastroProduto.xhtml";
        }
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> produtoService.importDto((ProdutoDTO) prod, getUsuarioLogado()))
                .collect(Collectors.toList());
    }

    public String doFinishExcluir() {
        try {
            produtoSelecionado.setAtivo("N");
            produtoService.salvarProduto(produtoSelecionado);
            createFacesSuccessMessage("Produto excluído com sucesso.");
            return "/restrito/produto/listaProduto.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/produto/listaProduto.xhtml";
        }
    }

    public void doShowHistoricoEstoque() {
        Map<String, Object> options = new HashMap<>();
        options.put("resizable", true);
        options.put("height", 600);
        options.put("width", 1100);
        showModal("/restrito/estoque/modals/mostraHistoricoEstoque.xhtml", options);
    }

    public List<Fornecedor> buscarFornecedores(String descricao) {
        List<Fornecedor> fornecedoresDisponiveis = fornecedorService.listar(descricao);
        if (produtoSelecionado.getProdutoFornecedorList() != null && !produtoSelecionado.getProdutoFornecedorList().isEmpty()) {
            List<Fornecedor> fornecedoresAtribuidos = produtoSelecionado.getProdutoFornecedorList().stream()
                    .map(FornecedorProduto::getIdFornecedor)
                    .distinct()
                    .collect(Collectors.toList());
            fornecedoresDisponiveis.removeAll(fornecedoresAtribuidos);
        }

        return fornecedoresDisponiveis;
    }

    public List<Ncm> listarNcmPorSecao() {
        //Busca os ncmDisponiveis
        return ncmService.listar();
    }

    public List<Ncm> buscarNcm(String descricao) {
        return ncmService.conferirCodigo(descricao);
    }

    public void cleanFornecedor() {
        fornecedorSelecionado = null;
    }

    public List<Fornecedor> getFornecedores() {
        List<Fornecedor> fornecedoresDisponiveis = fornecedorService.listarFornecedor();
        List<Fornecedor> fornecedoresAtribuidos = produtoSelecionado.getProdutoFornecedorList().stream()
                .map(FornecedorProduto::getIdFornecedor)
                .collect(Collectors.toList());

        if (StringUtil.verificarListEmpty(fornecedoresDisponiveis) && StringUtil.verificarListEmpty(fornecedoresAtribuidos)) {
            fornecedoresDisponiveis.removeAll(fornecedoresAtribuidos);
        }
        return fornecedoresDisponiveis;
    }

    public void addFornecedor() {

        List<Fornecedor> list = produtoSelecionado.getProdutoFornecedorList().stream()
                .map(FornecedorProduto::getIdFornecedor)
                .collect(Collectors.toList());
        // Verifica se preencheu o fornecedor
        if (fornecedorSelecionado == null || list.contains(fornecedorSelecionado)) {
            return;
        }

        fornecedorProdutoSelecionado = new FornecedorProduto();
        produtoService.atualizaTenat(fornecedorProdutoSelecionado);
        fornecedorProdutoSelecionado.setIdFornecedor(fornecedorSelecionado);

        produtoSelecionado.addFornecedor(fornecedorProdutoSelecionado);

        cleanFornecedor();
    }

    public void removeFornecedor() {
        produtoSelecionado.removeFornecedor(fornecedorProdutoSelecionado);
    }

    public List<Object> getDadosAuditoria() {
        return produtoService.getDadosAuditoriaByID(produtoSelecionado);
    }

    public String doShowAuditoria() {
        return "/restrito/produto/listaAuditoriaProduto.xhtml";
    }

    public void doToggleAddCategoria() {
        cadastraCategoria = cadastraCategoria.equals("S") ? "N" : "S";
        if (cadastraCategoria.equals("S")) {
            categoriaCadastrada = new ProdutoCategoria();
            categoriaCadastrada.setTenatID(getUsuarioLogado().getTenat());
        }
    }

    public List<AnexoDTO> getListaAnexos() {
        List<AnexoDTO> lista = new ArrayList<>();
        lista.addAll(listaAnexos);
        lista.add(new AnexoDTO());
        return lista;
    }

    public void setPart(FileUploadEvent event) {
        AnexoDTO anexo = new AnexoDTO();
        try {
            anexo.setConteudo("data:image/png;base64," + Base64.getEncoder().encodeToString(IOUtils.toByteArray(event.getFile().getInputstream())));
            anexo.setDataEnvio(new Date());
            anexo.setIdUsuarioEnvio(getUsuarioLogado().getId());
            anexo.setMimeType(event.getFile().getContentType());
            anexo.setNome(event.getFile().getFileName());
            listaAnexos.add(anexo);
        } catch (IOException ex) {
            Logger.getLogger(ProdutoControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removerAnexo(AnexoDTO adto) {
        listaAnexos.remove(adto);
    }

    public void adicionaItemComposto() {
        produtoSelecionado.getProdutoComposicaoList().add(new ProdutoComposicao());
    }

    public void removeItemComposto() {
        produtoSelecionado.getProdutoComposicaoList().remove(produtoComposicaoSelecionado);
    }

    public void setCusto(ProdutoComposicao item) {
        item.setCusto(NumeroUtil.somar(0d, item.getIdProduto().getValorCusto()));
    }

    public void updateValoresProduto() {
        if (produtoSelecionado.getComposto() == null || EnumTipoComposicaoProduto.SEM_COMPOSICAO.getTipo().equals(produtoSelecionado.getComposto())) {
            return;
        }
        Double valor = 0d;
        Double custo = 0d;
        for (ProdutoComposicao pc : produtoSelecionado.getProdutoComposicaoList()) {
            pc.setCusto(NumeroUtil.somar(0d, pc.getIdProduto().getValorCusto()) * pc.getQuantidade());
            pc.setPreco(NumeroUtil.somar(0d, pc.getIdProduto().getValorVenda()) * pc.getQuantidade());
            custo += NumeroUtil.somar(0d, pc.getCusto());
            valor += NumeroUtil.somar(0d, pc.getPreco());
        }
        if (EnumTipoComposicaoProduto.KIT_DE_PRODUTO.getTipo().equals(produtoSelecionado.getComposto())) {
            produtoSelecionado.setValorVenda(valor);
            produtoSelecionado.setValorCusto(custo);
        }
        if (EnumTipoComposicaoProduto.PRODUTO_COMPOSTO.getTipo().equals(produtoSelecionado.getComposto())) {
            produtoSelecionado.setCustoVariavel(custo);
        }
    }

    public void updateEstoque() {
        precisaAtualizarEstoque = true;
    }

    public Double getCustoMedio() {
        return compraProdutoList.stream()
                .mapToDouble(cp -> cp.getDadosProduto().getValor())
                .sum() / compraProdutoList.size();
    }

    public void limpaSubcategoria() {
        produtoSelecionado.setIdProdutoCategoriaSubcategoria(null);
    }

    public List<ProdutoCategoriaSubcategoria> getProdutosCategoriaSubcategoria() {
        if (produtoSelecionado.getIdProdutoCategoria() == null) {
            return new ArrayList<>();
        }
        return produtoService.listarSubcategoria(produtoSelecionado.getIdProdutoCategoria());
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("PRODUTO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Produto",
                    produtoService.hasAnyProduto(),
                    false,
                    this::doStartAdd,
                    this::doFinishImport,
                    ProdutoDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new ProdutoDTO();
    }

    public Double getCustoCalculado() {
        if (produtoSelecionado.getUsaPrecoCalculado() != null && produtoSelecionado.getUsaPrecoCalculado().equals("S")) {
            return NumeroUtil.somar(produtoSelecionado.getCustoFixo(), produtoSelecionado.getCustoVariavel());
        }
        return 0d;
    }

    public Double getVendaCalculado() {
        if (produtoSelecionado.getUsaPrecoCalculado() != null && produtoSelecionado.getUsaPrecoCalculado().equals("S")) {
            Double markup = produtoSelecionado.getMarkup() != null ? produtoSelecionado.getMarkup() : 0d;
            return ((getCustoCalculado() / 100) * markup) + getCustoCalculado();
        }
        return 0d;
    }

    public String getInformacao() {
        return "Para compor o preço de custo e venda do produto é obrigatório: <br/>"
                + "1-Cadastrar composição (Produto composto), ou informar o valor do custo variável. <br/>"
                + "2-Informar Custo fixo (Custo fixo total mensal/Custo de produção mensal). Após cadastro inicial, sistema calculará automaticamente. <br/>"
                + "3-Informar percentual de Margem de Lucro. Deverá ser alterado manualmente, quando necessário";
    }

    public void atualizaValores() {
        if (produtoSelecionado.getProdutoComposicaoList() == null) {
            return;
        }

        produtoSelecionado.getProdutoComposicaoList().stream()
                .filter(pc -> pc.getIdProduto() != null)
                .forEach(pc -> {
                    pc.setPreco(pc.getIdProduto().getValorVenda());
                    pc.setCusto(pc.getIdProduto().getValorCusto());
                    produtoSelecionado.setCustoVariavel(pc.getPreco());
                });
        updateValoresProduto();
    }

}
