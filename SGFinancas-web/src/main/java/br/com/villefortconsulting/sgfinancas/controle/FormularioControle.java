package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Formulario;
import br.com.villefortconsulting.sgfinancas.entidades.ItemSecao;
import br.com.villefortconsulting.sgfinancas.entidades.Secao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.FormularioFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.FormularioRespostaService;
import br.com.villefortconsulting.sgfinancas.servicos.FormularioService;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
import org.primefaces.event.ReorderEvent;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormularioControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private FormularioService formularioService;

    @EJB
    private FormularioRespostaService formularioRespostaService;

    private Formulario formularioSelecionado;

    private Secao secaoSelecionada;

    private ItemSecao itemSecaoSelecionado;

    private LazyDataModel<Formulario> model;

    private FormularioFiltro filtro = new FormularioFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, formularioService);
    }

    @Override
    public void cleanCache() {
        secaoSelecionada = null;
        itemSecaoSelecionado = null;
    }

    public void cleanItemSelecionado() {
        itemSecaoSelecionado = null;
    }

    public List<Formulario> getFormularios() {
        return formularioService.listar();
    }

    public String doStartAdd() {
        cleanCache();
        formularioSelecionado = new Formulario();
        formularioSelecionado.setAtivo("S");
        return "cadastroFormulario.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        formularioSelecionado.getFormularioSecaoList().stream()
                .forEach(secao -> {
                    secao.setKey(StringUtil.gerarStringAleatoria(10));
                    secao.getSecaoItemSecaoList().stream()
                            .forEach(item -> {
                                item.setKey(StringUtil.gerarStringAleatoria(10));
                                item.getItemSecaoSubItemSecaoList().stream()
                                        .forEach(subItem -> subItem.setKey(StringUtil.gerarStringAleatoria(10)));
                            });
                });
        return "cadastroFormulario.xhtml";
    }

    public String doFinishAdd() {
        try {
            formularioService.salvar(formularioSelecionado);

            createFacesSuccessMessage("Formulario salva com sucesso!");
            return "listaFormulario.xhtml";
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            createFacesErrorMessage(ex.getMessage());
            return "cadastroFormulario.xhtml";
        }
    }

    public String doFinishExcluir() {
        formularioSelecionado.setAtivo("N");
        formularioService.salvar(formularioSelecionado);
        return "listaFormulario.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return formularioService.getDadosAuditoriaByID(formularioSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaFormulario.xhtml";
    }

    public void doStartAdicionarSecao() {
        formularioSelecionado.getFormularioSecaoList().add(new Secao(true, formularioSelecionado));
    }

    public void doStartAdicionarItemSecao() {
        secaoSelecionada.getSecaoItemSecaoList().add(new ItemSecao(true, secaoSelecionada));
    }

    public void doStartAdicionarSubItemSecao() {
        itemSecaoSelecionado.getItemSecaoSubItemSecaoList().add(new ItemSecao(true, null));
    }

    public void doStartRemoverSecao(Secao item) {
        if (secaoSelecionada.equals(item)) {
            secaoSelecionada = null;
        }
        formularioSelecionado.getFormularioSecaoList().remove(item);
    }

    public void doStartRemoverItemSecao(ItemSecao item) {
        if (itemSecaoSelecionado.equals(item)) {
            itemSecaoSelecionado = null;
        }
        secaoSelecionada.getSecaoItemSecaoList().remove(item);
    }

    public void doStartRemoverSubItemSecao(ItemSecao item) {
        itemSecaoSelecionado.getItemSecaoSubItemSecaoList().remove(item);
    }

    public String getEmptyItem() {
        if (secaoSelecionada == null) {
            return "Selecione uma seção.";
        }
        return "Nenhum item adicionado.";
    }

    public String getEmptySubItem() {
        if (itemSecaoSelecionado == null) {
            return "Selecione um item.";
        }
        return "Nenhum sub-item adicionado.";
    }

    public void onSecaoReorder(ReorderEvent event) {
        Collections.swap(formularioSelecionado.getFormularioSecaoList(), event.getFromIndex(), event.getToIndex());
        Secao a = formularioSelecionado.getFormularioSecaoList().get(event.getFromIndex());
        Secao b = formularioSelecionado.getFormularioSecaoList().get(event.getToIndex());

        List<ItemSecao> auxA = new ArrayList<>();
        auxA.addAll(a.getSecaoItemSecaoList());
        List<ItemSecao> auxB = new ArrayList<>();
        auxB.addAll(b.getSecaoItemSecaoList());

        a.getSecaoItemSecaoList().clear();
        b.getSecaoItemSecaoList().clear();

        a.getSecaoItemSecaoList().addAll(auxB);
        b.getSecaoItemSecaoList().addAll(auxA);

        a.getSecaoItemSecaoList().forEach(s -> s.setIdSecao(a));
        b.getSecaoItemSecaoList().forEach(s -> s.setIdSecao(b));
    }

    public void onItemReorder(ReorderEvent event) {
        Collections.swap(secaoSelecionada.getSecaoItemSecaoList(), event.getFromIndex(), event.getToIndex());
        ItemSecao a = secaoSelecionada.getSecaoItemSecaoList().get(event.getFromIndex());
        ItemSecao b = secaoSelecionada.getSecaoItemSecaoList().get(event.getToIndex());

        List<ItemSecao> auxA = new ArrayList<>();
        auxA.addAll(a.getItemSecaoSubItemSecaoList());
        List<ItemSecao> auxB = new ArrayList<>();
        auxB.addAll(b.getItemSecaoSubItemSecaoList());

        a.getItemSecaoSubItemSecaoList().clear();
        b.getItemSecaoSubItemSecaoList().clear();

        a.getItemSecaoSubItemSecaoList().addAll(auxB);
        b.getItemSecaoSubItemSecaoList().addAll(auxA);

        a.getItemSecaoSubItemSecaoList().forEach(s -> s.setIdItemSecao(a));
        b.getItemSecaoSubItemSecaoList().forEach(s -> s.setIdItemSecao(b));
    }

    public boolean podeEditar() {
        return podeEditar(formularioSelecionado);
    }

    public boolean podeEditar(Formulario formulario) {
        return formulario.getId() == null || formularioRespostaService.temRespostaParaO(formulario);
    }

}
