package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Fornecedor;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorContato;
import br.com.villefortconsulting.sgfinancas.entidades.FornecedorProduto;
import br.com.villefortconsulting.sgfinancas.entidades.Produto;
import java.util.List;
import javax.persistence.EntityManager;

public class FornecedorRepositorio extends BasicRepository<Fornecedor> {

    public FornecedorRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FornecedorRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Fornecedor buscarPorCpfCnpj(String cpfCnpj) {
        String jpql = "select f from Fornecedor f where f.cpfCnpj = ?1 ";
        return getPurePojoTop1(jpql, cpfCnpj);
    }

    public List<Fornecedor> listar() {
        String jpql = "select f from Fornecedor f "
                + " left join f.idPlanoConta "
                + " left join f.endereco.idCidade "
                + " order by f.razaoSocial";
        return getPureList(jpql);
    }

    public List<FornecedorContato> listarFornecedorContato() {
        String jpql = "select cc from FornecedorContato cc order by cc.nome";
        return getPureList(jpql);
    }

    public List<FornecedorContato> listarFornecedorContato(Fornecedor fornecedor) {
        String jpql = " select cc from FornecedorContato cc where cc.idFornecedor = ?1 order by cc.nome ";
        return getPureList(jpql, fornecedor);
    }

    public List<Fornecedor> listar(String razaoSocial) {
        String jpql = "select f from Fornecedor f where f.razaoSocial like ?1 order by f.razaoSocial ";
        return getPureList(jpql, "%" + razaoSocial + "%");
    }

    public FornecedorProduto buscarFornecedorProduto(int id) {
        return getEntity(FornecedorProduto.class, id);
    }

    public List<FornecedorProduto> listarFornecedorProduto() {
        String jpql = "select fp from FornecedorProduto fp order by fp.id";
        return getPureList(jpql);
    }

    public List<Fornecedor> listarFornecedor() {
        String jpql = "select f from Fornecedor f order by f.razaoSocial";
        return getPureList(jpql);
    }

    public List<FornecedorProduto> listarFornecedorProduto(Produto produto) {
        String jpql = "select fp from FornecedorProduto fp where fp.idProduto = ?1 order by fp.id";
        return getPureList(jpql, produto);
    }

    public List<Fornecedor> listarFornecedorPorEmpresa(Empresa empresa) {
        String jpql = "select fp from Fornecedor fp where fp.tenatID = ?1 order by fp.razaoSocial";
        return getPureList(jpql, empresa.getTenatID());
    }

    public Fornecedor findByRazaoSocialCpfCnpj(String razaoSocial, String cpfCnpj) {
        String jpql = "select f from Fornecedor f where f.ativo = 'S' and upper(f.razaoSocial) = ?1 and f.cpfCnpj = ?2";
        return getPurePojo(jpql, razaoSocial.toUpperCase(), cpfCnpj);
    }

    public Fornecedor findByRazaoSocial(String razaoSocial) {
        return getPurePojoTop1("select f from Fornecedor f where f.ativo = 'S' and upper(f.razaoSocial) = ?1", razaoSocial.toUpperCase());
    }

}
