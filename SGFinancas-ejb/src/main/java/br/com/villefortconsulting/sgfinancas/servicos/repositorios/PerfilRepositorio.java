package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.PermissaoPerfil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PerfilFiltro;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class PerfilRepositorio extends BasicRepository<Perfil> {

    public PerfilRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public Perfil getPerfil(String tipoPerfil) {
        return getPurePojoTop1("select p from Perfil p where p.tipo = ?1 ", tipoPerfil);
    }

    public List<Perfil> getListaFiltrada(List<Criterion> criterions, PerfilFiltro filtro) {
        return getListaFiltrada(Perfil.class, criterions, filtro);
    }

    public List<PermissaoPerfil> getPermissoes(Perfil obj) {
        return getPureList("select perm from PermissaoPerfil perm where perm.idPerfil = ?1 ", obj);
    }

    public List<Perfil> getPerfis() {
        return getPureList("select p from Perfil p where p.tipo <> ?1", EnumTipoUsuario.ADMINISTRADOR.getTipo());
    }

    public List<Perfil> getPerfisPorPerfil(Perfil perfil) {
        if (Boolean.TRUE.equals(perfil.getEhSuporte())) {
            return getPureList("select p from Perfil p ");
        } else if (Boolean.TRUE.equals(perfil.getEhMasterVendedor())) {
            return getPureList("select p from Perfil p where p.tipo in (?1,?2) ", EnumTipoUsuario.VENDEDOR.getTipo(), EnumTipoUsuario.MASTER_VENDEDOR.getTipo());
        } else if (Boolean.TRUE.equals(perfil.getEhUsuarioMaster())) {
            return getPureList("select p from Perfil p where p.tipo not in (?1,?2) ", EnumTipoUsuario.ADMINISTRADOR.getTipo(), EnumTipoUsuario.FUNCIONARIO.getTipo());
        } else if (Boolean.TRUE.equals(perfil.getEhMasterContabilidade())) {
            return getPureList("select p from Perfil p where p.tipo in (?1,?2) ", EnumTipoUsuario.MASTER_CONTABILIDADE.getTipo(), EnumTipoUsuario.CONTABILIDADE.getTipo());
        }
        return getPureList("select p from Perfil p where p.tipo = ?1  and p.tipo <> ?2 ", perfil.getTipo(), EnumTipoUsuario.FUNCIONARIO.getTipo());
    }

    public List<Perfil> getPerfisComSuporte() {
        return getPureList("select p from Perfil p order by p.descricao");
    }

    public List<Perfil> getPerfisVendedor() {
        return getPureList("select perfil from Perfil perfil where perfil.tipo in (?1,?2)", EnumTipoUsuario.VENDEDOR.getTipo(), EnumTipoUsuario.MASTER_VENDEDOR.getTipo());
    }

    public List<Perfil> getPerfisUsuario() {
        return getPureList("select perfil from Perfil perfil where perfil.tipo in (?1,?2)", EnumTipoUsuario.MASTER_USUARIO.getTipo(), EnumTipoUsuario.MASTER_USUARIO.getTipo());
    }

}
