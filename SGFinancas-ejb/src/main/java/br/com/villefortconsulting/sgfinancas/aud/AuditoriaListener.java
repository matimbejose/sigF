/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.villefortconsulting.sgfinancas.aud;

import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.util.DataUtil;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 *
 * @author Christopher
 */
public class AuditoriaListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {

        CustomRevisionEntity entity = (CustomRevisionEntity) revisionEntity;

        // nos casos em que o usuario nao esta autenticado
        if (SecurityContextHolder.getContext().getAuthentication() != null) {

            // Data
            entity.setDataAtualizacao(DataUtil.getHoje());

            // Usuario
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Usuario) {
                Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                entity.setUsuario(usuarioLogado.getNome());
            } else {
                entity.setUsuario("Anômino");
            }

            // IP
            WebAuthenticationDetails details = (WebAuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
            if (details != null) {
                entity.setIp(details.getRemoteAddress());
            } else {
                entity.setIp("localhost");
            }
        } else {
            // Data

            entity.setDataAtualizacao(DataUtil.getHoje());

            // Usuario
            entity.setUsuario("Anômino");

            //IP
            FacesContext context = FacesContext.getCurrentInstance();
            if (context != null) {
                HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
                String ipAddress = request.getHeader("X-FORWARDED-FOR");
                if (ipAddress == null) {
                    ipAddress = request.getRemoteAddr();
                    entity.setIp(ipAddress);
                } else {
                    entity.setIp("localHost");
                }
            } else {
                entity.setIp("localHost");
            }
        }
    }

}
