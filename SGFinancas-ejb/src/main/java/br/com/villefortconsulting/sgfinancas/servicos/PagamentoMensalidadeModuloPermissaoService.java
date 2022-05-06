package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PagamentoMensalidadeModuloPermissaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PagamentoMensalidadeModuloPermissaoRepositorio;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PagamentoMensalidadeModuloPermissaoService extends BasicService<PagamentoMensalidadeModuloPermissao, PagamentoMensalidadeModuloPermissaoRepositorio, PagamentoMensalidadeModuloPermissaoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private ModuloService moduloService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private PagamentoMensalidadeModuloService pagamentoMensalidadeModuloService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private GrupoService grupoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PagamentoMensalidadeModuloPermissaoRepositorio(em, adHocTenant);
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPor(Empresa empresa) {
        return repositorio.getPermissoesParaUsuarioMasterPor(empresa);
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPorEmpresaEDescricao(Empresa empresa, String descricao) {
        return repositorio.getPermissoesParaUsuarioMasterPorEmpresaEDescricao(empresa, descricao);
    }

    public List<PagamentoMensalidadeModuloPermissao> getPermissoesParaUsuarioMasterPor(PagamentoMensalidade pm, String descricao) {
        return repositorio.getPermissoesParaUsuarioMasterPor(pm, descricao == null ? "" : descricao);
    }

    public List<PagamentoMensalidadeModuloPermissao> permissoesPor(PagamentoMensalidadeModulo pmm) {
        return moduloService.permissoesPor(pmm.getIdModulo()).stream()
                .map(permissao -> {
                    PagamentoMensalidadeModuloPermissao pmmp = new PagamentoMensalidadeModuloPermissao();
                    pmmp.setIdPagamentoMensalidadeModulo(pmm);
                    pmmp.setIdPermissao(permissao);
                    pmmp.setTenatID(pmm.getTenatID());
                    return pmmp;
                })
                .collect(Collectors.toList());

    }

    public void atualizaPermissoes(Empresa empresa, final boolean atualizaPagamentoMensalidadeModuloPermissaoList) {
        PagamentoMensalidade pm = pagamentoMensalidadeService.getUltimoPagamentoPor(empresa);
        if (pm == null) {
            return;
        }
        List<Permissao> permissoes = pm.getPagamentoMensalidadeModuloList().stream()
                .map(pmm -> {
                    List<Permissao> aux = moduloService.permissoesPor(pmm.getIdModulo());
                    if (atualizaPagamentoMensalidadeModuloPermissaoList) {
                        pmm.getPagamentoMensalidadeModuloPermissaoList().clear();
                        pmm.getPagamentoMensalidadeModuloPermissaoList().addAll(aux.stream()
                                .map(p -> new PagamentoMensalidadeModuloPermissao(pmm, p))
                                .collect(Collectors.toList()));
                        pagamentoMensalidadeModuloService.salvar(pmm);
                    }
                    return aux;
                })
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
        List<Usuario> usuarios = loginAcessoService.getAcessos(empresa.getTenatID()).stream()
                .map(LoginAcesso::getIdUsuario)
                .filter(user -> !user.getIdPerfil().getEhSuporte())
                .collect(Collectors.toList());
        usuarios.stream()
                .map(user -> {
                    user.getUsuarioPermissaoList().clear();
                    user.getUsuarioPermissaoList().addAll(usuarioService.getPermissoes(user).stream()
                            .filter(pu -> permissoes.stream().anyMatch(p -> p.equals(pu.getIdPermissao())))
                            .collect(Collectors.toList()));
                    return user;
                })
                .forEach(usuarioService::salvar);

        grupoService.getGrupos()
                .stream()
                .map(grupo -> {
                    grupo.getGrupoPermissaoList().clear();
                    grupo.getGrupoPermissaoList().addAll(grupoService.getPermissoes(grupo).stream()
                            .filter(pg -> permissoes.stream().anyMatch(p -> p.equals(pg.getIdPermissao())))
                            .collect(Collectors.toList()));
                    return grupo;
                })
                .forEach(grupoService::salvar);
    }

}
