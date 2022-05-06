package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.controle.apoio.ControleMenu;
import br.com.villefortconsulting.sgfinancas.controle.services.ServicosAgendadoService;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmpresaAcessoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ServicoAgendadoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.*;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.servicos.nfe.NfeProdutoService;
import br.com.villefortconsulting.util.SystemPreferencesUtil;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.NotFoundException;

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
public class AdministracaoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private AdministracaoService administracaoService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    private FipeService fipeService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private LoginAcessoService loginAcessoService;

    @EJB
    private ServicosAgendadoService servicosAgendadoService;

    @EJB
    private UsuarioService usuarioService;

    @EJB
    private NFService nFService;

    @EJB
    private ContaService contaService;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private NfeProdutoService nfeProdutoService;

    @Inject
    private ControleMenu controleMenu;

    private Usuario usuario;

    private List<ServicoAgendadoDTO> servicosAgendados = new ArrayList<>();

    private List<ServicoAgendadoDTO> rotinas = new ArrayList<>();

    @PostConstruct
    public void preencherMetodos() {
        servicosAgendados = new ArrayList<>();
        rotinas = new ArrayList<>();
        try {
            servicosAgendados.addAll(Arrays.stream(ServicosAgendadoService.class.getMethods())
                    .filter(m -> m.isAnnotationPresent(Schedule.class))
                    .map(m -> {
                        Schedule annotatation = m.getAnnotation(Schedule.class);
                        return new ServicoAgendadoDTO(m.getName(), annotatation.info(), annotatation.toString().replaceAll(".*\\(", "("));
                    })
                    .collect(Collectors.toList()));
            Collections.sort(servicosAgendados, (a, b) -> a.getName().compareTo(b.getName()));
        } catch (SecurityException ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        rotinas.add(new ServicoAgendadoDTO("atualizaSaldoCliente", "Atualizar saldo de clientes", "Atualiza o saldo dos cliente de aconrdo com a importação do IUGU e de notas fiscais"));
        rotinas.add(new ServicoAgendadoDTO("limpaCache", "Limpa o cache do sistema", "Limpa o cache do Hibernate e o arquivo server.properties"));
        rotinas.add(new ServicoAgendadoDTO("reprocessarNFe", "Re-processar notas com problema no retorno", "Re-processa notas que tiveram problema no retorno e foram incluídos manualmente"));
        rotinas.add(new ServicoAgendadoDTO("atualizaMarcas", "Atualizar marcas com tipo de veiculo", "Atualizar marcas com tipo de veiculo"));
        rotinas.add(new ServicoAgendadoDTO("criarPlanoContaTransferencias", "Cria os planos de contas de transferencias", "Cria os planos de contas de transferencias"));
        rotinas.add(new ServicoAgendadoDTO("ajustarTransferenciasSemPlanoContaTransferencias", "Ajusta as transferência ja realizadas sem plano de contas transferencia", "Ajusta as transferência ja realizadas sem plano de contas transferencia"));
        rotinas.add(new ServicoAgendadoDTO("ajustarTransferenciasSemPlanoContaTransferenciasEmpresaLogada", "Ajusta as transferência ja realizadas sem plano de contas transferencia na empresa logada", "Ajusta as transferência ja realizadas sem plano de contas transferencia na empresa logada"));

    }

    public void executaServicoAgendado(ServicoAgendadoDTO sa) {
        try {
            Method metodo = Arrays.stream(ServicosAgendadoService.class.getMethods())
                    .filter(m -> m.isAnnotationPresent(Schedule.class))
                    .filter(m -> m.getName().equals(sa.getName()))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException("O método " + sa.getName() + " não existe."));
            metodo.invoke(servicosAgendadoService);
            createFacesSuccessMessage("Serviço agendado executado com sucesso.");
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível executar o serviço. Motivo: " + ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public void executaRotina(ServicoAgendadoDTO sa) {
        try {
            Method metodo = Arrays.stream(AdministracaoControle.class.getMethods())
                    .filter(m -> m.getName().equals(sa.getName()))
                    .findAny()
                    .orElseThrow(() -> new NotFoundException("O método " + sa.getName() + " não existe."));
            metodo.invoke(this);
            createFacesSuccessMessage("Rotina executada com sucesso.");
        } catch (Exception ex) {
            createFacesErrorMessage("Não foi possível executar a rotina. Motivo: " + ex.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public void limpaCache() {
        administracaoService.limpaCache();
        SystemPreferencesUtil.reset();
        controleMenu.resetCache();
    }

    public void atualizaSaldoCliente() {
        clienteService.listar().forEach(clienteMovimentacaoService::atualizarSaldo);
    }

    public void atualizaMarcas() {
        fipeService.atualizaTipoVeiculoMarcasSistema();
    }

    public void reprocessarNFe() {
        nFService.listarNFProblemaRetorno().forEach(nf -> {
            boolean certo = nfeProdutoService.reProcessarNFe(nf);
            if (!certo) {
                createFacesErrorMessage("Erro na nota " + nf.getNumeroNotaFiscal() + " ID: " + nf.getId());
            }
        });
    }

    private Stream<Empresa> getEmpresasStream() {
        return loginAcessoService.getTodasEmpresas().stream()
                .map(EmpresaAcessoDTO::getTenat)
                .map(empresaService::getEmpresPorTenatID);
    }

    public void criarPlanoContaTransferencias() {
        getEmpresasStream().forEach(empresa -> {
            adHocTenant.setTenantID(empresa.getTenatID());
            contaBancariaService.listarContasPorEmpresa(empresa).forEach(cc -> {
                try {
                    contaBancariaService.salvar(cc);
                } catch (CadastroException ignored) {
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Erro ao cadastrar plano de conta para a empresa " + empresa.getDescricao() + " na conta bancária " + cc.getDescricao(), ex);
                }
            });
        });
    }

    public void ajustarTransferenciasSemPlanoContaTransferencias() {
        List<Empresa> empresas = empresaService.getEmpresas();
        for (Empresa empresa : empresas) {
            contaService.listarTransferenciasSemPlanoContaTransferencia(empresa);
        }
    }

    public void ajustarTransferenciasSemPlanoContaTransferenciasEmpresaLogada() {
        Empresa empresa = empresaService.getEmpresa();
        contaService.listarTransferenciasSemPlanoContaTransferencia(empresa);
    }

}
