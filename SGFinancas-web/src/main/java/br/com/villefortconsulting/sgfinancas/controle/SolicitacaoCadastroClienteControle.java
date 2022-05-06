package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.ClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.SolicitacaoCadastroClienteVeiculo;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.EmailDTO;
import br.com.villefortconsulting.sgfinancas.entidades.mapper.DocumentoMapper;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SolicitacaoCadastroClienteVeiculoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmailService;
import br.com.villefortconsulting.sgfinancas.servicos.SmsService;
import br.com.villefortconsulting.sgfinancas.servicos.SolicitacaoCadastroClienteService;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusSolicitacaoCadastroCliente;
import br.com.villefortconsulting.util.CpfCnpjUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.primefaces.PrimeFaces;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SolicitacaoCadastroClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ClienteService clienteService;

    @EJB
    protected DocumentoService documentoService;

    @EJB
    protected DocumentoAnexoService documentoAnexoService;

    @EJB
    private SolicitacaoCadastroClienteService service;

    @EJB
    private EmailService emailService;

    @EJB
    private SmsService smsService;

    @Inject
    protected DocumentoMapper documentoMapper;

    private SolicitacaoCadastroClienteVeiculo objetoSelecionado;

    private LazyDataModel<SolicitacaoCadastroClienteVeiculo> model;

    private SolicitacaoCadastroClienteVeiculoFiltro filtro = new SolicitacaoCadastroClienteVeiculoFiltro();

    private List<AnexoDTO> listaAnexos;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service);
    }

    public String doStartView() {
        listaAnexos = objetoSelecionado.getIdDocumento().getDocumentoAnexoList().stream()
                .map(documentoMapper::toDTO)
                .collect(Collectors.toList());
        return "visualizarSolicitacaoCadastroCliente.xhtml";
    }

    public void doReject() {
        if (objetoSelecionado.getMotivoCancelamento() == null || objetoSelecionado.getMotivoCancelamento().trim().isEmpty()) {
            createFacesErrorMessage("Informe o motivo do cancelamento");
        }
        try {
            objetoSelecionado.setStatus(EnumStatusSolicitacaoCadastroCliente.REJEITADO.getSigla());
            objetoSelecionado.setDataModificacao(new Date());
            service.salvar(objetoSelecionado);
        } catch (Exception ex) {
            createFacesErrorMessage("Ocorreu um erro ao rejeitar a solicitação");
        }
        PrimeFaces.current().executeScript("PF('cancelamento').hide();location.href = `/restrito/solicitacaoCadastroCliente/listaSolicitacaoCadastroCliente.xhtml`;");
        enviaEmailSms("Sua solicitação de adesão ao seguro ABAPAV do veículo " + objetoSelecionado.getIdModeloInformacao().getIdModelo().getIdMarca().getNome() + ", "
                + objetoSelecionado.getIdModeloInformacao().getIdModelo().getNome() + ", " + objetoSelecionado.getIdModeloInformacao().getAno() + " foi negada pelo motivo \""
                + objetoSelecionado.getMotivoCancelamento() + "\". Entre em contato no telefone (31) 9 8239-7105 para maiores esclarecimentos.");
    }

    private void enviaEmailSms(String mensagem) {
        try {
            Usuario dest = new Usuario();
            dest.setNome(objetoSelecionado.getIdSolicitacaoCadastroCliente().getNome());
            dest.setEmail(objetoSelecionado.getIdSolicitacaoCadastroCliente().getEmail());
            EmailDTO email = new EmailDTO();
            email.getDestinatarios().add(dest);
            email.setAssunto("Solicitação de cadastro");
            email.setTituloMensagem("Prezado (a)");
            email.setMensagem(mensagem);
            emailService.enviarEmailMS(email, "S");
        } catch (Exception ignored) {
        }
        try {
            smsService.send(mensagem, objetoSelecionado.getIdSolicitacaoCadastroCliente().getCelular());
        } catch (Exception ignored) {
        }
    }

    public String doAccept() {
        objetoSelecionado.setStatus(EnumStatusSolicitacaoCadastroCliente.APROVADO.getSigla());
        objetoSelecionado.setDataModificacao(new Date());
        try {
            service.salvar(objetoSelecionado);
            Cliente cliente = objetoSelecionado.getIdSolicitacaoCadastroCliente().getIdCliente();
            if (cliente == null) {
                cliente = new Cliente();

                cliente.setEndereco(objetoSelecionado.getIdSolicitacaoCadastroCliente().getEndereco());
                cliente.setNome(objetoSelecionado.getIdSolicitacaoCadastroCliente().getNome());
                cliente.setCpfCNPJ(objetoSelecionado.getIdSolicitacaoCadastroCliente().getCpfCnpj());
                cliente.setTipo(CpfCnpjUtil.removerMascaraCnpj(cliente.getCpfCNPJ()).length() == 11 ? "PF" : "PJ");
                cliente.setRazaoSocial(objetoSelecionado.getIdSolicitacaoCadastroCliente().getNome());
                cliente.setEmail(objetoSelecionado.getIdSolicitacaoCadastroCliente().getEmail());
                cliente.setTelefoneCelular(objetoSelecionado.getIdSolicitacaoCadastroCliente().getCelular());
                cliente.setAtivo("S");
                cliente.setSeguradora("N");
                cliente.setCnh(objetoSelecionado.getIdSolicitacaoCadastroCliente().getCnh());
                cliente.setCategoriaCNH(objetoSelecionado.getIdSolicitacaoCadastroCliente().getCategoriaCnh());
                cliente.setClienteVeiculoList(new ArrayList<>());
                cliente = clienteService.salvar(cliente);

                objetoSelecionado.getIdSolicitacaoCadastroCliente().setIdCliente(cliente);
                service.salvar(objetoSelecionado.getIdSolicitacaoCadastroCliente());
            }
            cliente = clienteService.salvar(cliente);
            ClienteVeiculo cv = new ClienteVeiculo();

            cv.setIdCliente(cliente);
            cv.setIdModelo(objetoSelecionado.getIdModeloInformacao().getIdModelo());
            cv.setAnoFabricacao(objetoSelecionado.getAnoFabricacao());
            cv.setAnoModelo(objetoSelecionado.getIdModeloInformacao().getAno());
            cv.setIdCombustivel(objetoSelecionado.getIdCombustivel());
            cv.setPlaca(objetoSelecionado.getPlaca());
            cv.setIdCorVeiculo(objetoSelecionado.getIdCorVeiculo());
            cv.setRenavam(objetoSelecionado.getRenavam());
            cv.setChassi(objetoSelecionado.getChassi());
            cv.setCambio(objetoSelecionado.getCambio());
            cv.setPortas(objetoSelecionado.getNroPortas());
            cv.setNumeroPassageiros(objetoSelecionado.getNroPassageiros());
            cv.setAtivo("S");
            cliente.getClienteVeiculoList().add(cv);

            clienteService.salvar(cliente);

            createFacesSuccessMessage("Solicitação aceita com sucesso");
            enviaEmailSms("Sua solicitação de adesão ao seguro ABAPAV do veículo " + objetoSelecionado.getIdModeloInformacao().getIdModelo().getIdMarca().getNome() + ", "
                    + objetoSelecionado.getIdModeloInformacao().getIdModelo().getNome() + ", " + objetoSelecionado.getIdModeloInformacao().getAno()
                    + " foi aceita com sucesso. Entre em contato no telefone (31) 9 8239-7105 para maiores esclarecimentos.");
        } catch (Exception ex) {

        }
        return "listaSolicitacaoCadastroCliente.xhtml";
    }

    public String getDescricaoStatus(String status) {
        return EnumStatusSolicitacaoCadastroCliente.retornaEnumSelecionado(status).getDescricao();
    }

    public String getStatusDesc() {
        return EnumStatusSolicitacaoCadastroCliente.retornaEnumSelecionado(objetoSelecionado.getStatus()).getDescricao();
    }

    public Boolean getPodeEditar() {
        return EnumStatusSolicitacaoCadastroCliente.retornaEnumSelecionado(objetoSelecionado.getStatus()) == EnumStatusSolicitacaoCadastroCliente.AGUARDANDO;
    }

    public StreamedContent baixarImagens() {
        try {
            String nome = "imagens solicitação.rar";
            Map<String, byte[]> files = new HashMap<>();
            objetoSelecionado.getIdDocumento().getDocumentoAnexoList()
                    .forEach(da -> files.put(da.getNomeArquivo(), da.readFromFile()));
            return FileUtil.downloadFile(FileUtil.ziparArquivos(files), "application/x-rar-compressed", nome);
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return null;
        }
    }

}
