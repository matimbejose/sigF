package br.com.villefortconsulting.sgfinancas.entidades;

import br.com.villefortconsulting.entity.RetornoWs;
import br.com.villefortconsulting.sgfinancas.util.EnumStatusEnvio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class StatusEnvio {

    private EnumStatusEnvio status;

    private RetornoWs.Erros erros;

    private String error;

    private String message;

}
