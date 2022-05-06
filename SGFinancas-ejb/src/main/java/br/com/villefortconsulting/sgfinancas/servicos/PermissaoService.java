package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.PermissaoPerfil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PermissaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PermissaoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

/**
 *
 * @author Christopher
 */
@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PermissaoService extends BasicService<Permissao, PermissaoRepositorio, PermissaoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new PermissaoRepositorio(em);
    }

    public List<Permissao> getPermissoes() {
        return repositorio.getPermissoes();
    }

    public List<Permissao> getPermissoes(Perfil perfil) {
        return repositorio.getPermissoes(perfil);
    }

    public List<Permissao> getPermissoes(String descricao) {
        return repositorio.getPermissoes(descricao == null ? "" : descricao);
    }

    public List<PermissaoPerfil> getPermissoesPerfil(String descricao) {
        descricao = (descricao == null) ? "" : descricao;
        return repositorio.getPermissoesPerfil(descricao);
    }

    @Override
    public Criteria getModel(PermissaoFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public List<Permissao> getPermissoesPorPerfil(String descricao, String tipoPerfil) {
        if (tipoPerfil.equals(EnumTipoUsuario.ADMINISTRADOR.getTipo())) {
            return getPermissoes(descricao);
        } else {
            return repositorio.getPermissoesPorPerfil(descricao, tipoPerfil);
        }
    }

    public Permissao buscarPorNome(String nome) {
        return repositorio.buscarPorNome(nome);
    }

}
