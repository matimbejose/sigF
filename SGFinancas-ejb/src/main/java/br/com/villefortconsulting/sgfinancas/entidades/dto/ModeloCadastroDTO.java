package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloCadastroDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String nome;

    private String codigo;

}
