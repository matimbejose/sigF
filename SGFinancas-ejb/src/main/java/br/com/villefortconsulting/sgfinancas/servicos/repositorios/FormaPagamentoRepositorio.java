package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FormaPagamento;
import java.util.List;
import javax.persistence.EntityManager;

public class FormaPagamentoRepositorio extends BasicRepository<FormaPagamento> {

    public FormaPagamentoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public FormaPagamentoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<FormaPagamento> listar() {
        String jpql = "select f from FormaPagamento f ";
        return getPureList(jpql);
    }

    public List<FormaPagamento> listarParaNFe() {
        String jpql = "select f from FormaPagamento f where f.codigoNfe is not null ";
        return getPureList(jpql);
    }

    public List<FormaPagamento> listarFormaDePagamentoAtivo() {
        String jpql = "select f from FormaPagamento f where f.ativo='S' ";
        return getPureList(jpql);
    }

    public FormaPagamento findByDescricao(String descricao) {
        String jpql = "select f from FormaPagamento f where f.ativo = 'S' and upper(f.descricao) = ?1";
        return getPurePojo(jpql, descricao.toUpperCase());
    }

}
