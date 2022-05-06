package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.CondicaoPagamento;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.CondicaoPagamentoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CondicaoPagamentoService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Arrays;
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
public class CondicaoPagamentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    private CondicaoPagamento condicaoPgtoSelecionado;

    private static final String MSG_SUCESSO = "Condição de Pagamento salva com sucesso!";

    @EJB
    private CondicaoPagamentoService service;

    private LazyDataModel<CondicaoPagamento> model;

    private CondicaoPagamentoFiltro filtro = new CondicaoPagamentoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String doStartAdd() {
        setCondicaoPgtoSelecionado(new CondicaoPagamento());
        return "/restrito/condicaoPagamento/cadastroCondicaoPagamento.xhtml";
    }

    public String doStartAlterar() {
        setCondicaoPgtoSelecionado(service.buscar(getCondicaoPgtoSelecionado().getId()));
        return "/restrito/condicaoPagamento/cadastroCondicaoPagamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            service.salvar(getCondicaoPgtoSelecionado());
            createFacesSuccessMessage(MSG_SUCESSO);
            return "/restrito/condicaoPagamento/listaCondicaoPagamento.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/contaBancaria/cadastroCondicaoPagamento.xhtml";
        }
    }

    public String doFinishExcluir() {
        return "/restrito/condicaoPagamento/listaCondicaoPagamento.xhtml";
    }

    public boolean isMostrarAjuda() {
        return false;
    }

    public List<Integer> getDiasMes() {
        return Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31);
    }

}
