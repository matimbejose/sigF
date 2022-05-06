package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPermissao;
import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModuloFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AdministracaoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloService;
import br.com.villefortconsulting.sgfinancas.servicos.PermissaoService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
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
public class ModuloControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModuloService moduloService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private PermissaoService permissaoService;

    @EJB
    private AdministracaoService administracaoService;

    private Modulo moduloSelecionado;

    private LazyDataModel<Modulo> model;

    private ModuloFiltro filtro = new ModuloFiltro();

    private String descricaoPesquisa;

    private List<Permissao> permissaoList;

    private Permissao permissaoSelecionada;

    private ModuloPermissao moduloPermissaoSelecionada;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, moduloService);
    }

    @Override
    public void cleanCache() {
        this.descricaoPesquisa = "";
        this.permissaoList = new ArrayList<>();
        this.moduloPermissaoSelecionada = null;
    }

    public List<Modulo> getModulos() {
        return moduloService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        moduloSelecionado = new Modulo();
        return "/restrito/administracao/modulo/cadastroModulo.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/administracao/modulo/cadastroModulo.xhtml";
    }

    public String doFinishAdd() {
        try {
            moduloService.salvar(moduloSelecionado);
            moduloSelecionado.getModuloPermissaoList().stream()
                    .filter(ModuloPermissao::isNova)
                    .forEach(administracaoService::aplicarNovaPermissao);

            createFacesSuccessMessage("Módulo cadastrado com sucesso!");
            return "/restrito/administracao/modulo/listaModulo.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/administracao/modulo/cadastroModulo.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/administracao/modulo/listaAuditoriaModulo.xhtml";
    }

    public String doFinishExcluir() {
        moduloSelecionado.setAtivo("N");
        moduloService.salvar(moduloSelecionado);
        createFacesSuccessMessage("Módulo desativado com sucesso.");
        return "/restrito/administracao/modulo/listaModulo.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return moduloService.getDadosAuditoriaByID(moduloSelecionado);
    }

    public void removerPermissao(ModuloPermissao mp) {
        moduloSelecionado.getModuloPermissaoList().remove(mp);
    }

    public void doAdicionaPermissao() {
        // adiciona permissao ao usuario
        this.moduloSelecionado.addPermissao(permissaoSelecionada);

        // Remove permissao da lista de disponiveis
        this.permissaoList.remove(permissaoSelecionada);
    }

    public void doAceitarPermissaoTodas() {
        if (!permissaoList.isEmpty()) {
            permissaoList.forEach(moduloSelecionado::addPermissao);
            permissaoList.clear();
        }
    }

    public void doRemoverPermissaoTodas() {
        if (!moduloSelecionado.getModuloPermissaoList().isEmpty()) {
            permissaoList.addAll(moduloSelecionado.getModuloPermissaoList().stream()
                    .map(ModuloPermissao::getIdPermissao)
                    .collect(Collectors.toList()));
            moduloSelecionado.getModuloPermissaoList().clear();
        }
    }

    public void doRemoverPermissao() {
        permissaoList.add(moduloPermissaoSelecionada.getIdPermissao());
        moduloSelecionado.removePermissao(moduloPermissaoSelecionada);
    }

    public void doPesquisaPermissao() {
        permissaoList = permissaoService.getPermissoes(descricaoPesquisa);

        // Removendo das permissoes disponiveis das permissoes ja atribuidas
        permissaoList.removeAll(moduloSelecionado.getModuloPermissaoList().stream()
                .map(ModuloPermissao::getIdPermissao)
                .distinct()
                .collect(Collectors.toList()));

        // Ordenando permissoes por descricao detalhada
        permissaoList = permissaoList.stream()
                .sorted((p1, p2) -> p1.getDescricaoDetalhada().compareTo(p2.getDescricaoDetalhada()))
                .collect(Collectors.toList());
    }

}
