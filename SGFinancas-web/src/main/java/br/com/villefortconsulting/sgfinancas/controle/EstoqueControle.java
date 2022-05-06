package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EstoqueFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EstoqueService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEstoque;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private RelatorioService relatorioService;

    private Estoque estoqueSelecionado;

    private Produto produtoSelecionado;

    private LazyDataModel<Produto> model;

    private LazyDataModel<Estoque> modelEstoque;

    private EstoqueFiltro filtro = new EstoqueFiltro();

    private ProdutoFiltro filtroProduto = new ProdutoFiltro();

    private String mostraImportacao = "N";

    private transient UploadedFile file;

    private EnumOrigemEstoque enumOrigemEstoque;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtroProduto, produtoService::quantidadeRegistrosFiltrados,
                produtoService::getListaFiltrada);
        modelEstoque = new VillefortDataModel<>(
                filtro,
                estoqueService::quantidadeRegistrosFiltrados,
                estoqueService::getListaFiltrada,
                filter -> {
                    filter.addPropriedadeOrdenacao("id");
                    filter.setProduto(produtoSelecionado);
                    filter.setAtivo("S");
                });
    }

    @Override
    public void cleanCache() {
        mostraImportacao = "N";
    }

    public List<Estoque> getEstoques() {
        return estoqueService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        estoqueSelecionado = new Estoque();
        estoqueSelecionado.setData(new Date());
        estoqueSelecionado.setPrimeiroCadastro("N");
        return "cadastroEstoque.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroEstoque.xhtml";
    }

    public String doFinishAdd() {
        try {
            estoqueService.salvar(estoqueSelecionado);
            createFacesSuccessMessage("Estoque salvo com sucesso!");
            return "listaExtratoEstoque.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage("Ocorreu um erro ao salvar o estoque.");
            return "cadastroEstoque.xhtml";
        }
    }

    public String doFinishExcluir() {
        Estoque novo = estoqueSelecionado.clone();
        novo.inverterMovimento();
        novo.setTipoOperacao(EnumTipoEstoque.REMOVIDO.getTipo());// Marcado como removido para não exibir na listagem
        estoqueSelecionado.setTipoOperacao(EnumTipoEstoque.REMOVIDO.getTipo());
        estoqueService.salvar(estoqueSelecionado);
        estoqueService.salvar(novo);
        return "listaExtratoEstoque.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaEstoque.xhtml";
    }

    public String doShowHistorico() {
        return "/restrito/estoque/listaHistoricoEstoque.xhtml";
    }

    public String buscarTipo(String tipo) {
        return EnumTipoEstoque.getDescricao(tipo);
    }

    public void esconderImportacao() {
        mostraImportacao = "N";
    }

    @Override
    public String mudaSituacaoImportacao() {

        return "importarEstoque.xhtml";
    }

    public StreamedContent downloadPlanilha() {
        try {
            return relatorioService.downloadModeloProdutosEstoque();
        } catch (FileException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return null;
    }

    public void processaArquivo(FileUploadEvent event) throws FileException {
        UploadedFile uploadedFile = event.getFile();

        try {
            relatorioService.lerArquivoImportacaoEstoque(uploadedFile);

            createFacesSuccessMessage("Arquivo importado com sucesso!");
            mostraImportacao = "N";

        } catch (IOException ex) {
            createFacesErrorMessage("Falha ao importar o arquivo.");
        }
    }

    public List<Estoque> getListaEstoquePorProduto() {
        return estoqueService.listarEstoquePorProduto(produtoSelecionado);
    }

    public List<Object> getDadosAuditoria() {
        return estoqueService.getDadosAuditoriaByID(estoqueSelecionado);
    }

    public String doStartProcessamento() {
        if (file == null) {
            createFacesErrorMessage("Ocorreu um erro ao enviar o arquivo, caso o problema ocorra novamente, entre em contato com o suporte!");
            return "importarEstoque.xhtml";
        }
        if (!file.getFileName().toLowerCase().endsWith(".csv")) {
            createFacesErrorMessage("Atenção, é necessário enviar um arquivo CSV.");
            return "importarEstoque.xhtml";
        }

        if (StringUtils.isNotEmpty(file.getFileName())) {
            try {
                relatorioService.lerArquivoImportacaoEstoque(file);

                createFacesSuccessMessage("Arquivo importado com sucesso!");
                return "listaEstoque.xhtml";
            } catch (Exception ex) {
                createFacesErrorMessage("Erro ao processar arquivo com as informações do produto.");
                return "importarEstoque.xhtml";
            }
        } else {
            createFacesErrorMessage("Informe o arquivo com os produtos antes de prosseguir.");
            return "importarEstoque.xhtml";
        }
    }

    public String retornarDescricaoOrigem(String enumOrigem) {

        if (enumOrigem != null) {
            return EnumOrigemEstoque.retornaEnumSelecionado(enumOrigem).getDescricao();
        }

        return "";
    }

}
