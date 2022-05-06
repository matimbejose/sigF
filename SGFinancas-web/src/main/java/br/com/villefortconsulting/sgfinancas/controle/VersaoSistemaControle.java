package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.VersaoSistema;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.VersaoSistemaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.VersaoSistemaService;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
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
public class VersaoSistemaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private VersaoSistemaService versaoSistemaService;

    private VersaoSistema versaoSistemaSelecionado;

    private LazyDataModel<VersaoSistema> model;

    private VersaoSistemaFiltro filtro = new VersaoSistemaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, versaoSistemaService);
    }

    public String doStartAdd() {
        cleanCache();
        versaoSistemaSelecionado = new VersaoSistema();
        versaoSistemaSelecionado.setDataInclusao(new Date());
        return "/restrito/administracao/versaoSistema/cadastroVersaoSistema.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/administracao/versaoSistema/cadastroVersaoSistema.xhtml";
    }

    public String doFinishAdd() {
        try {
            versaoSistemaService.salvar(versaoSistemaSelecionado);

            createFacesSuccessMessage("Versão do sistema cadastrada com sucesso!");
            return "/restrito/administracao/versaoSistema/listaVersaoSistema.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/administracao/versaoSistema/cadastroVersaoSistema.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/administracao/versaoSistema/listaAuditoriaVersaoSistema.xhtml";
    }

    public String doFinishExcluir() {
        versaoSistemaService.remover(versaoSistemaSelecionado);
        createFacesSuccessMessage("Versão do sistema removida com sucesso.");
        return "/restrito/administracao/versaoSistema/listaVersaoSistema.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return versaoSistemaService.getDadosAuditoriaByID(versaoSistemaSelecionado);
    }

}
