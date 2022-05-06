package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.GrupoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.GrupoRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GrupoService extends BasicService<Grupo, GrupoRepositorio, GrupoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private EmpresaService empresaService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new GrupoRepositorio(em);
    }

    public GrupoEmpresa adicionarGrupoEmpresa(GrupoEmpresa grp) {
        return repositorio.adicionarGrupoEmpresa(grp);
    }

    public GrupoEmpresa alterarGrupoEmpresa(GrupoEmpresa grp) {
        return repositorio.alterarGrupoEmpresa(grp);
    }

    public void removerGrupoEmpresa(GrupoEmpresa grp) {
        repositorio.removerGrupoEmpresa(grp);
    }

    public List<Grupo> getGrupos() {
        return repositorio.getGrupos();
    }

    public List<GrupoPermissao> getPermissoes(Grupo grupo) {
        return repositorio.getPermissoes(grupo);
    }

    public List<Permissao> getPermissoesGrupo(Grupo grupo) {
        return repositorio.getPermissoesGrupo(grupo);
    }

    public List<Grupo> getGrupos(String descricao) {
        return repositorio.getGrupos(descricao);
    }

    public List<Grupo> getGruposPadroes() {
        return repositorio.getGruposPadroes();
    }

    public List<Grupo> getGruposSemSuporte() {
        return repositorio.getGruposSemSuporte();
    }

    public Grupo getGrupoPorTipo(String tipo) {
        return repositorio.getGrupoPorTipo(tipo);
    }

    public List<GrupoEmpresa> listaGrupoEmpresa(Empresa empresa) {
        return repositorio.listarGrupoEmpresa(empresa);
    }

    public GrupoEmpresa salvarGrupoEmpresa(GrupoEmpresa grupoEmpresa) {
        boolean nomeRepetido = listaGrupoEmpresa(empresaService.getEmpresa()).stream()
                .filter(s -> s.getIdGrupo().getDescricao().equals(grupoEmpresa.getIdGrupo().getDescricao()))
                .anyMatch(s -> grupoEmpresa.getId() == null || !grupoEmpresa.getId().equals(s.getId()));
        if (nomeRepetido) {
            throw new CadastroException("Grupo já cadastrado. Favor alterar o nome.", null);
        }
        if (grupoEmpresa.getId() != null) {
            return alterarGrupoEmpresa(grupoEmpresa);
        } else {
            return adicionarGrupoEmpresa(grupoEmpresa);
        }
    }

    public List<GrupoEmpresa> getListaGrupoFiltrado(GrupoFiltro filtro) {
        Criteria criteria = getFiltroListaGrupo(filtro);
        return repositorio.getListaFiltrada(criteria, filtro);
    }

    public int quantidadeGrupoFiltrado(GrupoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getFiltroListaGrupo(filtro));
    }

    public Grupo getGrupo(String tipo) {
        return repositorio.getGrupoPorTipo(tipo);
    }

    public GrupoEmpresa getGrupoEmpresa(Grupo grupo, Empresa empresa) {
        return repositorio.buscarGrupoEmpresa(grupo, empresa);
    }

    private Criteria getFiltroListaGrupo(GrupoFiltro filtro) {

        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(GrupoEmpresa.class);

        criteria.createAlias("idGrupo", "idGrupo");
        criteria.createAlias("idEmpresa", "idEmpresa");

        if (filtro.getEmpresa() != null) {
            criteria.add(Restrictions.eq("idEmpresa", filtro.getEmpresa()));
        }

        if (StringUtils.isNotEmpty(filtro.getDescricao())) {
            criteria.add(Restrictions.ilike("idGrupo.descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
        }

        Perfil perfil = filtro.getUsuarioLogado().getIdPerfil();

        // Visualiza somente os grupos que nao sao de gestao do suporte
        if (!Boolean.TRUE.equals(perfil.getEhSuporte()) && !"S".equals(filtro.getVisualizaGestaoInterna())) {
            criteria.add(Restrictions.eq("idGrupo.gestaoInterna", "N"));
        }

        if (!Boolean.TRUE.equals(perfil.getEhSuporte())) {

            //Usuário não visualiza usuarios com perfil de administrador
            if (Boolean.TRUE.equals(perfil.getEhMasterVendedor())) {
                Criterion c1 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.MASTER_VENDEDOR.getTipo());
                Criterion c2 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.VENDEDOR.getTipo());
                criteria.add(Restrictions.or(c1, c2));
            } else if (Boolean.TRUE.equals(perfil.getEhUsuarioMaster())) {
                Criterion c1 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.MASTER_USUARIO.getTipo());
                Criterion c2 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.USUARIO_COMUM.getTipo());
                criteria.add(Restrictions.or(c1, c2));
            } else if (Boolean.TRUE.equals(perfil.getEhMasterContabilidade())) {
                Criterion c1 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.MASTER_CONTABILIDADE.getTipo());
                Criterion c2 = Restrictions.eq("idGrupo.tipo", EnumTipoUsuario.CONTABILIDADE.getTipo());
                criteria.add(Restrictions.or(c1, c2));
                // Visualiza somente ele mesmo
            } else {
                criteria.add(Restrictions.eq("id", filtro.getUsuarioLogado().getId()));
            }

        }

        return criteria;

    }

    public GrupoEmpresa buscarGrupoEmpresaPorTipoGrupo(String tipo, Empresa empresa) {
        return repositorio.buscarGrupoEmpresaPorTipoGrupo(tipo, empresa);
    }

    public UsuarioGrupoEmpresa adicionarUsuarioGrupoEmpresa(UsuarioGrupoEmpresa usuarioGrupoEmpresa) {
        return repositorio.adicionarUsuarioGrupoEmpresa(usuarioGrupoEmpresa);
    }

    public UsuarioGrupoEmpresa alterarUsuarioGrupoEmpresa(UsuarioGrupoEmpresa usuarioGrupoEmpresa) {
        return repositorio.alterarUsuarioGrupoEmpresa(usuarioGrupoEmpresa);
    }

    public void removerUsuarioGrupoEmpresa(UsuarioGrupoEmpresa usuarioGrupoEmpresa) {
        repositorio.removerUsuarioGrupoEmpresa(usuarioGrupoEmpresa);
    }

}
