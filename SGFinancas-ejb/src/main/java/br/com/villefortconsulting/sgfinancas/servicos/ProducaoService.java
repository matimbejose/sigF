package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Estoque;
import br.com.villefortconsulting.sgfinancas.entidades.Producao;
import br.com.villefortconsulting.sgfinancas.entidades.ProducaoProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProducaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ProducaoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumOrigemEstoque;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusProducao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProducaoService extends BasicService<Producao, ProducaoRepositorio, ProducaoFiltro> {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private EstoqueService estoqueService;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ProducaoRepositorio(em, adHocTenant);
    }

    @Override
    public Producao adicionar(Producao producao) {
        if (producao.getProducaoProdutoList().isEmpty()) {
            throw new CadastroException("Adicione no mínimo um produto para gerar produção.", null);
        }
        if (producao.getDataPedido() == null) {
            producao.setDataPedido(new Date());
        }
        producao.setStatus(EnumStatusProducao.NOVO.getTipo());
        producao.setNumero(repositorio.buscarProximoNumero());
        producao.setTenatID(adHocTenant.getTenantID());
        producao.getProducaoProdutoList().forEach(item -> {
            item.setTenatID(adHocTenant.getTenantID());
            item.setIdProducao(producao);
        });
        return super.adicionar(producao);
    }

    public Producao salvarAtualizarEstoque(Producao producao) {
        producao.setDataProducao(new Date());
        producao.getProducaoProdutoList().stream()
                .map(pP -> {
                    pP.setIdProducao(producao);
                    return pP;
                })
                .forEachOrdered(pP -> {
                    List<String> listaProdutosNaoProduziveis = listaProdutosNaoProduziveis(pP.getIdProduto(), pP.getQuantidade(), new Date());
                    if (listaProdutosNaoProduziveis.isEmpty()) {
                        Estoque estoque = estoqueService
                                .lancaProdutoEstoque(pP, EnumOrigemEstoque.ENTRADA_LANCADA.getOrigem());

                        // atualiza estoque em produto
                        pP.getIdProduto().setEstoqueDisponivel(estoque.getSaldo());
                        produtoService.alterar(pP.getIdProduto());

                        salvar(producao);
                        removeInsumosDoEstoque(pP.getIdProduto(), pP.getQuantidade());
                    } else {
                        throw new CadastroException("Não é possível produzir os seguintes produtos: " + StringUtil.listaParaTexto(listaProdutosNaoProduziveis), null);
                    }
                });
        return producao;
    }

    /**
     * Retorna a lista de todos os produtos que não podem ser produzidos por falta no estoque
     *
     * @param produto
     * @param quantidade
     * @param competencia
     * @return
     */
    private List<String> listaProdutosNaoProduziveis(Produto produto, Double quantidade, Date competencia) {
        List<String> lista = new ArrayList<>();
        if (produto.getComposto().equals("N")) {
            Double disponibilidade = estoqueService.buscarSaldo(competencia, produto);
            if (disponibilidade < quantidade) {
                lista.add(produto.getDescricao()
                        + "(necessário: " + NumeroUtil.converterDecimalParaString(quantidade, 2)
                        + ", disponível: " + NumeroUtil.converterDecimalParaString(disponibilidade, 2) + ")");
            }
        } else {
            List<String> listaIntermediaria = new ArrayList<>();
            produtoService.listarProdutosCompostosPorProduto(produto, true)
                    .forEach(pc -> listaIntermediaria.addAll(listaProdutosNaoProduziveis(pc.getIdProduto(), pc.getQuantidade() * quantidade, competencia)));
            if (!listaIntermediaria.isEmpty()) {
                lista.add(produto.getDescricao() + ", não há estoque dos seguintes produtos: " + StringUtil.listaParaTexto(listaIntermediaria));
            }
        }
        return lista;
    }

    private void removeInsumosDoEstoque(Produto produto, Double quantidade) {
        final Date dtHoje = DataUtil.getHoje();
        produtoService.listarProdutosCompostosPorProduto(produto, true).stream()
                .forEach(pc -> {
                    pc.setDataProducao(dtHoje);
                    pc.setQuantidadeFinal(quantidade * pc.getQuantidade());
                    Estoque estoque = estoqueService
                            .lancaProdutoEstoque(pc, EnumOrigemEstoque.SAIDA_LANCADA.getOrigem());
                    produto.setEstoqueDisponivel(estoque.getSaldoAnterior());
                    produtoService.alterar(produto);
                });
    }

    public List<Producao> criar(List<Producao> listaProducao) {
        List<Producao> retorno = new ArrayList<>();
        listaProducao.forEach(producao -> producao.getProducaoProdutoList()
                .forEach(producaoProduto -> {
                    Double estoque = estoqueService.buscarSaldo(DataUtil.getHoje(), producaoProduto.getIdProduto());
                    if (estoque < 0) {
                        retorno.add(salvar(producao));
                    }
                }));

        return retorno;
    }

    public List<Produto> listarProdutos() {
        Empresa empresa = empresaService.getEmpresa();
        return produtoService.listarProdutosPorEmpresa(empresa);
    }

    public List<ProducaoProduto> listarProdutos(Producao producao) {
        return repositorio.listarProdutos(producao);
    }

    @Override
    public Criteria getModel(ProducaoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addRestrictionTo(criteria, "dataPedido", filter.getDataPedido());
        addEqRestrictionTo(criteria, "idProduto", filter.getIdProduto());
        addEqRestrictionTo(criteria, "quantidade", filter.getQuantidade());
        addEqRestrictionTo(criteria, "numero", filter.getNumero());
        if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
            Criterion[] listTiposComposicao = new Criterion[filter.getStatus().size()];
            for (int i = 0; i < filter.getStatus().size(); i++) {
                listTiposComposicao[i] = Restrictions.eq("status", filter.getStatus().get(i));
            }
            criteria.add(Restrictions.or(listTiposComposicao));
        }

        return criteria;
    }

    public Producao baixaPedidoProducao(Producao producao) {
        producao.setDataProducao(DataUtil.getHoje());
        return alterar(producao);
    }

}
