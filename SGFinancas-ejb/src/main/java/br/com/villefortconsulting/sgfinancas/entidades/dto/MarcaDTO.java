package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarcaDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    private String name;

    @JsonProperty("fipe_name")
    private String fipeName;

    private String order;

    private String key;
    
    private String tipoVeiculo;

}
