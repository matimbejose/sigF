package br.com.villefortconsulting.sgfinancas.rest;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jboss.resteasy.spi.CorsHeaders;

@Data
@EqualsAndHashCode
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    protected boolean allowCredentials = true;

    protected String allowedMethods;

    protected String allowedHeaders;

    protected String exposedHeaders;

    protected int corsMaxAge = 3600;

    protected Set<String> allowedOrigins = new HashSet<>();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String origin = requestContext.getHeaderString(CorsHeaders.ORIGIN);
        if (origin == null) {
            return;
        }
        if (requestContext.getMethod().equalsIgnoreCase("OPTIONS")) {
            preflight(origin, requestContext);
        } else {
            checkOrigin(requestContext, origin);
        }
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString(CorsHeaders.ORIGIN);
        String headers = requestContext.getHeaderString("Access-Control-Request-Headers");
        String methods = requestContext.getHeaderString("Access-Control-Request-Method");

        responseContext.getHeaders().putSingle(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin == null ? "*" : origin);
        responseContext.getHeaders().putSingle(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, methods == null ? "*" : methods);
        responseContext.getHeaders().putSingle(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, headers == null ? "*" : headers);
        if (origin == null) {
            // don't do anything if origin is null, its an OPTIONS request, or cors.failure is set
            return;
        }

        if (allowCredentials) {
            responseContext.getHeaders().putSingle(CorsHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }

        if (exposedHeaders != null) {
            responseContext.getHeaders().putSingle(CorsHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, exposedHeaders);
        }
    }

    protected void preflight(String origin, ContainerRequestContext requestContext) {
        checkOrigin(requestContext, origin);

        Response.ResponseBuilder builder = Response.ok();

        builder.header(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        String requestMethods = requestContext.getHeaderString(CorsHeaders.ACCESS_CONTROL_REQUEST_METHOD);
        if (requestMethods != null) {
            if (allowedMethods != null) {
                requestMethods = this.allowedMethods;
            }
            builder.header(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, requestMethods);
        }
        String allowHeaders = requestContext.getHeaderString(CorsHeaders.ACCESS_CONTROL_REQUEST_HEADERS);
        if (allowHeaders != null) {
            if (allowedHeaders != null) {
                allowHeaders = this.allowedHeaders;
            }
            builder.header(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
        }
        if (corsMaxAge > -1) {
            builder.header(CorsHeaders.ACCESS_CONTROL_MAX_AGE, corsMaxAge);
        }
        requestContext.abortWith(builder.build());
    }

    protected void checkOrigin(ContainerRequestContext requestContext, String origin) {
        if (!allowedOrigins.contains("*") && !allowedOrigins.contains(origin)) {
            requestContext.setProperty("cors.failure", true);
            throw new ForbiddenException("Origin is not allowed by Access-Control-Allow-Origin");
        }
    }

}
