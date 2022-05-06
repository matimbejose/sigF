package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Conta;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcela;
import br.com.villefortconsulting.sgfinancas.entidades.ContaParcelaParcial;
import br.com.villefortconsulting.sgfinancas.entidades.UnidadeOcupacao;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.ContaParcelaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.ContaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.ParametroCondominioService;
import br.com.villefortconsulting.sgfinancas.servicos.UnidadeService;
import br.com.villefortconsulting.sgfinancas.servicos.exception.ContaException;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.sgfinancas.util.EnumTipoConta;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
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
public class MensalidadeControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private ContaService contaService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    @EJB
    private UnidadeService unidadeService;

    @EJB
    private ParametroCondominioService parametroCondominioService;

    private Conta mensalidadeSelecionada;

    private ContaParcela mensalidadeParcelaSelecionada;

    private ContaParcelaParcial mensalidadeParcelaParcialSelecionada;

    private List<UnidadeOcupacao> unidades = new LinkedList<>();

    private Double valorRecebido;

    private transient Part part;

    private LazyDataModel<ContaParcela> model;

    private ContaParcelaFiltro filtro = new ContaParcelaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(
                filtro,
                contaService::quantidadeParcelasFiltradas,
                contaService::getListaParcelaFiltrada,
                filter -> filter.setTipoConta(EnumTipoConta.MENSALIDADE.getTipo()));
    }

    public String mostrarAjuda() {

        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_CONTA_RECEBER.getChave()).getDescricao());
        return "cadastroConta.xhtml";
    }

    public void deletarAnexo() {
        mensalidadeSelecionada.setIdDocumento(null);
        part = null;
    }

    public boolean informarRecebimento() {
        return mensalidadeSelecionada != null && "S".equals(mensalidadeSelecionada.getInformarPagamento());
    }

    public boolean informarRecebimentoParcial() {
        return mensalidadeSelecionada != null && "S".equals(mensalidadeSelecionada.getInformarPagamento())
                && mensalidadeParcelaSelecionada != null && "S".equals(mensalidadeParcelaSelecionada.getPagamentoParcial());
    }

    public void selecionarDataVencimento() {
        if (mensalidadeSelecionada.getDataVencimento() != null) {

            // Mudar dia vencimento
            Date dataVencimento = parametroCondominioService.alterarDataMensalidade(mensalidadeSelecionada.getDataVencimento());
            mensalidadeSelecionada.setDataVencimento(dataVencimento);

            unidades = unidadeService.listarUnidadeOcupacaoSemMensalidade(dataVencimento);
            if (unidades == null) {
                mensalidadeSelecionada.setDataVencimento(null);
                createFacesErrorMessage("Não existe unidades para gerar mensalidade para competência informada");
            }
        }
    }

    public String doStartAdd() {
        this.part = null;
        this.valorRecebido = null;
        this.unidades = null;
        this.mensalidadeSelecionada = new Conta();
        this.mensalidadeSelecionada.setTipoConta(EnumTipoConta.MENSALIDADE.getTipo());
        this.mensalidadeSelecionada.setRepetirConta("N");
        this.mensalidadeSelecionada.setInformarPagamento("N");

        return "cadastroMensalidade.xhtml";
    }

    public String doStartAlterar() {
        return "cadastroMensalidade.xhtml";
    }

    public String doFinishAdd() {

        try {

            if (mensalidadeSelecionada.getId() != null) {
                contaService.alterarConta(mensalidadeSelecionada);
                createFacesSuccessMessage("Mensalidade salva sucesso!");
                return "listaMensalidade.xhtml";
            }

            if (unidades == null) {
                createFacesErrorMessage("Não existe unidades para gerar mensalidade para competência informada");
                return "cadastroMensalidade.xhtml";
            }

            contaService.salvarMensalidade(mensalidadeSelecionada, unidades);

            return "listaMensalidade.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroMensalidade.xhtml";
        }
    }

    public String doStartAddParcela() {
        Double valorRestante = contaService.valorRestanteSerRecebido(mensalidadeParcelaSelecionada);
        mensalidadeParcelaSelecionada.setValorRestante(valorRestante);

        valorRecebido = valorRestante;
        return "receberMensalidade.xhtml";
    }

    public String doFinishAddParcelaParcial() {

        try {
            contaService.pagarParcela(mensalidadeParcelaSelecionada, valorRecebido);
            createFacesSuccessMessage("Mensalidade paga com sucesso!");
        } catch (ContaException ex) {
            createFacesErrorMessage(ex.getMessage());
        }

        return "listaMensalidade.xhtml";
    }

    public String doShowAuditoria() {
        return "listaAuditoriaMensalidade.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return contaService.getDadosAuditoriaByID(mensalidadeSelecionada);
    }

}
