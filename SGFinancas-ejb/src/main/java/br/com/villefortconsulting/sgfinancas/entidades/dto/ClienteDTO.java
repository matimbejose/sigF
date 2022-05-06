package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "cliente")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String nome;

    private String tipoPessoa;

    @Importavel(nome = "CPF/CNPJ", obrigatorio = true)
    private String cpfCnpj;

    private Date dataNascimento;

    private String razaoSocial;

    private String inscricaoMunicipal;

    private String temInscricaoEstadual;

    private String inscricaoEstadual;

    private String contribuiIcms;

    private String optaSimples;

    private String status;

    private String ativo;

    private Double valorIR;

    private Double valorPIS;

    private Double valorCSLL;

    private Double valorINSS;

    private Double valorCOFINS;

    private Double valorISS;

    private ContatoDTO contato;

    private Boolean ehSeguradora = false;

    private List<VeiculoDTO> listaVeiculos;

}
