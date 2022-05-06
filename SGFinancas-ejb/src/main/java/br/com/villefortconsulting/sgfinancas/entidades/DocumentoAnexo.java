package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.EntityTenant;
import br.com.villefortconsulting.exception.FileException;
import br.com.villefortconsulting.util.FileUtil;
import br.com.villefortconsulting.util.SystemPreferencesUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

@Audited
@Entity
@Table(name = "DOCUMENTO_ANEXO")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Getter
@Setter
@NoArgsConstructor
@Inheritance
public class DocumentoAnexo extends EntityTenant implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_PATH = SystemPreferencesUtil.getProp("defaults.pathUserFiles");

    @NotNull(message = "Informe o nome do arquivo")
    @Column(name = "NOME_ARQUIVO")
    private String nomeArquivo;

    @NotNull(message = "Informe o content type")
    @Column(name = "CONTENT_TYPE")
    private String contentType;

    @NotNull(message = "Informe a data de envio")
    @Column(name = "DATA_ENVIO")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataEnvio;

    @JoinColumn(name = "ID_DOCUMENTO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Documento idDocumento;

    @NotNull(message = "Informe o usuário que enviou anexo")
    @JoinColumn(name = "ID_USUARIO_ENVIO", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuario idUsuarioEnvio;

    @NotNull
    @Column(name = "ATIVO")
    private String ativo = "S";

    public DocumentoAnexo setNomeArquivo(@NonNull String nome) throws FileException {
        if (nome.equals(nomeArquivo)) {
            return this;
        }
        if (nomeArquivo != null && FileUtil.fileExists(getFullPath()) && !FileUtil.moveFile(getFullPath(), getFileNamePrefix() + nome.replaceAll("[^a-zA-Z\\d \\.]", "_"))) {
            throw new FileException("Não foi possível renomear o arquivo.", null);
        }
        nomeArquivo = nome;
        return this;
    }

    private String getFileNamePrefix() {
        return BASE_PATH + File.separator + id + "_" + tenatID + "_";
    }

    private String getFullPath() {
        return getFileNamePrefix() + nomeArquivo.replaceAll("[^a-zA-Z\\d \\.]", "_");
    }

    public File getFile() {
        return new File(getFullPath());
    }

    public byte[] readFromFile() {
        try {
            return FileUtil.convertFileToBytes(getFile());
        } catch (Exception ex) {
            return new byte[]{};
        }
    }

    public DocumentoAnexo writeToFile(byte[] content) {
        if (id == null || tenatID == null) {
            throw new IllegalStateException("Entity must be persisted before saving a file.");
        }
        if (content == null) {
            ativo = "N";
            return this;
        }
        try {
            FileUtil.convertByteToFile(content, getFullPath());
        } catch (IOException ex) {
            Logger.getLogger(DocumentoAnexo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return this;
    }

    public String getConteudoArquivo64() {
        byte[] content = readFromFile();
        if (content == null) {
            return "";
        }
        return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(content);
    }

    public DocumentoAnexo setConteudoArquivo64(String conteudo64) {
        if (conteudo64 != null) {
            if (conteudo64.startsWith("data:")) {
                contentType = conteudo64.substring(5).split(";")[0];
                writeToFile(Base64.getDecoder().decode(conteudo64.substring(13 + contentType.length())));
                dataEnvio = new Date();
            } else {
                writeToFile(Base64.getDecoder().decode(conteudo64));
            }
        } else {
            writeToFile(null);
        }
        return this;
    }

    @Override
    public String toString() {
        return "DocumentoAnexo{" + "id=" + id + ", tenatID=" + tenatID + '}';
    }

    @PreRemove
    public void onPreRemove() {
        writeToFile(null);
    }

}
