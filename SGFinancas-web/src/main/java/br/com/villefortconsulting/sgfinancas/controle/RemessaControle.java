package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Empresa;
import br.com.villefortconsulting.sgfinancas.entidades.Remessa;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.RemessaFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.EmpresaService;
import br.com.villefortconsulting.sgfinancas.servicos.FuncaoAjudaService;
import br.com.villefortconsulting.sgfinancas.servicos.RemessaService;
import br.com.villefortconsulting.sgfinancas.util.EnumContentType;
import br.com.villefortconsulting.sgfinancas.util.EnumFuncaoAjuda;
import br.com.villefortconsulting.util.FileUtil;
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
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RemessaControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RemessaService remessaService;

    @EJB
    private EmpresaService empresaService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @EJB
    private FuncaoAjudaService funcaoAjudaService;

    private Remessa remessaSelecionado;

    private List<Boleto> boletosRemessa;

    private Boleto boletoSelecionado;

    private LazyDataModel<Remessa> model;

    private RemessaFiltro filtro = new RemessaFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, remessaService::quantidadeRegistrosFiltrados, remessaService::getListaFiltrada);
    }

    public String mostrarAjuda() {
        createFacesInfoMessage(funcaoAjudaService.getFuncaoAjuda(EnumFuncaoAjuda.CADASTRO_BANCO.getChave()).getDescricao());
        return "cadastroRemessa.xhtml";
    }

    public StreamedContent downloadRemessa() {

        try {

            DocumentoAnexo fatura = documentoAnexoService.buscarUltimoAnexoDocumento(remessaSelecionado.getIdDocumento());

            return FileUtil.downloadFile(fatura.readFromFile(), fatura.getContentType(), fatura.getNomeArquivo() + "." + EnumContentType.retornaExtensao(fatura.getContentType()));
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());

            return null;
        }
    }

    public List<Remessa> getRemessas() {
        return remessaService.listar();
    }

    public void cleanBoletos() {
        boletosRemessa = new LinkedList<>();
    }

    public String gerarRemessa() {
        try {

            if (boletosRemessa == null || boletosRemessa.isEmpty()) {
                createFacesErrorMessage("Selecione ao menos um boleto para gerar arquivo de remessa!");
                return "listaBoleto.xhtml";
            }

            Empresa empresa = empresaService.getEmpresa();

            remessaService.gerarArquivoRemessa(empresa, boletosRemessa, getUsuarioLogado());
            createFacesSuccessMessage("Remessa gerada com sucesso!");
            return "listaBoleto.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "listaBoleto.xhtml";
        } finally {
            cleanBoletos();
        }
    }

    public String doFinishAdd() {

        remessaService.salvar(remessaSelecionado);

        createFacesSuccessMessage("Remessa salvo com sucesso!");
        return "listaRemessa.xhtml";
    }

    public String doFinishExcluir() {
        return "listaRemessa.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return remessaService.getDadosAuditoriaByID(remessaSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaRemessa.xhtml";
    }

}
