package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Contrato;
import br.com.villefortconsulting.sgfinancas.entidades.ContratoAjuste;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ContaParcelaDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContratoAjusteFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContratoAjusteService;
import br.com.villefortconsulting.sgfinancas.servicos.ContratoService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoContrato;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.model.LazyDataModel;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoAjusteClienteControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContratoAjusteService contratoAjusteService;

    @EJB
    private ContratoService contratoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private ContratoAjuste contratoAjusteSelecionado;

    private LazyDataModel<ContratoAjuste> model;

    private ContratoAjusteFiltro filtro = new ContratoAjusteFiltro();

    private List<ContaParcelaDTO> listParcelas = new LinkedList<>();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contratoAjusteService::quantidadeRegistrosFiltradosEntrada,
                contratoAjusteService::getListaFiltradaEntrada,
                filter -> filter.setTipoContrato(EnumTipoContrato.CLIENTE.getTipo()));
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.AJUSTE_CONTRATO_CLIENTE.getChave()).getDescricao());
        return "cadastroAjusteContratoCliente.xhtml";
    }

    public String doStartAdd() {
        listParcelas = new LinkedList<>();
        contratoAjusteSelecionado = new ContratoAjuste();
        return "cadastroAjusteContratoCliente.xhtml";
    }

    public String doStartAlterar() {
        preencheListParcela();
        return "cadastroAjusteContratoCliente.xhtml";
    }

    public String doFinishAdd() {

        try {

            contratoAjusteSelecionado.setTipoContrato(EnumTipoContrato.CLIENTE.getTipo());

            contratoAjusteService.salvar(contratoAjusteSelecionado, listParcelas);
            createFacesSuccessMessage("Contrato foi ajustado com sucesso.");
            return "listaContratoAjusteCliente.xhtml";
        } catch (Exception e) {
            createFacesErrorMessage("Erro ao tentar ajustar o contrato.");
            return "cadastroAjusteContratoCliente.xhtml";
        }

    }

    public List<Object> getDadosAuditoria() {
        return contratoAjusteService.getDadosAuditoriaByID(contratoAjusteSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaAjusteContratoCliente.xhtml";
    }

    public void preencheListParcela() {
        listParcelas = contratoAjusteService.listaComTaxaCalculada(contratoAjusteSelecionado);
    }

    public List<Contrato> getListContratoCliente() {
        return contratoService.listarContratoClienteComAtualizacaoAutomatica();
    }

    public String preencheLabelContratoCliente(Contrato contrato) {

        if (!StringUtils.isEmpty(contrato.getObservacao())) {
            return contrato.getIdPlanoConta().getDescricao() + " - " + contrato.getIdCliente().getNome() + " - " + contrato.getObservacao();
        } else {
            return contrato.getIdPlanoConta().getDescricao() + " - " + contrato.getIdCliente().getNome();
        }
    }

}
