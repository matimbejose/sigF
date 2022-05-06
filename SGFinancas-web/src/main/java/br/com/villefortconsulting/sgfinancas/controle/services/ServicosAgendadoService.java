package br.com.villefortconsulting.sgfinancas.controle.services;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.PagamentoMensalidade;
import br.com.villefortconsulting.sgfinancas.entidades.ParametroSistema;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.CentroCustoService;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteMovimentacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.EmailService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.LoginAcessoService;
import br.com.villefortconsulting.sgfinancas.servicos.ModuloPacoteService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeModuloPermissaoService;
import br.com.villefortconsulting.sgfinancas.servicos.PagamentoMensalidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroNotificacaoService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroSistemaService;
import br.com.villefortconsulting.sgfinancas.servicos.RelatorioService;
import br.com.villefortconsulting.sgfinancas.servicos.SmsService;
import br.com.villefortconsulting.sgfinancas.servicos.UsuarioService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.EmailException;
import br.com.villefortconsulting.sgfinancas.util.EmailUtil;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoNotificacao;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.NumeroUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import br.com.villefortconsulting.util.operator.MinMax;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import org.apache.commons.lang3.StringUtils;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicosAgendadoService {

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ParametroNotificacaoService parametroNotificacaoService;

    @EJB
    private ParametroSistemaService parametroSistemaService;

    @EJB
    private ContaService contaService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private EmailService emailService;

    @EJB
    private SmsService smsService;

    @EJB
    private RelatorioService relatorioService;

    @EJB
    private PagamentoMensalidadeService pagamentoMensalidadeService;

    @EJB
    private ModuloPacoteService moduloPacoteService;

    @EJB
    private PagamentoMensalidadeModuloPermissaoService pagamentoMensalidadeModuloPermissaoService;

    @EJB
    private CentroCustoService centroCustoService;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    // Inject AdHocTenant adHocTenant -- Não incluir esse campo, ele é SessionScoped por isso vai dar erro ao executar o serviço agendado
    private final Object sincronizadorTenant = new Object();

    private Stream<Empresa> getEmpresasStream() {
        return loginAcessoService.getTodasEmpresas().stream()
                .map(EmpresaAcessoDTO::getTenat)
                .map(empresaService::getEmpresPorTenatID);
    }

    //@Schedule(hour = "08", minute = "10", info = "Envia email aos usuários de uma empresa informando das contas a pagar que a empresa possui.")
    public void enviarEmailContasAtrasadas() {
        getEmpresasStream()
                .filter(empresa -> empresa.getAtivo().equals("S"))
                .forEach(empresa -> {
                    ParametroSistema ps = parametroSistemaService.getParametroPorEmpresa(empresa);
                    if (ps == null || (!"S".equals(ps.getEnviaNotificacaoContas()) && !"S".equals(ps.getNotificacaoSmsEnviar()))) {
                        return;
                    }
                    String corpoEmail;
                    String corpoSms;

                    List<ContaParcela> vencidas = contaService.listarParcelasPagamentoAtraso(empresa.getTenatID());
                    List<ContaParcela> vencendo = contaService.listarParcelasPagamentoVencendoEm(ps.getPrazoNotificacao(), empresa.getTenatID());
                    List<ContaParcela> parcelas = new ArrayList<>();
                    parcelas.addAll(vencidas);
                    parcelas.addAll(vencendo);

                    String vencidasStr = vencidas.isEmpty() ? "" : vencidas.size() + " contas vencidas";
                    String vencendoStr = vencendo.isEmpty() ? "" : vencendo.size() + " contas vencendo em " + ps.getPrazoNotificacao() + (ps.getPrazoNotificacao() == 1 ? "dia" : "dias");
                    String parcelasStr = vencidasStr + (!vencidasStr.isEmpty() && !vencendoStr.isEmpty() ? " e " : "") + vencendoStr;

                    if (!parcelas.isEmpty()) {
                        corpoSms = "Você possui " + parcelasStr + " na empresa " + empresa.getDescricao() + "!";
                        corpoEmail = "Você possui " + parcelasStr + "! </br>";
                    } else {
                        return;
                    }

                    corpoSms += " Valor total: " + NumeroUtil.formatarCasasDecimais(parcelas.stream().mapToDouble(ContaParcela::getValor).sum(), 2, false);

                    final String corpoSmsFinal = corpoSms;
                    Arrays.asList(ps.getNotificacaoSmsNumeros().split(";")).forEach(numero -> smsService.send(corpoSmsFinal, numero));

                    StringBuilder html = new StringBuilder();
                    html.append("<table><tr><th>Plano de conta</th><th>Observação</th><th>Conta bancária</th><th>Fornecedor</th><th>Origem</th><th>Vencimento</th><th>Valor</th></tr>");
                    parcelas.forEach(parcela -> html
                            .append("<tr><td>")
                            .append(parcela.getIdConta().getIdPlanoConta().getDescricao())
                            .append("</td><td>")
                            .append(parcela.getObservacao())
                            .append("</td><td>")
                            .append(parcela.getIdContaBancaria().getDescricao())
                            .append("</td><td>")
                            .append(parcela.getIdConta().getIdFornecedor() == null ? "" : parcela.getIdConta().getIdFornecedor().getRazaoSocial())
                            .append("</td><td>")
                            .append(EnumTipoConta.getDescricao(parcela.getIdConta().getTipoConta()))
                            .append("</td><td>")
                            .append(DataUtil.dateToString(parcela.getDataVencimento()))
                            .append("</td><td>R$ ")
                            .append(NumeroUtil.formatarCasasDecimais(parcela.getValor(), 2))
                            .append("</td></tr>"));
                    html.append("</table>");
                    corpoEmail += "Parcelas a pagar:<br>" + html.toString();

                    List<Usuario> usuarios = usuarioService.listarUsuariosMasterComEmail(empresa);

                    // se houver email na empresa, adiciona la tambem
                    if (StringUtils.isNotEmpty(empresa.getEmail()) && StringUtils.isNotBlank(empresa.getEmail())) {
                        Usuario u = new Usuario();
                        u.setEmail(empresa.getEmail());
                        usuarios.add(u);
                    }

                    usuarios.addAll(parametroNotificacaoService.listarEmails(EnumTipoNotificacao.CONTAS_PENDENTES.getTipo(), empresa.getTenatID()).stream()
                            .map(email -> {
                                Usuario u = new Usuario();
                                u.setEmail(email);
                                return u;
                            })
                            .collect(Collectors.toList()));

                    usuarios.removeIf(user -> user.getEmail() == null || StringUtils.isBlank(user.getEmail())
                            || usuarios.stream().anyMatch(u -> !u.equals(user) && user.getEmail().equals(u.getEmail())));

                    try {
                        emailService.enviarEmailMS(EmailUtil.getEmailContaPendente(usuarios, empresa, corpoEmail), "S");
                    } catch (EmailException ex) {
                        Logger.getLogger(ServicosAgendadoService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
    }

    @Schedule(hour = "6", minute = "30", info = "Atualiza o campo tipoConta de acordo com o vencimento da última mensalidade paga")
    public void atualizaStatusPagamentoEmpresa() {
        usuarioService.logarComUsuarioSuporte();
        getEmpresasStream()
                .forEach(empresa -> {
                    if (empresa.getPrazoUsarSemComprar() == -1) {
                        empresa.setTipoConta("P");//Pago
                    } else if (empresa.getDataCredenciamento() == null) {
                        // Não pode ter empresa sem data de credenciamento, automaticamente expira a empresa.
                        // Se alguém estiver usando a pessoa entrará em contato e o problema será resolvido.
                        empresa.setTipoConta("E");//Expirada
                    } else if (DataUtil.diferencaEntreDias(empresa.getDataCredenciamento(), new Date()) <= empresa.getPrazoUsarSemComprar()) {
                        empresa.setTipoConta("G");//Grátis
                    } else {
                        PagamentoMensalidade pm = pagamentoMensalidadeService.getUltimoPagamentoPor(empresa);
                        if (pm == null || pm.getDataValidade().before(new Date())) {
                            empresa.setTipoConta("E");//Expirada
                        }
                    }

                    loginAcessoService.salvarEmpresa(empresa);
                });
    }

    //@Schedule(hour = "7", info = "Remove as permissões dos usuários de módulos não contratados")
    public void atualizaPermissoesUsuario() {
        usuarioService.logarComUsuarioSuporte();
        synchronized (sincronizadorTenant) {
            for (Empresa empresa : getEmpresasStream().collect(Collectors.toList())) {// Não usar stream para não dar problema com o tenant id
                if (!empresa.getTipoConta().equals("P")) {
                    continue;
                }
                loginAcessoService.alteraTenatComUsuarioSuporteSemPermissao(empresa.getTenatID());
                pagamentoMensalidadeModuloPermissaoService.atualizaPermissoes(empresa, false);
            }
            loginAcessoService.alteraTenatComUsuarioSuporteSemPermissao(null);
        }
    }

    @Schedule(hour = "7", info = "Atualiza a situação dos pacotes de módulos")
    public void atualizaSituacaoPacoteDeModulo() {
        moduloPacoteService.listar().stream()
                .filter(mp -> "S".equals(mp.getAtivo()) && mp.getDataVencimento() != null && new Date().after(mp.getDataVencimento()))
                .forEach(mp -> {
                    mp.setAtivo("N");
                    moduloPacoteService.salvar(mp);
                });
    }

    @Schedule(hour = "08", minute = "10", info = "Envia email informando que o contrato do Sg Finanças está quase vencendo")
    public void enviarEmailVencimentoContrato() {
        getEmpresasStream()
                .map(pagamentoMensalidadeService::getUltimoPagamentoPor)
                .filter(Objects::nonNull)
                .filter(pm -> DataUtil.diferencaEntreDias(new Date(), pm.getDataValidade()) == 7)
                .forEach(pm -> {
                    try {
                        emailService.enviarEmailMS(EmailUtil.getEmailVencimentoSistema(pm), "S");
                    } catch (EmailException ex) {
                        Logger.getLogger(ServicosAgendadoService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
    }

    @Schedule(dayOfMonth = "1", hour = "08", minute = "10", info = "Envia email informando clientes que não receberam fatura do Iugu mas treceberam notas")
    public void enviarEmailNotificacaoFaltaFaturaIUGU() {
        Date inicio = DataUtil.getPrimeiroDiaDoMes(DataUtil.subtrairMeses(DataUtil.getHoje(), 1));
        Date fim = DataUtil.getUltimoDiaDoMes(inicio);
        List<String> tenatEmpresasIugu = centroCustoService.empresasComIugu();
        for (String tenat : tenatEmpresasIugu) {
            List<Cliente> clientesIUGU = clienteMovimentacaoService.clientesComIuguNoPeriodo(inicio, fim, tenat);
            List<Cliente> clientesNota = clienteMovimentacaoService.clientesComNotasNoPeriodo(inicio, fim, tenat);
            clientesNota.removeIf(clientesIUGU::contains);
            if (clientesNota.isEmpty()) {
                return;
            }
            List<Usuario> usuarios = usuarioService.listarUsuariosRecebemNotificacaoIugu(tenat);
            try {
                emailService.enviarEmailMS(EmailUtil.getEmailClienteFaturasIuguFaltantes(clientesNota, usuarios), "S");
            } catch (EmailException ex) {
                Logger.getLogger(ServicosAgendadoService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Schedule(dayOfMonth = "1", hour = "5", minute = "15", info = "Envia o relatório de acessos por mês")
    public void enviaRelatorioAcessosPorEmpresa() {
        if (!SystemPreferencesUtil.getProp("ambiente", "TESTE").equalsIgnoreCase("PRODUCAO")) {
            return;
        }
        Date date = DataUtil.subtrairMeses(new Date(), 1);
        MinMax<Date> periodo = new MinMax<>();
        periodo.setMin(DataUtil.getPrimeiroDiaDoMes(date));
        periodo.setMax(DataUtil.getUltimoDiaDoMes(date));
        try {
            usuarioService.logarComUsuarioSuporte();
            byte[] xml = relatorioService.acessosPorEmpresa(false, true, false, periodo);
            List<Usuario> usuarios = usuarioService.listarUsuariosRecebemNotificacaoAcessosPorEmpresa();
            emailService.enviarEmailMS(EmailUtil.getEmailAcessoEmpresa(xml, usuarios), "S");
        } catch (IOException | EmailException ex) {
            Logger.getLogger(ServicosAgendadoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
