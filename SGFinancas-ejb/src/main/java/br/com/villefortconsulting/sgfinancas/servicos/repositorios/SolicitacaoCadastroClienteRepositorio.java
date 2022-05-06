package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroCliente;
import javax.persistence.EntityManager;

public class SolicitacaoCadastroClienteRepositorio extends BasicRepository<SolicitacaoCadastroCliente> {

    public SolicitacaoCadastroClienteRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

}
