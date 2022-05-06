/*
 * iDataSoft - Todos os direitos reservados
 */
package br.com.villefortconsulting.sgfinancas.controle.apoio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Christopher
 */
@Named
@SessionScoped
public class TrataData implements Serializable {

    private static final long serialVersionUID = 1L;

    public String formataData(Date data) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(data);
    }

}
