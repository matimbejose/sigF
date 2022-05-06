package br.com.villefortconsulting.sgfinancas.persistencia;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class TenatProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    private String tenantID;

    public TenatProvider() {
        super();
    }

    public void definirTenantID(final String tenantID) {
        if (tenantID == null) {
            return;
        }
        this.tenantID = tenantID;
    }

    public String getTenantID() {
        return tenantID;
    }

    public void setTenantID(String tenantID) {
        this.tenantID = tenantID;
    }

}
