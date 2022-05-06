package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloAnosCadastroDTO {

    @JsonProperty("nome")
    private String ano;

    @JsonProperty("codigo")
    private String codigoAno;


}
