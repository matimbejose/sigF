package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.annotations.Importavel;
import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Importavel(nome = "unidade de medida")
public class UnidadeMedidaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @Importavel(nome = "Nome", obrigatorio = true)
    private String descricao;

    @Importavel(nome = "Sigla", obrigatorio = true, observacoes = "Tamanho máximo: 6 caracteres")
    private String sigla;

}
