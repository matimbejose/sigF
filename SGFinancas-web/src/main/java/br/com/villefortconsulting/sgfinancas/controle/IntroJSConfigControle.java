package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.IntroJSConfig;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.IntroJSConfigFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.IntroJSConfigService;
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
@Getter
@Setter
@SessionScoped
@NoArgsConstructor
@AllArgsConstructor
public class IntroJSConfigControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private IntroJSConfigService introJSConfigService;

    private IntroJSConfig introJSConfigSelecionado;

    private IntroJSConfigFiltro filtro = new IntroJSConfigFiltro();

    private LazyDataModel<IntroJSConfig> model;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, introJSConfigService);
    }

    public String getIntroJSFor(String pageId) {
        return introJSConfigService.findByPageId(pageId).getScript();
    }

    public String doStartAdd() {
        introJSConfigSelecionado = new IntroJSConfig();
        introJSConfigSelecionado.setForcaExibicao("N");
        introJSConfigSelecionado.setRevisao(0);
        return "cadastroIntroJSConfig.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "cadastroIntroJSConfig.xhtml";
    }

    public String doStartRemover() {
        introJSConfigService.remover(introJSConfigSelecionado);
        return "listaIntroJSConfig.xhtml";
    }

    public String doFinishAdd() {
        try {
            introJSConfigService.salvar(introJSConfigSelecionado);

            createFacesSuccessMessage("Configuração salva com sucesso!");
            return "listaIntroJSConfig.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "cadastroIntroJSConfig.xhtml";
        }
    }

    public List<Object> getDadosAuditoria() {
        return introJSConfigService.getDadosAuditoriaByID(introJSConfigSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaIntroJSConfig.xhtml";
    }

}
