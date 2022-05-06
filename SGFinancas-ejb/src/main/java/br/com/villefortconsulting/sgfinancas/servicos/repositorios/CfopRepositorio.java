package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Cfop;
import java.util.List;
import javax.persistence.EntityManager;

public class CfopRepositorio extends BasicRepository<Cfop> {

    public CfopRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Cfop> listar() {
        String jpql = "select b from Cfop b order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeDevolucao() {
        String jpql = "select b from Cfop b where b.indicadorDevolucao = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeTransferencia() {
        String jpql = "select b from Cfop b where b.descricao like '%transferência%' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopVenda() {
        String jpql = "select b from Cfop b where b.indicadorNfe = 'S' ";
        jpql += " and b.indicadorTransporte = 'N' ";
        jpql += " and b.indicadorComunicacao = 'N' ";
        jpql += " and b.indicadorRetorno = 'N' ";
        jpql += " and b.indicadorAnulacao = 'N' ";
        jpql += " and b.indicadorRemessa = 'N' ";
        jpql += " and b.indicadorDevolucao = 'N' ";
        jpql += " and b.indicadorServico = 'N' ";
        jpql += " order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopServico() {
        String jpql = "select b from Cfop b where b.indicadorServico = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeTransporte() {
        String jpql = "select b from Cfop b where b.indicadorTransporte = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeComunicacao() {
        String jpql = "select b from Cfop b where b.indicadorComunicacao = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeRetorno() {
        String jpql = "select b from Cfop b where b.indicadorRetorno = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeAnulacao() {
        String jpql = "select b from Cfop b where b.indicadorAnulacao = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopDeRemessa() {
        String jpql = "select b from Cfop b where b.indicadorRemessa = 'S' order by b.codigo";
        return getPureList(jpql);
    }

    public List<Cfop> listarCfopCompra() {
        String jpql = "select b from Cfop b where b.tipo = 'C' order by b.codigo";
        return getPureList(jpql);
    }

}
