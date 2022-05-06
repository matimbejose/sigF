package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PlanoConta;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDreDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO;
import br.com.villefortconsulting.util.DataUtil;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public class PlanoContaRepositorio extends BasicRepository<PlanoConta> {

    public PlanoContaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public PlanoContaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public PlanoConta buscarPlanoContaPai(String tenat, String codigo) {
        String jpql = "select b from PlanoConta b where b.tenatID = ?1 and b.codigo = ?2 ";
        return getPurePojo(jpql, tenat, codigo);
    }

    public List<PlanoConta> listar() {
        String jpql = "select b from PlanoConta b order by b.codigo";
        return getPureList(jpql);
    }

    public boolean planoContaUtilizado(PlanoConta planoConta) {
        String jpql = "select c from Conta c where c.idPlanoConta = ?1 ";
        return getPurePojoTop1(jpql, planoConta) != null;
    }

    public List<PlanoConta> listarPlanosContaParaContrapartida(Empresa empresa, PlanoConta planoConta) {
        StringBuilder sql = new StringBuilder();

        if (null != planoConta && null != planoConta.getId()) {
            sql.append(" select a ");
            sql.append(" from PlanoConta a ");
            sql.append(" where a.id <> ?1 ");
            sql.append(" and a.tenatID = ?2 ");

            return getPureList(sql.toString(), planoConta.getId(), empresa.getTenatID());
        } else {
            sql.append(" select a ");
            sql.append(" from PlanoConta a ");
            sql.append(" where a.tenatID = ?1 ");

            return getPureList(sql.toString(), empresa.getTenatID());
        }
    }

    public List<PlanoConta> listarPlanosContaParaTransacoes() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a ");
        sql.append(" from PlanoConta a ");
        sql.append(" where (a.codigo like ?1 or a.codigo like ?2 or a.codigo like ?3 or a.codigo like ?4) ");
        sql.append(" order by a.codigo ");

        return getPureList(sql.toString(), "4%", "3%", "1%", "5%");
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaReceber() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a ");
        sql.append(" from PlanoConta a ");
        sql.append(" left join a.idContaPai ");
        sql.append(" left join a.idContaContrapartida ");
        sql.append(" where a.codigo like ?1 and a.codigo not like ?2 ");
        sql.append(" order by a.codigo ");

        return getPureList(sql.toString(), "4%", "4.1.2%");
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesContaPagar() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a ");
        sql.append(" from PlanoConta a ");
        sql.append(" left join a.idContaPai ");
        sql.append(" left join a.idContaContrapartida ");
        sql.append(" where a.codigo like ?1 ");
        sql.append(" order by a.codigo ");

        return getPureList(sql.toString(), "3%");
    }

    public List<PlanoConta> listarPlanosContaParaTransacoesCompra() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a ");
        sql.append(" from PlanoConta a ");
        sql.append(" left join a.idContaPai ");
        sql.append(" left join a.idContaContrapartida ");
        sql.append(" where (a.codigo like ?1 or a.codigo like ?2  or a.codigo like ?3 or a.codigo like ?4)");
        sql.append(" order by a.codigo ");

        return getPureList(sql.toString(), "3%", "1.2.3%", "1.1.3%", "4.1.2%");
    }

    public List<PlanoConta> listarPlanosContaTipoCredito() {
        String jpql = " select a from PlanoConta a where a.tipo = 'C'";
        return getPureList(jpql);
    }

    public List<PlanoConta> listarPlanosContaTipoDebito() {
        String jpql = " select a from PlanoConta a where a.tipo = 'D'";
        return getPureList(jpql);
    }

    public List<PlanoConta> listarPlanosContaPorEmpresa(Empresa empresa) {
        String jpql = " select a from PlanoConta a where a.tenatID =?1 order by a.codigo";
        return getPureList(jpql, empresa.getTenatID());
    }

    public List<PlanoConta> listarPlanoConta(PlanoConta planoConta) {
        String jpql = " select a from PlanoConta a where a.idContaPai = ?1 ";
        return getPureList(jpql, planoConta);
    }

    public List<PlanoContaDreDTO> listarPlanoContaDreDto(String codigo) {
        StringBuilder sb = new StringBuilder();
        sb.append("select new br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaDreDTO(")
                .append("a.descricao, a.tipo, a.codigo, b.codigo")
                .append(") ")
                .append("from PlanoConta a ")
                .append("join a.idContaPai b ");
        if (codigo == null) {
            return getPureList(sb.toString());
        }
        sb.append("where b.codigo = ?1");
        return getPureList(sb.toString(), codigo);
    }

    public List<PlanoConta> listarPlanoContaPai() {
        String jpql = " select a from PlanoConta a where a.idContaPai is null ";
        return getPureList(jpql);
    }

    public List<PlanoConta> listarPlanoContaOrderCodigo() {
        String jpq1 = "select p from PlanoConta p order by p.codigo";
        return getPureList(jpq1);
    }

    public boolean existeCodigoPlanoConta(PlanoConta planoConta) {
        String jpq1 = "select c.id from PlanoConta c where c.codigo = ?1";
        return getPurePojoTop1(jpq1, planoConta.getCodigo()) != null;
    }

    public boolean existeCodigoOutroPlanoConta(PlanoConta planoConta) {
        String jpq1 = "select c.id from PlanoConta c where c.codigo = ?1 and c.id <> ?2";
        return getPurePojoTop1(jpq1, planoConta.getCodigo(), planoConta.getId()) != null;
    }

    public List<PlanoConta> listarPlanoContaFilho(PlanoConta planoConta) {
        String jpq1 = "select p from PlanoConta p where p.idContaPai = ?1";
        return getPureList(jpq1, planoConta);
    }

    public String buscarUltimoCodigoPlanoContaPai(PlanoConta planoConta) {

        String jpql = " select max(a.codigo) from PlanoConta a where a.idContaPai = ?1 ";
        return getPurePojo(jpql, planoConta);
    }

    public List<PlanoContaLancamentoDTO> listarLancamentosPorPeriodo(Date data) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO( ");
        jpql.append(" a.idPlanoConta.id, sum(a.previsao), sum(a.realizado) ");
        jpql.append(" ) ");
        jpql.append(" from PlanoContaLancamento a ");
        jpql.append(" where ");
        jpql.append(" month(a.dataLacamento) = ?1 and year(a.dataLacamento) = ?2 ");
        jpql.append(" and tenatID = ?3 ");
        jpql.append(" group by a.idPlanoConta.id ");

        return getPureList(jpql.toString(), DataUtil.getMes(data), DataUtil.getAno(data), adHocTenant.getTenantID());
    }

    public List<PlanoConta> obterIndicadoresTelaInicial() {
        String jpq1 = "select p from PlanoConta p order by p.descricao where p.mostraTelaInicial = 'S'";
        return getPureList(jpq1);
    }

    public List<PlanoConta> obterPlanoContaPorCodigo(List<String> codigos) {
        StringBuilder todosCodigo = new StringBuilder();
        codigos.forEach(codigo -> todosCodigo.append(todosCodigo).append("'").append(codigo).append("',"));

        String jpq1 = " select p from PlanoConta p where p.codigo in (" + todosCodigo.substring(0, todosCodigo.length() - 1) + ") order by p.codigo  ";
        return getPureList(jpq1);
    }

    public PlanoConta obterUltimoPlanoContaGerado(PlanoConta planoConta) {
        String jpq1 = "select p from PlanoConta p where p.idContaPai = ?1 order by p.id desc";
        return getPurePojoTop1(jpq1, planoConta);
    }

    public PlanoConta obterPlanoContaPorCodigo(String codigo, String tenatID) {
        String jpq1 = "select p from PlanoConta p where p.codigo = ?1 and p.tenatID = ?2";
        return getPurePojoTop1(jpq1, codigo, tenatID);
    }

    public PlanoConta obterPlanoContaPorDescricao(String descricao, String tenatID) {
        String jpq1 = "select p from PlanoConta p where p.descricao = ?1 and p.tenatID = ?2";
        return getPurePojoTop1(jpq1, descricao, tenatID);
    }

    public List<PlanoContaLancamentoDTO> obterValorAnaliseOrcamentaria(Date data) {
        StringBuilder jpql = new StringBuilder("select new br.com.villefortconsulting.sgfinancas.entidades.dto.PlanoContaLancamentoDTO(");
        jpql.append(" cp.idPlanoConta, sum(cp.previsao), sum(cp.realizado) ");
        jpql.append(")  ");
        jpql.append(" from PlanoContaLancamento cp   ");
        jpql.append(" where cp.tenatID = ?3 and month(cp.dataLacamento) = ?1 and year(cp.dataLacamento) = ?2 and cp.dataLacamento is not null ");
        jpql.append(" group by cp.idPlanoConta ");

        return getPureList(jpql.toString(), DataUtil.getMes(data), DataUtil.getAno(data), adHocTenant.getTenantID());
    }

    public List<PlanoConta> listarPlanosContaQuePodemTerFilhos(String codigo) {
        return getPureList("select p from PlanoConta p where p.podeTerFilho = 'S' and p.codigo like ?1", codigo);
    }

    public List<PlanoConta> listarPlanosContaQuePodemTerFilhos() {
        return getPureList("select p from PlanoConta p where p.podeTerFilho = 'S'");
    }

    public List<PlanoConta> listarPlanoContaTelaInicial() {
        return getPureList("select p from PlanoConta p where p.mostraTelaInicial = 'S'");
    }

    public List<PlanoConta> listarFilhosByPlanoContaPai(PlanoConta planoConta) {
        return getPureList("select p from PlanoConta p where p.idContaPai = ?1", planoConta);
    }

    public PlanoConta buscarPlanoContaTransferencia(String tipo, PlanoConta planoConta) {
        return getPurePojo("select p from PlanoConta p where p.idContaPai = ?1 and p.tipo = ?2", planoConta, tipo);
    }

    public PlanoConta buscarPlanoContaByCodigo(String codigo) {
        return getPurePojo("select p from PlanoConta p where p.codigo = ?1 and p.tenatID = ?2", codigo, adHocTenant.getTenantID());
    }

}
