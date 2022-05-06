package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacoteCategoriaEmpresa;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacoteModulo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ModuloPacoteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloPacoteService;
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
public class ModuloPacoteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ModuloPacoteService moduloPacoteService;

    private ModuloPacote moduloPacoteSelecionado;

    private LazyDataModel<ModuloPacote> model;

    private ModuloPacoteFiltro filtro = new ModuloPacoteFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, moduloPacoteService);
    }

    public List<ModuloPacote> getModuloPacotes() {
        return moduloPacoteService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        setModuloPacoteSelecionado(new ModuloPacote());
        return "/restrito/administracao/moduloPacote/cadastroModuloPacote.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/administracao/moduloPacote/cadastroModuloPacote.xhtml";
    }

    public String doFinishAdd() {
        try {
            moduloPacoteService.salvar(moduloPacoteSelecionado);

            createFacesSuccessMessage("Pacote de módulos cadastrado com sucesso!");
            return "/restrito/administracao/moduloPacote/listaModuloPacote.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "/restrito/administracao/moduloPacote/cadastroModuloPacote.xhtml";
        }
    }

    public String doShowAuditoria() {
        return "/restrito/administracao/moduloPacote/listaAuditoriaModuloPacote.xhtml";
    }

    public String doFinishExcluir() {
        moduloPacoteSelecionado.setAtivo("N");
        moduloPacoteService.salvar(moduloPacoteSelecionado);
        createFacesSuccessMessage("Pacote de módulos desativado com sucesso.");
        return "/restrito/administracao/moduloPacote/listaModuloPacote.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return moduloPacoteService.getDadosAuditoriaByID(moduloPacoteSelecionado);
    }

    public void removerModulo(ModuloPacoteModulo mpm) {
        moduloPacoteSelecionado.getModuloPacoteModuloList().remove(mpm);
    }

    public void adicionarModulo() {
        moduloPacoteSelecionado.getModuloPacoteModuloList().add(new ModuloPacoteModulo(moduloPacoteSelecionado));
    }

    public void removerCategoria(ModuloPacoteCategoriaEmpresa mpce) {
        moduloPacoteSelecionado.getModuloPacoteCategoriaEmpresaList().remove(mpce);
    }

    public void adicionarCategoria() {
        moduloPacoteSelecionado.getModuloPacoteCategoriaEmpresaList().add(new ModuloPacoteCategoriaEmpresa(moduloPacoteSelecionado));
    }

}
