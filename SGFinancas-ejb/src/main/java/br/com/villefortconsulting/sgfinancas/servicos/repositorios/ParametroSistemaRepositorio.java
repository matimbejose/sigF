package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class ParametroSistemaRepositorio extends BasicRepository<ParametroSistema> {

    public ParametroSistemaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public ParametroSistemaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public ParametroSistema getInstrucaoBoleto(Empresa empresa) {
        return getPurePojoTop1("select par from ParametroSistema par where tenatID = ?1 ", empresa.getTenatID());
    }

    public ParametroSistema getParametro() {
        String sql = "select par from ParametroSistema par "
                + " left join fetch par.idCategoriaImportacaoProduto "
                + " left join fetch par.idCategoriaImportacaoServico "
                + " left join fetch par.idCentroCustoPadraoPdv "
                + " left join fetch par.idContaBancariaPadraoPdv  "
                + " left join fetch par.appContaBancariaPadrao  "
                + " left join fetch par.idPlanoContaContaBancaria a1 "
                + " left join fetch a1.idContaPai a11 "
                + " left join fetch a11.idContaPai "
                + " left join fetch par.idPlanoContaCliente a2"
                + " left join fetch a2.idContaPai a22"
                + " left join fetch a22.idContaPai "
                + " left join fetch par.idPlanoContaFornecedor a3"
                + " left join fetch a3.idContaPai a33"
                + " left join fetch a33.idContaPai "
                + " left join fetch par.idPlanoContaProduto a4"
                + " left join fetch a4.idContaPai a44 "
                + " left join fetch a44.idContaPai "
                + " left join fetch par.idPlanoContaServico a5"
                + " left join fetch a5.idContaPai a55"
                + " left join fetch a55.idContaPai "
                + " left join fetch par.idPlanoContaTransportadora a6"
                + " left join fetch a6.idContaPai a66"
                + " left join fetch a66.idContaPai "
                + " left join fetch par.idPlanoContaFuncionario a7"
                + " left join fetch a7.idContaPai a77"
                + " left join fetch a77.idContaPai "
                + " left join fetch par.idPlanoContaPadraoPdv a8"
                + " left join fetch a8.idContaPai a88"
                + " left join fetch a88.idContaPai "
                + " left join fetch par.appPlanoContaPadrao a9"
                + " left join fetch a9.idContaPai a99"
                + " left join fetch a99.idContaPai "
                + " where par.tenatID = ?1 ";

        return getPurePojo(sql, adHocTenant.getTenantID());
    }

    public boolean temParametroEmpresa(Empresa empresa) {
        return getPurePojo(Long.class, "select count(par.id) from ParametroSistema par where tenatID = ?1 ", empresa.getTenatID()) > 0;
    }

    public ParametroSistema pegaParametroPorEmpresa(Empresa empresa) {
        String jpql = "select d from ParametroSistema d where d.tenatID =?1";
        return getPurePojo(jpql, empresa.getTenatID());
    }

    public List<TipoProdutoUso> getTipoProdutoUsoList(ParametroSistema pa) {
        String jpql = " select t from TipoProdutoUso t where t.idParametroSistema =?1 ";
        return getPureList(jpql, pa);
    }

    public boolean updateParametro(String name, Object value) {
        String hql = "update ParametroSistema ps set ps." + name + " = ?1 where ps.tenatID = ?2";
        try {
            executeCommand(hql, value, adHocTenant.getTenantID());
            return true;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
