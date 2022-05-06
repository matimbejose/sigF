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
@Importavel(nome = "fornecedor")
public class FornecedorDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String tipoPessoa;

    @Importavel(nome = "Razão social", tipo = EnumTipoDadoImportacao.STRING, obrigatorio = true)
    private String razaoSocial;

    @Importavel(nome = "CPF/CNPJ", tipo = EnumTipoDadoImportacao.STRING, obrigatorio = true)
    private String cpfCnpj;

    private String usaInscricaoEstadual;

    @Importavel(nome = "Inscrição estadual", tipo = EnumTipoDadoImportacao.STRING)
    private String inscricaoEstadual;

    @Importavel(nome = "Inscrição municipal", tipo = EnumTipoDadoImportacao.STRING)
    private String inscricaoMunicipal;

    @Importavel(nome = "", tipo = EnumTipoDadoImportacao.OBJECT)
    private ContatoDTO contato;

    @Importavel(nome = "Observação", tipo = EnumTipoDadoImportacao.STRING)
    private String observacao;

    private String ativo;

}
