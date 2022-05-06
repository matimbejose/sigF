package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.entity.EntityId;
import br.com.villefortconsulting.sgfinancas.entidades.Banco;
import br.com.villefortconsulting.sgfinancas.entidades.ContaBancaria;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaBancariaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.dto.StatusCadastroDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaBancariaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.BancoService;
import br.com.villefortconsulting.sgfinancas.servicos.ContaBancariaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
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
import org.jrimum.bopepo.BancosSuportados;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContaBancariaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaBancariaService contaBancariaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private BancoService bancoService;

    @Inject
    private CadastroControle cadastroControle;

    private ContaBancaria contaBancariaSelecionado;

    private LazyDataModel<ContaBancaria> model;

    private ContaBancariaFiltro filtro = new ContaBancariaFiltro();

    private ContaBancariaDTO dtoCadastro;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, contaBancariaService::quantidadeRegistrosFiltrados, contaBancariaService::getListaFiltrada);
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CONTA_BANCARIA.getChave()).getDescricao());
        return "/restrito/contaBancaria/cadastroContaBancaria.xhtml";
    }

    public List<ContaBancaria> getContaBancarias() {
        return contaBancariaService.listarAtivas();
    }

    public List<ContaBancaria> listarContasBancarias() {
        return contaBancariaService.listarContasBancarias();
    }

    public List<Banco> getBancos() {
        return bancoService.listar();
    }

    public List<Object> getDadosAuditoria() {
        return contaBancariaService.getDadosAuditoriaByID(contaBancariaSelecionado);
    }

    public boolean ehBancoCaixa() {
        if (contaBancariaSelecionado.getIdBanco() != null) {
            return BancosSuportados.CAIXA_ECONOMICA_FEDERAL.getCodigoDeCompensacao().equals(contaBancariaSelecionado.getIdBanco().getNumero());
        }
        return false;
    }

    public String doStartAdd() {
        cleanCache();
        setContaBancariaSelecionado(new ContaBancaria());
        return "/restrito/contaBancaria/cadastroContaBancaria.xhtml";
    }

    public String doStartAlterar() {
        cleanCache();
        return "/restrito/contaBancaria/cadastroContaBancaria.xhtml";
    }

    public String doFinishAdd() {
        try {
            ContaBancaria contaBancariaCadastrado = contaBancariaService.findByDescricao(contaBancariaSelecionado.getDescricao());
            if (Objects.nonNull(contaBancariaCadastrado)
                    && (!contaBancariaCadastrado.getId().equals(contaBancariaSelecionado.getId()))) {
                createFacesErrorMessage("Existe uma conta bancária cadastrada para a descrição informada.");
                return "/restrito/contaBancaria/cadastroContaBancaria.xhtml";
            }
            contaBancariaService.salvar(contaBancariaSelecionado);
            createFacesSuccessMessage("Conta bancária salva com sucesso!");
            return "/restrito/contaBancaria/listaContaBancaria.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage(e.getMessage());
            return "/restrito/contaBancaria/cadastroContaBancaria.xhtml";
        }
    }

    public String doFinishExcluir() {
        contaBancariaService.remover(contaBancariaSelecionado);
        return "/restrito/contaBancaria/listaContaBancaria.xhtml";
    }

    public String cancelarConta() {
        try {
            contaBancariaService.cancelarConta(contaBancariaSelecionado);
            createFacesSuccessMessage("Conta bancária cancelada com sucesso!");
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
        }
        return "/restrito/contaBancaria/listaContaBancaria.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaContaBancaria.xhtml";
    }

    public void init() {
        if (contaBancariaSelecionado == null) {
            contaBancariaSelecionado = new ContaBancaria();
        }
    }

    @Override
    public StatusCadastroDTO getImportConfig() {
        if (checarPermissao("CONTA_BANCARIA_GERENCIAR")) {
            return new StatusCadastroDTO(
                    "Conta bancária",
                    contaBancariaService.hasAny(),
                    true,
                    this::doStartAdd,
                    ContaBancariaDTO.class);
        }
        return null;
    }

    @Override
    public String mudaSituacaoImportacao() {
        return cadastroControle.doStartImport(getImportConfig());
    }

    @Override
    public void initDtoCadastro(Object context) {
        dtoCadastro = new ContaBancariaDTO();
    }

    @Override
    public List<EntityId> doFinishImport(List<DtoId> obj) {
        return obj.stream()
                .map(conta -> contaBancariaService.importDto((ContaBancariaDTO) conta, getUsuarioLogado().getTenat()))
                .collect(Collectors.toList());
    }

}
