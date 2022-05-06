package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.LoginDuplicadoException;
import br.com.villefortconsulting.exception.SenhaIncorretaException;
import br.com.villefortconsulting.sgfinancas.controle.apoio.ControleMenu;
import br.com.villefortconsulting.sgfinancas.entidades.AcessoRapido;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Funcionario;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioAcessoRapido;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioLeituraTermo;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.UsuarioDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.GrupoFiltro;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UsuarioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AcessoRapidoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncionarioService;
import br.com.villefortconsulting.sgfinancas.servicos.GrupoService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.PerfilService;
import br.com.villefortconsulting.sgfinancas.servicos.PermissaoService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private PermissaoService permissaoService;

    @EJB
    private GrupoService grupoService;

    @EJB
    private PerfilService perfilService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private FuncionarioService funcionarioService;

    @EJB
    private AcessoRapidoService acessoRapidoService;

    @Inject
    private CadastroControle cadastroControle;

    private static final String PATH_LISTA_USUARIO = "/restrito/usuario/listaUsuario.xhtml";

    private static final String PATH_CADASTRO_USUARIO = "/restrito/usuario/cadastroUsuario.xhtml";

    private Usuario usuarioLogado;

    private ControleMenu controleMenu = new ControleMenu();

    @NotNull(message = "Você deve especificar o usuário")
    private String login;

    @NotNull(message = "Você deve especificar uma senha")
    @Size(min = 8, message = "Você deve especificar uma senha contendo letras e números com tamanho mínimo de 8 caracteres.")
    private String senha;

    private Usuario usuarioSelecionado;

    private String descricaoPesquisa;

    private List<Permissao> permissaoList;

    private Permissao permissaoSelecionada;

    private UsuarioPermissao usuarioPermissaoSelecionada;

    private String descricaoPesquisaGrupo;

    private GrupoEmpresa grupoEmpresaSelecionado;

    private boolean todasLista;

    private UsuarioGrupoEmpresa usuarioGrupoEmpresaSelecionado;

    private List<GrupoEmpresa> grupoEmpresaList;

    private transient Part part;

    private UsuarioFiltro filtro = new UsuarioFiltro();

    private LazyDataModel<Usuario> model;

    private boolean selecionaAbaGrupo = false;

    private String layout = "absolution";

    private boolean orientationRTL;

    private Integer revNumber = 8;

    private Boolean jaAceitouTermo;

    public String getTheme() {
        usuarioLogado = getUsuarioLogado();
        if (null == usuarioLogado.getTema() || usuarioLogado.getTema().isEmpty()) {
            return "absolution";
        } else {
            layout = usuarioLogado.getTema().split("-")[0];
            return usuarioLogado.getTema();
        }
    }

    public void setTheme(String theme) {
        this.layout = theme.split("-")[0];
        usuarioLogado = usuarioService.buscar(getUsuarioLogado().getId());
        usuarioLogado.setTema(theme);
        usuarioService.alterar(usuarioLogado);
        getUsuarioLogado().setTema(theme);
    }

    public String getMenuMode() {
        usuarioLogado = getUsuarioLogado();
        if (null == usuarioLogado.getMenuMode() || usuarioLogado.getMenuMode().isEmpty()) {
            return "layout-menu-static";
        } else {
            return usuarioLogado.getMenuMode();
        }
    }

    public void setMenuMode(String menuMode) {
        usuarioLogado = usuarioService.buscar(getUsuarioLogado().getId());
        usuarioLogado.setMenuMode(menuMode);
        usuarioService.alterar(usuarioLogado);
        getUsuarioLogado().setMenuMode(menuMode);
    }

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                usuarioService,
                filter -> {
                    filter.setUsuario(getUsuarioLogado());
                    filter.setTenant(empresaService.getEmpresa().getTenatID());
                });
    }

    public StreamedContent getImagemPerfil() {
        Usuario usuario = getUsuarioLogado();
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc.getRenderResponse()) {
            return new DefaultStreamedContent();
        } else if (usuario.getFoto() != null) {
            return new DefaultStreamedContent(new ByteArrayInputStream(usuario.getFoto()), "image/jpeg");
        } else {
            return null;
        }
    }

    public List<Perfil> getPerfis() {
        List<Perfil> perfis = new ArrayList<>();
        Perfil perfilUsuario = getUsuarioLogado().getIdPerfil();

        if (Boolean.TRUE.equals(perfilUsuario.getEhSuporte())) {
            perfis = perfilService.getPerfis();
        }

        return perfis;
    }

    public List<Perfil> getPerfisSemServidor() {
        List<Perfil> perfis = getPerfis();
        Perfil perfilServidor = new Perfil();
        perfis.remove(perfilServidor);
        return perfis;
    }

    public void salvarFoto() throws FileException {
        usuarioSelecionado = getUsuarioLogado();
        usuarioSelecionado.setFoto(FileUtil.convertPartToBytes(part));
        doFinishAdd();
    }

    @Override
    public Usuario getUsuarioLogado() {
        try {
            usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            usuarioLogado.setUsuarioAcessoRapidoList(usuarioService.getUsuarioAcessoRapidoList(usuarioLogado));
            if (usuarioLogado.isPrecisaAtualizarPermissao()) {
                usuarioLogado.setPrecisaAtualizarPermissao(false);
                SecurityContextHolder.getContext().setAuthentication(loginAcessoService.createAuthFor(usuarioLogado));
            }
        } catch (Exception ex) {
            usuarioLogado = new Usuario();
        }
        return usuarioLogado;
    }

    @Override
    public void cleanCache() {
        this.usuarioSelecionado = new Usuario();
        this.usuarioSelecionado.setIdContabilidade(getUsuarioLogado().getIdContabilidade());
        this.senha = "";
        this.descricaoPesquisa = "";
        this.descricaoPesquisaGrupo = "";
        this.permissaoList = new ArrayList<>();
        this.grupoEmpresaList = new ArrayList<>();
        this.selecionaAbaGrupo = false;
    }

    public List<Usuario> getUsuarios() {
        return usuarioService.getUsuarios();
    }

    public String doStartAdd() {
        cleanCache();
        return PATH_CADASTRO_USUARIO;
    }

    public String doFinishAdd() {
        try {
            usuarioSelecionado.setLogin(usuarioSelecionado.getEmail());
            usuarioService.salvarUsuario(usuarioSelecionado, senha, empresaService.getEmpresa());
            createFacesSuccessMessage("Usuário salvo com sucesso!");
        } catch (SenhaIncorretaException | LoginDuplicadoException e) {
            selecionaAbaGrupo = false;
            createFacesErrorMessage(e.getMessage());
            return PATH_CADASTRO_USUARIO;
        }
        return PATH_LISTA_USUARIO;
    }

    public void validarSuporte() {
        try {
            int qte = usuarioService.validarSuporte(usuarioSelecionado);
            createFacesSuccessMessage("Logins validados, " + qte + " logins adicionados.");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public List<Funcionario> getFuncionariosSemUsuario() {
        List<Funcionario> list = funcionarioService.listarSemUsuario();

        if (usuarioSelecionado.getId() != null && usuarioSelecionado.getIdFuncionario() != null) {
            list.add(usuarioSelecionado.getIdFuncionario());
        }

        return list;
    }

    public String doStartAlterar() {
        permissaoList = new LinkedList<>();

        List<UsuarioPermissao> permissoesAtribuidas = usuarioService.getPermissoes(usuarioSelecionado).stream().sorted((p1, p2) -> p1.getIdPermissao().getDescricaoDetalhada().compareTo(p2.getIdPermissao().getDescricaoDetalhada())).collect(Collectors.toList());
        List<UsuarioGrupoEmpresa> gruposAtribuidos = usuarioService.getUsuarioGrupoEmpresaList(usuarioSelecionado, empresaService.getEmpresa()).stream().sorted((g1, g2) -> g1.getIdGrupoEmpresa().getIdGrupo().getDescricao().compareTo(g2.getIdGrupoEmpresa().getIdGrupo().getDescricao())).collect(Collectors.toList());

        usuarioSelecionado.setUsuarioPermissaoList(permissoesAtribuidas);
        usuarioSelecionado.setUsuarioGrupoEmpresaList(gruposAtribuidos);

        doPesquisaPermissao();
        doPesquisaGrupo();

        return PATH_CADASTRO_USUARIO;
    }

    public String doStartAlterarSenha() {
        LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(usuarioSelecionado.getLogin());
        senha = "";
        usuarioSelecionado.setLoginAcesso(loginAcesso);
        return "/restrito/seguranca/alterarSenhaUsuario.xhtml?faces-redirect=true";
    }

    public String doFinishExcluir() {
        if (usuarioLogado.equals(usuarioSelecionado)) {
            createFacesErrorMessage("Você não pode apagar a si mesmo.");
        } else {
            usuarioService.bloquearExpirarUsuario(usuarioSelecionado);
        }
        return PATH_LISTA_USUARIO;
    }

    public String alterarSenhaContaExpirada() {
        try {
            usuarioService.alterarSenhaEemailContaExpirada(usuarioSelecionado, senha);
            createFacesSuccessMessage("Senha alterada com sucesso.");
            return "/restrito/dashboard.xhtml";
        } catch (SenhaIncorretaException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/usuario/alterarSenhaExpirada.xhtml";
        }
    }

    public String doFinishAlterarSenha() {
        try {
            usuarioService.salvarValidarSenhaAndEmail(usuarioSelecionado, senha);
            createFacesSuccessMessage("Senha alterada com sucesso.");
        } catch (SenhaIncorretaException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return PATH_LISTA_USUARIO;
    }

    public String tipoPerfilUsuario() {
        return usuarioSelecionado.getIdPerfil().getTipo();
    }

    public void doPesquisaPermissao() {
        permissaoList = loginAcessoService.getPermissoesUsuario(empresaService.getEmpresa(), usuarioLogado, descricaoPesquisa);

        // Removendo das permissoes disponiveis das permissoes ja atribuidas
        permissaoList.removeAll(usuarioSelecionado.getUsuarioPermissaoList().stream()
                .map(UsuarioPermissao::getIdPermissao)
                .distinct()
                .collect(Collectors.toList()));

        permissaoList.removeIf(p -> p.getId() == null);

        Collections.sort(permissaoList, (p1, p2) -> p1.getDescricaoDetalhada().compareTo(p2.getDescricaoDetalhada()));
    }

    public void doAdicionaPermissao() {
        // adiciona permissao ao usuario
        this.usuarioSelecionado.addPermissao(permissaoSelecionada);

        // Atribui tenant ao usuario permissao
        usuarioService.addTenantUsuarioPermissao(usuarioSelecionado.getUsuarioPermissaoList());

        // Remove permissao da lista de disponiveis
        this.permissaoList.remove(permissaoSelecionada);
    }

    public void doAceitarPermissaoTodas() {
        if (!permissaoList.isEmpty()) {
            permissaoList.forEach(usuarioSelecionado::addPermissao);
            permissaoList.clear();

            // Atribui tenant ao usuario permissao
            usuarioService.addTenantUsuarioPermissao(usuarioSelecionado.getUsuarioPermissaoList());
        }
    }

    public void doRemoverPermissaoTodas() {
        if (!usuarioSelecionado.getUsuarioPermissaoList().isEmpty()) {
            permissaoList.addAll(usuarioSelecionado.getUsuarioPermissaoList().stream()
                    .map(UsuarioPermissao::getIdPermissao)
                    .collect(Collectors.toList()));
            usuarioSelecionado.getUsuarioPermissaoList().clear();
        }
    }

    public void doRemoverPermissao() {
        permissaoList.add(usuarioPermissaoSelecionada.getIdPermissao());
        usuarioSelecionado.removePermissao(usuarioPermissaoSelecionada);
    }

    public void verificarUsuarioLogado() {
        if (usuarioSelecionado == null || usuarioSelecionado.getId() == null) {
            usuarioSelecionado = getUsuarioLogado();
            usuarioSelecionado.setSenha(null);
        }
    }

    public void doPesquisaGrupo() {
        GrupoFiltro filtroGrupo = new GrupoFiltro();

        filtroGrupo.setDescricao(descricaoPesquisaGrupo);
        filtroGrupo.setUsuarioLogado(usuarioLogado);
        filtroGrupo.setEmpresa(empresaService.getEmpresa());
        filtroGrupo.setVisualizaGestaoInterna("S");

        // grupos por perfil
        grupoEmpresaList = grupoService.getListaGrupoFiltrado(filtroGrupo);

        // Removendo das grupos disponiveis dos grupos ja atribuidos
        grupoEmpresaList.removeAll(usuarioSelecionado.getUsuarioGrupoEmpresaList().stream()
                .map(UsuarioGrupoEmpresa::getIdGrupoEmpresa)
                .distinct()
                .collect(Collectors.toList()));

        // Ordenando grupos por descricao
        grupoEmpresaList = grupoEmpresaList.stream()
                .sorted((g1, g2) -> g1.getIdGrupo().getDescricao().compareTo(g2.getIdGrupo().getDescricao()))
                .collect(Collectors.toList());
    }

    public void doAdicionaGrupo() {
        // Atribui grupo ao usuario
        this.usuarioSelecionado.addGrupoEmpresa(grupoEmpresaSelecionado);

        // Remove grupo da lista de disponiveis
        this.grupoEmpresaList.remove(grupoEmpresaSelecionado);
    }

    public void doRemoveGrupo() {
        grupoEmpresaList.add(usuarioGrupoEmpresaSelecionado.getIdGrupoEmpresa());
        usuarioSelecionado.removeGrupoEmpresa(usuarioGrupoEmpresaSelecionado);
    }

    public String doShowAuditoria() {
        return "/restrito/usuario/listaAuditoriaUsuario.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return usuarioService.getDadosAuditoriaByID(usuarioSelecionado);
    }

    public List<Usuario> getUsuariosPorNome(String query) {
        return usuarioService.getUsuariosPorNome(query);
    }

    public void bloquearTelaDoUsuario() {
        getUsuarioLogado().setTelaBloqueada(true);
    }

    public void desbloquearTela() {
        getUsuarioLogado().setTelaBloqueada(false);
    }

    public List<AcessoRapido> getAcessoRapidoList() {
        return acessoRapidoService.listar();
    }

    public List<Usuario> listaUsuarioVendedorPorEmpresaLogada() {
        return listaUsuarioVendedorPorEmpresa(getEmpresaService().getEmpresa());
    }

    public List<Usuario> listaUsuarioVendedorPorEmpresa(Empresa empresa) {
        return usuarioService.listarUsuarioVendedorPorEmpresaLogada(empresa);
    }

    public List<Usuario> listaUsuarioPorEmpresa(Empresa empresa) {
        return usuarioService.listarUsuarioPorEmpresaLogada(empresa);
    }

    public void doAdicionaAcessoRapidoTodos() {
        doRemoverAcessoRapidoTodos();
        getAcessoRapidoList()
                .forEach(item -> usuarioLogado.getUsuarioAcessoRapidoList().add(new UsuarioAcessoRapido(usuarioLogado, item)));
        salvarUsuario();
    }

    public void doAdicionaAcessoRapido(AcessoRapido ar) {
        boolean contains = usuarioLogado.getUsuarioAcessoRapidoList().stream()
                .anyMatch(item -> item.getIdAcessoRapido().equals(ar));
        if (!contains) {
            usuarioLogado.getUsuarioAcessoRapidoList().add(new UsuarioAcessoRapido(usuarioLogado, ar));
            salvarUsuario();
        }
    }

    public void doRemoverAcessoRapidoTodos() {
        usuarioLogado.getUsuarioAcessoRapidoList().clear();
        salvarUsuario();
    }

    public void doRemoverAcessoRapido(UsuarioAcessoRapido uar) {
        usuarioLogado.getUsuarioAcessoRapidoList().remove(uar);
        salvarUsuario();
    }

    public void onRowReorder(ReorderEvent event) {
        UsuarioAcessoRapido a = usuarioLogado.getUsuarioAcessoRapidoList().get(event.getFromIndex());
        UsuarioAcessoRapido b = usuarioLogado.getUsuarioAcessoRapidoList().get(event.getToIndex());
        AcessoRapido aux;
        aux = a.getIdAcessoRapido();
        a.setIdAcessoRapido(b.getIdAcessoRapido());
        b.setIdAcessoRapido(aux);
        salvarUsuario();
    }

    public void salvarUsuario() {
        final String tenat = usuarioLogado.getTenat();
        usuarioLogado.getUsuarioAcessoRapidoList().forEach(item -> item.setTenatID(tenat));
        usuarioService.alterar(usuarioLogado);
    }

    public boolean possuiAcessoRapido(AcessoRapido ar) {
        if (ar == null) {
            return false;
        }
        return getUsuarioLogado().getUsuarioAcessoRapidoList().stream()
                .map(UsuarioAcessoRapido::getIdAcessoRapido)
                .filter(ar::equals)
                .count() > 0;
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("USUARIO_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Usuário",
                    usuarioService.hasAny("", empresaService.getEmpresa()),
                    false,
                    this::doStartAdd,
                    UsuarioDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    public String urlAindaNaoCliente() {
        if (SystemPreferencesUtil.getProp("ambiente", "PRODUCAO").equalsIgnoreCase("PRODUCAO")) {
            return "https://site.sgfinancas.com/index.html#teste";
        }
        return "cadastro.xhtml";
    }

    public List<Perfil> getPerfisPorPerfil() {
        List<Perfil> perfis = perfilService.getPerfisPorPerfil(getUsuarioLogado().getIdPerfil());
        if ("dev".equals(getUsuarioLogado().getMenuMode())) {
            return perfis;
        }
        if (usuarioSelecionado.getIdPerfil() != null && usuarioSelecionado.getIdPerfil().getEhSuporte()) {
            // Se o usuário sendo editado for suporte não deixa troca o perfil
            perfis.removeIf(p -> !p.getEhSuporte());
        } else {
            // Se for um novo ou edição de um que não é suporte, não deixa selecionar o suporte
            perfis.removeIf(Perfil::getEhSuporte);
        }
        return perfis;
    }

    public void validarTermo() {
        String pathTermo = "/restrito/termo.xhtml";
        if (Boolean.TRUE.equals(getJaAceitouTermo())) {
            return;
        }
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            if (!request.getRequestURL().toString().endsWith(pathTermo)) {
                FacesContext.getCurrentInstance().getExternalContext().redirect(pathTermo);
            }
        } catch (IOException ex) {
            Logger.getLogger(UsuarioControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getTermoUsoBase64() {
        String pdf = SystemPreferencesUtil.getPropOrThrow("defaults.pathTermoUso", () -> new IllegalStateException("Termo de uso não encontrado"));
        try {
            return "data:application/pdf;base64," + Base64.getEncoder().encodeToString(FileUtil.convertFileToBytes(new File(pdf)));
        } catch (FileException ex) {
            Logger.getLogger(UsuarioControle.class.getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    public String aceitarTermo() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        UsuarioLeituraTermo ult = new UsuarioLeituraTermo();
        ult.setIdUsuario(getUsuarioLogado());
        ult.setVersaoTermo(Integer.parseInt(SystemPreferencesUtil.getPropOrThrow("defaults.versaoTermoUso", () -> new IllegalStateException("Termo de uso não configurado"))));
        ult.setDataAceite(new Date());
        ult.setIp(request.getRemoteAddr());
        getUsuarioLogado().setLeuTermo(true);
        usuarioService.salvarLeituraTermo(ult);
        jaAceitouTermo = true;

        return "/restrito/index.xhtml";
    }

    public Boolean getJaAceitouTermo() {
        if (jaAceitouTermo == null) {
            jaAceitouTermo = !usuarioService.precisaAceitarTermo(getUsuarioLogado());
        }
        return jaAceitouTermo;
    }

}
