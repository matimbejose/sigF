package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import java.util.List;
import javax.persistence.EntityManager;

public class GrupoRepositorio extends BasicRepository<Grupo> {

    public GrupoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public GrupoRepositorio(EntityManager em, AdHocTenant adHocTenant) {
        super(em, adHocTenant);
    }

    public List<Grupo> getGrupos() {
        return getPureList("select grp from Grupo grp order by grp.descricao");
    }

    public List<Grupo> getGruposPadroes() {
        return getPureList("select grp from Grupo grp where grp.gestaoInterna = 'S' order by grp.descricao");
    }

    public List<Grupo> getGruposSemSuporte() {
        return getPureList("select grp from Grupo grp where grp.gestaoInterna = 'S' and tipo <> ?1 order by grp.descricao", EnumTipoUsuario.ADMINISTRADOR.getTipo());
    }

    public List<Grupo> getGrupos(String descricao) {
        descricao = "%" + descricao + "%";

        return getPureList("select grp from Grupo grp where grp.descricao like ?1 order by grp.descricao", descricao);
    }

    public Grupo getGrupoPorTipo(String tipo) {
        return getPurePojoTop1("select grp from Grupo grp where grp.tipo = ?1 and grp.gestaoInterna = 'S' ", tipo);
    }

    public List<GrupoPermissao> getPermissoes(Grupo grupo) {
        return getPureList("select perm from GrupoPermissao perm where perm.idGrupo = ?1 ", grupo);
    }

    public List<Permissao> getPermissoesGrupo(Grupo grupo) {
        return getPureList("select perm.idPermissao from GrupoPermissao perm where perm.idGrupo = ?1 ", grupo);
    }

    public Grupo listarGrupo(Empresa empresa) {
        return getPurePojo("select grp.idGrupo from GrupoEmpresa grp where grp.id = ?1 ", empresa);
    }

    public GrupoEmpresa buscarGrupoEmpresa(Integer id) {
        return getPurePojo("select grp from GrupoEmpresa grp where grp.id = ?1 ", id);
    }

    public GrupoEmpresa buscarGrupoEmpresa(Grupo grupo, Empresa empresa) {
        return getPurePojo("select grp from GrupoEmpresa grp where grp.idGrupo = ?1 and grp.idEmpresa = ?2 ", grupo, empresa);
    }

    public GrupoEmpresa buscarGrupoEmpresaPorTipoGrupo(String tipo, Empresa empresa) {
        return getPurePojo("select grp from GrupoEmpresa grp where grp.idGrupo.tipo = ?1 and grp.idEmpresa = ?2 ", tipo, empresa);
    }

    public GrupoEmpresa adicionarGrupoEmpresa(GrupoEmpresa grp) {
        addEntity(grp);
        return grp;
    }

    public GrupoEmpresa alterarGrupoEmpresa(GrupoEmpresa grp) {
        return setEntity(grp);
    }

    public void removerGrupoEmpresa(GrupoEmpresa grp) {
        removeEntity(grp);
    }

    public UsuarioGrupoEmpresa adicionarUsuarioGrupoEmpresa(UsuarioGrupoEmpresa grp) {
        addEntity(grp);
        return grp;
    }

    public UsuarioGrupoEmpresa alterarUsuarioGrupoEmpresa(UsuarioGrupoEmpresa grp) {
        return setEntity(grp);
    }

    public void removerUsuarioGrupoEmpresa(UsuarioGrupoEmpresa grp) {
        removeEntity(grp);
    }

    public List<GrupoEmpresa> listarGrupoEmpresa(Empresa empresa) {
        return getPureList("select ge from GrupoEmpresa ge where ge.idEmpresa = ?1 ", empresa);
    }

}
