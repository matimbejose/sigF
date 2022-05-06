package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaContatoCliente;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaUsuarioAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.TipoSocialEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AcessoPorEmpresaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaFiltro;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.operator.In;
import br.com.villefortconsulting.util.operator.MinMax;
import br.com.villefortconsulting.util.sql.QueryBuilder;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class EmpresaRepositorio extends BasicRepository<Empresa> {

    public static final Boolean NAO_USA_TENANT = false;

    public EmpresaRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public EmpresaRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Empresa getEmpresa() {
        return getPurePojoTop1("select emp from Empresa emp");
    }

    public Empresa getEmpresabyID(Integer id) {
        return getPurePojo("select emp from Empresa emp where emp.id = ?1", id);
    }

    public List<Empresa> getEmpresas() {
        return getPureList("select emp from Empresa emp order by emp.descricao");
    }

    public Empresa getEmpresPorTenatID() {
        return getPurePojo("select emp from Empresa emp where emp.tenatID = ?1", adHocTenant.getTenantID());
    }

    public Empresa getEmpresPorTenatID(String tenatID) {
        return getPurePojo("select emp from Empresa emp where emp.tenatID = ?1", tenatID);
    }

    public List<EmpresaUsuarioAcesso> listarEmpresaUsuarioAcesso(Empresa empresa) {
        return getPureList("select emp from EmpresaUsuarioAcesso emp where emp.idEmpresa.tenatID = ?1 ", empresa.getTenatID());
    }

    public EmpresaContatoCliente setEmpresaContatoCliente(EmpresaContatoCliente obj) {
        return setEntity(obj);
    }

    public void removeEmpresaContatoCliente(EmpresaContatoCliente obj) {
        removeEntity(obj);
    }

    public EmpresaContatoCliente addEmpresaContatoCliente(EmpresaContatoCliente obj) {
        addEntity(obj);
        return obj;
    }

    public List<Empresa> getListaFiltrada(List<Criterion> criterions, EmpresaFiltro filtro) {
        return getListaFiltrada(Empresa.class, criterions, filtro);
    }

    public List<Empresa> getListaFiltrada(List<Criterion> criterions, EmpresaFiltro filtro, Boolean usaTenant) {
        return getListaFiltrada(Empresa.class, criterions, filtro, usaTenant);
    }

    public int getQuantidadeRegistrosFiltrados(List<Criterion> criterions, Boolean usaTenant) {
        return getQuantidadeRegistrosFiltrados(Empresa.class, criterions, usaTenant);
    }

    public String getTipo() {
        return getPurePojoTop1("select emp.tipo from Empresa emp");
    }

    public Empresa getEmpresaPorTenat(String tenat) {
        String sql = "select emp from Empresa emp "
                + " left join fetch emp.endereco.idCidade c"
                + " left join fetch c.idUF c1"
                + " left join fetch emp.idCnae cnae "
                + " left join fetch emp.idDocumento "
                + " left join fetch emp.idContabilidade con "
                + " left join fetch cnae.idClassificacao "
                + " where emp.tenatID = ?1 ";
        return getPurePojo(sql, tenat);
    }

    public boolean existeCnpjCadastrado(String cnpj) {
        return getPurePojo(Long.class, "select count(emp.id) from Empresa emp where emp.cnpj = ?1 and emp.tenatID is not null", cnpj) > 0;
    }

    public Long getQtdEmpresasExpiradas() {

        List<Date> datas = getPureList("select dataCredenciamento from Empresa emp where emp.tenatID is not null and emp.tipoConta = 'G' and emp.ativo = 'S' and dataCredenciamento is not null");

        return datas
                .stream()
                .filter(p -> DataUtil.diferencaEntreDias(p, new Date()) > 0)
                .count();

    }

    public Long getQtdClientesContatoHoje() {
        return getPurePojo(Long.class, " select count(emp.id) from EmpresaContatoCliente emp where emp.idEmpresa.tenatID is not null and emp.dataProximoContato = ?1 ", DataUtil.getHoje());
    }

    public List<Object> getDadosAuditoriaByID(Class<EmpresaContatoCliente> aClass, Integer id) {
        return getDadosAuditoria(aClass, id);
    }

    public List<Empresa> buscaCredorParaAdiantamento(Double valorTotal) {
        return getPureList("select e from Empresa e where e.tipoEmpresa = ?1 and e.saldoDisponivel <= = ?2 ", "CR", valorTotal);
    }

    public List<Empresa> listaEmpresaCadastradaPorPeriodo(Date dataInicio, Date dataFim) {
        return getPureList("select e from Empresa e where e.dataCredenciamento >= ?1 and e.dataCredenciamento <= ?2  and e.ondeConheceu is not null order by e.ondeConheceu", dataInicio, dataFim);
    }

    public List<TipoSocialEmpresa> getTipoSocialEmpresa() {
        return getPureList("select te from TipoSocialEmpresa te");
    }

    public Empresa buscarPorCnpj(String cnpj) {
        return getPurePojo("select e from Empresa e where e.cnpj = ?1 and e.tenatID is not null", cnpj);
    }

    public List<AcessoPorEmpresaDTO> listaAcessosPorEmpresa(boolean showSuporte, boolean showContabilidade, In<String> tipoPagamento, MinMax<Date> periodo) {
        QueryBuilder qb = new QueryBuilder();

        qb.select(AcessoPorEmpresaDTO.class, "eua.idEmpresa.id, eua.idUsuario.id,"
                + "eua.idEmpresa.descricao, eua.idUsuario.nome,"
                + "eua.idUsuario.idPerfil.descricao,"
                + "month(eua.dataAcesso), year(eua.dataAcesso),"
                + "0.0, count(eua), false")
                .from(EmpresaUsuarioAcesso.class)
                .join("eua.idEmpresa.pagamentoMensalidadeList", "pm")
                .where("eua.dataAcesso >= ?1 and eua.dataAcesso <= ?2")
                .addOrderBy("eua.idEmpresa.descricao, eua.idUsuario.nome")
                .addGroupBy("eua.idEmpresa.id, eua.idUsuario.id, eua.idEmpresa.descricao, eua.idUsuario.nome, eua.idUsuario.idPerfil.descricao, month(eua.dataAcesso), year(eua.dataAcesso)");

        if (!showSuporte) {
            qb.addWhere("and eua.idUsuario.idPerfil.tipo != 'AD'");
        }

        if (!showContabilidade) {
            qb.addWhere("and eua.idUsuario.idPerfil.tipo != 'C'");
        }

        if (!tipoPagamento.getValue().isEmpty()) {
            StringBuilder tipos = new StringBuilder();
            String sep = "";
            for (String tipo : tipoPagamento.getValue()) {
                tipos.append(sep).append("'").append(tipo).append("'");
                sep = ", ";
            }
            qb.addWhere("and eua.idEmpresa.tipoConta in (" + tipos.toString() + ")");
        }

        return getPureList(qb, periodo.getMin(), periodo.getMax());
    }

}
