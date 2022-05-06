package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.AcessoBloqueadoException;
import br.com.villefortconsulting.exception.ContaBloqueadaException;
import br.com.villefortconsulting.exception.LoginNaoEncontratoException;
import br.com.villefortconsulting.exception.UsuarioOuSenhaInvalidaException;
import br.com.villefortconsulting.sgfinancas.aud.CustomRevisionEntity;
import br.com.villefortconsulting.sgfinancas.entidades.ControleAcessoIp;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaUsuarioAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LoginAcessoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.ControleAcessoIpRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.EmpresaRepositorio;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.LoginAcessoRepositorio;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.sql.JoinType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LoginAcessoService extends BasicService<LoginAcesso, LoginAcessoRepositorio, LoginAcessoFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Resource
    transient SessionContext context;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PagamentoMensalidadeService pmService;

    @EJB
    private PagamentoMensalidadeModuloPermissaoService pmmpService;

    @EJB
    private PermissaoService permissaoService;

    private LoginAcesso loginAcesso;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new LoginAcessoRepositorio(em);
    }

    public LoginAcesso adicionarLoginAcesso(Usuario usuario, Empresa empresa) {
        if (usuario.getLoginAcesso() == null) {
            return adicionar(LoginAcesso.builder()
                    .dataAcesso(DataUtil.getHoje())
                    .idUsuario(usuario)
                    .idEmpresa(empresa)
                    .build());
        }
        return null;
    }

    public EmpresaRepositorio getEmpresaRepositorio(String tenat) {
        return new EmpresaRepositorio(em, adHocTenant);
    }

    public ControleAcessoIpRepositorio getControleAcessoIpRepositorio(String tenat) {
        return new ControleAcessoIpRepositorio(em);
    }

    public void limpaCache() {
        repositorio.limpaCache();
    }

    public boolean existeUserByLogin(String login) {
        return repositorio.existeUserByLogin(login, adHocTenant.getTenantID());
    }

    public Usuario autenticarUsuario(String login, String senha, boolean senhaJaCriptografada, String tenatID, boolean isApp, boolean addLogin) throws LoginNaoEncontratoException, UsuarioOuSenhaInvalidaException, AcessoBloqueadoException, ContaBloqueadaException {
        if (login.isEmpty() || senha.isEmpty()) {
            throw new LoginNaoEncontratoException("Informe o usuário e a senha.");
        }

        if (null != tenatID) {
            getLoginAcesso(login, tenatID);
        } else {
            getLoginAcesso(login);
        }

        if (loginAcesso == null && !isApp) {
            throw new LoginNaoEncontratoException("Login não está relacionado com nenhuma empresa.");
        }

        String tenat = null;
        if (loginAcesso != null) {
            tenat = loginAcesso.getIdEmpresa().getTenatID();
            adHocTenant.setTenantID(tenat);
        }
        Usuario usuario = loadUserByLogin(login, tenat);

        if ((usuario == null)
                || (senhaJaCriptografada && !senha.equals(usuario.getSenha()))
                || (!senhaJaCriptografada && !StringUtil.criptografarMD5(senha).equals(usuario.getSenha()))) {
            throw new UsuarioOuSenhaInvalidaException("Credenciais inválidas!");
        }

        if (usuario.isAccountNonLocked()) {
            throw new ContaBloqueadaException("A conta do usuário " + usuario.getNome() + " está bloqueada!");
        }

        if (!usuario.isEnabled() || (usuario.getIdFuncionario() != null && "N".equals(usuario.getIdFuncionario().getAtivo()))) {
            throw new ContaBloqueadaException("A conta do usuário " + usuario.getNome() + " está desativada!");
        }

        // verifica se a prefeitura esta habilitada para acesso de outros perfils
        if (loginAcesso != null && !usuario.getIdPerfil().getEhSuporte() && !isEmpresaAtiva(tenat)) {
            throw new AcessoBloqueadoException("O SGFinancas está em manutenção. Acesse novamente em alguns instantes.");
        }

        if (addLogin && loginAcesso != null && (tenatID != null || getTenats(login).size() == 1)) {
            adicionaEmpresaUsuarioAcesso(loginAcesso.getIdUsuario(), loginAcesso.getIdEmpresa(), ipUsuario(), isApp);
        }

        return usuario;
    }

    public Authentication createAuthFor(Usuario usuario) {
        try {
            return createAuthFor(autenticarUsuario(usuario.getLogin(), usuario.getSenha(), true, null, false, false), "");
        } catch (LoginNaoEncontratoException | UsuarioOuSenhaInvalidaException | AcessoBloqueadoException | ContaBloqueadaException ex) {
            return null;
        }
    }

    public Authentication createAuthFor(Usuario usuario, String password) {
        Locale.setDefault(new Locale("pt", "BR"));
        TimeZone.setDefault(TimeZone.getTimeZone("GMT-3"));
        return new UsernamePasswordAuthenticationToken(usuario, password, getPermissoesUsuario(empresaService.getEmpresa(), usuario, null));
    }

    public String ipUsuario() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String ip = "localhost";
        if (auth != null && auth.getDetails() != null) {
            ip = ((WebAuthenticationDetails) auth.getDetails()).getRemoteAddress();
        }
        return ip;
    }

    public void adicionaEmpresaUsuarioAcesso(Usuario usuario, Empresa empresa, String ip, boolean isApp) {
        EmpresaUsuarioAcesso empresaUsuarioAcesso = new EmpresaUsuarioAcesso();
        empresaUsuarioAcesso.setIdEmpresa(empresa);
        empresaUsuarioAcesso.setIdUsuario(usuario);
        empresaUsuarioAcesso.setDataAcesso(DataUtil.getHoje());
        empresaUsuarioAcesso.setIp(ip);
        empresaUsuarioAcesso.setOrigem(isApp ? "APP" : "SITE");

        repositorio.addEmpresaUsuarioAcesso(empresaUsuarioAcesso);
    }

    public LoginAcesso getLoginAcesso(String login) {
        loginAcesso = repositorio.getLoginAcesso(login);
        return loginAcesso;
    }

    public LoginAcesso getLoginAcesso(String login, Empresa empresa) {
        loginAcesso = repositorio.getLoginAcesso(login, empresa);
        return loginAcesso;
    }

    public LoginAcesso getLoginAcesso(Usuario usuario, Empresa empresa) {
        loginAcesso = repositorio.getLoginAcesso(usuario, empresa);
        return loginAcesso;
    }

    public LoginAcesso getLoginAcesso(String login, String tenat) {
        loginAcesso = repositorio.getLoginAcesso(login, tenat);
        return loginAcesso;
    }

    public List<LoginAcesso> getAcessos(String tenat) {
        return repositorio.getAcessos(tenat);
    }

    public List<LoginAcesso> getAcessos(Usuario usuario) {
        return repositorio.getAcessos(usuario);
    }

    public LoginAcesso getLoginAcesso() {
        return loginAcesso;
    }

    public Usuario getUserByLogin(String login) {
        return repositorio.getUserByLogin(login);
    }

    public Usuario loadUserByLogin(String login, String tenat) {
        Usuario usuario = usuarioService.getUserByLogin(login);
        if (usuario != null) {
            usuario.setTenat(tenat);
            usuario.setQtdEmpresas(getQtdEmpresaPorLogin(login));

            LoginAcesso acesso = getLoginAcesso(login, tenat);
            usuario.setLoginAcesso(acesso);
        }

        return usuario;
    }

    public void autenticaUsuario(Usuario usuario, String senha) {
        adHocTenant.setTenantID(usuario.getTenat());
        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, senha, getTodasPermissoes(usuario, usuario.getTenat()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void atualizaUsuario(Usuario usuario) {
        if (usuario.getLoginAcesso() != null) {
            alterar(usuario.getLoginAcesso());
        }
        usuarioService.alterar(usuario);
    }

    public List<Permissao> getPermissoesUsuario(Usuario usuario) {
        return usuarioService.getPermissoesUsuario(usuario);
    }

    public List<Permissao> getPermissaoGrupo(Usuario usuario, String tenat) {
        return usuarioService.getPermissaoGrupo(usuario, tenat);
    }

    public List<Permissao> getPermissoesUsuario(Empresa empresa, Usuario usuario, String descricao) {
        if (Boolean.TRUE.equals(usuario.getIdPerfil().getEhSuporte())) {
            List<Permissao> permissoes = permissaoService.getPermissoes(descricao);
            permissoes.add(new Permissao(null, "SUPORTE"));
            if ("dev".equalsIgnoreCase(usuario.getMenuMode())) {
                permissoes.add(new Permissao(null, "DEV"));
            }
            return permissoes;
        }
        if ("S".equals(empresa.getAcessoPrivado()) && Boolean.TRUE.equals(usuario.getIdPerfil().getEhUsuarioMaster())) {
            return permissaoService.getPermissoes(descricao);
        }
        if ("N".equals(empresa.getAcessoPrivado())) {
            if (empresa.getTipoConta().equals("E")) {
                return new ArrayList<>();
            }
            if (Boolean.TRUE.equals(usuario.getIdPerfil().getEhUsuarioMaster())) {
                PagamentoMensalidade pm = pmService.getUltimoPagamentoPor(empresa);
                if (pm == null) {
                    return new ArrayList<>();
                }
                return pmmpService.getPermissoesParaUsuarioMasterPor(pm, descricao).stream()
                        .map(PagamentoMensalidadeModuloPermissao::getIdPermissao)
                        .collect(Collectors.toList());
            }
        }
        List<Permissao> permissoes = new LinkedList<>();
        permissoes.addAll(LoginAcessoService.this.getPermissoesUsuario(usuario));
        getPermissaoGrupo(usuario, usuario.getTenat()).stream()
                .filter(permissao -> !permissoes.contains(permissao))
                .forEachOrdered(permissoes::add);

        return permissoes;
    }

    public List<EmpresaAcessoDTO> getEmpresasUsuarioLogado() {
        List<EmpresaAcessoDTO> lista = new ArrayList<>();
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Empresa> empresas;

        // todas empresas
        if (Boolean.TRUE.equals(usuario.getIdPerfil().getEhSuporte())) {
            empresas = repositorio.getTenats(); // todos os acessos do usuario
        } else {
            empresas = repositorio.getTenats(usuario.getLogin());
        }

        empresas.stream()
                .map(empresa -> new EmpresaAcessoDTO(empresa.getDescricao(), empresa.getTenatID(), empresa.getTipo()))
                .forEachOrdered(lista::add);

        lista.sort((c1, c2) -> c1.getDescricao().compareToIgnoreCase(c2.getDescricao()));

        return lista;
    }

    public List<EmpresaAcessoDTO> getEmpresasUsuario(Usuario usuario) {
        Perfil perfil = usuario.getIdPerfil();
        List<Empresa> empresas;

        // todas empresas
        if (Boolean.TRUE.equals(perfil.getEhSuporte())) {
            empresas = repositorio.getTenats(); // todos os acessos do usuario
        } else {
            empresas = repositorio.getTenats(usuario.getLogin());
        }

        return empresas.stream()
                .map(empresa -> new EmpresaAcessoDTO(empresa.getDescricao(), empresa.getTenatID()))
                .collect(Collectors.toList());
    }

    public List<Empresa> getTenats(String login) {
        return repositorio.getTenats(login);
    }

    public List<EmpresaAcessoDTO> getTodasEmpresas() {
        return repositorio.getTodosTenats().stream()
                .map(acesso -> new EmpresaAcessoDTO(acesso.getDescricao(), acesso.getTenatID()))
                .collect(Collectors.toList());
    }

    public EmpresaAcessoDTO getEmpresaAcessoDTOByTenat(String tenat) {
        return new EmpresaAcessoDTO(empresaService.getEmpresaPorTenat(tenat).getDescricao(), tenat);
    }

    public Integer getQtdEmpresaPorLogin(String login) {
        return repositorio.getQtdTenats(login);
    }

    public void alteraTenatEmpresaCredenciada(String tenatID) {
        adHocTenant.setTenantID(tenatID);
    }

    public void alteraTenat(String tenant) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

        adHocTenant.setTenantID(tenant);

        Usuario usuarioNovo = loadUserByLogin(usuario.getLogin(), tenant);

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuarioNovo, "", getTodasPermissoes(usuarioNovo, tenant));
        authentication.setDetails(details);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Usuario alteraTenatComUsuarioSuporte(String tenat) {
        Usuario usuarioNovo = loadUserByLogin("suporte", tenat);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuarioNovo;
    }

    public Usuario alteraTenatComUsuarioSuporteSemPermissao(String tenat) {
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setTenat(tenat);
        usuarioNovo.setLogin("suporte");

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuarioNovo;
    }

    public Usuario alteraTenatComUsuarioSuporte(EmpresaAcessoDTO prefeituraAcessoDTO) {
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setTenat(prefeituraAcessoDTO.getTenat());
        usuarioNovo.setLogin("suporte");

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuarioNovo;
    }

    public Usuario alteraTenatComUsuarioServico(EmpresaAcessoDTO prefeituraAcessoDTO) {
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setTenat(prefeituraAcessoDTO.getTenat());
        usuarioNovo.setLogin("servico");

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuarioNovo;
    }

    public List<Permissao> getTodasPermissoes(Usuario usuario, String tenat) {
        return getPermissoesUsuario(empresaService.getEmpresPorTenatID(tenat), usuario, "");
    }

    public List<Empresa> getTodosTenats() {
        return repositorio.getTodosTenats();
    }

    public List<Object> getDadosAuditoriaByID(Usuario usuario) {
        return usuarioService.getDadosAuditoriaByID(usuario);
    }

    public List<ControleAcessoIp> getListaIPs(String tenat) {
        ControleAcessoIpRepositorio controleAcessoIpRepositorio = getControleAcessoIpRepositorio(tenat);
        return controleAcessoIpRepositorio.getControleAcessoIps();
    }

    public boolean isEmpresaAtiva(String tenat) {
        EmpresaRepositorio rep = getEmpresaRepositorio(tenat);
        return "S".equals(rep.getEmpresa().getAtivo());
    }

    public Date getPrimeiraDataAtualizacaoSenhaAuditoria(Usuario usuario) {
        AuditReader reader = AuditReaderFactory.get(em);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(Usuario.class, false, true);
        query.add(AuditEntity.revisionNumber().maximize().add(AuditEntity.property("id").eq(usuario.getId())).add(AuditEntity.property("senha").hasChanged()));
        try {
            Object[] resultado = (Object[]) query.getSingleResult();
            CustomRevisionEntity revisionEntity = (CustomRevisionEntity) resultado[1];
            return revisionEntity.getDataAtualizacao();
        } catch (NoResultException ex) {
            Logger.getLogger(LoginAcessoService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public List<String> getListUltimasSenhas(Usuario usuario, int numAlteracoes) {
        AuditReader reader = AuditReaderFactory.get(em);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(Usuario.class, false, true);
        query.add(AuditEntity.property("id").eq(usuario.getId()));
        query.add(AuditEntity.property("senha").hasChanged());
        query.addOrder(AuditEntity.revisionNumber().desc()).setMaxResults(numAlteracoes);

        return ((List<Object[]>) query.getResultList()).stream()
                .map(object -> ((Usuario) object[0]).getSenha())
                .collect(Collectors.toList());
    }

    @Override
    public int quantidadeRegistrosFiltrados(LoginAcessoFiltro filtro) {
        return repositorio.getQuantidadeRegistrosFiltrados(getListaEmpresa(filtro));
    }

    public List<Empresa> getListaFiltradaEmpresa(LoginAcessoFiltro filtro) {
        return repositorio.getListaFiltrada(getListaEmpresa(filtro), filtro);
    }

    public Criteria getListaEmpresa(LoginAcessoFiltro filtro) {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Empresa.class);

        criteria.createAlias("endereco.idCidade", "idCidade", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idCnae", "idCnae", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idDocumento", "idDocumento", JoinType.LEFT_OUTER_JOIN);
        criteria.createAlias("idContabilidade", "idContabilidade", JoinType.LEFT_OUTER_JOIN);

        DetachedCriteria dt = DetachedCriteria.forClass(LoginAcesso.class);
        dt.add(Restrictions.eq("idUsuario", filtro.getUsuario())).setProjection(Projections.property("idEmpresa"));
        Criterion cAcessoUsuario = Subqueries.propertyIn("id", dt);

        // Visualiza todas as empresas menos as privadas
        if (Boolean.TRUE.equals(filtro.getUsuario().getIdPerfil().getEhSuporte())) {
            criteria.add(Restrictions.or(cAcessoUsuario, Restrictions.eq("acessoPrivado", "N")));
        } else {
            criteria.add(cAcessoUsuario);
        }

        if (null != filtro.getEmpresa()) {
            criteria.add(Restrictions.eq("id", filtro.getEmpresa().getId()));
        }

        addEqRestrictionTo(criteria, "ativo", filtro.getAtivo());
        addEqRestrictionTo(criteria, "tipo", filtro.getTipo());
        addIlikeRestrictionTo(criteria, "descricao", filtro.getDescricao(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "cnpj", filtro.getCnpj(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "inscricaoEstadual", filtro.getInscricaoEstadual(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "responsavel", filtro.getResponsavel(), MatchMode.ANYWHERE);
        addRestrictionTo(criteria, "tipoEmpresa", filtro.getTipoEmpresa());

        return criteria;
    }

    public LoginAcesso adicionarLoginAcesso(Empresa empresa, Usuario usuario) {
        return adicionar(LoginAcesso.builder()
                .idEmpresa(empresa)
                .idUsuario(usuario)
                .dataAcesso(new Date())
                .build());
    }

    public Usuario getCredorByEmpresa(Empresa credorSelecionado) {
        return repositorio.getCredorByEmpresa(credorSelecionado);
    }

    public Empresa salvarEmpresa(Empresa empresa) {
        return repositorio.salvarEmpresa(empresa);
    }

}
