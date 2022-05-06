package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import javax.persistence.EntityManager;

public class FormularioRepositorio extends BasicRepository<Formulario> {

    public FormularioRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public Formulario buscarFormularioPorDescricao(String descricao) {
        return getPurePojo("select f from Formulario f where f.descricao = ?1", descricao);
    }

}
