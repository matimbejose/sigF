package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.EmpresaUsuarioAcesso;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroGeral;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.EmpresaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroGeralService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
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
public class EmpresaCredenciadaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private LoginAcessoService service;

    @EJB
    private ParametroGeralService parametroGeralService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ModuloService moduloService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @Inject
    private TelaInicialControle telaInicialControle;

    private Empresa empresaSelecionada;

    private PagamentoMensalidade pagamentoMensalidadeSelecionado;

    private ParametroGeral parametroGeral;

    private LazyDataModel<Empresa> model;

    private EmpresaFiltro filtro = new EmpresaFiltro();

    private List<Modulo> modulos;

    private List<Modulo> selection;

    private String criaUsuarioAoCadastrarCliente;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, empresaService::quantidadeRegistrosFiltradosEmpresaCredenciada, empresaService::getListaFiltradaEmpresaCredenciada);
    }

    public List<EmpresaUsuarioAcesso> listarHistorico() {
        return empresaService.listarEmpresaUsuarioAcesso(empresaSelecionada);
    }

    public String doAcessar() {
        service.alteraTenatEmpresaCredenciada(empresaSelecionada.getTenatID());
        telaInicialControle.obterValoresParaGrafico();
        return "/restrito/index.xhtml";

    }

    public List<Object> getDadosAuditoria() {
        return empresaService.getDadosAuditoriaByID(empresaSelecionada);
    }

    public String doMostrarDetalhes() {
        criaUsuarioAoCadastrarCliente = parametroSistemaService.getParametroPorEmpresa(empresaSelecionada).getCriarUsuarioParaClienteCadastrado();
        return "detalhesEmpresaCredenciamento.xhtml";
    }

    public String doShowHistorico() {
        return "listaHistoricoEmpresaCredenciada.xhtml";
    }

    public Integer diasRestantes(Empresa empresa) {
        PagamentoMensalidade pm = pagamentoMensalidadeService.getUltimoPagamentoPor(empresa);
        int dias = 0;
        if (pm != null) {
            dias = DataUtil.diferencaEntreDias(new Date(), pm.getDataValidade());
        }
        return dias <= 0 ? 0 : dias;
    }

    public ParametroGeral getParametroGeral() {
        if (parametroGeral == null) {
            return parametroGeralService.getParametro();
        }
        return parametroGeral;
    }

    public String doArquivar() {
        empresaSelecionada.setAtivo("N");
        empresaService.salvar(empresaSelecionada);
        return "listaEmpresaCredenciada.xhtml";
    }

    public void atualizaAcesso() {
        empresaService.salvar(empresaSelecionada);
    }

    public void atualizaConfiguracaoCriaUsuario() {
        empresaService.atualizaConfiguracaoCriaUsuario(empresaSelecionada, criaUsuarioAoCadastrarCliente);
    }

    public String doManagePayment() {
        pagamentoMensalidadeSelecionado = pagamentoMensalidadeService.getUltimoPagamentoPor(empresaSelecionada);
        if (!pagamentoMensalidadeSelecionado.isDentroDaVigenciaDoContrato()) {
            String tenantID = pagamentoMensalidadeSelecionado.getIdEmpresa().getTenatID();
            pagamentoMensalidadeSelecionado.getIdUsuarioGeracao().setTenat(tenantID);

            pagamentoMensalidadeSelecionado = pagamentoMensalidadeService.gerarPagamentoMensalidade(
                    pagamentoMensalidadeSelecionado.getPagamentoMensalidadeModuloList().stream()
                            .map(PagamentoMensalidadeModulo::getIdModulo)
                            .collect(Collectors.toList()),
                    pagamentoMensalidadeSelecionado.getTipo(),
                    pagamentoMensalidadeSelecionado.getIdUsuarioGeracao());
            pagamentoMensalidadeSelecionado.getIdUsuarioGeracao().setTenat(tenantID);
            pagamentoMensalidadeSelecionado.setTenatID(tenantID);
        }
        return "alterarPagamentoMensalidade.xhtml";
    }

    public String atualizarPagamentoMensalidade() {
        try {
            String tipoConta = empresaSelecionada.getTipoConta();
            if (pagamentoMensalidadeSelecionado.getDataPagamento() != null && (pagamentoMensalidadeSelecionado.getIdUsuarioPagamento() == null || pagamentoMensalidadeSelecionado.getValorPago() == null || pagamentoMensalidadeSelecionado.getValorPago() < 0.01d)) {
                createFacesErrorMessage("Ao selecionar uma data de pagamento é necessário informar o valor pago e o usuário que realizou o pagamento do sistema.");
                return "alterarPagamentoMensalidade.xhtml";
            }
            if ((tipoConta == null || tipoConta.equals("G") || tipoConta.equals("E")) && pagamentoMensalidadeSelecionado.getDataValidade().after(new Date())) {
                empresaSelecionada.setTipoConta("P");
            }
            String tenantID = empresaSelecionada.getTenatID();
            pagamentoMensalidadeSelecionado.setTenatID(tenantID);
            pagamentoMensalidadeSelecionado.getPagamentoMensalidadeModuloList().forEach(pmm -> {
                pmm.setTenatID(tenantID);
                pmm.getPagamentoMensalidadeModuloPermissaoList().forEach(pmmp -> pmmp.setTenatID(tenantID));
            });
            pagamentoMensalidadeService.salvar(pagamentoMensalidadeSelecionado);
            empresaService.salvar(empresaSelecionada);
            return "listaEmpresaCredenciada.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "alterarPagamentoMensalidade.xhtml";
        }
    }

    public String doManageModules() {
        modulos = moduloService.listar();
        selection = pagamentoMensalidadeService.getUltimoPagamentoPor(empresaSelecionada).getPagamentoMensalidadeModuloList().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .collect(Collectors.toList());
        selection.forEach(mod -> {
            mod.setAdquirido(true);
            modulos.stream()
                    .filter(m -> m.getId().equals(mod.getId()))
                    .findAny()
                    .ifPresent(m -> m.setAdquirido(true));
        });
        return "alterarModulosEmpresa.xhtml";
    }

    public String atualizarModulos() {
        List<Modulo> novos = selection.stream()
                .filter(mod -> !mod.isAdquirido())
                .collect(Collectors.toList());
        PagamentoMensalidade pm = pagamentoMensalidadeService.getUltimoPagamentoPor(empresaSelecionada);
        pagamentoMensalidadeService.salvar(pagamentoMensalidadeService.adicionarModulo(pm, novos));
        return "listaEmpresaCredenciada";
    }

    public Double getMensalidadeTotal() {
        return selection.stream()
                .filter(Modulo::isAdquirido)
                .mapToDouble(Modulo::getValorMensalidade)
                .sum();
    }

}
