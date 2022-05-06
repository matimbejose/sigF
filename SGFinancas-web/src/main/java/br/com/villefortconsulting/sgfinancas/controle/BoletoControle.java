package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.Boleto;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.BoletoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.BoletoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.util.EnumContentType;
import br.com.villefortconsulting.util.DataUtil;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
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
public class BoletoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private BoletoService boletoService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    private Boleto boletoSelecionado;

    private LazyDataModel<Boleto> model;

    private BoletoFiltro filtro = new BoletoFiltro();

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, boletoService::quantidadeRegistrosFiltrados, boletoService::getListaFiltrada);
        filtro.getData().setMin(DataUtil.getPrimeiroDiaDoAnoCorrente());
        filtro.getData().setMax(DataUtil.getUltimoDiaDoAnoCorrente());
    }

    public List<Boleto> getBoletos() {
        return boletoService.listar();
    }

    public StreamedContent downloadBoleto() {
        try {
            DocumentoAnexo fatura = documentoAnexoService.buscarUltimoAnexoDocumento(boletoSelecionado.getIdDocumento());

            return FileUtil.downloadFile(fatura.readFromFile(), fatura.getContentType(), fatura.getNomeArquivo() + "." + EnumContentType.retornaExtensao(fatura.getContentType()));
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return null;
        }
    }

    public String doFinishExcluir() {
        boletoService.remover(boletoSelecionado);
        return "listaBoleto.xhtml";
    }

    public String doFinishExcluirRemessa() {
        boletoSelecionado.setIdRemessa(null);
        boletoService.alterar(boletoSelecionado);

        createFacesSuccessMessage("Remessa removida com sucesso!");
        return "listaBoleto.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return boletoService.getDadosAuditoriaByID(boletoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaBoleto.xhtml";
    }

}
