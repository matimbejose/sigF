package br.com.villefortconsulting.sgfinancas.seguranca;

import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = {"br.com.villefortconsulting.sgfinancas.seguranca"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private LoginAuthenticationFailure loginAuthenticationFailure;

    @Autowired
    private LoginAuthenticationSucess loginAuthenticationSucess;

    @Autowired
    private CaptchaVerifierFilter captchaVerifierFilter;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Inject
    private AutenticacaoProvider autenticacaoProvider;

    private static final String RESTFUL = "/RESTful/**";

    private static final String LOGIN = "/login.xhtml";

    @Inject
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(autenticacaoProvider);
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/index.html**")
                .antMatchers("/resources/**")
                .antMatchers("/resources**")
                .antMatchers("/contato.html**")
                .antMatchers("/envia.html**")
                .antMatchers("/planos.xhtml**")
                .antMatchers("/cadastro.html**")
                .antMatchers("/cadastro.xhtml**")
                .antMatchers("/checkout.xhtml**")
                .antMatchers("/sitemap.html**")
                .antMatchers("/sobre-nos.html**")
                .antMatchers("/contato.xhtml**")
                .antMatchers("/RESTful/v1/public/usuarios/token")
                .antMatchers(HttpMethod.POST, "/RESTful/v1/public/empresas")
                .antMatchers(HttpMethod.GET, "/RESTful/v1/public/empresas/termo")
                .antMatchers("/RESTful/v1/public/busca/**")
                .antMatchers("/RESTful/v1/public/transacao/**")
                .antMatchers("/RESTful/v1/public/transacao")
                .antMatchers("/RESTful/v1/public/transacao**")
                .antMatchers(HttpMethod.OPTIONS, "/RESTful**", RESTFUL);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/recuperarSenha.xhtml").permitAll()
                .antMatchers("/enviarSenhaEmail.xhtml").permitAll()
                .antMatchers("/recuperarSenhaExpirada.xhtml/**").permitAll()
                .antMatchers("/javax.faces.resource/**").permitAll()
                .antMatchers("/login.xhtml/**").permitAll()
                .antMatchers(RESTFUL).permitAll()
                .antMatchers("/agenda/**").permitAll()
                .antMatchers("/link/**").permitAll()
                .antMatchers(
                        "/restrito/administracao/acessosPorEmpresa.xhtml",
                        "/restrito/administracao/servicosAgendados.xhtml",
                        "/restrito/administracao/procedures.xhtml",
                        "/restrito/administracao/introJSConfig/**"
                ).hasAnyAuthority("DEV")
                .antMatchers(
                        "/restrito/administracao/**",
                        "/restrito/antecipador/**",
                        "/restrito/relatorio/relatorioIndicacao.xhtml",
                        "/restrito/parametroGeral/**"
                ).hasAnyAuthority("SUPORTE")
                //                // paginas de acesso ao sistema
                //                //permissao
                //                .antMatchers("/restrito/seguranca/listaPermissao.xhtml").hasAnyRole("PERMISSAO_VISUALIZAR")
                //                .antMatchers("/restrito/seguranca/cadastroPermissao.xhtml").hasAnyRole("PERMISSAO_GERENCIAR")
                //                //perfil
                //                .antMatchers("/restrito/seguranca/listaPerfil.xhtml").hasAnyRole("PERFIL_VISUALIZAR")
                //                .antMatchers("/restrito/seguranca/cadastroPerfil.xhtml").hasAnyRole("PERFIL_GERENCIAR")
                //                //grupo
                //                .antMatchers("/restrito/seguranca/listaGrupo.xhtml").hasAnyRole("GRUPO_VISUALIZAR")
                //                .antMatchers("/restrito/seguranca/cadastroGrupo.xhtml").hasAnyRole("GRUPO_GERENCIAR")
                //                //Usuario
                //                .antMatchers("/restrito/seguranca/listaUsuario.xhtml").hasAnyRole("USUARIO_VISUALIZAR")
                //                .antMatchers("/restrito/seguranca/cadastroUsuario.xhtml").hasAnyRole("USUARIO_GERENCIAR")
                //                .antMatchers("/restrito/seguranca/alterarSenhaUsuario.xhtml").hasAnyRole("USUARIO_GERENCIAR")
                //                //parametro do sistema
                //                .antMatchers("/restrito/parametroSistema/cadastroParametroSistema.xhtml").hasAnyRole("PARAMETROS_DO_SISTEMA_GERENCIAR")
                .antMatchers("/restrito/**").authenticated()
                .anyRequest().authenticated().and()
                .addFilterBefore(this.captchaVerifierFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage(LOGIN)
                .failureHandler(this.loginAuthenticationFailure)
                .successHandler(this.loginAuthenticationSucess)
                .permitAll().and()
                .logout()
                .logoutUrl("/loginLogout.xhtml")
                .logoutSuccessHandler(this.logoutSuccessHandler)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll().and()
                .sessionManagement()
                .enableSessionUrlRewriting(true)
                .sessionFixation()
                .changeSessionId()
                .invalidSessionUrl(LOGIN)
                .maximumSessions(1)
                .expiredUrl(LOGIN).and().and()
                .exceptionHandling()
                .accessDeniedPage("/access.xhtml").and()
                .csrf().disable()
                .headers().cacheControl().disable();

        http.headers().frameOptions().sameOrigin();
        restConfig(http);
    }

    private void restConfig(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(RESTFUL, "/RESTful**").authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("POST");
        source.registerCorsConfiguration("/**", config);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

}
