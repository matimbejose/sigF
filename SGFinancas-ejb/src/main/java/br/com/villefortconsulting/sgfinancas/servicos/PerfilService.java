package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.PermissaoPerfil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PerfilFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PerfilRepositorio;
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

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PerfilService extends BasicService<Perfil, PerfilRepositorio, PerfilFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new PerfilRepositorio(em);
    }

    public Perfil getPerfil(String tipoPerfil) {
        return repositorio.getPerfil(tipoPerfil);
    }

    public List<Perfil> getPerfisPorPerfil(Perfil perfil) {
        return repositorio.getPerfisPorPerfil(perfil);
    }

    public List<Perfil> getPerfis() {
        return repositorio.getPerfis();
    }

    public List<Perfil> getPerfisComSuporte() {
        return repositorio.getPerfisComSuporte();
    }

    public List<PermissaoPerfil> getPermissoes(Perfil obj) {
        return repositorio.getPermissoes(obj);
    }

    @Override
    public Criteria getModel(PerfilFiltro filter) {
        Criteria criteria = super.getModel(filter);

        addIlikeRestrictionTo(criteria, "descricao", filter.getDescricao(), MatchMode.ANYWHERE);

        return criteria;
    }

    public List<Perfil> getPerfisVendedor() {
        return repositorio.getPerfisVendedor();
    }

    public List<Perfil> getPerfisUsuario() {
        return repositorio.getPerfisUsuario();
    }

    public Perfil getPerfilCliente() {
        return repositorio.getPerfil(EnumTipoUsuario.CLIENTE.getTipo());
    }

}
