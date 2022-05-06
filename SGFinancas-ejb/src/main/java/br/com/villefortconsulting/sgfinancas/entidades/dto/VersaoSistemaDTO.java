package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersaoSistemaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String versao;

    private String descricao;

}
