package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.ControleAcessoIp;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ControleAcessoIpFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class ControleAcessoIpRepositorio extends BasicRepository<ControleAcessoIp> {

    public ControleAcessoIpRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ControleAcessoIp getControleAcessoIp(int id) {
        return getEntity(ControleAcessoIp.class, id);
    }

    public List<ControleAcessoIp> getControleAcessoIps() {
        return getPureList("select org from ControleAcessoIp org order by org.descricao");
    }

    public List<ControleAcessoIp> getListaFiltrada(List<Criterion> criterions, ControleAcessoIpFiltro filtro) {
        return getListaFiltrada(ControleAcessoIp.class, criterions, filtro);
    }

}
