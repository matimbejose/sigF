package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.ParametroCondominio;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ParametroCondominioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroCondominioService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
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
public class ParametroCondominioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ParametroCondominioService service;

    @EJB
    private ProdutoService produtoService;

    private ParametroCondominio parametroCondominioSelecionado;

    private LazyDataModel<ParametroCondominio> model;

    private ParametroCondominioFiltro filtro = new ParametroCondominioFiltro();

    @PostConstruct
    public void postConstruct() {
        parametroCondominioSelecionado = service.getParametroCondominio();

        if (parametroCondominioSelecionado == null) {
            parametroCondominioSelecionado = new ParametroCondominio();
        }
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String doStartAdd() {
        parametroCondominioSelecionado = new ParametroCondominio();
        return "cadastroParametroCondominio.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroParametroCondominio.xhtml";
    }

    public String doFinishAdd() {
        try {
            service.salvar(parametroCondominioSelecionado);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
        }
        return "cadastroParametroCondominio.xhtml";
    }

    public List<ProdutoCategoria> getCategorias() {
        return produtoService.listarCategoria();
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(parametroCondominioSelecionado);
    }

}
