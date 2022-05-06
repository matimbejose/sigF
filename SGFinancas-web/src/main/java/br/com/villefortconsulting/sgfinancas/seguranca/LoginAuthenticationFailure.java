/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.seguranca;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author Desenvolvimento1
 */
@Component
public class LoginAuthenticationFailure extends SimpleUrlAuthenticationFailureHandler {

    private static final String LOGIN_ERRO = "/login.xhtml?erro";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String username = request.getParameter("username");

        if (exception instanceof LockedException) {
            setDefaultFailureUrl(LOGIN_ERRO);

        } else if (exception instanceof AccountExpiredException) {
            request.getSession().setAttribute("loginUsuario", username);
            setDefaultFailureUrl("/recuperarSenhaExpirada.xhtml");

        } else if (exception instanceof DisabledException) {
            setDefaultFailureUrl(LOGIN_ERRO);

        } else if (exception instanceof IPInvalidoException) {
            setDefaultFailureUrl(LOGIN_ERRO);

        } else if (exception instanceof EmpresaBloqueadaException) {
            setDefaultFailureUrl(LOGIN_ERRO);

        } else if (exception instanceof SessionAuthenticationException) {
            setDefaultFailureUrl("/login.xhtml?erroSession");

        } else {
            setDefaultFailureUrl(LOGIN_ERRO);
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
