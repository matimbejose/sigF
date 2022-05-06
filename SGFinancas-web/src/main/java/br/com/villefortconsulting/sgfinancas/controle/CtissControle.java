package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Ctiss;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CtissFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CtissService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
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
public class CtissControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CtissService ctissService;

    private Ctiss ctissSelecionado;

    private LazyDataModel<Ctiss> model;

    private CtissFiltro filtro = new CtissFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, ctissService::quantidadeRegistrosFiltrados, ctissService::getListaFiltrada);
    }

    public List<Ctiss> getCtisss() {
        return ctissService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return ctissService.getDadosAuditoriaByID(ctissSelecionado);
    }

}
