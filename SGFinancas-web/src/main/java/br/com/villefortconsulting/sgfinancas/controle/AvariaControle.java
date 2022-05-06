package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Avaria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.AvariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.AvariaService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvariaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AvariaService avariaService;

    private Avaria avariaSelecionada;

    private LazyDataModel<Avaria> model;

    private AvariaFiltro filtro = new AvariaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, avariaService);
    }

    public List<Avaria> getAvarias() {
        return avariaService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        avariaSelecionada = new Avaria();
        avariaSelecionada.setAtivo("S");
        return "cadastroAvaria.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroAvaria.xhtml";
    }

    public String doFinishAdd() {
        try {
            avariaService.salvar(avariaSelecionada);

            createFacesSuccessMessage("Avaria salva com sucesso!");
            return "listaAvaria.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "cadastroAvaria.xhtml";
        }
    }

    public String doFinishExcluir() {
        avariaSelecionada.setAtivo("N");
        avariaService.salvar(avariaSelecionada);
        return "listaAvaria.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return avariaService.getDadosAuditoriaByID(avariaSelecionada);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAvaria.xhtml";
    }

}
