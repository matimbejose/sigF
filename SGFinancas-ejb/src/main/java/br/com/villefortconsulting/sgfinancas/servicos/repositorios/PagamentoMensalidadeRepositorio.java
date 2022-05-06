package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import br.com.villefortconsulting.util.sql.QueryBuilder;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class PagamentoMensalidadeRepositorio extends BasicRepository<PagamentoMensalidade> {

    public PagamentoMensalidadeRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    public List<PagamentoMensalidade> buscaPagamentosByEmpresa(Empresa empresa) {
        return getPurePojo("select pm from PagamentoMensalidade pm where pm.idEmpresa = ?1", empresa);
    }

    public PagamentoMensalidade getUltimoPagamentoPor(Empresa empresa) {
        return getPurePojoTop1("select pm from PagamentoMensalidade pm where pm.idEmpresa = ?1 and pm.dataPagamento is not null and pm.tenatID = ?2 order by pm.dataValidade desc , pm.id desc", empresa, empresa.getTenatID());
    }

    public PagamentoMensalidade buscarPorReferencia(String reference) {
        return getPurePojo("select pm from PagamentoMensalidade pm where pm.referenciaFitPag = ?1", reference);
    }

    public Double buscarValorPagoPor(Integer idEmpresa, Integer idUsuario, Date data) {
        QueryBuilder qb = new QueryBuilder();

        qb.select("sum(pm.valorPago)")
                .from(PagamentoMensalidade.class)
                .where("pm.idEmpresa.id = ?1 and pm.idUsuarioPagamento.id = ?2 and month(pm.dataPagamento) = ?3 and year(pm.dataPagamento) = ?4");

        return getPurePojo(qb.build(), idEmpresa, idUsuario, DataUtil.getMes(data), DataUtil.getAno(data));
    }

    public List<PagamentoMensalidade> buscarPagamentosPor(MinMax<Date> periodo) {
        QueryBuilder qb = new QueryBuilder();

        qb.select("pm")
                .from(PagamentoMensalidade.class)
                .where("pm.dataPagamento >= ?1 and pm.dataPagamento <= ?2 and pm.idUsuarioPagamento is not null and pm.valorPago > 0");

        return getPureList(qb.build(), periodo.getMin(), periodo.getMax());
    }

}
