package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "cliente")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteHdDDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @Importavel(nome = "Nome", obrigatorio = true)
    @JsonProperty("nome_da_pf")
    private String nome;

    private String tipoPessoa;

    @Importavel(nome = "CPF/CNPJ", obrigatorio = true)
    @JsonProperty("numero_de_cpf")
    private String cpfCnpj;

    @JsonProperty("data_nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date dataNascimento;

    private String razaoSocial;

    private String inscricaoMunicipal;

    private String temInscricaoEstadual;

    private String inscricaoEstadual;

    private String contribuiIcms;

    private String optaSimples;

    private String status;

}
