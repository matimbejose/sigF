package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.servicos.ParametroGeralService;
import br.com.villefortconsulting.util.fitpag.FitPagUtil;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FitPagControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParametroGeralService parametroGeralService;

    public String getSession() {
        if (!"fitpag".equals(parametroGeralService.getParametro().getMeioPagamentoPadrao())) {
            return "";
        }
        return FitPagUtil.createSession();
    }

}
