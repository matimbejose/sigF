/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Venda;
import br.com.villefortconsulting.sgfinancas.entidades.VendaProduto;
import br.com.villefortconsulting.sgfinancas.entidades.VendaServico;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Desenvolvimento2
 */
public class TransacaoGetnetRepositorio extends BasicRepository<TransacaoGetnet> {

    public TransacaoGetnetRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TransacaoGetnetRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public TransacaoGetnet buscar(String idPagamento) {
        String hql = "select t from TransacaoGetnet t where t.idPagamento = ?1";
        return getPurePojo(hql, idPagamento);
    }

    public TransacaoGetnet buscar(Venda venda) {
        String hql = "select t from TransacaoGetnet t where t.idVenda =?1";
        return getPurePojoTop1(hql, venda);
    }

    public List<VendaProduto> listarVendaProduto(Venda venda) {
        String jpql = "select v from VendaProduto v where v.idVenda = ?1 and v.tenatID=?2";
        return getPureList(jpql, venda, venda.getTenatID());
    }

    public List<VendaServico> listarVendaServico(Venda venda) {
        String jpql = "select v from VendaServico v where v.idVenda = ?1 and v.tenatID=?2";
        return getPureList(jpql, venda, venda.getTenatID());
    }

    public TransacaoGetnet buscarPorMd(String md) {
        String jpql = "select t from TransacaoGetnet t where t.md = ?1";
        return getPurePojoTop1(jpql, md);
    }

    public Integer quantidadePagamentoSistema() {
        return getPurePojo(Long.class, "select count(tg) from TransacaoGetnet tg where tg.idPagamentoSistema is not null").intValue();
    }

}
