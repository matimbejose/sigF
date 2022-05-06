package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.PermissaoPerfil;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PerfilFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.PerfilService;
import br.com.villefortconsulting.sgfinancas.servicos.PermissaoService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
public class PerfilControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PerfilService perfilService;

    @EJB
    private PermissaoService permissaoService;

    private Permissao permissaoSelecionada;

    private PermissaoPerfil permissaoPerfilSelecionado;

    private LazyDataModel<Perfil> model;

    private PerfilFiltro filtro = new PerfilFiltro();

    private List<Permissao> permissaoList;

    private String descricaoPesquisa;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, perfilService::quantidadeRegistrosFiltrados, perfilService::getListaFiltrada);
    }

    public String doFinishExcluir() {
        perfilService.remover(perfilSelecionado);
        return "listaPerfil.xhtml";
    }

    public void doAdicionaPermissao() {
        // adiciona permissao ao usuario
        this.perfilSelecionado.addPermissao(permissaoSelecionada);
        this.permissaoList.remove(permissaoSelecionada);
    }

    public void doAceitarPermissaoTodas() {
        if (!permissaoList.isEmpty()) {
            for (Permissao permissaoOjb : permissaoList) {
                this.perfilSelecionado.addPermissao(permissaoOjb);
            }
            permissaoList.clear();
        }
    }

    public void doRemoverPermissaoTodas() {

        if (!perfilSelecionado.getPermissaoPerfilList().isEmpty()) {
            for (PermissaoPerfil permissaoOjb : perfilSelecionado.getPermissaoPerfilList()) {
                permissaoList.add(permissaoOjb.getIdPermissao());
            }
            perfilSelecionado.getPermissaoPerfilList().clear();
        }
    }

    public void doRemoverPermissao() {
        permissaoList.add(permissaoPerfilSelecionado.getIdPermissao());
        perfilSelecionado.removePermissao(permissaoPerfilSelecionado);
    }

    @Override
    public void cleanCache() {
        descricaoPesquisa = "";
        permissaoList = new ArrayList<>();
    }

    public String doStartAlterar() {
        cleanCache();
        perfilSelecionado.setPermissaoPerfilList(perfilService.getPermissoes(perfilSelecionado));
        return "cadastroPerfil.xhtml";
    }

    public String doStartVincularPermicoesPerfil() {
        return "cadastroVinculoPerfilUsuarioPermissao.xhtml";
    }

    public void doPesquisaPermissao() {
        permissaoList = permissaoService.getPermissoesPorPerfil(descricaoPesquisa, getUsuarioLogado().getIdPerfil().getTipo());
    }

    private Perfil perfilSelecionado;

    public List<Perfil> getPerfisPorPerfil() {
        return perfilService.getPerfisPorPerfil(getUsuarioLogado().getIdPerfil());
    }

    public List<Perfil> getPerfis() {
        return perfilService.getPerfis();
    }

    public List<Perfil> getPerfisComSuporte() {
        return perfilService.getPerfisComSuporte();
    }

    public String doFinishAdd() {

        if (existsViolationsForJSF(perfilSelecionado)) {
            return "cadastroPerfil.xhtml";
        }

        if (perfilSelecionado.getId() != null) {
            perfilService.alterar(perfilSelecionado);
            createFacesSuccessMessage("Perfil alterado com sucesso!");
        } else {
            perfilService.adicionar(perfilSelecionado);
            createFacesSuccessMessage("Grupo cadastrado com sucesso!");
        }

        return "listaPerfil.xhtml";
    }

    public String doStartAdd() {
        cleanCache();
        setPerfilSelecionado(new Perfil());
        return "cadastroPerfil.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return perfilService.getDadosAuditoriaByID(perfilSelecionado);
    }

    public String getDescricaoPerfil() {
        return getUsuarioLogado().getIdPerfil().getDescricao();
    }

    public String doShowAuditoria() {
        return "listaAuditoriaPerfil.xhtml";
    }

}
