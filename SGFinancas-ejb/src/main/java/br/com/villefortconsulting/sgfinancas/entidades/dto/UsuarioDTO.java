package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String nome;

    private String cargo;

    private Date dataNascimento;

    private String cpf;

    private String login;

    private String senha;

    private String perfilSigla;

    private String perfilNome;

    private String contaBloqueada;

    private String token;

    private String foto64;

    private String telefone;

    private Boolean podeMudarPrecoUnitario;
   
    private List<VeiculoDTO> veiculoList;

    private Boolean empresaSelecionada;

    private EmpresaDTO dadosEmpresa;

    private Boolean aceitouTermo;

    @Override
    public String toString() {
        return "UsuarioDTO{" + "id=" + id + '}';
    }

}
