package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Contabilidade;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioAcessoRapido;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioLeituraTermo;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CredenciamentoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.VeiculoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UsuarioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.UsuarioRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumCadastroCredenciamento;
import br.com.villefortconsulting.sgfinancas.util.EnumRegimeTributario;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContaEmpresa;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoEmpresa;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoGrupo;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPerfil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.primefaces.model.UploadedFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UsuarioService extends BasicService<Usuario, UsuarioRepositorio, UsuarioFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @Inject
    private VeiculoMapper veiculoMapper;

    @Inject
    private LoginAcessoService loginAcessoService;

    @EJB
    private AdministracaoService administracaoService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private UnidadeMedidaService unidadeMedidaService;

    @EJB
    private PerfilService perfilService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private GrupoService grupoService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private EmailService emailService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private FormaPagamentoService formaPagamentoService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ModuloPacoteService moduloPacoteService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ClienteVeiculoService clienteVeiculoService;

    @EJB
    private TipoPagamentoService tipoPagamentoService;

    @EJB
    private EmailUtil emailUtil;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
        repositorio = new UsuarioRepositorio(em);
    }

    public List<UsuarioPermissao> addTenantUsuarioPermissao(List<UsuarioPermissao> lista) {
        lista.forEach(usuarioPermissao -> usuarioPermissao.setTenatID(adHocTenant.getTenantID()));
        return lista;
    }

    public Usuario setUsuario(Usuario usr, String senha) throws SenhaIncorretaException {
        validarSenha(senha);
        return alterar(usr);
    }

    public Usuario addUsuario(Usuario usr) {
        return adicionar(usr);
    }

    public void removeUsuario(Usuario usr) {
        LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(usr.getLogin());
        loginAcessoService.remover(loginAcesso);

        remover(usr);
    }

    public void alterarSenhaEemailContaExpirada(Usuario usuario, String password) throws SenhaIncorretaException {
        validarSenha(password);

        usuario.setContaExpirada("N");
        usuario.setSenhaExpirada(false);
        usuario.setSenha(StringUtil.criptografarMD5(password));

        alterar(usuario);
    }

    public void salvarValidarSenhaAndEmail(Usuario usuario, String password) throws SenhaIncorretaException {
        validarSenha(password);

        usuario.setSenhaExpirada(false);
        usuario.setSenha(StringUtil.criptografarMD5(password));

        alterar(usuario);
    }

    public void executaAtualizacaoDadosEmEmpresasDoUsuario(Usuario usuario, StringBuilder consulta) {
        List<EmpresaAcessoDTO> empresas = loginAcessoService.getEmpresasUsuario(usuario);
        administracaoService.executaAtualizacaoDados(empresas, consulta.toString());
    }

    public void recuperarSenha(Usuario usuario, String password) throws SenhaIncorretaException {
        usuario.setContaExpirada("S");
        usuario.setSenhaExpirada(true);

        setUsuario(usuario, password);
    }

    public boolean validarSenha(String senha) throws SenhaIncorretaException {
        if (senha == null || !StringUtil.possuiNumero(senha) || !StringUtil.possuiLetra(senha) || senha.length() < 8) {
            throw new SenhaIncorretaException("Você deve especificar uma senha contendo letras e números com tamanho mínimo de 8 caracteres.");
        }
        return true;
    }

    public Usuario salvarUsuario(CredenciamentoDTO credenciamentoDTO, Empresa empresa) throws LoginDuplicadoException, SenhaIncorretaException, EmailException, UsuarioException {
        Perfil perfil = perfilService.getPerfil(EnumTipoUsuario.MASTER_USUARIO.getTipo());

        empresa.setAtivo("S");
        empresa.setPrimeiroLogin("S");
        empresa.setIndicadorInscricaoEstadual("NC");
        empresa.setTipo(EnumTipoEmpresa.PRIVADO.getTipo());
        empresa.setRegimeTributario(EnumRegimeTributario.SIMPLES_NACIONAL_MICRO_EMPRE.getForma());
        empresa.setTipoConta(EnumTipoContaEmpresa.GRATUITA.getTipo());

        empresa = empresaService.adicionar(empresa);

        adHocTenant.setTenantID(empresa.getTenatID());

        planoContaService.salvarPlanoContaModeloPadrao(empresa);

        unidadeMedidaService.populaUnidadeMedida(empresa.getTenatID());

        ParametroSistema parametro = parametroSistemaService.populaParametroSistema(empresa.getTenatID());

        contaBancariaService.populaContaBancariaPadrao(empresa);

        parametroSistemaService.populaParametroContaBancariaPadrao(parametro);

        formaPagamentoService.popularFormaPagamento(empresa.getTenatID());

        produtoService.populaCategoriaInicial(empresa);

        tipoPagamentoService.populaTipoPagamento(empresa);

        List<GrupoEmpresa> listGrupoEmpresa = adicionarGrupoEmpresa(empresa);

        Usuario usuario = new Usuario();
        usuario.setPodeMudarPrecoUnitarioVenda("N");
        usuario.setIdPerfil(perfil);
        usuario.setCadCredenciamento(EnumCadastroCredenciamento.SIM.getTipo());
        usuario.setTenat(empresa.getTenatID());
        usuario.setUsuarioGrupoEmpresaList(new LinkedList<>());
        listGrupoEmpresa.stream().filter(p -> p.getIdGrupo().getTipo().equals(EnumTipoGrupo.USUARIO_MASTER.getTipo())).forEach(usuario::addGrupoEmpresa);

        usuario.setLogin(credenciamentoDTO.getLogin());
        usuario.setNome(credenciamentoDTO.getNome());
        usuario.setEmail(credenciamentoDTO.getLogin());

        Usuario usuarioToReturn = adicionarUsuario(usuario, empresa, credenciamentoDTO.getSenha());

        ModuloPacote mp = null;
        if (credenciamentoDTO.isUsarPromocao()) {
            mp = moduloPacoteService.buscarPor(empresa.getIdCategoriaEmpresa());
        }
        pagamentoMensalidadeService.populaPagamentoMensalidade(empresa, mp, usuarioToReturn);

        try {
            enviarSenhaPorEmailCredenciamento(usuario, credenciamentoDTO.getSenha());
        } catch (Exception ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return usuarioToReturn;
    }

    public void salvarUsuario(CredenciamentoDTO credenciamentoDTO) throws LoginDuplicadoException, SenhaIncorretaException, EmailException, UsuarioException {
        Empresa empresa = new Empresa();
        empresa.setDescricao(credenciamentoDTO.getNomeEmpresa());
        empresa.setNomeFantasia(credenciamentoDTO.getNomeEmpresa());
        empresa.setCnpj(credenciamentoDTO.getCnpjEmpresa());
        empresa.setDataCredenciamento(DataUtil.getHoje());
        empresa.setFone(credenciamentoDTO.getTelefone());
        empresa.setResponsavel(credenciamentoDTO.getNome());
        empresa.setEmail(credenciamentoDTO.getLogin());
        empresa.getEndereco().setIdCidade(credenciamentoDTO.getCidade());

        salvarUsuario(credenciamentoDTO, empresa);
    }

    public List<GrupoEmpresa> adicionarGrupoEmpresa(Empresa empresa) {
        //Lista todos os grupos padrões.
        List<Grupo> gruposPadroes = grupoService.getGruposPadroes();

        List<GrupoEmpresa> listGrupoEmpresa = new LinkedList<>();

        //Adiciona os grupos padrões para aquela empresa
        gruposPadroes.stream()
                .map(grupo -> {
                    GrupoEmpresa grupoEmpresa = new GrupoEmpresa();
                    grupoEmpresa.setIdEmpresa(empresa);
                    grupoEmpresa.setIdGrupo(grupo);
                    return grupoEmpresa;
                })
                .map(grupoEmpresa -> {
                    grupoService.adicionarGrupoEmpresa(grupoEmpresa);
                    return grupoEmpresa;
                })
                .forEachOrdered(listGrupoEmpresa::add);

        return listGrupoEmpresa;
    }

    public Usuario salvarUsuario(Usuario usuario, String senha, Empresa empresa) throws SenhaIncorretaException, LoginDuplicadoException {
        if (usuario.getId() == null) {
            usuario.setCadCredenciamento(EnumCadastroCredenciamento.NAO.getTipo());
            if(usuario.getIdPerfil().getEhUsuarioMaster()|| usuario.getIdPerfil().getEhSuporte()){
                usuario.setPodeMudarPrecoUnitarioVenda("S");
            }
            return adicionarUsuario(usuario, empresa, senha);
        }
        Usuario user = buscar(usuario.getId());
        if (("S".equals(user.getContaExpirada()) && "N".equals(usuario.getContaExpirada())) || ("S".equals(user.getContaBloqueada()) && "N".equals(usuario.getContaBloqueada()))) {
            validarLogin(usuario.getLogin());
        }
        return alterar(usuario);
    }

    public int validarSuporte(Usuario usuario) {
        if (!Boolean.TRUE.equals(usuario.getIdPerfil().getEhSuporte())) {
            return 0;
        }
        List<LoginAcesso> acessos = loginAcessoService.getAcessos(usuario);
        List<Empresa> empresas = loginAcessoService.getTodasEmpresas().stream()
                .map(EmpresaAcessoDTO::getTenat)
                .map(empresaService::getEmpresPorTenatID)
                .filter(e -> "N".equals(e.getAcessoPrivado()) && acessos.stream().noneMatch(la -> la.getIdEmpresa().equals(e)))
                .collect(Collectors.toList());
        empresas.forEach(e -> loginAcessoService.adicionarLoginAcesso(e, usuario));
        return empresas.size();
    }

    public Usuario adicionarUsuario(Usuario usuario, Empresa empresa, String senha) throws LoginDuplicadoException, SenhaIncorretaException {

        usuario.setSenha(senha);
        validaLoginSenha(usuario);
        // Adicionando usuario
        usuario.setSenha(StringUtil.criptografarMD5(senha));
        usuario = adicionar(usuario);

        // Adicionando acesso
        loginAcessoService.adicionarLoginAcesso(usuario, empresa);

        return usuario;
    }

    public Usuario adicionarUsuario(Usuario usuario) throws LoginDuplicadoException, SenhaIncorretaException {
        validaLoginSenha(usuario);
        usuario.setSenha(StringUtil.criptografarMD5(usuario.getSenha()));
        return salvar(usuario);
    }

    private void validaLoginSenha(Usuario usuario) throws LoginDuplicadoException, SenhaIncorretaException {
        // validando login e senha
        validarLogin(usuario.getLogin());
        validarSenha(usuario.getSenha());
    }

    public void validarLogin(String login) throws LoginDuplicadoException {
        if (loginAcessoService.existeUserByLogin(login)) {
            throw new LoginDuplicadoException("O e-mail informado já está em uso por outro funcionário/usuário.");
        }
        if (repositorio.existeUserByLogin(login)) {
            throw new LoginDuplicadoException("O e-mail informado já está em uso por outro funcionário/usuário.");
        }
    }

    public Grupo getGrupoByDescricao(String descricao) {
        return repositorio.getGrupoByDescricao(descricao);
    }

    public Usuario getUsuarioComPermissoesPorLogin(Usuario usuario) {
        return repositorio.getUsuarioComPermissoesPorLogin(usuario);
    }

    public List<Usuario> getUsuarios() {
        return repositorio.getUsuarios();
    }

    public List<Usuario> getUsuariosEmpresaLogada() {
        return repositorio.getUsuarios(adHocTenant.getTenantID());
    }

    public void bloquearUsuarioPorFuncionario(Funcionario funcionario) {
        Usuario usuario = getUsuarioById(funcionario);
        if (usuario != null) {
            usuario.setContaBloqueada("S");
            salvar(usuario);
        }
    }

    public void bloquearExpirarUsuario(Usuario usuario) {
        if (usuario != null) {
            usuario.setContaBloqueada("S");
            usuario.setContaExpirada("S");
            salvar(usuario);
        }
    }

    public void desbloquearUsuarioPorLogin(Funcionario funcionario) throws LoginDuplicadoException {
        validarLogin(funcionario.getEmail());
        Usuario usuario = getUsuarioById(funcionario);
        if (usuario != null) {
            usuario.setContaBloqueada("N");
            usuario.setIdFuncionario(funcionario);
            salvar(usuario);
        }
    }

    public Usuario getUsuarioByLogin(String login) {
        return repositorio.getUserByLogin(login);
    }

    public Usuario getUsuarioById(Funcionario funcionario) {
        return repositorio.getUserById(funcionario);
    }

    public List<Usuario> getUserByName(String name) {
        return repositorio.getUserByName(name);
    }

    public List<Usuario> getUsuarioPorEmpresaLogada(String name, Empresa empresaLogada) {
        return repositorio.getUsuarioPorEmpresaLogada(name, empresaLogada);
    }

    public List<Usuario> listarUsuarioVendedorPorEmpresaLogada(Empresa empresaLogada) {
        return repositorio.listarUsuarioVendedorPorEmpresaLogada(empresaLogada);
    }

    public List<Usuario> listarUsuarioPorEmpresaLogada(Empresa empresaLogada) {
        return repositorio.listarUsuarioPorEmpresaLogada(empresaLogada);
    }

    public List<Usuario> getUsuariosServidores() {
        return repositorio.getUsuariosServidores();
    }

    public List<Usuario> getUsuariosServidoresSimplificado() {
        return repositorio.getUsuariosServidoresSimplificado();
    }

    public Usuario loadUserByUsername(String login, String tenat) {
        Usuario usuario = repositorio.getUserByLogin(login);

        usuario.setTenat(tenat);
        usuario.setQtdEmpresas(loginAcessoService.getQtdEmpresaPorLogin(usuario.getLogin()));

        return usuario;
    }

    public UsuarioPermissao adicionarUsuarioPermissao(UsuarioPermissao usuarioPermissao) {
        return repositorio.addUsuarioPermissao(usuarioPermissao);
    }

    public UsuarioPermissao alterarUsuarioPermissao(UsuarioPermissao usuarioPermissao) {
        return repositorio.setUsuarioPermissao(usuarioPermissao);
    }

    public List<UsuarioPermissao> getPermissoes(Usuario usuario) {
        return repositorio.getPermissoes(usuario);
    }

    public List<Permissao> getPermissoesUsuario(Usuario usuario) {
        return repositorio.getPermissoesUsuario(usuario);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresaList(Empresa empresa) {
        return repositorio.getUsuarioGrupoEmpresaList(empresa);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresaList(Usuario usuario, Empresa empresa) {
        return repositorio.getUsuarioGrupoEmpresaList(usuario, empresa);
    }

    public List<Permissao> getPermissaoGrupo(Usuario usuario, String tenant) {
        return repositorio.getPermissaoGrupo(usuario, tenant);
    }

    public UsuarioGrupoEmpresa getUsuarioGrupoEmpresa(Usuario usuario, GrupoEmpresa grupoEmpresa) {
        return repositorio.getUsuarioGrupoEmpresa(usuario, grupoEmpresa);
    }

    public List<UsuarioGrupoEmpresa> getUsuarioGrupoEmpresa(GrupoEmpresa grupoEmpresa) {
        return repositorio.getUsuarioGrupoEmpresa(grupoEmpresa);
    }

    public List<Usuario> getServidroUserByName(String name) {
        return repositorio.getServidroUserByName(name);
    }

    public boolean existeUserByLogin(String login) {
        return repositorio.existeUserByLogin(login);
    }

    public void enviarSenha(Usuario usuario) throws EmailException {

        String senha = StringUtil.gerarStringAleatoria(10);
        recuperarSenhaAtualizarEmail(usuario, senha);

        EmailDTO emailDTO = EmailUtil.getEmailRecuperacaoSenha(usuario, senha);
        emailService.enviarEmailMS(emailDTO, EmailService.ENVIO_OBRIGATORIO_SIM);
    }

    public void enviarSenhaPorEmail(Usuario usuario) throws EmailException, UsuarioException {
        LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(usuario.getLogin());
        if (loginAcesso == null) {
            throw new UsuarioException("Usuário não possui acesso criado", null);
        }
        usuario.setLoginAcesso(loginAcesso);

        String senha = StringUtil.gerarStringAleatoria(10);
        recuperarSenhaAtualizarEmail(usuario, senha);

        EmailDTO emailDTO = EmailUtil.getEmailRecuperacaoSenha(usuario, senha);
        emailService.enviarEmailMS(emailDTO, EmailService.ENVIO_OBRIGATORIO_SIM);
    }

    public void enviarSenhaPorEmailFuncionario(Usuario usuario, String senha) throws EmailException, UsuarioException {
        LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(usuario.getLogin());
        if (loginAcesso == null) {
            throw new UsuarioException("Usuário não possui acesso criado", null);
        }
        usuario.setLoginAcesso(loginAcesso);

        EmailDTO emailDTO = EmailUtil.getEmailSenhaFuncionario(usuario, senha);
        emailService.enviarEmailMS(emailDTO, EmailService.ENVIO_OBRIGATORIO_SIM);
    }

    public void enviarSenhaPorEmailCredenciamento(Usuario usuario, String senha) throws EmailException, UsuarioException {
        LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(usuario.getLogin());
        if (loginAcesso == null) {
            throw new UsuarioException("Usuário não possui acesso criado", null);
        }
        usuario.setLoginAcesso(loginAcesso);

        EmailDTO emailDTO = emailUtil.getEmailSenhaAcesso(usuario, loginAcesso.getIdEmpresa());
        if (!StringUtils.isEmpty(emailDTO.getMensagem())) {
            emailService.enviarEmailMS(emailDTO, EmailService.ENVIO_OBRIGATORIO_SIM);
        } else {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Layout do email de cadastro não configurado.");
        }
    }

    public void recuperarSenhaAtualizarEmail(Usuario usuario, String password) {
        usuario.setSenha(StringUtil.criptografarMD5(password));
        usuario.setEmail(usuario.getEmail());
        usuario.setContaExpirada("S");

        alterar(usuario);
    }

    public List<Usuario> listarUsuariosMasterComEmail(Empresa empresa) {
        return repositorio.listarUsuariosMasterComEmail(empresa);
    }

    public List<Usuario> getUsuariosPorNome(String nome) {
        return repositorio.getUsuariosPorNome(nome);
    }

    public List<Usuario> getUsuariosNaoSuspensosPorNome(String nome) {
        return repositorio.getUsuariosNaoSuspensosPorNome(nome);
    }

    public List<Usuario> getUsuarioPesquisaPorNomeContrato(String nome) {
        return repositorio.getUsuarioPesquisaPorNomeContrato(nome);
    }

    // SUSPENSAO DE USUARIOS
    public boolean verificarUsuarioEstaSuspenso(Usuario usuario) {
        return repositorio.verificarUsuarioEstaSuspenso(usuario);
    }

    public Integer getDiasSenhaExpirada(Usuario usuario) {
        Date ultimaAtualizacaoDeSenha = loginAcessoService.getPrimeiraDataAtualizacaoSenhaAuditoria(usuario);
        if (ultimaAtualizacaoDeSenha != null) {
            // Calcula a diferenca de dias entre hoje e a ultima atualizacao de senha
            DateTime dataAtual = new DateTime(new Date().getTime());
            DateTime dataUltimaAtualizacao = new DateTime(ultimaAtualizacaoDeSenha.getTime());
            return Days.daysBetween(dataUltimaAtualizacao, dataAtual).getDays();
        } else {
            return Integer.MAX_VALUE; // true
        }
    }

    public List<Usuario> getListUsuarioPorNameParaAddGrupo(Grupo grupo, String nome) {
        Criteria criteria = super.getModel(new UsuarioFiltro());
        addIlikeRestrictionTo(criteria, "nome", nome);

        if (EnumTipoUsuario.ADMINISTRADOR.getTipo().equals(grupo.getTipo())
                || EnumTipoUsuario.MASTER_USUARIO.getTipo().equals(grupo.getTipo())
                || EnumTipoUsuario.VENDEDOR.getTipo().equals(grupo.getTipo())
                || EnumTipoUsuario.MASTER_VENDEDOR.getTipo().equals(grupo.getTipo())) {
            criteria.createAlias("idPerfil", "perf");
            criteria.add(Restrictions.eq("perf.tipo", grupo.getTipo()));
        }

        return criteria.setProjection(Projections.projectionList()
                .add(Projections.property("id").as("id"))
                .add(Projections.property("nome").as("nome")))
                .setResultTransformer(new AliasToBeanResultTransformer(Usuario.class))
                .setCacheable(true)
                .list();
    }

    public void alterarSenha(Usuario usuario, String senhaAtual, String senhaNova, String confimacaoSenha) throws SenhaIncorretaException {
        if (!StringUtil.criptografarMD5(senhaAtual).equals(usuario.getSenha())) {
            throw new SenhaIncorretaException("Senha do usuário está incorreta.");
        } else if (!senhaNova.equals(confimacaoSenha)) {
            throw new SenhaIncorretaException("Senhas não conferem.");
        } else {
            salvarValidarSenhaAndEmail(usuario, senhaNova);
        }
    }

    public Usuario salvarFotoPadrao(Usuario usuario, Usuario usuarioLogado, UploadedFile part) {
        byte[] fotoConvertida = part.getContents();
        usuario.setFoto(fotoConvertida);
        usuarioLogado.setFoto(fotoConvertida);
        alterar(usuario);
        return buscar(usuario.getId());
    }

    public Usuario removerFotoPadrao(Usuario usuario, Usuario usuarioLogado) {
        usuario.setFoto(null);
        usuarioLogado.setFoto(null);
        alterar(usuario);
        return buscar(usuario.getId());
    }

    public List<Usuario> listarUsuarioPorPerfil(Perfil perfil, String tenant) {
        return repositorio.listarUsuarioPorPerfil(perfil.getTipo(), tenant);
    }

    public String getEmailUsuario(Usuario usuario) {
        return repositorio.getEmailUsuario(usuario);
    }

    public Usuario getUserByLogin(String login) {
        return repositorio.getUserByLogin(login);
    }

    public List<Usuario> listarUsuarioSemAcessoEmpresaPorContabilidade(Empresa empresa, Contabilidade contabilidade) {
        return repositorio.listarUsuarioSemAcessoEmpresaPorContabilidade(empresa, contabilidade);
    }

    public List<Usuario> listarUsuarioComAcessoEmpresaComContabilidade(Empresa empresa) {
        return repositorio.listarUsuarioComAcessoEmpresaComContabilidade(empresa);
    }

    public UsuarioGrupoEmpresa addUsuarioGrupoEmpresa(UsuarioGrupoEmpresa uge) {
        return repositorio.addUsuarioGrupoEmpresa(uge);
    }

    public void removerUsuarioGrupoEmpresa(UsuarioGrupoEmpresa uge) {
        repositorio.removeUsuarioGrupoEmpresa(uge);
    }

    public UsuarioDTO converterDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId(usuario.getId());
        if (usuario.getIdFuncionario() != null) {
            usuarioDTO.setCpf(usuario.getIdFuncionario().getCpf());
        }
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setPerfilSigla(usuario.getIdPerfil().getTipo());
        usuarioDTO.setPerfilNome(usuario.getIdPerfil().getDescricao());
        usuarioDTO.setContaBloqueada(usuario.getContaBloqueada());
        usuarioDTO.setLogin(usuario.getLogin());
        usuarioDTO.setTelefone(usuario.getTelefone());
        usuarioDTO.setFoto64(usuario.getFoto64());
        if(usuario.getPodeMudarPrecoUnitarioVenda().equals("S")){
            usuarioDTO.setPodeMudarPrecoUnitario(true);
        }else{
            usuarioDTO.setPodeMudarPrecoUnitario(false);
        }

        List<Cliente> cliente = clienteService.buscarPor(usuario);
        if (!cliente.isEmpty()) {
            usuarioDTO.setVeiculoList(clienteVeiculoService.listaVeiculosPor(cliente.get(0)).stream()
                    .map(veiculoMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return usuarioDTO;
    }

    public boolean hasAny(String name, Empresa empresaLogada) {
        return repositorio.hasAny(name, empresaLogada);
    }

    public void concederAcessoUsuariosSuporte(Empresa empresa) {
        GrupoEmpresa grupoEmpresa = grupoService.buscarGrupoEmpresaPorTipoGrupo(EnumTipoGrupo.SUPORTE.getTipo(), empresa);

        if (grupoEmpresa == null) {
            Grupo grupo = grupoService.getGrupo(EnumTipoGrupo.SUPORTE.getTipo());
            grupoEmpresa = new GrupoEmpresa(grupo, empresa);
            grupoEmpresa = grupoService.salvarGrupoEmpresa(grupoEmpresa);
        }

        List<Usuario> usuarios = listarUsuarioSuporte();

        for (Usuario usuario : usuarios) {
            loginAcessoService.adicionarLoginAcesso(empresa, usuario);

            UsuarioGrupoEmpresa usuarioGrupoEmpresa = new UsuarioGrupoEmpresa(usuario, grupoEmpresa);
            grupoService.adicionarUsuarioGrupoEmpresa(usuarioGrupoEmpresa);
        }

    }

    private List<Usuario> listarUsuarioSuporte() {
        return repositorio.listarUsuarioPorPerfil(EnumTipoPerfil.SUPORTE.getTipo());
    }

    public List<UsuarioAcessoRapido> getUsuarioAcessoRapidoList(Usuario usuario) {
        return repositorio.getUsuarioAcessoRapidoList(usuario);
    }

    public Usuario getUserByFuncionario(Funcionario funcionario) {
        return repositorio.getUserByFuncionario(funcionario);
    }

    @Override
    public Criteria getModel(UsuarioFiltro filtro) {
        Criteria criteria = super.getModel(filtro);
        criteria.createAlias("idPerfil", "idPerfil");

        addIlikeRestrictionTo(criteria, "nome", filtro.getNome(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "login", filtro.getLogin(), MatchMode.ANYWHERE);
        addIlikeRestrictionTo(criteria, "email", filtro.getEmail(), MatchMode.ANYWHERE);
        addEqRestrictionTo(criteria, "contaBloqueada", "N");
        addEqRestrictionTo(criteria, "contaExpirada", "N");
        addEqRestrictionTo(criteria, "idPerfil", filtro.getPerfil());
        addEqRestrictionTo(criteria, "idPerfil.tipo", filtro.getTipoPerfil());
        criteria.createAlias("loginAcessoList", "loginAcesso");
        criteria.createAlias("loginAcesso.idEmpresa", "empresa");
        addEqRestrictionTo(criteria, "empresa.tenatID", adHocTenant.getTenantID());
        if (!filtro.getUsuario().getIdPerfil().getTipo().equals(EnumTipoPerfil.SUPORTE.getTipo())) {
            criteria.add(Restrictions.ne("idPerfil.tipo", EnumTipoPerfil.SUPORTE.getTipo()));
        }

        return criteria;
    }

    public Usuario getUsuarioSuporte() {
        return repositorio.getUserByLogin("suporte");
    }

    public Usuario logarComUsuarioSuporte() {
        Usuario usuario = getUsuarioSuporte();

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuario;
    }

    public Usuario logarComTenant(String tenant) {
        Usuario usuario = new Usuario();
        usuario.setTenat(tenant);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, "", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return usuario;
    }

    public Usuario logarComCnpj(String cnpj) {
        Empresa empresa = empresaService.buscarPorCnpj(CpfCnpjUtil.mascararCnpj(CpfCnpjUtil.removerMascaraCnpj(cnpj)));
        if (empresa != null) {
            Usuario usuario = new Usuario();
            usuario.setTenat(empresa.getId().toString());

            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, "", null);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return usuario;
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        return null;
    }

    public List<Usuario> listarUsuariosRecebemNotificacaoIugu(String tenat) {
        return repositorio.listarUsuarioQueRecebemNotificacaoIugu(tenat);
    }

    public boolean precisaAceitarTermo(Usuario usuario) {
        Integer versaoTermo = Integer.parseInt(SystemPreferencesUtil.getPropOrThrow("defaults.versaoTermoUso", () -> new IllegalStateException("Termo de uso não configurado")));

        return !repositorio.leuVersaoTermo(usuario, versaoTermo);
    }

    public UsuarioLeituraTermo salvarLeituraTermo(UsuarioLeituraTermo ult) {
        return repositorio.setEntity(ult);
    }

    public void removerLeituraTermo(Usuario user) {
        repositorio.removerLeituraTermo(user);
    }

    public List<Usuario> listarUsuariosRecebemNotificacaoAcessosPorEmpresa() {
        return repositorio.listarUsuariosRecebemNotificacaoAcessosPorEmpresa();
    }

}
