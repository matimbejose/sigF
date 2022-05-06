package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.controle.apoio.ControleMenu;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.LoginAcessoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.util.EnumPorteEmpresa;
import br.com.villefortconsulting.util.VillefortDataModel;
import br.com.villefortconsulting.util.operator.In;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
public class EmpresaSelecaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LoginAcessoService service;

    private EmpresaAcessoDTO prefeituraAcessoDTOSelecionada;

    private Boolean empresaSelecionada;

    private LazyDataModel<Empresa> model;

    private LoginAcessoFiltro filtro = new LoginAcessoFiltro();

    private Empresa empresa;

    @Inject
    private TelaInicialControle telaInicialControle;

    @Inject
    private ControleMenu controleMenu;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                service::quantidadeRegistrosFiltrados,
                service::getListaFiltradaEmpresa,
                filter -> {
                    if (filter.getPropriedadeOrdenacao() == null) {
                        filter.addPropriedadeOrdenacao("descricao");
                    }
                    filter.setUsuario(getUsuarioLogado());
                    filter.setAtivo("S");
                    filter.setTipoEmpresa(In.fromList(Arrays.asList("CR", "EM")));
                });
    }

    public EmpresaSelecaoControle() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        empresaSelecionada = !(usuario.getQtdEmpresas() != null && usuario.getQtdEmpresas() > 1);
    }

    public List<EmpresaAcessoDTO> getEmpresasPorLogin() {
        return service.getEmpresasUsuarioLogado();
    }

    public String doStartAlterar() {
        this.empresaSelecionada = true;
        service.alteraTenat(empresa.getTenatID());
        telaInicialControle.obterValoresParaGrafico();
        controleMenu.resetCache();

        preencheUsuarioEmpresaAcesso();
        return "/restrito/index.xhtml";
    }

    public void preencheUsuarioEmpresaAcesso() {
        String ip = service.ipUsuario();
        service.adicionaEmpresaUsuarioAcesso(getUsuarioLogado(), empresa, ip, false);
    }

    public String preenchePorte(String sigla) {
        return EnumPorteEmpresa.getDescricao(sigla);
    }

}
