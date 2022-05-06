package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ClienteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.TransacaoGetnetService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
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
public class TransacaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private TransacaoGetnetService transacaoService;

    private LazyDataModel<Cliente> model;

    private ClienteFiltro filtro = new ClienteFiltro();

    private TransacaoGetnet transacaoSelecionada;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, transacaoService);
    }

    public String doStartAdd() {
        cleanCache();
        return "/restrito/pagamento/cadastroPagamento.xhtml";
    }

    public String doFinishAdd() {
        try {
            transacaoService.salvar(transacaoSelecionada);
            createFacesSuccessMessage("Cliente salvo com sucesso!");
            return "/restrito/pagamento/listaPagamento.xhtml";
        } catch (CadastroException e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/pagamento/cadastroPagamento.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/pagamento/listaAuditoriaPagamento.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return transacaoService.getDadosAuditoriaByID(transacaoSelecionada);
    }

}
