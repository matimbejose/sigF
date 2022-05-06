package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.OrigemMercadoria;
import java.util.List;
import javax.persistence.EntityManager;

public class OrigemMercadoriaRepositorio extends BasicRepository<OrigemMercadoria> {

    public OrigemMercadoriaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public OrigemMercadoriaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<OrigemMercadoria> listar() {
        String jpql = "select c from OrigemMercadoria c order by c.codOrigemMercadoria";
        return getPureList(jpql);
    }

}
