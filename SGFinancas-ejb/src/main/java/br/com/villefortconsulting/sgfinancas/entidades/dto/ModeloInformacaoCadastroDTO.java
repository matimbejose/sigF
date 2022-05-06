package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModeloInformacaoCadastroDTO {

    @JsonProperty("Marca")
    private String marca;

    @JsonProperty("CodigoFipe")
    private String codigoFipe;

    @JsonProperty("Modelo")
    private String modelo;

    @JsonProperty("AnoModelo")
    private String anoModelo;

    @JsonProperty("Valor")
    private String valor;

    private String tipoVeiculo;

    @JsonProperty("MesReferencia")
    private String mesReferencia;

    @JsonProperty("Combustivel")
    private String combustivel;

}
