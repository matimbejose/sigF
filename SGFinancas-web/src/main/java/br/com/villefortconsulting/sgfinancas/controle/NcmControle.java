package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Ncm;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.NcmFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.NcmService;
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
import org.primefaces.model.TreeNode;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NcmControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private NcmService ncmService;

    private Ncm ncmSelecionado;

    private LazyDataModel<Ncm> model;

    private transient TreeNode root;

    private NcmFiltro filtro = new NcmFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, ncmService::quantidadeRegistrosFiltrados, ncmService::getListaFiltrada);
    }

    public List<Ncm> listarCapitulos() {
        return ncmService.listaCapitulos();
    }

    public List<Ncm> getNcms() {
        return ncmService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return ncmService.getDadosAuditoriaByID(ncmSelecionado);
    }

}
