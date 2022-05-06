package br.com.villefortconsulting.sgfinancas.controle;

import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoAnexoService;
import br.com.villefortconsulting.sgfinancas.servicos.DocumentoService;
import br.com.villefortconsulting.util.FileUtil;
import java.io.Serializable;
import java.util.Base64;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.primefaces.model.StreamedContent;

@Named
@SessionScoped
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoControle extends BasicControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DocumentoService documentoService;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    public StreamedContent downloadAnexo(Documento documento) {
        DocumentoAnexo anexo = documentoAnexoService.buscarUltimoAnexoDocumento(documento);
        return downloadAnexo(anexo);
    }

    public StreamedContent downloadAnexo(AnexoDTO anexo) {
        try {
            if (anexo != null) {
                return FileUtil.downloadFile(Base64.getDecoder().decode(anexo.getConteudo().split(",")[1]), anexo.getMimeType(), anexo.getNome());
            }
        } catch (FileException e) {
            createFacesErrorMessage(e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            createFacesErrorMessage("Ocorreu um erro ao localizar o arquivo");
        }
        return null;
    }

    public StreamedContent downloadAnexo(DocumentoAnexo anexo) {
        try {
            if (anexo != null) {
                return FileUtil.downloadFile(anexo.readFromFile(), anexo.getContentType(), anexo.getNomeArquivo());
            }
        } catch (FileException e) {
            createFacesErrorMessage(e.getMessage());
        }
        return null;
    }

}
