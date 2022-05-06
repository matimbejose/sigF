package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import br.com.villefortconsulting.sgfinancas.entidades.interfaces.EntityDescricao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescricaoDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String descricao;

    public DescricaoDTO(EntityDescricao entity) {
        this.id = entity.getId();
        this.descricao = entity.getDescricao();
    }

}
