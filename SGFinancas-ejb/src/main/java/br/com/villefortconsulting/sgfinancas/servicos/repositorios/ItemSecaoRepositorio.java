package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ItemSecao;
import java.util.List;
import javax.persistence.EntityManager;

public class ItemSecaoRepositorio extends BasicRepository<ItemSecao> {

    public ItemSecaoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<ItemSecao> listAtivo() {
        return getPureList("select is from ItemSecao is where is.ativo = 'S' order by is.descricao");
    }

}
