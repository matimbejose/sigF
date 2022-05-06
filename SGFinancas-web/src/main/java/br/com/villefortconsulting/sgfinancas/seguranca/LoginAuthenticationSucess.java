package br.com.villefortconsulting.sgfinancas.seguranca;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationSucess extends SimpleUrlAuthenticationSuccessHandler {

    private InitialContext initialContext;

    private InitialContext getInitialContext() throws NamingException {
        if (initialContext == null) {
            initialContext = new InitialContext();
        }
        return initialContext;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            UsuarioService usuarioService = (UsuarioService) getInitialContext().lookup("java:module/UsuarioService");
            if (usuarioService.precisaAceitarTermo(usuario)) {
                setDefaultTargetUrl("/restrito/termo.xhtml");
                usuario.setLeuTermo(false);
                super.onAuthenticationSuccess(request, response, authentication);
                return;
            }
            EmpresaService empresaService = (EmpresaService) getInitialContext().lookup("java:module/EmpresaService");
            Empresa empresa = empresaService.getEmpresa();
            PagamentoMensalidadeService pagamentoMensalidadeService = (PagamentoMensalidadeService) getInitialContext().lookup("java:module/PagamentoMensalidadeService");
            PagamentoMensalidade pagamento = pagamentoMensalidadeService.getUltimoPagamentoEmpresaLogada();
            boolean isSuporte = usuario.getIdPerfil().getEhSuporte();
            setDefaultTargetUrl("/restrito/index.xhtml");
            if (usuario.isSenhaExpirada() || usuario.isAccountNonExpired()) {
                setDefaultTargetUrl("/restrito/seguranca/alterarSenhaExpirada.xhtml");
            } else if (pagamento != null && !pagamento.isDentroDaVigenciaDoContrato() && !isSuporte) {
                setDefaultTargetUrl("/restrito/pagamento/renovar.xhtml");
            } else if (usuario.getQtdEmpresas() != null && usuario.getQtdEmpresas() > 1) {
                setDefaultTargetUrl("/selecionarEmpresa.xhtml");
            } else if (empresa != null && "S".equals(empresa.getPrimeiroLogin()) && !isSuporte) {
                empresa.setPrimeiroLogin("N");
                empresaService.alterar(empresa);
            } else if (empresa != null && empresa.isCredor()) {
                setDefaultTargetUrl("/restrito/index.xhtml");
            }
        } catch (NamingException ex) {
            Logger.getLogger(LoginAuthenticationSucess.class.getName()).log(Level.SEVERE, null, ex);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
