package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import java.util.List;
import javax.persistence.EntityManager;

public class AvariaRepositorio extends BasicRepository<Avaria> {

    public AvariaRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    @Override
    public List<Avaria> list() {
        return getPureList("select a from Avaria a where a.ativo = 'S' order by a.descricao");
    }

    public Avaria buscarAvariaPorDescricao(String descricao) {
        return getPurePojo("select a from Avaria a where a.descricao = ?1", descricao);
    }

}
