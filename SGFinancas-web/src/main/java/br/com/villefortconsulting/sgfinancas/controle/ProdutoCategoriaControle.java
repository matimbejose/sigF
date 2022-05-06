package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoriaSubcategoria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CategoriaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoCategoriaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
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
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCategoriaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @Inject
    private CadastroControle cadastroControle;

    private ProdutoCategoria produtoCategoriaSelecionado;

    private ProdutoCategoriaSubcategoria produtoCategoriaSubcategoriaSelecionado;

    private LazyDataModel<ProdutoCategoria> model;

    private ProdutoCategoriaFiltro filtro = new ProdutoCategoriaFiltro();

    private CategoriaDTO dtoCadastro;

    private String controleEdicao;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, produtoService::quantidadeRegistrosFiltrados, produtoService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CATEGORIA.getChave()).getDescricao());
        return "/restrito/produtoCategoria/cadastroCategoria.xhtml";
    }

    public List<ProdutoCategoria> getProdutos() {
        return produtoService.listarCategoria();
    }

    @Override
    public void cleanCache() {
        produtoCategoriaSubcategoriaSelecionado = new ProdutoCategoriaSubcategoria();
    }

    public String doStartAdd() {
        cleanCache();
        setProdutoCategoriaSelecionado(new ProdutoCategoria());
        return "/restrito/produtoCategoria/cadastroCategoria.xhtml";
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(prod -> produtoService.importDto((CategoriaDTO) prod, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/produtoCategoria/cadastroCategoria.xhtml";
    }

    public String doFinishAdd() {
        try {
            produtoCategoriaSelecionado.setAtivo("S");
            produtoService.salvarCategoria(produtoCategoriaSelecionado);
            createFacesSuccessMessage("Categoria salvo com sucesso!");
            return "/restrito/produtoCategoria/listaProdutoCategoria.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/produtoCategoria/cadastroCategoria.xhtml";
        }
    }

    public String doFinishExcluir() {
        produtoCategoriaSelecionado.setAtivo("N");
        produtoService.salvarCategoria(produtoCategoriaSelecionado);
        createFacesSuccessMessage("A categoria foi removida com sucesso!");
        return "/restrito/produtoCategoria/listaProdutoCategoria.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return produtoService.getDadosAuditoriaProdutoCategoriaByID(produtoCategoriaSelecionado);
    }

    public String doShowAuditoria() {
        return "/restrito/produtoCategoria/listaAuditoriaProdutoCategoria.xhtml";
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CATEGORIA_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Categoria de produto/serviço",
                    produtoService.hasAnyProdutoCategoria(),
                    true,
                    this::doStartAdd,
                    this::doFinishImport,
                    CategoriaDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new CategoriaDTO();
    }

    public void editItem(ProdutoCategoriaSubcategoria item) {
        controleEdicao = item.getControleEdicao();
        produtoCategoriaSubcategoriaSelecionado = item;
    }

    public void removeItem(ProdutoCategoriaSubcategoria item) {
        produtoCategoriaSelecionado.getListProdutoCategoriaSubcategoria().remove(item);
        produtoCategoriaSubcategoriaSelecionado = new ProdutoCategoriaSubcategoria();
    }

    public void addItem() {
        if (produtoCategoriaSubcategoriaSelecionado.getDescricao() == null || produtoCategoriaSubcategoriaSelecionado.getDescricao().trim().isEmpty()) {
            createFacesErrorMessage("Informe a descrição");
            return;
        }
        if (controleEdicao == null) {
            produtoCategoriaSubcategoriaSelecionado.setControleEdicao(StringUtil.gerarStringAleatoria(8));
            produtoCategoriaSelecionado.getListProdutoCategoriaSubcategoria().add(produtoCategoriaSubcategoriaSelecionado);
        } else {
            for (int i = 0; i < produtoCategoriaSelecionado.getListProdutoCategoriaSubcategoria().size(); i++) {
                ProdutoCategoriaSubcategoria item = produtoCategoriaSelecionado.getListProdutoCategoriaSubcategoria().get(i);
                if (item.getControleEdicao().equals(controleEdicao)) {
                    produtoCategoriaSelecionado.getListProdutoCategoriaSubcategoria().set(i, produtoCategoriaSubcategoriaSelecionado);
                    controleEdicao = null;
                    break;
                }
            }
        }
        produtoCategoriaSubcategoriaSelecionado = new ProdutoCategoriaSubcategoria();
        controleEdicao = null;
        PrimeFaces.current().executeScript("PF('pnlItens').hide();");
    }

    public void resetSubcategoria() {
        produtoCategoriaSubcategoriaSelecionado = new ProdutoCategoriaSubcategoria();
    }

}
