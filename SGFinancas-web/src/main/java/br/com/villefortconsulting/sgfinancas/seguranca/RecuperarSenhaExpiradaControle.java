package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.entidades.LoginAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.util.StringUtil;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
public class RecuperarSenhaExpiradaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Você deve a nova senha")
    private String senhaNova;

    @NotNull(message = "Você repetir a nova senha")
    private String senhaRepetir;

    @NotNull(message = "Você repetir a nova senha")
    private String senhaAntiga;

    @NotNull(message = "Você deve informa o login")
    private String login;

    private transient InitialContext initialContext;

    private static final String RECUPERAR_SENHA = "recuperarSenhaExpirada.xhtml";

    private InitialContext getInitialContext() throws NamingException {
        if (initialContext == null) {
            initialContext = new InitialContext();
        }
        return initialContext;
    }

    public String recuperarNomeLogin() {
        HttpServletRequest session = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String loginUsuario = (String) session.getSession().getAttribute("loginUsuario");
        login = loginUsuario;
        return loginUsuario;
    }

    public String doStartRecuperarSenha() {
        try {
            LoginAcessoService loginAcessoService = (LoginAcessoService) getInitialContext().lookup("java:module/LoginAcessoService");
            LoginAcesso loginAcesso = loginAcessoService.getLoginAcesso(login);

            if (loginAcesso == null) {
                createFacesErrorMessage("Usuario não existe favor entrar em contato com o RH");
                return RECUPERAR_SENHA;
            } else {
                Usuario usuario = loginAcessoService.loadUserByLogin(login, loginAcesso.getIdEmpresa().getTenatID());
                if (StringUtil.criptografarMD5(senhaNova).equals(usuario.getPassword())) {
                    createFacesErrorMessage("Senha nova do usuario esta igual a anterior.");
                    return RECUPERAR_SENHA;
                } else if (!senhaRepetir.equals(senhaNova)) {
                    createFacesErrorMessage("Favor digitar a senha corretamente.");
                    return RECUPERAR_SENHA;
                }
                usuario.setSenha(StringUtil.criptografarMD5(senhaNova));
                usuario.setContaExpirada("N");
                loginAcessoService.atualizaUsuario(usuario);
                createFacesSuccessMessage("Senha alterada com sucesso!");
                return "login.xhtml";
            }
        } catch (NamingException ex) {
            return "recuperarSenha.xhtml";
        }
    }

    public String doCancelRecuperarSenha() {
        login = "";
        return "login.xhtml";
    }

}
