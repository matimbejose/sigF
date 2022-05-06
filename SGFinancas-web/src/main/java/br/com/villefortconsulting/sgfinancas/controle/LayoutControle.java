package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Layout;
import br.com.villefortconsulting.sgfinancas.servicos.LayoutService;
import java.io.Serializable;
import java.util.List;
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
public class LayoutControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LayoutService layoutService;

    public List<Layout> getLayouts() {
        return layoutService.getLayouts();
    }

    public List<Layout> getLayoutsExportacao() {
        return layoutService.getLayoutsExportacao();
    }

}
