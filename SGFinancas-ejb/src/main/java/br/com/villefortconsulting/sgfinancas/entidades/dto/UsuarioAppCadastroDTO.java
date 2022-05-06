package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioAppCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private String email;

    private String login;

    private String senha;

    private String foto;

    private String telefone;

    @JsonIgnore
    public String getPerfil() {
        return "UA";
    }

}
