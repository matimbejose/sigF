package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorProduto;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoriaSubcategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoComposicao;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeMedida;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CategoriaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ProdutoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.embeddable.DadosProduto;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.ProdutoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoCategoriaFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ProdutoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ProdutoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoCadastro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoProduto;
import br.com.villefortconsulting.util.DataUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProdutoService extends BasicService<Produto, ProdutoRepositorio, ProdutoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private VendaService vendaService;

    @EJB
    private NcmService ncmService;

    @EJB
    private PlanoContaService planoContaPadraoService;

    @EJB
    private EstoqueService estoqueService;

    @EJB
    private CompraService compraService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @Inject
    private transient ProdutoMapper produtoMapper;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new ProdutoRepositorio(em, adHocTenant);
    }

    public Produto salvarProduto(Produto produto) {
        PlanoConta planoConta;
        boolean nomeRepetido = repositorio.list().stream()
                .filter(p -> p.getDescricao().equals(produto.getDescricao()))
                .anyMatch(p -> produto.getId() == null || !produto.getId().equals(p.getId()));

        if (nomeRepetido) {
            throw new CadastroException("Já existe um produto com o nome " + produto.getDescricao() + ". Favor alterar o nome.", null);
        }
        if (produto.getCodigo() == null || produto.getCodigo().trim().isEmpty()) {
            if (produto.getId() != null) {
                String cod = buscar(produto.getId()).getCodigo();
                if (cod != null && !cod.trim().isEmpty()) {
                    throw new CadastroException("Não é possível remover o código do produto.", null);
                }
            }
            produto.setCodigo(getProximoCodigo());
        }
        if (produto.getProdutoComposicaoList() != null) {
            boolean temCustoNulo = produto.getProdutoComposicaoList().stream()
                    .anyMatch(pc -> pc.getCusto() == null);
            boolean temPrecoNulo = produto.getProdutoComposicaoList().stream()
                    .anyMatch(pc -> pc.getPreco() == null);
            if (temCustoNulo || temPrecoNulo) {
                String err = "Para salvar o produto " + produto.getDescricao() + " é necessário preencher o ";
                if (temCustoNulo) {
                    err += "valor de custo";
                }
                if (temPrecoNulo) {
                    if (temCustoNulo) {
                        err += " e o ";
                    }
                    err += "valor de venda";
                }
                err += " de toda a composição do produto";
                throw new CadastroException(err, null);
            }
        }
        produto.setCodigo(produto.getCodigo().replaceAll("\\D", ""));
        if (produto.getComposto() == null) {
            produto.setComposto("N");
        }
        if (produto.getId() == null) {
            produto.setTipo(EnumTipoProduto.PRODUTO.getTipo());

            if (produto.getDataEntrada() == null) {
                produto.setDataEntrada(DataUtil.getHoje());
            }

            if (produto.getEstoqueDisponivel() == null) {
                produto.setEstoqueDisponivel(0d);
            }

            planoConta = planoContaPadraoService.criaPlanoContaCadastroBasico(produto.getDescricao(), EnumTipoCadastro.PRODUTO.getTipo());
            produto.setIdPlanoConta(planoConta);

            adicionar(produto);
            adicionarProdutoEstoque(produto);
            return produto;
        } else {
            estoqueService.adicionarEstoqueImportacao(produto, produto.getEstoqueDisponivel());

            if (produto.getIdPlanoConta() != null) {
                produto.getIdPlanoConta().setDescricao(produto.getDescricao());
                planoContaPadraoService.alterar(produto.getIdPlanoConta());
            } else {
                planoConta = planoContaPadraoService
                        .criaPlanoContaCadastroBasico(produto.getDescricao(),
                                EnumTipoCadastro.PRODUTO.getTipo());
                produto.setIdPlanoConta(planoConta);
            }

            return alterar(produto);
        }
    }

    private void adicionarProdutoEstoque(Produto produto) {
        estoqueService.criarEstoqueInicial(produto);
    }

    private void removerProdutoEstoque(Produto produto) {
        estoqueService.excluirProdutoEstoque(produto);
    }

    public Produto adicionarPorCompraProduto(CompraProduto compraProduto) {
        Produto produto = new Produto();
        produto.setTipo("P");
        produto.setDescricao(compraProduto.getDescricaoProdutoXML());
        produto.setValorVenda(compraProduto.getDadosProduto().getValor());
        produto.setValorCusto(compraProduto.getDadosProduto().getValor());
        produto.setIdUnidadeMedida(compraProduto.getIdUnidadeMedida());
        produto.setComposto("N");
        if (!"SEM GTIN".equals(compraProduto.getCodigoBarras()) && compraProduto.getCodigoBarras() != null) {
            produto.setCodigoBarra(compraProduto.getCodigoBarras());
        }
        produto.setIdNcm(ncmService.buscarPorNcm(compraProduto.getNcm()));
        produto.setDataEntrada(compraProduto.getIdCompra().getDataCompra());
        produto.setEstoqueDisponivel(0d);

        return salvarProduto(produto);
    }

    @Override
    public void remover(Produto produto) {

        //Lista todas as vendas e compras que tem aquele produto em específico.
        List<VendaProduto> listVendaProduto = vendaService.vendasPorProduto(produto);
        List<CompraProduto> listCompraProduto = compraService.comprasPorProduto(produto);

        //Se as duas listas vierem vázias, deixa remover os mesmos.
        if (listVendaProduto.isEmpty() && listCompraProduto.isEmpty()) {

            PlanoConta planoConta = planoContaPadraoService.buscar(produto.getIdPlanoConta().getId());

            if (planoConta != null) {
                planoContaPadraoService.remover(planoConta);
            }

            removerProdutoEstoque(produto);

            remover(produto);

        } else { //Caso o contrário, lança uma exception.
            throw new CadastroException("Produto associado a outros cadastros. Favor desassociar o mesmo antes de tentar remove-lo.", null);
        }
    }

    public Produto buscar(String descricao) {
        return repositorio.buscar(descricao);
    }

    public Produto buscarPorCodigoBarras(String codigoBarras) {
        return repositorio.buscarPorCodigoBarras(codigoBarras);
    }

    public Produto buscarPorCodigoNCM(String codigoNCM) {
        return repositorio.buscarPorCodigoNCM(codigoNCM);
    }

    public List<Produto> listarParaVenda() {
        return repositorio.listar(parametroSistemaService.getParametro().getTipoProdutoVendaList());
    }

    public List<Produto> listarParaCompra() {
        return repositorio.listar(parametroSistemaService.getParametro().getTipoProdutoCompraList());
    }

    public List<Produto> listarProdutosCompostos() {
        return repositorio.listarProdutosCompostos();
    }

    public List<Produto> listar(String descricao) {
        return repositorio.listar(descricao);
    }

    public List<Produto> listarProdutosDoFornecedor(Fornecedor fornecedor) {
        return repositorio.listarProdutosDoFornecedor(fornecedor);
    }

    public Produto buscarServico(String descricao) {
        return repositorio.buscarServico(descricao);
    }

    public List<Produto> listarServicos() {
        return repositorio.listarServicos();
    }

    public List<Produto> listarProdutos() {
        return repositorio.listarProdutos();
    }

    public List<Produto> listarProdutosPorEmpresa(Empresa empresa) {
        return repositorio.listarProdutoPorEmpresa(empresa);
    }

    @Override
    public Criteria getModel(ProdutoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        criteria.add(Restrictions.eq("tipo", "P"));

        if (filter.getTipoComposicao() != null && !filter.getTipoComposicao().isEmpty()) {
            Criterion[] listTiposComposicao = new Criterion[filter.getTipoComposicao().size()];
            for (int i = 0; i < filter.getTipoComposicao().size(); i++) {
                listTiposComposicao[i] = Restrictions.eq("composto", filter.getTipoComposicao().get(i));
            }
            criteria.add(Restrictions.or(listTiposComposicao));
        }

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);
        if (filter.getAtivo() != null && !filter.getAtivo().equals("A")) {
            addEqRestrictionTo(criteria, "ativo", filter.getAtivo());
        }
        addEqRestrictionTo(criteria, "codigo", filter.getCodigo());
        addEqRestrictionTo(criteria, "idProdutoCategoria", filter.getIdProdutoCategoria());
        addEqRestrictionTo(criteria, "idUnidadeMedida", filter.getIdUnidadeMedida());

        return criteria;
    }

    public ProdutoCategoria salvarCategoria(ProdutoCategoria categoriaProduto) {
        boolean nomeRepetido = listarCategoria().stream()
                .filter(pc -> pc.getDescricao().equals(categoriaProduto.getDescricao()))
                .anyMatch(pc -> categoriaProduto.getId() == null || !categoriaProduto.getId().equals(pc.getId()));

        if (nomeRepetido) {
            throw new CadastroException("Existe uma categoria cadastrada para a descrição informada.", null);
        }

        if (categoriaProduto.getListProdutoCategoriaSubcategoria() != null) {
            categoriaProduto.getListProdutoCategoriaSubcategoria().forEach(sc -> {
                sc.setTenatID(adHocTenant.getTenantID());
                sc.setIdProdutoCategoria(categoriaProduto);
            });
        }

        if (categoriaProduto.getId() == null) {
            return repositorio.adicionarCategoria(categoriaProduto);
        } else {
            return repositorio.alterarCategoria(categoriaProduto);
        }
    }

    public ProdutoCategoria adicionarCategoria(ProdutoCategoria categoriaProduto) {
        return repositorio.adicionarCategoria(categoriaProduto);
    }

    public void populaCategoriaInicial(Empresa empresa) {
        ProdutoCategoria categoria = new ProdutoCategoria();
        categoria.setAtivo("S");
        categoria.setDescricao("Geral");
        categoria.setTenatID(empresa.getTenatID());

        repositorio.adicionarCategoria(categoria);

    }

    public ProdutoCategoria alterarCategoria(ProdutoCategoria categoriaProduto) {
        return repositorio.alterarCategoria(categoriaProduto);
    }

    public void removerCategoria(ProdutoCategoria categoriaProduto) {
        repositorio.removerCategoria(categoriaProduto);
    }

    public ProdutoCategoria buscarCategoria(int id) {
        return repositorio.buscarCategoria(id);
    }

    public ProdutoCategoria buscarCategoria(String descricao) {
        return repositorio.buscarCategoria(descricao);
    }

    public List<ProdutoCategoria> listarCategoria() {
        return repositorio.listarCategoria();
    }

    public List<ProdutoCategoria> listarCategoriaAtiva() {
        return repositorio.listarCategoriaAtiva();
    }

    public int quantidadeRegistrosFiltrados(ProdutoCategoriaFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getCategoriaProdutoModel(filtro));
    }

    public List<ProdutoCategoria> getListaFiltrada(ProdutoCategoriaFiltro filtro) {
        return repositorio.getListaFiltrada(getCategoriaProdutoModel(filtro), filtro);
    }

    public Criteria getCategoriaProdutoModel(ProdutoCategoriaFiltro filtro) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(ProdutoCategoria.class);

        addIlikeRestrictionTo(criteria, "descricao", filtro.getDescricao(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "ativo", filtro.getAtivo());

        return criteria;
    }

    public List<Object> getDadosAuditoriaProdutoCategoriaByID(ProdutoCategoria obj) {
        return repositorio.getDadosAuditoriaByID(ProdutoCategoria.class, obj.getId());
    }

    public List<Produto> listarProdutos(Venda venda) {
        return vendaService.listarVendaProduto(venda).stream()
                .map(VendaProduto::getDadosProduto)
                .map(DadosProduto::getIdProduto)
                .collect(Collectors.toList());
    }

    public void atualizaTenat(FornecedorProduto fornecedorProduto) {
        repositorio.atualizaTenat(fornecedorProduto);
    }

    public void atualizarEstoqueImportacaoESalvarProduto(Produto produto, Double saldoInformado) {
        estoqueService.adicionarEstoqueImportacao(produto, saldoInformado);
        alterar(produto);
    }

    public boolean hasAnyProdutoCategoria() {
        return repositorio.hasAnyProdutoCategoria();
    }

    public boolean hasAnyProduto() {
        return repositorio.hasAnyProduto();
    }

    public Produto importDto(ProdutoDTO produtoDTO, Usuario usuario) {
        String tenatID = usuario.getTenat();
        Produto prod = produtoMapper.toEntity(produtoDTO, usuario);
        prod.setTenatID(tenatID);
        prod.setDataEntrada(produtoDTO.getDataEntradaImportacao());
        prod.setAtivo("S");
        ProdutoCategoria pc = buscarCategoria(produtoDTO.getCategoriaImportacao());
        if (pc == null) {
            pc = new ProdutoCategoria();
            pc.setDescricao(produtoDTO.getCategoriaImportacao());
            pc.setTenatID(tenatID);
            pc.setAtivo("S");
            pc = adicionarCategoria(pc);
        }
        prod.setIdProdutoCategoria(pc);

        UnidadeMedida un = unidadeMedidaService.buscar(produtoDTO.getUnidadeDeMedidaImportacao());
        if (un == null) {
            un = new UnidadeMedida();
            un.setDescricao(produtoDTO.getCategoriaImportacao());
            un.setSigla(produtoDTO.getCategoriaImportacao());
            if (un.getSigla().length() > 6) {
                un.setSigla(un.getSigla().substring(0, 6));
            }
            un.setTenatID(tenatID);
            un = unidadeMedidaService.adicionar(un);
        }
        prod.setIdUnidadeMedida(un);
        if (prod.getCodigo() == null || prod.getCodigo().trim().isEmpty()) {
            prod.setCodigo(getProximoCodigo());
        } else {
            try {
                Integer.parseInt(prod.getCodigo());
            } catch (NumberFormatException ex) {
                throw new CadastroException("Informe um código para o produto " + prod.getDescricao(), ex);
            }
        }
        return salvarProduto(prod);
    }

    public ProdutoCategoria importDto(CategoriaDTO categoriaDTO, String tenatID) {
        ProdutoCategoria produtoCategoria = produtoMapper.toEntity(categoriaDTO);
        produtoCategoria.setTenatID(tenatID);
        produtoCategoria.setAtivo("S");
        produtoCategoria.setListProdutoCategoriaSubcategoria(new ArrayList<>());
        return salvarCategoria(produtoCategoria);
    }

    public List<ProdutoComposicao> listarProdutosCompostosPorProduto(Produto produto, boolean retornarLista) {
        List<ProdutoComposicao> lista = repositorio.listarProdutosCompostosPorProduto(produto);
        if (lista == null && retornarLista) {
            return new ArrayList<>();
        }
        return lista;
    }

    public boolean validaUsoProduto(Produto prod, EnumTipoUsoProduto tipo) {
        List<TipoProdutoUso> usos = new ArrayList<>();
        if (tipo == EnumTipoUsoProduto.COMPRA) {
            usos = parametroSistemaService.getParametro().getTipoProdutoCompraList();
        } else if (tipo == EnumTipoUsoProduto.VENDA) {
            usos = parametroSistemaService.getParametro().getTipoProdutoVendaList();
        }
        if (usos.isEmpty()) {
            return true;
        }

        EnumTipoProdutoVenda etpv = EnumTipoProdutoVenda.retornaEnumSelecionado(prod.getComposto());
        if (etpv == EnumTipoProdutoVenda.NORMAL && !repositorio.buscaUsoProduto(prod).isEmpty()) {
            etpv = EnumTipoProdutoVenda.INSUMO;
        }
        final EnumTipoProdutoVenda finalEtpv = etpv;
        return usos.stream()
                .filter(item -> item.getTipoProduto().equals(finalEtpv.getTipo()))
                .findAny()
                .orElse(null) != null;
    }

    public void atualizarQuantidadeEstoque(List<VendaProduto> listarVendaProduto) {
        listarVendaProduto.forEach(vendaProduto -> atualizarQuantidadeEstoque(vendaProduto.getDadosProduto().getIdProduto()));
    }

    public void atualizarQuantidadeEstoque(Produto produto) {
        Double quantidadeDisponivel = estoqueService.buscarSaldo(DataUtil.getHoje(), produto);

        produto.setEstoqueDisponivel(quantidadeDisponivel);

        alterar(produto);

    }

    public List<Produto> listarProdutosAtivos() {
        return repositorio.listarProdutosAtivos();
    }

    public String getProximoCodigo() {
        return "" + (repositorio.listaCodigosProduto().stream()
                .filter(Objects::nonNull)
                .map(cod -> cod.trim().replaceAll("\\D", ""))
                .filter(cod -> !cod.isEmpty())
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0) + 1);
    }

    public List<ProdutoCategoriaSubcategoria> listarSubcategoria(ProdutoCategoria idProdutoCategoria) {
        return repositorio.listarSubcategoria(idProdutoCategoria);
    }

}
