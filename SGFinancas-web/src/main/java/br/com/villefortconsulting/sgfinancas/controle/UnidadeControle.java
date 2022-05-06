package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Cliente;
import br.com.villefortconsulting.sgfinancas.entidades.MoradorUnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.Unidade;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.UnidadeFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ClienteService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.CadastroException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumSituacaoUnidade;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoUnidade;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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
public class UnidadeControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UnidadeService unidadeService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private ClienteService clienteService;

    private LazyDataModel<Unidade> model;

    private UnidadeFiltro filtro = new UnidadeFiltro();

    private Unidade unidadeSelecionada;

    private Cliente moradorSelecionado;

    private UnidadeOcupacao unidadeOcupacao;

    private MoradorUnidadeOcupacao moradorUnidadeOcupacaoSelecionado;

    private MoradorUnidadeOcupacao responsavel;

    private Boolean informaDataSaida;

    private Boolean editarMoradores;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, unidadeService::quantidadeRegistrosFiltrados, unidadeService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroUnidade.xhtml";
    }

    @Override
    public void cleanCache() {
        informaDataSaida = false;
        editarMoradores = false;
    }

    public String doStartAdd() {
        cleanCache();
        unidadeSelecionada = new Unidade();
        unidadeOcupacao = new UnidadeOcupacao();
        unidadeOcupacao.setMoradorUnidadeOcupacaoList(new ArrayList<>());
        unidadeSelecionada.setIdBloco(filtro.getBloco());
        moradorSelecionado = new Cliente();

        return "cadastroUnidade.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        carregarMoradores();
        return "cadastroUnidade.xhtml";
    }

    public void carregarMoradores() {
        unidadeOcupacao = unidadeService.buscarUnidadeOcupacao(unidadeSelecionada);
        unidadeOcupacao.setMoradorUnidadeOcupacaoList(unidadeService.buscarListaMoradores(unidadeOcupacao));
        responsavel = unidadeService.buscarMoradorResponsavel(unidadeOcupacao);
        unidadeSelecionada.setUnidadeOcupacaoList(new LinkedList<>());
        unidadeSelecionada.getUnidadeOcupacaoList().add(unidadeOcupacao);
    }

    public void informarDataSaida() {
        unidadeOcupacao = new UnidadeOcupacao();
        informaDataSaida = false;
    }

    public void editarMoradores() {
        editarMoradores = false;
    }

    public void informarResponsavel() {
        moradorUnidadeOcupacaoSelecionado.setResponsavel("S");
        unidadeOcupacao.getMoradorUnidadeOcupacaoList().stream()
                .filter(m -> !m.equals(moradorUnidadeOcupacaoSelecionado))
                .forEach(m -> m.setResponsavel("N"));
    }

    public List<Cliente> buscarMoradores(String descricao) {
        List<Cliente> clientesDisponiveis = clienteService.listarPessoa(descricao);
        if (unidadeOcupacao.getMoradorUnidadeOcupacaoList() != null && !unidadeOcupacao.getMoradorUnidadeOcupacaoList().isEmpty()) {
            List<Cliente> clientesAtribuidos = unidadeOcupacao.getMoradorUnidadeOcupacaoList().stream().map(MoradorUnidadeOcupacao::getIdMorador).distinct().collect(Collectors.toList());
            clientesDisponiveis.removeAll(clientesAtribuidos);
        }

        return clientesDisponiveis;
    }

    public void removerMorador() {
        unidadeOcupacao.removerMorador(moradorUnidadeOcupacaoSelecionado);
        if (moradorUnidadeOcupacaoSelecionado.getResponsavel().equals("S") && !unidadeOcupacao.getMoradorUnidadeOcupacaoList().isEmpty()) {
            unidadeOcupacao.getMoradorUnidadeOcupacaoList().get(0).setResponsavel("S");
        }
        moradorUnidadeOcupacaoSelecionado = new MoradorUnidadeOcupacao();
    }

    public void addMorador() {
        if (unidadeOcupacao.getMoradorUnidadeOcupacaoList().stream().noneMatch(morador -> morador.getIdMorador().equals(moradorSelecionado))) {
            MoradorUnidadeOcupacao moradorUnidadeOcupacao = new MoradorUnidadeOcupacao();
            moradorUnidadeOcupacao.setIdMorador(moradorSelecionado);

            if (unidadeOcupacao.getMoradorUnidadeOcupacaoList().isEmpty()) {
                moradorUnidadeOcupacao.setResponsavel("S");
            } else {
                moradorUnidadeOcupacao.setResponsavel("N");
            }
            unidadeOcupacao.addMorador(moradorUnidadeOcupacao);

            // stribuindo tenant ao morador unidade ocupacao
            unidadeService.addTenantMoradorUnidadeOcupacao(unidadeOcupacao.getMoradorUnidadeOcupacaoList());

            moradorSelecionado = new Cliente();
        }
    }

    public String doFinishAdd() {
        try {
            unidadeService.salvar(unidadeSelecionada, unidadeOcupacao);
            createFacesSuccessMessage("Unidade salva com sucesso!");
            return "listaUnidade.xhtml";
        } catch (CadastroException ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroUnidade.xhtml";
        }
    }

    public String doFinishExcluir() {
        unidadeService.remover(unidadeSelecionada);
        return "listaUnidade.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return unidadeService.getDadosAuditoriaByID(unidadeSelecionada);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaUnidade.xhtml";
    }

    public String listarUnidades() {
        return "listaUnidade.xhtml";
    }

    public String buscarTipoUnidade(String tipo) {
        return EnumTipoUnidade.retornaEnumSelecionado(tipo).getObservacao();
    }

    public List<EnumTipoUnidade> getTiposUnidade() {
        return Arrays.asList(EnumTipoUnidade.values());
    }

    public String buscarSituacaoUnidade(String situacao) {
        return EnumSituacaoUnidade.retornaEnumSelecionado(situacao).getObservacao();
    }

    public List<EnumSituacaoUnidade> getSituacoesUnidade() {
        return Arrays.asList(EnumSituacaoUnidade.values());
    }

}
