package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.sgfinancas.entidades.Cidade;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredenciamentoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String senha;

    private String confirmacaoSenha;

    private String email;

    private String login;

    private String telefone;

    private Cidade cidade;

    private String nomeEmpresa;

    private String nomeFantasia;

    private String cnpjEmpresa;

    private String regimeTributario;

    private boolean usarPromocao;

}
