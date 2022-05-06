package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Servico;
import br.com.villefortconsulting.sgfinancas.entidades.ServicoProduto;
import java.util.List;
import javax.persistence.EntityManager;

public class ServicoProdutoRepositorio extends BasicRepository<ServicoProduto> {

    public ServicoProdutoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ServicoProdutoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<ServicoProduto> getListaByServico(Servico servico) {
        String hql = "select sp from ServicoProduto sp where sp.idServico = ?1";
        return getPureList(hql, servico);
    }

}
