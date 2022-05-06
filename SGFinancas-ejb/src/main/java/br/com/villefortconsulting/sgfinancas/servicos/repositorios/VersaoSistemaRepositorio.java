package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.VersaoSistema;
import javax.persistence.EntityManager;

public class VersaoSistemaRepositorio extends BasicRepository<VersaoSistema> {

    public VersaoSistemaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public VersaoSistema findByVersao(String versao) {
        return getPurePojoTop1("select vs from VersaoSistema vs where vs.versao = ?1", versao);
    }

    public VersaoSistema findLast() {
        return getPurePojoTop1("select vs from VersaoSistema vs order by vs.dataInclusao desc");
    }

}
