package br.com.villefortconsulting.sgfinancas.entidades.dto;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaCadastroDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cpfCnpj;

    private String nomeResponsavel;

    private String emailResponsavel;

    private String senhaResponsavel;

    private boolean aceitouTermo;

    private String telefone;

    private String celular;

    private Date dataNascimento;

    private String cep;

    private String codCidade;

    private String endereco;

    private String numero;

    private String complemento;

    private String bairro;

    private String tipoUso;

    private String ondeConheceu;

    private Integer idCategoriaEmpresa;

}
