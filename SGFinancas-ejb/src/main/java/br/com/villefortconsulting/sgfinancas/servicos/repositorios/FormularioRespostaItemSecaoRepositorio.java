package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioRespostaItemSecao;
import javax.persistence.EntityManager;

public class FormularioRespostaItemSecaoRepositorio extends BasicRepository<FormularioRespostaItemSecao> {

    public FormularioRespostaItemSecaoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

}
