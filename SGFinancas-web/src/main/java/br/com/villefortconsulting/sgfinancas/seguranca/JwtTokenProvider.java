package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.sgfinancas.servicos.TokenAppService;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private TokenAppService tokenAppService;

    @PostConstruct
    protected void init() {
        tokenAppService = new TokenAppService();
    }

    public Authentication getAuthentication(String token) {
        StringBuilder name = new StringBuilder();
        for (String s : tokenAppService.getTokenData(token)) {
            name.append(":").append(s);
        }
        return new UsernamePasswordAuthenticationToken(name.substring(1), "", new ArrayList<>());
    }

    public String getUsername(String token) {
        return tokenAppService.getTokenData(token)[0];
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            getUsername(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
