package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.Sindico;
import br.com.villefortconsulting.sgfinancas.entidades.SindicoConselho;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.SindicoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.SindicoService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
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
public class SindicoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private SindicoService service;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ClienteService clienteService;

    private Sindico sindicoSelecionada;

    private Cliente conselheiroSelecionado;

    private SindicoConselho sindicoConselhoSelecionado;

    private LazyDataModel<Sindico> model;

    private SindicoFiltro filtro = new SindicoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, service::quantidadeRegistrosFiltrados, service::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_UNIDADE_MEDIDA.getChave()).getDescricao());
        return "cadastroSindico.xhtml";
    }

    @Override
    public void cleanCache() {
        cleanSindicoConselho();
        conselheiroSelecionado = new Cliente();
        sindicoConselhoSelecionado = new SindicoConselho();
    }

    public List<Sindico> getSindicos() {
        return service.listar();
    }

    public boolean existeSindicoAtivo() {
        return service.existeSindicoAtivo();
    }

    public String doStartAdd() {
        cleanCache();
        setSindicoSelecionada(new Sindico());
        return "cadastroSindico.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        sindicoSelecionada.setSindicoConselhoList(service.obterConselheiros(sindicoSelecionada));
        return "cadastroSindico.xhtml";
    }

    public String doFinishAdd() {
        try {
            service.salvar(sindicoSelecionada);

            createFacesSuccessMessage("Síndico salvo com sucesso!");
            return "listaSindico.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroSindico.xhtml";
        }
    }

    public String doFinishExcluir() {
        service.remover(sindicoSelecionada);
        return "listaSindico.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaSindico.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return service.getDadosAuditoriaByID(sindicoSelecionada);
    }

    public List<Cliente> buscarConselheiros(String descricao) {
        List<Cliente> clientesDisponiveis = clienteService.listarPessoa(descricao);
        if (sindicoSelecionada.getSindicoConselhoList() != null && !sindicoSelecionada.getSindicoConselhoList().isEmpty()) {
            clientesDisponiveis.removeAll(sindicoSelecionada.getSindicoConselhoList().stream()
                    .map(SindicoConselho::getIdPessoaConselho)
                    .distinct()
                    .collect(Collectors.toList()));
        }
        return clientesDisponiveis;
    }

    public void addConselheiro() {
        List<Cliente> list = sindicoSelecionada.getSindicoConselhoList().stream()
                .map(SindicoConselho::getIdPessoaConselho)
                .collect(Collectors.toList());
        // Verifica se preencheu o fornecedor
        if (conselheiroSelecionado == null || list.contains(conselheiroSelecionado)) {
            return;
        }

        sindicoConselhoSelecionado = new SindicoConselho();
        sindicoConselhoSelecionado.setIdPessoaConselho(conselheiroSelecionado);
        service.atualizaTenat(sindicoConselhoSelecionado);
        sindicoSelecionada.addConselheiro(sindicoConselhoSelecionado);

        cleanSindicoConselho();
    }

    public void cleanSindicoConselho() {
        sindicoConselhoSelecionado = new SindicoConselho();
    }

    public void removeSindicoConselho() {
        sindicoSelecionada.removeConselheiro(sindicoConselhoSelecionado);
    }

}
