package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoriaSubcategoria;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoComposicao;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoComposicaoProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ProdutoRepositorio extends BasicRepository<Produto> {

    public ProdutoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ProdutoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Produto buscar(String descricao) {
        String jpql = "select p from Produto p where p.tipo = 'P' and p.descricao = ?1 ";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Produto> buscarProdutos(String descricao) {
        String jpql = "select p from Produto p where p.tipo = 'P' and p.descricao = ?1 ";
        return getPureList(jpql, descricao);
    }

    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String jpql = "select p from Produto p where p.tipo = 'P' and p.codigoBarra = ?1 ";
        return getPurePojoTop1(jpql, codigoBarras);
    }

    public Produto buscarPorCodigoNCM(String codigoNCM) {
        String jpql = "select p from Produto p where p.tipo = 'P' and p.idNcm.codigo = ?1 ";
        return getPurePojoTop1(jpql, codigoNCM);
    }

    @Override
    public List<Produto> list() {
        String jpql = "select p from Produto p where p.tipo = 'P' and ativo = 'S' order by p.descricao";
        return getPureList(jpql);
    }

    public List<Produto> listarProdutosCompostos() {
        String jpql = "select p from Produto p where p.tipo = 'P' and p.composto = ?1 and ativo = 'S' order by p.descricao";
        return getPureList(jpql, EnumTipoComposicaoProduto.PRODUTO_COMPOSTO.getTipo());
    }

    public List<Produto> listarProdutosDoFornecedor(Fornecedor fornecedor) {
        String jpql = "select prod.idProduto from FornecedorProduto prod where prod.idFornecedor = ?1";
        return getPureList(jpql, fornecedor);
    }

    public List<Produto> listar(List<TipoProdutoUso> tipos) throws CadastroException {
        String tenat = adHocTenant.getTenantID();
        StringBuilder sb = new StringBuilder();
        sb.append(" select p from Produto p where p.tenatID = ").append(tenat).append(" and p.ativo = 'S' and (");
        String separator = "";
        String subQuery = "(select pc.idProdutoOrigem.id from ProdutoComposicao pc where pc.tenatID = " + tenat + ")";
        boolean hasNormal = false;
        boolean hasInsumo = false;
        if(tipos.isEmpty()){
            throw new CadastroException("Nenhum tipo de produto foi configurado no parametro do sistema, favor realizar o cadastro em Parametro do Sistema -> Produtos.", null);
        }
        for (TipoProdutoUso tipo : tipos) {
            switch (EnumTipoProdutoVenda.retornaEnumSelecionado(tipo.getTipoProduto())) {
                case COMPOSTO:
                    sb.append(separator).append("p.composto = 'C'");
                    separator = " or ";
                    break;
                case KIT:
                    sb.append(separator).append("p.composto = 'K'");
                    separator = " or ";
                    break;
                case NORMAL:
                    hasNormal = true;
                    break;
                case INSUMO:
                    hasInsumo = true;
                    break;
                default:
                    Logger.getLogger(getClass().getName()).log(Level.INFO, () -> "Tipo de produto '" + tipo.getTipoProduto() + "' não configurado para a listagem de venda/compra.");
                    break;
            }
        }
        if (hasNormal || hasInsumo) {
            if (hasNormal && hasInsumo) {
                sb.append(separator).append("p.composto = 'N'");
            } else if (hasNormal) {
                sb.append(separator).append("(p.composto = 'N' and p.id not in ").append(subQuery).append(")");
            } else {
                sb.append(separator).append("(p.composto = 'N' and p.id in ").append(subQuery).append(")");
            }
        }

        sb.append(") order by p.descricao ");
        return getPureList(sb.toString());
    }

    public List<Produto> listar(String descricao) {
        String jpql = "select p from Produto p where p.descricao like ?1 order by p.descricao ";
        return getPureList(jpql, "%" + descricao + "%");
    }

    public Produto buscarServico(String descricao) {
        String jpql = "select p from Produto p where p.tipo = 'S' and p.descricao = ?1 ";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Produto> listarServicos() {
        String jpql = "select p from Produto p where p.tipo = 'S' order by p.descricao";
        return getPureList(jpql);
    }

    public List<Produto> listarProdutos() {
        String jpql = "select p from Produto p where p.tipo = 'P' order by p.descricao";
        return getPureList(jpql);
    }

    public ProdutoCategoria adicionarCategoria(ProdutoCategoria categoriaProduto) {
        addEntity(categoriaProduto);
        return categoriaProduto;
    }

    public ProdutoCategoria alterarCategoria(ProdutoCategoria categoriaProduto) {
        return setEntity(categoriaProduto);
    }

    public void removerCategoria(ProdutoCategoria categoriaproduto) {
        removeEntity(categoriaproduto);
    }

    public ProdutoCategoria buscarCategoria(int id) {
        return getEntity(ProdutoCategoria.class, id);
    }

    public ProdutoCategoria buscarCategoria(String descricao) {
        String jpql = "select p from ProdutoCategoria p where p.descricao = ?1";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<ProdutoCategoria> listarCategoria() {
        String jpql = "select p from ProdutoCategoria p order by p.descricao";
        return getPureList(jpql);
    }

    public List<ProdutoCategoria> listarCategoriaAtiva() {
        String jpql = "select p from ProdutoCategoria p where p.ativo = 'S' order by p.descricao";
        return getPureList(jpql);
    }

    public List<Produto> listarProdutoPorEmpresa(Empresa empresa) {
        String jpql = "select p from Produto p where p.tenatID =?1 order by p.descricao";
        return getPureList(jpql, empresa.getTenatID());
    }

    public boolean hasAnyProdutoCategoria() {
        return ((Long) getPurePojoTop1(" select count(*) from ProdutoCategoria where tenatID = ?1 ", adHocTenant.getTenantID())).intValue() > 0;
    }

    public boolean hasAnyProduto() {
        return ((Long) getPurePojoTop1(" select count(*) from Produto where tenatID = ?1 ", adHocTenant.getTenantID())) > 0L;
    }

    public List<ProdutoComposicao> listarProdutosCompostosPorProduto(Produto produto) {
        String jpql = " select pc from ProdutoComposicao pc where pc.idProdutoOrigem = ?1 ";
        return getPureList(jpql, produto);
    }

    public List<ProdutoComposicao> buscaUsoProduto(Produto produto) {
        String jpql = " select pc from ProdutoComposicao pc where pc.idProduto = ?1 ";
        return getPureList(jpql, produto);
    }

    public List<Object> getDadosAuditoriaByID(Class<ProdutoCategoria> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

    public void atualizaTenat(FornecedorProduto fornecedorProduto) {
        super.atualizaTenat(fornecedorProduto);
    }

    public List<Produto> listarProdutosAtivos() {
        String jpql = "select p from Produto p where p.ativo='S' ";
        return getPureList(jpql);
    }

    public List<String> listaCodigosProduto() {
        return getPureList("select p.codigo from Produto p");
    }

    public ProdutoCategoria buscarCategoriaAtiva(String descricao) {
        String jpql = "select p from ProdutoCategoria p where upper(p.descricao) = ?1 and p.ativo = 'S' ";
        return getPurePojoTop1(jpql, descricao.toUpperCase());
    }

    public List<ProdutoCategoriaSubcategoria> listarSubcategoria(ProdutoCategoria idProdutoCategoria) {
        return getPureList("select pcsc from ProdutoCategoriaSubcategoria pcsc where pcsc.idProdutoCategoria = ?1 ", idProdutoCategoria);
    }

}
