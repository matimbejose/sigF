package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.UF;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UFFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.UFService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
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
public class UfControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UFService ufService;

    private LazyDataModel<UF> model;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(new UFFiltro(), ufService::quantidadeRegistrosFiltrados, ufService::getListaFiltrada);
    }

}
