package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Compra;
import br.com.villefortconsulting.sgfinancas.entidades.CompraProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import java.util.List;
import javax.persistence.EntityManager;

public class CompraRepositorio extends BasicRepository<Compra> {

    public CompraRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public CompraRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Compra> listar() {
        String jpql = "select c from Compra c";
        return getPureList(jpql);
    }

    public CompraProduto adicionarCompraProduto(CompraProduto compraProduto) {
        addEntity(compraProduto);
        return compraProduto;
    }

    public CompraProduto alterarCompraProduto(CompraProduto compraProduto) {
        return setEntity(compraProduto);
    }

    public void removerCompraProduto(CompraProduto compraProduto) {
        removeEntity(compraProduto);
    }

    public CompraProduto buscarCompraProduto(int id) {
        return getEntity(CompraProduto.class, id);
    }

    public List<CompraProduto> listarCompraProduto(Compra compra) {
        String jpql = "select c from CompraProduto c where c.idCompra = ?1 order by c.dadosProduto.detalhesItem ";
        return getPureList(jpql, compra);
    }

    public List<CompraProduto> listarCompraProduto(Integer idCompra) {
        String jpql = "select c from CompraProduto c where c.idCompra.id = ?1";
        return getPureList(jpql, idCompra);
    }

    public Compra buscarCompraPorConta(Conta conta) {
        String jpql = "select c from Compra c where c.idConta = ?1";
        return getPurePojo(jpql, conta);
    }

    public List<CompraProduto> listarCompraPorProduto(Produto produto) {
        String jpql = " select vp from CompraProduto vp where vp.dadosProduto.idProduto = ?1 and vp.tenatID = ?2";
        return getPureList(jpql, produto, adHocTenant.getTenantID());
    }

    public boolean compraPossuiNF(Compra venda) {
        String jpq1 = " select p.id from NF p where p.idCompra = ?1 and p.situacao <> ?2";
        return getPurePojoTop1(Integer.class, jpq1, venda, EnumSituacaoNF.CANCELADA_DEVOLUCAO.getSituacao()) != null;
    }

    public List<CompraProduto> getComprasProdutoList(Produto produtoSelecionado) {
        return getPureList(10, "select cp from CompraProduto cp where cp.dadosProduto.idProduto = ?1", produtoSelecionado);
    }

}
