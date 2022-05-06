package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroCondominio;
import javax.persistence.EntityManager;

public class ParametroCondominioRepositorio extends BasicRepository<ParametroCondominio> {

    public ParametroCondominioRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ParametroCondominioRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public ParametroCondominio getParametro() {
        return getPurePojoTop1("select par from ParametroCondominio par");
    }

}
