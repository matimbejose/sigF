package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.IntroJSConfig;
import javax.persistence.EntityManager;

public class IntroJSConfigRepositorio extends BasicRepository<IntroJSConfig> {

    public IntroJSConfigRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public IntroJSConfig findByPageId(String pageId) {
        return getPurePojoTop1("select ijsc from IntroJSConfig ijsc where ijsc.pageId = ?1", pageId);
    }

}
