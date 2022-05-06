package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoContaPadrao;
import java.util.List;
import javax.persistence.EntityManager;

public class ContabilidadeRepositorio extends BasicRepository<Contabilidade> {

    public ContabilidadeRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Contabilidade> listarContabilidade() {
        return getPureList("select par from Contabilidade par ");
    }

    public String buscarCodigoContabilidadePlanoConta(Contabilidade contabilidade, PlanoContaPadrao planoConta) {
        return getPurePojoTop1(" select a.codigo from ContabilidadePlanoConta a where a.idContabilidade = ?1 and a.idPlanoContaPadrao = ?2 ", contabilidade, planoConta);
    }

}
