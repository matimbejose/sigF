package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubDoDesenvolvedorWrapperEmpresa implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean status;

    @JsonProperty("return")
    private String retorno;

    private EmpresaDTO result;

}
