package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.exception.MessageListException;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Modulo;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacote;
import br.com.villefortconsulting.sgfinancas.entidades.ModuloPacoteModulo;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidadeModulo;
import br.com.villefortconsulting.sgfinancas.entidades.Perfil;
import br.com.villefortconsulting.sgfinancas.entidades.dto.CepDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.PagamentoSistemaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.PagamentoMensalidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.CidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.EmailService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FitPagService;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloPacoteService;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeModuloPermissaoService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeModuloService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroGeralService;
import br.com.villefortconsulting.sgfinancas.servicos.SmsService;
import br.com.villefortconsulting.sgfinancas.servicos.TransacaoGetnetService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoPagamentoSistema;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.StringUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeuPlanoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ParametroGeralService parametroGeralService;

    @EJB
    private FitPagService fitPagService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private TransacaoGetnetService transacaoGetnetService;

    @EJB
    private PagamentoMensalidadeModuloService pagamentoMensalidadeModuloService;

    @EJB
    private PagamentoMensalidadeModuloPermissaoService pagamentoMensalidadeModuloPermissaoService;

    @EJB
    private ModuloService moduloService;

    @EJB
    private ModuloPacoteService moduloPacoteService;

    @EJB
    private SmsService smsService;

    @EJB
    private EmailService emailService;

    @EJB
    private CidadeService cidadeService;

    private PagamentoMensalidade pagamentoMensalidadeSelecionado;

    private LazyDataModel<PagamentoMensalidade> model;

    private PagamentoMensalidadeFiltro filtro = new PagamentoMensalidadeFiltro();

    private Modulo moduloSelecionado;

    private ModuloPacote moduloPacoteSelecionado;

    private List<Modulo> modulos;

    private List<Modulo> selection;

    private PagamentoSistemaDTO dadosPagamento;

    private String tipoPagamento;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, pagamentoMensalidadeService);
    }

    public List<PagamentoMensalidadeModulo> getModulosContratados() {
        return pagamentoMensalidadeModuloService.getModulosPor(empresaService.getEmpresa());
    }

    public List<ModuloPacote> getModuloPacotesDisponiveis() {
        List<ModuloPacote> lista = moduloPacoteService.listar();
        PagamentoMensalidade pm = getPagamentoMensalidade();
        if (pm != null && pm.getIdModuloPacote() != null) {
            lista.removeIf(mp -> mp.equals(getPagamentoMensalidade().getIdModuloPacote()));
        }
        return lista;
    }

    public List<Modulo> getModulosDisponiveis() {
        List<Modulo> listaContratados = getModulosContratados().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .collect(Collectors.toList());
        List<Modulo> listaCompleta = moduloService.listar();

        return listaCompleta.stream()
                .filter(md -> listaContratados.stream().noneMatch(mc -> mc.getId().equals(md.getId())))
                .collect(Collectors.toList());
    }

    public PagamentoMensalidade getPagamentoMensalidade() {
        if (pagamentoMensalidadeSelecionado == null) {
            pagamentoMensalidadeSelecionado = pagamentoMensalidadeService.getUltimoPagamentoEmpresaLogada();
        }
        return pagamentoMensalidadeSelecionado;
    }

    public Double getValorPlano() {
        return getModulosContratados().stream()
                .mapToDouble(mod -> mod.getIdModulo().getValorMensalidade())
                .sum();
    }

    public String getTitle() {
        if (getPagamentoMensalidade() != null) {
            if (!getPagamentoMensalidade().isDentroDaVigenciaDoContrato()) {
                return "Renovar plano";
            } else if (empresaService.getEmpresa().getTipoConta().equals("G")) {
                return "Assine já";
            }
        }
        return "Contratação de " + (moduloPacoteSelecionado != null ? "promoção" : "modulo");
    }

    public String getTitleAcao() {
        if (getPagamentoMensalidade() != null) {
            if (empresaService.getEmpresa().getTipoConta().equals("G")) {
                return "Assinar";
            } else if (!getPagamentoMensalidade().isDentroDaVigenciaDoContrato()) {
                return "Renovar";
            }
        }
        return "Contratar";
    }

    public void init() {
        if (modulos == null || selection == null) {
            doStartRenovar();
        }
    }

    public boolean exibeBotaoExportarDados() {
        Perfil perfil = getUsuarioLogado().getIdPerfil();
        return Boolean.TRUE.equals(perfil.getEhSuporte()) || Boolean.TRUE.equals(perfil.getEhUsuarioMaster());
    }

    public void solictarExportacaoDados() {
        try {
            Empresa empresa = empresaService.getEmpresa();
            emailService.enviarEmailMS(EmailUtil.getSolicitacaoDados(empresa), "S");
            createFacesInfoMessage("Fique atento, logo entraremos em contato com você. A equipe do SG Finanças tem até 30 dias para lhe enviar os dados solicitados.");
        } catch (EmailException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String doStartRenovar() {
        if (getPagamentoMensalidade().isDentroDaVigenciaDoContrato()) {
            tipoPagamento = EnumTipoPagamentoSistema.RENOVACAO_ANTECIPADA.getCategoria();
        } else {
            tipoPagamento = EnumTipoPagamentoSistema.RENOVACAO.getCategoria();
        }
        modulos = moduloService.listar();
        Collections.sort(modulos, (a, b) -> {
            int obrigatorio = b.getObrigatorio().compareTo(a.getObrigatorio());
            return obrigatorio != 0 ? obrigatorio : a.getNome().compareTo(b.getNome());
        });
        selection = getModulosContratados().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .collect(Collectors.toList());
        modulos.stream()
                .filter(m -> selection.stream().anyMatch(ms -> ms.getId().equals(m.getId())))
                .forEach(m -> m.setAdquirido(true));
        selection.forEach(m -> m.setAdquirido(true));
        return fillFields();
    }

    public String doStartContratar() {
        tipoPagamento = EnumTipoPagamentoSistema.CONTRATACAO_MODULO.getCategoria();
        modulos = moduloService.listar();
        Collections.sort(modulos, (a, b) -> {
            int obrigatorio = b.getObrigatorio().compareTo(a.getObrigatorio());
            return obrigatorio != 0 ? obrigatorio : a.getNome().compareTo(b.getNome());
        });
        selection = getModulosContratados().stream()
                .map(PagamentoMensalidadeModulo::getIdModulo)
                .collect(Collectors.toList());
        modulos.removeIf(m -> selection.stream().anyMatch(ms -> ms.getId().equals(m.getId())));
        selection.removeIf(ms -> modulos.stream().noneMatch(m -> ms.getId().equals(m.getId())));
        return fillFields();
    }

    public String doStartAddModuloAoContrato() {
        doStartContratar();
        if (!selection.contains(moduloSelecionado)) {
            selection.add(moduloSelecionado);
        }
        return fillFields();
    }

    private String fillFields() {
        Empresa empresa = empresaService.getEmpresa();
        if (dadosPagamento == null) {
            dadosPagamento = new PagamentoSistemaDTO();
        }
        dadosPagamento.getFatura().setEndereco(empresa.getEnderecoGetnet());
        dadosPagamento.getFatura().setNome(empresa.getDescricao());
        dadosPagamento.getFatura().setCpfCnpj(empresa.getCnpj());
        if (CpfCnpjUtil.removerMascaraCnpj(empresa.getCnpj()).length() == 11) {
            dadosPagamento.getCartao().setCpfTitular(empresa.getCnpj());
            dadosPagamento.getCartao().setNascimentoTitular(empresa.getDataConstituicao());
        }
        return "/restrito/pagamento/renovar.xhtml";
    }

    public String doShowHistorico() {
        return "listaPagamento.xhtml";
    }

    public void buscarEnderecoPorCep() {
        CepDTO cepDTO = cidadeService.getEnderecoPorCep(dadosPagamento.getFatura().getEndereco().getCep());
        if (cepDTO != null) {
            dadosPagamento.getFatura().getEndereco().setUf(cepDTO.getUf().getDescricao());
            dadosPagamento.getFatura().getEndereco().setCidade(cepDTO.getDescricaoCidade());
            dadosPagamento.getFatura().getEndereco().setBairro(cepDTO.getBairro());
            dadosPagamento.getFatura().getEndereco().setRua(cepDTO.getEndereco());
            dadosPagamento.getFatura().getEndereco().setNumero("");
            dadosPagamento.getFatura().getEndereco().setComplemento("");
        }
    }

    public String geraCobranca() {
        Double valor;
        if (empresaService.getEmpresa().getTipoConta().equals("G")) {// Compra
            valor = selection.stream()
                    .mapToDouble(mod -> mod.getValorAdesao() + mod.getValorMensalidade())
                    .sum();
        } else {// Renovação/Contratação de módulo
            valor = getAdesaoTotal() + (getMensalidadeTotal() / 30) * getQuantidadeDiasRestantes();
        }
        dadosPagamento.setPreco(valor);
        PagamentoMensalidade pm = gerarEProcessarPagamento();
        if (pm != null) {
            pagamentoMensalidadeService.salvar(pm);
        }
        return "renovar.xhtml";
    }

    public Double getAdesaoTotal() {
        return selection.stream()
                .filter(m -> !m.isAdquirido())
                .mapToDouble(Modulo::getValorAdesao)
                .sum();
    }

    public Double getMensalidadeTotal() {
        return selection.stream()
                .mapToDouble(Modulo::getValorMensalidade)
                .sum();
    }

    public boolean ehExpirada() {
        return empresaService.getEmpresa().getTipoConta().equals("E");
    }

    public StreamedContent downloadTermoDeAdesao() {
        File file = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathTermoAdesao", NotFoundException::new));
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(file);
            return FileUtil.downloadFile(arquivo, "application/pdf", "Termo de uso.pdf");
        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public StreamedContent downloadPoliticaPrivacidade() {
        File file = new File(SystemPreferencesUtil.getPropOrThrow("defaults.pathPoliticaPrivacidade", NotFoundException::new));
        try {
            byte[] arquivo = FileUtil.convertFileToBytes(file);
            return FileUtil.downloadFile(arquivo, "application/pdf", "Pol[itica de privacidade.pdf");

        } catch (FileException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer getQuantidadeDiasRestantes() {
        if (getPagamentoMensalidade() == null) {
            return 30;
        }
        int qteDias = DataUtil.diferencaEntreDias(new Date(), getPagamentoMensalidade().getDataValidade());
        if (qteDias <= 0) {
            qteDias = 30;
        }
        return qteDias;
    }

    public String contratarPacote() {
        tipoPagamento = EnumTipoPagamentoSistema.CONTRATACAO_MODULO.getCategoria();
        modulos = moduloPacoteSelecionado.getModuloPacoteModuloList().stream()
                .map(ModuloPacoteModulo::getIdModulo)
                .collect(Collectors.toList());
        Collections.sort(modulos, (a, b) -> a.getNome().compareTo(b.getNome()));

        dadosPagamento = new PagamentoSistemaDTO();
        dadosPagamento.getFatura().setEndereco(empresaService.getEmpresa().getEnderecoGetnet());
        dadosPagamento.getFatura().setNome(empresaService.getEmpresa().getDescricao());
        dadosPagamento.getFatura().setCpfCnpj(empresaService.getEmpresa().getCnpj());
        return "/restrito/pagamento/contratarPacote.xhtml";
    }

    public String geraCobrancaPacote() {
        dadosPagamento.setPreco(moduloPacoteSelecionado.getValorAdesao() + moduloPacoteSelecionado.getValorMensalidade());
        PagamentoMensalidade pm = gerarEProcessarPagamento();
        if (pm != null) {
            pm.setIdModuloPacote(moduloPacoteSelecionado);
            pagamentoMensalidadeService.salvar(pm);
        }
        return "renovar.xhtml";
    }

    private PagamentoMensalidade gerarEProcessarPagamento() {
        try {
            List<Modulo> modulosNovos = new ArrayList<>();
            modulosNovos.addAll(selection);
            PagamentoMensalidade pm = pagamentoMensalidadeService.gerarPagamentoMensalidade(selection, tipoPagamento, getUsuarioLogado());
            if (pagamentoMensalidadeService.sendRequestPagamento(pm, dadosPagamento, modulosNovos, getQuantidadeDiasRestantes().doubleValue())) {
                if (pm.getDataPagamento() == null) {
                    createFacesSuccessMessage("Pagamento enviado com sucesso.");
                } else {
                    createFacesSuccessMessage("Pagamento realizado com sucesso!");
                }
            } else {
                createFacesErrorMessage("Transação não autorizada.");
            }
            return pm;
        } catch (MessageListException ex) {
            createFacesErrorMessage("Não foi possível enviar a solicitação de pagamento. Transação não aprovada.");
            Logger.getLogger(getClass().getName()).log(Level.WARNING, StringUtil.listaParaTexto(ex.getMessages()), ex);
            return null;
        }
    }

    public Double getValorTotal() {
        return getAdesaoTotal() + ((getMensalidadeTotal() / 30) * getQuantidadeDiasRestantes());
    }

}
