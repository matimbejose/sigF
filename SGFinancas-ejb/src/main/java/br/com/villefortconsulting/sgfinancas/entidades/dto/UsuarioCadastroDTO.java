package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UsuarioCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private String email;

    private String cpf;

    private String login;

    private String senha;

    private String perfil;

    private String contaBloqueada;

    private String token;

    private Integer funcionario;

    private String foto;

    private String telefone;

    public UsuarioCadastroDTO(String nome, String email, String senha, String perfil) {
        this.nome = nome;
        this.email = email;
        this.cpf = "";
        this.login = email;
        this.senha = senha;
        this.perfil = perfil;
        this.contaBloqueada = "N";
    }

}
