package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Csosn;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CsosnFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CsosnService;
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
public class CsosnControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CsosnService csosnService;

    private Csosn csosnSelecionado;

    private LazyDataModel<Csosn> model;

    private CsosnFiltro filtro = new CsosnFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, csosnService::quantidadeRegistrosFiltrados, csosnService::getListaFiltrada);
    }

    public List<Csosn> getCsosns() {
        return csosnService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return csosnService.getDadosAuditoriaByID(csosnSelecionado);
    }

}
