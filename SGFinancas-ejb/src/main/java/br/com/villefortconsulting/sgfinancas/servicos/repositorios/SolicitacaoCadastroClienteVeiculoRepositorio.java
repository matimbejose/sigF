package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroClienteVeiculo;
import javax.persistence.EntityManager;

public class SolicitacaoCadastroClienteVeiculoRepositorio extends BasicRepository<SolicitacaoCadastroClienteVeiculo> {

    public SolicitacaoCadastroClienteVeiculoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

}
