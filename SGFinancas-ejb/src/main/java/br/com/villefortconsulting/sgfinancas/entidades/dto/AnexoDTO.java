package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnexoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private String mimeType;

    private Date dataEnvio;

    private Integer idUsuarioEnvio;

    @JsonIgnore
    private String nomeUsuarioEnvio;

    private String conteudo;

    public AnexoDTO(String conteudo) {
        this.conteudo = conteudo;
    }

}
