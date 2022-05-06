package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "centro de custo")
public class CentroCustoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String descricao;

    @Importavel(nome = "CEP", obrigatorio = false)
    private String cep;

    @Importavel(nome = "Logradouro", obrigatorio = false)
    private String logradouro;

    @Importavel(nome = "Bairro", obrigatorio = false)
    private String bairro;

    @Importavel(nome = "Numero", obrigatorio = false)
    private String numero;

    @Importavel(nome = "Complemento", obrigatorio = false)
    private String complemento;

    private String cidade;

    private String estado;

    @Importavel(nome = "Responsavel", obrigatorio = false)
    private String responsavel;

    @Importavel(nome = "Telefone", obrigatorio = false)
    private String fone;

    @Importavel(nome = "Celular", obrigatorio = false)
    private String celular;

    @Importavel(nome = "Celular Responsavel", obrigatorio = false)
    private String celularResponsavel;

    @Importavel(nome = "email", obrigatorio = false)
    private String email;

}
