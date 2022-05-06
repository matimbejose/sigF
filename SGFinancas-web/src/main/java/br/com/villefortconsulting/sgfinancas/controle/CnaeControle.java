package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cnae;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CnaeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CnaeService;
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
public class CnaeControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private CnaeService cnaeService;

    private Cnae cnaeSelecionado;

    private LazyDataModel<Cnae> model;

    private CnaeFiltro filtro = new CnaeFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, cnaeService::quantidadeRegistrosFiltrados, cnaeService::getListaFiltrada);
    }

    public List<Cnae> getCnaes() {
        return cnaeService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return cnaeService.getDadosAuditoriaByID(cnaeSelecionado);
    }

}
