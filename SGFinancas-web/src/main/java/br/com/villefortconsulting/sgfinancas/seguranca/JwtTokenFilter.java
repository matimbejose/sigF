package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.entity.Erro;
import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.rest.ApplicationConfig;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        String token = jwtTokenProvider.resolveToken(httpReq);
        String sep = "/";
        if (token != null) {
            if (!jwtTokenProvider.validateToken(token)) {
                RetornoWs.Erros erros = new RetornoWs.Erros();
                erros.addErro(new Erro("Token inválido"));

                HttpServletResponse response = (HttpServletResponse) res;
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(new Gson().toJson(erros));
                return;
            } else {
                SecurityContextHolder.getContext().setAuthentication(jwtTokenProvider.getAuthentication(token));
            }
        } else if (httpReq.getRequestURI().startsWith(sep + ApplicationConfig.BASE_PATH + sep)) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("", "", new ArrayList<>()));
        }
        filterChain.doFilter(req, res);
    }

}
