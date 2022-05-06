package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.EntityId;
import javax.persistence.EntityManager;

public class AdministracaoRepositorio extends BasicRepository<EntityId> {

    public AdministracaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public boolean executaAtualizaoByNativeQuery(String consulta) {
        return executeCommandByNativeQuery(consulta) > 0;
    }

    public boolean executaAtualizaoDados(String consulta) {
        executeCommand(consulta);
        return true;
    }

}
