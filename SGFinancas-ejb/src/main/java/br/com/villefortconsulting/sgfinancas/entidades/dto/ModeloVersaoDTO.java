package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModeloVersaoDTO {

    private String referencia;

    @JsonProperty("fipe_codigo")
    private String fipeCodigo;

    private String name;

    private String combustivel;

    private String marca;

    @JsonProperty("ano_modelo")
    private String anoModelo;

    private String preco;

    private String key;

    private Double time;

    private String veiculo;

    private String id;

}
