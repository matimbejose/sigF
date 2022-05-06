package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.IntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoIntegracaoBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntegracaoBancariaFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class IntegracaoBancariaRepositorio extends BasicRepository<IntegracaoBancaria> {

    public IntegracaoBancariaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public IntegracaoBancariaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<IntegracaoBancaria> getIntegracaoBancarias() {
        return getPureList("select i from IntegracaoBancaria i");
    }

    public TransacaoIntegracaoBancaria addTransacaoIntegracaoBancaria(TransacaoIntegracaoBancaria obj) {
        return setEntity(obj);
    }

    public TransacaoIntegracaoBancaria setTransacaoIntegracaoBancaria(TransacaoIntegracaoBancaria obj) {
        return setEntity(obj);
    }

    public List<IntegracaoBancaria> getListaFiltrada(List<Criterion> criterions, IntegracaoBancariaFiltro filtro) {
        return getListaFiltrada(IntegracaoBancaria.class, criterions, filtro);
    }

    public List<TransacaoIntegracaoBancaria> listarTransacaoIntegracaoBancaria(IntegracaoBancaria integracaoBancaria) {
        return getPureList("select i from TransacaoIntegracaoBancaria i where i.idIntegracaoBancaria = ?1 ", integracaoBancaria);
    }

    public boolean possuiVinculoConciliacao(ContaParcela cp) {
        return getPurePojo(Long.class, "select count(e) from TransacaoIntegracaoBancariaContaParcela e where e.idContaParcela = ?1", cp) > 0;
    }

    public Double valorConciliadoPor(ContaParcela cp) {
        return getPurePojo(Double.class, "select sum(e.idTransacaoIntegracaoBancaria.valor) from TransacaoIntegracaoBancariaContaParcela e where e.idContaParcela = ?1", cp);
    }

}
