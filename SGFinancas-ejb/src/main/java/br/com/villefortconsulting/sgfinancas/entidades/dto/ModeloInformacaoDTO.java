package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloInformacaoDTO extends DtoId {

    private Integer ano;

    private Double preco;

    private String fipeCodigo;

    private String tipo;

    private String veiculo;

}
