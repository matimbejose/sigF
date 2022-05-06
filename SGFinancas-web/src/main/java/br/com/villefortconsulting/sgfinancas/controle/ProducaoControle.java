package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Producao;
import br.com.villefortconsulting.sgfinancas.entidades.ProducaoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProducaoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ProducaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusProducao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProducaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ProducaoService producaoService;

    @EJB
    private ProdutoService produtoService;

    private Producao producaoSelecionado;

    @SuppressWarnings("unused")
    private List<Produto> produtos = new ArrayList<>();

    private LazyDataModel<Producao> model;

    private ProducaoFiltro filtro = new ProducaoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                producaoService::quantidadeRegistrosFiltrados,
                producaoService::getListaFiltrada,
                filter -> filter.setUsuarioLogado(getUsuarioLogado()));
        filtro.getDataPedido().setMin(DataUtil.getPrimeiroDiaDoMes(new Date()));
        filtro.getDataPedido().setMax(DataUtil.getUltimoDiaDoMes(new Date()));
    }

    public String doStartAdd() {
        produtos = producaoService.listarProdutos();
        producaoSelecionado = new Producao();
        producaoSelecionado.setStatus(EnumStatusProducao.NOVO.getTipo());
        producaoSelecionado.setProducaoProdutoList(new ArrayList<>());

        return "/restrito/producao/cadastroProducao.xhtml";
    }

    public String doStartAlterar() {
        produtos = producaoService.listarProdutos();
        producaoSelecionado.setProducaoProdutoList(producaoService.listarProdutos(producaoSelecionado));

        return "/restrito/producao/cadastroProducao.xhtml";
    }

    public String doFinishAdd() {
        try {
            producaoService.adicionar(producaoSelecionado);

            createFacesSuccessMessage("Produção salva com sucesso!");
            return "/restrito/producao/listaProducao.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/producao/cadastroProducao.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/producao/listaAuditoriaProducao.xhtml";
    }

    public String gerarProducao() {
        try {
            if (!producaoSelecionado.getStatus().equals(EnumStatusProducao.PRODUZIDO.getTipo())) {
                producaoSelecionado.setDataProducao(new Date());
                producaoSelecionado.setStatus(EnumStatusProducao.PRODUZIDO.getTipo());
                producaoSelecionado.setProducaoProdutoList(producaoService.listarProdutos(producaoSelecionado));
                producaoService.salvarAtualizarEstoque(producaoSelecionado);
                createFacesSuccessMessage("Produção gerada com sucesso!");
            }
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
        }
        return "/restrito/producao/listaProducao.xhtml";
    }

    public void removeProduto(ProducaoProduto prod) {
        for (ProducaoProduto pp : producaoSelecionado.getProducaoProdutoList()) {
            if ((pp.getIdProduto() != null && pp.getIdProduto().equals(prod.getIdProduto()))
                    || pp.getControle().equals(prod.getControle())) {
                producaoSelecionado.getProducaoProdutoList().remove(pp);
                break;
            }
        }
    }

    public void addProduto() {
        ProducaoProduto pp = new ProducaoProduto();
        pp.setControle(StringUtil.gerarNumeroAleatorio(10));
        producaoSelecionado.getProducaoProdutoList().add(pp);
    }

    public List<Produto> getProdutos() {
        ProdutoFiltro pf = new ProdutoFiltro();
        pf.setTipoComposicao(new ArrayList<>());
        pf.getTipoComposicao().add(EnumTipoProdutoVenda.COMPOSTO.getTipo());
        return produtoService.getListaFiltrada(pf);
    }

    public List<Object> getDadosAuditoria() {
        return producaoService.getDadosAuditoriaByID(producaoSelecionado);
    }

}
