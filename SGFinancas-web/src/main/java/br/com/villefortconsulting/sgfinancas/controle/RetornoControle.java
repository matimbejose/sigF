package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Retorno;
import br.com.villefortconsulting.sgfinancas.entidades.dto.ConciliacaoDTO;
import br.com.villefortconsulting.sgfinancas.entidades.paginacao.RetornoFiltro;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.sgfinancas.servicos.RetornoService;
import br.com.villefortconsulting.sgfinancas.util.EnumContentType;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.VillefortDataModel;
import java.io.Serializable;
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
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RetornoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private RetornoService retornoService;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    private List<ConciliacaoDTO> listaConciliacaoDTO;

    private Retorno retornoSelecionado;

    private LazyDataModel<Retorno> model;

    private RetornoFiltro filtro = new RetornoFiltro();

    private transient Part part;

    @PostConstruct
    public void postConstruct() {
        model = new VillefortDataModel<>(filtro, retornoService::quantidadeRegistrosFiltrados, retornoService::getListaFiltrada);
    }

    public List<Retorno> getRetornos() {
        return retornoService.listar();
    }

    public String doStartAdd() {
        retornoSelecionado = new Retorno();
        return "cadastroRetorno.xhtml";
    }

    public StreamedContent downloadRetorno() {
        try {

            DocumentoAnexo fatura = documentoAnexoService.buscarUltimoAnexoDocumento(retornoSelecionado.getIdDocumento());

            return FileUtil.downloadFile(fatura.readFromFile(), fatura.getContentType(), fatura.getNomeArquivo() + "." + EnumContentType.retornaExtensao(fatura.getContentType()));
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());

            return null;
        }
    }

    public String uploadArquivo() {
        try {

            retornoService.updloadArquivo(retornoSelecionado, getUsuarioLogado(), part);

            createFacesSuccessMessage("Upload de arquivo de retorno realizado com sucesso! ");
            return "listaRetorno.xhtml";
        } catch (Exception ex) {
            createFacesErrorMessage(ex.getMessage());
            return "cadastroRetorno.xhtml";
        }
    }

    public String doStartConciliacaoBancaria() {
        try {

            listaConciliacaoDTO = retornoService.buscarParcelasRetorno(retornoSelecionado);

            return "listaConciliacaoBancaria.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "listaRetorno.xhtml";
        }

    }

    public String doFinishConciliacaoBancaria() {
        try {

            retornoService.processarRetorno(retornoSelecionado, listaConciliacaoDTO);

            createFacesSuccessMessage("Parcelas baixadas com sucesso!");
            return "listaRetorno.xhtml";
        } catch (Exception ex) {

            createFacesErrorMessage(ex.getMessage());
            return "listaConciliacaoBancaria.xhtml";
        }

    }

    public String doFinishExcluir() {
        retornoService.remover(retornoSelecionado);

        createFacesSuccessMessage("Arquivo apagado com sucesso! ");
        return "listaRetorno.xhtml";
    }

    public List<Object> getDadosAuditoria() {
        return retornoService.getDadosAuditoriaByID(retornoSelecionado);
    }

    public String doShowAuditoria() {
        return "listaAuditoriaRetorno.xhtml";
    }

}
