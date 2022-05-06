package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaUsuarioAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

public class LoginAcessoRepositorio extends BasicRepository<LoginAcesso> {

    public LoginAcessoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public LoginAcesso getLoginAcesso(String login) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l from LoginAcesso l  ")
                .append(" left join fetch l.idUsuario usu  ")
                .append(" left join fetch usu.idPerfil  ")
                .append(" left join fetch l.idEmpresa emp ")
                .append(" left join fetch emp.endereco.idCidade ")
                .append(" left join fetch emp.idCnae ")
                .append(" left join fetch emp.idContabilidade ")
                .append(" left join fetch emp.idDocumento ")
                .append(" where l.idUsuario.login = ?1  ")
                .append(" order by l.dataAcesso desc ");
        return getPurePojoTop1(sql.toString(), login);
    }

    public LoginAcesso getLoginAcesso(String login, String tenat) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l from LoginAcesso l  ")
                .append(" where l.idUsuario.login = ?1  ")
                .append(" and l.idEmpresa.tenatID = ?2  ")
                .append(" order by l.dataAcesso desc ");
        return getPurePojoTop1(sql.toString(), login, tenat);
    }

    public List<LoginAcesso> getAcessos(String tenat) {
        return getPureList("select l from LoginAcesso l where l.idEmpresa.tenatID = ?1 order by l.dataAcesso desc", tenat);
    }

    public List<LoginAcesso> getAcessos(Usuario usuario) {
        return getPureList("select la from LoginAcesso la where la.idUsuario = ?1 order by la.dataAcesso desc", usuario);
    }

    public Usuario getUserByLogin(String login) {
        return getPurePojoTop1("select usr from Usuario usr where usr.login = ?1 order by usr.id", login);
    }

    public List<Empresa> getTenats() {
        return getPureList("select distinct l.idEmpresa from LoginAcesso l ");
    }

    public List<Empresa> getTenats(String login) {
        return getPureList("select l.idEmpresa from LoginAcesso l where l.idUsuario.login = ?1 ", login);
    }

    public Integer getQtdTenats(String login) {
        return getPurePojo(Long.class, "select count(l) from LoginAcesso l where l.idUsuario.login = ?1 ", login).intValue();
    }

    public List<Empresa> getTodosTenats() {
        return getPureList("select p from Empresa p order by p.descricao ");
    }

    public boolean existeUserByLogin(String login, String tenant) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(login) from LoginAcesso login ");
        sql.append(" join login.idUsuario u ");
        sql.append(" join login.idEmpresa e ");
        sql.append(" where u.login = ?1 ");
        sql.append(" and e.tenatID = ?2 ");
        sql.append(" and u.contaBloqueada = 'N' ");
        sql.append(" and u.contaExpirada = 'N' ");
        return getPurePojo(Long.class, sql.toString(), login, tenant) > 0;
    }

    public LoginAcesso getLoginAcesso(String login, Empresa empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l from LoginAcesso l ")
                .append(" where l.idUsuario.login = ?1 ")
                .append(" and l.idEmpresa = ?2 ")
                .append(" order by l.idUsuario.id ");

        return getPurePojoTop1(sql.toString(), login, empresa);
    }

    public LoginAcesso getLoginAcesso(Usuario usuario, Empresa empresa) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select l from LoginAcesso l ")
                .append(" where l.idUsuario = ?1 ")
                .append(" and l.idEmpresa = ?2 ");

        return getPurePojoTop1(sql.toString(), usuario, empresa);
    }

    public List<EmpresaUsuarioAcesso> getEmpresaAcesso(Empresa empresa) {
        String jpql = "select p from EmpresaUsuarioAcesso where p.idEmpresa =?1 p order by p.id ";
        return getPureList(jpql, empresa);
    }

    public EmpresaUsuarioAcesso addEmpresaUsuarioAcesso(EmpresaUsuarioAcesso empresaUsuarioAcesso) {
        return addEntity(empresaUsuarioAcesso);
    }

    public Usuario getCredorByEmpresa(Empresa credorSelecionado) {
        return getPurePojoTop1("select l.idUsuario from LoginAcesso l where l.idEmpresa = ?1 ", credorSelecionado);
    }

    public Empresa salvarEmpresa(Empresa empresa) {
        if (empresa.getTenatID() == null) {
            return null;
        }
        return entityManager.merge(empresa);
    }

}
