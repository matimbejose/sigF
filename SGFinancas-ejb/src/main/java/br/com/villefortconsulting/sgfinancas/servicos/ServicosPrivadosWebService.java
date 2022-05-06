package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.sgfinancas.entidades.CentroCusto;
import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.NFS;
import br.com.villefortconsulting.sgfinancas.entidades.TokenApp;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ClienteIuguDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.FaturaCallbackDto;
import br.com.villefortconsulting.sgfinancas.entidades.dto.NFSeRetornoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ParcelaFaturaDto;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.NFSeMapper;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoClienteMovimentacao;
import br.com.villefortconsulting.sgfinancas.util.nf.EnumSituacaoNF;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.MicroServiceUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ServicosPrivadosWebService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    private static final String OK = "\"OK\"";

    @Inject
    private NFSeMapper nfseMapper;

    @EJB
    private ContaService contaService;

    @EJB
    private ClienteService clienteService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private ClienteMovimentacaoService clienteMovimentacaoService;

    @EJB
    private NFSService nfsService;

    @EJB
    private TokenAppService tokenAppService;

    @EJB
    private CentroCustoService centroCustoService;

    @EJB
    private UsuarioService usuarioService;

    //<editor-fold defaultstate="collapsed" desc="Util">
    private Authentication getUserAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new NotAuthorizedException("Bearer");
        } else if (auth.getPrincipal() instanceof String) {
            String[] tk = ((String) auth.getPrincipal()).split(":");
            if (tk.length == 1 && tk[0].trim().isEmpty()) {
                throw new NotAuthorizedException("Bearer");
            }
            Empresa empresa = null;
            if (!tk[1].equals("null")) {
                empresa = empresaService.getEmpresPorTenatID(tk[1]);
            }
            TokenApp token = tokenAppService.buscar(usuarioService.buscar(Integer.parseInt(tk[0])), empresa, tk[2]);
            if (token == null || token.getIdUsuario() == null) {
                throw new NotAuthorizedException("Bearer");
            }
            Usuario usuarioNovo = token.getIdUsuario();
            if (token.getIdEmpresa() != null) {
                usuarioNovo.setTenat(token.getIdEmpresa().getTenatID());
                adHocTenant.setTenantID(usuarioNovo.getTenat());
            }
            usuarioNovo.setUuid(token.getDeviceUuid());

            auth = new UsernamePasswordAuthenticationToken(usuarioNovo, "", null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else if (!(auth.getPrincipal() instanceof Usuario)) {
            throw new NotAuthorizedException("Bearer");
        }
        return auth;
    }

    private Usuario getUsuarioLogado() {
        return (Usuario) getUserAuth().getPrincipal();
    }

    private boolean logarComCnpj(String cnpj) {
        try {
            if (usuarioService.logarComCnpj(cnpj) == null) {
                return false;
            }
            adHocTenant.setTenantID(getUsuarioLogado().getTenat());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    //</editor-fold>

    private void processaRetorno(NFSeRetornoDTO retorno) {
        if (!logarComCnpj(retorno.getCnpjEmpresa())) {
            return;
        }
        NFS nfs = nfsService.buscar(Integer.parseInt(retorno.getNome().split("-")[0]));// formato /ID-(ret-loterps|cannfse)\.(xml|err)/
        String operacao = retorno.getNome().replaceFirst("\\d+-", "").replaceFirst("\\..*", "");
        NFS.TipoRetorno tipo = "ret-loterps".equals(operacao) ? NFS.TipoRetorno.ENVIO : NFS.TipoRetorno.CANCELAMENTO;

        if (retorno.getXmlRetorno() != null) {
            nfs.setXmlRetorno(tipo, retorno.getXmlRetorno(), retorno.getXmlArmazenamento());
            nfs.setProtocolo(retorno.getProtocolo());
            nfs.setNumeroNotaFiscal(retorno.getNumeroNotaFiscal());
            ContaParcela cp = contaService.buscarParcelaPorNFS(nfs);
            cp.setNumNf(nfs.getNumeroNotaFiscalFormatada());
            cp.appendObservacao(nfs.getNumeroNotaFiscalFormatada() + "(" + (tipo == NFS.TipoRetorno.ENVIO ? "enivada" : "cancelada") + ")");
            contaService.salvarContaParcela(cp);
        } else {
            nfs.setErro(retorno.getMensagemErro());
        }
        nfsService.salvar(nfs);

        EnumSituacaoNF situacao = EnumSituacaoNF.retornaEnumSelecionado(nfs.getSituacao());
        if (EnumSituacaoNF.CANCELADA == situacao) {
            List<ContaParcela> parcelasDaNota = contaService.parcelasPorNFS(nfs);
            if ("S".equals(nfs.getCancelarParcelas())) {
                // Caso seja uma NFS gerada pelo agrupamento de parcelas, cancelar as parcelas que foram agrupadas
                // Caso ela tenha sido gerada de outra forma mas esteja vinculada a uma lista determinada de parcelas, cancelar apenas essa lista
                if (!parcelasDaNota.isEmpty()) {
                    parcelasDaNota.stream().forEach(contaService::cancelarContaParcela);
                } else {// Caso não tenha uma lista determinada de parcelas mas tenha uma conta vinculada, cancelar as parcelas em aberto da conta
                    contaService.contaPorNFS(nfs).ifPresent(conta -> contaService.cancelarParcelas(conta, contaService.parcelasEmAberto(conta)));
                }
            } else {
                parcelasDaNota.forEach(cp -> {
                    cp.setIdNFS(null);
                    contaService.salvarContaParcela(cp);
                });
            }
            return;
        }
        if (EnumSituacaoNF.ENVIADA == situacao && contaService.buscarParcelaPorNFS(nfs) == null) {
            contaService.adicionarConta(nfsService.preencherContaPorNFS(nfs));
        }
    }

    public Object nfse(List<NFSeRetornoDTO> listaRetorno) {
        listaRetorno.forEach(this::processaRetorno);
        return OK;
    }

    public Object nfse(Integer id) {
        return nfseMapper.toDTO(nfsService.buscar(id));
    }

    public Object atulizarDadosCliente() {
        for (Cliente cliente : clienteService.listarClienteComIdIntegracao()) {
            ResponseEntity<String> clienteCallback = MicroServiceUtil.doGet(MicroServiceUtil.MicroServicos.IUGU, "iugo/v0/cliente/" + cliente.getIdIntegracao());
            try {
                ClienteIuguDTO clienteDto = new ObjectMapper().readValue(clienteCallback.getBody(), ClienteIuguDTO.class);
                if (clienteDto != null) {
                    cliente.setNome(clienteDto.getName());
                    clienteService.salvar(cliente);
                }
            } catch (IOException ex) {
                Logger.getLogger(ServicosPrivadosWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return OK;
    }

    public Object salvaFatura(FaturaCallbackDto fatura) {
        try {
            adHocTenant.setTenantID(fatura.getIdEmpresa().toString());
            CentroCusto centro = centroCustoService.buscaCentroCustoByCNPJ(CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(fatura.getCnpjCentroCusto())));
            Cliente cliente;
            if (fatura.getIdCliente() == null) {
                cliente = clienteService.findClienteByNomeAndCreate("Clientes diversos");
            } else {
                String cpfCnpj = CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(fatura.getIdCliente().getCpfCnpj()));
                cliente = clienteService.buscarPorCpfCnpj(cpfCnpj);
                if ((cliente == null && cpfCnpj.replaceAll("\\D", "").isEmpty())) {
                    cliente = clienteService.findClienteByNomeAndCreate("Clientes diversos");
                }
            }
            if (cliente == null) {
                cliente = new Cliente();
                cliente.setNome(fatura.getIdCliente().getName());
                cliente.setCpfCNPJ(CpfCnpjUtil.mascararCpfOuCnpj(CpfCnpjUtil.removerMascaraCnpj(fatura.getIdCliente().getCpfCnpj())));
                cliente.setEmail(fatura.getIdCliente().getEmail());
                cliente.setIdIntegracao(fatura.getIdCliente().getId());
                cliente.setTipo(CpfCnpjUtil.removerMascaraCnpj(fatura.getIdCliente().getCpfCnpj()).length() == 14 ? "PJ" : "PF");
                cliente = clienteService.salvar(cliente);
            }

            ClienteMovimentacao movimentacaoJaLancada = clienteMovimentacaoService.buscaMovimentobyIdIntegracao(cliente, fatura.getIdFatura());
            if (movimentacaoJaLancada != null) {
                boolean temPagamento = fatura.getParcelas().stream()
                        .anyMatch(p -> p.getStatus().equals("done"));
                if (fatura.getDataPagamento() != null && movimentacaoJaLancada.getDataPagamento() == null) {
                    if (movimentacaoJaLancada.getDataCancelamento() != null) {
                        reativarMovimento(movimentacaoJaLancada);
                    }
                    gerarPagamentoMovimentacao(fatura, movimentacaoJaLancada, cliente);
                    gerarPagamentoFatura(fatura, movimentacaoJaLancada, cliente, centro);
                }
                if (fatura.getDataLiquidacao() != null && (movimentacaoJaLancada.getDataLiquidacao() == null || temPagamento)) {
                    if (movimentacaoJaLancada.getDataCancelamento() != null) {
                        reativarMovimento(movimentacaoJaLancada);
                    }
                    gerarPagamentoFatura(fatura, movimentacaoJaLancada, cliente, centro);
                }
                if (fatura.getDataExtorno() != null && movimentacaoJaLancada.getDataCancelamento() == null && fatura.getDataPagamento() == null) {
                    gerarExtorno(fatura, movimentacaoJaLancada, cliente);
                    contaService.cancelarContaEBaixa(fatura);
                }
                if (fatura.getDataCancelamento() != null && movimentacaoJaLancada.getDataCancelamento() == null && fatura.getDataPagamento() == null) {
                    cancelarMovimentacao(fatura, movimentacaoJaLancada, cliente);
                    contaService.cancelarContaEBaixa(fatura);
                }
                if (fatura.getDataExpiracao() != null && movimentacaoJaLancada.getDataCancelamento() == null && fatura.getDataPagamento() == null) {
                    expirarMovimentacao(fatura, movimentacaoJaLancada, cliente);
                    contaService.cancelarContaEBaixa(fatura);
                }
                if (fatura.getDataReembolso() != null && movimentacaoJaLancada.getDataCancelamento() == null) {
                    gerarReembolso(fatura, movimentacaoJaLancada, cliente);
                    contaService.cancelarContaEBaixa(fatura);
                }
            } else {
                ClienteMovimentacao movimentacao = clienteMovimentacaoService.lancarMovimentacao(cliente, fatura, EnumTipoClienteMovimentacao.IUGU, centro);
                if (fatura.getQuantidadeParcelas() != null) {
                    movimentacao.setQuantidadeParcelas(fatura.getQuantidadeParcelas());
                    clienteMovimentacaoService.salvar(movimentacao);
                }
                Conta conta = contaService.preencheContaIUGU(cliente, fatura, centro, movimentacao, centro.getIdPlanoContaIugu());
                contaService.listarContaParcela(conta).stream()
                        .map(cp -> {
                            cp.setNumNf(fatura.getIdFatura());
                            return cp;
                        })
                        .forEach(contaService::salvarContaParcela);
                if (fatura.getDataPagamento() != null) {
                    gerarPagamentoMovimentacao(fatura, movimentacao, cliente);
                }
                if (fatura.getDataCancelamento() != null && fatura.getDataPagamento() == null) {
                    cancelarMovimentacao(fatura, movimentacao, cliente);
                }
                if (fatura.getDataExpiracao() != null && fatura.getDataPagamento() == null) {
                    expirarMovimentacao(fatura, movimentacao, cliente);
                }
                if (fatura.getDataExtorno() != null && fatura.getDataPagamento() == null) {
                    gerarExtorno(fatura, movimentacao, cliente);
                }
                if (fatura.getDataReembolso() != null) {
                    gerarReembolso(fatura, movimentacao, cliente);
                }
            }
            return OK;
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.WARNING, ex.getMessage(), ex);
            return null;
        }
    }

    public void gerarReembolso(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente) {
        cm.setDataCancelamento(fatura.getDataReembolso());
        cm.setStatus("Reembolsado");
        clienteMovimentacaoService.salvar(cm);
        clienteMovimentacaoService.atualizarSaldo(cliente);
        contaService.cancelarContaEBaixa(fatura);
    }

    public void gerarExtorno(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente) {
        cm.setDataCancelamento(fatura.getDataExtorno());
        cm.setStatus("Extornado");
        clienteMovimentacaoService.salvar(cm);
        clienteMovimentacaoService.atualizarSaldo(cliente);
        contaService.cancelarContaEBaixa(fatura);
    }

    public void cancelarMovimentacao(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente) {
        cm.setDataCancelamento(fatura.getDataCancelamento());
        cm.setStatus("Cancelado");
        clienteMovimentacaoService.salvar(cm);
        clienteMovimentacaoService.atualizarSaldo(cliente);
        contaService.cancelarContaEBaixa(fatura);
    }

    public void expirarMovimentacao(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente) {
        cm.setDataCancelamento(fatura.getDataExpiracao());
        cm.setStatus("Expirado");
        clienteMovimentacaoService.salvar(cm);
        clienteMovimentacaoService.atualizarSaldo(cliente);
        contaService.cancelarContaEBaixa(fatura);
    }

    public void gerarPagamentoMovimentacao(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente) {
        if (fatura.getDataLiquidacao() != null && cm.getDataLiquidacao() == null) {
            cm.setDataLiquidacao(fatura.getDataLiquidacao());
        }
        clienteMovimentacaoService.pagar(cm, fatura.getDataPagamento(), fatura.getTotalFatura(),
                fatura.getTotalJuros(), fatura.getTotalTaxaPaga(), EnumTipoClienteMovimentacao.IUGU);
        clienteMovimentacaoService.atualizarSaldo(cliente);
    }

    public void reativarMovimento(ClienteMovimentacao cm) {
        if (cm.getDataCancelamento() != null) {
            cm.setDataCancelamento(null);
        }
    }

    public void gerarPagamentoFatura(FaturaCallbackDto fatura, ClienteMovimentacao cm, Cliente cliente, CentroCusto centro) throws ContaException {
        List<ContaParcela> parcelas = contaService.buscarParcelasByIdClienteMovimentacao(cm);
        for (ContaParcela parcela : parcelas) {
            if (parcela.getDataCancelamento() != null) {
                contaService.reativarContaParcela(parcela, parcelas);
            }
        }
        if (fatura.getQuantidadeParcelas() != null) {
            for (ParcelaFaturaDto parcela : fatura.getParcelas()) {
                if (parcela.getDataLiquidacao() != null) {
                    ContaParcela cp = parcelas.stream()
                            .filter(p -> p.getNumParcela().equals((Integer) Integer.parseInt(parcela.getNumeroParcela())))
                            .filter(p -> p.getDataPagamento() == null)
                            .findAny()
                            .orElse(null);

                    if (cp != null && parcela.getStatus().equals("done")) {
                        cp.setDataPagamento(parcela.getDataLiquidacao());
                        contaService.pagarParcelaIntegral(cp);
                    }
                    boolean tudoPago = fatura.getParcelas().stream()
                            .allMatch(p -> p.getStatus().equals("done"));
                    if (tudoPago) {
                        cm.setDataLiquidacao(fatura.getDataLiquidacao());
                        clienteMovimentacaoService.salvar(cm);
                    }
                }

            }
        } else {
            if (parcelas.size() == 1 && fatura.getDataLiquidacao() != null) {
                parcelas.get(0).setDataPagamento(fatura.getDataLiquidacao());
                contaService.pagarParcelaIntegral(parcelas.get(0));
            }
            cm.setDataLiquidacao(fatura.getDataLiquidacao());
            clienteMovimentacaoService.salvar(cm);
        }
    }

}
