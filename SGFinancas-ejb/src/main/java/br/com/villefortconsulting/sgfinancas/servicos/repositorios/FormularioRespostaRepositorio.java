package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.FormularioResposta;
import javax.persistence.EntityManager;

public class FormularioRespostaRepositorio extends BasicRepository<FormularioResposta> {

    public FormularioRespostaRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public boolean temRespostaParaO(Formulario formulario) {
        return getPurePojo(Long.class, "select count(fr) from FormularioResposta fr where fr.idFormulario = ?1", formulario) == 0;
    }

}
