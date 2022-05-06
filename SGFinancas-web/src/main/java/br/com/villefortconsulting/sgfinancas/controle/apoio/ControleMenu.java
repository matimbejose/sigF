package br.com.villefortconsulting.sgfinancas.controle.apoio;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.controle.BasicControl;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.ContratoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FornecedorService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.PlanoContaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.ServicoService;
import br.com.villefortconsulting.sgfinancas.servicos.nfe.NfeProdutoService;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumAmbienteEmissaoNF;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.springframework.security.core.context.SecurityContextHolder;

@Named
@SessionScoped
public class ControleMenu extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private NfeProdutoService nfeProdutoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContratoService contratoService;

    @EJB
    private PlanoContaService planoContaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private ServicoService servicoService;

    @EJB
    private FornecedorService fornecedorService;

    private HashMap<String, Boolean> cacheCadastro = new HashMap<>();

    public int obterQtdNotificacao() {
        if (empresaService.getEmpresa() == null) {
            return 0;
        }
        int qtd = 0;

        if (precisaAtualizarDados()) {
            qtd++;
        }

        if (!existeContaCorrenteCadastrada()) {
            qtd++;
        }

        if (!existeCerticadoInformado()) {
            qtd++;
        }

        if (!dadosNotaFiscalPreenchidos()) {
            qtd++;
        }

        if (mostraEmpresaExpirada() > 0) {
            qtd++;
        }

        if (quatidadeDeContratoClienteVencendo() > 0) {
            qtd++;
        }

        if (quatidadeDeContratoFornecedorVencendo() > 0) {
            qtd++;
        }

        return qtd;
    }

    public Integer quatidadeDeContratoClienteVencendo() {
        if (isPerfilSuporte()) {
            Long qtd = contratoService.getQteContratosVencendo(EnumTipoContrato.CLIENTE);

            if (qtd != null) {
                return qtd.intValue();
            }
        }
        return 0;
    }

    public Integer quatidadeDeContratoFornecedorVencendo() {
        if (isPerfilSuporte()) {
            Long qtd = contratoService.getQteContratosVencendo(EnumTipoContrato.FORNECEDOR);

            if (qtd != null) {
                return qtd.intValue();
            }
        }
        return 0;
    }

    public Integer mostraEmpresaExpirada() {
        if (isPerfilSuporte()) {
            Long qtd = empresaService.getQtdEmpresasExpiradas();

            if (qtd != null) {
                return qtd.intValue();
            }
        }
        return 0;
    }

    public Integer mostraClienteContatoHoje() {
        if (isPerfilSuporte()) {
            Long qtd = empresaService.getQtdClientesContatoHoje();

            if (qtd != null) {
                return qtd.intValue();
            }
        }
        return 0;
    }

    public boolean perfilPodeVisualizarNotificacoes() {
        Perfil perfil = getUsuarioLogado().getIdPerfil();
        return perfil.getEhSuporte() || perfil.getEhUsuarioMaster();
    }

    public boolean precisaAtualizarDados() {
        return perfilPodeVisualizarNotificacoes() && empresaService.precisaAtualizarDados();
    }

    public boolean existeContaCorrenteCadastrada() {
        if (!perfilPodeVisualizarNotificacoes()) {
            return true;
        }
        return contaBancariaService.existeContaCorrenteInformada();
    }

    public boolean existeCerticadoInformado() {
        if (!perfilPodeVisualizarNotificacoes()) {
            return true;
        }
        Empresa empresa = empresaService.getEmpresa();
        if (empresa == null || !empresa.isPJ()) {
            return true;
        }
        return empresaService.existeCerticadoInformado();
    }

    public boolean dadosNotaFiscalPreenchidos() {
        if (!perfilPodeVisualizarNotificacoes()) {
            return true;
        }
        Empresa empresa = empresaService.getEmpresa();
        if (empresa == null) {
            return true;
        }
        if (!empresa.isPJ()) {
            return true;
        }
        return !parametroSistemaService.dadosNotaFiscalPreenchidos();
    }

    public boolean dadosImportacaoPreenchidos() {
        return perfilPodeVisualizarNotificacoes() && !parametroSistemaService.dadosImportacaoPreenchidos();
    }

    public String doAcionarPaginaNaoRestrita(String url) {
        return "/" + url + "?faces-redirect=true";
    }

    public String doAcionarPagina(String url) {
        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.isSenhaExpirada() || usuarioLogado.isAccountNonExpired()) {
            url = "/restrito/seguranca/alterarSenhaExpirada.xhtml?faces-redirect=true";
        } else {
            url = "/restrito/" + url + "?faces-redirect=true";
        }

        return url;
    }

    public String doAcionarListagem(String url) {
        Usuario usuarioLogado = getUsuarioLogado();

        if (usuarioLogado.isSenhaExpirada() || usuarioLogado.isAccountNonExpired()) {
            url = "/restrito/seguranca/alterarSenhaExpirada.xhtml?faces-redirect=true";
        } else if (!informacaoCompleta(url)) {
            url = "/restrito/" + url + "/informacao.xhtml?faces-redirect=true";
        } else if (url.contains(".xhtml")) {
            url = "/restrito/" + url + "?faces-redirect=true";
        } else {
            String entidade;
            if (url.contains("/")) {
                String[] partes = url.split("/");
                entidade = partes[partes.length - 1];
            } else {
                entidade = url;
            }
            url = "/restrito/" + url + "/lista" + entidade.substring(0, 1).toUpperCase() + entidade.substring(1) + ".xhtml?faces-redirect=true";
        }

        return url;
    }

    public boolean informacaoCompleta(String modulo) {
        if (Boolean.TRUE.equals(cacheCadastro.getOrDefault(modulo, Boolean.FALSE))) {
            return true;
        }
        Boolean ret;
        switch (modulo) {
            case "notaFiscalProduto":
                ret = nfeProdutoService.empresaAptaEmitirNF(isPerfilSuporte() ? EnumAmbienteEmissaoNF.HOMOLOGACAO.getTipo() : EnumAmbienteEmissaoNF.PRODUCAO.getTipo()).isTodosOK();
                break;
            case "orcamento":
            case "venda":
                boolean cadastrosOk = Arrays.asList("PC", "CL", "CA", "CB").stream()
                        .allMatch(this::isCadastroOk);
                boolean prodServOk = Arrays.asList("PT", "SE").stream()
                        .anyMatch(this::isCadastroOk);
                ret = cadastrosOk && prodServOk;
                break;
            case "compra":
                ret = Arrays.asList("PC", "FO", "CA", "CB").stream()
                        .allMatch(this::isCadastroOk);
                break;
            case "producao":
                ret = Arrays.asList("CA", "PC").stream()
                        .allMatch(this::isCadastroOk);
                break;
            default:
                ret = true;
        }
        cacheCadastro.put(modulo, ret);
        return ret;
    }

    public boolean isCadastroOk(String sigla) {
        if (Boolean.TRUE.equals(cacheCadastro.getOrDefault(sigla, Boolean.FALSE))) {
            return true;
        }
        Boolean ret;
        switch (sigla) {
            case "PC":
                ret = planoContaService.hasAny();
                break;
            case "CL":
                ret = clienteService.hasAny();
                break;
            case "FO":
                ret = fornecedorService.hasAny();
                break;
            case "CA":
                ret = produtoService.hasAnyProdutoCategoria();
                break;
            case "PT":
                ret = produtoService.hasAnyProduto();
                break;
            case "SE":
                ret = servicoService.hasAny();
                break;
            case "CB":
                ret = contaBancariaService.hasAny();
                break;
            default:
                ret = false;
        }
        cacheCadastro.put(sigla, ret);
        return ret;
    }

    public String doOpenGestao() {
        ((Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).setTenat(null);
        adHocTenant.setTenantID(null);
        return "/restrito/index.xhtml";
    }

    public void resetCache() {
        cacheCadastro.clear();
    }

}
