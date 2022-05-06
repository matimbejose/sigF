package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Producao;
import br.com.villefortconsulting.sgfinancas.entidades.ProducaoProduto;
import br.com.villefortconsulting.util.NumeroUtil;
import java.util.List;
import javax.persistence.EntityManager;

public class ProducaoRepositorio extends BasicRepository<Producao> {

    public ProducaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ProducaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Producao> listar() {
        String jpql = "select p from Producao p order by p.dataPedido";
        return getPureList(jpql);
    }

    public Integer buscarProximoNumero() {
        String jpql = "select p.numero from Producao p order by p.dataPedido desc ";
        return NumeroUtil.somar((Double) getPurePojoTop1(jpql), 1d).intValue();
    }

    public List<ProducaoProduto> listarProdutos(Producao producao) {
        String jpql = "select pp from ProducaoProduto pp where pp.idProducao = ?1";
        return getPureList(jpql, producao);
    }

}
