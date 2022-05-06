package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioAcessoRapido;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UsuarioFiltro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.sql.QueryBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

public class UsuarioRepositorio extends BasicRepository<Usuario> {

    public UsuarioRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public UsuarioRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public Usuario getUsuarioComPermissoesPorLogin(Usuario usuario) {
        StringBuilder consulta = new StringBuilder();

        consulta.append(" select usr ");
        consulta.append(" from Usuario usr   ");
        consulta.append(" left join fetch usr.usuarioGrupoList  ");
        consulta.append(" where usr.login = ?1 ");
        consulta.append(" order by user.id ");

        return getPurePojoTop1(consulta.toString(), usuario.getLogin());
    }

    public List<Usuario> getUsuarios() {
        return getPureList("select usr from Usuario usr order by usr.nome");
    }

    public List<Usuario> getUsuarios(String tenantId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select u ")
                .append("from LoginAcesso la ")
                .append("join la.idUsuario u ")
                .append("join la.idEmpresa e ")
                .append("where e.id = ?1 ")
                .append("order by u.nome");
        return getPureList(sql.toString(), Integer.parseInt(tenantId));
    }

    public List<Usuario> getUserByName(String name) {
        return getPureList("select usr from Usuario usr where usr.nome like ?1 order by usr.nome", "%" + name + "%");
    }

    public List<Usuario> getUsuarioPorEmpresaLogada(String name, Empresa empresaLogada) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select la.idUsuario from LoginAcesso la ");
        sql.append(" where la.idEmpresa= ?1 ");

        if (name != null) {
            sql.append(" and la.idUsuario.nome like '%").append(name).append("%'");
        }
        sql.append(" order by la.idUsuario.nome ");

        return getPureList(sql.toString(), empresaLogada);
    }

    public List<Usuario> listarUsuarioVendedorPorEmpresaLogada(Empresa empresaLogada) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select u from LoginAcesso la ");
        sql.append(" join la.idUsuario u ");
        sql.append(" join u.idFuncionario f ");
        sql.append(" where la.idEmpresa= ?1 ");
        sql.append(" and f.ehOrcamentista = 'S' ");
        sql.append(" and f.dataDemissao is null ");

        return getPureList(sql.toString(), empresaLogada);
    }

    public List<Usuario> listarUsuarioPorEmpresaLogada(Empresa empresaLogada) {
        QueryBuilder qb = new QueryBuilder();
        qb.select("u")
                .from(LoginAcesso.class)
                .join("la.idUsuario", "u")
                .where("la.idEmpresa = ?1 and u.idPerfil.tipo != 'AD'");

        return getPureList(qb, empresaLogada);
    }

    public Grupo getGrupoByDescricao(String descricao) {
        return getPurePojo("select grupo from Grupo grupo where grupo.descricao = ?1", descricao);
    }

    public List<Usuario> getUsuariosServidores() {
        return getPureList("select usr from Usuario usr where usr.idServidor is not null");
    }

    public List<Usuario> getUsuariosServidoresSimplificado() {
        return getPureList("select new br.com.villefortconsulting.entidades.Usuario(usu.id) from Usuario usu where usu.idServidor is not null");
    }

    public Usuario getUserByLogin(String login) {
        return getPurePojoTop1("select usr from Usuario usr where usr.login = ?1 order by usr.id ", login);
    }

    public Usuario getUserById(Funcionario funcionario) {
        return getPurePojo("select usr from Usuario usr where usr.idFuncionario = ?1 ", funcionario);
    }

    public boolean existeUserByLogin(String login) {
        return getPurePojo(Long.class, "select count(usr) from Usuario usr where usr.login = ?1 and usr.contaBloqueada = 'N' and usr.contaExpirada = 'N'", login) > 0;
    }

    public UsuarioPermissao addUsuarioPermissao(UsuarioPermissao usr) {
        return addEntity(usr);
    }

    public UsuarioPermissao setUsuarioPermissao(UsuarioPermissao usr) {
        return setEntity(usr);
    }

    public List<UsuarioPermissao> getPermissoes(Usuario usuario) {
        return getPureList("select perm from UsuarioPermissao perm where perm.idUsuario = ?1 order by perm.idPermissao.descricao", usuario);
    }

    public List<Permissao> getPermissoesUsuario(Usuario usuario) {
        return getPureList("select perm.idPermissao from UsuarioPermissao perm where perm.idUsuario = ?1 ", usuario);
    }

    public Permissao getPermissaoPorDescricao(Usuario usuario, String descricao) {
        return getPurePojoTop1("select perm.idPermissao from UsuarioPermissao perm where perm.idUsuario = ?1 and perm.idPermissao.descricao = ?2 ", usuario, descricao);
    }

    public Permissao getPermissaoGrupoPorDescricao(Usuario usuario, String descricao) {

        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct perm.idPermissao from GrupoPermissao perm  ");
        sql.append(" where perm.idGrupo in ( ");
        sql.append(" select distinct grp.idGrupoEmpresa.idGrupo from UsuarioGrupoEmpresa grp where grp.idUsuario = ?1 ");
        sql.append(" ) and perm.idPermissao.descricao = ?2   ");

        return getPurePojoTop1(sql.toString(), usuario, descricao);
    }

    public List<Permissao> getPermissaoGrupo(Usuario usuario, String tenant) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct perm.idPermissao from GrupoPermissao perm ");
        sql.append(" where perm.idGrupo in ( ");
        sql.append(" select distinct grp.idGrupoEmpresa.idGrupo from UsuarioGrupoEmpresa grp  ");
        sql.append(" where grp.idUsuario = ?1 and grp.idGrupoEmpresa.idEmpresa.tenatID = ?2  ");
        sql.append(" ) ");

        return getPureList(sql.toString(), usuario, tenant);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresaList(Empresa empresa) {
        return getPureList("select grp from UsuarioGrupoEmpresa grp where grp.idGrupoEmpresa.idEmpresa = ?1 ", empresa);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresaList(Usuario usuario, Empresa empresa) {
        return getPureList("select grp from UsuarioGrupoEmpresa grp where grp.idUsuario = ?1 and grp.idGrupoEmpresa.idEmpresa = ?2 ", usuario, empresa);
    }

    public UsuarioGrupoEmpresa addUsuarioGrupoEmpresa(UsuarioGrupoEmpresa uge) {
        return addEntity(uge);
    }

    public void removeUsuarioGrupoEmpresa(UsuarioGrupoEmpresa usr) {
        removeEntity(usr);
    }

    public List<Usuario> getListaFiltrada(List<Criterion> criterions, UsuarioFiltro filtro) {
        return getListaFiltrada(Usuario.class, criterions, filtro);
    }

    public List<Usuario> getListaFiltrada(UsuarioFiltro filtro, Criteria criteria) {
        return getListaFiltrada(criteria, filtro);
    }

    public List<Usuario> getServidroUserByName(String name) {
        return getPureList("select usr from select new br.com.villefortconsulting.entidades.Usuario(usu.id, usu.nome) usr where usr.nome like ?1 and usr.idPerfil.tipo in('MR','SE') order by usr.nome", "%" + name + "%");
    }

    public List<Usuario> getUsuariosComMensagensObrigatoriasPendentesLeitura(Calendar dataExpiracao) {
        return getPureList("select distinct msg.idUsuarioDestino from MensagemDestinatario msg where msg.idMensagem.leituraObrigatoria = 'S' and msg.dataLeitura is null and msg.idMensagem.data <= ?1 ", dataExpiracao.getTime());
    }

    public boolean verificarUsuarioComMensagensObrigatoriasPendentesLeitura(Usuario usuario, Calendar dataExpiracao) {
        return getPurePojo(Long.class, "select count(msg) from MensagemDestinatario msg where msg.idMensagem.leituraObrigatoria = 'S' and msg.dataLeitura is null and msg.idMensagem.data <= ?1 and msg.idUsuarioDestino = ?2", dataExpiracao.getTime(), usuario) > 0;
    }

    public List<Usuario> listarUsuariosMasterComEmail(Empresa empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct tenatID.idUsuario   ")
                .append(" from LoginAcesso tenatID ")
                .append(" where tenatID.idUsuario.idPerfil.tipo = ?1 ")
                .append(" and tenatID.idEmpresa = ?2 ")
                .append(" and tenatID.idUsuario.email is not null ");
        return getPureList(sql.toString(), EnumTipoUsuario.MASTER_USUARIO.getTipo(), empresa);
    }

    public List<Usuario> getUsuariosPorNome(String nome) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct usu from Usuario usu  ")
                .append(" where usu.nome like ?1 ")
                .append(" or usu.login like ?1 ");
        return getPureList(sql.toString(), "%" + nome + "%");
    }

    public List<Usuario> getUsuariosNaoSuspensosPorNome(String nome) {
        return getPureList("select distinct usu from Usuario usu where usu.nome like ?1 and usu not in (select sus.idUsuarioSuspenso from UsuarioSuspensao sus where sus.dataRetornoSuspensao is null)", "%" + nome + "%");
    }

    public List<Usuario> getUsuarioPesquisaPorNomeContrato(String nome) {
        return getPureList("select distinct con.idUsuarioContrato from Contrato con where con.idUsuarioContrato.nome like ?1", "%" + nome + "%");
    }

    public boolean verificarUsuarioEstaSuspenso(Usuario usuario) {
        return getPurePojo(Long.class, "select count(usu) from UsuarioSuspensao usu where usu.idUsuarioSuspenso = ?1 and usu.dataRetornoSuspensao is null", usuario) > 0;
    }

    public String getEmailUsuario(Usuario usuario) {
        return getPurePojo("select usr.email from Usuario usr where usr = ?1", usuario);
    }

    public List<Usuario> listarUsuarioPorPerfil(String tipo, String tenant) {
        return getPureList("select usu from Usuario usu where usu.idPerfil.tipo = ?1 and usu.tenatID = ?2 ", tipo, tenant);
    }

    public Usuario getUserByLogin(String login, Date dataNascimento) {
        return getPurePojo("select usr from Usuario usr where usr.login = ?1, usr.idServidor. = ?2, use", login);
    }

    public LoginAcesso getLoginAcessoByUser(Usuario usuario) {
        return getPurePojo("select la from LoginAcesso la where la.idUsuario.login =?1", usuario.getLogin());
    }

    public UsuarioGrupoEmpresa getUsuarioGrupoEmpresa(Usuario usuario, GrupoEmpresa grupoEmpresa) {
        return getPurePojo("select uge from UsuarioGrupoEmpresa uge where uge.idGrupoEmpresa = ?1 and uge.idUsuario = ?2 ", grupoEmpresa, usuario);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresa(GrupoEmpresa grupoEmpresa) {
        return getPureList("select uge from UsuarioGrupoEmpresa uge where uge.idGrupoEmpresa = ?1 order by uge.idUsuario.nome", grupoEmpresa);
    }

    public List<Usuario> listarUsuarioSemAcessoEmpresaPorContabilidade(Empresa empresa, Contabilidade contabilidade) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select u from Usuario u ");
        sql.append(" where u.idContabilidade = ?1 ");
        sql.append(" and u.id not in (select l.idUsuario from LoginAcesso l where l.idUsuario.id = u.id and l.idEmpresa = ?2 ) ");

        return getPureList(sql.toString(), contabilidade, empresa);
    }

    public List<Usuario> listarUsuarioComAcessoEmpresaComContabilidade(Empresa empresa) {

        // !Muito cuidado ao alterar esta query!
        StringBuilder sql = new StringBuilder();
        sql.append(" select u from Usuario u ");
        sql.append(" where u.idContabilidade is not null ");
        sql.append(" and u.id in (select l.idUsuario from LoginAcesso l where l.idUsuario.id = u.id and l.idEmpresa = ?1 ) ");

        return getPureList(sql.toString(), empresa);
    }

    public boolean hasAny(String name, Empresa empresaLogada) {
        // Maior que 1 para ignorar o usuário logado
        return getUsuarioPorEmpresaLogada(name, empresaLogada).size() > 1;
    }

    public List<Usuario> listarUsuarioPorPerfil(String tipo) {
        return getPureList("select usu from Usuario usu where usu.idPerfil.tipo = ?1", tipo);
    }

    public List<UsuarioAcessoRapido> getUsuarioAcessoRapidoList(Usuario usuario) {
        return getPureList("select uar from UsuarioAcessoRapido uar where uar.idUsuario = ?1", usuario);
    }

    public Usuario getUserByFuncionario(Funcionario funcionario) {
        return getPurePojo("select u from Usuario u where u.idFuncionario = ?1", funcionario);
    }

    public List<Usuario> listarUsuarioQueRecebemNotificacaoIugu(String tenat) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l.idUsuario from LoginAcesso l  ")
                .append(" join l.idUsuario usu  ")
                .append(" join l.idEmpresa emp ")
                .append(" where l.idUsuario.receberEmailIUGU = 'S' and emp.id = ?1");
        return getPureList(sql.toString(), Integer.parseInt(tenat));
    }

    public boolean leuVersaoTermo(Usuario usuario, Integer versaoTermo) {
        if (usuario == null) {
            return true;
        }
        return getPurePojo(Long.class, "select count(ult.id) from UsuarioLeituraTermo ult where ult.idUsuario = ?1 and ult.versaoTermo = ?2", usuario, versaoTermo) > 0;
    }

    public void removerLeituraTermo(Usuario user) {
        executeCommand("delete from UsuarioLeituraTermo ult where ult.idUsuario = ?1", user);
    }

    public List<Usuario> listarUsuariosRecebemNotificacaoAcessosPorEmpresa() {
        return getPureList("select u from Usuario u where u.receberEmailAcessoEmpresa = 'S'");
    }

}
