package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LoginAcessoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.PerfilService;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.In;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.ValidationException;
import javax.ws.rs.NotFoundException;
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
public class CredorControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private CidadeService cidadeService;

    @EJB
    private UFService ufService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private PerfilService perfilService;

    private LazyDataModel<Empresa> model;

    private LoginAcessoFiltro filtro = new LoginAcessoFiltro();

    private Empresa credorSelecionado;

    private UF ufSelecionado;

    private String login;

    private String senha;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro,
                loginAcessoService::quantidadeRegistrosFiltrados,
                loginAcessoService::getListaFiltradaEmpresa,
                filter -> {
                    filter.setUsuario(getUsuarioLogado());
                    filter.setTipoEmpresa(In.fromList(Arrays.asList("CR")));
                });
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAntecipador.xhtml";
    }

    public String doStartAdd() {
        credorSelecionado = new Empresa();
        credorSelecionado.setTipoEmpresa("CR");
        return "cadastroAntecipador.xhtml";
    }

    public String doStartAlterar() {
        Usuario user = getUsuarioLogado();
        if (!Boolean.TRUE.equals(user.getIdPerfil().getEhCredor())) {
            user = loginAcessoService.getCredorByEmpresa(credorSelecionado);
        }
        login = user.getLogin();
        return "/restrito/antecipador/cadastroAntecipador.xhtml";
    }

    public String doFinishAdd() {
        try {
            empresaService.salvarCredor(credorSelecionado, login, senha, getUsuarioLogado());
            createFacesSuccessMessage("Credor salvo com sucesso.");
            return "listaAntecipador.xhtml";
        } catch (UsuarioException ex) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "cadastroAntecipador.xhtml";
        }
    }

    public String doFinishExcluir() {
        empresaService.remover(credorSelecionado);
        createFacesSuccessMessage("Credor excluído com sucesso.");
        return "listaCredor.xhtml";
    }

    public void buscarDadosEmpresaPorCNPJ() {
        try {
            empresaService.buscarDadosEmpresaPorCNPJ(credorSelecionado.getCnpj(), credorSelecionado, new ArrayList<>());
        } catch (IllegalArgumentException | IllegalStateException ex) {
            if (!ex.getMessage().isEmpty()) {
                createFacesErrorMessage(ex.getMessage());
            }
        } catch (NotFoundException ex) {
            createFacesInfoMessage(ex.getMessage());
        } catch (ValidationException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
    }

    public void buscarEnderecoPorCep() {
        cidadeService.buscarEnderecoPorCep(credorSelecionado);
    }

    public List<Object> getDadosAuditoria() {
        return empresaService.getDadosAuditoriaByID(credorSelecionado);
    }

    public List<UF> getUFs() {
        return ufService.listar();
    }

    public List<Cidade> getCidades() {
        if (ufSelecionado != null) {
            return cidadeService.listar(ufSelecionado);
        }
        return new LinkedList<>();
    }

    public void preecherCredor() {
        if (credorSelecionado == null) {
            credorSelecionado = empresaService.getEmpresa();
            login = getUsuarioLogado().getLogin();

        }
    }

}
