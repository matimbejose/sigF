package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import java.util.List;
import javax.persistence.EntityManager;

public class PagamentoMensalidadeModuloRepositorio extends BasicRepository<PagamentoMensalidadeModulo> {

    public PagamentoMensalidadeModuloRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<PagamentoMensalidadeModulo> getPagamentoMensalidadeModuloPor(PagamentoMensalidade pm) {
        return getPureList("select pmm from PagamentoMensalidadeModulo pmm where pmm.idPagamentoMensalidade = ?1", pm);
    }

    public List<PagamentoMensalidadeModulo> getPagamentoMensalidadeModuloPor(Modulo modulo) {
        return getPureList("select pmm from PagamentoMensalidadeModulo pmm where pmm.idModulo = ?1 and pmm.tenatID is not null", modulo);
    }

}
