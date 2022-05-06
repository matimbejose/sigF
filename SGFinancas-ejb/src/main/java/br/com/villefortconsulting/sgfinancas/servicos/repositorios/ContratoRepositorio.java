package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public class ContratoRepositorio extends BasicRepository<Contrato> {

    public ContratoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ContratoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Contrato> listar() {
        String jpql = "select b from Contrato b order by b.idPlanoConta.descricao";
        return getPureList(jpql);
    }

    public Contrato getContratoPorConta(Conta conta) {
        String jpql = "select c from Contrato c where c.idConta = ?1";
        return getPurePojo(jpql, conta);
    }

    public List<Contrato> listarContratoCliente() {
        String jpql = "select b from Contrato b where b.tipoContrato = ?1 and b.tenatID = ?2 order by b.idPlanoConta.descricao";
        return getPureList(jpql, EnumTipoContrato.CLIENTE.getTipo(), adHocTenant.getTenantID());
    }

    public List<Contrato> listarContratoClienteComAtualizacaoAutomatica() {
        String jpql = "select b from Contrato b where b.tipoContrato = ?1 and b.tenatID = ?2 and b.usaAtualizacaoAutomatica = 'S' order by b.idPlanoConta.descricao";
        return getPureList(jpql, EnumTipoContrato.CLIENTE.getTipo(), adHocTenant.getTenantID());
    }

    public List<Contrato> listarContratoFornecedor() {
        String jpql = "select b from Contrato b where b.tipoContrato = ?1 and b.tenatID = ?2 order by b.idPlanoConta.descricao";
        return getPureList(jpql, EnumTipoContrato.FORNECEDOR.getTipo(), adHocTenant.getTenantID());
    }

    public List<Contrato> listarContratoFornecedorComAtualizacaoAutomatica() {
        String jpql = "select b from Contrato b where b.tipoContrato = ?1 and b.tenatID = ?2 and b.usaAtualizacaoAutomatica = 'S' order by b.idPlanoConta.descricao";
        return getPureList(jpql, EnumTipoContrato.FORNECEDOR.getTipo(), adHocTenant.getTenantID());
    }

    public List<DocumentoAnexo> buscarDocumentosPorContrato(Contrato contrato) {
        return buscarDocumentosPorDocumento(contrato.getIdDocumento());
    }

    public List<DocumentoAnexo> buscarDocumentosPorDocumento(Documento documento) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select da ");
        sql.append(" from DocumentoAnexo da ");
        sql.append(" where da.idDocumento = ?1 ");

        return getPureList(sql.toString(), documento);
    }

    public Contrato buscarAnexos(Contrato contrato) {
        if (contrato.getIdDocumento() != null) {
            contrato.getIdDocumento().setDocumentoAnexoList(buscarDocumentosPorContrato(contrato));
        }
        return contrato;
    }

    public Conta buscarContaPorContrato(int numContrato) {
        String jpql = "select c,idConta from Contrato c where c.id = ?1";
        return getPurePojo(jpql, numContrato);
    }

    public Long getQteContratosVencendo(EnumTipoContrato tipoContrato) {
        return 1l * getContratosVencendo(tipoContrato).size();
    }

    public List<Contrato> getContratosVencendo(EnumTipoContrato tipoContrato) {
        StringBuilder sql = new StringBuilder();

        sql.append(" select c ");
        sql.append(" from Contrato c ");
        sql.append(" where c.prazoParaNotificacao is not null ");
        sql.append(" and c.dataCancelamento is null ");
        sql.append(" and c.tipoContrato = ?1 ");
        sql.append(" and c.situacao not in (?2, ?3) ");

        List<Contrato> datas = getPureList(sql.toString(), tipoContrato.getTipo(), EnumSituacaoConta.PAGA.getSituacao(),
                EnumSituacaoConta.INTERROMPIDO.getSituacao());

        return datas.stream()
                .filter(contrato -> DataUtil.diferencaEntreDias(new Date(), contrato.getDataFim()) <= contrato.getPrazoParaNotificacao())
                .filter(contrato -> DataUtil.diferencaEntreDias(new Date(), contrato.getDataFim()) > 0)
                .collect(Collectors.toList());
    }

    public boolean possuiListaServico(Contrato contrato) {
        return getPurePojo(Long.class, "select count(cs) from ContratoServico cs where cs.idContrato = ?1", contrato) > 1l;
    }

}
