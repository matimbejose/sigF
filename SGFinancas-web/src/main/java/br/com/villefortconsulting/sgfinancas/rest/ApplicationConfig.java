package br.com.villefortconsulting.sgfinancas.rest;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import lombok.Getter;

@ApplicationPath(ApplicationConfig.BASE_PATH)
@Getter
@SuppressWarnings({"squid:S2139", "squid:S2325", "squid:CallToDeprecatedMethod", "deprecation"})
public class ApplicationConfig extends Application {

    private final Set<Object> singletons = new HashSet<>();

    public static final String BASE_PATH = "RESTful";

    public ApplicationConfig() {
        initSwagger();
        initCors();
    }

    private static void initSwagger() {
        String sep = "/";
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("http://homolog-sgfinancas.com/");
        beanConfig.setBasePath(sep + BASE_PATH + sep);
        beanConfig.setResourcePackage(UsuarioRest.class.getPackage().getName());
        beanConfig.setTitle("SG Finanças API");
        beanConfig.setDescription("API para o aplicativo mobile do SG Finanças");
        beanConfig.setScan(true);
    }

    private void initCors() {
        CorsFilter corsFilter = new CorsFilter();
        corsFilter.getAllowedOrigins().add("*");
        corsFilter.setAllowedMethods("OPTIONS, GET, POST, DELETE, PUT, PATCH");
        singletons.add(corsFilter);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        addRestResourceClasses(resources);
        resources.add(ApiListingResource.class);
        resources.add(SwaggerSerializers.class);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(br.com.villefortconsulting.sgfinancas.rest.AgendamentoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.BancoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.BuscaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.CallbackRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.CategoriaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.CidadeRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ClienteRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.CompraRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ContaBancariaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ContaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.EmpresaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.EstoqueRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ExceptionHandler.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.FormaPagamentoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.FornecedorRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.FuncionarioRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.NFeRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.OSRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.OrcamentoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.OrdemDeServicoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.PagamentoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ParametroRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ProdutoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.ServicoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.TransacaoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.TransportadoraRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.UsuarioRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.VeiculoRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.VendaRest.class);
        resources.add(br.com.villefortconsulting.sgfinancas.rest.VistoriaRest.class);
    }

}
