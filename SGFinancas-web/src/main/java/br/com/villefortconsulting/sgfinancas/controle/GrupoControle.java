package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Grupo;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.GrupoPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.UsuarioGrupoEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.GrupoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.GrupoService;
import br.com.villefortconsulting.sgfinancas.servicos.PermissaoService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrupoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private GrupoService service;

    @EJB
    private PermissaoService permissaoService;

    @EJB
    private UsuarioService usuarioService;

    private GrupoEmpresa grupoEmpresaSelecionado;

    private String descricaoPesquisa;

    private String descricaoPesquisaUsuario;

    private List<Permissao> permissaoList;

    private List<Usuario> usuarioList;

    private Permissao permissaoSelecionada;

    private Usuario usuarioSelecionado;

    private GrupoPermissao grupoPermissaoSelecionada;

    private UsuarioGrupoEmpresa usuarioGrupoEmpresaSelecionado;

    private LazyDataModel<GrupoEmpresa> model;

    private GrupoFiltro filtro = new GrupoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                service::quantidadeGrupoFiltrado,
                service::getListaGrupoFiltrado,
                filter -> {
                    filter.setUsuarioLogado(getUsuarioLogado());
                    filter.setEmpresa(empresaService.getEmpresa());
                });
    }

    public List<Grupo> getGrupos() {
        return service.getGrupos();
    }

    @Override
    public void cleanCache() {
        permissaoList = new LinkedList<>();
        usuarioList = new LinkedList<>();
    }

    public String doStartAdd() {
        cleanCache();
        this.grupoEmpresaSelecionado = new GrupoEmpresa(new Grupo(), empresaService.getEmpresa(), getUsuarioLogado());
        doPesquisaPermissao();
        doPesquisaUsuario();
        return "cadastroGrupo.xhtml?faces-redirect=true";
    }

    public String doFinishAdd() {
        try {
            service.salvarGrupoEmpresa(grupoEmpresaSelecionado);
            createFacesSuccessMessage("Grupo salvo com sucesso!");
            return "listaGrupo.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroGrupo.xhtml";
        }
    }

    public String doStartAlterar() {
        cleanCache();
        grupoEmpresaSelecionado.getIdGrupo().setGrupoPermissaoList(service.getPermissoes(grupoEmpresaSelecionado.getIdGrupo()));
        grupoEmpresaSelecionado.setUsuarioGrupoEmpresaList(usuarioService.getUsuarioGrupoEmpresa(grupoEmpresaSelecionado));

        return "cadastroGrupo.xhtml";
    }

    public String doFinishExcluir() {
        service.removerGrupoEmpresa(grupoEmpresaSelecionado);
        createFacesSuccessMessage("Grupo removido com sucesso!");
        return "listaGrupo.xhtml";
    }

    public void doPesquisaUsuario() {
        usuarioList = usuarioService.getUsuarioPorEmpresaLogada(descricaoPesquisaUsuario, empresaService.getEmpresa()).stream()
                .filter(user -> !user.getIdPerfil().getEhSuporte())
                .collect(Collectors.toList());

        grupoEmpresaSelecionado.getUsuarioEmpresaGrupoList().stream()
                .map(UsuarioGrupoEmpresa::getIdUsuario)
                .forEach(usuarioList::remove);
    }

    public void clearUsuariosAndPermissoes() {
        this.permissaoList = new LinkedList<>();
        this.usuarioList = new LinkedList<>();
        this.grupoEmpresaSelecionado.getIdGrupo().setGrupoPermissaoList(new LinkedList<>());
        this.grupoEmpresaSelecionado.setUsuarioGrupoEmpresaList(new LinkedList<>());
    }

    public void doPesquisaPermissao() {
        permissaoList = permissaoService.getPermissoes(descricaoPesquisa);

        grupoEmpresaSelecionado.getIdGrupo().getGrupoPermissaoList().stream()
                .map(GrupoPermissao::getIdPermissao)
                .forEach(permissaoList::remove);
    }

    public void doAceitarTodasPermissoes() {
        if (!permissaoList.isEmpty()) {
            for (Permissao permissao : permissaoList) {
                this.grupoEmpresaSelecionado.getIdGrupo().addPermissao(permissao);
            }
            permissaoList.clear();
        }
    }

    public void doRemoverPermissaoTodas() {

        if (!grupoEmpresaSelecionado.getIdGrupo().getGrupoPermissaoList().isEmpty()) {
            for (GrupoPermissao permissaoOjb : grupoEmpresaSelecionado.getIdGrupo().getGrupoPermissaoList()) {
                permissaoList.add(permissaoOjb.getIdPermissao());
            }
            grupoEmpresaSelecionado.getIdGrupo().getGrupoPermissaoList().clear();
        }
    }

    public void doAdicionaPermissao() {

        // adiciona permissao ao grupo
        this.grupoEmpresaSelecionado.getIdGrupo().addPermissao(permissaoSelecionada);

        // remove permissao da lista de disponíveis
        this.permissaoList.remove(permissaoSelecionada);
    }

    public void doAdicionaUsuario() {
        this.grupoEmpresaSelecionado.addUsuario(usuarioSelecionado);
        this.usuarioList.remove(usuarioSelecionado);
    }

    public void doRemoverPermissao() {
        permissaoList.add(grupoPermissaoSelecionada.getIdPermissao());
        grupoEmpresaSelecionado.getIdGrupo().removePermissao(grupoPermissaoSelecionada);
    }

    public void doRemoveUsuario() {
        usuarioList.add(usuarioGrupoEmpresaSelecionado.getIdUsuario());
        grupoEmpresaSelecionado.removeUsuario(usuarioGrupoEmpresaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaGrupo.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(GrupoEmpresa.class, grupoEmpresaSelecionado.getId());
    }

    public String getTipoSuporte() {
        return EnumTipoUsuario.ADMINISTRADOR.getTipo();
    }

    public String getTipoUsuarioMaster() {
        return EnumTipoUsuario.MASTER_USUARIO.getTipo();
    }

    public String getTipoMasterVendedor() {
        return EnumTipoUsuario.MASTER_VENDEDOR.getTipo();
    }

    public String getTipoUsuarioComum() {
        return EnumTipoUsuario.USUARIO_COMUM.getTipo();
    }

    public String getTipoVendedor() {
        return EnumTipoUsuario.VENDEDOR.getTipo();
    }

    public LazyDataModel<GrupoEmpresa> getModel() {
        return model;
    }

    public void setModel(LazyDataModel<GrupoEmpresa> model) {
        this.model = model;
    }

}
