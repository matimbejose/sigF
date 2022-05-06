package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cst;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CstFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CstService;
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
public class CstControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CstService cstService;

    private Cst cstSelecionado;

    private LazyDataModel<Cst> model;

    private CstFiltro filtro = new CstFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, cstService::quantidadeRegistrosFiltrados, cstService::getListaFiltrada);
    }

    public List<Cst> getCsts() {
        return cstService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return cstService.getDadosAuditoriaByID(cstSelecionado);
    }

}
