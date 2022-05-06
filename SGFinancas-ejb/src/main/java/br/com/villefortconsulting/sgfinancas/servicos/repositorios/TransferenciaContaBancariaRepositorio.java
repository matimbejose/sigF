package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.TransferenciaContaBancaria;
import java.util.List;
import javax.persistence.EntityManager;

public class TransferenciaContaBancariaRepositorio extends BasicRepository<TransferenciaContaBancaria> {

    public TransferenciaContaBancariaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public TransferenciaContaBancariaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Object> getDadosAuditoriaByID(Class<ContaParcela> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

}
