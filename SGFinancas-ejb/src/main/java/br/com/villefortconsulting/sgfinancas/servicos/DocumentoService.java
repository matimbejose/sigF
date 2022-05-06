package br.com.villefortconsulting.sgfinancas.servicos;

import br.com.villefortconsulting.basic.BasicFilter;
import br.com.villefortconsulting.basic.BasicService;
import br.com.villefortconsulting.entity.AdHocTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.sgfinancas.entidades.Documento;
import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import br.com.villefortconsulting.sgfinancas.servicos.repositorios.DocumentoRepositorio;
import br.com.villefortconsulting.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
import org.primefaces.model.UploadedFile;

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DocumentoService extends BasicService<Documento, DocumentoRepositorio, BasicFilter<Documento>> {

    private static final long serialVersionUID = 1L;

    @Inject
    AdHocTenant adHocTenant;

    @EJB
    private DocumentoAnexoService documentoAnexoService;

    @PersistenceContext(unitName = "sgfinancas_villefortconsulting", name = "persistence/sgfinancas_villefortconsulting")
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @PostActivate
    @PostConstruct
    protected void postConstruct() {
        repositorio = new DocumentoRepositorio(em, adHocTenant);
    }

    @Override
    public void remover(Documento doc) {
        doc.getDocumentoAnexoList().stream()
                .forEach(documentoAnexoService::remover);
        super.remover(doc);
    }

    public Documento criarDocumento(Usuario usuarioLogado, String nome) {
        Documento documento = new Documento();
        documento.setDescricao(nome);
        documento.setTenatID(adHocTenant.getTenantID());
        if (documento.getTenatID() == null) {
            documento.setTenatID(usuarioLogado.getTenat());
        }
        return adicionar(documento);
    }

    public Documento criarDocumento(Usuario usuarioLogado, String nome, File... files) throws FileException, IOException {
        Documento documento = criarDocumento(usuarioLogado, nome);
        for (File file : files) {
            documentoAnexoService.criarDocumentoAnexo(documento, usuarioLogado, file.getName(), Files.probeContentType(file.toPath()), FileUtil.convertFileToBytes(file));
        }
        return documento;
    }

    public Documento criarDocumento(Usuario usuarioLogado, String nome, UploadedFile... files) throws FileException {
        Documento documento = criarDocumento(usuarioLogado, nome);
        for (UploadedFile file : files) {
            documentoAnexoService.criarDocumentoAnexo(documento, usuarioLogado, file.getFileName(), file.getContentType(), file.getContents());
        }
        return documento;
    }

    public Documento criarDocumento(Usuario usuarioLogado, String nome, Part... files) throws FileException {
        Documento documento = criarDocumento(usuarioLogado, nome);
        for (Part file : files) {
            documentoAnexoService.criarDocumentoAnexo(documento, usuarioLogado, nome, file.getContentType(), FileUtil.convertPartToBytes(file));
        }
        return documento;
    }

    public Documento criarDocumento(Usuario usuarioLogado, String nome, String contentType, byte[] file) throws FileException {
        Documento documento = criarDocumento(usuarioLogado, nome);
        documentoAnexoService.criarDocumentoAnexo(documento, usuarioLogado, nome, contentType, file);
        return documento;
    }

}
