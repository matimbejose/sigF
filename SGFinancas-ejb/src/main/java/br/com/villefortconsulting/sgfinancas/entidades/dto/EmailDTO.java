package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String assunto;

    private String tituloMensagem;

    private String mensagem;

    private String remetente;

    private List<Usuario> destinatarios = new ArrayList<>();

    private List<Usuario> destinatariosOcultos = new ArrayList<>();

    private List<File> anexos = new ArrayList<>();

    @JsonIgnore
    private Map<String, File> embededFiles = new HashMap<>();

    @JsonIgnore
    public Map<String, File> getEmbededFiles() {
        return embededFiles;
    }

    public String attachFile(File arquivo, HtmlEmail htmlEmail) throws EmailException {
        String name = htmlEmail.embed(arquivo);
        embededFiles.put(name, arquivo);
        return name;
    }

    public String attachFile(File arquivo, String name) {
        embededFiles.put(name, arquivo);
        return name;
    }

}
