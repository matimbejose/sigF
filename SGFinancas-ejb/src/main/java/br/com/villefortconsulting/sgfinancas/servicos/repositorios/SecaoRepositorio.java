package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Secao;
import java.util.List;
import javax.persistence.EntityManager;

public class SecaoRepositorio extends BasicRepository<Secao> {

    public SecaoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<Secao> listAtivo() {
        return getPureList("select s from Secao s where s.ativo = 'S' order by s.descricao");
    }

}
