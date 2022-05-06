package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModuloPermissao;
import java.util.List;
import javax.persistence.EntityManager;

public class PagamentoMensalidadeModuloPermissaoRepositorio extends BasicRepository<PagamentoMensalidadeModuloPermissao> {

    public PagamentoMensalidadeModuloPermissaoRepositorio(EntityManager entityManager, AdHocTenant adHocTenant) {
        super(entityManager, adHocTenant);
    }

    @Override
    public List<PagamentoMensalidadeModuloPermissao> list() {
        return getPureList("select pmmp from PagamentoMensalidadeModuloPermissao pmmp where pmmp.tenatID = ?1", adHocTenant.getTenantID());
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPor(Empresa empresa) {
        return getPureList("select pmmp from PagamentoMensalidadeModuloPermissao pmmp where pmmp.idPagamentoMensalidadeModulo.idPagamentoMensalidade.idEmpresa = ?1", empresa);
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPorEmpresaEDescricao(Empresa empresa, String descricao) {
        return getPureList("select pmmp from PagamentoMensalidadeModuloPermissao pmmp where pmmp.idPagamentoMensalidadeModulo.idPagamentoMensalidade.idEmpresa = ?1 and upper(pmmp.idPermissao.descricaoDetalhada) like ?2", empresa, "%" + descricao.toUpperCase() + "%");
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPor(PagamentoMensalidade pm, String descricao) {
        return getPureList("select pmmp from PagamentoMensalidadeModuloPermissao pmmp where pmmp.idPagamentoMensalidadeModulo.idPagamentoMensalidade = ?1 and upper(pmmp.idPermissao.descricaoDetalhada) like ?2", pm, "%" + descricao.toUpperCase() + "%");
    }

}
