package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContatoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Email", tipo = EnumTipoDadoImportacao.STRING)
    private String email;

    @Importavel(nome = "Telefone comercial", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneComercial;

    @Importavel(nome = "Telefone residencial", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneResidencial;

    @Importavel(nome = "Telefone celular", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneCelular;

    @Importavel(nome = "CEP", tipo = EnumTipoDadoImportacao.STRING)
    private String cep;

    @Importavel(nome = "Endereço", tipo = EnumTipoDadoImportacao.STRING)
    private String logradouro;

    @Importavel(nome = "Número", tipo = EnumTipoDadoImportacao.INTEGER)
    private Integer numero;

    @Importavel(nome = "Complemento", tipo = EnumTipoDadoImportacao.STRING)
    private String complemento;

    @Importavel(nome = "Bairro", tipo = EnumTipoDadoImportacao.STRING)
    private String bairro;

}
