package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FuncionarioServico;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.ServicoProduto;
import java.util.List;
import javax.persistence.EntityManager;

public class ServicoRepositorio extends BasicRepository<Servico> {

    public ServicoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ServicoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Servico buscarServico(String descricao) {
        String jpql = "select s from Servico s where s.descricao like ?1 ";
        return getPurePojoTop1(jpql, descricao);
    }

    public List<Servico> listarServicos(String descricao) {
        String jpql = "select s from Servico s where s.descricao = ?1 ";
        return getPureList(jpql, descricao);
    }

    public List<Servico> listar() {
        String jpql = "select s from Servico s where s.ativo = ?1 order by s.descricao";
        return getPureList(jpql, "S");
    }

    public List<FuncionarioServico> listarFuncionarioServico(Servico servico) {
        String jpql = "select b from FuncionarioServico b where b.idServico = ?1";
        return getPureList(jpql, servico);
    }

    public List<ServicoProduto> listarServicoProduto(Servico servico) {
        String jpql = "select b from ServicoProduto b where b.idServico = ?1";
        return getPureList(jpql, servico);
    }

    public List<Servico> listarServicosAtivos() {
        String jpql = "select s from Servico s where s.ativo = 'S'";
        return getPureList(jpql);
    }

    public Servico findByDescricao(String descricao) {
        String jpql = "select s from Servico s where s.ativo = 'S' and upper(s.descricao) = ?1";
        return getPurePojo(jpql, descricao.toUpperCase());
    }

}
