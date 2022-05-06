package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacaoAlteracao;
import javax.persistence.EntityManager;

public class ClienteMovimentacaoAlteracaoRepositorio extends BasicRepository<ClienteMovimentacaoAlteracao> {

    public ClienteMovimentacaoAlteracaoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

}
