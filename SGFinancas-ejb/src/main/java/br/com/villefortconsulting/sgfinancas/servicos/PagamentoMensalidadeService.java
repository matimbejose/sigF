package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacoteModulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.TransacaoGetnet;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.SmsDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PagamentoMensalidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.PagamentoMensalidadeRepositorio;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamentoSistema;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.fitpag.FitPagUtil;
import br.com.villefortconsulting.util.fitpag.request.Payment;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.primefaces.PrimeFaces;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistryImpl;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PagamentoMensalidadeService extends BasicService<PagamentoMensalidade, PagamentoMensalidadeRepositorio, PagamentoMensalidadeFiltro> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private EmailService emailService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private FitPagService fitPagService;

    @EJB
    private ParametroGeralService parametroGeralService;

    @EJB
    private ModuloService moduloService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private PagamentoMensalidadeModuloService pagamentoMensalidadeModuloService;

    @EJB
    private PagamentoMensalidadeModuloPermissaoService pagamentoMensalidadeModuloPermissaoService;

    @EJB
    private SmsService smsService;

    @EJB
    private TransacaoGetnetService transacaoGetnetService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new PagamentoMensalidadeRepositorio(em, adHocTenant);
    }

    public List<PagamentoMensalidade> buscarPagamentos() {
        return repositorio.buscaPagamentosByEmpresa(empresaService.getEmpresa());
    }

    public PagamentoMensalidade getUltimoPagamentoPor(Empresa empresa) {
        return repositorio.getUltimoPagamentoPor(empresa);
    }

    public PagamentoMensalidade getUltimoPagamentoEmpresaLogada() {
        return repositorio.getUltimoPagamentoPor(empresaService.getEmpresa());
    }

    public PagamentoMensalidade gerarPagamentoMensalidade(List<Modulo> modulos, String tipoPagamento, Usuario user) {
        Empresa empresa;
        if (adHocTenant != null && adHocTenant.getTenantID() != null) {
            empresa = empresaService.getEmpresa();
        } else if (user.getTenat() != null) {
            empresa = empresaService.getEmpresPorTenatID(user.getTenat());
        } else {
            throw new IllegalStateException("TenantID não definido");
        }
        PagamentoMensalidade pm = new PagamentoMensalidade();
        PagamentoMensalidade ultimoPagamento = getUltimoPagamentoPor(empresa);
        switch (tipoPagamento) {
            case "RA":// renovação adiantada
                pm.setDataValidade(DataUtil.adicionarMesVerficandoUltimoDia(ultimoPagamento.getDataValidade(), 1));
                break;
            case "R":// renovação
                pm.setDataValidade(DataUtil.adicionarMesVerficandoUltimoDia(new Date(), 1));
                break;
            case "CM":// contratação de módulo
                pm.setDataValidade(ultimoPagamento.getDataValidade());
                break;
            default:
                pm.setDataValidade(DataUtil.adicionarMesVerficandoUltimoDia(ultimoPagamento.getDataValidade(), 1));
                break;
        }

        modulos.addAll(ultimoPagamento.getPagamentoMensalidadeModuloList().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .collect(Collectors.toList()));

        pm.setIdModuloPacote(ultimoPagamento.getIdModuloPacote());
        pm.setIdUsuarioGeracao(user);
        pm.setIdEmpresa(empresa);
        pm.setTipo(tipoPagamento);
        pm.setValorPago(0d);
        pm.setTenatID(pm.getIdEmpresa().getTenatID());
        pm.getPagamentoMensalidadeModuloList().addAll(modulos.stream()
                .map(modulo -> new PagamentoMensalidadeModulo(pm, modulo))
                .collect(Collectors.toList()));
        return pm;
    }

    public boolean tratarPagamentoMensalidade(PagamentoMensalidade pm, PagamentoSistemaDTO dadosPagamento) {
        if ("CC".equals(dadosPagamento.getTipoPagamento())) {
            pm.setDataPagamento(new Date());
            pm.setValorPago(dadosPagamento.getPreco());
            pm.setIdUsuarioPagamento(getUsuarioLogado());
            return true;
        } else if (pm.getIdTransacaoGetnet() != null) {
            Empresa empresa = empresaService.getEmpresa();
            smsService.send(new SmsDTO("Pagamento do SG Finanças.\\nCódigo de barras: " + pm.getIdTransacaoGetnet().getLinhaDigitavel(), empresa.getFone()));
            try {
                emailService.enviarEmailMS(EmailUtil.getEmailBoletoPagamentoSistema(pm.getIdTransacaoGetnet(), empresa), "S");
                PrimeFaces.current().executeScript("window.open(`/link/pagamento.xhtml?id=" + TransacaoGetnetService.getTransacaoId(pm.getIdTransacaoGetnet()) + "`, `_BLANK`);");
            } catch (EmailException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException ex) {
                return false;
            }
        }
        return false;
    }

    public boolean sendRequestPagamento(PagamentoMensalidade pm, PagamentoSistemaDTO dadosPagamento, List<Modulo> selectedModules, Double diasRestantes) throws MessageListException {
        List<Modulo> modulos = new ArrayList<>();
        modulos.addAll(selectedModules.stream()
                .distinct()
                .collect(Collectors.toList()));
        try {
            switch (parametroGeralService.getParametro().getMeioPagamentoPadrao()) {
                case "getnet":
                    TransacaoGetnet tg = transacaoGetnetService.adicionarEnviarTransacaoPendente(dadosPagamento);
                    pm.setIdTransacaoGetnet(tg);
                    return tratarPagamentoMensalidade(pm, dadosPagamento);
                case "fitpag":
                    Payment pagamento = fitPagService.createPayment(dadosPagamento, empresaService.getEmpresa(), modulos, diasRestantes);
                    pm.setReferenciaFitPag(pagamento.getReference());
                    FitPagUtil.sendTransaction(pagamento);
                    return true;
                default:
                    return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    private static Usuario getUsuarioLogado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public PagamentoMensalidade salvar(PagamentoMensalidade pm) {
        boolean jaFoiPago = false;
        boolean estaPago = pm.getDataPagamento() != null;
        final String tenant = pm.getIdEmpresa().getTenatID();
        pm.getPagamentoMensalidadeModuloList().forEach(pmm -> {
            pmm.setTenatID(tenant);
            if (pmm.getPagamentoMensalidadeModuloPermissaoList() != null) {
                pmm.getPagamentoMensalidadeModuloPermissaoList().forEach(pmmp -> pmmp.setTenatID(tenant));
            }
        });
        if (estaPago) {
            if (pm.getId() != null) {
                PagamentoMensalidade pmBanco = buscar(pm.getId());
                if (pmBanco.getDataPagamento() != null) {
                    jaFoiPago = true;
                }
            }
            if (!jaFoiPago) {
                Usuario usuario = pm.getIdUsuarioPagamento();
                if (usuario == null) {
                    usuario = getUsuarioLogado();
                    pm.setIdUsuarioPagamento(usuario);
                }
                pm = super.salvar(pm);
                adicionarModulo(pm, pm.getPagamentoMensalidadeModuloList().stream()
                        .map(PagamentoMensalidadeModulo::getIdModulo)
                        .collect(Collectors.toList()));
                Empresa empresa = empresaService.getEmpresPorTenatID(tenant);
                empresa.setTipoConta("P");
                empresaService.salvar(empresa);
                pagamentoMensalidadeModuloPermissaoService.atualizaPermissoes(empresa, true);
                new SessionRegistryImpl().getAllPrincipals().stream()
                        .filter(Usuario.class::isInstance)
                        .map(Usuario.class::cast)
                        .forEach(user -> user.setPrecisaAtualizarPermissao(true));
                SecurityContextHolder.getContext().setAuthentication(loginAcessoService.createAuthFor(usuario));
            }
        }
        return super.salvar(pm);
    }

    public PagamentoMensalidade adicionarModulo(PagamentoMensalidade pm, List<Modulo> modulos) {
        List<PagamentoMensalidadeModulo> pmmList = pagamentoMensalidadeModuloService.getModulosPor(pm);
        pmmList.addAll(modulos.stream()
                .map(modulo -> from(modulo, pm))
                .collect(Collectors.toList()));
        pm.getPagamentoMensalidadeModuloList().clear();
        pm.getPagamentoMensalidadeModuloList().addAll(pmmList.stream()
                .distinct()
                .collect(Collectors.toList()));
        return pm;
    }

    private PagamentoMensalidadeModulo from(Modulo m, PagamentoMensalidade pm) {
        PagamentoMensalidadeModulo pmm = new PagamentoMensalidadeModulo();
        pmm.setIdModulo(m);
        pmm.setIdPagamentoMensalidade(pm);
        pmm.setTenatID(pm.getIdEmpresa().getTenatID());
        pmm.setPagamentoMensalidadeModuloPermissaoList(pagamentoMensalidadeModuloPermissaoService.permissoesPor(pmm));
        return pmm;
    }

    public PagamentoMensalidade populaPagamentoMensalidade(Empresa empresa, ModuloPacote pacote, Usuario usuario) {
        PagamentoMensalidade pm = new PagamentoMensalidade();
        pm.setTipo(EnumTipoPagamentoSistema.CONTRATACAO.getCategoria());
        pm.setValorPago(0d);
        pm.setDataValidade(DataUtil.adicionarDias(new Date(), parametroGeralService.getParametro().getPrazoUsarSemComprar()));
        pm.setIdEmpresa(empresa);
        pm.setTenatID(pm.getIdEmpresa().getTenatID());
        pm.setIdUsuarioGeracao(usuario);
        pm = salvar(pm);
        if (pacote != null) {
            pm = adicionarModulo(pm, pacote.getModuloPacoteModuloList().stream()
                    .map(ModuloPacoteModulo::getIdModulo)
                    .collect(Collectors.toList()));
        } else {
            pm.setDataPagamento(new Date());
            pm = adicionarModulo(pm, moduloService.listar());
        }

        return salvar(pm);
    }

    public PagamentoMensalidade buscarPorReferencia(String reference) {
        return repositorio.buscarPorReferencia(reference);
    }

    public Double buscarValorPagoPor(Integer idEmpresa, Integer idUsuario, Date data) {
        Double valorPago = repositorio.buscarValorPagoPor(idEmpresa, idUsuario, data);
        if (valorPago == null) {
            return 0.0d;
        }
        return valorPago;
    }

    public List<PagamentoMensalidade> buscarPagamentosPor(MinMax<Date> periodo) {
        return repositorio.buscarPagamentosPor(periodo);
    }

}
