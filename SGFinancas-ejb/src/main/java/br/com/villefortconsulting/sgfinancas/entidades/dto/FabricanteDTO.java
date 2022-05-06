package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FabricanteDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    private String ativo;

}
