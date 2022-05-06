package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.DocumentoAnexo;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.entidades.dto.AnexoDTO;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.DocumentoAnexoRepositorio;
import br.com.villefortconsulting.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.PostActivate;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.Part;
import lombok.NonNull;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoAnexoService extends BasicService<DocumentoAnexo, DocumentoAnexoRepositorio, BasicFilter<DocumentoAnexo>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private DocumentoService documentoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new DocumentoAnexoRepositorio(em, adHocTenant);
    }

    public List<DocumentoAnexo> listByDocumento(Documento documento) {
        return repositorio.listByDocumento(documento);
    }

    public DocumentoAnexo buscarUltimoAnexoDocumento(Documento documento) {
        return repositorio.buscarUltimoAnexoDocumento(documento);
    }

    public DocumentoAnexo adicionarAnexo(Documento documento, @NonNull Usuario usuario, File file) throws FileException, IOException {
        return criarDocumentoAnexo(documento, usuario, file.getName(), Files.probeContentType(file.toPath()), FileUtil.convertFileToBytes(file));
    }

    public DocumentoAnexo adicionarAnexo(Documento documento, @NonNull Usuario usuario, UploadedFile file) throws FileException {
        return criarDocumentoAnexo(documento, usuario, file.getFileName(), file.getContentType(), file.getContents());
    }

    public DocumentoAnexo adicionarAnexo(Documento documento, @NonNull Usuario usuario, Part file) throws FileException {
        return criarDocumentoAnexo(documento, usuario, file.getName(), file.getContentType(), FileUtil.convertPartToBytes(file));
    }

    public DocumentoAnexo criarDocumentoAnexo(Documento documento, @NonNull Usuario usuario, FileUploadEvent file) throws FileException {
        return adicionarAnexo(documento, usuario, file.getFile());
    }

    public DocumentoAnexo criarDocumentoAnexo(Documento documento, @NonNull Usuario usuario, String name, String contentType, byte[] content) throws FileException {
        return criarDocumentoAnexo(documento, usuario, name, contentType, content, new Date());
    }

    public DocumentoAnexo criarDocumentoAnexo(Documento documento, @NonNull Usuario usuario, String name, String contentType, byte[] content, Date date) throws FileException {
        if (documento == null) {
            documento = documentoService.criarDocumento(usuario, name);
        }
        DocumentoAnexo documentoAnexo = new DocumentoAnexo();

        documentoAnexo.setNomeArquivo(name);
        documentoAnexo.setContentType(contentType);
        documentoAnexo.setDataEnvio(date);
        documentoAnexo.setIdUsuarioEnvio(usuario);
        documentoAnexo.setAtivo("S");
        documentoAnexo.setTenatID(documento.getTenatID());

        documento.getDocumentoAnexoList().add(salvar(documentoAnexo));
        documentoAnexo.setIdDocumento(documento);
        return documentoAnexo.writeToFile(content);
    }

    public Documento persistirAnexoDTO(@NonNull Documento doc, @NonNull Usuario usuario, @NonNull List<AnexoDTO> listaAnexos) {
        doc.getDocumentoAnexoList().stream()
                .filter(da -> da.getId() != null && ("N".equals(da.getAtivo()) || listaAnexos.stream().noneMatch(anexo -> da.getId().equals(anexo.getId()))))
                .forEach(da -> {
                    da.setAtivo("N");
                    remover(da);
                });
        doc.updateList();
        doc.getDocumentoAnexoList().addAll(listaAnexos.stream()
                .filter(anexo -> anexo.getId() == null || doc.getDocumentoAnexoList().stream().noneMatch(da -> anexo.getId().equals(da.getId())))
                .map(anexo -> {
                    if (anexo.getDataEnvio() == null) {
                        anexo.setDataEnvio(new Date());
                    }
                    try {
                        String mimeType = anexo.getMimeType();
                        if (mimeType == null || mimeType.isEmpty()) {
                            mimeType = "text/plain";
                            if (anexo.getConteudo() != null) {
                                String[] partes = anexo.getConteudo().split(";")[0].split(":");
                                if (partes.length > 1) {
                                    mimeType = partes[1];
                                }
                            }
                        }
                        return criarDocumentoAnexo(doc, usuario, anexo.getNome(), mimeType,
                                Base64.getDecoder().decode(anexo.getConteudo().split(",")[1]), anexo.getDataEnvio());
                    } catch (FileException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList()));
        final Documento docFinal = documentoService.salvar(doc);
        doc.getDocumentoAnexoList().stream()
                .forEach(da -> da.setIdDocumento(docFinal));
        return docFinal;
    }

}
