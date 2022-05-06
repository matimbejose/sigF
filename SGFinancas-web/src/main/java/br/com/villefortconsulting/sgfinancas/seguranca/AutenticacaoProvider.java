package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.exception.AcessoBloqueadoException;
import br.com.villefortconsulting.exception.ContaBloqueadaException;
import br.com.villefortconsulting.exception.LoginNaoEncontratoException;
import br.com.villefortconsulting.exception.UsuarioOuSenhaInvalidaException;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Named
@SessionScoped
public class AutenticacaoProvider implements AuthenticationProvider, Serializable {

    private static final long serialVersionUID = 1L;

    private LoginAcessoService loginAcessoService;

    private transient InitialContext initialContext;

    private Boolean ambienteTeste;

    @Override
    public Authentication authenticate(Authentication authentication) {
        initLoginService();
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        try {
            Usuario usuario;
            if (isAmbienteTeste() && username != null && username.equals(password) && !username.trim().isEmpty()) {
                usuario = loginAcessoService.getUserByLogin(username);
                usuario = loginAcessoService.autenticarUsuario(username, usuario.getSenha(), true, null, false, false);
            } else {
                usuario = loginAcessoService.autenticarUsuario(username, password, false, null, false, true);
            }
            return loginAcessoService.createAuthFor(usuario, password);
        } catch (LoginNaoEncontratoException | UsuarioOuSenhaInvalidaException | AcessoBloqueadoException | ContaBloqueadaException ex) {
            throw new AuthenticationException(ex.getMessage(), ex) {

                private static final long serialVersionUID = 1L;

            };
        } catch (CadastroException ex) {
            throw new DisabledException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return true;
    }

    private void initLoginService() {
        if (loginAcessoService == null) {
            try {
                loginAcessoService = (LoginAcessoService) getInitialContext().lookup("java:module/LoginAcessoService");
            } catch (NamingException ex) {
                FacesMessage fm = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Não foi possível conectar-se com a fonte de dados.",
                        "Não foi possível conectar-se com a fonte de dados.");
                FacesContext.getCurrentInstance().addMessage(null, fm);
            }
        }
    }

    private InitialContext getInitialContext() throws NamingException {
        if (initialContext == null) {
            initialContext = new InitialContext();
        }
        return initialContext;
    }

    private boolean isAmbienteTeste() {
        if (ambienteTeste == null) {
            ambienteTeste = "teste".equalsIgnoreCase(SystemPreferencesUtil.getProp("ambiente", "producao"));
        }
        return ambienteTeste;
    }

}
