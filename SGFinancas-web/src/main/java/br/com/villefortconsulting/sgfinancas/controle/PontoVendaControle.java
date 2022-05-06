package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.PontoVenda;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PontoVendaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.PontoVendaService;
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
public class PontoVendaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    private PontoVenda pontoVendaSelecionado;

    private static final String MSG_SUCESSO = "Ponto de Venda salvo com sucesso!";

    private static final String PATH_LISTA = "/restrito/pontoVenda/listaPontoVenda.xhtml";

    private static final String PATH_CADASTRO = "/restrito/pontoVenda/cadastroPontoVenda.xhtml";

    @EJB
    private PontoVendaService service;

    private LazyDataModel<PontoVenda> model;

    PontoVendaFiltro filtro = new PontoVendaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String doStartAdd() {
        setPontoVendaSelecionado(new PontoVenda());
        return PATH_CADASTRO;
    }

    public String doStartAlterar() {
        setPontoVendaSelecionado(service.buscar(getPontoVendaSelecionado().getId()));
        return PATH_CADASTRO;
    }

    public String doFinishAdd() {
        try {
            service.salvar(getPontoVendaSelecionado());
            createFacesSuccessMessage(MSG_SUCESSO);
            return PATH_LISTA;
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return PATH_CADASTRO;
        }
    }

    public String doFinishExcluir() {
        service.remover(getPontoVendaSelecionado());
        return PATH_LISTA;
    }

    public boolean isMostrarAjuda() {
        return false;
    }

}
