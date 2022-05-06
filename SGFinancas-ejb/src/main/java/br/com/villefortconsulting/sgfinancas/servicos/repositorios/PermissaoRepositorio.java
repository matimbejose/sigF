package br.com.villefortconsulting.sgfinancas.servicos.repositorios;

import br.com.villefortconsulting.basic.BasicRepository;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.PermissaoPerfil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PermissaoFiltro;
import java.util.List;
import javax.persistence.EntityManager;
import org.hibernate.criterion.Criterion;

public class PermissaoRepositorio extends BasicRepository<Permissao> {

    public PermissaoRepositorio(EntityManager entityManager) {
        super(entityManager);
    }

    public List<Permissao> getPermissoes() {
        return getPureList("select perm from Permissao perm order by perm.descricao");
    }

    public List<Permissao> getPermissoes(Perfil perfil) {
        return getPureList("select perm.idPermissao from PermissaoPerfil perm where perm.idPerfil.tipo = ?1", perfil.getTipo());
    }

    public List<Permissao> getPermissoes(String descricao) {
        return getPureList("select perm from Permissao perm where upper(perm.descricaoDetalhada) like ?1 order by perm.descricaoDetalhada", "%" + descricao.toUpperCase() + "%");
    }

    public List<PermissaoPerfil> getPermissoesPerfil(String descricao) {
        return getPureList("select perm from PermissaoPerfil perm where idPermissao like ?1 order by perm.idPermissao.descricaoDetalhada", "%" + descricao.toUpperCase() + "%");
    }

    public List<Permissao> getListaFiltrada(List<Criterion> criterions, PermissaoFiltro filtro) {
        return getListaFiltrada(Permissao.class, criterions, filtro);
    }

    public List<Permissao> getPermissoesPorPerfil(String descricao, String tipo) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select distinct perm.idPermissao from PermissaoPerfil perm ");
        sql.append(" where perm.idPerfil.tipo = ?1 ");

        if (descricao != null) {
            sql.append(" and perm.idPermissao.descricaoDetalhada like '%").append(descricao).append("%'");
        }
        sql.append(" order by perm.idPermissao.descricaoDetalhada ");

        return getPureList(sql.toString(), tipo);
    }

    public Permissao buscarPorNome(String nome) {
        return getPurePojoTop1(" select p from Permissao p where descricao = ?1 ", nome.toUpperCase());
    }

}
