package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.enums.EnumTipoDadoImportacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "cliente")
public class ClienteCadastroDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Nome", tipo = EnumTipoDadoImportacao.STRING, obrigatorio = true)
    private String nome;

    @Importavel(nome = "Razão Social", tipo = EnumTipoDadoImportacao.STRING)
    private String razaoSocial;

    @Importavel(nome = "CPF/CNPJ", tipo = EnumTipoDadoImportacao.STRING)
    private String cpfCnpj;

    @Importavel(nome = "Telefone comercial", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneComercial;

    @Importavel(nome = "Telefone residencial", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneResidencial;

    @Importavel(nome = "Telefone celular", tipo = EnumTipoDadoImportacao.STRING)
    private String telefoneCelular;

    @Importavel(nome = "Email", tipo = EnumTipoDadoImportacao.STRING)
    private String email;

    @Importavel(nome = "Inscrição municipal", tipo = EnumTipoDadoImportacao.STRING)
    private String inscricaoMunicipal;

    @Importavel(nome = "Inscrição estadual", tipo = EnumTipoDadoImportacao.STRING)
    private String inscricaoEstadual;

    @Importavel(nome = "CEP", tipo = EnumTipoDadoImportacao.STRING)
    private String cep;

    @Importavel(nome = "Endereço", tipo = EnumTipoDadoImportacao.STRING)
    private String endereco;

    @Importavel(nome = "Número", tipo = EnumTipoDadoImportacao.INTEGER)
    private Integer numero;

    @Importavel(nome = "Bairro", tipo = EnumTipoDadoImportacao.STRING)
    private String bairro;

    @Importavel(nome = "Complemento", tipo = EnumTipoDadoImportacao.STRING)
    private String complemento;

    private Boolean ehSeguradora;

    private Double valorIR;

    private Double valorPIS;

    private Double valorCSLL;

    private Double valorINSS;

    private Double valorCOFINS;

    private Double valorISS;

    public String getTelefone() {
        return telefoneComercial;
    }

    public void setTelefone(String telefone) {
        this.telefoneComercial = telefone;
    }

}
