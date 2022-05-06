package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Permissao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PermissaoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.PermissaoService;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PermissaoService service;

    private Permissao permissaoSelecionada;

    @NotNull(message = "Você deve especificar a descrição")
    private String descricao;

    private PermissaoFiltro filtro = new PermissaoFiltro();

    public List<Permissao> getPermissoes() {
        return service.getPermissoes();
    }

    public String doStartAdd() {
        setPermissaoSelecionada(new Permissao());
        return "cadastroPermissao.xhtml";
    }

    public String doFinishAdd() {
        if (existsViolationsForJSF(permissaoSelecionada)) {
            return "cadastroPermissao.xhtml";
        }

        service.salvar(permissaoSelecionada);

        return "listaPermissao.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroPermissao.xhtml";
    }

    public String doFinishExcluir() {
        service.remover(permissaoSelecionada);
        return "listaPermissao.xhtml";
    }

}
