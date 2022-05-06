package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.entity.Erro;
import br.com.villefortconsulting.entity.RetornoWs;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class MyBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx) throws IOException, ServletException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + getRealmName() + "\"");
        response.addHeader("Content-Type", "application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        PrintWriter writer = response.getWriter();
        RetornoWs.Erros erros = new RetornoWs.Erros();
        ObjectMapper mapper = new ObjectMapper();
        erros.addErro(new Erro(authEx.getMessage()));
        writer.print(mapper.writeValueAsString(erros));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("SG Finanças");
        super.afterPropertiesSet();
    }

}
