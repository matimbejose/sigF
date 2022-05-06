package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.exception.UsuarioException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecuperarSenhaUsuarioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Você deve especificar o login do usuário")
    private String login;

    @NotNull(message = "Você deve especificar a data nascimento do usuário")
    private String telefone;

    private Usuario usuario;

    private transient InitialContext initialContext;

    private LoginAcessoService loginAcessoService;

    private UsuarioService usuarioService;

    private static final String LOGIN_PAGE = "login.xhtml";

    @PostConstruct
    protected void postConstruct() {
        try {
            loginAcessoService = (LoginAcessoService) getInitialContext().lookup("java:module/LoginAcessoService");
            usuarioService = (UsuarioService) getInitialContext().lookup("java:module/UsuarioService");
        } catch (NamingException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limparLogin() {
        login = "";
    }

    public String recuperarSenhaEmailCadastrado() {
        try {
            if (StringUtils.isEmpty(login)) {
                throw new CadastroException("Informe um login.", null);
            }
            LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(login);
            if (loginAcesso == null) {
                createFacesErrorMessage("Usuario não encontrado.");
                return "recuperarSenha.xhtml";
            } else {
                usuario = loginAcessoService.loadUserByLogin(login, loginAcesso.getIdEmpresa().getTenatID());
                if (StringUtils.isEmpty(usuario.getEmail())) {
                    throw new CadastroException("Esse usuário não possui um email cadastrado, favor entrar em contato com o suprote.", null);
                }
                return enviarSenhaPorEmail();
            }
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "recuperarSenha.xhtml";
        }
    }

    public String recuperarSenhaEmailNovo() {
        if (usuario != null && StringUtils.isEmpty(telefone)) {
            createFacesErrorMessage("Informe um número de telefone.");
            return "enviarSenhaEmail.xhtml";
        }
        return enviarSenhaPorEmail();
    }

    private String enviarSenhaPorEmail() {
        try {
            usuarioService.enviarSenhaPorEmail(usuario);
            return LOGIN_PAGE + "?faces-redirect=true&alert=Senha alterada com sucesso! Email de envio: " + usuario.getEmail();
        } catch (UsuarioException | EmailException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "recuperarSenha.xhtml";
        }
    }

    public String doCancelRecuperarSenha() {
        return LOGIN_PAGE;
    }

    private InitialContext getInitialContext() throws NamingException {
        if (initialContext == null) {
            initialContext = new InitialContext();
        }
        return initialContext;
    }

}
