package br.com.villefortconsulting.sgfinancas.entidades.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HubDoDesenvolvedorWrapperPessoa implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean status;

    @JsonProperty("return")
    private String retorno;

    private ClienteHdDDTO result;

}
