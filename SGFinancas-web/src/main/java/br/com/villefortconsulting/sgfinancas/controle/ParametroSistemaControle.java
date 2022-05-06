package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.ProdutoCategoria;
import br.com.villefortconsulting.sgfinancas.entidades.RecorrenciaAgendamento;
import br.com.villefortconsulting.sgfinancas.entidades.TipoProdutoUso;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ParametroSistemaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.NFSService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroNotificacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.ProdutoService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusOrdemDeServico;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNotificacao;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoProdutoVenda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsoProduto;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUsuario;
import br.com.villefortconsulting.util.ListUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
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
public class ParametroSistemaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private NFSService nfsService;

    @EJB
    private ParametroSistemaService service;

    @EJB
    private ParametroNotificacaoService parametroNotificacaoService;

    @EJB
    private ProdutoService produtoService;

    @EJB
    private UsuarioService usuarioService;

    private List<String> emails;

    private List<String> notificacaoSmsNumeros;

    private ParametroSistema parametroSistemaSelecionado;

    private LazyDataModel<ParametroSistema> model;

    private ParametroSistemaFiltro filtro = new ParametroSistemaFiltro();

    private List<TipoProdutoUso> listTipoProdutoUsoVenda;

    private List<TipoProdutoUso> listTipoProdutoUsoCompra;

    private List<Usuario> usuarios;

    private Usuario usuarioSelecionado;

    private List<Usuario> usuariosNotificacao;

    private Usuario usuarioSelecionadoNotificacao;

    private Integer activeIndex;

    public static final String PATH_CADASTRO = "/restrito/parametroSistema/cadastroParametroSistema.xhtml";

    public ParametroSistema getParametroSistemaSelecionado() {
        String tenatEmpresa = empresaService.getEmpresa().getTenatID();
        String tenatParametro = parametroSistemaSelecionado == null ? "" : parametroSistemaSelecionado.getTenatID();
        if (!tenatEmpresa.equals(tenatParametro)) {
            postConstruct();
        }
        return parametroSistemaSelecionado;
    }

    @PostConstruct
    public void postConstruct() {
        parametroSistemaSelecionado = service.getParametro();
        if (parametroSistemaSelecionado == null) {
            parametroSistemaSelecionado = new ParametroSistema();
        }
        listTipoProdutoUsoVenda = parametroSistemaSelecionado.getTipoProdutoVendaList();
        listTipoProdutoUsoCompra = parametroSistemaSelecionado.getTipoProdutoCompraList();
        emails = parametroNotificacaoService.listarEmails(EnumTipoNotificacao.CONTAS_PENDENTES.getTipo());
        if(parametroSistemaSelecionado.getNotificacaoSmsNumeros() != null && !parametroSistemaSelecionado.getNotificacaoSmsNumeros().trim().isEmpty()) {
            notificacaoSmsNumeros = Arrays.asList(parametroSistemaSelecionado.getNotificacaoSmsNumeros().split(";"));
        }
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
        usuarios = usuarioService.getUsuariosEmpresaLogada();
        usuarios.removeIf(user -> user.getIdPerfil().getEhSuporte());
        usuariosNotificacao = usuarioService.getUsuariosEmpresaLogada();
        usuariosNotificacao.removeIf(user -> user.getEmail() == null);
        usuariosNotificacao.removeIf(user -> user.getIdPerfil().getEhSuporte());
    }

    public String doFinishAdd() {
        try {
            List<TipoProdutoUso> aux = new ArrayList<>();
            aux.addAll(listTipoProdutoUsoVenda);
            aux.addAll(listTipoProdutoUsoCompra);
            ListUtil.persist(parametroSistemaSelecionado.getTipoProdutoUsoList(), aux, TipoProdutoUso::contains);
            parametroSistemaSelecionado.getTipoProdutoUsoList().forEach(tpu -> tpu.setTenatID(parametroSistemaSelecionado.getTenatID()));
            parametroSistemaSelecionado.getRecorrenciaAgendamentoList().forEach(ra -> ra.setTenatID(parametroSistemaSelecionado.getTenatID()));
            boolean recorrenciaValida = parametroSistemaSelecionado.getRecorrenciaAgendamentoList().stream()
                    .allMatch(ra -> "S".equals(ra.getEnviaEmail()) || "S".equals(ra.getEnviaSms()));
            if(notificacaoSmsNumeros != null && !notificacaoSmsNumeros.isEmpty()){
                parametroSistemaSelecionado.setNotificacaoSmsNumeros(notificacaoSmsNumeros.stream().reduce("", (a, v) -> a + ";" + v).substring(1));
            }
            service.salvar(parametroSistemaSelecionado);
            if (!recorrenciaValida) {
                throw new CadastroException("Informe se o prazo para o alerta de recorrência pode enviar email ou sms.", null);
            }
            parametroNotificacaoService.atualizarEmailsNotificacao(emails, EnumTipoNotificacao.CONTAS_PENDENTES.getTipo());
            createFacesSuccessMessage("Parâmetros do sistema salvo com sucesso!");

            if (Boolean.TRUE.equals(parametroSistemaSelecionado.getNeedToUpdateNFCeInfo())) {
                parametroSistemaSelecionado.setNeedToUpdateNFCeInfo(false);
                if (service.salvarCredenciaisNFCe(parametroSistemaSelecionado)) {
                    createFacesInfoMessage("Configuração da NFC-e salva com sucesso no MS");
                } else {
                    createFacesErrorMessage("Não foi possível salvar a configuração de NFC-e no MS");
                }
            }
            nfsService.atualizaMicrosservico();
            usuarios.stream().map(usuarioService::salvar).forEach(user -> {
                usuariosNotificacao.stream()
                        .filter(ax -> ax.getId().equals(user.getId()))
                        .findAny()
                        .orElseGet(Usuario::new)
                        .setPodeMudarPrecoUnitarioVenda(user.getPodeMudarPrecoUnitarioVenda());
            });
            usuariosNotificacao.forEach(usuarioService::salvar);
        } catch (CadastroException | IllegalAccessException | InvocationTargetException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return PATH_CADASTRO;
    }

    public String doCancelAdd() {
        postConstruct();
        createFacesInfoMessage("Alterações descartadas.");
        return PATH_CADASTRO;
    }

    public String doRegisterForNFe() {
        final String erroHabilitacao = "Não foi possível habilitar o envio. Tente novamente mais tarde ou entre em contato com o suporte.";
        try {
            String token = service.registrarParaEnvioDeNfe(parametroSistemaSelecionado);
            if (token != null) {
                parametroSistemaSelecionado.setNfeMsToken(token);
                service.salvar(parametroSistemaSelecionado);

                createFacesInfoMessage("Envio habilidado!");
            } else {
                createFacesErrorMessage(erroHabilitacao);
            }
        } catch (Exception ex) {
            createFacesErrorMessage(erroHabilitacao + " (" + ex.getClass().getSimpleName() + ")");
        }
        return PATH_CADASTRO;
    }

    public boolean temParametroEmpresa() {
        return service.temParametroEmpresa(empresaService.getEmpresa());
    }

    public List<ProdutoCategoria> getCategorias() {
        return produtoService.listarCategoria();
    }

    public String doShowAuditoria() {
        return "listaAuditoriaParametroSistema.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(parametroSistemaSelecionado);
    }

    public String doStartAlterarInf() {
        postConstruct();
        return PATH_CADASTRO;
    }

    public EnumStatusOrdemDeServico[] getListStatusOS() {
        return EnumStatusOrdemDeServico.values();
    }

    public String getStatusOSDesc(String cod) {
        return EnumStatusOrdemDeServico.retornaEnumSelecionado(cod).getDescricao();
    }

    public void validaQuantidadeDeParametros() {
        if (model.getRowCount() == 1) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(PATH_CADASTRO);
            } catch (IOException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<TipoProdutoUso> getSourceTipoProdutoUsoVenda() {
        return Arrays.asList(EnumTipoProdutoVenda.values()).stream()
                .map(EnumTipoProdutoVenda::getTipo)
                .map(tipo -> new TipoProdutoUso(EnumTipoUsoProduto.VENDA.getTipo(), tipo, parametroSistemaSelecionado))
                .collect(Collectors.toList());
    }

    public List<TipoProdutoUso> getSourceTipoProdutoUsoCompra() {
        return Arrays.asList(EnumTipoProdutoVenda.values()).stream()
                .map(EnumTipoProdutoVenda::getTipo)
                .map(tipo -> new TipoProdutoUso(EnumTipoUsoProduto.COMPRA.getTipo(), tipo, parametroSistemaSelecionado))
                .collect(Collectors.toList());
    }

    public void addRecorrencia() {
        parametroSistemaSelecionado.getRecorrenciaAgendamentoList().add(new RecorrenciaAgendamento(parametroSistemaSelecionado));
    }

    public void removeRecorrencia(RecorrenciaAgendamento ra) {
        parametroSistemaSelecionado.getRecorrenciaAgendamentoList().remove(ra);
    }

    public List<Usuario> getUsuarioList() {
        return usuarios.stream()
                .filter(user -> "N".equals(user.getPodeMudarPrecoUnitarioVenda())
                && (!user.getIdPerfil().getTipo().equals(EnumTipoUsuario.MASTER_USUARIO.getTipo())
                || !user.getIdPerfil().getTipo().equals(EnumTipoUsuario.ADMINISTRADOR.getTipo())))
                .collect(Collectors.toList());
    }

    public List<Usuario> getUsuariosSelecionados() {
        return usuarios.stream()
                .filter(user -> "S".equals(user.getPodeMudarPrecoUnitarioVenda())
                && (!user.getIdPerfil().getTipo().equals(EnumTipoUsuario.MASTER_USUARIO.getTipo()) || !user.getIdPerfil().getTipo().equals(EnumTipoUsuario.ADMINISTRADOR.getTipo())))
                .collect(Collectors.toList());
    }

    public void doAceitarTodasUsuario() {
        usuarios.forEach(user -> user.setPodeMudarPrecoUnitarioVenda("S"));
    }

    public void doAdicionaUsuario() {
        usuarioSelecionado.setPodeMudarPrecoUnitarioVenda("S");
    }

    public void doRemoverTodosUsuarios() {
        usuarios.forEach(user -> user.setPodeMudarPrecoUnitarioVenda("N"));
    }

    public void doRemoverUsuario() {
        usuarioSelecionado.setPodeMudarPrecoUnitarioVenda("N");
    }

    public List<Usuario> getUsuarioNotificacaoList() {
        return usuariosNotificacao.stream()
                .filter(user -> "N".equals(user.getReceberEmailIUGU()))
                .collect(Collectors.toList());
    }

    public List<Usuario> getUsuariosSelecionadosNotificacao() {
        return usuariosNotificacao.stream()
                .filter(user -> "S".equals(user.getReceberEmailIUGU()))
                .collect(Collectors.toList());
    }

    public void doAceitarTodasUsuarioNotificacao() {
        usuariosNotificacao.forEach(user -> user.setReceberEmailIUGU("S"));
    }

    public void doAdicionaUsuarioNotificacao() {
        usuarioSelecionadoNotificacao.setReceberEmailIUGU("S");
    }

    public void doRemoverTodosUsuariosNotificacao() {
        usuariosNotificacao.forEach(user -> user.setReceberEmailIUGU("N"));
    }

    public void doRemoverUsuarioNotificacao() {
        usuarioSelecionadoNotificacao.setReceberEmailIUGU("N");
    }

}
