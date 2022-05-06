package br.com.villefortconsulting.sgfinancas.entidades.dto;

import br.com.villefortconsulting.entity.DtoId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDTO extends DtoId {

    private static final long serialVersionUID = 1L;

    @JsonProperty("fipe_marca")
    private String fipeMarca;

    private String name;

    private String marca;

    private String key;

    @JsonProperty("fipe_name")
    private String fipeName;

    private String tipo;

}
