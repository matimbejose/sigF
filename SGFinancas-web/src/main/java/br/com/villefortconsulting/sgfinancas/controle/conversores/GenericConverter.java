package br.com.villefortconsulting.sgfinancas.controle.conversores;

import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.servicos.GenericService;
import br.com.villefortconsulting.util.AESUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Named;

@Named
@RequestScoped
public class GenericConverter implements Converter {

    @EJB
    private GenericService service;

    private static final String SECRET_KEY = "cJe65pucIZOXKm18";

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String valor) {
        if (valor == null || valor.isEmpty()) {
            return null;
        }
        String enumName = (String) uic.getAttributes().getOrDefault("className", null);
        try {
            String texto = AESUtil.decrypt(valor, SECRET_KEY);
            int pos = texto.indexOf(':');
            String className = texto.substring(0, pos);
            String val = texto.substring(pos + 1);

            if (className.equals("String")) {
                return val;
            } else if (isEnum(className)) {
                return Enum.valueOf(getEnumClass(className), val);
            } else if (enumName != null && isEnum(enumName)) {
                return Enum.valueOf(getEnumClass(enumName), val);
            }
            return service.buscar(className, Integer.parseInt(val));
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        if (o == null) {
            return "";
        }
        try {
            String serialized;
            if (o instanceof String) {
                serialized = "String:" + o;
            } else if (o instanceof EntityId) {
                serialized = o.getClass().getSimpleName() + ":" + ((EntityId) o).getId();
            } else {
                serialized = o.getClass().getName() + ":" + o.toString();
            }
            return AESUtil.encrypt(serialized, SECRET_KEY);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return "";
        }
    }

    private static boolean isEnum(String className) {
        try {
            return Class.forName(className).isEnum();
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private static Class<Enum> getEnumClass(String className) {
        try {
            return (Class<Enum>) Class.forName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

}
